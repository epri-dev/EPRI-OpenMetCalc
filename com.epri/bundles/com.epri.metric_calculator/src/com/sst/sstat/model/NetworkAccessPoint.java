package com.sst.sstat.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NetworkAccessPoint implements IDataPointScope {
	public enum Protocol implements Serializable {
		_802_11a, _802_11b, _802_11g, _802_11n, _802_11ac, _802_11ad, other, Null
	};

	public enum Encryption implements Serializable {
		none, WEP, WPA, WPA2_Preshared, WPA2_Enterprise, Other, NA
	};

	public enum AntennaType implements Serializable {
		Omni_directional,
		directional,
		point_to_point,
		Other,
		NA
	};

	public enum Frequency implements Serializable {
		none,
		FH_CDMA,
		AFH,
		Cognitive,
		Other,
		NA
	}

	private String NID;
	private int N01;
	private int N02;
	private int N03;
	private int N04;
	private int N05;
	private int N06;
	private int N07;
	private int N08;
	private int N09;
	private BoolEx N10;
	private Protocol N11;
	private double N12;
	private BoolEx N16;
	private String N17;

	@Override
	public String getId() {
		return NID;
	}

	/**
	 * @return the nID
	 */
	public String getNID() {
		return NID;
	}

	/**
	 * @param nID
	 *            the nID to set
	 */
	public void setNID(String nID) {
		NID = nID;
	}

	/**
	 * @return the n01
	 */
	public int getN01() {
		return N01;
	}

	/**
	 * @param n01
	 *            the n01 to set
	 */
	public void setN01(int n01) {
		N01 = n01;
	}

	/**
	 * @return the n02
	 */
	public int getN02() {
		return N02;
	}

	/**
	 * @param n02
	 *            the n02 to set
	 */
	public void setN02(int n02) {
		N02 = n02;
	}

	/**
	 * @return the n03
	 */
	public int getN03() {
		return N03;
	}

	/**
	 * @param n03
	 *            the n03 to set
	 */
	public void setN03(int n03) {
		N03 = n03;
	}

	/**
	 * @return the n04
	 */
	public int getN04() {
		return N04;
	}

	/**
	 * @param n04
	 *            the n04 to set
	 */
	public void setN04(int n04) {
		N04 = n04;
	}

	/**
	 * @return the n05
	 */
	public int getN05() {
		return N05;
	}

	/**
	 * @param n05
	 *            the n05 to set
	 */
	public void setN05(int n05) {
		N05 = n05;
	}

	/**
	 * @return the n06
	 */
	public int getN06() {
		return N06;
	}

	/**
	 * @param n06
	 *            the n06 to set
	 */
	public void setN06(int n06) {
		N06 = n06;
	}

	/**
	 * @return the n07
	 */
	public int getN07() {
		return N07;
	}

	/**
	 * @param n07
	 *            the n07 to set
	 */
	public void setN07(int n07) {
		N07 = n07;
	}

	/**
	 * @return the n08
	 */
	public int getN08() {
		return N08;
	}

	/**
	 * @param n08
	 *            the n08 to set
	 */
	public void setN08(int n08) {
		N08 = n08;
	}

	/**
	 * @return the n09
	 */
	public int getN09() {
		return N09;
	}

	/**
	 * @param n09
	 *            the n09 to set
	 */
	public void setN09(int n09) {
		N09 = n09;
	}

	/**
	 * @return the n10
	 */
	public BoolEx getN10() {
		return N10;
	}

	/**
	 * @param n10
	 *            the n10 to set
	 */
	public void setN10(BoolEx n10) {
		N10 = n10;
	}

	/**
	 * @return the n11
	 */
	public Protocol getN11() {
		return N11;
	}

	/**
	 * @param n11
	 *            the n11 to set
	 */
	public void setN11(Protocol n11) {
		N11 = n11;
	}

	/**
	 * @return the n12
	 */
	public double getN12() {
		return N12;
	}

	/**
	 * @param n12
	 *            the n12 to set
	 */
	public void setN12(double n12) {
		N12 = n12;
	}

	/**
	 * @return the n16
	 */
	public BoolEx getN16() {
		return N16;
	}

	/**
	 * @param n16
	 *            the n16 to set
	 */
	public void setN16(BoolEx n16) {
		N16 = n16;
	}

	/**
	 * @return the n17
	 */
	public String getN17() {
		return N17;
	}

	/**
	 * @param n17
	 *            the n17 to set
	 */
	public void setN17(String n17) {
		N17 = n17;
	}
}
