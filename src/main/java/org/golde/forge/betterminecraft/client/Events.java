package org.golde.forge.betterminecraft.client;

import java.util.List;

import org.golde.forge.betterminecraft.CommonAccess;
import org.golde.forge.betterminecraft.Constants;
import org.golde.forge.betterminecraft.client.bettermenus.*;
import org.golde.forge.betterminecraft.config.BMConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Events {

	Minecraft mc;
	EntityPlayerSP player;
	WorldClient world;
	
	//Slime chunk F3
	//Not so crouded F3 menu
	@SubscribeEvent
	public void onRenderF3(RenderGameOverlayEvent.Text event)
	{
		mc = Minecraft.getMinecraft();
		player = mc.player;
		world = mc.world;


		if(mc.gameSettings.showDebugInfo) {
			removeText(event.getLeft(), "Minecraft");
			removeText(event.getLeft(), "MultiplayerChunk");
			removeText(event.getLeft(), "Debug: Pie");
			removeText(event.getLeft(), "For help");
			removeText(event.getRight(), "MCP");
			removeText(event.getRight(), "Powered By Forge");
			removeText(event.getRight(), "mods loaded");
			removeText(event.getRight(), "Display");
			removeText(event.getRight(), "CPU");
			removeText(event.getRight(), "Geforce");
			removeText(event.getRight(), "NVIDIA");
			removeText(event.getRight(), "Java");
			removeText(event.getLeft(), "All: ");
			removeText(event.getRight(), "Optifine");
			//event.getLeft().add("World Time: " + time());
			addSlimeInfoToList(event.getLeft());
		}
	}

	String time() {
		long timeDay = world.getTotalWorldTime();
		int day = (int)(timeDay / 24000L) + 1;

		int hour = (int)(timeDay / 1000L + 6L) % 24;
		int min = (int)(timeDay / 16.666666D) % 60;
		int sec = (int)(timeDay / 0.277777D) % 60;
		return String.format("Time: %02d:%02d:%02d (day %d)", hour, min, sec, day);
	}

	public void addSlimeInfoToList(List<String> list)
	{
		//System.out.println("S: " + BMConfig.seed);
		if(BMConfig.seed ==0) {return;}
		if ((player == null) || (world == null) /*|| (!this.mc.field_71474_y.field_74330_P)*/ || 
				(!CommonAccess.isInOverworld(player))) {
			return;
		}
		list.add(null);

		boolean seedCheck = false;
		boolean dataAvailable = false;
		Boolean chunkResult = hasSlimeChunk(player);
		if (chunkResult != null)
		{
			dataAvailable = true;
			seedCheck = chunkResult.booleanValue();
		}
		boolean swampCheck = SlimeSpawning.isInSwamp(world, player);
		if (!dataAvailable) {
			list.add("Slime Chunk info unavailable");
		}
		String out;
		if ((seedCheck) && (SlimeSpawning.chunkHeightCheck(player)))
		{
			out = "Yes (Slime Chunk)";
		}
		else
		{
			if ((swampCheck) && (SlimeSpawning.swampHeightCheck(player)))
			{
				out = "Yes (Swamp)";
			}
			else
			{
				out = "No";
				if (!dataAvailable) {
					out = out + "?";
				}
				if ((seedCheck) && (swampCheck)) {
					out = out + " (in Swamp and Slime Chunk, but wrong height)";
				} else if (seedCheck) {
					out = out + " (in Slime Chunk, but wrong height)";
				} else if (swampCheck) {
					out = out + " (in Swamp, but wrong height)";
				}
			}
		}
		Long seed = BMConfig.seed;
		if (seed != null)
		{
			list.add("Seed: " + seed);
		}
		list.add("Slimes: " + out);
		if (swampCheck)
		{
			String lunar = "Spawn Rate (Swamp): ";
			lunar = lunar + SlimeSpawning.getSwampSpawnRate(world) * 100.0F + "% (";
			lunar = lunar + SlimeSpawning.getNextSwampSpawnRate(world) * 100.0F + "% in ";
			lunar = lunar + (24000L - world.getWorldTime() % 24000L + 600L) / 1200L;
			lunar = lunar + "m)";

			list.add(lunar);
		}
	}

	void removeText(List<String> list, String startsWith) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).toLowerCase().contains(startsWith.toLowerCase())) {
				list.remove(i);
			}
		}
	}
	
	public Boolean hasSlimeChunk(EntityPlayer player)
	{
		return SlimeSpawning.isInSlimeChunk(BMConfig.seed, player);
	}

	//Food bars + Space
	@SubscribeEvent
	public void renderFoodSpace(ItemTooltipEvent event) {
		if(!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemFood) {
			int pips = ((ItemFood) event.getItemStack().getItem()).getHealAmount(event.getItemStack());
			int len = (int) Math.ceil((double) pips / 2);

			String s = " ";
			for(int i = 0; i < len; i++)
				s += "  ";

			event.getToolTip().add(1, s);
		}
	}

	@SubscribeEvent
	public void renderFood(RenderTooltipEvent.PostText event) {
		if(event.getStack() != null && event.getStack().getItem() instanceof ItemFood) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F);
			mc.getTextureManager().bindTexture(GuiIngameForge.ICONS);
			int pips = ((ItemFood) event.getStack().getItem()).getHealAmount(event.getStack());
			for(int i = 0; i < Math.ceil((double) pips / 2); i++) {
				Gui.drawModalRectWithCustomSizedTexture(event.getX() + i * 9 - 2, event.getY() + 12, 16, 27, 9, 9, 256, 256);

				int u = 52;
				if(pips % 2 != 0 && i == 0)
					u += 9;

				Gui.drawModalRectWithCustomSizedTexture(event.getX() + i * 9 - 2, event.getY() + 12, u, 27, 9, 9, 256, 256);
			}

			GlStateManager.popMatrix();
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	@SideOnly(Side.CLIENT)
	public void onGuiOpen(GuiOpenEvent e){
		GuiScreen gui = e.getGui();
		if(gui == null) {return;}
		
		if(gui.getClass() == GuiScreenResourcePacks.class){
			e.setGui(new BetterResourcePack(Minecraft.getMinecraft().currentScreen));
		}
		else if(gui.getClass() == GuiMainMenu.class) {
			e.setGui(new BetterMainMenu());
		}
		else if(gui.getClass() == GuiControls.class) {
			e.setGui(new BetterOptionsMenu(Minecraft.getMinecraft().currentScreen));
		}
	}
	

}
