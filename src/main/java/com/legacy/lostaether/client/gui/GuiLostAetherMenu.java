package com.legacy.lostaether.client.gui;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.client.gui.button.GuiAetherButton;
import com.gildedgames.the_aether.client.gui.menu.GuiMenuToggleButton;
import com.legacy.lostaether.LostAetherContent;
import com.legacy.lostaether.LostContentConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLostAetherMenu extends GuiScreen implements GuiYesNoCallback
{
	private static final Logger logger = LogManager.getLogger();
	private static final Random RANDOM = new Random();
	private GuiButton buttonResetDemo;
	private DynamicTexture viewportTexture;
	private final Object threadLock = new Object();
	private String openGLWarning1, openGLWarning2, openGLWarningLink, splashText;
	private static final ResourceLocation minecraftTitleTextures = Aether.locate("textures/gui/title/aether.png");
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] { Aether.locate("textures/gui/title/panorama/panorama_0.png"), Aether.locate("textures/gui/title/panorama/panorama_1.png"), Aether.locate("textures/gui/title/panorama/panorama_2.png"), Aether.locate("textures/gui/title/panorama/panorama_3.png"), Aether.locate("textures/gui/title/panorama/panorama_4.png"), Aether.locate("textures/gui/title/panorama/panorama_5.png") };
	public static final String field_96138_a = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";
	// private static final ResourceLocation SPLASH_TEXTS =
	// LostAetherContent.locate("texts/splashes.txt");
	private int panoramaTimer, field_92024_r, field_92023_s, field_92022_t, field_92021_u, field_92020_v, field_92019_w;
	private ResourceLocation backgroundTexture;

	public GuiLostAetherMenu()
	{
		this.openGLWarning2 = field_96138_a;
		IResource iresource = null;
		this.openGLWarning1 = "";

		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported())
		{
			this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
			this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
			this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}

		try
		{
			List<String> list = LostAetherContent.SPLASHES.getSplashes();
			if (!list.isEmpty())
			{
				while (true)
				{
					this.splashText = list.get(RANDOM.nextInt(list.size()));

					if (this.splashText.hashCode() != 125780783)
					{
						break;
					}
				}
			}
			else
				this.splashText = "Lost Content!";
		}
		finally
		{
			IOUtils.closeQuietly((Closeable) iresource);
		}
	}

	@Override
	public void updateScreen()
	{
		++this.panoramaTimer;
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
	}

	@Override
	public void initGui()
	{
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);

		int j = this.height / 5 + 30;
		int margin = 50 - (this.width < 700 ? 30 : 0);
		int extraWidth = !this.mc.isDemo() && LostContentConfig.visual.server_button ? 24 : 0;

		if (this.mc.isDemo())
		{
			this.addDemoButtons(j, 24);
		}
		else
		{
			this.buttonList.add(new GuiAetherButton(1, width / margin, j, I18n.format("menu.singleplayer", new Object[0])));
			this.buttonList.add(new GuiAetherButton(2, width / margin, j + 24, I18n.format("menu.multiplayer", new Object[0])));

			// menu toggle
			this.buttonList.add(new GuiMenuToggleButton(this.width - 25, 28));

			if (LostContentConfig.visual.server_button)
				this.buttonList.add(new GuiAetherButton(17, width / margin, j + 48 + extraWidth, "Modding Legacy Aether Server"));
		}

		this.buttonList.add(new GuiAetherButton(6, width / margin, j + 48, I18n.format("fml.menu.mods")));

		this.buttonList.add(new GuiAetherButton(0, width / margin, j + 72 + extraWidth, I18n.format("menu.options", new Object[0])));
		this.buttonList.add(new GuiAetherButton(4, width / margin, j + 96 + extraWidth, I18n.format("menu.quit", new Object[0])));
		this.buttonList.add(new GuiButtonLanguage(7, this.width - 25, 4));

		synchronized (this.threadLock)
		{
			this.field_92023_s = this.fontRenderer.getStringWidth(this.openGLWarning1);
			this.field_92024_r = this.fontRenderer.getStringWidth(this.openGLWarning2);
			int k = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - k) / 2;
			this.field_92021_u = ((GuiButton) this.buttonList.get(0)).y - 24;
			this.field_92020_v = this.field_92022_t + k;
			this.field_92019_w = this.field_92021_u + 24;
		}
	}

	private void addDemoButtons(int p_73972_1_, int p_73972_2_)
	{
		this.buttonList.add(new GuiAetherButton(11, width / 70, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
		this.buttonList.add(this.buttonResetDemo = new GuiAetherButton(12, width / 70, p_73972_1_ + 24, I18n.format("menu.resetdemo", new Object[0])));

		ISaveFormat isaveformat = this.mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

		if (worldinfo == null)
		{
			this.buttonResetDemo.enabled = false;
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.id == 0)
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));

		if (button.id == 5)
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));

		if (button.id == 1)
			this.mc.displayGuiScreen(new GuiWorldSelection(this));

		if (button.id == 2)
			this.mc.displayGuiScreen(new GuiMultiplayer(this));

		if (button.id == 4)
			this.mc.shutdown();

		if (button.id == 6)
			this.mc.displayGuiScreen(new GuiModList(this));

		if (button.id == 11)
			this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);

		if (button.id == 12)
		{
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

			if (worldinfo != null)
			{
				this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion"), "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning"), I18n.format("selectWorld.deleteButton"), I18n.format("gui.cancel"), 12));
			}
		}

		if (button.id == 7)
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));

		if (button.id == 17)
			FMLClientHandler.instance().connectToServer(new GuiMultiplayer(this), new ServerData("Modding Legacy", "aether.moddinglegacy.com", false));
	}

	@Override
	public void confirmClicked(boolean result, int id)
	{
		if (result && id == 12)
		{
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		}
		else if (id == 13)
		{
			if (result)
			{
				try
				{
					Class<?> oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { new URI(this.openGLWarningLink) });
				}
				catch (Throwable throwable)
				{
					logger.error("Couldn\'t open link", throwable);
				}
			}

			this.mc.displayGuiScreen(this);
		}
	}

	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		int i = 8;

		for (int j = 0; j < i * i; ++j)
		{
			GlStateManager.pushMatrix();
			float f = ((float) (j % i) / (float) i - 0.5F) / 64.0F;
			float f1 = ((float) (j / i) / (float) i - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, f2);
			GlStateManager.rotate(MathHelper.sin(((float) this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-((float) this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int k = 0; k < 6; ++k)
			{
				GlStateManager.pushMatrix();

				if (k == 1)
				{
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 2)
				{
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 3)
				{
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 4)
				{
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (k == 5)
				{
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	private void rotateAndBlurSkybox(float p_73968_1_)
	{
		this.mc.getTextureManager().bindTexture(this.backgroundTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		int i = 3;

		for (int j = 0; j < i; ++j)
		{
			float f = 1.0F / (float) (j + 1);
			int k = this.width;
			int l = this.height;
			float f1 = (float) (j - i / 2) / 256.0F;
			worldrenderer.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
	{
		this.mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		float f = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
		float f1 = (float) this.height * f / 256.0F;
		float f2 = (float) this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double) i, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos((double) i, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.disableAlpha();
		this.renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		byte var6 = 15;
		byte var7 = 15;
		this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 155, 44);
		drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.translate(180.0F, 50.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		float f = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
		f = f * 100.0F / (float) (this.fontRenderer.getStringWidth(this.splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRenderer, this.splashText.replace("%s", "" + GetAlphaTime.getYearsInAlpha()).replace("%a", ""), 5, -5, -256);
		GlStateManager.popMatrix();
		// drawFooter();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.popMatrix();
		String s = "Minecraft 1.12.2";

		if (this.mc.isDemo())
		{
			s = s + " Demo";
		}

		java.util.List<String> brandings = com.google.common.collect.Lists.reverse(net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
		for (int brdline = 0; brdline < brandings.size(); brdline++)
		{
			String brd = brandings.get(brdline);
			if (!com.google.common.base.Strings.isNullOrEmpty(brd))
				this.drawString(this.fontRenderer, brd, (width - this.fontRenderer.getStringWidth(brd)), this.height - (10 + brdline * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
		}

		ForgeVersion.Status status = ForgeVersion.getStatus();
		if (status == ForgeVersion.Status.BETA || status == ForgeVersion.Status.BETA_OUTDATED)
		{
			String line = I18n.format("forge.update.beta.1", TextFormatting.RED, TextFormatting.RESET);
			this.drawString(this.fontRenderer, line, (width - this.fontRenderer.getStringWidth(line)) / 2, 4 + (0 * (this.fontRenderer.FONT_HEIGHT + 1)), -1);
			line = I18n.format("forge.update.beta.2");
			this.drawString(this.fontRenderer, line, (width - this.fontRenderer.getStringWidth(line)) / 2, 4 + (1 * (this.fontRenderer.FONT_HEIGHT + 1)), -1);
		}

		String line = null;
		switch (status)
		{
		case OUTDATED:
		case BETA_OUTDATED:
			line = I18n.format("forge.update.newversion", ForgeVersion.getTarget());
			break;
		default:
			break;
		}

		if (line != null)
		{
			this.drawString(this.fontRenderer, line, 2, height - (2 * (this.fontRenderer.FONT_HEIGHT + 1)), -1);
		}

		String s1 = "Copyright Mojang AB. Do not distribute!";
		this.drawString(this.fontRenderer, s1, 2, this.height - 10, -1);

		if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0)
		{
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
			this.drawString(this.fontRenderer, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
			this.drawString(this.fontRenderer, this.openGLWarning2, (this.width - this.field_92024_r) / 2, ((GuiButton) this.buttonList.get(0)).y - 12, -1);
		}

		/*java.util.List<String> brandings = com.google.common.collect.Lists.reverse(net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
		for (int brdline = 0; brdline < brandings.size(); brdline++)
		{
		    String brd = brandings.get(brdline);
		    if (!com.google.common.base.Strings.isNullOrEmpty(brd))
		    {
		        int offset = 30;
		
		        if (!modUpdateNotification.shouldRender())
		        {
		            offset = 20;
		        }
		
		        this.drawString(this.fontRenderer, brd, this.width - this.fontRenderer.getStringWidth(brd) - 2, this.height - (offset + brdline * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
		    }
		}
		
		this.drawString(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, -1);
		
		if (mouseX > this.widthCopyrightRest && mouseX < this.widthCopyrightRest + this.widthCopyright && mouseY > this.height - 10 && mouseY < this.height && Mouse.isInsideWindow())
		{
		    drawRect(this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, -1);
		}
		
		if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty())
		{
		    drawRect(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
		    this.drawString(this.fontRenderer, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
		    this.drawString(this.fontRenderer, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, (this.buttonList.get(0)).y - 12, -1);
		}*/

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		synchronized (this.threadLock)
		{
			if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w)
			{
				GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
				guiconfirmopenlink.disableSecurityWarning();
				this.mc.displayGuiScreen(guiconfirmopenlink);
			}
		}
	}

	public static class GetAlphaTime
	{
		public GetAlphaTime()
		{
		}

		public static int getYearsInAlpha()
		{
			LocalDate releaseDate = LocalDate.of(2013, 06, 9);
			LocalDate currentDate = LocalDate.now();
			Period difference = Period.between(releaseDate, currentDate);
			return difference.getYears();
		}
	}
}