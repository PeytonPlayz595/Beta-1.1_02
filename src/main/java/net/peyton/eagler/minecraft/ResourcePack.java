package net.peyton.eagler.minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;
import net.minecraft.src.IProgressUpdate;

public class ResourcePack {
	
	private VFile2 rp;
	private VFile2 files;
	private String name;
	
	private ResourcePack(String rpName) {
		this.rp = new VFile2(mc.mcDataDir, "texturePacks", rpName);
		this.files = new VFile2(rp, "files.txt");
		this.name = rpName;
	}
	
	public String getName() {
		return this.name;
	}
	
	public InputStream getResourceAsStream(String fileName) {
		byte[] data = new VFile2(rp, fileName).getAllBytes();
		if(data != null) {
			return new EaglerInputStream(data);
		} else {
			return EagRuntime.getRequiredResourceStream(fileName);
		}
	}
	
	public void deletePack() {
		String s;
		JSONObject obj = new JSONObject((s = this.files.getAllChars()) != null ? s : ""); //sanity check lol
		JSONArray arr = obj.getJSONArray("files", null);
		if(arr != null) {
			for(int i = 0, j = arr.length(); i < j; ++i) {
				new VFile2(rp, arr.getString(i)).delete();
			}
		}
	}
	
	private static Minecraft mc = Minecraft.getMinecraft();
	private static Logger logger = LogManager.getLogger("ResourcePack");
	
	private static boolean uploaded = false;
	
	public static boolean uploadResourcePack(String name, byte[] data, IProgressUpdate progress) throws IOException {
		List<ResourcePack> existingPacks = getExistingResourcePacks();
		
		vigg: for(;;) {
			for(int i = 0, l = existingPacks.size(); i < l; ++i) {
				ResourcePack rp = existingPacks.get(i);
				if(rp.name.equalsIgnoreCase(name)) {
					name = name + "-";
					continue vigg;
				}
			}
			break;
		}
		
		VFile2 texturePackDir = new VFile2(mc.mcDataDir, "texturePacks");
		VFile2 saveDir = new VFile2(texturePackDir, name);
		
		String status = null;
		List<String> lst = new ArrayList<>();
		try {
			status = "counting files";
			progress.displayLoadingString(status);
			progress.setLoadingProgress(25);
			ZipEntry entry;
			try(ZipInputStream zis = new ZipInputStream(new EaglerInputStream(data))) {
				while((entry = zis.getNextEntry()) != null) {
					lst.add(entry.getName());
				}
			}
			
			int totalSize = 0;
			int totalFiles = 0;
			int lastProg = 0;
			status = "extracting files";
			progress.displayLoadingString(status);
			progress.setLoadingProgress(50);
			try(ZipInputStream zis = new ZipInputStream(new EaglerInputStream(data))) {
				int size;
				String fileName;
				while((entry = zis.getNextEntry()) != null) {
					fileName = entry.getName();
					size = (int)entry.getSize();
					byte[] buffer;
				
					if(size >= 0) {
						buffer = new byte[size];
						int i = 0, j;
						while(i < buffer.length && (j = zis.read(buffer, i, buffer.length - i)) != -1) {
							i += j;
						}
					} else {
						buffer = EaglerInputStream.inputStreamToBytesNoClose(zis);
					}
					new VFile2(saveDir, fileName).setAllBytes(buffer);
					totalSize += buffer.length;
					++totalFiles;
					if(totalSize - lastProg > 25000) {
						lastProg = totalSize;
						logger.info("Extracted {} files, {} bytes from ZIP file...", totalFiles, totalSize);
					}
				}
			}
		} catch(IOException e) {
			logger.error("Encountered an error extracting zip file, deleting extracted files...");
			String fn;
			for(int i = 0, l = lst.size(); i < l; ++i) {
				fn = lst.get(i);
				(new VFile2(saveDir, fn)).delete();
			}
			throw new IOException("Exception while " + status, e);
		}
		
		status = "updating manifest";
		progress.displayLoadingString(status);
		progress.setLoadingProgress(75);
		
		JSONArray arr = new JSONArray();
		String fn;
		for(int i = 0, l = lst.size(); i < l; ++i) {
			fn = lst.get(i);
			arr.put(fn);
		}
		JSONObject obj = new JSONObject();
		obj.put("files", arr);
		new VFile2(saveDir, "files.txt").setAllChars(obj.toString());
		
		VFile2 packsFile = new VFile2(texturePackDir, "packs.txt");
		if(packsFile.exists()) {
			obj = new JSONObject(packsFile.getAllChars());
			arr = obj.getJSONArray("packs");
			arr.put(name);
		} else {
			obj = new JSONObject();
			arr = new JSONArray();
			arr.put(name);
			obj.put("packs", arr);
		}
		packsFile.setAllChars(obj.toString());
		
		progress.setLoadingProgress(100);
		
		uploaded = true;
		return true;
	}
	
	private static List<ResourcePack> packCache = null;
	public static List<ResourcePack> getExistingResourcePacks() {
		boolean changed = uploaded;
		uploaded = false;
		
		VFile2 texturePackDir = new VFile2(mc.mcDataDir, "texturePacks");
		if(packCache == null || changed) {
			VFile2 existingPacks = new VFile2(texturePackDir, "packs.txt");
			if(existingPacks.exists()) {
				JSONObject obj = new JSONObject(existingPacks.getAllChars());
				JSONArray arr = obj.getJSONArray("packs");
				List<ResourcePack> packs = new ArrayList<ResourcePack>();
				for(int i = 0, j = arr.length(); i < j; ++i) {
					String name = arr.getString(i);
					if(name != null) {
						packs.add(new ResourcePack(name));
					}
				}
				return (packCache = packs);
			}
		}
		return packCache != null ? packCache : Collections.emptyList();
	}

}