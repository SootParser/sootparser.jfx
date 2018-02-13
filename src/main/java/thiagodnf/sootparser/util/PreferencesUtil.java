package thiagodnf.sootparser.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.prefs.Preferences;

public class PreferencesUtil {

	private static Preferences prefs = Preferences.userNodeForPackage(PreferencesUtil.class);

	public static void save(String key, String value) {
		prefs.put(key, value);
	}

	public static String restore(String key, String def) {
		return prefs.get(key, def);
	}
	
	public static String restore(String key) {
		return restore(key, "");
	}
	
	public static Properties load(File file) throws FileNotFoundException, IOException {
		
		Properties prop = new Properties();

		prop.load(new FileInputStream(file));
		
		return prop;
	}
}
