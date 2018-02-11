package thiagodnf.sootparser.util;

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
}
