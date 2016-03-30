package bluney.sample.sample.common.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportExcel {

	private static final Integer NOT_DEFINE_LAST_ROW = 0;
	private static final Integer NOT_DEFINE_LAST_COLUMN = 0;
	
	protected static final Logger logger = LoggerFactory.getLogger(ImportExcel.class);

	/** rowsNum */
	private static int rowsNum = NOT_DEFINE_LAST_ROW;

	/** columnNum */
	private static int columnNum = NOT_DEFINE_LAST_COLUMN;

	public static List<List<String>> getExcelStringList(File file) {
		return getExcelStringListAtIndex(file, 0, 0, 0, NOT_DEFINE_LAST_ROW, NOT_DEFINE_LAST_COLUMN);
	}

	public static List<List<String>> getExcelStringList(File file, int startRow, int startColumn) {
		return getExcelStringListAtIndex(file, 0, startRow, startColumn, NOT_DEFINE_LAST_ROW, NOT_DEFINE_LAST_COLUMN);
	}
	
	public static List<List<String>> getExcelStringListAtIndex(File file, int index, int startRow, int startColumn) {
		return getExcelStringListAtIndex(file, index, startRow, startColumn, NOT_DEFINE_LAST_ROW, NOT_DEFINE_LAST_COLUMN);
	}
	
	public static List<List<String>> getExcelStringListAtIndex(File file, int index, int startRow, int startColumn, int lastRow, int lastColumn) {
		Workbook workbook = null;
		List<List<String>> result = null;
		try {
			result = new ArrayList<List<String>>();
			workbook = WorkbookFactory.create(file);
			
			if (index < workbook.getNumberOfSheets()) {
				Sheet sheet = workbook.getSheetAt(index);

				rowsNum = (lastRow == NOT_DEFINE_LAST_ROW ? sheet.getLastRowNum() : lastRow);				
				columnNum = (lastColumn == NOT_DEFINE_LAST_COLUMN ? getColumnNum(sheet, startRow, startColumn): lastColumn);

				result = readExcel(sheet, startRow, startColumn, rowsNum, columnNum);				
			}
			else {
				logger.warn("invalid index of sheet ");
			}
			
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        
		return result;
	}
	
	public static List<List<List<String>>> getExcelStringListAllSheets(File file) {
		return getExcelStringListAllSheets(file, 0, 0, NOT_DEFINE_LAST_ROW, NOT_DEFINE_LAST_COLUMN);
	}
	
	public static List<List<List<String>>> getExcelStringListAllSheets(File file, int startRow, int startColumn) {
		return getExcelStringListAllSheets(file, startRow, startColumn, NOT_DEFINE_LAST_ROW, NOT_DEFINE_LAST_COLUMN);
	}

	public static List<List<List<String>>> getExcelStringListAllSheets(File file, int startRow, int startColumn, int lastRow, int lastColumn) {
		Workbook workbook = null;
		List<List<List<String>>> results = null;
		try {
			results = new ArrayList<List<List<String>>>();
			workbook = WorkbookFactory.create(file);
			
			int sheetCount = workbook.getNumberOfSheets();
			for(int i=0; i<sheetCount; i++) {
				List<List<String>>result = new ArrayList<List<String>>();
				Sheet sheet = workbook.getSheetAt(i);

				rowsNum = (lastRow == NOT_DEFINE_LAST_ROW ? sheet.getLastRowNum() : lastRow);				
				columnNum = (lastColumn == NOT_DEFINE_LAST_COLUMN ? getColumnNum(sheet, startRow, startColumn): lastColumn);

				result = readExcel(sheet, startRow, startColumn, rowsNum, columnNum);		
				results.add(result);
			}
			
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        
		return results;
	}
	
	/**
	 * getColumnNum
	 * 
	 * @param sheet
	 * @param startRow
	 * @param startColumn
	 * @return
	 */
	private static int getColumnNum(Sheet sheet, int startRow,
			int startColumn) {
		int columnNum = 0;
		Row row = null;
		if (startRow == 0) {
			row = sheet.getRow(0);
		} else {
			row = sheet.getRow(startRow - 1);
		}
		for (int i = startColumn;; i++) {
			Cell cell = row.getCell(i);
			if (cell == null) {
				columnNum = i-1;
				break;
			} else {
				if (("").equals(cell.toString())) {
					columnNum = i-1;
					break;
				}
			}
		}
		return columnNum;
	}

	/**
	 * read excel
	 * 
	 * @param sheet
	 * @param startRow
	 * @param startColumn
	 * @param totalRows
	 * @param columnNum
	 * @return
	 */
	private static List<List<String>> readExcel(Sheet sheet, int startRow, int startColumn, int totalRows, int columnNum) {
		List<List<String>> results = new ArrayList<List<String>>();
		try {
			boolean flag = true;

			for (int i = startRow; i <= totalRows; i++) {
				if (!flag) {
					break;
				}
				Row row = sheet.getRow(i);
				if (row == null) {
					break;
				}

				List<String> list = new ArrayList<String>();
				for (int j = startColumn; j <= columnNum; j++) {
					Cell cell = row.getCell(j);
					if (cell == null) {
						list.add("");
					} else {
						list.add(getCellValue(cell));
					}
				}
				results.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	private static String getCellValue(Cell cell) {
		
		String ret;
		
		if(cell.getClass() == HSSFCell.class) {
			switch (cell.getCellType()) {

			case HSSFCell.CELL_TYPE_BLANK:
				ret = "";
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				ret = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				ret = null;
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell) || cell.getCellStyle().getDataFormat()==179) {
	            		SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateUtil.BOTH);            		
						ret = sdf.format(cell.getDateCellValue()); 
				}else{
						ret = cell.getNumericCellValue() + "";
				}            	             
	            break;  
			case HSSFCell.CELL_TYPE_STRING:
				ret = cell.getStringCellValue();
				break;
			default:
				ret = null;
			}			
		}
		
		else {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				ret = "";
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				ret = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				ret = null;
				break;
			case Cell.CELL_TYPE_NUMERIC:
				double cellvalue = cell.getNumericCellValue();

				if (org.apache.poi.ss.usermodel.DateUtil.isValidExcelDate(cellvalue)
						|| cell.getCellStyle().getDataFormat() == 179) {
					SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateUtil.BOTH);
					ret = sdf.format(cell.getDateCellValue());
				} else {
					ret = cellvalue + "";
				}
				break;
			case Cell.CELL_TYPE_STRING:
				ret = cell.getStringCellValue();
				break;
			default:
				ret = null;
			}
		}
		return ret;
	}
}
