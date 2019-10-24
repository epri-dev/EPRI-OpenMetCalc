package com.sst.sstat.model;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class Incident implements IDataPointScope {
	public enum Level implements Serializable {
		Low, Med, High
	};

	public enum CategoryOfI18 {
		unknown,

		public_disclosure,

		adversary_notification,

		law_enforcement_compromise_notification,

		malfunction_of_resource,

		non_security_staff_report,

		security_staff_report,

		threat_hunting_process,

		alert_event_generated_by_security_software_hardware
	}

	@Override
	public String getId() {
		return IID;
	}

	private String IID;
	private Date I01;
	private Date I02;
	private Date I03;
	private Date I04;
	private Date I05;
	private BoolEx I06;
	private BoolEx I07;
	private BoolEx I08;
	private BoolEx I09;
	private BoolEx I10;
	private BoolEx I11;
	private BoolEx I12;
	private BoolEx I13;
	private BoolEx I14;
	private Level I15;
	private double I16;
	private double I17;

	private CategoryOfI18 I18;

	private String I19;

	/**
	 * @return the iID
	 */
	public String getIID() {
		return IID;
	}

	/**
	 * @param iID
	 *            the iID to set
	 */
	public void setIID(String iID) {
		IID = iID;
	}

	/**
	 * @return the i01
	 */
	public Date getI01() {
		return I01;
	}

	/**
	 * @param i01
	 *            the i01 to set
	 */
	public void setI01(Date i01) {
		I01 = i01;
	}

	/**
	 * @return the i02
	 */
	public Date getI02() {
		return I02;
	}

	/**
	 * @param i02
	 *            the i02 to set
	 */
	public void setI02(Date i02) {
		I02 = i02;
	}

	/**
	 * @return the i03
	 */
	public Date getI03() {
		return I03;
	}

	/**
	 * @param i03
	 *            the i03 to set
	 */
	public void setI03(Date i03) {
		I03 = i03;
	}

	/**
	 * @return the i04
	 */
	public Date getI04() {
		return I04;
	}

	/**
	 * @param i04
	 *            the i04 to set
	 */
	public void setI04(Date i04) {
		I04 = i04;
	}

	/**
	 * @return the i05
	 */
	public Date getI05() {
		return I05;
	}

	/**
	 * @param i05
	 *            the i05 to set
	 */
	public void setI05(Date i05) {
		I05 = i05;
	}

	/**
	 * @return the i06
	 */
	public BoolEx getI06() {
		return I06;
	}

	/**
	 * @param i06
	 *            the i06 to set
	 */
	public void setI06(BoolEx i06) {
		I06 = i06;
	}

	/**
	 * @return the i07
	 */
	public BoolEx getI07() {
		return I07;
	}

	/**
	 * @param i07
	 *            the i07 to set
	 */
	public void setI07(BoolEx i07) {
		I07 = i07;
	}

	/**
	 * @return the i08
	 */
	public BoolEx getI08() {
		return I08;
	}

	/**
	 * @param i08
	 *            the i08 to set
	 */
	public void setI08(BoolEx i08) {
		I08 = i08;
	}

	/**
	 * @return the i09
	 */
	public BoolEx getI09() {
		return I09;
	}

	/**
	 * @param i09
	 *            the i09 to set
	 */
	public void setI09(BoolEx i09) {
		I09 = i09;
	}

	/**
	 * @return the i10
	 */
	public BoolEx getI10() {
		return I10;
	}

	/**
	 * @param i10
	 *            the i10 to set
	 */
	public void setI10(BoolEx i10) {
		I10 = i10;
	}

	/**
	 * @return the i11
	 */
	public BoolEx getI11() {
		return I11;
	}

	/**
	 * @param i11
	 *            the i11 to set
	 */
	public void setI11(BoolEx i11) {
		I11 = i11;
	}

	/**
	 * @return the i12
	 */
	public BoolEx getI12() {
		return I12;
	}

	/**
	 * @param i12
	 *            the i12 to set
	 */
	public void setI12(BoolEx i12) {
		I12 = i12;
	}

	/**
	 * @return the i13
	 */
	public BoolEx getI13() {
		return I13;
	}

	/**
	 * @param i13
	 *            the i13 to set
	 */
	public void setI13(BoolEx i13) {
		I13 = i13;
	}

	/**
	 * @return the i14
	 */
	public BoolEx getI14() {
		return I14;
	}

	/**
	 * @param i14
	 *            the i14 to set
	 */
	public void setI14(BoolEx i14) {
		I14 = i14;
	}

	/**
	 * @return the i15
	 */
	public Level getI15() {
		return I15;
	}

	/**
	 * @param i15
	 *            the i15 to set
	 */
	public void setI15(Level i15) {
		I15 = i15;
	}

	/**
	 * @return the i16
	 */
	public double getI16() {
		return I16;
	}

	/**
	 * @param i16
	 *            the i16 to set
	 */
	public void setI16(double i16) {
		I16 = i16;
	}

	/**
	 * @return the i17
	 */
	public double getI17() {
		return I17;
	}

	/**
	 * @param i17
	 *            the i17 to set
	 */
	public void setI17(double i17) {
		I17 = i17;
	}

	/**
	 * @return the i18
	 */
	public CategoryOfI18 getI18() {
		return I18;
	}

	/**
	 * @param i18
	 *            the i18 to set
	 */
	public void setI18(CategoryOfI18 i18) {
		I18 = i18;
	}

	/**
	 * @return the i19
	 */
	public String getI19() {
		return I19;
	}

	/**
	 * @param i19
	 *            the i19 to set
	 */
	public void setI19(String i19) {
		I19 = i19;
	}
}
