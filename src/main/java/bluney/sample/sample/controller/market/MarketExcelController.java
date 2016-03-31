package bluney.sample.sample.controller.market;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import bluney.sample.sample.controller.common.excel.AbstractExcelController;
import bluney.sample.sample.service.market.MarketExcelService;
import lombok.Getter;

@Controller
@RequestMapping(value = "/service/market")
public class MarketExcelController extends AbstractExcelController {

	@Resource(name="marketExcelService")
	private @Getter MarketExcelService excelService;


	@Override
	protected String getUploadFileInfo() {
		return "시계열 자료를 업로드해 주세요.";
	}

}