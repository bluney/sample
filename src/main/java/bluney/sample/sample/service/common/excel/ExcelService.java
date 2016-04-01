package bluney.sample.sample.service.common.excel;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.View;

public interface ExcelService {
	
	public final static String RESULT_MESSAGE_SUCCESS = "success";
	public final static String RESULT_MESSAGE_FAIL = "failure";
	
	public String importExcel(CommonsMultipartFile file) throws IOException;
	
	public View exportExcel(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException;
		
}
