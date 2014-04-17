/*****************************************************************************
 * VideoPlayerActivity.java
 *****************************************************************************
 * Copyright © 2011-2012 VLC authors and VideoLAN
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

package org.videolan.vlc.gui.video;

import java.lang.reflect.Method;

import org.videolan.vlc.DatabaseManager;
import org.videolan.vlc.EventManager;
import org.videolan.vlc.LibVLC;
import org.videolan.vlc.LibVlcException;
import org.videolan.vlc.Media;
import org.videolan.vlc.Util;
import org.videolan.vlc.WeakHandler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iconverge.ct.traffic.R;
import com.iconverge.ct.traffic.TrafficApplication;

public class VideoPlayerActivity extends Activity {

    public final static String TAG = "VLC/VideoPlayerActivity";

    private ProgressBar progressBar;
    private SurfaceView mSurface;
    private SurfaceHolder mSurfaceHolder;
    private FrameLayout mSurfaceFrame;
    private int mSurfaceAlign;
    private LibVLC mLibVLC;
    private String mLocation;

    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FIT_HORIZONTAL = 1;
    private static final int SURFACE_FIT_VERTICAL = 2;
    private static final int SURFACE_FILL = 3;
    private static final int SURFACE_16_9 = 4;
    private static final int SURFACE_4_3 = 5;
    private static final int SURFACE_ORIGINAL = 6;
    private int mCurrentSize = SURFACE_BEST_FIT;

    /** Overlay */
    private View mOverlayHeader;
    private static final int OVERLAY_TIMEOUT = 4000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int SURFACE_SIZE = 3;
    private static final int FADE_OUT_INFO = 4;
    private boolean mDragging;
    private boolean mShowing;
    private int mUiVisibility = -1;
    private TextView mTitle;
    private TextView mSysTime;
    private TextView mBattery;
    private int mScreenOrientation;
    private boolean mIsLocked = false;
    private int mLastAudioTrack = -1;
    private int mLastSpuTrack = -1;

    /**
     * For uninterrupted switching between audio and video mode
     */
    private boolean mSwitchingView;
    private boolean mEndReached;

    // Playlist
    private int savedIndexPosition = -1;

    // size of the video
    private int mVideoHeight;
    private int mVideoWidth;
    private int mSarNum;
    private int mSarDen;

    @Override
    @TargetApi(11)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(Util.isICSOrLater())
            getWindow().getDecorView().findViewById(android.R.id.content).setOnSystemUiVisibilityChangeListener(
                    new OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if (visibility == mUiVisibility)
                                return;
                            setSurfaceSize(mVideoWidth, mVideoHeight, mSarNum, mSarDen);
                            if (visibility == View.SYSTEM_UI_FLAG_VISIBLE && !mShowing) {
                                showOverlay();
                            }
                            mUiVisibility = visibility;
                        }
                    }
            );

        /** initialize Views an their Events */
        mOverlayHeader = findViewById(R.id.player_overlay_header);

        /* header */
        mTitle = (TextView) findViewById(R.id.player_overlay_title);
        mSysTime = (TextView) findViewById(R.id.player_overlay_systime);
        mBattery = (TextView) findViewById(R.id.player_overlay_battery);

        mScreenOrientation = Integer.valueOf(
                pref.getString("screen_orientation_value", "4" /*SCREEN_ORIENTATION_SENSOR*/));


        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mSurface = (SurfaceView) findViewById(R.id.player_surface);
        mSurfaceHolder = mSurface.getHolder();
        mSurfaceFrame = (FrameLayout) findViewById(R.id.player_surface_frame);
        int pitch;
        if(Util.isGingerbreadOrLater() && pref.getBoolean("enable_yv12_format", false)) {
            mSurfaceHolder.setFormat(ImageFormat.YV12);
            pitch = ImageFormat.getBitsPerPixel(ImageFormat.YV12) / 8;
        } else {
            mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
            PixelFormat info = new PixelFormat();
            PixelFormat.getPixelFormatInfo(PixelFormat.RGBX_8888, info);
            pitch = info.bytesPerPixel;
        }
        mSurfaceAlign = 16 / pitch - 1;
        mSurfaceHolder.addCallback(mSurfaceCallback);

//        mSeekbar = (SeekBar) findViewById(R.id.player_overlay_seekbar);
//        mSeekbar.setOnSeekBarChangeListener(mSeekListener);

        mSwitchingView = false;
        mEndReached = false;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(TrafficApplication.SLEEP_INTENT);
        registerReceiver(mReceiver, filter);

        try {
            mLibVLC = LibVLC.getInstance();
        } catch (LibVlcException e) {
        	//#debug debug
            Log.d(TAG, "LibVLC initialisation failed");
            return;
        }

        EventManager em = EventManager.getInstance();
        em.addHandler(eventHandler);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // 100 is the value for screen_orientation_start_lock
        setRequestedOrientation(mScreenOrientation != 100
                ? mScreenOrientation
                : getScreenOrientation());
    }

    @Override
    protected void onStart() {
        super.onStart();
        showOverlay();
        mSwitchingView = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        long time = mLibVLC.getTime();
        long length = mLibVLC.getLength();
        //remove saved position if in the last 5 seconds
        if (length - time < 5000)
            time = 0;
        else
            time -= 5000; // go back 5 seconds, to compensate loading time

        /*
         * Pausing here generates errors because the vout is constantly
         * trying to refresh itself every 80ms while the surface is not
         * accessible anymore.
         * To workaround that, we keep the last known position in the playlist
         * in savedIndexPosition to be able to restore it during onResume().
         */
        if (savedIndexPosition >= 0)
            mLibVLC.stop();
        else {
            /* FIXME when the playback is started externally from AudioService
             * we don't have a savedIndexPosition. Use pause as a fallback until
             * we find a solution.
             */
            mLibVLC.pause();
        }

        mSurface.setKeepScreenOn(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (mLibVLC != null && !mSwitchingView) {
            mLibVLC.stop();
        }

        EventManager em = EventManager.getInstance();
        em.removeHandler(eventHandler);

    }

    @Override
    protected void onResume() {
        super.onResume();

//        load();
        startPlay();

        /*
         * if the activity has been paused by pressing the power button,
         * pressing it again will show the lock screen.
         * But onResume will also be called, even if vlc-android is still in the background.
         * To workaround that, pause playback if the lockscreen is displayed
         */
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLibVLC != null && mLibVLC.isPlaying()) {
                    KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
                    if (km.inKeyguardRestrictedInputMode())
            mLibVLC.pause();
                }
            }}, 500);

        showOverlay();
    }

    public static void start(Context context, String location) {
        start(context, location, null, false, false);
    }

    public static void start(Context context, String location, Boolean fromStart) {
        start(context, location, null, false, fromStart);
    }

    public static void start(Context context, String location, String title, Boolean dontParse) {
        start(context, location, title, dontParse, false);
    }

    public static void start(Context context, String location, String title, Boolean dontParse, Boolean fromStart) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("itemLocation", location);
        intent.putExtra("itemTitle", title);
        intent.putExtra("dontParse", dontParse);
        intent.putExtra("fromStart", fromStart);

        if (dontParse)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        else {
            // Stop the currently running audio
        }

        context.startActivity(intent);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)) {
                int batteryLevel = intent.getIntExtra("level", 0);
                if (batteryLevel >= 50)
                    mBattery.setTextColor(Color.GREEN);
                else if (batteryLevel >= 30)
                    mBattery.setTextColor(Color.YELLOW);
                else
                    mBattery.setTextColor(Color.RED);
                mBattery.setText(String.format("%d%%", batteryLevel));
            }
            else if (action.equalsIgnoreCase(TrafficApplication.SLEEP_INTENT)) {
                finish();
            }
        }
    };

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        showOverlay();
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setSurfaceSize(mVideoWidth, mVideoHeight, mSarNum, mSarDen);
        super.onConfigurationChanged(newConfig);
    }

    public void setSurfaceSize(int width, int height, int sar_num, int sar_den) {
        if (width * height == 0)
            return;

        // store video size
        mVideoHeight = height;
        mVideoWidth = width;
        mSarNum = sar_num;
        mSarDen = sar_den;
        Message msg = mHandler.obtainMessage(SURFACE_SIZE);
        mHandler.sendMessage(msg);
    }

    /**
     *  Handle libvlc asynchronous events
     */
    private final Handler eventHandler = new VideoPlayerEventHandler(this);

    private static class VideoPlayerEventHandler extends WeakHandler<VideoPlayerActivity> {
        public VideoPlayerEventHandler(VideoPlayerActivity owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoPlayerActivity activity = getOwner();
            if(activity == null) return;

            switch (msg.getData().getInt("event")) {
                case EventManager.MediaPlayerPlaying:
                	//#debug debug
                	Log.i(TAG, "MediaPlayerPlaying");
                    activity.setESTracks();
                    break;
                case EventManager.MediaPlayerPaused:
                	//#debug debug
                	Log.i(TAG, "MediaPlayerPaused");
                    break;
                case EventManager.MediaPlayerStopped:
                	if(activity.progressBar.getVisibility() == View.GONE){
                		activity.progressBar.setVisibility(View.VISIBLE);
                	}
                	//#debug debug
                	Log.i(TAG, "MediaPlayerStopped");
                    break;
                case EventManager.MediaPlayerEndReached:
                	//#debug debug
                	Log.i(TAG, "MediaPlayerEndReached");
                    activity.endReached();
                    break;
                case EventManager.MediaPlayerVout:
                	//#debug debug
                	Log.i(TAG, "MediaPlayerVout");
                	if(activity.progressBar.getVisibility() == View.VISIBLE){
                		activity.progressBar.setVisibility(View.GONE);
                	}
                    activity.handleVout(msg);
                    break;
                default:
                	//#debug debug
                	Log.e(TAG, String.format("Event not handled (0x%x)", msg.getData().getInt("event")));
                    break;
            }
            activity.updateOverlayPausePlay();
        }
    };

    /**
     * Handle resize of the surface and the overlay
     */
    private final Handler mHandler = new VideoPlayerHandler(this);

    private static class VideoPlayerHandler extends WeakHandler<VideoPlayerActivity> {
        public VideoPlayerHandler(VideoPlayerActivity owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoPlayerActivity activity = getOwner();
            if(activity == null) // WeakReference could be GC'ed early
                return;

            switch (msg.what) {
                case FADE_OUT:
                    activity.hideOverlay(false);
                    break;
                case SHOW_PROGRESS:
                    int pos = activity.setOverlayProgress();
                    if (activity.canShowProgress()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
                case SURFACE_SIZE:
                    activity.changeSurfaceSize();
                    break;
                case FADE_OUT_INFO:
//                    activity.fadeOutInfo();
                    break;
            }
        }
    };

    private boolean canShowProgress() {
        return !mDragging && mShowing && mLibVLC.isPlaying();
    }

    private void endReached() {
        /* Exit player when reach the end */
        mEndReached = true;
        finish();
    }

    private void handleVout(Message msg) {
        if (msg.getData().getInt("data") == 0 && !mEndReached) {
            /* Video track lost, open in audio mode */
            //#debug debug
        	Log.i(TAG, "Video track lost, switching to audio");
            mSwitchingView = true;
            finish();
        }
    }

    private void changeSurfaceSize() {
        // get screen size
        int dw = getWindow().getDecorView().getWidth();
        int dh = getWindow().getDecorView().getHeight();

        // getWindow().getDecorView() doesn't always take orientation into account, we have to correct the values
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (dw > dh && isPortrait || dw < dh && !isPortrait) {
            int d = dw;
            dw = dh;
            dh = d;
        }
        if (dw * dh == 0)
            return;

        // compute the aspect ratio
        double ar, vw;
        double density = (double)mSarNum / (double)mSarDen;
        if (density == 1.0) {
            /* No indication about the density, assuming 1:1 */
            vw = mVideoWidth;
            ar = (double)mVideoWidth / (double)mVideoHeight;
        } else {
            /* Use the specified aspect ratio */
            vw = mVideoWidth * density;
            ar = vw / mVideoHeight;
        }

        // compute the display aspect ratio
        double dar = (double) dw / (double) dh;

        switch (mCurrentSize) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_FIT_HORIZONTAL:
                dh = (int) (dw / ar);
                break;
            case SURFACE_FIT_VERTICAL:
                dw = (int) (dh * ar);
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoHeight;
                dw = (int) vw;
                break;
        }

        // align width on 16bytes
        int alignedWidth = (mVideoWidth + mSurfaceAlign) & ~mSurfaceAlign;

        // force surface buffer size
        mSurfaceHolder.setFixedSize(alignedWidth, mVideoHeight);

        // set display size
        LayoutParams lp = mSurface.getLayoutParams();
        lp.width = dw * alignedWidth / mVideoWidth;
        lp.height = dh;
        mSurface.setLayoutParams(lp);

        // set frame size (crop if necessary)
        lp = mSurfaceFrame.getLayoutParams();
        lp.width = dw;
        lp.height = dh;
        mSurfaceFrame.setLayoutParams(lp);

        mSurface.invalidate();
    }

    /**
     * attach and disattach surface to the lib
     */
    private final SurfaceHolder.Callback mSurfaceCallback = new Callback() {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(format == PixelFormat.RGBX_8888){
            	//#debug debug
            	Log.d(TAG, "Pixel format is RGBX_8888");
            }
            else if(format == ImageFormat.YV12){
            	//#debug debug
                Log.d(TAG, "Pixel format is YV12");
            }
            else{
            	//#debug debug
                Log.d(TAG, "Pixel format is other/unknown");
            }
            mLibVLC.attachSurface(holder.getSurface(), VideoPlayerActivity.this, width, height);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mLibVLC.detachSurface();
        }
    };

    /**
     * show overlay the the default timeout
     */
    private void showOverlay() {
        showOverlay(OVERLAY_TIMEOUT);
    }

    /**
     * show overlay
     */
    private void showOverlay(int timeout) {
       /* mHandler.sendEmptyMessage(SHOW_PROGRESS);
        if (!mShowing) {
            mShowing = true;
            if (!mIsLocked) {
                mOverlayHeader.setVisibility(View.VISIBLE);
                dimStatusBar(false);
            }
        }
        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
        updateOverlayPausePlay();*/
    }


    /**
     * hider overlay
     */
    private void hideOverlay(boolean fromUser) {
        /*if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);
          //#debug debug
            Log.i(TAG, "remove View!");
            if (!fromUser && !mIsLocked) {
                mOverlayHeader.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
            }
            mOverlayHeader.setVisibility(View.INVISIBLE);
            mShowing = false;
            dimStatusBar(true);
        }*/
    }

    /**
     * Dim the status bar and/or navigation icons when needed on Android 3.x.
     * Hide it on Android 4.0 and later
     */
    @TargetApi(11)
    private void dimStatusBar(boolean dim) {
        if (!Util.isHoneycombOrLater() || !Util.hasNavBar())
            return;
        mSurface.setSystemUiVisibility(
                dim ? (Util.hasCombBar()
                        ? View.SYSTEM_UI_FLAG_LOW_PROFILE
                        : View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                    : View.SYSTEM_UI_FLAG_VISIBLE);
    }

    private void updateOverlayPausePlay() {
        if (mLibVLC == null) {
            return;
        }
    }

    /**
     * update the overlay
     */
    private int setOverlayProgress() {
        if (mLibVLC == null) {
            return 0;
        }
        int time = (int) mLibVLC.getTime();
        // Update all view elements

        mSysTime.setText(DateFormat.format("kk:mm", System.currentTimeMillis()));

        return time;
    }

    private void setESTracks() {
        if (mLastAudioTrack >= 0) {
            mLibVLC.setAudioTrack(mLastAudioTrack);
            mLastAudioTrack = -1;
        }
        if (mLastSpuTrack >= 0) {
            mLibVLC.setSpuTrack(mLastSpuTrack);
            mLastSpuTrack = -1;
        }
    }

    private void startPlay() {
        mLocation = null;
        String title = getResources().getString(R.string.title);
        boolean dontParse = false;
        boolean fromStart = false;
        
        if (getIntent() != null) {
            /* Started from external application */
            mLocation = getIntent().getStringExtra("rtsp");
            title = getIntent().getStringExtra("title");
        }
        mSurface.setKeepScreenOn(true);

        /* Start / resume playback */
        if (savedIndexPosition > -1) {
            mLibVLC.playIndex(savedIndexPosition);
        } else if (mLocation != null && mLocation.length() > 0 && !dontParse) {
            savedIndexPosition = mLibVLC.readMedia(mLocation, false);
        }

        if (mLocation != null && mLocation.length() > 0 && !dontParse) {
            // restore last position
            Media media = DatabaseManager.getInstance(this).getMedia(this, mLocation);
            if(media != null) {
                if(media.getTime() > 0 && !fromStart)
                    mLibVLC.setTime(media.getTime());

                mLastAudioTrack = media.getAudioTrack();
                mLastSpuTrack = media.getSpuTrack();
            }

//            try {
//                title = URLDecoder.decode(mLocation, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//            }
//            if (title.startsWith("file:")) {
//                title = new File(title).getName();
//                int dotIndex = title.lastIndexOf('.');
//                if (dotIndex != -1)
//                    title = title.substring(0, dotIndex);
//            }
        } 
        mTitle.setText(title);
    }

    @SuppressWarnings("deprecation")
    private int getScreenRotation(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 8 /* Android 2.2 has getRotation */) {
            try {
                Method m = display.getClass().getDeclaredMethod("getRotation");
                return (Integer) m.invoke(display);
            } catch (Exception e) {
                return Surface.ROTATION_0;
            }
        } else {
            return display.getOrientation();
        }
    }

    private int getScreenOrientation (){
        switch (getScreenRotation()) {
        case Surface.ROTATION_0:
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        case Surface.ROTATION_90:
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        case Surface.ROTATION_180:
            // SCREEN_ORIENTATION_REVERSE_PORTRAIT only available since API Level 9+
             return (Build.VERSION.SDK_INT >= 8
                    ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        case Surface.ROTATION_270:
            // SCREEN_ORIENTATION_REVERSE_LANDSCAPE only available since API Level 9+
            return (Build.VERSION.SDK_INT >= 8
                    ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        default :
            return 0;
        }
    }

}
