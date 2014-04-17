package com.iconverge.ct.traffic.data;

import java.util.List;

import com.mapabc.mapapi.route.Route;

public class DataManager {

	private static DataManager dataManager;

	public static DataManager getInstance() {
		if (dataManager == null)
			dataManager = new DataManager();
		return dataManager;
	}

	public static void setData(DataManager mDataManager) {
		dataManager = mDataManager;
	}

	public List<Route> routes;
}
