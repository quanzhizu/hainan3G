package com.iconverge.ct.traffic.bean;

/**
 * 停车场区域对象
 * 
 * @author guozhaoge
 * 
 */
public class RegionBean {

	private int regionId;
	private String id;
	private String name;

	public RegionBean() {
		super();
	}

	public RegionBean(int regionId, String id, String name) {
		super();
		this.regionId = regionId;
		this.id = id;
		this.name = name;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
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

	@Override
	public String toString() {
		return "CarportAreaBean [regionId=" + regionId + ", id=" + id + ", name=" + name + "]";
	}

}
