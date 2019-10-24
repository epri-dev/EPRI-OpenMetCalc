package com.sst.sstat;

import com.sst.sstat.model.Asset;
import com.sst.sstat.model.Database;
import com.sst.sstat.model.EmailServerFilter;
import com.sst.sstat.model.EndUserDevice;
import com.sst.sstat.model.Event;
import com.sst.sstat.model.Incident;
import com.sst.sstat.model.NetworkAccessPoint;
import com.sst.sstat.model.Personnel;
import com.sst.sstat.model.ThreadWarningAlert;
import com.sst.sstat.model.Vulnerability;
import com.sst.sstat.model.WebProxy;
import com.sst.sstat.model.WirelessNetworkAccess;

public class ModelProvider {
	public static Class<?>[] getModelClasses() {
		return new Class<?>[] { Asset.class, Database.class, EmailServerFilter.class,
				EndUserDevice.class, Event.class, Incident.class, NetworkAccessPoint.class, Personnel.class,
				Vulnerability.class, ThreadWarningAlert.class, WebProxy.class, WirelessNetworkAccess.class };
	}
}
