package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.beta.TextureNewClockFX;
import net.lax1dude.eaglercraft.beta.TextureNewCompassFX;
import net.lax1dude.eaglercraft.internal.PlatformApplication;
import net.lax1dude.eaglercraft.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.minecraft.EaglerFontRenderer;
import net.lax1dude.eaglercraft.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiUnused;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.Teleporter;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.Timer;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldProviderHell;
import net.minecraft.src.WorldRenderer;
import net.peyton.eagler.minecraft.DetectAnisotropicGlitch;
import net.peyton.eagler.minecraft.FontRenderer;
import net.peyton.eagler.minecraft.Tessellator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Minecraft {
	public PlayerController playerController;
	public int displayWidth;
	public int displayHeight;
	public float dpi;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public boolean field_6317_l = true;
	public volatile boolean isWorldLoaded = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private int ticksRan = 0;
	private int field_6282_S = 0;
	public String field_6310_s = null;
	public int field_6309_t = 0;
	public GuiIngame ingameGUI;
	public boolean field_6307_v = false;
	public ModelBiped unusedModelBiped = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings gameSettings;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public TexturePackList texturePackList;
	public VFile2 mcDataDir;
	public static long[] frameTimes = new long[512];
	public static long[] tickTimes = new long[512];
	public static int numRecordedFrameTimes = 0;
	private static VFile2 minecraftDir = null;
	public volatile boolean running = true;
	public String debug = "";
	boolean isTakingScreenshot = false;
	long prevFrameTime = -1L;
	public boolean inGameHasFocus = false;
	private int field_6302_aa = 0;
	public boolean isFancyGraphics = false;
	long systemTime = EagRuntime.steadyTimeMillis();
	private int field_6300_ab = 0;
	
	private static Minecraft minecraft;
	
	private boolean isShuttingDown = false;
	private static int debugFPS;
	
	public ScaledResolution scaledResolution = null;
	
	private boolean checkErrors;
	private int dontPauseTimer = 0;
	
	private Logger LOGGER = LogManager.getLogger();

	public Minecraft() {
		this.displayWidth = Display.getWidth();
		this.displayHeight = Display.getHeight();
		this.session = new Session("Player");
		checkErrors = EagRuntime.getConfiguration().isCheckGLErrors();
		minecraft = this;
	}
	
	public void updateDisplay() {
		if(Display.isVSyncSupported()) {
			//Fix for WASM and menu input lag
			if(/*EagRuntime.getPlatformType() == EnumPlatformType.WASM_GC ||*/ (this.theWorld == null || this.currentScreen != null)) {
				Display.setVSync(true);
			} else {
				Display.setVSync(this.gameSettings.limitFramerate);
			}
		}
		Display.update(0);
		this.checkWindowResize();
	}
	
	protected void checkWindowResize() {
		float dpiFetch = -1.0f;
		if ((Display.wasResized() || (dpiFetch = Math.max(Display.getDPI(), 1.0f)) != this.dpi)) {
			int i = this.displayWidth;
			int j = this.displayHeight;
			float f = this.dpi;
			this.displayWidth = Display.getWidth();
			this.displayHeight = Display.getHeight();
			this.dpi = dpiFetch == -1.0f ? Math.max(Display.getDPI(), 1.0f) : dpiFetch;
			if (this.displayWidth != i || this.displayHeight != j || this.dpi != f) {
				if (this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if (this.displayHeight <= 0) {
					this.displayHeight = 1;
				}

				this.resize(this.displayWidth, this.displayHeight);
			}
		}
	}

	public void startGame() throws LWJGLException {
		Display.setTitle("Minecraft Beta 1.1_02");

		try {
			Display.create();
		} catch (LWJGLException var6) {
		}

		this.scaledResolution = new ScaledResolution(this.displayWidth, this.displayHeight);
		RenderManager.instance.field_4236_f = new ItemRenderer(this);
		this.mcDataDir = getMinecraftDir();
		this.gameSettings = new GameSettings(this, this.mcDataDir);
		this.texturePackList = new TexturePackList(this, this.mcDataDir);
		this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
		this.fontRenderer = EaglerFontRenderer.createSupportedFontRenderer(this.gameSettings, "/font/default.png", this.renderEngine);
		this.loadScreen();
		this.mouseHelper = new MouseHelper();
		
		ColorizerFoliage.init();
		ColorizerGrass.init();

		this.checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.checkGLError("Startup");
		this.sndManager.init(this.gameSettings);
		
		renderEngine.registerTextureFX(new TextureNewCompassFX());
		renderEngine.registerTextureFX(new TextureNewClockFX());
		renderEngine.registerSpriteSheet("portal", Block.portal.blockIndexInTexture, 1);
		renderEngine.registerSpriteSheet("water", Block.waterStill.blockIndexInTexture, 1);
		renderEngine.registerSpriteSheet("water_flow", Block.waterMoving.blockIndexInTexture + 1, 2);
		renderEngine.registerSpriteSheet("lava", Block.lavaStill.blockIndexInTexture, 1);
		renderEngine.registerSpriteSheet("lava_flow", Block.lavaMoving.blockIndexInTexture + 1, 2);
		renderEngine.registerSpriteSheet("fire_0", Block.fire.blockIndexInTexture, 1);
		renderEngine.registerSpriteSheet("fire_1", Block.fire.blockIndexInTexture + 16, 1);
		
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		GlStateManager.anisotropicPatch(PlatformOpenGL.checkAnisotropicFilteringSupport() && DetectAnisotropicGlitch.hasGlitch());
		this.displayGuiScreen(new GuiMainMenu());
	}
	
	private void updateDisplayMode() {
		this.displayWidth = Display.getWidth();
		this.displayHeight = Display.getHeight();
		this.dpi = Display.getDPI();
		this.scaledResolution = new ScaledResolution(this.displayWidth, this.displayHeight);
	}

	private void loadScreen() throws LWJGLException {
		Display.update();
		updateDisplayMode();
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double) scaledResolution.getScaledWidth(),
				(double) scaledResolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(255, 255, 255, 255);
		tessellator.addVertexWithUV(0.0D, (double) this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV((double) this.displayWidth, (double) this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV((double) this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		short short1 = 256;
		short short2 = 256;
		this.func_6274_a((scaledResolution.getScaledWidth() - short1) / 2,
				(scaledResolution.getScaledHeight() - short2) / 2, 0, 0, short1, short2, 255, 255, 255, 255);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		this.updateDisplay();
	}
	
	public void func_6274_a(int parInt1, int parInt2, int parInt3, int parInt4, int parInt5, int parInt6, int parInt7,
			int parInt8, int parInt9, int parInt10) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(parInt7, parInt8, parInt9, parInt10);
		tessellator.addVertexWithUV((double) parInt1, (double) (parInt2 + parInt6), 0.0D, (double) ((float) parInt3 * f), (double) ((float) (parInt4 + parInt6) * f1));
		tessellator.addVertexWithUV((double) (parInt1 + parInt5), (double) (parInt2 + parInt6), 0.0D, (double) ((float) (parInt3 + parInt5) * f), (double) ((float) (parInt4 + parInt6) * f1));
		tessellator.addVertexWithUV((double) (parInt1 + parInt5), (double) parInt2, 0.0D, (double) ((float) (parInt3 + parInt5) * f), (double) ((float) parInt4 * f1));
		tessellator.addVertexWithUV((double) parInt1, (double) parInt2, 0.0D, (double) ((float) parInt3 * f), (double) ((float) parInt4 * f1));
		tessellator.draw();
	}

	public void func_6274_a(int var1, int var2, int var3, int var4, int var5, int var6) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double)(var1 + 0), (double)(var2 + var6), 0.0D, (double)((float)(var3 + 0) * var7), (double)((float)(var4 + var6) * var8));
		var9.addVertexWithUV((double)(var1 + var5), (double)(var2 + var6), 0.0D, (double)((float)(var3 + var5) * var7), (double)((float)(var4 + var6) * var8));
		var9.addVertexWithUV((double)(var1 + var5), (double)(var2 + 0), 0.0D, (double)((float)(var3 + var5) * var7), (double)((float)(var4 + 0) * var8));
		var9.addVertexWithUV((double)(var1 + 0), (double)(var2 + 0), 0.0D, (double)((float)(var3 + 0) * var7), (double)((float)(var4 + 0) * var8));
		var9.draw();
	}

	public static VFile2 getMinecraftDir() {
		if(minecraftDir == null) {
			minecraftDir = new VFile2("minecraft");
		}

		return minecraftDir;
	}

	public void displayGuiScreen(GuiScreen var1) {
		if(!(this.currentScreen instanceof GuiUnused)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(var1 == null && this.theWorld == null) {
				var1 = new GuiMainMenu();
			} else if(var1 == null && this.thePlayer.health <= 0) {
				var1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)var1;
			this.scaledResolution = new ScaledResolution(this.displayWidth, this.displayHeight);
			if(var1 != null) {
				this.func_6273_f();
				((GuiScreen)var1).setWorldAndResolution(this, this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
				this.field_6307_v = false;
			} else {
				this.func_6259_e();
			}

		}
	}

	private void checkGLError(String var1) {
		if(checkErrors) {
			int var2 = GL11.glGetError();
			if(var2 != 0) {
				String var3 = GLU.gluErrorString(var2);
				LOGGER.error("########## GL ERROR ##########");
				LOGGER.error("@ {}", var1);
				LOGGER.info("{}: {}", var2, var3);
			}
		}
	}
	
	public void shutdownMinecraftApplet() {
		LOGGER.info("Stopping!");
		isShuttingDown = true;
		this.func_6261_a((World)null);

		try {
			GLAllocation.deleteTexturesAndDisplayLists();
		} catch (Exception var6) {
		}

		this.sndManager.closeMinecraft();
		EagRuntime.exit();
		System.gc();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Exception var15) {
			LOGGER.error(var15);
			throw new RuntimeException("Failed to start game", var15);
		}

		try {
			try {
				long var1 = EagRuntime.steadyTimeMillis();
				int var3 = 0;

				while(this.running) {
					AxisAlignedBB.clearBoundingBoxPool();
					Vec3D.initialize();
					
					Display.checkContextLost();
					
					if(Display.isCloseRequested()) {
						this.shutdown();
					}

					if(this.isWorldLoaded && this.theWorld != null) {
						float var4 = this.timer.renderPartialTicks;
						this.timer.updateTimer();
						this.timer.renderPartialTicks = var4;
					} else {
						this.timer.updateTimer();
					}

					long var19 = EagRuntime.nanoTime();

					for(int var6 = 0; var6 < this.timer.elapsedTicks; ++var6) {
						++this.ticksRan;
						this.runTick();
					}

					long var20 = EagRuntime.nanoTime() - var19;
					this.checkGLError("Pre render");
					this.sndManager.func_338_a(this.thePlayer, this.timer.renderPartialTicks);
					
					EaglercraftGPU.optimize();
					PlatformOpenGL._wglBindFramebuffer(0x8D40, null);
					GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
					GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
					GL11.glPushMatrix();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					
					if(this.theWorld != null && !this.theWorld.multiplayerWorld) {
						while(this.theWorld.func_6465_g()) {
						}
					}

					if(this.theWorld != null && this.theWorld.multiplayerWorld) {
						this.theWorld.func_6465_g();
					}

					if(!Keyboard.isKeyDown(Keyboard.KEY_F7)) {
						this.updateDisplay();
					}

					if(!this.field_6307_v) {
						if(this.playerController != null) {
							this.playerController.setPartialTime(this.timer.renderPartialTicks);
						}

						this.entityRenderer.func_4136_b(this.timer.renderPartialTicks);
					}
					
					GL11.glPopMatrix();

					if(Keyboard.isKeyDown(Keyboard.KEY_F3)) {
						this.displayDebugInfo(var20);
					} else {
						this.prevFrameTime = EagRuntime.nanoTime();
					}

					if(Keyboard.isKeyDown(Keyboard.KEY_F7)) {
						this.updateDisplay();
					}

					this.screenshotListener();
					this.checkGLError("Post render");
					++var3;

					for(this.isWorldLoaded = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); EagRuntime.steadyTimeMillis() >= var1 + 1000L; var3 = 0) {
						debugFPS = var3;
						this.debug = var3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
						WorldRenderer.chunksUpdated = 0;
						var1 += 1000L;
					}
				}
			} catch (Throwable var17) {
				this.theWorld = null;
				LOGGER.error(var17);
				throw new RuntimeException("Unexpected error", var17);
			}
		} finally {
			this.shutdownMinecraftApplet();
		}
	}

	private void screenshotListener() {
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			if(!this.isTakingScreenshot) {
				//TODO
//				if(Keyboard.isKeyDown(Keyboard.KEY_F1)) {
//					this.ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, this.displayWidth, this.displayHeight));
//				}

				this.isTakingScreenshot = true;
			}
		} else {
			this.isTakingScreenshot = false;
		}

	}

	private void displayDebugInfo(long var1) {
		long var3 = 16666666L;
		if(this.prevFrameTime == -1L) {
			this.prevFrameTime = EagRuntime.nanoTime();
		}

		long var5 = EagRuntime.nanoTime();
		tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = var1;
		frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = var5 - this.prevFrameTime;
		this.prevFrameTime = var5;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator var7 = Tessellator.instance;
		var7.startDrawing(7);
		int var8 = (int)(var3 / 200000L);
		var7.setColorOpaque_I(536870912);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8), 0.0D);
		var7.setColorOpaque_I(538968064);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8 * 2), 0.0D);
		var7.addVertex(0.0D, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8), 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var8 * 2), 0.0D);
		var7.draw();
		long var9 = 0L;

		int var11;
		for(var11 = 0; var11 < frameTimes.length; ++var11) {
			var9 += frameTimes[var11];
		}

		var11 = (int)(var9 / 200000L / (long)frameTimes.length);
		var7.startDrawing(7);
		var7.setColorOpaque_I(541065216);
		var7.addVertex(0.0D, (double)(this.displayHeight - var11), 0.0D);
		var7.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		var7.addVertex((double)frameTimes.length, (double)(this.displayHeight - var11), 0.0D);
		var7.draw();
		var7.startDrawing(1);

		for(int var12 = 0; var12 < frameTimes.length; ++var12) {
			int var13 = (var12 - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
			int var14 = var13 * var13 / 255;
			var14 = var14 * var14 / 255;
			int var15 = var14 * var14 / 255;
			var15 = var15 * var15 / 255;
			if(frameTimes[var12] > var3) {
				var7.setColorOpaque_I(-16777216 + var14 * 65536);
			} else {
				var7.setColorOpaque_I(-16777216 + var14 * 256);
			}

			long var16 = frameTimes[var12] / 200000L;
			long var18 = tickTimes[var12] / 200000L;
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - var16) + 0.5F), 0.0D);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)this.displayHeight + 0.5F), 0.0D);
			var7.setColorOpaque_I(-16777216 + var14 * 65536 + var14 * 256 + var14 * 1);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - var16) + 0.5F), 0.0D);
			var7.addVertex((double)((float)var12 + 0.5F), (double)((float)((long)this.displayHeight - (var16 - var18)) + 0.5F), 0.0D);
		}

		var7.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void shutdown() {
		this.running = false;
	}

	public void func_6259_e() {
		if(!this.inGameHasFocus && Display.isActive()) {
			this.inGameHasFocus = true;
			this.mouseHelper.func_774_a();
			this.displayGuiScreen((GuiScreen)null);
			this.field_6302_aa = this.ticksRan + 10000;
		}
	}

	public void func_6273_f() {
		if(this.inGameHasFocus) {
			if(this.thePlayer != null) {
				this.thePlayer.resetPlayerKeyState();
			}

			this.inGameHasFocus = false;
			this.mouseHelper.func_773_b();
		}
	}

	public void func_6252_g() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void func_6254_a(int var1, boolean var2) {
		if(!this.playerController.field_1064_b) {
			if(var1 != 0 || this.field_6282_S <= 0) {
				if(var2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0 && var1 == 0) {
					int var3 = this.objectMouseOver.blockX;
					int var4 = this.objectMouseOver.blockY;
					int var5 = this.objectMouseOver.blockZ;
					this.playerController.sendBlockRemoving(var3, var4, var5, this.objectMouseOver.sideHit);
					this.effectRenderer.func_1191_a(var3, var4, var5, this.objectMouseOver.sideHit);
				} else {
					this.playerController.func_6468_a();
				}

			}
		}
	}

	private void clickMouse(int var1) {
		if(var1 != 0 || this.field_6282_S <= 0) {
			if(var1 == 0) {
				this.thePlayer.swingItem();
			}

			boolean var2 = true;
			if(this.objectMouseOver == null) {
				if(var1 == 0 && !(this.playerController instanceof PlayerControllerTest)) {
					this.field_6282_S = 10;
				}
			} else if(this.objectMouseOver.typeOfHit == 1) {
				if(var1 == 0) {
					this.playerController.func_6472_b(this.thePlayer, this.objectMouseOver.entityHit);
				}

				if(var1 == 1) {
					this.playerController.func_6475_a(this.thePlayer, this.objectMouseOver.entityHit);
				}
			} else if(this.objectMouseOver.typeOfHit == 0) {
				int var3 = this.objectMouseOver.blockX;
				int var4 = this.objectMouseOver.blockY;
				int var5 = this.objectMouseOver.blockZ;
				int var6 = this.objectMouseOver.sideHit;
				Block var7 = Block.blocksList[this.theWorld.getBlockId(var3, var4, var5)];
				if(var1 == 0) {
					this.theWorld.onBlockHit(var3, var4, var5, this.objectMouseOver.sideHit);
					if(var7 != Block.bedrock || this.thePlayer.field_9371_f >= 100) {
						this.playerController.clickBlock(var3, var4, var5, this.objectMouseOver.sideHit);
					}
				} else {
					ItemStack var8 = this.thePlayer.inventory.getCurrentItem();
					int var9 = var8 != null ? var8.stackSize : 0;
					if(this.playerController.sendPlaceBlock(this.thePlayer, this.theWorld, var8, var3, var4, var5, var6)) {
						var2 = false;
						this.thePlayer.swingItem();
					}

					if(var8 == null) {
						return;
					}

					if(var8.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if(var8.stackSize != var9) {
						this.entityRenderer.itemRenderer.func_9449_b();
					}
				}
			}

			if(var2 && var1 == 1) {
				ItemStack var10 = this.thePlayer.inventory.getCurrentItem();
				if(var10 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, var10)) {
					this.entityRenderer.itemRenderer.func_9450_c();
				}
			}

		}
	}

	private void resize(int var1, int var2) {
		this.displayWidth = Math.max(1, var1);
		this.displayHeight = Math.max(1, var2);
		this.scaledResolution = new ScaledResolution(this.displayWidth, this.displayHeight);
		
		if(this.currentScreen != null) {
			this.currentScreen.setWorldAndResolution(this, this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight());
		}
		
		this.loadingScreen = new LoadingScreenRenderer(this);

		EagRuntime.getConfiguration().getHooks().callScreenChangedHook(
				currentScreen != null ? currentScreen.getClass().getName() : null, scaledResolution.getScaledWidth(),
				scaledResolution.getScaledHeight(), displayWidth, displayHeight, scaledResolution.scaleFactor);
	}

	private void clickMiddleMouseButton() {
		if(this.objectMouseOver != null) {
			int var1 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
			if(var1 == Block.grass.blockID) {
				var1 = Block.dirt.blockID;
			}

			if(var1 == Block.stairDouble.blockID) {
				var1 = Block.stairSingle.blockID;
			}

			if(var1 == Block.bedrock.blockID) {
				var1 = Block.stone.blockID;
			}

			this.thePlayer.inventory.setCurrentItem(var1, this.playerController instanceof PlayerControllerTest);
		}

	}

	public void runTick() {
		this.ingameGUI.func_555_a();
		this.entityRenderer.getMouseOver(1.0F);
		if(this.thePlayer != null) {
			this.thePlayer.func_6420_o();
		}

		if(!this.isWorldLoaded && this.theWorld != null) {
			this.playerController.updateController();
		}

		if(!this.isWorldLoaded) {
			this.renderEngine.updateTerrainTextures();
			GL11.glViewport(0, 0, displayWidth, displayHeight);
		}
		
		if(this.currentScreen == null && this.thePlayer != null) {
			if(this.thePlayer.health <= 0) {
				this.displayGuiScreen((GuiScreen)null);
			}
			if (this.currentScreen == null && this.dontPauseTimer <= 0) {
				if(!Mouse.isMouseGrabbed()) {
					this.func_6273_f();
					this.func_6252_g();
				}
			}
		}

		if(this.currentScreen != null) {
			this.dontPauseTimer = 10;
			this.field_6302_aa = this.ticksRan + 10000;
		} else {
			if (this.dontPauseTimer > 0) {
				--this.dontPauseTimer;
			}
		}

		if(this.currentScreen != null) {
			this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

		if(this.currentScreen == null || this.currentScreen.field_948_f) {
			label238:
			while(true) {
				while(true) {
					while(true) {
						long var1;
						do {
							if(!Mouse.next()) {
								if(this.field_6282_S > 0) {
									--this.field_6282_S;
								}

								while(true) {
									while(true) {
										do {
											if(!Keyboard.next()) {
												if(this.currentScreen == null) {
													if(Mouse.isButtonDown(0) && (float)(this.ticksRan - this.field_6302_aa) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
														this.clickMouse(0);
														this.field_6302_aa = this.ticksRan;
													}

													if(Mouse.isButtonDown(1) && (float)(this.ticksRan - this.field_6302_aa) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
														this.clickMouse(1);
														this.field_6302_aa = this.ticksRan;
													}
												}

												this.func_6254_a(0, this.currentScreen == null && Mouse.isButtonDown(0) && this.inGameHasFocus);
												break label238;
											}

											this.thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState());
										} while(!Keyboard.getEventKeyState());

										if(this.currentScreen != null) {
											this.currentScreen.handleKeyboardInput();
										} else {
											if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
												this.func_6252_g();
											}

											if(Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
												this.forceReload();
											}

											if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
												this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
											}
											
											if(Keyboard.getEventKey() == this.gameSettings.keyBindScreenshot.keyCode) {
												this.ingameGUI.addChatMessage("Saved Screenshot As: " + PlatformApplication.saveScreenshot());
											}

											if(Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
												this.displayGuiScreen(new GuiInventory(this.thePlayer));
											}

											if(Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
												this.thePlayer.func_20060_w();
											}

											if(this.isMultiplayerWorld() && Keyboard.getEventKey() == this.gameSettings.keyBindChat.keyCode) {
												this.displayGuiScreen(new GuiChat());
											}

											for(int var4 = 0; var4 < 9; ++var4) {
												if(Keyboard.getEventKey() == Keyboard.KEY_1 + var4) {
													this.thePlayer.inventory.currentItem = var4;
												}
											}
										}
									}
								}
							}

							var1 = EagRuntime.steadyTimeMillis() - this.systemTime;
						} while(var1 > 200L);

						int var3 = Mouse.getEventDWheel();
						if(var3 != 0) {
							this.thePlayer.inventory.changeCurrentItem(var3);
						}

						if(this.currentScreen == null) {
							if(!this.inGameHasFocus && Mouse.getEventButtonState()) {
								this.func_6259_e();
							} else {
								if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
									this.clickMouse(0);
									this.field_6302_aa = this.ticksRan;
								}

								if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
									this.clickMouse(1);
									this.field_6302_aa = this.ticksRan;
								}

								if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
									this.clickMiddleMouseButton();
								}
							}
						} else if(this.currentScreen != null) {
							this.currentScreen.handleMouseInput();
						}
					}
				}
			}
		}

		if(this.theWorld != null) {
			if(this.thePlayer != null) {
				++this.field_6300_ab;
				if(this.field_6300_ab == 30) {
					this.field_6300_ab = 0;
					this.theWorld.func_705_f(this.thePlayer);
				}
			}

			this.theWorld.difficultySetting = this.gameSettings.difficulty;
			if(this.theWorld.multiplayerWorld) {
				this.theWorld.difficultySetting = 3;
			}

			if(!this.isWorldLoaded) {
				this.entityRenderer.func_911_a();
			}

			if(!this.isWorldLoaded) {
				this.renderGlobal.func_945_d();
			}

			if(!this.isWorldLoaded) {
				this.theWorld.func_633_c();
			}

			if(!this.isWorldLoaded || this.isMultiplayerWorld()) {
				this.theWorld.tick();
			}

			if(!this.isWorldLoaded && this.theWorld != null) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			if(!this.isWorldLoaded) {
				this.effectRenderer.func_1193_a();
			}
		}

		this.systemTime = EagRuntime.steadyTimeMillis();
	}

	private void forceReload() {
		LOGGER.info("FORCING RELOAD!");
		this.sndManager = new SoundManager();
		this.sndManager.init(this.gameSettings);
	}

	public boolean isMultiplayerWorld() {
		return this.theWorld != null && this.theWorld.multiplayerWorld;
	}

	public void func_6247_b(String var1) {
		this.func_6261_a((World)null);
		System.gc();
		World var2 = new World(new VFile2(getMinecraftDir(), "saves"), var1);
		if(var2.field_1033_r) {
			this.func_6263_a(var2, "Generating level");
		} else {
			this.func_6263_a(var2, "Loading level");
		}

	}

	public void usePortal() {
		if(this.thePlayer.dimension == -1) {
			this.thePlayer.dimension = 0;
		} else {
			this.thePlayer.dimension = -1;
		}

		this.theWorld.setEntityDead(this.thePlayer);
		this.thePlayer.isDead = false;
		double var1 = this.thePlayer.posX;
		double var3 = this.thePlayer.posZ;
		double var5 = 8.0D;
		World var7;
		if(this.thePlayer.dimension == -1) {
			var1 /= var5;
			var3 /= var5;
			this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			var7 = new World(this.theWorld, new WorldProviderHell());
			this.changeWorld(var7, "Entering the Nether", this.thePlayer);
		} else {
			var1 *= var5;
			var3 *= var5;
			this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			var7 = new World(this.theWorld, new WorldProvider());
			this.changeWorld(var7, "Leaving the Nether", this.thePlayer);
		}

		this.thePlayer.worldObj = this.theWorld;
		this.thePlayer.setLocationAndAngles(var1, this.thePlayer.posY, var3, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
		this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
		(new Teleporter()).func_4107_a(this.theWorld, this.thePlayer);
	}

	public void func_6261_a(World var1) {
		this.func_6263_a(var1, "");
	}

	public void func_6263_a(World var1, String var2) {
		this.changeWorld(var1, var2, (EntityPlayer)null);
	}

	public void changeWorld(World var1, String var2, EntityPlayer var3) {
		if(!this.isShuttingDown) {
			this.loadingScreen.printText(var2);
			this.loadingScreen.displayLoadingString("");
		}
		this.sndManager.func_331_a((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		if(this.theWorld != null) {
			this.theWorld.func_651_a(this.loadingScreen);
		}

		this.theWorld = var1;
		LOGGER.info("Player is {}", this.thePlayer);
		if(var1 != null) {
			this.playerController.func_717_a(var1);
			if(!this.isMultiplayerWorld()) {
				if(var3 == null) {
					this.thePlayer = (EntityPlayerSP)null;
				}
			} else if(this.thePlayer != null) {
				this.thePlayer.preparePlayerToSpawn();
				if(var1 != null) {
					var1.entityJoinedWorld(this.thePlayer);
				}
			}

			if(!var1.multiplayerWorld) {
				this.func_6255_d(var2);
			}

			LOGGER.info("Player is now {}", this.thePlayer);
			if(this.thePlayer == null) {
				this.thePlayer = (EntityPlayerSP)this.playerController.func_4087_b(var1);
				this.thePlayer.preparePlayerToSpawn();
				this.playerController.flipPlayer(this.thePlayer);
			}

			this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
			if(this.renderGlobal != null) {
				this.renderGlobal.func_946_a(var1);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.func_1188_a(var1);
			}

			this.playerController.func_6473_b(this.thePlayer);
			if(var3 != null) {
				var1.func_6464_c();
			}

			var1.func_608_a(this.thePlayer);
			if(var1.field_1033_r) {
				var1.func_651_a(this.loadingScreen);
			}
		} else {
			this.thePlayer = null;
		}

		System.gc();
		this.systemTime = 0L;
	}

	private void func_6255_d(String var1) {
		this.loadingScreen.printText(var1);
		this.loadingScreen.displayLoadingString("Building terrain");
		short var2 = 128;
		int var3 = 0;
		int var4 = var2 * 2 / 16 + 1;
		var4 *= var4;

		for(int var5 = -var2; var5 <= var2; var5 += 16) {
			int var6 = this.theWorld.spawnX;
			int var7 = this.theWorld.spawnZ;
			if(this.thePlayer != null) {
				var6 = (int)this.thePlayer.posX;
				var7 = (int)this.thePlayer.posZ;
			}

			for(int var8 = -var2; var8 <= var2; var8 += 16) {
				this.loadingScreen.setLoadingProgress(var3++ * 100 / var4);
				this.theWorld.getBlockId(var6 + var5, 64, var7 + var8);

				while(this.theWorld.func_6465_g()) {
				}
			}
		}

		this.loadingScreen.displayLoadingString("Simulating world for a bit");
		this.theWorld.func_656_j();
	}

	public String func_6241_m() {
		return this.renderGlobal.func_953_b();
	}

	public String func_6262_n() {
		return this.renderGlobal.func_957_c();
	}

	public String func_6245_o() {
		return "P: " + this.effectRenderer.func_1190_b() + ". T: " + this.theWorld.func_687_d();
	}

	public void respawn() {
		if(!this.theWorld.worldProvider.func_6477_d()) {
			this.usePortal();
		}

		this.theWorld.func_4076_b();
		this.theWorld.func_9424_o();
		int var1 = 0;
		if(this.thePlayer != null) {
			var1 = this.thePlayer.field_620_ab;
			this.theWorld.setEntityDead(this.thePlayer);
		}

		this.thePlayer = (EntityPlayerSP)this.playerController.func_4087_b(this.theWorld);
		this.thePlayer.preparePlayerToSpawn();
		this.playerController.flipPlayer(this.thePlayer);
		this.theWorld.func_608_a(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.field_620_ab = var1;
		this.playerController.func_6473_b(this.thePlayer);
		this.func_6255_d("Respawning");
		if(this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen)null);
		}

	}

	public NetClientHandler func_20001_q() {
		return this.thePlayer instanceof EntityClientPlayerMP ? ((EntityClientPlayerMP)this.thePlayer).field_797_bg : null;
	}

	public static Minecraft getMinecraft() {
		return minecraft;
	}
	
	public static int getDebugFPS() {
		return debugFPS;
	}
}
