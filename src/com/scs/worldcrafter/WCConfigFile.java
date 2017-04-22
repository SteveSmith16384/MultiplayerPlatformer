package com.scs.worldcrafter;

import java.io.IOException;

import ssmith.android.util.ConfigFile;

public final class WCConfigFile extends ConfigFile {
	
	public static final String PARAM_NAME_BUTTONS = "use_buttons";
	public static final String PARAM_NAME_MUTE = "mute";
	public static final String PARAM_MAX_LEVEL = "max_level";
	public static final String PARAM_SHOW_CTRL_SQUARES = "show_ctrl_squares";
	
	public boolean using_buttons, mute, show_control_squares;
	public int max_level;
	
	public WCConfigFile() {
		super(Statics.GetConfigDir() + "/config.txt");

		updateCache();
	}
	
	
	private void updateCache() {
		using_buttons = this.getParamAsBoolean(PARAM_NAME_BUTTONS, true);
		mute = this.getParamAsBoolean(PARAM_NAME_MUTE, false);
		max_level = this.getParamAsInt(PARAM_MAX_LEVEL, 1);
		show_control_squares = this.getParamAsBoolean(PARAM_SHOW_CTRL_SQUARES, true);

	}

	
	public boolean getUsingButtons() {
		return this.using_buttons;
	}
	
	
	public boolean getShowControlSquares() {
		return this.show_control_squares;
	}
	
	
	public boolean isMute() {
		return this.mute;
	}
	
	
	public int getMaxLevel() {
		return this.max_level;
	}
	
	
	public void setUseButtons(boolean b) throws IOException {
		Statics.cfg.setParam(WCConfigFile.PARAM_NAME_BUTTONS, b);
		this.saveCfg();

	}
	
	
	public void setIsMute(boolean b) throws IOException {
		Statics.cfg.setParam(WCConfigFile.PARAM_NAME_MUTE, b);
		this.saveCfg();

	}
	
	
	public void setShowControlSquares(boolean b) throws IOException {
		Statics.cfg.setParam(WCConfigFile.PARAM_SHOW_CTRL_SQUARES, b);
		this.saveCfg();

	}
	
	
	public void setMaxLevel(int lvl) throws IOException {
		Statics.cfg.setParam(WCConfigFile.PARAM_MAX_LEVEL, lvl);
		this.saveCfg();

	}
	
	
	protected void saveCfg() throws IOException {
		super.saveCfg();
		this.updateCache();
	}

}
