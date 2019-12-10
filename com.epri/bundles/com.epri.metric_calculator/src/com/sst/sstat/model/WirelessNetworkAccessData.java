package com.sst.sstat.model;

public class WirelessNetworkAccessData {
	private String YID;
	private String Y01;
	private String Y02;
	private String Y03;
	private String Y04;
	
	public WirelessNetworkAccessData(WirelessNetworkAccess dataModel) {
		this.YID = dataModel.getId();
		this.Y01 = dataModel.getY01().toString().replace('_', '-');
		this.Y02 = dataModel.getY02().toString().replace('_', '-');
		this.Y03 = dataModel.getY03().toString().replace('_', '-');
		this.Y04 = dataModel.getY04();
	}
	
	public String getYID() {
		return YID;
	}
	public void setYID(String yID) {
		YID = yID;
	}
	public String getY01() {
		return Y01;
	}
	public void setY01(String y01) {
		Y01 = y01;
	}
	public String getY02() {
		return Y02;
	}
	public void setY02(String y02) {
		Y02 = y02;
	}
	public String getY03() {
		return Y03;
	}
	public void setY03(String y03) {
		Y03 = y03;
	}
	public String getY04() {
		return Y04;
	}
	public void setY04(String y04) {
		Y04 = y04;
	}
}
