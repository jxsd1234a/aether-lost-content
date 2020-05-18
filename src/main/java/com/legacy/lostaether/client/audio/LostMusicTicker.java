package com.legacy.lostaether.client.audio;

import java.util.Random;

import com.legacy.aether.registry.sounds.SoundsAether;

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
	private final Random rand = new Random();
	private final Minecraft mc;
	private ISound menuMusic;
    private int timeUntilNextMusic = 100;

	public LostMusicTicker(Minecraft mcIn)
	{
		this.mc = mcIn;
	}

	public void update()
	{
		TrackType tracktype = TrackType.AETHER_MENU_ONE;

		if (this.mc.player == null && Minecraft.getMinecraft().getAmbientMusicType() == MusicType.MENU)
		{
			if (!this.mc.getSoundHandler().isSoundPlaying(this.menuMusic))
			{
				/*this.menuMusic = new AetherUnpositionedSound(SoundsAether.aether_menu_2);
				this.mc.getSoundHandler().playSound(this.menuMusic);*/
				
				this.playMusic(tracktype);
			}
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
		this.timeUntilNextMusic = Integer.MAX_VALUE;
	}

	public void stopMusic()
	{
		if (this.menuMusic != null)
		{
			this.mc.getSoundHandler().stopSound(this.menuMusic);
			this.menuMusic = null;
			this.timeUntilNextMusic = 0;
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