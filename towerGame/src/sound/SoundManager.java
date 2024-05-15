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

public abstract class SoundManager {
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
	public static void preloadSounds() {
		try {
			URI uri = SoundManager.class.getResource("/sound").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
				myPath = fileSystem.getPath("/sound");
			} else {
				myPath = Paths.get(uri);
			}
			Stream<Path> walk = Files.walk(myPath, 1);
			for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
				Path p = it.next();
				if(p.getFileName().toString().equals("sound"))
					continue;
				AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sound/"+p.getFileName().toString()));
				
			}
			walk.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

