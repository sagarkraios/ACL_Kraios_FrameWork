package com.hm.seleniumFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import com.aventstack.extentreports.ExtentTest;

public class PropertyLoader {
	static Properties properties;
	public static String isPassedScreenShotRequired;

	public static Properties init_properties() {
		try {
			FileInputStream fileInputStream = new FileInputStream("./src/test/resources/config/config.properties");
			properties = new Properties();
			properties.load(fileInputStream);

			isPassedScreenShotRequired = properties.getProperty("isPassedScreenShotRequired");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static String getProperties(String key) {
		String value = properties.getProperty(key).trim();
		return value;
	}
}
