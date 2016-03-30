package bluney.sample.sample.service.common.excel;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.View;

public interface ExcelService {
	
	public String importExcel(CommonsMultipartFile file) throws IOException;
	public View exportExcel(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException;
		
}
