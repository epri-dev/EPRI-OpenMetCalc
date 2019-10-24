package com.sst.sstat.model;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class Event implements IDataPointScope {
	public enum Level implements Serializable {
		Low, Medium, High
	};

	private String EID;
	private String E01;
	private Level E02;
	private Date E03;
	private Date E04;
	private BoolEx E05;
	private String E06;

	@Override
	public String getId() {
		return EID;
	}

	public String getEID() {
		return EID;
	}

	public void setEID(String eID) {
		EID = eID;
	}

	public String getE01() {
		return E01;
	}

	public void setE01(String e01) {
		E01 = e01;
	}

	public Level getE02() {
		return E02;
	}

	public void setE02(Level e02) {
		E02 = e02;
	}

	public Date getE03() {
		return E03;
	}

	public void setE03(Date e03) {
		E03 = e03;
	}

	public Date getE04() {
		return E04;
	}

	public void setE04(Date e04) {
		E04 = e04;
	}

	/**
	 * @return the e05
	 */
	public BoolEx getE05() {
		return E05;
	}

	/**
	 * @param e05
	 *            the e05 to set
	 */
	public void setE05(BoolEx e05) {
		E05 = e05;
	}

	public String getE06() {
		return E06;
	}

	public void setE06(String e06) {
		E06 = e06;
	}

}
