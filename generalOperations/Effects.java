package generalOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * 
 * Allows user to adjust brightness, contrast, hue, saturation, and gamma while
 * the video is playing. This class is responsible for the drawing of the
 * components as well as the adjustment background processes.
 * 
 * @author zoe
 *
 */
public class Effects extends JPanel {
	private MediaPlayer _mediaPlayer;

	private JCheckBox adjustment = new JCheckBox("Adjustments");
	private JLabel brightness = new JLabel("Brightness");
	private JSlider brightnessSlider = new JSlider(0, 2000);
	private JLabel contrast = new JLabel("Contrast");
	private JSlider contrastSlider = new JSlider(0, 2000);
	private JLabel hue = new JLabel("Hue");
	private JSlider hueSlider = new JSlider(0, 360);
	private JLabel saturation = new JLabel("Saturation");
	private JSlider saturationSlider = new JSlider(0, 3000);
	private JLabel gamma = new JLabel("Gamma");
	private JSlider gammaSlider = new JSlider(0, 10000);

	public Effects() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		brightnessSlider.setEnabled(false);
		contrastSlider.setEnabled(false);
		hueSlider.setEnabled(false);
		saturationSlider.setEnabled(false);
		gammaSlider.setEnabled(false);

		this.add(adjustment);
		this.add(Box.createVerticalStrut(10));
		this.add(brightness);
		this.add(brightnessSlider);
		this.add(contrast);
		this.add(contrastSlider);
		this.add(hue);
		this.add(hueSlider);
		this.add(saturation);
		this.add(saturationSlider);
		this.add(gamma);
		this.add(gammaSlider);

		adjustment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (adjustment.isSelected()) {
					_mediaPlayer.setAdjustVideo(true);
					brightnessSlider.setEnabled(true);
					contrastSlider.setEnabled(true);
					hueSlider.setEnabled(true);
					saturationSlider.setEnabled(true);
					gammaSlider.setEnabled(true);
				} else {
					_mediaPlayer.setAdjustVideo(false);
					brightnessSlider.setEnabled(false);
					contrastSlider.setEnabled(false);
					hueSlider.setEnabled(false);
					saturationSlider.setEnabled(false);
					gammaSlider.setEnabled(false);
				}
			}

		});

		brightnessSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_mediaPlayer.setBrightness(brightnessSlider.getValue() / 1000.0f);
			}
		});

		contrastSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_mediaPlayer.setContrast(contrastSlider.getValue() / 1000.0f);
			}
		});

		hueSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_mediaPlayer.setHue(hueSlider.getValue());
			}
		});

		saturationSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_mediaPlayer.setSaturation(saturationSlider.getValue() / 1000.0f);
			}
		});

		gammaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_mediaPlayer.setGamma(gammaSlider.getValue() / 1000.0f);
			}
		});
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		_mediaPlayer = mediaPlayer;
		adjustment.setSelected(false);
	}
}
