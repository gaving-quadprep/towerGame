package map.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
	private static Clip clip;
	private static Thread t;
	public static synchronized void play(String fileName) {
		Thread t2 = new Thread() {
			{
				setDaemon(true);
			}
			@Override public void run() {
				AudioInputStream ais;
				try {
					ais = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sound/"+fileName));
					clip = AudioSystem.getClip();
					clip.open(ais);
					clip.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		if(t != null && t.isAlive()) {
			new Thread() {
				{
					setDaemon(true);
				}
				@Override public void run() {
					try {
						t.join();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					t = t2;
					t.start();
				}
			}.start();
		}else {
			t = t2;
			t.start();
		}
	}
	public static void loop(int count) {
		clip.loop(count);
	}
	public static void stop() {
		clip.stop();
	}
}
