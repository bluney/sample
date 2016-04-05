package bluney.sample.sample.controller.common.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import bluney.sample.sample.common.excel.UploadFile;
import bluney.sample.sample.service.common.excel.ExcelService;

public abstract class AbstractExcelController implements ExcelController, HandlerExceptionResolver{
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractExcelController.class);
	protected abstract String getUploadFileInfo();
	protected abstract ExcelService getExcelService();
	/////////////////////////////////////////
	// 
	////////////////////////////////////////
	@Override
	public View exportExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		View view = null;
		try {
			view = getExcelService().exportExcel(model, request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return view;
	}

	@Override
	public String requestExcel(Model model, HttpServletRequest request) {
		logger.warn("request method: requestExcel()");
		logger.warn("request base url : "+request.getRequestURI());	
		
		int index =  request.getRequestURI().lastIndexOf('/');
		String baseURI = request.getRequestURI().substring(0, index);
		UploadFile fileEntity = new UploadFile(); 
		String uploadFileInfo = getUploadFileInfo();
		
		model.addAttribute(UPLOAD_BASE_URL, baseURI);
		model.addAttribute(UPLOAD_FILE_ENTITY, fileEntity);
		model.addAttribute(UPLOAD_FILE_INFO, uploadFileInfo);
		
		return "/service/common/uploadExcel.html";
	}
	
	
	@Override
	public String importExcel(UploadFile fileObject, Model model, HttpServletRequest request, BindingResult result) throws IOException {
		logger.warn("request method: importExcel()");	
		
		if (!result.hasErrors()) {
			boolean isSuccess = getExcelService().importExcel(fileObject.getFile(), result);
			model.addAttribute(MODEL_MESSAGE_RESPONSE_SUCCESS, isSuccess);
		} else {
			model.addAttribute(MODEL_MESSAGE_RESPONSE_SUCCESS, false);
		}
		
		logger.debug(request.getRequestURL().toString());
		
		String msg = "IMPORT SUCCESS!";
		model.addAttribute(MODEL_MESSAGE_RESPONSE, msg);
		
		return "/service/common/uploadExcel.html";
	}
	
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception exception) {
		// TODO : 에러 결과를 새창에서 출력해주자. 
		Map<Object, Object> model = new HashMap<Object, Object>();
        if (exception instanceof FileNotFoundException)
        {
            model.put("errors", "FileNotFoundException");
        } else
        {
            model.put("errors", "Unexpected error: " + exception.getMessage());
        }

        // TODO exception handling page
        return new ModelAndView("", (Map) model);
	}
	
}
