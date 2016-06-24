package bluney.sample.sample.common.excel;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {
	
	private static final Integer NOT_FOUND = -1;
	
	public static int getIndexBySheetName(File file, String sheetName) {
		Workbook workbook = null;
		int result = NOT_FOUND;
		try {
			workbook = WorkbookFactory.create(file);
			
			int sheetCount = workbook.getNumberOfSheets();
			for(int i=0; i<sheetCount; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				if(sheet.getSheetName().equals(sheetName)) {
					result = i;
					break;
				}
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
}
