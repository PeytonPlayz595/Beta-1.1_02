/*
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.lax1dude.eaglercraft.profile;

import java.io.IOException;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.EaglerOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;

public class EaglerProfile {

	private static String username;
	
	public static String getName() {
		return username;
	}

	public static void setName(String str) {
		username = str;
		Minecraft mc = Minecraft.getMinecraft();
		if(mc != null && mc.session != null) {
			mc.session.playerName = str;
		}
	}

	public static void read() {
		read(EagRuntime.getStorage("p"));
	}

	public static void read(byte[] profileStorage) {
		if (profileStorage == null) {
			return;
		}

		NBTTagCompound profile;
		try {
			profile = CompressedStreamTools.readCompressed(new EaglerInputStream(profileStorage));
		}catch(IOException ex) {
			return;
		}

		if (profile == null || profile.hasNoTags()) {
			return;
		}

		String loadUsername = profile.getString("username").trim();

		if(!loadUsername.isEmpty()) {
			username = loadUsername.replaceAll("[^A-Za-z0-9]", "_");
		}
	}

	public static byte[] write() {
		NBTTagCompound profile = new NBTTagCompound();
		profile.setString("username", username);
		EaglerOutputStream bao = new EaglerOutputStream();
		try {
			CompressedStreamTools.writeGzippedCompoundToOutputStream(profile, bao);
		} catch (IOException e) {
			return null;
		}
		return bao.toByteArray();
	}

	public static void save() {
		byte[] b = write();
		if(b != null) {
			EagRuntime.setStorage("p", b);
		}
	}

	static {
		read();
	}

	public static boolean isDefaultUsername(String str) {
		return str.toLowerCase().matches("^(yeeish|yee|yeer|yeeler|eagler|eagl|darver|darvler|vool|vigg|deev|yigg|yeeg){2}\\d{2,4}$");
	}

}