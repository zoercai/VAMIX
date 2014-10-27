package generalOperations;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicSliderUI;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * 
 * Displays time and progress of the video, as well as control the video time.
 * This is the class responsible for the drawing of the timebar related
 * components as well as the logic and processes.
 * 
 * @author zoe
 *
 */
public class TimeBar extends JPanel {

	JFrame _frame;
	MediaPlayer _video;

	// TimeBar
	private JSlider timeBar = new JSlider(0, 0);

	private JPanel timeCountPanel = new JPanel();
	private JLabel timeCountLabel = new JLabel("Current time: ");
	private JLabel timeCount = new JLabel(" --:-- ");

	private JPanel timeTotalPanel = new JPanel();
	private JLabel timeTotalLabel = new JLabel("Total time: ");
	private JLabel timeTotal = new JLabel(" --:-- ");

	public TimeBar(JFrame frame, EmbeddedMediaPlayer video) {
		_frame = frame;
		_video = video;

		// Progress Bar Setup
		timeCount.setHorizontalAlignment(JTextField.CENTER);
		timeCountPanel.add(timeCountLabel);
		timeCountPanel.add(timeCount);
		this.add(timeCountPanel, BorderLayout.WEST);

		this.add(timeBar, BorderLayout.CENTER);
		timeBar.addMouseListener(new timeListener());

		timeTotalPanel.add(timeTotalLabel);
		timeTotalPanel.add(timeTotal);
		timeTotal.setHorizontalAlignment(JTextField.CENTER);
		this.add(timeTotalPanel, BorderLayout.EAST);
	}

	public String getTime() {
		return timeCount.getText();
	}

	public void setMaximum(int max) {
		timeBar.setMaximum(max);
		timeTotal.setText(String.format("%02d:%02d", (int) max / 60000,
				(int) max % 60000 / 1000));
	}

	public void setMediaPlayer(MediaPlayer video) {
		_video = video;
	}

	public void setValue(int time) {
		timeBar.setValue(time);
		if (!timeCount.getText().equals(
				String.format("%02d:%02d", time / 60000, time % 60000 / 1000))) {
			timeCount.setText(String.format("%02d:%02d", time / 60000,
					time % 60000 / 1000));
		}
	}

	public void stop() {
		timeBar.setValue(0);
		timeCount.setText("--:--");
	}

	private class timeListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// REFERENCE
			// http://www.java-forums.org/awt-swing/84832-move-jslider-per-click.html
			JSlider sourceSlider = (JSlider) e.getSource();
			BasicSliderUI ui = (BasicSliderUI) sourceSlider.getUI();
			int value = ui.valueForXPosition(e.getX());
			timeBar.setValue(value);
			_video.setTime(timeBar.getValue());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

}
