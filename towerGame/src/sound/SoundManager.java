package sound;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public abstract class SoundManager {
	private static Clip clip;
	private static Thread t;
	private static Object clipLock = new Object();
	private static int numberOfSoundsPlaying = 0;
	private static final int maxNumberOfSounds = 16;
	public static synchronized void play(String fileName) {
		Thread t2 = new Thread() {
			{
				setDaemon(true);
			}
			@Override public void run() {
				AudioInputStream ais;
				numberOfSoundsPlaying++;
				System.out.println(numberOfSoundsPlaying);
				if(numberOfSoundsPlaying < maxNumberOfSounds) {
					try {
						synchronized(clipLock) {
							ais = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sounds/"+fileName));
							//System.out.println("got ais");
							DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
							//System.out.println("got dataline");
							clip = (Clip)AudioSystem.getLine(info);
							//System.out.println("got clip");
							clip.open(ais);
							//System.out.println("opened clip");

						}
						clip.start();
						//System.out.println("started clip");
						//clip.close();
						numberOfSoundsPlaying--;
					} catch (Exception e) {
						numberOfSoundsPlaying--;
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					numberOfSoundsPlaying--;
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
		synchronized(clipLock) {
			clip.loop(count);
		}
	}
	public static void stop() {
		synchronized(clipLock) {
			clip.stop();
			clip.close();
		}
	}
	public static void preloadSounds() {
		try {
			URI uri = SoundManager.class.getResource("/sounds").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
				myPath = fileSystem.getPath("/sounds");
			} else {
				myPath = Paths.get(uri);
			}
			Stream<Path> walk = Files.walk(myPath, 1);
			for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
				Path p = it.next();
				if(p.getFileName().toString().equals("sounds"))
					continue;
				AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sounds/"+p.getFileName().toString()));
				
			}
			walk.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

