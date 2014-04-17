/*****************************************************************************
 * LibVLC.java
 *****************************************************************************
 * Copyright © 2010-2012 VLC authors and VideoLAN
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package org.videolan.vlc;

import java.util.ArrayList;
import java.util.Map;

import org.videolan.vlc.gui.video.VideoPlayerActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;

import com.iconverge.ct.traffic.TrafficApplication;

public class LibVLC {
    private static final String TAG = "VLC/LibVLC";
    private static final int AOUT_AUDIOTRACK_JAVA = 0;
    private static final int AOUT_AUDIOTRACK = 1;
    private static final int AOUT_OPENSLES = 2;

    private static LibVLC sInstance;

    /** libVLC instance C pointer */
    private long mLibVlcInstance = 0; // Read-only, reserved for JNI
    /** libvlc_media_list_player pointer */
    private long mMediaListPlayerInstance = 0; // Read-only, reserved for JNI
    private long mInternalMediaPlayerInstance = 0; // Read-only, reserved for JNI
    /** libvlc_media_list_t pointer */
    private long mMediaListInstance = 0; // Read-only, reserved for JNI

    private Aout mAout;

    /** Keep screen bright */
    //private WakeLock mWakeLock;

    /** Check in libVLC already initialized otherwise crash */
    private boolean mIsInitialized = false;
    public native void attachSurface(Surface surface, VideoPlayerActivity player, int width, int height);

    public native void detachSurface();

    /* Load library before object instantiation */
    static {
        try {
            if (Build.VERSION.SDK_INT <= 10)
                System.loadLibrary("iomx-gingerbread");
            else if (Build.VERSION.SDK_INT <= 13)
                System.loadLibrary("iomx-hc");
            else
                System.loadLibrary("iomx-ics");
        } catch (Throwable t) {
            Log.w(TAG, "Unable to load the iomx library: " + t);
        }
        try {
            System.loadLibrary("vlcjni");
        } catch (UnsatisfiedLinkError ule) {
            Log.e(TAG, "Can't load vlcjni library: " + ule);
            /// FIXME Alert user
            System.exit(1);
        } catch (SecurityException se) {
            Log.e(TAG, "Encountered a security issue when loading vlcjni library: " + se);
            /// FIXME Alert user
            System.exit(1);
        }
    }

    /**
     * Singleton constructor of libVLC Without surface and vout to create the
     * thumbnail and get information e.g. on the MediaLibraryActivity
     *
     * @return libVLC instance
     * @throws LibVlcException
     */
    public static LibVLC getInstance() throws LibVlcException {
        synchronized (LibVLC.class) {
            if (sInstance == null) {
                /* First call */
                sInstance = new LibVLC();
                sInstance.init();
            }
        }

        return sInstance;
    }

    /**
     * Return an existing instance of libVLC Call it when it is NOT important
     * that this fails
     *
     * @return libVLC instance OR null
     */
    public static LibVLC getExistingInstance() {
        synchronized (LibVLC.class) {
            return sInstance;
        }
    }

    /**
     * Constructor
     * It is private because this class is a singleton.
     */
    private LibVLC() {
        mAout = new Aout();
    }

    /**
     * Destructor:
     * It is bad practice to rely on them, so please don't forget to call
     * destroy() before exiting.
     */
    @Override
    public void finalize() {
        if (mLibVlcInstance != 0) {
            Log.d(TAG, "LibVLC is was destroyed yet before finalize()");
            destroy();
        }
    }

    /**
     * Give to LibVLC the surface to draw the video.
     * @param f the surface to draw
     */
    public native void setSurface(Surface f);

    public static synchronized void restart() {
        if (sInstance != null) {
            try {
                sInstance.destroy();
                sInstance.init();
            } catch (LibVlcException lve) {
                Log.e(TAG, "Unable to reinit libvlc: " + lve);
            }
        }
    }

    /**
     * those are called from native code to get settings values
     */
    public boolean useIOMX() {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(TrafficApplication.getAppContext());
        return  p.getBoolean("enable_iomx", false);
    }
    public String getSubtitlesEncoding() {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(TrafficApplication.getAppContext());
        return p.getString("subtitles_text_encoding", "");
    }
    public int getAout() {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(TrafficApplication.getAppContext());
        int defaultAout = Util.isGingerbreadOrLater() ? AOUT_OPENSLES : AOUT_AUDIOTRACK_JAVA;
        int aout = defaultAout;
        try {
            aout = Integer.parseInt(p.getString("aout", String.valueOf(defaultAout)));
        }
        catch (NumberFormatException nfe) {
            aout = defaultAout;
        }
        return aout;
    }
    public boolean timeStretchingEnabled() {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(TrafficApplication.getAppContext());
        return p.getBoolean("enable_time_stretching_audio", false);
    }
    public String getChroma() {
        final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(TrafficApplication.getAppContext());
        String chroma = p.getString("chroma_format", "");
        return chroma.equals("YV12") && !Util.isGingerbreadOrLater() ? "" : chroma;
    }

    /**
     * Initialize the libVLC class
     */
    private void init() throws LibVlcException {
        Log.v(TAG, "Initializing LibVLC");
        if (!mIsInitialized) {
            if(!Util.hasCompatibleCPU()) {
                Log.e(TAG, Util.getErrorMsg());
                throw new LibVlcException();
            }
            Context context = TrafficApplication.getAppContext();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            nativeInit(pref.getBoolean("enable_verbose_mode", true));
            setEventManager(EventManager.getInstance());
            mIsInitialized = true;
        }
    }

    /**
     * Destroy this libVLC instance
     * @note You must call it before exiting
     */
    public void destroy() {
        Log.v(TAG, "Destroying LibVLC instance");
        nativeDestroy();
        detachEventManager();
        mIsInitialized = false;
    }

    /**
     * Open the Java audio output.
     * This function is called by the native code
     */
    public void initAout(int sampleRateInHz, int channels, int samples) {
        Log.d(TAG, "Opening the java audio output");
        mAout.init(sampleRateInHz, channels, samples);
    }

    /**
     * Play an audio buffer taken from the native code
     * This function is called by the native code
     */
    public void playAudio(byte[] audioData, int bufferSize) {
        mAout.playBuffer(audioData, bufferSize);
    }

    /**
     * Pause the Java audio output
     * This function is called by the native code
     */
    public void pauseAout() {
        Log.d(TAG, "Pausing the java audio output");
        mAout.pause();
    }

    /**
     * Close the Java audio output
     * This function is called by the native code
     */
    public void closeAout() {
        Log.d(TAG, "Closing the java audio output");
        mAout.release();
    }

    public void readMedia(String mrl) {
        readMedia(mLibVlcInstance, mrl, false);
    }

    /**
     * Read a media.
     */
    public int readMedia(String mrl, boolean novideo) {
        Log.v(TAG, "Reading " + mrl);
        return readMedia(mLibVlcInstance, mrl, novideo);
    }

    /**
     * Play a media from the media list (playlist)
     *
     * @param position The index of the media
     */
    public void playIndex(int position) {
        playIndex(mLibVlcInstance, position);
    }

    public String[] readMediaMeta(String mrl) {
        return readMediaMeta(mLibVlcInstance, mrl);
    }

    public TrackInfo[] readTracksInfo(String mrl) {
        return readTracksInfo(mLibVlcInstance, mrl);
    }

    /**
     * Get a media thumbnail.
     */
    public byte[] getThumbnail(String mrl, int i_width, int i_height) {
        return getThumbnail(mLibVlcInstance, mrl, i_width, i_height);
    }

    /**
     * Return true if there is a video track in the file
     */
    public boolean hasVideoTrack(String mrl) throws java.io.IOException {
        return hasVideoTrack(mLibVlcInstance, mrl);
    }

    /**
     * Return the length of the stream, in milliseconds
     */
    public long getLengthFromLocation(String mrl) {
        return getLengthFromLocation(mLibVlcInstance, mrl);
    }

    /**
     * Sets the speed of playback (1 being normal speed, 2 being twice as fast)
     *
     * @param rate
     */
    public native void setRate(float rate);

    /**
     * Get the current playback speed
     */
    public native float getRate();

    /**
     * Change the verbosity of libvlc
     * @param verbose: true for increased verbosity
     */
    public native void changeVerbosity(boolean verbose);

    /**
     * Initialize the libvlc C library
     * @return a pointer to the libvlc instance
     */
    private native void nativeInit(boolean verbose) throws LibVlcException;

    /**
     * Close the libvlc C library
     * @note mLibVlcInstance should be 0 after a call to destroy()
     */
    private native void nativeDestroy();

    /**
     * Read a media
     * @param instance: the instance of libVLC
     * @param mrl: the media mrl
     * @param novideo: don't enable video decoding for this media
     * @return the position in the playlist
     */
    private native int readMedia(long instance, String mrl, boolean novideo);

    /**
     * Play an index in the native media list (playlist)
     */
    private native void playIndex(long instance, int position);

    /**
     * Return true if there is currently a running media player.
     */
    public native boolean hasMediaPlayer();

    /**
     * Returns true if any media is playing
     */
    public native boolean isPlaying();

    /**
     * Returns true if any media is seekable
     */
    public native boolean isSeekable();

    /**
     * Plays any loaded media
     */
    public native void play();

    /**
     * Pauses any playing media
     */
    public native void pause();

    /**
     * Stops any playing media
     */
    public native void stop();

    /**
     * Play the previous media (if any) in the media list
     */
    public native void previous();

    /**
     * Play the next media (if any) in the media list
     */
    public native void next();

    /**
     * Gets volume as integer
     */
    public native int getVolume();

    /**
     * Sets volume as integer
     * @param volume: Volume level passed as integer
     */
    public native int setVolume(int volume);

    /**
     * Gets the current movie time (in ms).
     * @return the movie time (in ms), or -1 if there is no media.
     */
    public native long getTime();

    /**
     * Sets the movie time (in ms), if any media is being played.
     * @param time: Time in ms.
     * @return the movie time (in ms), or -1 if there is no media.
     */
    public native long setTime(long time);

    /**
     * Gets the movie position.
     * @return the movie position, or -1 for any error.
     */
    public native float getPosition();

    /**
     * Sets the movie position.
     * @param pos: movie position.
     */
    public native void setPosition(float pos);

    /**
     * Gets current movie's length in ms.
     * @return the movie length (in ms), or -1 if there is no media.
     */
    public native long getLength();

    /**
     * Get the libVLC version
     * @return the libVLC version string
     */
    public native String version();

    /**
     * Get the libVLC compiler
     * @return the libVLC compiler string
     */
    public native String compiler();

    /**
     * Get the libVLC changeset
     * @return the libVLC changeset string
     */
    public native String changeset();

    /**
     * Get a media thumbnail.
     * @return a bytearray with the RGBA thumbnail data inside.
     */
    private native byte[] getThumbnail(long instance, String mrl, int i_width, int i_height);

    /**
     * Return true if there is a video track in the file
     */
    private native boolean hasVideoTrack(long instance, String mrl);

    private native String[] readMediaMeta(long instance, String mrl);

    private native TrackInfo[] readTracksInfo(long instance, String mrl);

    public native TrackInfo[] readTracksInfoPosition(int position);

    public native int getAudioTracksCount();

    public native Map<Integer,String> getAudioTrackDescription();

    public native int getAudioTrack();

    public native int setAudioTrack(int index);

    public native int getVideoTracksCount();

    public native String[] getSpuTrackDescription();

    public native int getSpuTrack();

    public native int setSpuTrack(int index);

    public native int getSpuTracksCount();

    public static native String nativeToURI(String path);

    public static native void nativeReadDirectory(String path, ArrayList<String> res);

    public native static boolean nativeIsPathDirectory(String path);

    /**
     * Get the list of existing items in the media list (playlist)
     */
    public native void getMediaListItems(ArrayList<String> arl);

    /**
     * Return the length of the stream, in milliseconds
     */
    private native long getLengthFromLocation(long instance, String mrl);

    private native void setEventManager(EventManager eventManager);

    private native void detachEventManager();
}
