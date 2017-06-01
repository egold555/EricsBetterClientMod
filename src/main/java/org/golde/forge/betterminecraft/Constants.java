package org.golde.forge.betterminecraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {

	// Mod Constants
	public static final String MOD_ID = "ebc";
	public static final String MOD_NAME = "Erics Better Client";
	public static final String VERSION = "1.0";
	public static final String DEPENDENCIES = "";
	public static final String PREFIX_MOD = MOD_ID + ":";
	public static final String MC_VERSION = "[1.11.2]";
	public static final Logger LOG = LogManager.getLogger(MOD_NAME);

	// Proxy Constants
	public static final String PROXY_COMMON = "org.golde.forge.betterminecraft.proxy.CommonProxy";
	public static final String PROXY_CLIENT = "org.golde.forge.betterminecraft.proxy.ClientProxy";
	
	//GUI
	public static final String GUI_FACTORY = "org.golde.forge.betterminecraft.config.BMConfigGui";

}
