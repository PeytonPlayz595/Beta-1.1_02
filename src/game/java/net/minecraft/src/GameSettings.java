package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GameSettings {
	private static final String[] field_20105_z = new String[]{"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
	private static final String[] field_20106_A = new String[]{"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};
	public float musicVolume = 1.0F;
	public float soundVolume = 1.0F;
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse = false;
	public int renderDistance = 0;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;
	public boolean limitFramerate = false;
	public boolean fancyGraphics = true;
	public String skin = "Default";
	public KeyBinding ofKeyBindZoom = new KeyBinding("Zoom", Keyboard.KEY_C);
	public KeyBinding keyBindScreenshot = new KeyBinding("Screenshot", Keyboard.KEY_F2);
	public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
	public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
	public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
	public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
	public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
	public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 23);
	public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
	public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
	public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.ofKeyBindZoom, this.keyBindScreenshot};
	protected Minecraft mc;
	private VFile2 optionsFile;
	public int difficulty = 2;
	public boolean thirdPersonView = false;
	public String lastServer = "";
	
	private Logger LOGGER = LogManager.getLogger();

	public GameSettings(Minecraft var1, VFile2 var2) {
		this.mc = var1;
		this.optionsFile = new VFile2(var2, "options.txt");
		this.loadOptions();
	}

	public GameSettings() {
	}

	public String func_20102_a(int var1) {
		StringTranslate var2 = StringTranslate.func_20162_a();
		return var2.func_20163_a(this.keyBindings[var1].keyDescription);
	}

	public String getOptionDisplayString(int var1) {
		return Keyboard.getKeyName(this.keyBindings[var1].keyCode);
	}

	public void setKeyBinding(int var1, int var2) {
		this.keyBindings[var1].keyCode = var2;
		this.saveOptions();
	}

	public void setOptionFloatValue(EnumOptions var1, float var2) {
		if(var1 == EnumOptions.MUSIC) {
			this.musicVolume = var2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(var1 == EnumOptions.SOUND) {
			this.soundVolume = var2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(var1 == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = var2;
		}

	}

	public void setOptionValue(EnumOptions var1, int var2) {
		if(var1 == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if(var1 == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + var2 & 3;
		}

		if(var1 == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(var1 == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if(var1 == EnumOptions.LIMIT_FRAMERATE) {
			this.limitFramerate = !this.limitFramerate;
		}

		if(var1 == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + var2 & 3;
		}

		if(var1 == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		this.saveOptions();
	}

	public float func_20104_a(EnumOptions var1) {
		return var1 == EnumOptions.MUSIC ? this.musicVolume : (var1 == EnumOptions.SOUND ? this.soundVolume : (var1 == EnumOptions.SENSITIVITY ? this.mouseSensitivity : 0.0F));
	}

	public boolean func_20103_b(EnumOptions var1) {
		switch(EnumOptionsMappingHelper.field_20155_a[var1.ordinal()]) {
		case 1:
			return this.invertMouse;
		case 2:
			return this.viewBobbing;
		case 3:
			return this.anaglyph;
		case 4:
			return this.limitFramerate;
		default:
			return false;
		}
	}

	public String getKeyBinding(EnumOptions var1) {
		StringTranslate var2 = StringTranslate.func_20162_a();
		String var3 = var2.func_20163_a(var1.func_20138_d()) + ": ";
		if(var1.func_20136_a()) {
			float var5 = this.func_20104_a(var1);
			return var1 == EnumOptions.SENSITIVITY ? (var5 == 0.0F ? var3 + var2.func_20163_a("options.sensitivity.min") : (var5 == 1.0F ? var3 + var2.func_20163_a("options.sensitivity.max") : var3 + (int)(var5 * 200.0F) + "%")) : (var5 == 0.0F ? var3 + var2.func_20163_a("options.off") : var3 + (int)(var5 * 100.0F) + "%");
		} else if(var1.func_20140_b()) {
			boolean var4 = this.func_20103_b(var1);
			return var4 ? var3 + var2.func_20163_a("options.on") : var3 + var2.func_20163_a("options.off");
		} else {
			return var1 == EnumOptions.RENDER_DISTANCE ? var3 + var2.func_20163_a(field_20105_z[this.renderDistance]) : (var1 == EnumOptions.DIFFICULTY ? var3 + var2.func_20163_a(field_20106_A[this.difficulty]) : (var1 == EnumOptions.GRAPHICS ? (this.fancyGraphics ? var3 + var2.func_20163_a("options.graphics.fancy") : var3 + var2.func_20163_a("options.graphics.fast")) : var3));
		}
	}

	public void loadOptions() {
		try {
			if(!this.optionsFile.exists()) {
				return;
			}

			BufferedReader var1 = new BufferedReader(new InputStreamReader(this.optionsFile.getInputStream()));
			String var2 = "";

			int line = 0;
			while(true) {
				var2 = var1.readLine();
				line++;
				if(var2 == null) {
					var1.close();
					break;
				}

				String[] var3 = var2.split(":");
				if(var3.length >= 2) {
					if(var3[0].equals("music")) {
						this.musicVolume = this.parseFloat(var3[1]);
					}

					if(var3[0].equals("sound")) {
						this.soundVolume = this.parseFloat(var3[1]);
					}

					if(var3[0].equals("mouseSensitivity")) {
						this.mouseSensitivity = this.parseFloat(var3[1]);
					}

					if(var3[0].equals("invertYMouse")) {
						this.invertMouse = var3[1].equals("true");
					}

					if(var3[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(var3[1]);
					}

					if(var3[0].equals("bobView")) {
						this.viewBobbing = var3[1].equals("true");
					}

					if(var3[0].equals("anaglyph3d")) {
						this.anaglyph = var3[1].equals("true");
					}

					if(var3[0].equals("limitFramerate")) {
						this.limitFramerate = var3[1].equals("true");
					}

					if(var3[0].equals("difficulty")) {
						this.difficulty = Integer.parseInt(var3[1]);
					}

					if(var3[0].equals("fancyGraphics")) {
						this.fancyGraphics = var3[1].equals("true");
					}

					if(var3[0].equals("skin")) {
						this.skin = var3[1];
					}

					if(var3[0].equals("lastServer")) {
						this.lastServer = var3[1];
					}

					for(int var4 = 0; var4 < this.keyBindings.length; ++var4) {
						if(var3[0].equals("key_" + this.keyBindings[var4].keyDescription)) {
							this.keyBindings[var4].keyCode = Integer.parseInt(var3[1]);
						}
					}
				} else {
					if(var3.length >= 1) {
						LOGGER.warn("Skipping bad option {} on line #{}", var3[0], line);
					} else {
						LOGGER.warn("Failed to parse '{}' one line #{}", var2, line);
					}
				}
			}
		} catch (Exception var5) {
			LOGGER.error("Failed to load options");
			LOGGER.error(var5);
		}

	}

	private float parseFloat(String var1) {
		return var1.equals("true") ? 1.0F : (var1.equals("false") ? 0.0F : Float.parseFloat(var1));
	}

	public void saveOptions() {
		try {
			PrintWriter var1 = new PrintWriter(new OutputStreamWriter(this.optionsFile.getOutputStream()));
			var1.println("music:" + this.musicVolume);
			var1.println("sound:" + this.soundVolume);
			var1.println("invertYMouse:" + this.invertMouse);
			var1.println("mouseSensitivity:" + this.mouseSensitivity);
			var1.println("viewDistance:" + this.renderDistance);
			var1.println("bobView:" + this.viewBobbing);
			var1.println("anaglyph3d:" + this.anaglyph);
			var1.println("limitFramerate:" + this.limitFramerate);
			var1.println("difficulty:" + this.difficulty);
			var1.println("fancyGraphics:" + this.fancyGraphics);
			var1.println("skin:" + this.skin);
			var1.println("lastServer:" + this.lastServer);

			for(int var2 = 0; var2 < this.keyBindings.length; ++var2) {
				var1.println("key_" + this.keyBindings[var2].keyDescription + ":" + this.keyBindings[var2].keyCode);
			}

			var1.close();
		} catch (Exception var3) {
			LOGGER.error("Failed to save options");
			LOGGER.error(var3);
		}

	}
	
	public int shouldRenderClouds() {
		return this.renderDistance <= 1 ? (this.fancyGraphics ? 2 : 1) : 0;
	}
}
