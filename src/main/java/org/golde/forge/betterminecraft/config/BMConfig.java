package org.golde.forge.betterminecraft.config;

import java.io.File;
import java.util.List;

import org.golde.forge.betterminecraft.Constants;
import org.golde.forge.betterminecraft.client.bettermenus.resourcepack.DisplayPosition;
import org.golde.forge.betterminecraft.client.bettermenus.resourcepack.RenderPackListOverlay;

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
	public static final String CATEGORY_RESOURCE_PACKS = "category_resourcepacks";

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
	
	public static List<String> getEnabledPacks(){
		return ImmutableList.copyOf(enabledPacks);
	}
	
	public static void updateEnabledPacks(){
		List<String> packs = Minecraft.getMinecraft().gameSettings.resourcePacks;
		propEnabledPacks.set(packs.toArray(new String[packs.size()]));
		config.save();
	}

	public static void syncFromFields() {
		syncConfig(false, false);
	}
	
	public static DisplayPosition getDisplayPosition(){
		for(int index = 0; index < DisplayPosition.validValues.length; index++){
			if (DisplayPosition.validValues[index].equals(displayPosition)){
				return DisplayPosition.values()[index];
			}
		}
		
		return DisplayPosition.DISABLED;
	}
	
	private static String[] enabledPacks;
	static Property propEnabledPacks;
	private static String displayPosition = "disabled";
	private static char displayColor = 'f';
	public static int getDisplayColor(){
		return GuiUtils.getColorCode(displayColor, true);
	}
	private static void syncConfig(boolean loadConfigFromFile, boolean readFieldsFromConfig) 
	{
		if (loadConfigFromFile) {
			config.load();
		}
		
		displayPosition = config.getString("displayPosition", CATEGORY_RESOURCE_PACKS, "disabled", "Sets the position of active resource pack list on the screen. Valid values are: disabled, top left, top right, bottom right, bottom left", DisplayPosition.validValues);
		String color = config.getString("displayColor", CATEGORY_RESOURCE_PACKS, "f", "Sets the color of active resource pack list. Valid value is a single lowercase hexadecimal character (0 to f) where '0' is black and 'f' is white", "f0123456789abcde".split(""));
		if (!color.isEmpty())displayColor = color.charAt(0);
		
		propEnabledPacks = config.get(CATEGORY_RESOURCE_PACKS, "enabledPacks", new String[0], "Internal list of enabled resource packs.");
		propEnabledPacks.setShowInGui(false);
		
		enabledPacks = propEnabledPacks.getStringList();
		
		if (enabledPacks.length == 0){
			List<String> packs = Minecraft.getMinecraft().gameSettings.resourcePacks;
			enabledPacks = packs.toArray(new String[packs.size()]);
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
		
		private void onConfigLoaded(){
			if (getDisplayPosition() == DisplayPosition.DISABLED){
				RenderPackListOverlay.unregister();
			}
			else{
				RenderPackListOverlay.register();
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
