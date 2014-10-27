package audio;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Replace extends JPanel {
	private File sourceVideoFile;
	private File sourceAudioFile;
	private File outputFile;

	private JPanel main = new JPanel();
	private FlowLayout flow = new FlowLayout();

	private JPanel inputVideoPanel = new JPanel(new FlowLayout());
	private JLabel inputVideoLabel = new JLabel("Video file input: ");
	private JTextField inputVideoField = new JTextField();
	private JButton inputVideoSelectButton = new JButton("Browse");

	private JPanel inputAudioPanel = new JPanel(new FlowLayout());
	private JLabel inputAudioLabel = new JLabel("Audio file input: ");
	private JTextField inputAudioField = new JTextField();
	private JButton inputAudioSelectButton = new JButton("Browse");

	private JPanel outputPanel = new JPanel(new FlowLayout());
	private JLabel outputLabel = new JLabel("Save As: ");
	private JTextField outputField = new JTextField();
	private JButton outputSelectButton = new JButton("Browse");

	private JProgressBar progressBar = new JProgressBar();

	private JPanel bottomPanel = new JPanel(new FlowLayout());
	private JButton extractButton = new JButton("Replace");

	public Replace() {

		flow.setVgap(15);

		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

		main.add(inputVideoPanel);
		inputVideoField.setColumns(20);
		inputVideoPanel.add(inputVideoLabel);
		inputVideoPanel.add(inputVideoField);
		inputVideoPanel.add(inputVideoSelectButton);

		main.add(inputAudioPanel);
		inputAudioField.setColumns(20);
		inputAudioPanel.add(inputAudioLabel);
		inputAudioPanel.add(inputAudioField);
		inputAudioPanel.add(inputAudioSelectButton);

		main.add(outputPanel);
		outputPanel.add(outputLabel);
		outputField.setColumns(20);
		outputPanel.add(outputField);
		outputPanel.add(outputSelectButton);

		main.add(bottomPanel);
		bottomPanel.add(progressBar);
		bottomPanel.add(extractButton);

		add(main);

		// Allows user to choose input files
		inputVideoSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser videoOpener = new JFileChooser();
				videoOpener.showDialog(null, "Choose source video file");
				sourceVideoFile = videoOpener.getSelectedFile();
				inputVideoField.setText(sourceVideoFile.toString());
			}
		});

		inputAudioSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser audioOpener = new JFileChooser();
				audioOpener.showDialog(null, "Choose source audio file");
				sourceAudioFile = audioOpener.getSelectedFile();
				inputAudioField.setText(sourceAudioFile.toString());
			}
		});

		// Allows user to specify location and name of the output file
		outputSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileSaver = new JFileChooser();
				fileSaver.setFileFilter(new FileNameExtensionFilter(".avi","AVI audio format"));
				fileSaver.showDialog(null, "Name output video file");
				outputFile = fileSaver.getSelectedFile();
				outputField.setText(outputFile.toString());
			}
		});

		//button activates the replaceBackground process
		extractButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if ((inputVideoField.getText() != null) && (inputAudioField.getText() != null)
						&& (outputField.getText() != null)) {
					if (!outputField.getText().endsWith(".avi")) {
						outputField.setText(outputField.getText()+".avi");
					}
					replaceBackground replace = new replaceBackground(
							inputVideoField.getText(), inputAudioField.getText()
									, outputField.getText());
					replace.execute();
				} else {
					JOptionPane
							.showMessageDialog(null,
									"Replace not completed. Please specify all files correctly!");
				}
			}
		});
	}

	public class replaceBackground extends SwingWorker<Integer, Integer> {
		private int status;
		private String inputVideoFile;
		private String inputAudioFile;
		private String outputFile;

		public replaceBackground(String inputVideoFile, String inputAudioFile,
				String outputFile) {
			this.inputVideoFile = inputVideoFile;
			this.inputAudioFile = inputAudioFile;
			this.outputFile = outputFile;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			String chkFileExistsCmd = "test -e " + outputFile;
			ProcessBuilder checkFileBuilder = new ProcessBuilder("bash", "-c",
					chkFileExistsCmd);
			checkFileBuilder.redirectErrorStream(true);
			Process checkFileProcess = checkFileBuilder.start();
			if (!isCancelled()) {
				status = checkFileProcess.waitFor();
			}
			if (checkFileProcess.exitValue() == 0) { // file exists already
				Object[] confirm = { "Override", "Cancel" };
				int a = JOptionPane
						.showOptionDialog(
								null,
								"Output file name already exists! Would you like to override existing file? Click Cancel if you would like to specify another output file name.",
								"Output File Name Exists!",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, confirm,
								confirm[1]);
				if (a == JOptionPane.YES_OPTION) { // override
					String avconvCmd = "avconv -y -i " + inputVideoFile
							+ " -i " + inputAudioFile
							+ " -map 0:v -map 1:a -vcodec copy -acodec copy "
							+ outputFile;
					ProcessBuilder avconvBuilder = new ProcessBuilder("bash",
							"-c", avconvCmd);
					avconvBuilder.redirectErrorStream(true);
					progressBar.setIndeterminate(true);
					Process avconvProcess = avconvBuilder.start();
					if (!isCancelled()) {
						status = avconvProcess.waitFor();
					}
					if (avconvProcess.exitValue() != 0) {
						this.cancel(true);
					}
				} else {
					this.cancel(true);
					JOptionPane
							.showMessageDialog(
									null,
									"Error! Replace was not successful. Please check output file name and make sure it contains the appropriate extension.");
				}

			} else { // file doesn't exist
				// avconv it
				String avconvCmd = "avconv -i "+inputVideoFile+" -i "+inputAudioFile+" -map 0:v -map 1:a -vcodec copy -acodec copy "+outputFile;
				ProcessBuilder avconvBuilder = new ProcessBuilder("bash", "-c",
						avconvCmd);
				avconvBuilder.redirectErrorStream(true);
				progressBar.setIndeterminate(true);
				Process avconvProcess = avconvBuilder.start();
				if (!isCancelled()) {
					status = avconvProcess.waitFor();
				}
				if (avconvProcess.exitValue() != 0) {
					this.cancel(true);
					JOptionPane
							.showMessageDialog(
									null,
									"Error! Replace was not successful. Please check output file name and make sure it contains the appropriate extension.");
				}
			}

			return null;
		}

		@Override
		protected void done() {
			if (!this.isCancelled()) {
				JOptionPane.showMessageDialog(null, "Replace completed!");
				progressBar.setIndeterminate(false);
			} else if (this.isCancelled()) {
				JOptionPane.showMessageDialog(null, "Replace not completed.");
				progressBar.setIndeterminate(false);
			}
		}

	}

}
