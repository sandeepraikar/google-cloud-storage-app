package edu.uta.cse.gcs.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author Sandeep_Raikar
 * 
 */
public class Constants {

	public static final String PROJECT_ID;
	public static final String ACCOUNT_ID;
	public static final String PRIVATE_KEY_PATH;
	public static final String APP_NAME;
	public static final String BUCKET_NAME;
	public static final String ADMIN_EMAIL_ID;

	static {

		Configuration config = null;
		try {
			config = new PropertiesConfiguration("config.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		PROJECT_ID = config.getString("google.app.engine.project.id");
		ACCOUNT_ID = config.getString("google.app.engine.account.id");
		PRIVATE_KEY_PATH = config
				.getString("google.app.engine.private.key.path");
		APP_NAME = config.getString("google.app.engine.applicaion.name");
		BUCKET_NAME = config.getString("google.cloud.storage.bucket.name");
		ADMIN_EMAIL_ID =config.getString("admin.email.id");		
	}
}
