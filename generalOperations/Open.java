package generalOperations;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class Open {
	
	public Open(EmbeddedMediaPlayer video){
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		String videoLocation = fc.getSelectedFile().getAbsolutePath();
		video.playMedia(videoLocation);
	}
	
	
	
}
