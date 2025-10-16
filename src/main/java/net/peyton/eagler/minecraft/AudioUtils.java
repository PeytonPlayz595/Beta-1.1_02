package net.peyton.eagler.minecraft;

import java.util.List;

import com.carrotsearch.hppc.ObjectArrayList;

import net.lax1dude.eaglercraft.Random;

public class AudioUtils {
	
	private static Random rand = new Random();
	
	private static List<String> music = null;
	private static List<String> sounds = null;
	
	private static ObjectArrayList<String> temp = new ObjectArrayList<>();
	
	private static int lastIdx = -1;
	
	public static String getSound(String name) {
		if (sounds == null) {
			sounds = ResourceLoader.getSounds();
		}
		
		int size;
		if (sounds == null || (size = sounds.size()) == 0) {
			return null;
		}
		
		String ext = ".ogg";
		String path = name.replace(".", "/") + ext;
		temp.clear();
		
		for (int i = 0, j = size; i < j; ++i) {
			String file = sounds.get(i);
			String file2 = file.replaceAll("[0-9]", "");
			if (file2.contains(path)) {
				temp.add(file);
			}
		}
		
		size = temp.size();
		
		if (size == 0) {
			return null;
		}
		
		int idx = rand.nextInt(size);
		return temp.get(idx);
	}
	
	public static String getMusic() {
		if (music == null) {
			music = ResourceLoader.getMusic();
		}
		
		int size;
		if (music == null || (size = music.size()) == 0) {
			return null;
		}
		
		int idx = rand.nextInt(size);
		while (idx == lastIdx) {
			idx = rand.nextInt(size);
		}
		
		return music.get(idx);
	}
}
