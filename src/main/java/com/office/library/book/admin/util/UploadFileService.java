package com.office.library.book.admin.util;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
public class UploadFileService {
	
	//파일 업로드
	public String upload(MultipartFile file) {
		
		boolean result = false;
		
		//파일 저장
		String fileOriName = file.getOriginalFilename(); //파일 이름
		String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length()); //파일 확장자
		String uploadDir = "C:\\library\\upload\\"; //파일 저장되는 디렉터리 경로
		
		UUID uuid = UUID.randomUUID();
		String uniqueName = uuid.toString().replace("-", "");
		
		File saveFile = new File(uploadDir + "\\" + uniqueName + fileExtension);
		
		if (!saveFile.exists())
			saveFile.mkdirs();
		
		try {
			file.transferTo(saveFile);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (result) {
			System.out.println("[UploadFileService] FILE UPLOAD SUCCESS !!");
			return uniqueName + fileExtension;
			
		} else {
			System.out.println("[UploadFileService] FILE UPLOAD FAIL !!");
			
			return null;
		}
	}

}





