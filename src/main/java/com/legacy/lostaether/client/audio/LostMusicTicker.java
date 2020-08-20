package com.legacy.lostaether.client.audio;

import com.gildedgames.the_aether.registry.sounds.SoundsAether;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LostMusicTicker implements ITickable
{
	private final Minecraft mc;
	private ISound menuMusic;

	public LostMusicTicker(Minecraft mcIn)
	{
		this.mc = mcIn;
	}

	public void update()
	{
		TrackType tracktype = TrackType.AETHER_MENU_ONE;

		if (this.mc.player == null && Minecraft.getMinecraft().getAmbientMusicType() == MusicType.MENU)
		{
			/*if (AetherConfig.visual_options.menu_enabled && LostContentConfig.visual.aether_menu && !this.mc.getSoundHandler().isSoundPlaying(this.menuMusic))
			{
				this.playMusic(tracktype);
			}
			else// if (this.mc.getSoundHandler().isSoundPlaying(this.menuMusic))
			{
				stopMusic();
			}*/			
		}
	}

	public boolean playingMusic()
	{
		return this.menuMusic != null;
	}

	public void playMusic(TrackType requestedMusicType)
	{
		this.menuMusic = PositionedSoundRecord.getMusicRecord(requestedMusicType.getMusicLocation());
		this.mc.getSoundHandler().playSound(this.menuMusic);
	}

	public void stopMusic()
	{
		if (this.menuMusic != null)
		{
			this.mc.getSoundHandler().stopSound(this.menuMusic);
			this.menuMusic = null;
		}
	}

	@SideOnly(Side.CLIENT)
	public static enum TrackType
	{
		AETHER_MENU_ONE(SoundsAether.aether_menu, 0, 5);

		private final SoundEvent musicLocation;
		private final int minDelay;
		private final int maxDelay;

		private TrackType(SoundEvent musicLocationIn, int minDelayIn, int maxDelayIn)
		{
			this.musicLocation = musicLocationIn;
			this.minDelay = minDelayIn;
			this.maxDelay = maxDelayIn;
		}

		public SoundEvent getMusicLocation()
		{
			return this.musicLocation;
		}

		public int getMinDelay()
		{
			return this.minDelay;
		}

		public int getMaxDelay()
		{
			return this.maxDelay;
		}
	}

}