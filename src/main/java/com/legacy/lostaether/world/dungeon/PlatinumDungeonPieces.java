package com.legacy.lostaether.world.dungeon;

import java.util.Random;

import com.legacy.lostaether.LostAetherContent;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class PlatinumDungeonPieces 
{
	WorldServer worldServer;
	MinecraftServer minecraftServer;
	TemplateManager templateManager;
	public Template ground_1;
	public Template ground_2;
	public Template ground_3;
	public Template ground_4;
	public Template tower_1;
	public Template tower_2;
	public Template tower_3;
	public Template tower_4;
	
	// Stuff for Raptor's addon
	public Template skyroot_tower_1;
	public Template skyroot_tower_2;
	public Template skyroot_tower_3;
	public Template skyroot_tower_4;
	
	public PlatinumDungeonPieces(World world, Random rand)
	{
		this.init(world, rand);
	}
	
	private void init(World world, Random rand)
	{
		worldServer = (WorldServer) world;
		minecraftServer = world.getMinecraftServer();
		templateManager = worldServer.getStructureTemplateManager();
		
		ground_1 = register("platinum_ground_1");
		ground_2 = register("platinum_ground_2");
		ground_3 = register("platinum_ground_3");
		ground_4 = register("platinum_ground_4");

		tower_1 = register("platinum_dungeon_1");
		tower_2 = register("platinum_dungeon_2");
		tower_3 = register("platinum_dungeon_3");
		tower_4 = register("platinum_dungeon_4");
		
		skyroot_tower_1 = register("continuation/platinum_dungeon_1");
		skyroot_tower_2 = register("continuation/platinum_dungeon_2");
		skyroot_tower_3 = register("continuation/platinum_dungeon_3");
		skyroot_tower_4 = register("continuation/platinum_dungeon_4");
	}
	
	private Template register(String file)
	{
		return templateManager.getTemplate(minecraftServer, new ResourceLocation(LostAetherContent.MODID + ":platinum_dungeon/" + file));
	}
}
