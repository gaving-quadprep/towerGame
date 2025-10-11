package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public abstract class SoundManager {
	private static int numberOfSoundsPlaying = 0;
	private static Object soundNumberLock = new Object();
	private static final int maxNumberOfSounds = 16;
	
	public static synchronized void play(final String fileName, final int loopCount) {
		Thread t = new Thread() {
			{
				setDaemon(true);
			}
			@Override public void run() {
				AudioInputStream ais;
				boolean canPlay;
				synchronized(soundNumberLock) {
					canPlay = numberOfSoundsPlaying < maxNumberOfSounds;
				}
				
				if(canPlay) {
					synchronized(soundNumberLock) {
						numberOfSoundsPlaying++;
					}
					try {
						Clip clip;
						
						ais = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sounds/"+fileName));
						
						DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
						
						clip = (Clip)AudioSystem.getLine(info);
						clip.open(ais);
						
						if(loopCount != 0)
							clip.loop(loopCount);
						
						clip.start();
						
						while(!clip.isActive());
						while(clip.isActive())
							Thread.sleep(10);
							
						synchronized(soundNumberLock) {
							numberOfSoundsPlaying--;
						}
						
						clip.close();
					} catch (Exception e) {
						synchronized(soundNumberLock) {
							numberOfSoundsPlaying--;
						}
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		};
		
		t.start();
	}
	
	public static void cleanUpSounds() {
		synchronized(soundNumberLock) {
			numberOfSoundsPlaying = 0;
		}
		
	}
}

