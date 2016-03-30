package bluney.sample.sample.common.excel;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import lombok.Getter;
import lombok.Setter;

public class UploadFile {
	private @Getter @Setter String name;
	private @Getter @Setter CommonsMultipartFile file;
}
