package com.sst.sstat.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Personnel implements IDataPointScope {
	public enum LastSecurityAwarenessTraining implements Serializable {
		Less_Than_One_Week,
		Less_Than_One_Month,
		Less_Than_Three_Months,
		More_Than_Three_Months,
		never,
		Null
	};

	private String PID;
	private LastSecurityAwarenessTraining P01;
	private BoolEx P02;
	private BoolEx P03;
	private BoolEx P04;
	private BoolEx P05;
	private String P06;
	private BoolEx P07;
	private BoolEx P08;

	@Override
	public String getId() {
		return PID;
	}

	/**
	 * @return the pID
	 */
	public String getPID() {
		return PID;
	}

	/**
	 * @param pID
	 *            the pID to set
	 */
	public void setPID(String pID) {
		PID = pID;
	}

	/**
	 * @return the p01
	 */
	public LastSecurityAwarenessTraining getP01() {
		return P01;
	}

	/**
	 * @param p01
	 *            the p01 to set
	 */
	public void setP01(LastSecurityAwarenessTraining p01) {
		P01 = p01;
	}

	/**
	 * @return the p02
	 */
	public BoolEx getP02() {
		return P02;
	}

	/**
	 * @param p02
	 *            the p02 to set
	 */
	public void setP02(BoolEx p02) {
		P02 = p02;
	}

	/**
	 * @return the p03
	 */
	public BoolEx getP03() {
		return P03;
	}

	/**
	 * @param p03
	 *            the p03 to set
	 */
	public void setP03(BoolEx p03) {
		P03 = p03;
	}

	/**
	 * @return the p04
	 */
	public BoolEx getP04() {
		return P04;
	}

	/**
	 * @param p04
	 *            the p04 to set
	 */
	public void setP04(BoolEx p04) {
		P04 = p04;
	}

	/**
	 * @return the p05
	 */
	public BoolEx getP05() {
		return P05;
	}

	/**
	 * @param p05
	 *            the p05 to set
	 */
	public void setP05(BoolEx p05) {
		P05 = p05;
	}

	/**
	 * @return the p06
	 */
	public String getP06() {
		return P06;
	}

	/**
	 * @param p06
	 *            the p06 to set
	 */
	public void setP06(String p06) {
		P06 = p06;
	}

	public BoolEx getP07() {
		return P07;
	}

	public void setP07(BoolEx p07) {
		P07 = p07;
	}

	public BoolEx getP08() {
		return P08;
	}

	public void setP08(BoolEx p08) {
		P08 = p08;
	}

}
