package map.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
	private static Clip clip;
	private static Thread t;
	
	public static synchronized void setFile(String fileName) {
			t = new Thread() {
				@Override public void run() {
					AudioInputStream ais;
					try {
						ais = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sound/"+fileName));
						clip = AudioSystem.getClip();
						clip.open(ais);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			t.start();
	}
	public static void play() {
		if(t.isAlive()) {
			new Thread () {
				@Override public void run() {
					while(t.isAlive());
					clip.start();
				}
			}.start();
		}else {
			clip.start();
		}
	}
	public static void loop(int count) {
		clip.loop(count);
	}
	public static void stop() {
		clip.stop();
	}
}
