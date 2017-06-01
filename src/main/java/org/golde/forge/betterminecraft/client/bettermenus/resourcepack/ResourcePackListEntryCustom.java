package org.golde.forge.betterminecraft.client.bettermenus.resourcepack;

import org.golde.forge.betterminecraft.client.bettermenus.BetterResourcePack;

import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ResourcePackListEntryCustom extends ResourcePackListEntryFound{
	public ResourcePackListEntryCustom(BetterResourcePack ownerScreen){
		super(ownerScreen, null);
	}
	
	@Override
	public abstract void bindResourcePackIcon();
	
	@Override
	public abstract String getResourcePackName();
	
	@Override
	public abstract String getResourcePackDescription();
	
	@Override
	public boolean showHoverOverlay(){
		return super.showHoverOverlay();
	}
}