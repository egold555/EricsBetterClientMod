package org.golde.forge.betterminecraft.client.bettermenus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.golde.forge.betterminecraft.Reflection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

/**
 * Removes the stupid buttons like auto jump and reset keys. They are useless and annoying when clicked.
 * @author Eric
 *
 */
@SideOnly(Side.CLIENT)
public class BetterOptionsMenu extends GuiControls{

	public BetterOptionsMenu(GuiScreen screen) {
		super(screen, Minecraft.getMinecraft().gameSettings);
	}

	@Override
	public void initGui(){

		/*
		 * Remove auto jump button
		 * Called before initGui() so we do not half to update the screen to see changes
		 */
		Options[] OPTIONS_ARR = (Options[]) Reflection.getField(this, GuiControls.class, "OPTIONS_ARR");
		List<Options> list = new ArrayList<Options>(Arrays.asList(OPTIONS_ARR));
		list.remove(Options.AUTO_JUMP);
		Reflection.setField(this, GuiControls.class, "OPTIONS_ARR", list.toArray(new Options[0]));

		super.initGui();
		
		/*
		 * Remove reset controls button
		 */
		GuiButton buttonReset = (GuiButton) Reflection.getField(this, GuiControls.class, "buttonReset");
		buttonReset.visible = false;
		Reflection.setField(this, GuiControls.class, "buttonReset", buttonReset);

		for(GuiButton button: this.buttonList) {
			if(button.displayString.equals("Done")) {
				button.xPosition = this.width / 4;
				button.width = this.height - 10;
			}
		}

	}

	

}
