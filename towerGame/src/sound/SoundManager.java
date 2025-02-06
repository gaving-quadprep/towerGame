package sound;

import java.util.ArrayList;
import java.util.List;

public abstract class SoundManager {
	private static int numberOfSoundsPlaying = 0;
	private static final int maxNumberOfSounds = 16;
	public static native void play(String fileName, int loopCount);	
	public static native void cleanUpSounds();
}

