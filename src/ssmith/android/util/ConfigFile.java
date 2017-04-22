package ssmith.android.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import ssmith.android.io.IOFunctions;
import ssmith.lang.NumberFunctions;

public class ConfigFile {

	private static final String SEP = "~";

	private Hashtable<String, String> cfg = new Hashtable<String, String>();
	private String path;

	public ConfigFile(String _path) {
		super();

		path = _path;

		loadCfg();
	}


	private void loadCfg() {
		cfg.clear();
		if (new File(path).exists()) {
			try {
				String text = IOFunctions.LoadText(path);
				String cfgs[] = text.split("\n");
				for (String s : cfgs) {
					if (s.length() > 0) {
						String keys[] = s.split(SEP);
						if (keys.length == 2) {
							if (keys[0].length() > 0) {
								cfg.put(keys[0], keys[1]);
							}
						}
					}
				}
			} catch (IOException ex) {
				// Do nothing - we still want to be able to choose the params
			}
		}
	}


	/**
	 * This needs to be protected so the superclass can update its cache
	 */
	protected void saveCfg() throws IOException {
		StringBuffer str = new StringBuffer();
		Enumeration<String> enumr = cfg.keys();
		while (enumr.hasMoreElements()) {
			String key = enumr.nextElement();
			String val = cfg.get(key);
			if (key.endsWith("\\") == false) { // Remove corruption
				str.append(key + SEP + val + "\n");
			}
		}
		IOFunctions.SaveText(path, str.toString(), false);
	}


	protected String getParamAsString(String key, String def)  {
		if (cfg.containsKey(key)) {
			return cfg.get(key);
		} else {
			return def;
		}
	}


	protected boolean getParamAsBoolean(String key, boolean def)  {
		if (cfg.containsKey(key)) {
			return cfg.get(key).toLowerCase().startsWith("y");
		} else {
			return def;
		}
	}


	protected int getParamAsInt(String key, int def)  {
		if (cfg.containsKey(key)) {
			return NumberFunctions.ParseInt(cfg.get(key), false);
		} else {
			return def;
		}
	}


	protected void setParam(String key, String val) {
		this.cfg.remove(key);
		this.cfg.put(key, val);
	}


	protected void setParam(String key, int val) {
		this.cfg.remove(key);
		this.cfg.put(key, ""+val);
	}


	protected void setParam(String key, boolean val) {
		this.cfg.remove(key);
		this.cfg.put(key, val?"yes":"no");
	}

}
