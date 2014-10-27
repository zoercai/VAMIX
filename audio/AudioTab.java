package audio;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * 
 * AudioTab is the JPanel that contains all audio functions.
 * @author zoe
 *
 */
public class AudioTab extends JPanel{
	
	public AudioTab(){
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createHorizontalStrut(10));
		
		//super.paint(null);
		Extract extract = new Extract();
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder extractTitle = BorderFactory.createTitledBorder(blackline, "Extract");
		extractTitle.setTitleJustification(TitledBorder.CENTER);
		extract.setBorder(extractTitle);
		add(extract);
		
		this.add(Box.createHorizontalStrut(10));
		Overlay overlay = new Overlay();
		TitledBorder stripTitle = BorderFactory.createTitledBorder(blackline, "Overlay");
		stripTitle.setTitleJustification(TitledBorder.CENTER);
		overlay.setBorder(stripTitle);
		add(overlay);
		
		this.add(Box.createHorizontalStrut(10));
		Replace replace = new Replace();
		TitledBorder replaceTitle = BorderFactory.createTitledBorder(blackline, "Replace");
		replaceTitle.setTitleJustification(TitledBorder.CENTER);
		replace.setBorder(replaceTitle);
		add(replace);

	}
}
