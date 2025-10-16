package net.minecraft.src;

import net.lax1dude.eaglercraft.EagRuntime;

public class GuiOptions extends GuiScreen {
	private GuiScreen parentScreen;
	protected String screenTitle = "Options";
	private GameSettings options;

	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private long mouseStillTime = 0L;

	public GuiOptions(GuiScreen var1, GameSettings var2) {
		this.parentScreen = var1;
		this.options = var2;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.func_20162_a();
		this.screenTitle = var1.func_20163_a("options.title");
		EnumOptions[] var2 = EnumOptions.values();
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			EnumOptions var5 = var2[var4];
			int var6 = var5.func_20135_c();
			if (!var5.func_20136_a()) {
				this.controlList.add(new GuiSmallButton(var5.func_20135_c(), this.width / 2 - 155 + var6 % 2 * 160,
						this.height / 6 + 24 * (var6 >> 1), var5, this.options.getKeyBinding(var5)));
			} else {
				this.controlList.add(new GuiSlider(var5.func_20135_c(), this.width / 2 - 155 + var6 % 2 * 160,
						this.height / 6 + 24 * (var6 >> 1), var5, this.options.getKeyBinding(var5),
						this.options.func_20104_a(var5)));
			}
		}

		this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12,
				var1.func_20163_a("options.controls")));
		this.controlList
				.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.func_20163_a("gui.done")));
	}

	protected void actionPerformed(GuiButton var1) {
		if (var1.enabled) {
			if (var1.id < 100 && var1 instanceof GuiSmallButton) {
				this.options.setOptionValue(((GuiSmallButton) var1).func_20078_a(), 1);
				var1.displayString = this.options.getKeyBinding(EnumOptions.func_20137_a(var1.id));
			}

			if (var1.id == 100) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiControls(this, this.options));
			}

			if (var1.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
		if (Math.abs(var1 - this.lastMouseX) <= 5 && Math.abs(var2 - this.lastMouseY) <= 5) {
			short activateDelay = 700;
			if (EagRuntime.steadyTimeMillis() >= this.mouseStillTime + (long) activateDelay) {
				int x1 = this.width / 2 - 150;
				int y1 = this.height / 6 - 5;
				if (var2 <= y1 + 98) {
					y1 += 105;
				}

				int x2 = x1 + 150 + 150;
				int y2 = y1 + 84 + 10;
				GuiButton btn = this.getSelectedButton(var1, var2);
				if (btn != null) {
					String s = this.getButtonName(btn.displayString);
					String[] lines = this.getTooltipLines(s);
					if (lines == null) {
						return;
					}

					this.drawGradientRect(x1, y1, x2, y2, -536870912, -536870912);

					for (int i = 0; i < lines.length; ++i) {
						String line = lines[i];
						this.fontRenderer.drawStringWithShadow(line, x1 + 5, y1 + 5 + i * 11, 14540253);
					}
				}

			}
		} else {
			this.lastMouseX = var1;
			this.lastMouseY = var2;
			this.mouseStillTime = EagRuntime.steadyTimeMillis();
		}
	}

	private String[] getTooltipLines(String btnName) {
		return btnName
				.equals("Graphics")
						? new String[] { "Visual quality", "  Fast  - lower quality, faster",
								"  Fancy - higher quality, slower", "Changes the appearance of clouds, leaves, water,",
								"shadows and grass sides." }
						: (btnName.equals("Render Distance")
								? new String[] { "Visible distance", "  Far - 256m (slower)", "  Normal - 128m",
										"  Short - 64m (faster)", "  Tiny - 32m (fastest)" }
								: (btnName.equals("Limit Framerate")
										? new String[] { "Framerate Limit", "  OFF - no limit",
												"  ON - limit to monitor framerate (60, 30, 20)",
												"Turn this ON if you're experiening input lag.", }
										: (btnName.equals("3D Anaglyph")
												? new String[] { "3D mode used with red-cyan 3D glasses." }
												: (btnName.equals("View Bobbing")
														? new String[] { "More realistic movement." }
														: null))));
	}

	private String getButtonName(String displayString) {
		int pos = displayString.indexOf(58);
		return pos < 0 ? displayString : displayString.substring(0, pos);
	}

	private GuiButton getSelectedButton(int i, int j) {
		for (int k = 0; k < this.controlList.size(); ++k) {
			GuiButton btn = (GuiButton) this.controlList.get(k);
			boolean flag = i >= btn.xPosition && j >= btn.yPosition && i < btn.xPosition + btn.width
					&& j < btn.yPosition + btn.height;
			if (flag) {
				return btn;
			}
		}

		return null;
	}
}
