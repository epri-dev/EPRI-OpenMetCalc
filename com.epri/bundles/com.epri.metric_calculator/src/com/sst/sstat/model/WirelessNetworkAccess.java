package com.sst.sstat.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WirelessNetworkAccess implements IDataPointScope {

	public enum Encryption implements Serializable {
		WEP, WPA, WPA2_Preshared, WPA2_Enterprise
	};

	public enum AntennaType implements Serializable {
		omni_directional,
		directional,
		point_to_point,
		Other,
		NA
	};	

	public enum Frequency implements Serializable {
		FH_CDMA,
		AFH,
		Cognitive,
		Other
	}
	
	private String YID;
	private Encryption Y01;
	private AntennaType Y02;
	private Frequency Y03;
	private String Y04;
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return YID;
	}

	public String getYID() {
		return YID;
	}

	public void setYID(String yID) {
		YID = yID;
	}

	public Encryption getY01() {
		return Y01;
	}

	public void setY01(Encryption y01) {
		Y01 = y01;
	}

	public AntennaType getY02() {
		return Y02;
	}

	public void setY02(AntennaType y02) {
		Y02 = y02;
	}

	public Frequency getY03() {
		return Y03;
	}

	public void setY03(Frequency y03) {
		Y03 = y03;
	}

	public String getY04() {
		return Y04;
	}

	public void setY04(String y04) {
		Y04 = y04;
	}

}
