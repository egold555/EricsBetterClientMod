package org.golde.forge.betterminecraft.client.bettermenus;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import org.golde.forge.betterminecraft.Reflection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/***
 * Make the splash text wish me a happy birthday
 * Disable blinking realms button that is distracting.
 * @author Eric
 *
 */
@SideOnly(Side.CLIENT)
public class BetterMainMenu extends GuiMainMenu {

	@Override
	public void initGui() {
		super.initGui();
		
		/*
		 * Birthday wishes
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if(calendar.get(2) + 1 == 4 && calendar.get(5) == 12) {
			Reflection.setField(this, GuiMainMenu.class, "splashText", "Happy birthday Eric!");
		}
		
		/*
		 * Stop blinking relms notifications of news
		 */
		Minecraft.getMinecraft().gameSettings.realmsNotifications = false;
	}

}
