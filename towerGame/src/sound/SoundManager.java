package sound;

import java.util.ArrayList;
import java.util.List;

public abstract class SoundManager {
	private static int numberOfSoundsPlaying = 0;
	private static Object soundNumberLock = new Object();
	private static final int maxNumberOfSounds = 16;
	
	public static synchronized void play(String fileName, int loopCount) {
		Thread t = new Thread() {
			{
				setDaemon(true);
			}
			@Override public void run() {
				boolean canPlay;
				synchronized(soundNumberLock) {
					canPlay = numberOfSoundsPlaying < maxNumberOfSounds;
					System.out.println(numberOfSoundsPlaying);
				}
				
				if(canPlay) {
					synchronized(soundNumberLock) {
						numberOfSoundsPlaying++;
					}
					try {
						int i = loopCount;
						do {
							_play(fileName);
							i--;
						} while (i > 0);
						synchronized(soundNumberLock) {
							numberOfSoundsPlaying--;
						}
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
	
	private static native void _play(String fileName);	
	public static native void cleanUpSounds();
}

