package com.sst.sstat.model;

@SuppressWarnings("serial")
public class NetworkAccessPoint implements IDataPointScope 
{
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
	private String N10;
	private BoolEx N16;

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
	public String getN10() {
		return N10;
	}

	/**
	 * @param n10
	 *            the n10 to set
	 */
	public void setN10(String n10) {
		N10 = n10;
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
}
