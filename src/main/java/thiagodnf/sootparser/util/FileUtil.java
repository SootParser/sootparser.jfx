package thiagodnf.sootparser.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

public class FileUtil {
	
	public static List<String> getFiles(String directory){
		
		if(StringUtils.isEmpty(directory)) {
			return Arrays.asList();
		}
		
		try {
			return Files.walk(Paths.get(directory))
				     .filter(Files::isRegularFile)
				     .filter(p -> !p.endsWith(".DS_Store"))
				     .map(path -> path.toFile().getAbsolutePath())
				     .collect(Collectors.toList());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return Arrays.asList();
	}

}
