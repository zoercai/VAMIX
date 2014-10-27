package titlecredit;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class AddTitle extends JPanel {
	private JPanel inputPanel = new JPanel();
	private JLabel inputFileLabel = new JLabel("Source file: ");
	private JTextField sourceField = new JTextField();
	private JButton sourceBrowse = new JButton("Browse");

	private JPanel outputPanel = new JPanel();
	private JLabel outputFileLabel = new JLabel("Output location: ");
	private JTextField outputField = new JTextField();
	private JButton outputBrowse = new JButton("Browse");
	private JPanel browsePanel = new JPanel(new FlowLayout());

	private JLabel title = new JLabel("Enter your text ");
	private JTextField text = new JTextField();
	private JPanel textPanel = new JPanel(new FlowLayout());

	private JLabel size = new JLabel("Size ");
	private String[] sizeStrings = { "10", "12", "14", "16", "18", "20" };
	private JComboBox<String> sizeChoice = new JComboBox<String>(sizeStrings);

	private JLabel font = new JLabel("Font ");
	private String[] fontStrings = { "Ubuntu-C", "Ubuntu-M", "Ubuntu-R",
			"UbuntuMono-B", "Ubuntu-B" };
	private JComboBox<String> fontChoice = new JComboBox<String>(fontStrings);

	private JLabel colour = new JLabel("Colour ");
	private String[] colourStrings = { "White", "Blue", "Black", "Purple",
			"Red" };
	private JComboBox<String> colourChoice = new JComboBox<String>(
			colourStrings);

	private JLabel position = new JLabel("Position ");
	private String[] vertical = { "Top", "Centre", "Bottom" };
	private JComboBox<String> positionChoiceVertical = new JComboBox<String>(
			vertical);

	private JLabel duration = new JLabel("Duration (seconds) "); // Limit of 10
	private Integer[] durationStrings = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private JComboBox<Integer> durationChoice = new JComboBox<Integer>(
			durationStrings);

	private JPanel textSettingPanel = new JPanel();
	private JPanel sizePanel = new JPanel();
	private JPanel fontPanel = new JPanel();
	private JPanel colourPanel = new JPanel();

	private JPanel videoSettingPanel = new JPanel();
	private JPanel positionPanel = new JPanel();
	private JPanel durationPanel = new JPanel();

	private JButton enter = new JButton("Save");
	private JButton preview = new JButton("Preview");
	private JProgressBar progress = new JProgressBar();
	private JPanel buttons = new JPanel(new FlowLayout());

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
	private final EmbeddedMediaPlayer previewArea = mediaPlayerComponent
			.getMediaPlayer();

	private JPanel panelCont = new JPanel();

	private String videoLocation;
	private String saveLocation;
	private Boolean _isEdit;
	private Boolean _isCancelled;
	private Boolean _isPreview;

	String _vamixDir;

	public AddTitle() {
		String homeDir = System.getProperty("user.home");
		_vamixDir = homeDir + "/.VAMIX";

		_isEdit = false;

		panelCont.setLayout(new BoxLayout(panelCont, BoxLayout.Y_AXIS));
		panelCont.add(Box.createVerticalGlue());

		inputPanel.add(inputFileLabel);
		sourceField.setColumns(20);
		sourceField.setEditable(false);
		inputPanel.add(sourceField);
		inputPanel.add(sourceBrowse);

		outputPanel.add(outputFileLabel);
		outputField.setColumns(20);
		outputField.setEditable(false);
		outputPanel.add(outputField);
		outputPanel.add(outputBrowse);

		textPanel.add(title);
		text.setColumns(20);
		textPanel.add(text);

		sizePanel.add(size);
		sizeChoice.setVisible(true);
		sizeChoice.setLightWeightPopupEnabled(false);
		sizePanel.add(sizeChoice);

		fontPanel.add(font);
		fontChoice.setVisible(true);
		fontChoice.setLightWeightPopupEnabled(false);
		fontPanel.add(fontChoice);

		colourPanel.add(colour);
		colourChoice.setVisible(true);
		colourChoice.setLightWeightPopupEnabled(false);
		colourPanel.add(colourChoice);

		textSettingPanel.add(sizePanel);
		textSettingPanel.add(fontPanel);
		textSettingPanel.add(colourPanel);

		videoSettingPanel.add(positionPanel);
		videoSettingPanel.add(durationPanel);

		positionPanel.add(position);
		positionChoiceVertical.setVisible(true);
		positionChoiceVertical.setLightWeightPopupEnabled(false);
		positionPanel.add(positionChoiceVertical);

		durationPanel.add(duration);
		durationChoice.setVisible(true);
		durationChoice.setLightWeightPopupEnabled(false);
		durationPanel.add(durationChoice);

		buttons.add(progress);
		buttons.add(preview);
		buttons.add(enter);

		panelCont.add(inputPanel);
		panelCont.add(outputPanel);
		panelCont.add(textPanel);
		panelCont.add(textSettingPanel);
		panelCont.add(videoSettingPanel);
		panelCont.add(buttons);

		sourceBrowse.addActionListener(new inputBrowseListener());
		outputBrowse.addActionListener(new outputBrowserListener());
		enter.addActionListener(new EnterListener());
		preview.addActionListener(new PreviewListener());

		add(panelCont);
	}

	private class inputBrowseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				File file = fc.getSelectedFile();
				videoLocation = file.getAbsolutePath();
				if (file.exists()) {
					Path source = Paths.get(file.toString());
					if (Files.probeContentType(source).contains("video")) {
						sourceField.setText(videoLocation);
					} else {
						JOptionPane
								.showMessageDialog(
										null,
										"This is not a video file, please choose another file.",
										"Invalid file type",
										JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "File doesn't exist!",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (HeadlessException | IOException f) {
				f.printStackTrace();
			}
		}
	}

	private class outputBrowserListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileSaver = new JFileChooser();
			fileSaver.setFileFilter(new FileNameExtensionFilter("MP4 Format",
					"mp4"));
			fileSaver.showDialog(null, "Name output video file");
			saveLocation = fileSaver.getSelectedFile().getAbsolutePath();
			outputField.setText(saveLocation.toString());
			// If user did not enter .mp4 file extension, add it
			if (!outputField.getText().endsWith(".mp4")) {
				outputField.setText(outputField.getText() + ".mp4");
			}
			
			//check if file exists
			try {
				String chkFileExistsCmd = "test -e " + outputField.getText();
				ProcessBuilder checkFileBuilder = new ProcessBuilder("bash",
						"-c", chkFileExistsCmd);
				checkFileBuilder.redirectErrorStream(true);
				Process checkFileProcess;
				checkFileProcess = checkFileBuilder.start();
				checkFileProcess.waitFor();
				if (checkFileProcess.exitValue() == 0) {
					Log log = new Log(true, outputField.getText().toString());
					String logResult = log.checkLog();
					if (logResult!=null){
						String[] parts = logResult.split("\t");
						sourceField.setText(parts[2]);
						outputField.setText(parts[1]);
						text.setText(parts[3]);
						sizeChoice.setSelectedItem(parts[4]);
						fontChoice.setSelectedItem(parts[5]);
						colourChoice.setSelectedItem(parts[6]);
						positionChoiceVertical.setSelectedItem(parts[7]);
						durationChoice.setSelectedItem(Integer.parseInt(parts[8]));
					} else {
						JOptionPane.showMessageDialog(null, "Output file exists, please change file name if you do would not like to overwrite this file.", "Warning",
                                JOptionPane.WARNING_MESSAGE);
					}
				}
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	private class PreviewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				String chkFileExistsCmd = "test -e " + sourceField.getText();
				ProcessBuilder checkFileBuilder = new ProcessBuilder("bash",
						"-c", chkFileExistsCmd);
				checkFileBuilder.redirectErrorStream(true);
				Process checkFileProcess = checkFileBuilder.start();
				checkFileProcess.waitFor();
				if (checkFileProcess.exitValue() == 0) {
					addTextVideo preview = new addTextVideo();
					_isPreview = true;
					preview.execute();
				} else {
					JOptionPane.showMessageDialog(null,
							"Please make sure file exists.");
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class EnterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				String chkFileExistsCmd = "test -e " + sourceField.getText();
				ProcessBuilder checkFileBuilder = new ProcessBuilder("bash",
						"-c", chkFileExistsCmd);
				checkFileBuilder.redirectErrorStream(true);
				Process checkFileProcess = checkFileBuilder.start();
				checkFileProcess.waitFor();
				if (checkFileProcess.exitValue() == 0) {
					_isPreview = false;
					concatVideo concat = new concatVideo();
					progress.setIndeterminate(true);
					concat.execute();
				} else {
					JOptionPane.showMessageDialog(null,
							"Please make sure file exists.");
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a blank video and draws text onto it.
	 */
	class addTextVideo extends SwingWorker<Void, Integer> {
		@Override
		protected Void doInBackground() throws Exception {
			try {
				_isCancelled = true;
				progress.setIndeterminate(true);

				// Get video size and aspect ratio and frame rate
				String getMeta = "avprobe " + sourceField.getText();
				ProcessBuilder getMetaProcess = new ProcessBuilder("bash",
						"-c", getMeta);
				getMetaProcess.redirectErrorStream(true);
				Process metaProcess = getMetaProcess.start();
				BufferedReader stdoutBuffered = new BufferedReader(
						new InputStreamReader(metaProcess.getInputStream()));
				String[] videoMeta = null;
				String line = null;
				while ((line = stdoutBuffered.readLine()) != null) {
					if (line.contains("Video:")) {
						videoMeta = line.split(",");
					}
				}
				String videoSize = (videoMeta[2].split(" "))[1];
				String videoAspect = "1:1";
				if (videoMeta[2].contains("DAR")) {
					videoAspect = videoMeta[2].split(" ")[5];
					videoAspect = videoAspect.substring(0,
							videoAspect.length() - 1);
				}
				String frameRate = (videoMeta[4].split(" "))[1];
				metaProcess.waitFor();
				metaProcess.destroy();

				// Create blank video
				String createBlank = "avconv -y -filter_complex 'color=black' -t "
						+ durationChoice.getSelectedItem().toString()
						+ " -s "
						+ videoSize
						+ " -r "
						+ frameRate
						+ " -aspect "
						+ videoAspect + " " + _vamixDir + "/blank.mpg";
				ProcessBuilder createBlankProcess = new ProcessBuilder("bash",
						"-c", createBlank);
				createBlankProcess.redirectErrorStream(true);
				Process blankProcess = createBlankProcess.start();
				blankProcess.waitFor();
				blankProcess.destroy();

				// Add text to blank video

				// Process text position (should always be centered
				// horizontally)
				String verticalPositionCmd;
				Object cmdYposition = positionChoiceVertical.getSelectedItem();
				if (cmdYposition.equals("Top")) {
					verticalPositionCmd = "y=h-text_h-30";
				} else if (cmdYposition.equals("Centre")) {
					verticalPositionCmd = "y=main_h/2-text_h/2";
				} else {
					verticalPositionCmd = "y=main_h+30";
				}

				String addText = "avconv -y -i "
						+ _vamixDir
						+ "/blank.mpg"
						+ " -strict experimental -vf \"drawtext=fontfile='/usr/share/fonts/truetype/ubuntu-font-family/"
						+ fontChoice.getSelectedItem().toString()
						+ ".ttf':text='" + text.getText() + "':fontsize="
						+ sizeChoice.getSelectedItem().toString()
						+ ":fontcolor=" + colourChoice.getSelectedItem()
						+ ":x=(main_w/2-text_w/2):" + verticalPositionCmd
						+ "\" " + _vamixDir + "/title.mpg";
				ProcessBuilder addTextProcess = new ProcessBuilder("bash",
						"-c", addText);
				addTextProcess.redirectErrorStream(true);
				Process addProcess = addTextProcess.start();
				addProcess.waitFor();
				addProcess.destroy();
				_isCancelled = false;
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void done() {
			if (_isCancelled == false) {
				if (_isPreview == true) {
					try {
						String preview = "avplay " + _vamixDir + "/title.mpg";
						ProcessBuilder previewProcess = new ProcessBuilder(
								"bash", "-c", preview);
						previewProcess.redirectErrorStream(true);
						Process addProcess;
						addProcess = previewProcess.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
					progress.setIndeterminate(false);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Please make sure source file is a valid video file.");
			}
			
		}
	}

	/**
	 * Concatenates title video to the source video.
	 */
	class concatVideo extends SwingWorker<Void, Integer> {
		@Override
		protected Void doInBackground() throws Exception {
			_isCancelled = true;
			progress.setIndeterminate(true);
			
			addTextVideo preview = new addTextVideo();
			preview.execute();
			preview.get();
			progress.setIndeterminate(true);

			// Convert source file to mpg format if it is not already
			videoLocation = sourceField.getText();
			if (!sourceField.getText().endsWith(".mpg")) {
				String convertToAvi = "avconv -y -i " + sourceField.getText()
						+ " " + _vamixDir + "/original.mpg";
				System.out.println(convertToAvi);
				ProcessBuilder convertToAviBuilder = new ProcessBuilder("bash",
						"-c", convertToAvi);
				convertToAviBuilder.redirectErrorStream(true);
				Process convertToAviProcess = convertToAviBuilder.start();
				convertToAviProcess.waitFor();
				convertToAviProcess.destroy();
				videoLocation = _vamixDir + "/original.mpg";
			}

			// Concatenate the videos
			String concatenate;
			concatenate = "avconv -y -i \"concat:" + _vamixDir + "/title.mpg|"
					+ videoLocation + "\" -c copy " + _vamixDir + "/silent.mpg";
			System.out.println(concatenate);
			ProcessBuilder concatenateBuilder = new ProcessBuilder("bash",
					"-c", concatenate);
			concatenateBuilder.redirectErrorStream(true);
			Process concatenateProcess = concatenateBuilder.start();
			concatenateProcess.waitFor();
			concatenateProcess.destroy();

			// Extract sound from original video
			String audioCmd = "avconv -y -i " + sourceField.getText() + " -vn "
					+ _vamixDir + "/audio.mp3";
			ProcessBuilder audioBuilder = new ProcessBuilder("bash", "-c",
					audioCmd);
			audioBuilder.redirectErrorStream(true);
			Process audioProcess = audioBuilder.start();
			audioProcess.waitFor();

			// Cut silence to appropriate length
			String cutCmd = "avconv -y -i resources/30sec.mp3 -t "
					+ durationChoice.getSelectedItem() + " -acodec copy "
					+ _vamixDir + "/cutSilence.mp3";
			ProcessBuilder cutBuilder = new ProcessBuilder("bash", "-c", cutCmd);
			cutBuilder.redirectErrorStream(true);
			Process cutProcess = cutBuilder.start();
			cutProcess.waitFor();

			// Concatenate silent file to audio file
			String concatSilence;
			concatSilence = "cat " + _vamixDir + "/cutSilence.mp3 " + _vamixDir
					+ "/audio.mp3 > " + _vamixDir + "/finalAudio.mp3";
			System.out.println(concatSilence);
			ProcessBuilder concatBuilder = new ProcessBuilder("bash", "-c",
					concatSilence);
			concatBuilder.redirectErrorStream(true);
			Process concatProcess = concatBuilder.start();
			concatProcess.waitFor();

			// Add final audio file to video file
			String mergeCmd = "avconv -i "
					+ _vamixDir
					+ "/silent.mpg -i "
					+ _vamixDir
					+ "/finalAudio.mp3 -map 0:v -map 1:a -vcodec copy -acodec copy "
					+ _vamixDir + "/finalTitleVideo.mpg";
			ProcessBuilder mergeBuilder = new ProcessBuilder("bash", "-c",
					mergeCmd);
			mergeBuilder.redirectErrorStream(true);
			Process mergeProcess = mergeBuilder.start();
			mergeProcess.waitFor();
			
			//Convert mpg to avi
			String convertCmd = "avconv -y -i "
					+ _vamixDir + "/finalTitleVideo.mpg"
					+ " -acodec copy -vcodec copy "
					+ outputField.getText();
			ProcessBuilder convertBuilder = new ProcessBuilder("bash", "-c",
					convertCmd);
			convertBuilder.redirectErrorStream(true);
			Process convertProcess = convertBuilder.start();
			convertProcess.waitFor();

			_isCancelled = false;
			return null;
		}

		@Override
		protected void done() {
			progress.setIndeterminate(false);
			if (_isCancelled == false) {
				new Log(true, outputField.getText(), sourceField.getText(),
						text.getText(),
						sizeChoice.getSelectedItem().toString(),
						fontChoice.getSelectedItem().toString(), colourChoice.getSelectedItem()
								.toString(), positionChoiceVertical
								.getSelectedItem().toString(), durationChoice
								.getSelectedItem().toString());
			}
		}

	}
}