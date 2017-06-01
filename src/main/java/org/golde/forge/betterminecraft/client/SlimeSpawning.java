package org.golde.forge.betterminecraft.client;

import java.util.Random;

import org.golde.forge.betterminecraft.config.BMConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class SlimeSpawning
{
  private static final float[] slimeLunar = { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };
  
  public static boolean isInSlimeChunk(long seed, EntityPlayer player)
  {
    return isSlimeChunk(seed, MathHelper.floor(player.posX / 16.0D), 
      MathHelper.floor(player.posZ / 16.0D));
  }
  
  public static boolean isSlimeChunk(World world, int chunkX, int chunkZ)
  {
    return isSlimeChunk(world.getSeed(), chunkX, chunkZ);
  }
  
  private static boolean isSlimeChunk(long seed, int chunkX, int chunkZ)
  {
    Random r = new Random(seed + chunkX * chunkX * 4987142 + chunkX * 5947611 + chunkZ * chunkZ * 4392871L + chunkZ * 389711 ^ 0x3AD8025F);
    
    return r.nextInt(10) == 0;
  }
  
  public static boolean chunkHeightCheck(EntityPlayer player)
  {
    int playerY = (int)player.getPosition().getY();
    return playerY < 40;
  }
  
  public static boolean isInSwamp(World world, EntityPlayer player)
  {
    Biome biome = world.getBiome(new BlockPos(MathHelper.floor(player.posX), 64, MathHelper.floor(player.posZ)));
    return biome == Biomes.SWAMPLAND;
  }
  
  public static boolean swampHeightCheck(EntityPlayer player)
  {
    int playerY = (int)player.getPosition().getY();
    return (playerY < 70) && (playerY > 50);
  }
  
  public static float getSwampSpawnRate(World world)
  {
    return slimeLunar[world.provider.getMoonPhase(world.getWorldTime())];
  }
  
  public static float getNextSwampSpawnRate(World world)
  {
    return slimeLunar[((world.provider.getMoonPhase(world.getWorldTime()) + 1) % 8)];
  }
}
