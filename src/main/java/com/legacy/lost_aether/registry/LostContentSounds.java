package com.legacy.lost_aether.registry;

import com.legacy.lost_aether.LostContentMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;

public class LostContentSounds
{
	public static SoundEvent ENTITY_AERWHALE_KING_IDLE;
	public static SoundEvent ENTITY_AERWHALE_KING_HURT;
	public static SoundEvent ENTITY_AERWHALE_KING_DEATH;

	public static IForgeRegistry<SoundEvent> soundRegistry;

	public static void init(Register<SoundEvent> event)
	{
		soundRegistry = event.getRegistry();

		ENTITY_AERWHALE_KING_IDLE = register("entity.aerwhale_king.idle");
		ENTITY_AERWHALE_KING_HURT = register("entity.aerwhale_king.hurt");
		ENTITY_AERWHALE_KING_DEATH = register("entity.aerwhale_king.death");
	}

	private static SoundEvent register(String name)
	{
		ResourceLocation location = LostContentMod.locate(name);
		SoundEvent sound = new SoundEvent(location);
		sound.setRegistryName(location);

		if (soundRegistry != null)
			soundRegistry.register(sound);

		return sound;
	}

}