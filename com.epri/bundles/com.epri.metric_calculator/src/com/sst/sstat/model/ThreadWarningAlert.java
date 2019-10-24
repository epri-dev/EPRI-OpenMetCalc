package com.sst.sstat.model;

import java.util.Date;
@SuppressWarnings("serial")
public class ThreadWarningAlert implements IDataPointScope {

	private String TID;
	private String T01;
	private Date T02;
	private Date T03;
	private Date T04;
	private BoolEx T05;
	private BoolEx T06;
	private BoolEx T07;
	private BoolEx T08;
	private String T09;

	@Override
	public String getId() {
		return TID;
	}
	
	/**
	 * @return the tID
	 */
	public String getTID() {
		return TID;
	}

	/**
	 * @param tID
	 *            the tID to set
	 */
	public void setTID(String tID) {
		TID = tID;
	}

	/**
	 * @return the t01
	 */
	public String getT01() {
		return T01;
	}

	/**
	 * @param t01
	 *            the t01 to set
	 */
	public void setT01(String t01) {
		T01 = t01;
	}

	/**
	 * @return the t02
	 */
	public Date getT02() {
		return T02;
	}

	/**
	 * @param t02
	 *            the t02 to set
	 */
	public void setT02(Date t02) {
		T02 = t02;
	}

	/**
	 * @return the t03
	 */
	public Date getT03() {
		return T03;
	}

	/**
	 * @param t03
	 *            the t03 to set
	 */
	public void setT03(Date t03) {
		T03 = t03;
	}

	/**
	 * @return the t04
	 */
	public Date getT04() {
		return T04;
	}

	/**
	 * @param t04
	 *            the t04 to set
	 */
	public void setT04(Date t04) {
		T04 = t04;
	}

	/**
	 * @return the t05
	 */
	public BoolEx getT05() {
		return T05;
	}

	/**
	 * @param t05
	 *            the t05 to set
	 */
	public void setT05(BoolEx t05) {
		T05 = t05;
	}

	/**
	 * @return the t06
	 */
	public BoolEx getT06() {
		return T06;
	}

	/**
	 * @param t06
	 *            the t06 to set
	 */
	public void setT06(BoolEx t06) {
		T06 = t06;
	}

	public String getT09() {
		return T09;
	}

	public void setT09(String t09) {
		T09 = t09;
	}

	public BoolEx getT08() {
		return T08;
	}

	public void setT08(BoolEx t08) {
		T08 = t08;
	}

	public BoolEx getT07() {
		return T07;
	}

	public void setT07(BoolEx t07) {
		T07 = t07;
	}

}
