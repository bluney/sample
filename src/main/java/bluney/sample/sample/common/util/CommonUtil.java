package bluney.sample.sample.common.util;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonUtil {
	
	public static String getBrowser(HttpServletRequest request) {
        String header =request.getHeader("User-Agent");
        if (header.contains("MSIE")) {
               return "MSIE";
        } else if(header.contains("Chrome")) {
               return "Chrome";
        } else if(header.contains("Opera")) {
               return "Opera";
        }
        return "Firefox";
	}
	
	/**
	 * response에 한글 파일명이 다운로드 되도록 설정
	 * 다운로드 파일명 => 한글파일명 + '_' + yyyyMMdd + '.' + extension
	 * @param request
	 * @param response
	 * @param _fileName
	 * @throws Exception
	 */
	public static void setUTF8FileNameToResponse(HttpServletRequest request, HttpServletResponse response, String _fileName, String fileExtension) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String fileName = _fileName + "_" + dateFormat.format(new Date());
		if(fileExtension != null) {
			fileName += "." + fileExtension;
		}
        
        String header = CommonUtil.getBrowser(request);
        if (header.contains("MSIE")) {
               String docName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
               response.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
        } else if (header.contains("Firefox")) {
               String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
               response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
        } else if (header.contains("Opera")) {
               String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
               response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
        } else if (header.contains("Chrome")) {
               String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
               response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
        }
        response.setHeader("Content-Type", "application/octet-stream");
//        response.setContentLength((int)file.getSize());
        response.setHeader("Content-Transfer-Encoding", "binary;");
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");
	}
}
