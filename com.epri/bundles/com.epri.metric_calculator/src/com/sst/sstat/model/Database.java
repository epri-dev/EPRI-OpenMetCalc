package com.sst.sstat.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Database implements IDataPointScope {
	public enum Level implements Serializable {
		Low, Med, High
	};

	public enum Frequency implements Serializable {
		on_change, hourly, daily, weekly, monthly, none
	};

	public enum Audit implements Serializable {
		formal, informal, none, NA
	};

	private String DID;
	private Level D01;
	private BoolEx D02;
	private BoolEx D03;
	private double D04;
	private double D05;
	private BoolEx D06;

	private Frequency D07;
	private BoolEx D08;

	private Audit D09;
	private BoolEx D10;
	private String D11;
	private String D12;


	@Override
	public String getId() {
		return DID;
	}

	public String getDID() {
		return DID;
	}

	public void setDID(String dID) {
		DID = dID;
	}

	public Level getD01() {
		return D01;
	}

	public void setD01(Level d01) {
		D01 = d01;
	}

	/**
	 * @return the d02
	 */
	public BoolEx getD02() {
		return D02;
	}

	/**
	 * @param d02
	 *            the d02 to set
	 */
	public void setD02(BoolEx d02) {
		D02 = d02;
	}

	/**
	 * @return the d03
	 */
	public BoolEx getD03() {
		return D03;
	}

	/**
	 * @param d03
	 *            the d03 to set
	 */
	public void setD03(BoolEx d03) {
		D03 = d03;
	}

	public void setD04(double d04) {
		D04 = d04;
	}

	public double getD05() {
		return D05;
	}

	public void setD05(double d05) {
		D05 = d05;
	}

	public BoolEx getD06() {
		return D06;
	}

	public void setD06(BoolEx d06) {
		D06 = d06;
	}
	public Frequency getD07() {
		return D07;
	}

	public void setD07(Frequency d07) {
		D07 = d07;
	}

	public double getD04() {
		return D04;
	}

	/**
	 * @return the d08
	 */
	public BoolEx getD08() {
		return D08;
	}

	/**
	 * @param d08
	 *            the d08 to set
	 */
	public void setD08(BoolEx d08) {
		D08 = d08;
	}
	public Audit getD09() {
		return D09;
	}

	public void setD09(Audit d09) {
		D09 = d09;
	}

	public BoolEx getD10() {
		return D10;
	}

	public void setD10(BoolEx d10) {
		D10 = d10;
	}

	public String getD11() {
		return D11;
	}

	public void setD11(String d11) {
		D11 = d11;
	}

	public String getD12() {
		return D12;
	}

	public void setD12(String d12) {
		D12 = d12;
	}
}
