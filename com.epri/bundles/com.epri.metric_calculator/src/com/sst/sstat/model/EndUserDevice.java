package com.sst.sstat.model;

import java.io.Serializable;
@SuppressWarnings("serial")
public class EndUserDevice implements IDataPointScope {
	public enum UpdateFrequency implements Serializable {
		none, min, hour, day, week
	};

	public enum ScanFrequency implements Serializable {
		none, on_access, min, hour, day, week
	}

	public enum Encryption implements Serializable {
		none, mandatory, discretionary, no_encryption, Null
	};

	public enum HIDSManagement implements Serializable {
		Null, mandatory, discretionary
	};


	private String UID;
	private UpdateFrequency U01;
	private ScanFrequency U02;
	private double U03;
	private double U04;
	private Encryption U05;
	private BoolEx U06;
	private BoolEx U07;
	private HIDSManagement U08;
	private int U09;
	private String U10;
	private String U11;
	private BoolEx U12;

	@Override
	public String getId() {
		return UID;
	}

	/**
	 * @return the uID
	 */
	public String getUID() {
		return UID;
	}

	/**
	 * @param uID
	 *            the uID to set
	 */
	public void setUID(String uID) {
		UID = uID;
	}

	/**
	 * @return the u01
	 */
	public UpdateFrequency getU01() {
		return U01;
	}

	/**
	 * @param u01
	 *            the u01 to set
	 */
	public void setU01(UpdateFrequency u01) {
		U01 = u01;
	}

	/**
	 * @return the u02
	 */
	public ScanFrequency getU02() {
		return U02;
	}

	/**
	 * @param u02
	 *            the u02 to set
	 */
	public void setU02(ScanFrequency u02) {
		U02 = u02;
	}

	/**
	 * @return the u03
	 */
	public Double getU03() {
		return U03;
	}

	/**
	 * @param u03
	 *            the u03 to set
	 */
	public void setU03(double u03) {
		U03 = u03;
	}

	/**
	 * @return the u04
	 */
	public Double getU04() {
		return U04;
	}

	/**
	 * @param u04
	 *            the u04 to set
	 */
	public void setU04(double u04) {
		U04 = u04;
	}

	/**
	 * @return the u05
	 */
	public Encryption getU05() {
		return U05;
	}

	/**
	 * @param u05
	 *            the u05 to set
	 */
	public void setU05(Encryption u05) {
		U05 = u05;
	}

	/**
	 * @return the u06
	 */
	public BoolEx getU06() {
		return U06;
	}

	/**
	 * @param u06
	 *            the u06 to set
	 */
	public void setU06(BoolEx u06) {
		U06 = u06;
	}

	/**
	 * @return the u07
	 */
	public BoolEx getU07() {
		return U07;
	}

	/**
	 * @param u07
	 *            the u07 to set
	 */
	public void setU07(BoolEx u07) {
		U07 = u07;
	}

	/**
	 * @return the u08
	 */
	public HIDSManagement getU08() {
		return U08;
	}

	/**
	 * @param u08
	 *            the u08 to set
	 */
	public void setU08(HIDSManagement u08) {
		U08 = u08;
	}

	/**
	 * @return the u09
	 */
	public int getU09() {
		return U09;
	}

	/**
	 * @param u09
	 *            the u09 to set
	 */
	public void setU09(int u09) {
		U09 = u09;
	}

	/**
	 * @return the u10
	 */
	public String getU10() {
		return U10;
	}

	/**
	 * @param u10
	 *            the u10 to set
	 */
	public void setU10(String u10) {
		U10 = u10;
	}

	public String getU11() {
		return U11;
	}

	public void setU11(String u11) {
		U11 = u11;
	}

	public BoolEx getU12() {
		return U12;
	}

	public void setU12(BoolEx u12) {
		U12 = u12;
	}

}
