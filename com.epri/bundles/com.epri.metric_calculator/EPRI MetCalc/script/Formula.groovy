// Master Script
//Last edited by: CSL [05.31.2018] 
//1. Added back all functions for S and T metrics
//2. changed all function call 'getMetricScore(..)' to 'getMetricValue(..)'

//*** GLOBAL

	//** control constants
	int internal_error = -1000
	float init_value = -1
	float zero_value = -2.0
	
	
	//** cross-metric factors
	
	// asset vulnerability risk score map
	Map avrs_map = [:]
	
	
	// asset access control score map
	Map acs_map = [:]

	// network vulnerability risk score map
	Map nvrs_map = [:]
	
	
	// network access control score map
	Map nacs_map = [:]
	
	// number of employees
	def num_emp
	
	//** global functions
	def getNegativeScore (float max, float min, float value) {
		assert (max-min > 0)
		return (max-value)/(max-min)
	}
	
	def getPositiveScore (float max, float min, float value) {
		assert (max-min > 0)
		return (value-min)/(max-min)
	}

	
//*** STRATEGIC
	// Richard chagned on June 4th 2018 with using getWeight(metricid) 
	float S_PS () {
			
		float sum = 0
		float weight_sum = 0 
		//weight_sum = getFactorValue(0) + getFactorValue(1) + getFactorValue(2) + getFactorValue(3) + getFactorValue(4) + getFactorValue(5) + getFactorValue(6) + getFactorValue(7)

		weight_sum = getWeight("T-NPPS") + getWeight("T-EPS") + getWeight("T-PAS") + getWeight("T-HSS") + getWeight("T-NVS") + getWeight("T-NAS") + getWeight("T-DPS") + getWeight("O-I-MTBI")
		
		float MTBI_score = (100 - getMetricValue("O-I-MTBI"))*10/100	
		sum = getMetricValue("T-NPPS")*getWeight("T-NPPS") + getMetricValue("T-EPS")*getWeight("T-EPS") + getMetricValue("T-PAS")*getWeight("T-PAS") + getMetricValue("T-HSS")*getWeight("T-HSS") + getMetricValue("T-NVS")*getWeight("T-NVS") + getMetricValue("T-NAS")*getWeight("T-NAS") + getMetricValue("T-DPS")*getWeight("T-DPS") + MTBI_score*getWeight("O-I-MTBI")
		return sum/weight_sum
	}
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float S_DS () {
	
		float sum = 0
		float weight_sum = getWeight("T-TAS") + getWeight("T-TDS")
		
		sum += getMetricValue("T-TAS") * getWeight("T-TAS")
		sum += getMetricValue("T-TDS") * getWeight("T-TDS")
		
		return sum/weight_sum
	}
	
	float S_RS () {
	
		float sum = 0
		sum += getMetricValue("T-IRS")
		return sum
	}
	

//*** TACTICAL

	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_NPPS () {
		float sum = 0
		//float weight_sum = getFactorValue("O-N-MAPS") + getFactorValue("O-N-MWAPS") + getFactorValue("O-N-MIPS") + getFactorValue("O-I-MCME") + getFactorValue("O-I-MCMU")+ getFactorValue("O-I-MCNP")
		float weight_sum = getWeight("O-N-MAPS") + getWeight("O-N-MWAPS") + getWeight("O-N-MIPS") + getWeight("O-I-MCME") + getWeight("O-I-MCMU")+ getWeight("O-I-MCNP")
		
		float val_1 = getPositiveScore(10.0F, 0.0F, getMetricValue("O-N-MAPS"))
		float val_2 = getPositiveScore(10.0F, 0.0F, getMetricValue("O-N-MWAPS"))
		float val_3 = getPositiveScore(10.0F, 0.0F, getMetricValue("O-N-MIPS"))
		float val_4 = getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCME"))
		float val_5 = getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCMU"))
		float val_6 = getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCNP"))
		sum = val_1*getWeight("O-N-MAPS")/weight_sum + val_2*getWeight("O-N-MWAPS")/weight_sum + val_3*getWeight("O-N-MIPS")/weight_sum + val_4*getWeight("O-I-MCME")/weight_sum + val_5*getWeight("O-I-MCMU")/weight_sum + val_6*getWeight("O-I-MCNP")/weight_sum
		
		return sum*10
	}
	
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_EPS () {
		float sum = 0
		//float weight_sum = getFactorValue("O-U-MSDPS") + getFactorValue("O-U-MMDPS") + getFactorValue("O-I-MCMW") + getFactorValue("O-I-MCMD") + getFactorValue("O-I-MCSD")
		float weight_sum = getWeight("O-U-MSDPS") + getWeight("O-U-MMDPS") + getWeight("O-I-MCMW") + getWeight("O-I-MCMD") + getWeight("O-I-MCSD")

		float eval_1 = getPositiveScore(10.0F, 0.0F, getMetricValue("O-U-MSDPS"))
		float eval_2 = getPositiveScore(10.0F, 0.0F, getMetricValue("O-U-MMDPS"))
		float eval_3 = getPositiveScore(30.0F, 0.0F, getMetricValue("O-I-MCMW"))
		float eval_4 = getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCMD"))
		float eval_5 = getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCSD"))

		sum += eval_1*getWeight("O-U-MSDPS")/weight_sum
		sum += eval_2*getWeight("O-U-MMDPS")/weight_sum
		sum += eval_3*getWeight("O-I-MCMW")/weight_sum
		sum += eval_4* getWeight("O-I-MCMD")/weight_sum
		sum += eval_5*getWeight("O-I-MCSD")/weight_sum
		return sum*10
	}
	
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_PAS () {
		float sum = 0
		
		//float weight_sum = getFactorValue("O-A-MPACS") + getFactorValue("O-I-MPAV") 	
		float weight_sum = getWeight("O-A-MPACS") + getWeight("O-I-MPAV") 	
		
		//Richard Changed
		//[CSL 2019.06.04] added "/weight_sum" => fixed the value
		sum += ( getMetricValue("O-A-MPACS") * getWeight("O-A-MPACS") )/weight_sum + ( getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MPAV"))*10* getWeight("O-I-MPAV") )/weight_sum
		
		//println ( getMetricValue("O-A-MPACS") * getFactorValue(0)/weight_sum)
		//sum += getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MPAV"))*10* getFactorValue(1)/weight_sum
		//println ( getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MPAV"))*10* getFactorValue(1)/weight_sum)
		
		return sum	
	}
	
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_HSS () {
				
		// Richard changed with using getWeight on June 4th 2018
		//float weight_sum = getFactorValue("O-H-MHSS") + getFactorValue("O-I-MCSE") + getFactorValue("O-I-MCHR") 
		
		float weight_sum = getWeight("O-H-MHSS") + getWeight("O-I-MCSE") + getWeight("O-I-MCHR") 
		
		//assert (num_emp > 0)
		//float se_max = getMetricValue ("O-I-MCT") * 0.1
		//float hr_max = getMetricValue ("O-I-MCT") * 0.1
		
		//float sum = getPositiveScore(10.0F, 0.0F, getMetricValue("O-H-MHSS"))*getFactorValue(0)/weight_sum + getNegativeScore(se_max, 0.0F, getMetricValue("O-I-MCSE"))*getFactorValue(1)/weight_sum + getPositiveScore(hr_max, 0.0F, getMetricValue("O-H-MHSS"))*getFactorValue(2)/weight_sum
		
		// Richard changed with using getWeight on June 4th 2018
		float sum = ( ( (getPositiveScore(10.0F, 0.0F, getMetricValue("O-H-MHSS"))*getWeight("O-H-MHSS")/weight_sum) + (getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCSE"))*getWeight("O-I-MCSE")/weight_sum) + (getPositiveScore(20.0F, 0.0F, getMetricValue("O-I-MCHR"))*getWeight("O-I-MCHR")/weight_sum) )) * 10
		
		//float sum = (getPositiveScore(10.0F, 0.0F, getMetricValue("O-H-MHSS"))*getFactorValue(0) + getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCSE"))*getFactorValue(1) + getPositiveScore(20.0F, 0.0F, getMetricValue("O-I-MCHR"))*getFactorValue(2))/weight_sum
				
		return sum
	}
	
	
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_NVS () {
	
		float sum = 0
		//float weight_sum = getFactorValue(0) + getFactorValue(1) + getFactorValue(2) + getFactorValue(3) + getFactorValue(4)
		float weight_sum = getWeight("O-A-MAC") + getWeight("O-A-MAP") + getWeight("O-A-MVRS") + getWeight("O-A-MNVRS") + getWeight("O-I-MCNP")
				
		// Changed by Richard
		sum += getNegativeScore(10.0F, 0.0F, getMetricValue("O-A-MAC"))*10*getWeight("O-A-MAC")/weight_sum
		sum += getNegativeScore(10.0F, 0.0F, getMetricValue("O-A-MAP"))*10*getWeight("O-A-MAP")/weight_sum
		sum += getNegativeScore(100.0F, 0.0F, getMetricValue("O-A-MVRS"))*10*getWeight("O-A-MVRS")/weight_sum
		sum += getNegativeScore(10.0F, 0.0F, getMetricValue("O-A-MNVRS"))*10*getWeight("O-A-MNVRS")/weight_sum
		sum += getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCNP"))*10*getWeight("O-I-MCNP")/weight_sum
		
		return sum
	}
	
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_NAS () {
		float sum = 0
		//float weight_sum = getFactorValue("O-A-MAC") + getFactorValue("O-A-MAP") + getFactorValue("O-A-MACS") + getFactorValue("O-A-MNACS") + getFactorValue("O-I-MCNP")
		float weight_sum = getWeight("O-A-MAC") + getWeight("O-A-MAP") + getWeight("O-A-MACS") + getWeight("O-A-MNACS") + getWeight("O-I-MCNP")
	
		sum += getPositiveScore(10.0F, 0.0F, getMetricValue("O-A-MAC"))*getWeight("O-A-MAC")
		sum += getPositiveScore(10.0F, 0.0F, getMetricValue("O-A-MAP"))*getWeight("O-A-MAP")
		sum += getPositiveScore(10.0F, 0.0F, getMetricValue("O-A-MACS"))*getWeight("O-A-MACS")
		sum += getPositiveScore(10.0F, 0.0F, getMetricValue("O-A-MNACS"))*getWeight("O-A-MNACS")
		sum += getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCNP"))*getWeight("O-I-MCNP")
		//sum += getMetricValue("O-A-MACS")*getWeight("O-A-MACS")/weight_sum
		//println(sum)
		return sum*10/weight_sum	}
	
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_DPS () {
		float sum = 0
		//float weight_sum = getFactorValue("O-D-MDCS") + getFactorValue("O-D-MDIS") + getFactorValue("O-D-MDAS") + getFactorValue("O-I-MCDL")
		float weight_sum = getWeight("O-D-MDCS") + getWeight("O-D-MDIS") + getWeight("O-D-MDAS") + getWeight("O-I-MCDL")

		sum += getPositiveScore(10.0F, 0.0F, getMetricValue("O-D-MDCS"))*getWeight("O-D-MDCS") 
		sum += getPositiveScore(10.0F, 0.0F, getMetricValue("O-D-MDIS"))*getWeight("O-D-MDIS") 
		sum += getPositiveScore(10.0F, 0.0F, getMetricValue("O-D-MDAS"))*getWeight("O-D-MDAS")
		sum += getNegativeScore(30.0F, 0.0F, getMetricValue("O-I-MCDL"))*getWeight("O-I-MCDL")
		return sum*10/weight_sum

	}
	
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_TAS () {
	
		
		//float factorsum = getFactorValue(0) + getFactorValue(1) + getFactorValue(2) + getFactorValue(3)
		float factorsum = getWeight("O-T-IES") + getWeight("O-T-MTIA") + getWeight("O-T-MTIP") + getWeight("O-T-THES")

		float ies = getPositiveScore (10.0F, 0.0F, getMetricValue("O-T-IES")) * getWeight("O-T-IES")/factorsum
		float mtia = getNegativeScore(30.0F, 0.0F, getMetricValue("O-T-MTIA"))* getWeight("O-T-MTIA")/factorsum
		float mtip = getNegativeScore(30.0F, 0.0F, getMetricValue("O-T-MTIP"))* getWeight("O-T-MTIP")/factorsum
		float thes = getPositiveScore (10.0F, 0.0F, getMetricValue("O-T-THES")) * getWeight("O-T-THES")/factorsum
		
		return (ies + mtia + mtip + thes)*10

	}

	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_TDS () {

		//float factorsum = getFactorValue(0) + getFactorValue(1) + getFactorValue(2) + getFactorValue(3)+ getFactorValue(4) + getFactorValue(5)
		float factorsum = getWeight("O-T-MITP") + getWeight("O-E-METP") + getWeight("O-T-THTP") + getWeight("O-I-MTTD") + getWeight("O-I-MCMSI") + getWeight("O-I-MCH")

		float mitp = getPositiveScore (1.0F, 0.0F, getMetricValue("O-T-MITP")) * getWeight("O-T-MITP")/factorsum
		//println ( getMetricValue("O-T-MITP")+ " " + mitp)
		
		float metp = getPositiveScore(1.0F, 0.0F, getMetricValue("O-E-METP"))* getWeight("O-E-METP")/factorsum
		//println ( getMetricValue("O-E-METP")+ " " + metp )
		
		float thtp = getPositiveScore(1.0F, 0.0F, getMetricValue("O-T-THTP"))* getWeight("O-T-THTP")/factorsum
		//println (  getMetricValue("O-T-THTP")+ " " + thtp)

		float mttd = getNegativeScore (60F, 0.0F, getMetricValue("O-I-MTTD")) * getWeight("O-I-MTTD")/factorsum
		//println ( getMetricValue("O-I-MTTD")+ " " + mttd)

		float mcmsi = getNegativeScore (10.0F, 0.0F, getMetricValue("O-I-MCMSI")) * getWeight("O-I-MCMSI")/factorsum
		//println ( getMetricValue("O-I-MCMSI")+ " " + mcmsi)

		float mch =getNegativeScore (10.0F, 0.0F, getMetricValue("O-I-MCH")) * getWeight("O-I-MCH")/factorsum
		//println ( getMetricValue("O-I-MCH")+ " " + mch + " "  + getFactorValue (5) + " " + factorsum)

		return (mitp + metp + thtp+ mttd + mcmsi + mch)*10

	}
	
	// Richard chagned on June 4th 2018 with using getWeight(metricid)
	float T_IRS () {
		float sum = 0
		//float weight_sum = getFactorValue("O-I-MTTC") + getFactorValue("O-I-MTR") + getFactorValue("O-I-MTTA") + getFactorValue("O-I-MCRM") + getFactorValue("O-I-MCRX")
		float weight_sum = getWeight("O-I-MTTC") + getWeight("O-I-MTR") + getWeight("O-I-MTTA") + getWeight("O-I-MCRM") + getWeight("O-I-MCRX")
		sum = getNegativeScore(30.0F, 1.0F, getMetricValue("O-I-MTTC"))*getWeight("O-I-MTTC") + getNegativeScore(365.0F, 1.0F, getMetricValue("O-I-MTR"))*getWeight("O-I-MTR") + getNegativeScore(7.0F, 0.0F, getMetricValue("O-I-MTTA"))*getWeight("O-I-MTTA") + getNegativeScore(2920.0F, 0.0F, getMetricValue("O-I-MCRM"))*getWeight("O-I-MCRM") + getNegativeScore(292000.0F, 0.0F, getMetricValue("O-I-MCRX"))*getWeight("O-I-MCRX")
		sum = sum*10/weight_sum
		return sum

		/* float c_min = 1
		float c_max = 30
		float r_min = 1
		float r_max = 365
		float a_min = 1
		float a_max = 7
		float m_min = 0
		float m_max = 2920
		float x_min = 1
		float x_max = 292000
		sum += getTScore(getMetricValue("O-I-MTTC"), c_min, c_max)*getFactorValue(0)
		sum += getTScore(getMetricValue("O-I-MTR"), r_min, r_max)*getFactorValue(1)
		sum += getTScore(getMetricValue("O-I-MTTA"), a_min, a_max)*getFactorValue(2)
		sum += getTScore(getMetricValue("O-I-MCRM"), m_min, m_max)*getFactorValue(3)
		sum += getTScore(getMetricValue("O-I-MCRX"), x_min, x_max)*getFactorValue(4)

		return sum*10/weight_sum */
	}

//*** OPERATIONAL

//** ASSET

	def O_A_MAC () {
						
		def contents = getDataPointList("Asset")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
				
		def sum = 0  
		def num_asset = contents.size()
		def max_asset = contents.A01.max()
		for(asset in contents) {
					
			float conn = asset.A01 			
			sum = sum + conn/max_asset
			
		}
		return sum*10/num_asset
		
	}
	
	
	def O_A_MAP () {
	
		def contents = getDataPointList("Asset")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
				
		def seg_val = getFactorValue(0)	
				
		float sum1, sum2 = 0 
		def prox_internet = 0
		def prox_internal = 0
		for(asset in contents) {
			if (asset.A07 == -1 && asset.A04 != -1) {
				prox_internal = asset.A04
				sum1 = sum1 + (prox_internal)/contents.size()
			}
			else if(asset.A07 != -1 && asset.A04 == -1){
				prox_internet = asset.A07 + seg_val
				sum2 = sum2 + (prox_internet)/contents.size()
			}
		}	
		return (sum1 + sum2)
	}
	
	
	
	def O_A_MVRS () {
		
		def assets = getDataPointList("Asset")
		if( assets == null || assets.isEmpty()){
			return RESULT_FAIL
		}
				
		def vulns = getDataPointList("Vulnerability")
		if( vulns == null || vulns.isEmpty()){
			return RESULT_FAIL
		}
						
		float min_score = 0
		float sum = 0
		avrs_map = [:]
				
		for(a in assets) {
			float v_sum = 0
			for (v in vulns) {
				if ((v.V02 > 0 ) && (v.V03 == a.A20)) {
					v_sum += v.V02
				}
			}
			float max_num_vuln_per_asset = getFactorValue(0)
			float score = getPositiveScore(max_num_vuln_per_asset, min_score, v_sum) 
			avrs_map.put(a.A20,score)
			//println a.A20 + ":" + score
			sum = sum + score
		
		}			
		return sum*10/assets.size()
		
	}
	
		
	def O_A_MNVRS () {
	
		def assets = getDataPointList("Asset")
		if( assets == null || assets.isEmpty()){
			return RESULT_FAIL
		}
		def vulns = getDataPointList("Vulnerability")
		if( vulns == null || vulns.isEmpty()){
			return RESULT_FAIL
		}
				
		double sum = 0
		double nvrs = 0
		double avrs = 0
		double rvrs = 0
		double log_rvrs = 0
		double norm = 0
				
		float max_prox = assets.A04.max()
				
		for(a in assets) {				
			double conn_rate = a.A01/assets.size()
			double prox_rate = a.A04/max_prox
			double risk_rate = conn_rate/(prox_rate + 1)
			def value = avrs_map.get(a.A20,0)
			if (value == null) {
				println "cannot find vuln risk score for asset: " + a.A20
				return RESULT_FAIL
			}
			avrs = value
			log_rvrs = Math.log10(risk_rate + 1) + Math.log10(avrs + 1)
			norm = log_rvrs/(2*Math.log10(2))
			//println a.A20 + ":" + norm
			nvrs_map.put(a.A20,norm)
			sum += norm		
		}
			
		return sum*10/assets.size()
	}
	
	def O_A_MACS () {
		//[AA : 04/25/2018] - Changed variable names and corrected weights 		
		float w_unauth = 0.7
		float w_default = 0.9
		float w_privacct = 0.5
		float w_sharedaccts = 0.7
		float w_sharedpriv = 0.8
		float w_nopassexp = 0.6
		float w_passexp = 0.4
		float w_sum = w_unauth + w_default + w_privacct + w_sharedaccts + w_sharedpriv + w_nopassexp + w_passexp
				
		float sum = 0, racs_score = 0, default_score = 0, privacct_score = 0, sharedaccts_score = 0, sharedpriv_score = 0, nopassexp_score = 0, passexp_score = 0
				
		def assets = getDataPointList("Asset")
		if( assets == null || assets.isEmpty()){
			return RESULT_FAIL
		}
				
		float un_auth = 0, crit_rate = 0, un_auth_score = 0, acs_score = 0
		float max_crit = 3
		float num_users = getFactorValue(0)
		def max_default = getFactorValue(1)
		acs_map = [:]
				
		for(a in assets){
			un_auth = a.A15 - a.A16
			crit_rate = a.A08/max_crit
			un_auth_score = getNegativeScore(num_users, 0.0F, un_auth)
			default_score = getNegativeScore(max_default, 0.0F, a.A09)
			privacct_score = getNegativeScore(num_users, 0.0F, a.A10)
			sharedaccts_score = getNegativeScore(num_users, 0.0F, a.A11)
			sharedpriv_score = getNegativeScore(num_users, 0.0F, a.A12)
			nopassexp_score = getNegativeScore(num_users, 0.0F, a.A13)
			passexp_score = getNegativeScore(num_users, 0.0F, a.A14)
			acs_score = (w_unauth*(un_auth_score) + w_default*(default_score) + w_privacct*(privacct_score) + w_sharedaccts*(sharedaccts_score) + w_sharedpriv*(sharedpriv_score) + w_nopassexp*(nopassexp_score) + w_passexp*(passexp_score))/w_sum
			racs_score = acs_score*crit_rate
			sum = sum + racs_score
			acs_map.put(a.A20,racs_score)
		}
		return sum*10/assets.size()
				
	}

	def O_A_MNACS () {
		//[AA: 04/25/2018] - Added crit_rate and max_crit
		def assets = getDataPointList("Asset")
		if( assets == null || assets.isEmpty()){
			return RESULT_FAIL
		}
				
		def acs_score = 0
		float max_prox = assets.A04.max()
		float sum = 0
		float nacs = 0
		float max_crit = 3
		nacs_map = [:]
		for(a in assets) {
			float conn_rate = a.A01/assets.size()
			float prox_rate = a.A04/max_prox
			float risk_rate = conn_rate/(prox_rate + 1)
			float crit_rate = a.A08/max_crit
			acs_score = acs_map.get(a.A20,0)
			if (acs_score == null) {
				println "cannot find access control score for asset: " + a.A20
				return RESULT_FAIL
			}
			nacs = acs_score*risk_rate*crit_rate
			sum += nacs
			//println a.A20 + ":" + nacs
			nacs_map.put(a.A20,nacs)
		}
		return sum*10/assets.size()
	}
	
	def O_A_MPACS () {
		def assets = getDataPointList("Asset")
				
		if( assets == null || assets.isEmpty()){
			return RESULT_FAIL
		}
		def max_crit = 3
		def max_num_emp = getFactorValue(1)
		def max_barriers = getFactorValue(0)
				
		float sum, un_auth = 0
		for(a in assets) {
			if (un_auth < 0) un_auth = 0
			else un_auth = a.A18 - a.A17
			
			float barrier_score = a.A19/ max_barriers
			float crit_rate = a.A08/max_crit
			float un_auth_score = (max_num_emp - un_auth)/max_num_emp
			float pacs_score = un_auth_score*barrier_score*crit_rate
			sum = sum + pacs_score
							
		}
		// save it for later
		//num_emp =  num_employees		
		return sum*10/assets.size()
	}

//** DATABASE

	def O_D_MDCS () {
	
		def dbs = getDataPointList("Database")
		if( dbs == null || dbs.isEmpty()){
			return RESULT_FAIL
		}		
		def crit_high = 1
		def crit_med = 0.5
		def crit_low = 0
		def max_crit = crit_high
				
		def sum = 0
		float d1_val = 0, d2_val = 0, d3_val = 0, enc = 0, acc = 0, vun = 0, cric = 0, log_dcs = 0, norm = 0
				
		for(d in dbs) {
			if(d.D01 == Database.Level.High) cric = crit_high
			else if (d.D01 == Database.Level.Med) cric = crit_med
			else if (d.D01 == Database.Level.Low) cric = crit_low
			else cric = 0
				       
			if(d.D02 == BoolEx.Yes) d2_val = 1
			else d2_val = 0
					
			if(d.D03 == BoolEx.Yes) d3_val = 1
			else d3_val = 0
			enc = (d2_val + d3_val)/2
					
			def nacs = nacs_map.get(d.D11,0)
			if (nacs == null) {
				println "cannot find network access control score for asset: " + d.D11
				return RESULT_FAIL
			}
			acc = nacs
					
			def nvrs = nvrs_map.get(d.D11,0)
			if (nvrs == null) {
				println "cannot find network vuln risk score for asset: " + d.D11
				return RESULT_FAIL
			}
			if (nvrs < 1) vun = 1 - nvrs
			else vun = nvrs
					
			log_dcs = (Math.log10(enc + 1)+ Math.log10(acc + 1)+ Math.log10(vun + 1)+ Math.log10(cric + 1))/(Math.log10(16)) 				
			sum = sum + log_dcs
		}
		return sum*10/dbs.size()
	}
	
	def O_D_MDIS () {
				
		def dbs = getDataPointList("Database")
		if( dbs == null || dbs.isEmpty()){
			return RESULT_FAIL
		}
				
		def crit_high = 1
		def crit_med = 0.5
		def crit_low = 0
		def max_crit = crit_high
				
		def rev_formal = 1
		def rev_informal = 0.5
		def max_review = rev_formal
				
		float sum = 0
		float cric = 0
		float seg = 0
		float intg_score = 0
		float aud_trl = 0
		float rev = 0 
		float norm = 0
	
		for(d in dbs) {
			if(d.D01 == Database.Level.High) cric = crit_high
			else if (d.D01 == Database.Level.Med) cric = crit_med
			else if (d.D01 == Database.Level.Low) cric = crit_low
			else cric = 0
		
			if (d.D08 == BoolEx.Yes) aud_trl = 1
			else aud_trl = 0
			if (d.D09 == "formal") rev = rev_formal
			else if (d.D09 == "informal") rev = rev_informal
			else rev = 0
			if (d.D10 == BoolEx.Yes) seg = 1
			else seg = 0
					
			intg_score = (Math.log10(cric+1)+ Math.log10(aud_trl + 1)+ Math.log10(seg + 1)+ Math.log10(rev + 1))/(4*Math.log10(2))		
			sum = sum + intg_score		
		}
		return sum*10/dbs.size()
	}
	
	def O_D_MDAS () {
	
		def dbs = getDataPointList("Database")
		if( dbs == null || dbs.isEmpty()){
			return RESULT_FAIL
		}
			
		def crit_high = 1
		def crit_med = 0.5
		def crit_low = 0
		def max_crit = crit_high
		
		// on_change, hourly, daily, weekly, monthly, none, NA
		def bk_onchange = 5
		def bk_hourly = 4
		def bk_daily = 3
		def bk_weekly = 2
		def bk_monthly = 1
		def max_backup = bk_onchange
				
		float score, sum, redn, seg, norm, cric, freq, d1_val = 0
				
		for(d in dbs) {
		
			if(d.D01 == Database.Level.High) cric = crit_high
			else if (d.D01 == Database.Level.Med) cric = crit_med
			else if (d.D01 == Database.Level.Low) cric = crit_low
			else cric = 0
		
			if(d.D06 == BoolEx.Yes) redn = 1
			else redn = 0
					
			if(d.D07 == "On_Change") freq = bk_onchange/max_backup
			else if (d.D07 == "Hourly") freq = bk_hourly/max_backup
			else if (d.D07 == "Daily") freq = bk_daily/max_backup
			else if (d.D07 == "Weekly") freq = bk_weekly/max_backup
			else if (d.D07 == "Monthly") freq = bk_monthly/max_backup
		
			score = (Math.log10(cric + 1)+ Math.log10(redn + 1)+ Math.log10(freq + 1))/(3*Math.log10(2)) 
			sum = sum + score
		}
		return sum*10/dbs.size()
	}

//** INCIDENT

	def O_I_MCT () {
		
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
			
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return RESULT_FAIL
			
		def sum = 0
		map.each { entry ->
			def l = entry.value
			sum += l.size()
		}	
		return sum/12
	}
	
	def O_I_MCH () {

		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return RESULT_FAIL
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I15 == Incident.Level.High) count++
			}
			sum += count
		}	
		return sum/12	
	}
	
	def O_I_MCM () {
			
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
				
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I15 == Incident.Level.Med) count++
			}
			sum += count
		}	
		return sum/12
	}
	
	def O_I_MCMSI () {
			
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if (i.I18 == Incident.CategoryOfI18.malfunction_of_resource || i.I18 == Incident.CategoryOfI18.adversary_notification ||
				i.I18 == Incident.CategoryOfI18.law_enforcement_compromise_notification || i.I18 == Incident.CategoryOfI18.non_security_staff_report )
				count++
			}
			sum += count
		}	
		return sum/12
	}
	
	def O_I_MCHR () {
			
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if (i.I18 == Incident.CategoryOfI18.non_security_staff_report) count++
			}
			sum += count
		}	
		return sum/12

	}


	def O_I_MCDL () {
	
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
			
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I07 == BoolEx.Yes) count++
			}
		sum += count
		}	
		
		return sum/12
	}

	def O_I_MCME () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
			
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I11 == BoolEx.Yes) count++
			}
		sum += count
		}	
		return sum/12
	}

	def O_I_MCMU () {
		
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I12 == BoolEx.Yes) count++
			}
			sum += count
		}	
		
		return sum/12

	}

	def O_I_MCMW () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I09 == BoolEx.Yes) count++
			}
			sum += count
		}	
		return sum/12
	}
	def O_I_MCNP () {
						
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I06 == BoolEx.Yes) count++
				else count = 0
			}
			sum += count
		}	
		return sum/12
	}
	def O_I_MCMD () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I10 == BoolEx.Yes) count++
			}
		sum += count
		}	
		
		return sum/12
	}


	def O_I_MCSD () {
			
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I14 == BoolEx.Yes) count++
			}
			sum += count
		}	
		
		return sum/12

	}

	def O_I_MCSE () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I08 == BoolEx.Yes) count++
			}
		sum += count
		}	
		
		return sum/12
	}

	def O_I_MPAV () {
	
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty())
		{
			return RESULT_FAIL
		}
		Map map	= [:]
		for (i in contents) {
			def l = map.get (i.i01.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [i]
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(i)
				map.put(i.i01.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def count = 0
			for (i in l) {
				if(i.I13 == BoolEx.Yes) count++
			}
			sum += count
		}	
		
		return sum/12
	}

	def O_I_MTTD () {
			
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty()){
				return RESULT_FAIL
		}
		def sum = 0
		for (i in contents) {
			use(groovy.time.TimeCategory)
			{
				def diff = i.I01 - i.I02
				if(diff.days < 0) diff.days = diff.days + 31
				else diff = i.I01 - i.I02
				sum = sum + diff.days
			}
		}		
		return sum/contents.size()
	}


	def O_I_MTBI () {
		
	
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
				
		//float noticed = getFactorValue(0)
		def noticed = 0
				
				
		List dateList = new LinkedList()
				
		for (i in contents) {
			if (noticed == 0) {	
				dateList.add (i.I02)
				//println ("added-->" + i.I02)
			}
			else { 
				dateList.add(i.I01)
				//println ("added-->" + i.I01)
			}
		}	
				
		dateList.sort()
				
		def sum = 0
		def top = dateList.get(0)
				
		for (d in dateList) {
					
			def diff_mtbi = d - top
			if (diff_mtbi > 0) 
			sum += diff_mtbi
			//println 'diff=' + diff
			top = d
		}		
				
		if (dateList.size ==1) return -2
				
		return sum/(dateList.size()-1)
	}

	def O_I_MTTC () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		def sum =0
		for (i in contents) {
			use(groovy.time.TimeCategory){
				def diff_mttc = i.I04 - i.I01
				if(diff_mttc.days < 0) diff_mttc.days = diff_mttc.days + 31
				else diff_mttc = i.I04 - i.I01
				sum = sum + diff_mttc.days
			}
		}		
		return sum/contents.size()			
	}


	def O_I_MTR () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		def sum = 0
		for (i in contents) {
			use(groovy.time.TimeCategory){
				def diff_mtr = i.I05 - i.I01
				if(diff_mtr.days < 0) diff_mtr.days = diff_mtr.days + 31
				else diff_mtr = i.I05 - i.I01
				
				sum = sum + diff_mtr.days    			
				}
		}
		
		return sum/contents.size()
	}
	
	def O_I_MTTA () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		def sum = 0
		for (i in contents) {
			use(groovy.time.TimeCategory){
				def diff_mtta = i.I03 - i.I01
				if (diff_mtta.days < 0) diff_mtta.days = diff_mtta.days + 31
				else diff_mtta = i.I03 - i.I01
				sum = sum + diff_mtta.days
			}
		}		
		return sum/contents.size()			
	}						
	def O_I_MCRM () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		def sum = 0
		for (i in contents) {
			sum += i.I16
		}		
		return sum/contents.size()		
	}
	
	def O_I_MCRX () {
		def contents = getDataPointList("Incident")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		def sum = 0
		for (i in contents) {
			sum += i.I17
		}		
		return sum/contents.size()	
	}
	
//** NETWORK

	def O_N_MAPS () {
	
		def droppedRateWeight = 1
		def alertRateWeight = 2
		def probRateWeight = 3
		def dosWeight  = 4
		def intrusionWeight = 4
		def humanIntWeight = 3
		def NACWeight = 4
		def encrptweight = 4
		def antennaweight = 3
		def freqweight = 1
		def wirelessnacwt = 4
		def freq_w = 1
		def encrypt_wpa_value = 1
		def encrypt_wpa2_pre_val = 3
		def encrypt_wpa2_ent_val = 5
		def nap_omni_dir_value = 0
		def nap_dir_value = 2
		def nap_p2p_value = 3
		def nap_cdma_value = 1
		def nap_cogn_value = 1
		def nap_afh_value = 1
		float droppedRateScore, alertRateScore, probRate, dosScore, intrusionScore, humanIntScore, NACScore, wirel_encrypt_score, atype_score, freq_score  = 0     
		def weight_sum = (droppedRateWeight + alertRateWeight + probRateWeight + dosWeight + intrusionWeight + humanIntWeight + NACWeight)

		def contents = getDataPointList("NetworkAccessPoint")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}

		def (count, sum, value, val2, val3, val4, apScore) = [0, 0, 0, 0, 0, 0, 0]

		for (ap in contents) {
			if (ap.n10 == BoolEx.No) {
				if (ap.n01 == 0 || ap.n01 == -1 || ap.n03 == -1) droppedRateScore = getNegativeScore(1.0, 0.0, 0.0)
				else droppedRateScore = getNegativeScore(1.0, 0.0, (ap.n03/ap.n01))
				
				if (ap.n04 == 0 || ap.n04 == -1 || ap.n05 == -1) alertRateScore = getNegativeScore(1.0, 0.0, 0.0)
				else alertRateScore = (getNegativeScore(1.0, 0.0, (ap.n05/ap.n04))) 
				
				if (ap.n01 == 0 || ap.n01 == -1 || ap.n06 == -1) probRate = getNegativeScore(0.05, 0.0, 0.0)
				else probRate = (getNegativeScore(0.05, 0.0, (ap.n06/ap.n01)))
				
				if (ap.n07 == -1) dosScore = getNegativeScore(10, 0.0, 0.0) 
				else dosScore = (getNegativeScore(10, 0.0, (ap.n07)))
				
				if (ap.n08 == -1) intrusionScore = getNegativeScore(10, 0.0, 0.0)
				else intrusionScore = getNegativeScore(10, 0.0, (ap.n08))
				
				if (ap.n09 == -1) humanIntScore = getNegativeScore(10, 0.0, 0.0)
				else humanIntScore = getNegativeScore(10, 0.0, ap.n09)
				
				if (ap.n16 == BoolEx.Yes) value = 1.0
				else value = 0.0
				NACScore = getPositiveScore(1.0, 0.0, value)
				
				apScore = ((droppedRateScore*droppedRateWeight + alertRateScore*alertRateWeight + probRateWeight*probRate + dosScore*dosWeight +intrusionScore*intrusionWeight 
				+ humanIntScore*humanIntWeight + NACScore*NACWeight)/weight_sum)
				sum = sum + apScore
				count ++
			}
			
		}
		if (count == 0) return init_value
		else return sum*10/count
}

	def O_N_MWAPS () {
		
		//weighting
		def droppedRateWeight = 1
		def alertRateWeight = 2
		def probRateWeight = 3
		def dosWeight  = 4
		def intrusionWeight = 4
		def humanIntWeight = 3
		def NACWeight = 4
		def crypt_w = 4
		def atype_w = 3
		def freq_w = 1
				
		def weightSum = (droppedRateWeight + alertRateWeight + probRateWeight + dosWeight + intrusionWeight + humanIntWeight + NACWeight + crypt_w + atype_w + freq_w)
				
		//value mapping
		def encrypt_wpa_value = 1
		def encrypt_wpa2_pre_val = 3
		def encrypt_wpa2_ent_val = 5
		def nap_omni_dir_value = 0
		def nap_dir_value = 2
		def nap_p2p_value = 3
		def nap_cdma_value = 1
		def nap_cogn_value = 1
		def nap_afh_value = 1
		
		def contents = getDataPointList("NetworkAccessPoint")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		
		def (sum, count, val1, val2, val3, val4, val5, freq_s, apScore) = [0, 0, 0, 0, 0, 0, 0, 0, 0]
		def (droppedRateScore, alertRateScore, probRate, dosScore, intrusionScore, humanIntScore, NACScore, crypt_s, atype_s ) = [0, 0, 0, 0, 0, 0, 0, 0, 0]
				
		for (ap in contents) {
			//[2018.01.31 AA] Addedd conditions for null values 
			if (ap.n10 == BoolEx.Yes) {
		 
				if (ap.n01 == 0 | ap.n01 == -1 | ap.n03 == -1) droppedRateScore = getNegativeScore(1.0, 0.0, 0.0)
				else droppedRateScore = getNegativeScore(1.0, 0.0, (ap.n03/ap.n01))
					
				if (ap.n04 == 0 | ap.n04 == -1 | ap.n05 == -1) alertRateScore = getNegativeScore(1.0, 0.0, 0.0)
				else alertRateScore = getNegativeScore(1.0, 0.0, (ap.n05/ap.n04))
						
				if (ap.n01 == 0 | ap.n01 == -1 | ap.n06 == -1) probRate = getNegativeScore(0.05, 0.0, 0.0)
				else probRate = getNegativeScore(0.05, 0.0, (ap.n06/ap.n01))
					
				if (ap.n07 == 0 | ap.n07 == -1) dosScore = getNegativeScore(10.0, 0.0, 0.0)
				else dosScore = getNegativeScore(10.0, 0.0, ap.n07)
						
				if (ap.n08 == 0 | ap.n08 == -1) intrusionScore = getNegativeScore(10.0, 0.0, 0.0)
				else intrusionScore = getNegativeScore(10.0, 0.0, ap.n08)
						
				if (ap.n09 == 0 | ap.n09 == -1) humanIntScore = getNegativeScore(10.0, 0.0, 0.0)
				else humanIntScore = getNegativeScore(10.0, 0.0, ap.n09)
						
				if (ap.N16 == BoolEx.Yes) val1 = 1.0
				else val1 = 0
				NACScore = getPositiveScore(1.0, 0.0, val1)
				
				if (ap.n13 == NetworkAccessPoint.Encryption.WPA) val2 = encrypt_wpa_value
				else if (ap.n13 ==  NetworkAccessPoint.Encryption.WPA2_Preshared) val2 = encrypt_wpa2_pre_val
				else if (ap.n13 == NetworkAccessPoint.Encryption.WPA2_Enterprise) val2 = encrypt_wpa2_ent_val
				else val2 = 0
				crypt_s = getPositiveScore(5, 0 , val2)
				
				if (ap.n14 == NetworkAccessPoint.AntennaType.Omni_directional) val3 = nap_omni_dir_value
				else if (ap.n14 == NetworkAccessPoint.AntennaType.directional) val3 = nap_dir_value
				else if (ap.n14 == NetworkAccessPoint.AntennaType.point_to_point) val3 = nap_p2p_value
				else val3 = 0
				atype_s = getPositiveScore(3, 0 , val3)
				if (ap.n15 == NetworkAccessPoint.Frequency.FH_CDMA) val4 = nap_cdma_value
				else if (ap.n15 == NetworkAccessPoint.Frequency.AFH) val4 = nap_afh_value
				else if (ap.n15 == NetworkAccessPoint.Frequency.Cognitive) val4 = nap_cogn_value
				else val4 = 0
				freq_s = getPositiveScore(1, 0 , val4)
				apScore = (droppedRateScore*droppedRateWeight + alertRateScore*alertRateWeight + probRate*probRateWeight + dosScore*dosWeight +intrusionScore*intrusionWeight + humanIntScore*humanIntWeight + NACScore*NACWeight + crypt_s*crypt_w + atype_s*atype_w + freq_s*freq_w)/weightSum
				sum += apScore
				count ++
			}
		}
		if (count == 0) return init_value
		return sum*10/count
	}


	def O_N_MIPS () {
	
		float email_w = getFactorValue(0)
		float proxy_w = getFactorValue(1)
		float max_spam_rate = getFactorValue(2)
		float max_phishing_rate = getFactorValue(3)
		float max_malware_rate = getFactorValue(4)
		float max_data_leak_rate = getFactorValue(5)
				
		float miss_rate = 0.1
				
		float filter_w = 5
		float spam_w = 2
		float phishing_w = 2
		float malware_w  = 2
		float spam_miss_w = 4
		float phishing_miss_w = 5
		float malware_miss_w = 5
		float data_loss_w = 4
				
		float sn_w = 1
		float proxyrate_w = 3
		float private_email_w = 1
		float cloud_w  = 1
		
		def mail_w_sum = ( filter_w + spam_w + phishing_w + malware_w + spam_miss_w + phishing_miss_w + malware_miss_w + data_loss_w)
		def prx_w_sum = ( proxyrate_w + sn_w + private_email_w + cloud_w)
		
		def filters = getDataPointList("EmailServerFilter")
		if( filters == null || filters.isEmpty()){
			return RESULT_FAIL
		}
				
		float e_sum, mail_score, spam_miss_s, malware_miss_s, filter_s, spam_s, phishing_s, malware_s, data_loss_s, phishing_miss_s = 0
		//[2018.01.31 AA] Addedd conditions for null values 
		for (f in filters) {
			if (f.M01 == 0 | f.M01 == -1 | f.M03 == -1) filter_s = getPositiveScore(1.0F, 0.0F,0.0F)
			else filter_s = getPositiveScore(1.0F, 0.0F, (float)(f.M03/f.M01))
			
			
			if (f.M01 == 0 | f.M01 == -1 | f.M04 == -1) spam_s = getPositiveScore(max_spam_rate, 0.0F, 0.0F)
			else spam_s = getPositiveScore(max_spam_rate, 0.0F, (float)(f.M04/f.M01))
			
			if (f.M01 == 0 | f.M01 == -1 | f.M05 == -1) phishing_s = getPositiveScore(max_phishing_rate, 0.0F,0.0F)
			else phishing_s = getPositiveScore(max_phishing_rate, 0.0F, (float)(f.M05/f.M01))
			
			if (f.M01 == 0 | f.M01 == -1 | f.M06 == -1) malware_s = getPositiveScore(max_malware_rate, 0.0F, 0.0F)
			else malware_s = getPositiveScore(max_malware_rate, 0.0F, (float)(f.M06/f.M01))
			
			if (f.M07 == -1 | f.M01 == 0 | f.M01 == -1) spam_miss_s = getNegativeScore((float)(max_spam_rate*miss_rate), 0.0F, 0.0F)
			else spam_miss_s = getNegativeScore((float)(max_spam_rate*miss_rate), 0.0F, (float)(f.M07/f.M01))
			
			if (f.M08 == -1 | f.M01 == 0 | f.M01 == -1) phishing_miss_s = getNegativeScore((float)(max_phishing_rate*miss_rate), 0.0F, 0.0F)
			else phishing_miss_s = getNegativeScore((float)(max_phishing_rate*miss_rate), 0.0F, (float)(f.M08/f.M01))
			
			if (f.M09 == -1 | f.M01 == 0 | f.M01 == -1) malware_miss_s = getNegativeScore((float)(max_malware_rate*miss_rate), 0.0F, 0.0F)
			else malware_miss_s =  getNegativeScore((float)(max_malware_rate*miss_rate), 0.0F, (float)(f.M09/f.M01))
			
			if (f.M10 == -1 | f.M01 == 0 | f.M01 == -1) data_loss_s = getPositiveScore (max_data_leak_rate , 0.0F, 0.0F)
			else data_loss_s = getPositiveScore(max_data_leak_rate , 0.0F, (float)(f.M10/f.M01))
			
			float m_score = (filter_s*filter_w + spam_s*spam_w + phishing_s*phishing_w + malware_s*malware_w + spam_miss_s*spam_miss_w + phishing_miss_s*phishing_miss_w + malware_miss_s*malware_miss_w + data_loss_s*data_loss_w)/mail_w_sum
			e_sum = e_sum + m_score
		}
		mail_score = e_sum/filters.size()
		
				
		def proxies = getDataPointList("WebProxy")
		if( proxies == null || proxies.isEmpty()){
			return RESULT_FAIL
		}
				
		float sn_s, private_email_s, cloud_s, p_sum, proxy_score, p_score, proxyrate = 0
				
		for (p in proxies) {
			proxyrate = getPositiveScore(1.0F, 0.0F, (float)(p.W01/100))
			
			if (p.W02 == BoolEx.Yes) sn_s = 1
			else sn_s = 0
			
			if (p.W03 == BoolEx.Yes) private_email_s = 1
			else private_email_s = 0
			
			if (p.W04 == BoolEx.Yes) cloud_s = 1
			else cloud_s = 0
			
			p_score = (proxyrate*proxyrate_w + sn_s*sn_w + private_email_s*private_email_w + cloud_s*cloud_w)/prx_w_sum 
			p_sum = p_sum + p_score  
		}		
		proxy_score = p_sum/proxies.size()
		return (mail_score*email_w + proxy_score*proxy_w)*10/(email_w + proxy_w) 
	}	


//** HUMAN

	 def O_H_MHSS () {
		
		
		def priv_w = 3
		def phy_w = 1
		def read_w = 2
		def write_w  = 3
		def multiplier = 10.01
		def (sum, n_sum, priv_s, phy_s, read_s, write_s, train, risk_s, hs_score, bu_score) = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		def perm_less1_week_value = 4
		def perm_less1_month_value = 3
		def perm_less3_months_value = 2
		def perm_more3_month_value = 1
		float max_perm = perm_less1_week_value
		def contents = getDataPointList("Personnel")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		def bu = getDataPointList("BusinessUnit")
		if( bu == null || bu.isEmpty()){
			return RESULT_FAIL
		}
		//def map = new LinkedHashMap()
		double b_score = 0
		def bu_map = [:]
		for (b in bu) {
			b_score = ((1-b.b03)*b.b02)/(b.b01)
			bu_map.put(b.T09,b_score)
			for (p in contents) {
				if (p.P02 == BoolEx.Yes) priv_s = 1
				else priv_s = 0
				if (p.P03 == BoolEx.Yes) phy_s = 1
				else phy_s = 0
				if (p.P04 == BoolEx.Yes) read_s = 1
				else read_s = 0
				if (p.P05 == BoolEx.Yes) write_s = 1
				else write_s = 0
				risk_s = (priv_s*priv_w + phy_s*phy_w + read_s*read_w + write_s*write_w)/(priv_w + phy_w + read_w + write_w)
				
				if (p.P01 == Personnel.Term.Less_Than_One_Week) train = perm_less1_week_value
				else if (p.P01 == Personnel.Term.Less_Than_One_Month) train = perm_less1_month_value
				else if (p.P01 == Personnel.Term.Less_Than_Three_Months) train = perm_less3_months_value
				else if (p.P01 == Personnel.Term.More_Than_Three_Months) train = perm_more3_month_value
				else train = 0
				
							
				if (bu_map == null) return RESULT_FAIL
				if (bu_map.size() <= 0) return RESULT_FAIL
				bu_map.each { entry ->
					def l = entry.key
					def v = entry.value
					for (p06 in l) {
						bu_score = v 
					}
				}
				hs_score = (Math.log10((train/max_perm) + 1) + Math.log10(bu_score + 1) + Math.log10(risk_s + 1))/Math.log10(8)
				sum = sum + hs_score				
			}	
		}
		
		n_sum = sum*10
		
		return n_sum/contents.size()
		
		
	} 

//** USER DEVICE

	//def msdps = -1
	def O_U_MSDPS () {
		
		//weighting
		def update_w = 1
		def scan_w = 1
		def app_w = 1
		def file_w  = 1
		def float hids_w = 0.33
		def access_w = 2
		def w_sum = update_w + scan_w + app_w + file_w + hids_w + access_w
				
		// value mappings
		def min_upfreq_value = 5
		def hour_upfreq_value = 4
		def day_upfreq_value = 3
		def week_upfreq_value = 1
		
		def access_scanfreq_value = 6
		def min_scanfreq_value = 4
		def hour_scanfreq_value = 3
		def day_scanfreq_value = 2
		def week_scanfreq_value = 1
		
		def mand_hids_value = 3
		def disc_hids_value = 1
			
		float scan_s = 0
		float hids_s = 0
		def sum = 0
		def max_conn = getFactorValue(0)
		float max_upfreq, max_scanfreq, max_hids = 0
				
		//max value assignments
		max_hids = mand_hids_value
		max_upfreq = min_upfreq_value
		max_scanfreq = access_scanfreq_value
				
		def contents = getDataPointList("EndUserDevice")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
				
		def count = 0
		//[2018.01.31 AA] Added condition for null values
		for (u in contents) {
			float update_s, app_exempt, file_exempt = 0
			if (u.U05 == EndUserDevice.Encryption.NA || u.U06 == BoolEx.NA || u.U07 == BoolEx.NA){
				count ++
				if (u.u01 == EndUserDevice.UpdateFrequency.Min) update_s = min_upfreq_value/max_upfreq
				else if (u.u01 == EndUserDevice.UpdateFrequency.Hour) update_s = hour_upfreq_value/max_upfreq
				else if (u.u01 == EndUserDevice.UpdateFrequency.Day) update_s = day_upfreq_value/max_upfreq
				else if (u.u01 == EndUserDevice.UpdateFrequency.Week) update_s = week_upfreq_value/max_upfreq
				if (u.u02 == EndUserDevice.ScanFrequency.On_Access) scan_s = access_scanfreq_value/max_scanfreq
				else if (u.u02 == EndUserDevice.ScanFrequency.Min) scan_s = min_scanfreq_value/max_scanfreq
				else if (u.u02 == EndUserDevice.ScanFrequency.Hour ) scan_s = hour_scanfreq_value/max_scanfreq
				else if (u.u02 == EndUserDevice.ScanFrequency.Day) scan_s = day_scanfreq_value/max_scanfreq
				else if (u.u02 == EndUserDevice.ScanFrequency.Week) scan_s = week_scanfreq_value/max_scanfreq
				if (u.U03 == -1) app_exempt = 1
				else app_exempt = 1 - u.U03
				if (u.U04 == -1) file_exempt = 1
				else file_exempt = 1- u.U04 
				if (u.u08 == EndUserDevice.HIDSManagement.Mandatory) hids_s = mand_hids_value/max_hids
				else if (u.u08 == EndUserDevice.HIDSManagement.Discretionary) hids_s = disc_hids_value/max_hids
				float access_s = (max_conn - u.U09)/max_conn
				float score = (update_s*update_w + scan_s *scan_w + app_exempt*app_w + file_exempt*file_w + hids_s*hids_w + access_s*access_w)/w_sum
				sum = sum + score
						
			}	
					
		}
		if (count == 0) return init_value
		return 	sum*10/count	
	}
	
	def O_U_MMDPS () {
		
		//weighting 
		def update_w = 1
		def scan_w = 1
		def app_w = 1
		def file_w  = 1
		def hids_w = 1
		def access_w = 5
		def crypt_w = 3
		def theft_w = 3
		def pol_w =3
		def w_sum = update_w + scan_w + app_w + hids_w + crypt_w + theft_w + pol_w
				
		//value mappings
		def min_upfreq_value = 5
		def hour_upfreq_value = 4
		def day_upfreq_value = 3
		def week_upfreq_value = 1
				
		def access_scanfreq_value = 6
		def min_scanfreq_value = 4
		def hour_scanfreq_value = 3
		def day_scanfreq_value = 2
		def week_scanfreq_value = 1
				
		def mand_crypt_value = 3
		def disc_crypt_value = 1
				
		def mand_hids_value = 3
		def disc_hids_value = 1
				
		//max value assignments
		float max_hids = mand_hids_value
		float max_upfreq = min_upfreq_value
		float max_scanfreq = access_scanfreq_value	
		float max_crypt = mand_crypt_value	
		
		def contents = getDataPointList("EndUserDevice")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
				
		def (sum, update_s, scan_s, crypt_s, pol_s, theft_s, hids_s, app_exempt, file_exempt, access_s, score) = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		def max_access = getFactorValue(0)
				
		def count = 0
				
		for (u in contents) {
		//[2018.01.31 AA] Added condition for null values
		if (u.u05 != EndUserDevice.Encryption.NA && u.u06 != BoolEx.NA && u.u07 != BoolEx.NA)
		
		{			
			count++
						
			if (u.U01 == EndUserDevice.UpdateFrequency.Min) update_s = min_upfreq_value/max_upfreq
			if (u.U01 == EndUserDevice.UpdateFrequency.Hour) update_s = hour_upfreq_value/max_upfreq
			else if (u.U01 == EndUserDevice.UpdateFrequency.Day) update_s = day_upfreq_value/max_upfreq
			else if (u.U01 == EndUserDevice.UpdateFrequency.Week) update_s = week_upfreq_value/max_upfreq
			else update_s = 0			
						
			if (u.U02 == EndUserDevice.ScanFrequency.On_Access) scan_s = access_scanfreq_value/max_scanfreq
			else if (u.U02 == EndUserDevice.ScanFrequency.Min) scan_s = min_scanfreq_value/max_scanfreq
			else if (u.U02 == EndUserDevice.ScanFrequency.Hour) scan_s = hour_scanfreq_value/max_scanfreq
			else if (u.u02 == EndUserDevice.ScanFrequency.Day) scan_s = day_scanfreq_value/max_scanfreq
			else if (u.u02 == EndUserDevice.ScanFrequency.Week) scan_s = week_scanfreq_value/max_scanfreq
			else scan_s = 0
						
			if (u.U03 == -1) app_exempt = 1
			else app_exempt = 1 - u.U03		
		
			if (u.U05 == EndUserDevice.Encryption.Mandatory) crypt_s = mand_crypt_value/max_crypt
			else if (u.U05 == EndUserDevice.Encryption.Discretionary) crypt_s = disc_hids_value/max_crypt			
		
			if (u.U06 == BoolEx.Yes) pol_s = 1
			if (u.U07 == BoolEx.Yes) theft_s = 1
		
			if (u.U08 == EndUserDevice.HIDSManagement.Mandatory) hids_s = mand_hids_value/max_hids
			else if (u.u08 == EndUserDevice.HIDSManagement.Discretionary) hids_s = disc_hids_value/max_hids
		
			score = (update_s*update_w + scan_s * scan_w + app_exempt*app_w + hids_s * hids_w + crypt_s*crypt_w + theft_s*theft_w + pol_s*pol_w)*10/w_sum
		
			sum += score
		}
					
								
		}
		if (count == 0) return init_value
				
		return 	sum = sum/count	
	}
	
//** EVENT

	def O_E_METP () {
	
		def contents = getDataPointList("Event")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
				
		Map map	= [:]
		for (e in contents) {
			def l = map.get (e.e03.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [e]
				map.put(e.e03.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(e)
				map.put(e.e03.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def tp = 0
			for (e in l) {
						
				if (e.e05 == BoolEx.Yes) tp++
			}
		sum += tp/l.size()
		}
		return sum/12
	}

	def O_E_MC () {
	
		def contents = getDataPointList("Event")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		
		Map map	= [:]
		for (e in contents) {
			def l = map.get (e.e03.getAt(Calendar.MONTH))
			if (l == null) 	{
				l = [e]
				map.put(e.e03.getAt(Calendar.MONTH), l)
			}
			else {
				l.add(e)
				map.put(e.e03.getAt(Calendar.MONTH), l)
			}	
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
	
		def sum = 0
		map.each { entry ->
			def l = entry.value
			sum += l.size()		
		}	
		return sum/12			
	}

//** THREAT

	def O_T_MITP () {

		def contents = getDataPointList("ThreadWarningAlert")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		
		Map map	= [:]
		for (t in contents) {
			if (t.t06 == BoolEx.No) {
				def l = map.get (t.t03.getAt(Calendar.MONTH))
				if (l == null) 	{
					l = [t]
					map.put(t.t02.getAt(Calendar.MONTH), l)
				}
				else {
					l.add(t)
					map.put(t.t02.getAt(Calendar.MONTH), l)
				}	
			}
		}		
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
		
		def sum = 0
		def mitp
		map.each { entry ->
			def l = entry.value
			def tp = 0
			for (t in l) {
				if (t.t05 == BoolEx.Yes) tp++			
			}
			sum += tp/l.size()		
		}	
		mitp = sum/map.size()
		return mitp
	}


	def O_T_MCI () {
		def contents = getDataPointList("ThreadWarningAlert")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		Map map	= [:]
		for (t in contents) {
			if (t.t06 == BoolEx.No) {
				def count = map.get (t.T02.getAt(Calendar.MONTH))
				if (count == null) map.put(t.T02.getAt(Calendar.MONTH), 1)
				else {
					count ++
					map.put(t.T02.getAt(Calendar.MONTH), count)
				}	
			}
		}		
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return zero_value
		
		def sum = 0
		map.each { entry ->
			sum += 	entry.value
		}	
		return sum/12
	}


	def O_T_MTIA() {
		def contents = getDataPointList("ThreadWarningAlert")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		def sum = 0
		def num = 0
		for (t in contents) {
			if (t.T06 == BoolEx.No)
			{
				use(groovy.time.TimeCategory){
					def diff_tia = t.T03 - t.T02
					sum = sum + diff_tia.days
				}
				num++
			}
		}
		return sum/num
	}

	def O_T_MTIP() {
		def contents = getDataPointList("ThreadWarningAlert")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		//def sum =0
		def mtip = 0
		for (t in contents) {
			use(groovy.time.TimeCategory){
				if (t.T04 != null && (t.T06 == BoolEx.No)) 
				{
					def diff_mtip = t.T04 - t.T02
					mtip = diff_mtip.days
				}
			}	
		}
		return mtip	
	}
	
	def O_T_IES() {
	
		float mitp_w = 2
		float bu_w = 5
		float mtip_w = 5
		float form_w = 2
		float inform_w = 1
		float score, sum, tp_score, formal_score, informal_score = 0
			
		def bu = getDataPointList("BusinessUnit")
		if( bu == null || bu.isEmpty()){
			return RESULT_FAIL
		}
		for (b in bu) {
			formal_score = b.b04/getFactorValue(0)
			informal_score = b.b05/getFactorValue(1)
			score = ((formal_score)*form_w + (informal_score)*inform_w)*10/(form_w + inform_w)
			sum = sum + score
			
			
		}
			
		float bu_score = sum
		
		tp_score = getMetricValue("O-T-MITP")
		
		float w_sum = mitp_w + bu_w
		float ties = (bu_score*bu_w + tp_score*mitp_w)/w_sum
		return ties
	}


	def O_T_THTP() {
	
		def contents = getDataPointList("ThreadWarningAlert")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
				
		Map map	= [:]
		for (t in contents) {
			if (t.t06 == BoolEx.Yes) {
				def l = map.get (t.T02.getAt(Calendar.MONTH))
				if (l == null) {	
					l =[t]
					map.put(t.T02.getAt(Calendar.MONTH),l)	
				}
				else {
					l.add(t)
					map.put(t.T02.getAt(Calendar.MONTH),l)
				}	
			}
		}
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return 0
				
		def sum = 0
		map.each { entry ->
			def l = entry.value
			def tp = 0
			for (t in l) {
				if (t.t05 == BoolEx.Yes) tp++
			}
			sum += tp/l.size()
		}
		return sum/12
	}
	
	def O_T_MCH() {
		
		def contents = getDataPointList("ThreadWarningAlert")
		if( contents == null || contents.isEmpty()){
			return RESULT_FAIL
		}
		
		Map map	= [:]
		
		for (t in contents) {
			if (t.t06 == BoolEx.Yes) {
				def count = map.get (t.T02.getAt(Calendar.MONTH))
				if (count == null) map.put(t.T02.getAt(Calendar.MONTH), 1)
				else {
					count ++
					map.put(t.T02.getAt(Calendar.MONTH), count)
				}	
			}
		}		
		if (map == null) return RESULT_FAIL
		if (map.size() <=0) return 0
				
		def sum = 0
		map.each { entry ->
			sum += 	entry.value
		}	
		return sum/12
		

	}
	
	def O_T_THES() {
				
		def pract_w = 1
		def th_emp_w = 1
		def th_ct_w = 2
		def thtp_w = 5
		def bu_w = 2
		def max_th_ct = 2
		def max_pract = getFactorValue(0)
		def th_pract_inform_val = 2
		def th_pract_form_val = 1
		float sum = 0
				
		def bu = getDataPointList("BusinessUnit")
		if( bu == null || bu.isEmpty()){
			return RESULT_FAIL
		}
		for (b in bu) {
				
			def th_int = 0
			if(b.t06 == BusinessUnit.Practice.informal) th_int = th_pract_inform_val/max_pract
			else if(b.t06 == BusinessUnit.Practice.formal)th_int = th_pract_form_val/max_pract
			else th_int = 0 		
			float th_emp = (b.T07)/(b.B01)
			float th_inv = (b.T08)/max_th_ct
			float th_w = pract_w + th_emp_w + th_ct_w
			
			float score = (th_int*pract_w + th_emp*th_emp_w + th_inv*th_ct_w)/(th_w)
			
			sum += score
		
		}
				
		float bu_score = sum
		float tp_rate = getMetricValue("O-T-THTP")
		
		float thes = ((tp_rate*thtp_w + bu_score*bu_w)*10)/(bu_w + thtp_w)
		return thes
	}
