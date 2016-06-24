package bluney.sample.sample.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the ajax request.
 */
@Controller
public class AjaxController {

	private static final int BUFFER_SIZE = 100 * 1024;
	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/ajax/dashboard.html", method = RequestMethod.GET)
	public String dashboard(Locale locale, Model model) {
	
		return "/ajax/dashboard.html";
//		return "/loginForm";
	}
	
	@RequestMapping(value = "/service/bigdata/map_daum_sample.html", method = RequestMethod.GET)
	public String sampleDaumMap(Locale locale, Model model) {
	
		return "/service/bigdata/map_daum_sample";
	}

	@RequestMapping(value = "/service/fileUpload", method = RequestMethod.GET)
    public String dragAndDrop(Model model) {
         
        return "/service/file/fileUpload.html";
         
    }
     
	@ResponseBody
	@RequestMapping(value="/service/file/plupload",method=RequestMethod.POST)	
	public String plupload(@RequestParam MultipartFile file, HttpServletRequest request, HttpSession session) {
		try {
			String name = request.getParameter("name");
			Integer chunk = 0, chunks = 0;
			if(null != request.getParameter("chunk") && !request.getParameter("chunk").equals("")){
				chunk = Integer.valueOf(request.getParameter("chunk"));
			}
			if(null != request.getParameter("chunks") && !request.getParameter("chunks").equals("")){
				chunks = Integer.valueOf(request.getParameter("chunks"));
			}
			logger.info("chunk:[" + chunk + "] chunks:[" + chunks + "]");
			//检查文件目录，不存在则创建
			String relativePath = "/plupload/files/";
			String realPath = session.getServletContext().getRealPath("");
			File folder = new File(realPath + relativePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			
			//目标文件 
			File destFile = new File(folder, name);
			//文件已存在删除旧文件（上传了同名的文件） 
	        if (chunk == 0 && destFile.exists()) {  
	        	destFile.delete();  
	        	destFile = new File(folder, name);
	        }
	        //合成文件
	        appendFile(file.getInputStream(), destFile);  
	        if (chunk == chunks - 1) {  
	            logger.info("上传完成");
	        }else {
	        	logger.info("还剩["+(chunks-1-chunk)+"]个块文件");
	        }
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return "";
	}
	
	private void appendFile(InputStream in, File destFile) {
		OutputStream out = null;
		try {
			// plupload 配置了chunk的时候新上传的文件append到文件末尾
			if (destFile.exists()) {
				out = new BufferedOutputStream(new FileOutputStream(destFile, true), BUFFER_SIZE); 
			} else {
				out = new BufferedOutputStream(new FileOutputStream(destFile),BUFFER_SIZE);
			}
			in = new BufferedInputStream(in, BUFFER_SIZE);
			
			int len = 0;
			byte[] buffer = new byte[BUFFER_SIZE];			
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {		
			try {
				if (null != in) {
					in.close();
				}
				if(null != out){
					out.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	@RequestMapping(value = "/service/report", method = RequestMethod.GET)
    public String test(Model model) {
         
        return "/service/report/fileupload.html";
         
    }
}
