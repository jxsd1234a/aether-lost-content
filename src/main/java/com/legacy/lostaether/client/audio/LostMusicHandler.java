package com.legacy.lostaether.client.audio;

import com.legacy.aether.Aether;
import com.legacy.lostaether.LostContentConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LostMusicHandler
{
	private Minecraft mc = Minecraft.getMinecraft();
	private LostMusicTicker musicTicker = new LostMusicTicker(mc);

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) throws Exception
	{
		TickEvent.Phase phase = event.phase;
		TickEvent.Type type = event.type;

		if (phase == TickEvent.Phase.END)
		{
			if (type.equals(TickEvent.Type.CLIENT))
			{
				if (!mc.isGamePaused())
				{
					musicTicker.update();
				}
			}
		}
	}

	@SubscribeEvent
	public void onMusicControl(PlaySoundEvent event)
	{
		ISound sound = event.getResultSound();

		if (sound == null)
		{
			return;
		}

		SoundCategory category = sound.getCategory();

		if (category == SoundCategory.MUSIC)
		{
			if (LostContentConfig.visual.aether_menu && !sound.getSoundLocation().toString().contains(Aether.modid))
			{
				if ((Minecraft.getMinecraft().getAmbientMusicType() == MusicType.MENU))
				{
					event.setResultSound(null);
					return;
				}
			}
		}
	}
}