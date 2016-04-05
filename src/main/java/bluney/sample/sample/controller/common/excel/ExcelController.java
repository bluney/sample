package bluney.sample.sample.controller.common.excel;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import bluney.sample.sample.common.excel.UploadFile;

public interface ExcelController {
	
	public final static String UPLOAD_FILE_ENTITY   = "upload_file_entity";
	public final static String UPLOAD_FILE_INFO		= "upload_file_info";
	public final static String UPLOAD_BASE_URL		= "upload_base_url";
	
	public final static String MODEL_MESSAGE = "model_message";
	public final static String MODEL_MESSAGE_RESPONSE  = "model_message_response";
	public final static String MODEL_MESSAGE_RESPONSE_SUCCESS  = "model_message_response_success";
	
	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)	
	public View exportExcel(Model model, HttpServletRequest request, HttpServletResponse response);
	
	@RequestMapping(value = "/requestExcel", method = RequestMethod.GET)
	public String requestExcel(Model model, HttpServletRequest request);
	
	@RequestMapping(value="/importExcel", method=RequestMethod.POST)
//	public String importExcel(MultipartFile file, BindingResult result) throws IOException;
	public String importExcel(@ModelAttribute(UPLOAD_FILE_ENTITY)UploadFile fileObject, Model model, HttpServletRequest request, BindingResult result) throws IOException;
}