package com.iconverge.ct.traffic.bean;

import java.io.Serializable;

public class PoiResultBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -9177650864833134759L;
	private String name;
	private String poiid;
	private String featcode;
	private String permanentid;
	private String province;
	private String coord;
	private String city;
	private String addr;
	private String tel;
	private String county;
	private String custid;
	private String subjectid;
	private double distance;
	private String admincode;
	private String bsflag;

	public PoiResultBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPoiid() {
		return poiid;
	}

	public void setPoiid(String poiid) {
		this.poiid = poiid;
	}

	public String getFeatcode() {
		return featcode;
	}

	public void setFeatcode(String featcode) {
		this.featcode = featcode;
	}

	public String getPermanentid() {
		return permanentid;
	}

	public void setPermanentid(String permanentid) {
		this.permanentid = permanentid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCoord() {
		return coord;
	}

	public void setCoord(String coord) {
		this.coord = coord;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getAdmincode() {
		return admincode;
	}

	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}

	public String getBsflag() {
		return bsflag;
	}

	public void setBsflag(String bsflag) {
		this.bsflag = bsflag;
	}

}
