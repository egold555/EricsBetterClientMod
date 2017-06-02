package org.golde.forge.betterminecraft.config;

import java.io.File;
import java.util.List;

import org.golde.forge.betterminecraft.Constants;


import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BMConfig {

	public static long seed = 0;
	public static final String CATEGORY_NAME_GENERAL = "category_general";

	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), "BetterMinecraft.cfg");

		// initialize your configuration object with your configuration file values.
		config = new Configuration(configFile);

		// load config from file (see mbe70 package for more info)
		syncFromFile();
	}

	public static void clientPreInit() {
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}

	public static Configuration getConfig() {
		return config;
	}

	public static void syncFromFile() {
		syncConfig(true, true);
	}

	public static void syncFromGUI() {
		syncConfig(false, true);
	}

	public static void syncFromFields() {
		syncConfig(false, false);
	}
	
	
	
	private static void syncConfig(boolean loadConfigFromFile, boolean readFieldsFromConfig) 
	{
		if (loadConfigFromFile) {
			config.load();
		}
		
		
		Property propSeed = config.get(CATEGORY_NAME_GENERAL, "seed", "",
				"World seed to show slime data");
		propSeed.setLanguageKey("gui.config.seed");

		//if (readFieldsFromConfig)
		//{
			seed = parseLong(propSeed.getString());
			propSeed.set(String.valueOf(seed));

			//if (config.hasChanged()) {
				config.save();
			//}
		//}
	}

	// Define your configuration object
	private static Configuration config = null;

	public static class ConfigEventHandler 
	{
		/*
		 * This class, when instantiated as an object, will listen on the Forge
		 * event bus for an OnConfigChangedEvent
		 */
		@SubscribeEvent(priority = EventPriority.NORMAL)
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) 
		{
			if (Constants.MOD_ID.equals(event.getModID()) && !event.isWorldRunning()) 
			{
				if (event.getConfigID().equals(CATEGORY_NAME_GENERAL)) 
				{
					syncFromGUI();
				}
			}
		}
		
		
	}
	
	private static long parseLong(String l) {
		try {
			return Long.parseLong(l);
		}catch(Exception e) {
			return 0;
		}
	}
}
