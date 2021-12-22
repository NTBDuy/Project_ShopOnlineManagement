package poly.store.service.impl;

import java.io.File;
import java.nio.file.Path;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.store.service.UploadService;

@Service
public class UploadServiceImpl implements UploadService {
	@Autowired
	ServletContext app;
	@Autowired
	HttpServletRequest req;
	
	
	@Override
	public File save(MultipartFile file, String folder) {
		File dir = new File(app.getRealPath("/assets/images/" + folder));
		System.out.println(dir);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String s = System.currentTimeMillis()+file.getOriginalFilename();
		String name = Integer.toHexString(s.hashCode())+ s.substring(s.lastIndexOf("."));
		try {
			File saveFile = new File(dir,name);
			file.transferTo(saveFile);
			System.out.println(saveFile.getAbsolutePath());
			return saveFile;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	
	}

}
