package net.peyton.eagler.minecraft;

import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;

public class ResourceLoader {
	
	private static final ObjectArrayList<String> sounds = new ObjectArrayList<>();
	private static final ObjectArrayList<String> music = new ObjectArrayList<>();
	
	private static final ObjectArrayList<Resource> resources = new ObjectArrayList<>();
	
	public static List<String> getSounds() {
		return sounds;
	}
	
	public static List<String> getMusic() {
		return music;
	}
	
	public static void onResourceLoad(String fileName) {
		onResourceLoad(fileName, null, null);
	}

	public static void onResourceLoad(String fileName, byte[] data, Map<String, byte[]> loadedFiles) {
		String filePath = fileName.contains("resources/") ? fileName.replace("resources/", "") : fileName;
		
		Resource resource;
		for (int i = 0, j = resources.size(); i < j; ++i) {
			resource = resources.get(i);
			
			if (fileName.contains(resource.dir) && fileName.endsWith(resource.ext)) {
				i = j;
				
				System.out.println("Resource loaded: " + fileName);
				if (resource.hasList) {
					resource.getList().add(filePath);
				}
			}
		}
		
		if (EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP && loadedFiles != null) {
			loadedFiles.put(fileName, data);
		}
	}
	
	static {
		resources.add(new Resource("newsound/", "ogg", sounds));
		resources.add(new Resource("newmusic/", "ogg", music));
	}
}
