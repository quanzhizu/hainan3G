package com.iconverge.ct.traffic.bean;

/**
 * 停车场信息对象
 * 
 * @author guozhaoge
 * 
 */
public class ParkBean {

	private int parkId;
	/* 停车场编号,eg：A1 */
	private String id;
	private int regionId;
	/* 停车场名称 ,eg：盾安九龙都 */
	private String name;
	/* 停车场总共车位数 ,eg：551 */
	private String total;
	/* 停车场闲置车位数 ,eg：119 */
	private String remainder;
	/* 数据更新时间 ,eg：2013-02-20 09:31:40 */
	private String time;
	/* 停车场状态,eg：1 */
	private String status;
	/* 停车场地址,eg：重庆市场杨家坪石杨路与兴胜路交汇处 */
	private String address;
	/* 停车场GPS经纬度，eg：106.5129366517067,29.512052564170906 */
	private double lon;
	private double lat;

	public ParkBean() {
		super();
	}

	public ParkBean(int parkId, String id, int regionId, String name, String total, String remainder, String time, String status, String address, double lon, double lat) {
		super();
		this.parkId = parkId;
		this.id = id;
		this.regionId = regionId;
		this.name = name;
		this.total = total;
		this.remainder = remainder;
		this.time = time;
		this.status = status;
		this.address = address;
		this.lon = lon;
		this.lat = lat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getRemainder() {
		return remainder;
	}

	public void setRemainder(String remainder) {
		this.remainder = remainder;
	}


	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@Override
	public String toString() {
		return "CarportBean [parkId=" + parkId + ", id=" + id + ", regionId=" + regionId + ", name=" + name + ", total=" + total + ", remainder=" + remainder + ", time=" + time + ", status=" + status + ", address=" + address + ", lon=" + lon + ", lat=" + lat + "]";
	}

}
