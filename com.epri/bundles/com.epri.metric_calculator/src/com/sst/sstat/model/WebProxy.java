package com.sst.sstat.model;

@SuppressWarnings("serial")
public class WebProxy implements IDataPointScope {

	private String NWID;
	private double W01;
	private BoolEx W02;
	private BoolEx W03;
	private BoolEx W04;
	private String W05;
	private String W06;	

	@Override
	public String getId() {
		return NWID;
	}
	
	public String getNWID() {
		return NWID;
	}
	
	public void setNWID(String nwID) {
		NWID = nwID;
	}
	
	/**
	 * @return the w01
	 */
	public double getW01() {
		return W01;
	}

	/**
	 * @param w01
	 *            the w01 to set
	 */
	public void setW01(double w01) {
		W01 = w01;
	}

	/**
	 * @return the w02
	 */
	public BoolEx getW02() {
		return W02;
	}

	/**
	 * @param w02
	 *            the w02 to set
	 */
	public void setW02(BoolEx w02) {
		W02 = w02;
	}

	/**
	 * @return the w03
	 */
	public BoolEx getW03() {
		return W03;
	}

	/**
	 * @param w03
	 *            the w03 to set
	 */
	public void setW03(BoolEx w03) {
		W03 = w03;
	}

	/**
	 * @return the w04
	 */
	public BoolEx getW04() {
		return W04;
	}

	/**
	 * @param w04
	 *            the w04 to set
	 */
	public void setW04(BoolEx w04) {
		W04 = w04;
	}

	/**
	 * @return the w05
	 */
	public String getW05() {
		return W05;
	}

	/**
	 * @param w05
	 *            the w05 to set
	 */
	public void setW05(String w05) {
		W05 = w05;
	}

	public String getW06() {
		return W06;
	}

	public void setW06(String w06) {
		W06 = w06;
	}
}
