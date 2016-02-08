package edu.uta.cse.gcs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.stream.FileImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

public class GoogleCloudStorageManager {

	private static Logger LOGGER = LoggerFactory
			.getLogger(GoogleCloudStorageManager.class);

	private static Storage storage;

	static {

		try {
			HttpTransport httpTransport = new NetHttpTransport();
			JsonFactory jsonFactory = new JacksonFactory();

			List<String> scopes = new ArrayList<String>();
			scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);

			Credential credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(jsonFactory)
					.setServiceAccountId(Constants.ACCOUNT_ID)
					.setServiceAccountPrivateKeyFromP12File(new File(Constants.PRIVATE_KEY_PATH))
					.setServiceAccountScopes(scopes).build();

			storage = new Storage.Builder(httpTransport, jsonFactory,
					credential).setApplicationName(Constants.APP_NAME).build();
		}catch (GeneralSecurityException | IOException e) {
			LOGGER.error("Security | IO Exception occured : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception occured : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void createBucket(String bucketName) throws Exception {

		Bucket bucket = new Bucket();
		bucket.setName(bucketName);
		storage.buckets().insert(Constants.PROJECT_ID, bucket).execute();
	}

	public static void deleteBucket(String bucketName) throws Exception {
		storage.buckets().delete(bucketName).execute();
		
	}

	public static List<String> listBucket(String bucketName) {

		List<String> list = new ArrayList<String>();

		List<StorageObject> objects;
		try {
			objects = storage.objects().list(bucketName).execute().getItems();

			if (objects != null) {
				for (StorageObject o : objects) {
					list.add(o.getName());
				}
			}
		} catch (IOException e) {
			LOGGER.error("IOException occured : "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	public static void uploadFile(String fileName, InputStream inputStream,
			String bucketName) throws Exception {

		StorageObject object = new StorageObject();
		object.setBucket(bucketName);

		try {
			String contentType = URLConnection
					.guessContentTypeFromStream(inputStream);
			InputStreamContent content = new InputStreamContent(contentType,
					inputStream);

			Storage.Objects.Insert insert = storage.objects().insert(
					bucketName, null, content);
			insert.setName(fileName);
			insert.execute();
			
			

		} finally {
			inputStream.close();
		}
	}
	
	public static void deleteFile(String bucketName, String fileName){		
		try {
			storage.objects().delete(bucketName, fileName).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static InputStream downloadFile(String bucketName, String fileName){
		InputStream stream = null;
		
		Storage.Objects.Get get;
		try {
			get = storage.objects().get(bucketName, fileName);
			stream = get.executeAsInputStream();
			LOGGER.info("InputStream : "+stream);
		} catch (IOException e) {
			LOGGER.error("IOException occured!"+ e.getMessage());
			e.printStackTrace();
		}
		return stream;
	}
}
