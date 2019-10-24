package com.sst.sstat.model;

@SuppressWarnings("serial")
public class BusinessUnit implements IDataPointScope {

	private String BID;
	private int B01;
	private int B02;
	private double B03;
	private int B04;
	private int B05;

	enum Practice {
		formal, informal, none
	}

	private Practice T06;
	private int T07;
	private int T08;
	private String T09;

	@Override
	public String getId() {
		return BID;
	}

	/**
	 * @return the bID
	 */
	public String getBID() {
		return BID;
	}

	/**
	 * @param bID
	 *            the bID to set
	 */
	public void setBID(String bID) {
		BID = bID;
	}

	/**
	 * @return the b01
	 */
	public int getB01() {
		return B01;
	}

	/**
	 * @param b01
	 *            the b01 to set
	 */
	public void setB01(int b01) {
		B01 = b01;
	}

	/**
	 * @return the b02
	 */
	public int getB02() {
		return B02;
	}

	/**
	 * @param b02
	 *            the b02 to set
	 */
	public void setB02(int b02) {
		B02 = b02;
	}

	/**
	 * @return the b03
	 */
	public double getB03() {
		return B03;
	}

	/**
	 * @param b03
	 *            the b03 to set
	 */
	public void setB03(double b03) {
		B03 = b03;
	}

	/**
	 * @return the b04
	 */
	public int getB04() {
		return B04;
	}

	/**
	 * @param b04
	 *            the b04 to set
	 */
	public void setB04(int b04) {
		B04 = b04;
	}

	/**
	 * @return the b05
	 */
	public int getB05() {
		return B05;
	}

	/**
	 * @param b05
	 *            the b05 to set
	 */
	public void setB05(int b05) {
		B05 = b05;
	}

	/**
	 * @return the t06
	 */
	public Practice getT06() {
		return T06;
	}

	/**
	 * @param t06
	 *            the t06 to set
	 */
	public void setT06(Practice t06) {
		T06 = t06;
	}

	/**
	 * @return the t07
	 */
	public int getT07() {
		return T07;
	}

	/**
	 * @param t07
	 *            the t07 to set
	 */
	public void setT07(int t07) {
		T07 = t07;
	}

	/**
	 * @return the t08
	 */
	public int getT08() {
		return T08;
	}

	/**
	 * @param t08
	 *            the t08 to set
	 */
	public void setT08(int t08) {
		T08 = t08;
	}

	public String getT09() {
		return T09;
	}

	public void setT09(String t09) {
		T09 = t09;
	}
}
