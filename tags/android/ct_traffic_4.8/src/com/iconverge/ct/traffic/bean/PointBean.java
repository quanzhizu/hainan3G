package com.iconverge.ct.traffic.bean;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

import com.mapabc.mapapi.core.GeoPoint;

public class PointBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8767317071143129805L;
	private String name;
	private String address;
	private GeoPoint point;
	private Drawable drawable;
	private String label;

	public PointBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public GeoPoint getPoint() {
		return point;
	}

	public void setPoint(GeoPoint point) {
		this.point = point;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
