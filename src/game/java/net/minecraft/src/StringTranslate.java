package net.minecraft.src;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.HString;

public class StringTranslate {
	private static StringTranslate field_20165_a = new StringTranslate();
	private Properties field_20164_b = new Properties();
	private static Logger LOGGER = LogManager.getLogger();

	private StringTranslate() {
		try {
			this.field_20164_b.load(EagRuntime.getRequiredResourceStream("/lang/en_US.lang"));
		} catch (IOException var2) {
			LOGGER.error(var2);
		}

	}

	public static StringTranslate func_20162_a() {
		return field_20165_a;
	}

	public String func_20163_a(String var1) {
		return this.field_20164_b.getProperty(var1, var1);
	}

	public String func_20160_a(String var1, Object... var2) {
		String var3 = this.field_20164_b.getProperty(var1, var1);
		return HString.format(var3, var2);
	}

	public String func_20161_b(String var1) {
		return this.field_20164_b.getProperty(var1 + ".name", "");
	}
}
