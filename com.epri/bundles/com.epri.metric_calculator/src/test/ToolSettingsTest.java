package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.epri.metric_calculator.Const;
import com.epri.metric_calculator.ToolSetting;

public class ToolSettingsTest {

	public static void main(String[] args) throws IOException {
//		String settingFailMsg = null;
//		try {
//			ToolSetting.newInstance();
//		} catch (VersionNotFoundException e) {
//			settingFailMsg = e.getMessage();
//			e.printStackTrace();
//		} catch (IOException e) {
//			settingFailMsg = "The setting file does not exist or can not be loaded.";
//			e.printStackTrace();
//		}
//		
//		try {
//			ToolSetting.getInstance().set(ToolSetting.PROP_METRICS_HUB_URL, "http://211.47.188.251:8082");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
		File file = Const.getUserResource(Const.PATH_SETTING_FILE);
		
        FileOutputStream out = new FileOutputStream(file);
        FileInputStream in = new FileInputStream(file);
        Properties props = new Properties();
        props.load(in);
        in.close();
        props.setProperty(ToolSetting.PROP_METRICS_HUB_URL, "http://211.47.188.251:8082");
        props.store(out, null);
        out.close();
	}
}