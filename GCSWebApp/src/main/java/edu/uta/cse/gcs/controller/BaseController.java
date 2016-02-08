package edu.uta.cse.gcs.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.google.api.client.util.IOUtils;

import edu.uta.cse.gcs.util.Constants;
import edu.uta.cse.gcs.util.GoogleCloudStorageManager;


@Controller
public class BaseController {

	private static Logger LOGGER = LoggerFactory
			.getLogger(BaseController.class);


	@RequestMapping(value = "/uploadDoc", method = RequestMethod.GET)
	public String uploadRedirect(ModelMap map) {
		return "UploadDocument";
	}
	
	@RequestMapping(value = "/uploadDocumentImpl", method = RequestMethod.POST)
	public String uploadDocument(MultipartRequest request, ModelMap map){
		LOGGER.info("inside upload method");
		long startTime,endTime,totalTime;
		try{
			startTime = System.currentTimeMillis();
			MultipartFile mpf = null;
			Iterator<String> itr = request.getFileNames();
			while (itr.hasNext()) {
				mpf = request.getFile(itr.next());
				LOGGER.info("fileName : "+mpf.getOriginalFilename());
				LOGGER.info("inputstream : "+mpf.getInputStream());
				InputStream data = new ByteArrayInputStream(mpf.getBytes());
				GoogleCloudStorageManager.uploadFile(mpf.getOriginalFilename(), data, Constants.BUCKET_NAME);
				data.close();
			}
			endTime = System.currentTimeMillis();
			totalTime = endTime - startTime;
			LOGGER.info("Total Time taken to Upload Documet in milli seconds: "+ totalTime);

			List<String> objectsInBucket = new ArrayList<>();
			objectsInBucket=GoogleCloudStorageManager.listBucket(Constants.BUCKET_NAME);
			if(objectsInBucket.size()<0){
				map.addAttribute("emptyBucket", "No objects in - "+Constants.BUCKET_NAME);
			}
			map.addAttribute("objectsInBucket", objectsInBucket);
			
			//Upload Result
			map.addAttribute("uploadedFileName", mpf.getOriginalFilename());
			map.addAttribute("uploadedFileSize", (mpf.getSize()/1024)+"KB");
			map.addAttribute("uploadTime", totalTime);
			
		}catch(IOException e){
			LOGGER.error("IOException occured : "+e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			LOGGER.error("Exception occured : "+e.getMessage());
			e.printStackTrace();
		}
		return "Home";
	}
	
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public String deleteItem(@RequestParam(value = "fileName") String fileName) {
		LOGGER.info("Inside deleteItem method");
		GoogleCloudStorageManager.deleteFile(Constants.BUCKET_NAME, fileName);
		return "Home";
	}
	

	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response,
			@RequestParam(value = "name") String uploadedFileName) {
		try {
			LOGGER.info("inside getFile method.!!!");
			LOGGER.info("Name of the file to be retrieved : "
					+ uploadedFileName);
			response.setHeader("Content-disposition", "attachment; filename=\""+uploadedFileName + "\"");
			IOUtils.copy(GoogleCloudStorageManager.downloadFile(Constants.BUCKET_NAME, uploadedFileName), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
