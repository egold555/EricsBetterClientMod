package org.golde.forge.betterminecraft.client.bettermenus.resourcepack;

import java.io.File;

import org.golde.forge.betterminecraft.Constants;
import org.golde.forge.betterminecraft.client.bettermenus.BetterResourcePack;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ResourcePackListEntryFolder extends ResourcePackListEntryCustom{
	private static final ResourceLocation folderResource = new ResourceLocation(Constants.PREFIX_MOD + "textures/gui/folder.png"); // http://www.iconspedia.com/icon/folion-icon-27237.html
	
	private final BetterResourcePack ownerScreen;
	
	public final File folder;
	public final String folderName;
	public final boolean isUp;
	
	public ResourcePackListEntryFolder(BetterResourcePack ownerScreen, File folder){
		super(ownerScreen);
		this.ownerScreen = ownerScreen;
		this.folder = folder;
		this.folderName = folder.getName();
		this.isUp = false;
	}
	
	public ResourcePackListEntryFolder(BetterResourcePack ownerScreen, File folder, boolean isUp){
		super(ownerScreen);
		this.ownerScreen = ownerScreen;
		this.folder = folder;
		this.folderName = "..";
		this.isUp = isUp;
	}
	
	@Override
	public void bindResourcePackIcon(){
		mc.getTextureManager().bindTexture(folderResource);
	}
	
	@Override
	public String getResourcePackName(){
		return folderName;
	}
	
	@Override
	public String getResourcePackDescription(){
		return isUp ? "(Back)" : "(Folder)";
	}
	
	@Override
	public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_){
		ownerScreen.moveToFolder(folder);
		return true;
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected){
		GuiUtils.renderFolderEntry(this, x, y, isSelected);
	}
}