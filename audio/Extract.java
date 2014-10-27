package audio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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

public class Extract extends JPanel{

	private File sourceFile;
	private File outputFile;
	
	private JPanel main = new JPanel();
	private FlowLayout flow = new FlowLayout();

	private JPanel inputPanel = new JPanel(new FlowLayout());
	private JLabel inputLabel = new JLabel("Video file input: ");
	private JTextField inputField = new JTextField();
	private JButton inputSelectButton = new JButton("Browse");

	private JPanel outputPanel = new JPanel(new FlowLayout());
	private JLabel outputLabel = new JLabel("Save As: ");
	private JTextField outputField = new JTextField();
	private JButton outputSelectButton = new JButton("Browse");

	private JProgressBar progressBar = new JProgressBar();

	private JPanel bottomPanel = new JPanel(new FlowLayout());
	private JButton extractButton = new JButton("Extract");
	
	public Extract(){
		flow.setVgap(15);
		
		main.setLayout(new BoxLayout(main,BoxLayout.PAGE_AXIS));
		
		main.add(inputPanel);
		inputField.setColumns(20);
		inputField.setEditable(false);
		inputPanel.add(inputLabel);
		inputPanel.add(inputField);
		inputPanel.add(inputSelectButton);
		
		main.add(outputPanel);
		outputPanel.add(outputLabel);
		outputField.setColumns(20);
		outputField.setEditable(false);
		outputPanel.add(outputField);
		outputPanel.add(outputSelectButton);
		
		main.add(bottomPanel);
		bottomPanel.add(progressBar);
		bottomPanel.add(extractButton);
		
		add(main);
		
		
		// Allows user to choose input files
		inputSelectButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					JFileChooser fileOpener = new JFileChooser();
					fileOpener.showDialog(null,"Choose video file to be extracted");
					sourceFile = fileOpener.getSelectedFile();
					
					Path source = Paths.get(sourceFile.toString());
					if(Files.probeContentType(source).contains("video")){
						inputField.setText(sourceFile.toString());
					} else {
						JOptionPane.showMessageDialog(null,
							    "This is not a video file, please choose another file.",
							    "Invalid file type",
							    JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
		});
		
		// Allows user to specify location and name of the output file
		outputSelectButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileSaver = new JFileChooser();
				fileSaver.setFileFilter(new FileNameExtensionFilter("MP3 audio format","mp3"));
				fileSaver.showDialog(null, "Name output audio file");
				outputFile = fileSaver.getSelectedFile();
				outputField.setText(outputFile.toString());
			}
		});
		
		//button activates the stripBackground process
		extractButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ((inputField.getText()!=null) && (outputFile!=null)){
					if(!outputField.getText().endsWith(".mp3"))
					{
						outputField.setText(outputField.getText()+".mp3");
					}
					stripBackground extract = new stripBackground(inputField.getText(),outputField.getText());
					extract.execute();		
				}else{
					JOptionPane.showMessageDialog(null, "File not extracted. Please specify both files correctly!");
				}
			}
		});
	}
	
	
	public class stripBackground extends SwingWorker<Integer, Integer> {
		private int status;
		private String inputFile;
		private String outputFile;

		public stripBackground(String inputFile,String outputFile) {
			this.inputFile = inputFile;
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
					String avconvCmd = "avconv -y -i " + inputFile + " -vn " + outputFile;
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
					}
				} else {
					this.cancel(true);
					JOptionPane
							.showMessageDialog(
									null,
									"Error! Extraction was not successful. Please check output file name and make sure it contains the appropriate extension.");
				}

			} else { // file doesn't exist
				// avconv it
				String avconvCmd = "avconv -i " + inputFile + " -vn " + outputFile;
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
									"Error! Extraction was not successful. Please check output file name and make sure it contains the appropriate extension.");
				}
			}

			return null;
		}

		@Override
		protected void done() {
			if (!this.isCancelled()) {
				JOptionPane.showMessageDialog(null, "Extract completed!");
				progressBar.setIndeterminate(false);
			} else if (this.isCancelled()) {
				JOptionPane.showMessageDialog(null, "Extract not completed.");
				progressBar.setIndeterminate(false);
			}
		}

		
	}
}
