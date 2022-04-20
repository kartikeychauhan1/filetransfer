package com.converter.fileTransfer.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.converter.fileTransfer.DownloadDTO;
import com.converter.fileTransfer.Dto;

@RestController
// @RequestMapping("/transfer")
public class FileTransferController {
	static final Logger log = LoggerFactory.getLogger(FileTransferController.class);

	@Value(value = "${ccm.filePath}")
	private String path;

	@Value(value = "${ccm.downloadFilePath}")
	private String downloadPath;

	@PostMapping(path = "/transfer")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public String getFileConvert(@RequestBody(required = true) Dto dto) {
		log.info("Controller");
		File file = new File(path + dto.getFilePath());
		FileInputStream input;
		try {
			input = new FileInputStream(file);
			String stream = DatatypeConverter.printBase64Binary(IOUtils.toByteArray(input));
			log.info(
					"File converted for filepath : " + path + dto.getFilePath() + " and file name : " + file.getName());
			return stream;
		} catch (FileNotFoundException e) {
			log.info("file not found for filepath : " + path + dto.getFilePath());
			return "file not found exception";
		} catch (IOException e) {
			log.info("excecption in io for filepath : " + path + dto.getFilePath());
			return "IO exeption in the file conversion";
		}
	}

	@PostMapping(path = "/transfertoccm")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public String getFileDownload(@RequestBody(required = true) DownloadDTO dto) {
		log.info("filename:"+ dto.getFileName()+ ","+ "filebytes :" + dto.getFileBytes()+ "," +"workitem :" +dto.getWorkItem());
		
		File dir = new File(downloadPath + dto.getWorkItem());
		if (dir.mkdirs()) {
			log.info("file directory has been created");
		}
		try {

			decode(downloadPath + dto.getWorkItem() + "/" + dto.getFileName(), dto.getFileBytes().toString().getBytes());
			log.info("created successfully");
			return "created successfully";

		} catch (Exception e) {

			log.info("file having exception while creating in ccm : " + e.getMessage());
			return "file having exception while creating in ccm :" + e.getMessage();
		}

	}
	
	public static void decode(String targetFile, byte[] sourceFilebtyes) throws Exception {
		byte[] decodedBytes = Base64.decodeBase64(sourceFilebtyes);
		log.info("decoded started");
		writeByteArraysToFile(targetFile, decodedBytes);
	}

	public static void writeByteArraysToFile(String fileName, byte[] content) throws IOException {
		File file = new File(fileName);
		log.info("file created with name :" +file.getName());
		BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
		writer.write(content);
		writer.flush();
		writer.close();
	}

}
