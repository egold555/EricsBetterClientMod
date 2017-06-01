package org.golde.forge.betterminecraft.client.bettermenus.resourcepack;

import java.util.ArrayList;
import java.util.List;

import org.golde.forge.betterminecraft.config.BMConfig;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class RenderPackListOverlay{
	private static final RenderPackListOverlay instance = new RenderPackListOverlay();
	private static boolean isRegistered;
	
	public static void register(){
		if (isRegistered)return;
		
		isRegistered = true;
		MinecraftForge.EVENT_BUS.register(instance);
		refreshPackNames();
	}
	
	public static void unregister(){
		if (!isRegistered)return;
		
		isRegistered = false;
		MinecraftForge.EVENT_BUS.unregister(instance);
	}

	@SideOnly(Side.CLIENT)
	public static void refreshPackNames(){
		instance.refresh();
	}
	
	private List<String> packNames = new ArrayList<String>(4);
	
	@SideOnly(Side.CLIENT)
	private void refresh(){
		packNames.clear();
		
		List<ResourcePackRepository.Entry> entries = Lists.reverse(Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries());
		
		for(ResourcePackRepository.Entry entry : entries){
			String name = entry.getResourcePackName();
			
			if (name.endsWith(".zip")){
				name = name.substring(0, name.length()-4);
			}
			
			packNames.add(name);
		}
	}
	
	@SubscribeEvent(receiveCanceled = true)
	@SideOnly(Side.CLIENT)
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post e){
		if (e.getType() == ElementType.TEXT && !packNames.isEmpty()){
			DisplayPosition position = BMConfig.getDisplayPosition();
			
			if ((position == DisplayPosition.TOP_LEFT || position == DisplayPosition.TOP_RIGHT) && Minecraft.getMinecraft().gameSettings.showDebugInfo){
				return;
			}
			
			final FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
			final int color = BMConfig.getDisplayColor();
			
			final int edgeDist = 3, topOffset = 2;
			final int ySpacing = font.FONT_HEIGHT;
			
			int x = position == DisplayPosition.TOP_LEFT || position == DisplayPosition.BOTTOM_LEFT ? edgeDist : e.getResolution().getScaledWidth()-edgeDist;
			int y = position == DisplayPosition.TOP_LEFT || position == DisplayPosition.TOP_RIGHT ? edgeDist : e.getResolution().getScaledHeight()-edgeDist-topOffset-ySpacing*(1+packNames.size());
			boolean alignRight = position == DisplayPosition.TOP_RIGHT || position == DisplayPosition.BOTTOM_RIGHT;
			
			renderText(font, TextFormatting.UNDERLINE+"Resource Packs", x, y, color, alignRight);
			
			for(int line = 0; line < packNames.size(); line++){
				renderText(font, packNames.get(line), x, y+topOffset+(line+1)*ySpacing, color, alignRight);
			}
		}
	}
	
	private static void renderText(FontRenderer renderer, String line, int x, int y, int color, boolean alignRight){
		renderer.drawString(line, alignRight ? x-renderer.getStringWidth(line) : x, y, color, color != 0);
	}
}
