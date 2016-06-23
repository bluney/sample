package bluney.sample.sample.service.market;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.View;

import com.u2ware.springfield.repository.EntityRepository;

import bluney.sample.sample.common.util.DataConvertUtil;
import bluney.sample.sample.common.util.NullJudgeUtil;
import bluney.sample.sample.customtype.market.Market;
import bluney.sample.sample.customtype.market.MarketType;
import bluney.sample.sample.domain.lease.price.LeasePriceEntity;
import bluney.sample.sample.domain.lease.rate.LeaseRateEntity;
import bluney.sample.sample.domain.selling.price.SellingPriceEntity;
import bluney.sample.sample.domain.selling.rate.SellingRateEntity;
import bluney.sample.sample.service.common.excel.AbstractExcelService;

@Service("marketExcelService")
public class MarketExcelService extends AbstractExcelService {

	private final static int START_ROW_IN_EXCEL_FILE = 1; // 엑셀의 row 시작 위치
	private final static int START_COLUMN_IN_EXCEL_FILE = 0; // 엑셀의 column 시작 위치
	private final static int LAST_ROW_IN_EXCEL_FILE = NOT_DEFINE; // 엑셀의 row 마지막
																	// 위치
	private final static int LAST_COLUMN_IN_EXCEL_FILE = 188; // 엑셀의 column 마지막
																// 위치

	private final static String DELIM_CLASSFICATION = "-";

//	@Autowired
//	@Qualifier("leasePriceEntityRepository")
//	private EntityRepository<ClassificationEntity, Integer> classificationRepository;

	@Autowired
	@Qualifier("leasePriceEntityRepository")
	private EntityRepository<LeasePriceEntity, Integer> leasePriceRepository;

	@Autowired
	@Qualifier("leaseRateEntityRepository")
	private EntityRepository<LeaseRateEntity, Integer> leaseRateRepository;

	@Autowired
	@Qualifier("sellingPriceEntityRepository")
	private EntityRepository<SellingPriceEntity, Integer> sellingPriceRepository;

	@Autowired
	@Qualifier("sellingRateEntityRepository")
	private EntityRepository<SellingRateEntity, Integer> sellingRateRepository;

	@Override
	public Boolean importExcel(CommonsMultipartFile file, BindingResult result) {

		setStartRow(START_ROW_IN_EXCEL_FILE);
		setStartColumn(START_COLUMN_IN_EXCEL_FILE);

		boolean isSuccess = true;
		isSuccess &= importMarketExcel(file, MarketType.SELLING_PRICE, sellingPriceRepository);
		isSuccess &= importMarketExcel(file, MarketType.SELLING_RATE, sellingRateRepository);
		isSuccess &= importMarketExcel(file, MarketType.LEASE_PRICE, leasePriceRepository);
		isSuccess &= importMarketExcel(file, MarketType.LEASE_RATE, leaseRateRepository);

		return isSuccess;
	}

	private <T extends Market> boolean importMarketExcel(CommonsMultipartFile file, MarketType type,
			EntityRepository<T, Integer> repository) {
		logger.debug("importMarketExcel: begin - type:" + type.toString());
		List<List<String>> sheet;
		try {
			sheet = parseExcelFile(file, type.getCode(), getStartRow(), getStartColumn(), LAST_ROW_IN_EXCEL_FILE,
					LAST_COLUMN_IN_EXCEL_FILE);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		List<T> entities = parseMarketExcelSheet(sheet, type);

		repository.deleteAll();
		logger.debug("importMarketExcel: begin to save db - type=" + type.toString() + ", entities.size()=" + entities.size());
		repository.save(entities);
		logger.debug("importMarketExcel: success to save db - type=" + type.toString() + ", entities.size()=" + entities.size());
		
		return true;
	}

	@Override
	public View exportExcel(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object parseExcelSheet(List<List<String>> sheet) {
		// not used
		return null;
	}

	@Override
	protected boolean isValidRow(List<String> list) {
		// TODO Auto-generated method stub
		return false;
	}

	private <T extends Market> List<T> parseMarketExcelSheet(List<List<String>> sheet, MarketType type) {

		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<Integer, String> classificationMap = makeClassification(sheet);

		List<T> result = new ArrayList<T>();
		List<List<String>> dataSheet = sheet.subList(3, sheet.size());

		for (List<String> row : dataSheet) {

			try {
				Date date = transFormat.parse(row.get(0));

				int size = row.size();
				for (int i = 1; i < size; i++) {
					@SuppressWarnings("unchecked")
					T entity = (T) createInstanceOfMarket(type);
					entity.setDate(date);
					entity.setClassification(classificationMap.get(i));
					entity.setValue(DataConvertUtil.stringToDouble(row.get(i)));

					result.add(entity);
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("Error : Cannot parse date. toString=" + row.toString());
				e.printStackTrace();
			}
		}

		return result;
	}

	private Market createInstanceOfMarket(MarketType type) {
		Market instance = null;
		switch (type) {
		case SELLING_PRICE:
			instance = new SellingPriceEntity();
			break;
		case SELLING_RATE:
			instance = new SellingRateEntity();
			break;
		case LEASE_PRICE:
			instance = new LeasePriceEntity();
			break;
		case LEASE_RATE:
			instance = new LeaseRateEntity();
			break;
		default:
			break;
		}

		return instance;
	}

//	private Vector<Date> makeDate(List<List<String>> sheet) {
//		Vector<Date> vecDate = new Vector<Date>();
//
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		for (List<String> row : sheet) {
//			Date date;
//			try {
//				date = transFormat.parse(row.get(0));
//				vecDate.add(date);
//			} catch (ParseException e) {
//				logger.error("Error : Cannot make date vector");
//				e.printStackTrace();
//
//				// 잘못된 데이터가 들어가지 않고 죽도록 초기화 해버리자!
//				vecDate.clear();
//				break;
//			}
//		}
//		return vecDate;
//	}

	private Map<Integer, String> makeClassification(List<List<String>> sheet) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		List<String> level1NmList = sheet.get(0);
		List<String> level2NmList = sheet.get(1);

		Map<String, Integer> map = new HashMap<String, Integer>();

		// make classfication
		Assert.isTrue(level1NmList.size() == level2NmList.size(), "Invalid input! please check classifications");

		int size = level1NmList.size();
		String level1Nm, level2Nm;
		String prevLevel1Nm = null;

		for (int i = 1; i < size; i++) {
			String classStr = new String();
			level1Nm = level1NmList.get(i).replaceAll("\\s+", "");
			level2Nm = level2NmList.get(i).replaceAll("\\s+", "");

			boolean isNotNull1Nm = NullJudgeUtil.isNotNull(level1Nm);
			boolean isNotNull2Nm = NullJudgeUtil.isNotNull(level2Nm);

			if (isNotNull1Nm) {

				classStr = level1Nm;

				if (isNotNull2Nm) {
					classStr += level1Nm.equals(level2Nm) ? "" : (DELIM_CLASSFICATION + level2Nm);
				} else {

				}
				prevLevel1Nm = level1Nm;

			} else {
				if (isNotNull2Nm) {
					classStr = prevLevel1Nm + DELIM_CLASSFICATION + level2Nm;
				} else {
					logger.error("error : EMPTY CLASSIFICATION INPUT VALUES! pos = " + i);
				}
			}

			if (map.containsKey(classStr)) {
				logger.error(
						"error : DUPLICATE CLASSIFICATION INPUT VALUES! pos = " + i + ", classifcation = " + classStr);
				String rename = null;
				int addition = 1;
				do {
					rename = classStr + "(" + addition + ")";
				} while (map.containsKey(rename));

				classStr = rename;
			}

			map.put(classStr, i);
			result.put(i, classStr);
		}

		return result;
	}

}
