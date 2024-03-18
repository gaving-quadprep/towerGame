package map.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
	private static Clip clip;
	
	public static void setFile(String fileName) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sound/"+fileName));
			clip = AudioSystem.getClip();
			clip.open(ais);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void play() {
		clip.start();
	}
	public static void loop(int count) {
		clip.loop(count);
	}
	public static void stop() {
		clip.stop();
	}
}
