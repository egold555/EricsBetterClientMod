package org.golde.forge.betterminecraft;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonAccess
{
  public static MinecraftServer getWorldServer()
  {
    return FMLCommonHandler.instance().getMinecraftServerInstance();
  }
  
  public static boolean isInOverworld(Entity e)
  {
    return e.dimension == 0;
  }
}
