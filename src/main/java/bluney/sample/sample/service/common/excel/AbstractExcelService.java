package bluney.sample.sample.service.common.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import bluney.sample.sample.common.util.ImportExcel;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractExcelService implements ExcelService{
	
	public static final Integer NOT_DEFINE =	0;
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractExcelService.class);
	
	private @Getter @Setter int startRow = NOT_DEFINE;
	private @Getter @Setter int startColumn = NOT_DEFINE;
	
//	@Override 
//	public String exportExcel(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
//		return "";
//	}
	
	@Override
	public String importExcel(CommonsMultipartFile file) throws IOException {
		return "";
	}
	
	protected List<List<String>> parseExcelFile(CommonsMultipartFile file, int startRow, int startColumn) throws IOException {
		List<List<String>> result = null;
		
		if (isValidExcelExtenstion(file)) {
					
			// save & load location
			String filePath = System.getProperty("java.io.tmpdir") + file.getOriginalFilename();
			saveFile(file, filePath);
			
			result = ImportExcel.getExcelStringList(new File(filePath), startRow, startColumn);
		}
		return result;		
	}
	
	
	protected List<List<List<String>>> parseAllSheetsExcelFile(CommonsMultipartFile file, int startRow, int startColumn) throws IOException {
		return parseAllSheetsExcelFile(file, startRow, startColumn, NOT_DEFINE, NOT_DEFINE);
	}
	
	protected List<List<List<String>>> parseAllSheetsExcelFile(CommonsMultipartFile file, int startRow, int startColumn, int lastRow, int lastColumn) throws IOException {
		
		List<List<List<String>>> results = null;
		
		if (isValidExcelExtenstion(file)) {
			
			String filePath = System.getProperty("java.io.tmpdir") + file.getOriginalFilename();
			saveFile(file, filePath);			
			results = ImportExcel.getExcelStringListAllSheets(new File(filePath), startRow, startColumn, lastRow, lastColumn);
			deleteFile(filePath);
		}

		return results;				
	}
	
	abstract protected Object parseExcelRows(List<List<String>> sheet);
	

	abstract protected boolean isValidRow(List<String> list);
	

	private boolean isValidExcelExtenstion(CommonsMultipartFile file) {
		boolean isValid = false;
		
		if (file != null && !file.isEmpty()) {
			String suffix = null;
			int index = file.getOriginalFilename().lastIndexOf(".");
			
			if (index != -1 && index != file.getOriginalFilename().length() - 1) {
			
				suffix = file.getOriginalFilename().substring(index + 1);
				if (suffix != null && "xls|xlsx".indexOf(suffix) > -1) {
					
					// save & load location
					isValid = true;
				}
			}
		}
		
		return isValid;
	}
	
	private void saveFile(CommonsMultipartFile file, String filePath) throws FileNotFoundException, IOException {
		try(OutputStream os = new FileOutputStream(filePath)) {
			os.write(file.getFileItem().get());			
		}
	}
	
	private void deleteFile(String filePath) {
		File file = new File(filePath);
		file.delete();
	}


	/**
	 * 실제 엑셀 Sheet의 좌표를 입력으로 받아 parsing한 sheet list에서 원하는 cell string을 리턴받는 함수 
	 * 
	 * @param sheet  : sheet를 parsing한 string list의 list 
	 * @param row    : 실제 sheet의 row index
	 * @param column : 실제 sheet의 column index
	 * @return       : cell string
	 */
	protected String getCellString(List<List<String>> sheet, char columnCh, int row) {
		// row : START_ROW_IN_EXCEL_FILE + header row 만큼 제외
		// column : START_COLUMN_IN_EXCEL_FILE 만큼 제외
		int column = Character.isUpperCase(columnCh) ? columnCh-'A' : columnCh-'a';		
		return sheet.get(row - startRow -1).get(column - startColumn);
	}
}
