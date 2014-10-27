package subtitle;

import generalOperations.Effects;
import generalOperations.Playback;
import generalOperations.TimeBar;
import gui.VideoPlayer;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import uk.co.caprica.vlcj.player.MediaPlayer;

public class SubtitleTab extends JPanel {
	Canvas _canvas;
	TimeBar _timeBar;
	Playback _playback;
	Effects _effects;
	String _videoLocation;

	VideoPlayer video;

	private File sourceFile;

	private JPanel main = new JPanel();

	private JPanel inputPanel = new JPanel(new FlowLayout());
	private JLabel inputLabel = new JLabel("Video file input: ");
	private JTextField inputField = new JTextField();
	private JButton inputSelectButton = new JButton("Browse");
	private JButton playButton = new JButton("Play");

	private JPanel setPanel = new JPanel();
	private JButton startSetButton = new JButton(
			"Set Current Time as Start Time");
	private JButton endSetButton = new JButton("Set Current Time as End Time");

	String[] columnNames = { "Start Time", "Text", "End Time" };
	int numRows = 1;
	DefaultTableModel model = new DefaultTableModel(numRows, columnNames.length);
	JTable table;
	
	private JPanel tableControlPanel = new JPanel();
	private JButton addRow = new JButton("Add row");
	private JButton deleteRow = new JButton("Delete row");
	private JButton clearButton = new JButton("Clear");

	private JPanel bottomPanel = new JPanel(new FlowLayout());
	private JButton checkButton = new JButton("Check");
	private JButton saveButton = new JButton("Save");

	public SubtitleTab(Canvas canvas, TimeBar timebar, Playback playback,
			Effects effects) {
		_canvas = canvas;
		_timeBar = timebar;
		_playback = playback;
		_effects = effects;

		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

		main.add(inputPanel);
		inputField.setColumns(17);
		inputField.setEnabled(false);
		inputPanel.add(inputLabel);
		inputPanel.add(inputField);
		inputPanel.add(inputSelectButton);
		inputPanel.add(playButton);
		playButton.setEnabled(false);

		main.add(setPanel);
		setPanel.add(startSetButton);
		setPanel.add(endSetButton);

		model.setColumnIdentifiers(columnNames);
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		main.add(new JScrollPane(table));
		
		main.add(tableControlPanel);
		tableControlPanel.add(addRow);
		tableControlPanel.add(deleteRow);
		tableControlPanel.add(clearButton);

		main.add(bottomPanel);
		bottomPanel.add(checkButton);
		bottomPanel.add(saveButton);

		add(main);
		
		inputField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				inputCheck();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				inputCheck();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				//inputCheck();
			}
		});

		// Allows user to choose input files
		inputSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser fileOpener = new JFileChooser();
					fileOpener.showDialog(null,
							"Choose video file to be extracted");
					sourceFile = fileOpener.getSelectedFile();
					if (sourceFile.exists()) {
						Path source = Paths.get(sourceFile.toString());
						if (Files.probeContentType(source).contains("video")) {
							inputField.setText(sourceFile.toString());
							playButton.setEnabled(true);
						} else {
							JOptionPane
									.showMessageDialog(
											null,
											"This is not a video file, please choose another file.",
											"Invalid file type",
											JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"File doesn't exist!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (HeadlessException | IOException e) {
					e.printStackTrace();
				}
			}
		});

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MediaPlayer mediaPlayer;
				if (video != null) {
					mediaPlayer = video.getMediaPlayer();
					if (mediaPlayer.isPlayable()) {
						video.cancel();
					}
				}
				_videoLocation = inputField.getText();
				video = new VideoPlayer(_canvas, _videoLocation, _timeBar,
						_playback, _effects);
				video.execute();
				_playback.setPlayButton();
			}
		});

		addRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.addRow(new Object[] { null, null, null });
			}
		});

		deleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < model.getRowCount(); i++) {
					if (table.isRowSelected(i)) {
						model.removeRow(table.convertRowIndexToModel(i));
					}
				}
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});

		startSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Boolean isSelected = false;
				for (int i = 0; i < table.getRowCount(); i++) {
					if (table.isRowSelected(i)) {
						isSelected = true;
					}
				}
				if (isSelected = true && video != null) {
					String time = _timeBar.getTime();
					for (int i = 0; i < table.getRowCount(); i++) {
						if (table.isRowSelected(i)) {
							table.setValueAt(time, i, 0);
						}
					}
				}
			}
		});

		endSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Boolean isSelected = false;
				for (int i = 0; i < table.getRowCount(); i++) {
					if (table.isRowSelected(i)) {
						isSelected = true;
					}
				}
				if (isSelected = true && video != null) {
					String time = _timeBar.getTime();
					for (int i = 0; i < table.getRowCount(); i++) {
						if (table.isRowSelected(i)) {
							table.setValueAt(time, i, 2);
						}
					}
				}
			}
		});

		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validate();
				Boolean itemsSelected = false;
				for (int i = 0; i < table.getRowCount(); i++) {
					if (table.isRowSelected(i)) {
						itemsSelected = true;
					}
				}
				if (itemsSelected == true) {
					JOptionPane
							.showMessageDialog(
									null,
									"Highlighted rows contain errors! \n All cells should be filled in, end time should not be earlier than start time, and end time should not be later than the start time of the next row.",
									"Warning", JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane
					.showMessageDialog(
							null,
							"All correct, you may save safely.",
							"Great!", JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				validate();

				// Check if any row is selected (meaning error)
				Boolean itemsSelected = false;
				for (int i = 0; i < table.getRowCount(); i++) {
					if (table.isRowSelected(i)) {
						itemsSelected = true;
					}
				}

				if (itemsSelected == false) {
					// Save
					String inputFieldNull = inputField.getText();
					if (inputFieldNull.isEmpty()) {
						JOptionPane.showMessageDialog(null,
								"Please specify a video file above!", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						try {
							int pos = inputField.getText().lastIndexOf(".");
							String filePathName;
							if (pos == -1) {
								filePathName = inputField.getText();
							} else {
								filePathName = inputField.getText().substring(
										0, pos);
							}
							new FileOutputStream(filePathName + ".srt", true)
									.close();
							FileWriter srtFile = new FileWriter(filePathName
									+ ".srt", false);

							for (int i = 0; i < table.getRowCount(); i++) {
								srtFile.write((i + 1) + "\n");
								srtFile.write("00:" + model.getValueAt(i, 0)
										+ ",000 --> 00:"
										+ model.getValueAt(i, 2) + ",000\n");
								srtFile.write(model.getValueAt(i, 1) + "\n\n");
							}
							srtFile.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"Please make sure the selected rows are correct. \n All cells should be filled in, end time should not be earlier than start time, and end time should not be later than the start time of the next row.",
									"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
	}

	public void setTimeBar(TimeBar timeBar) {
		_timeBar = timeBar;
	}

	public void setPlayback(Playback playback) {
		_playback = playback;
	}

	public void setEffects(Effects effects) {
		_effects = effects;
	}
	
	public void clear(){
		for (int i = ((DefaultTableModel) table.getModel())
				.getRowCount() - 1; i > -1; i--) {
			((DefaultTableModel) table.getModel()).removeRow(i);
		}
		model.addRow(new Object[] { null, null, null });
	}
	
	public void inputCheck(){
		try {
			// Check if video file exists
			File f = new File(inputField.getText());
			if(f.exists()) {
				// File exists, check if .srt file exists for this video file
				int pos = inputField.getText().lastIndexOf(".");
				String filePathName;
				if (pos == -1) {
					filePathName = inputField.getText();
				} else {
					filePathName = inputField.getText().substring(
							0, pos);
				}
				String chkSRTExistsCmd = "test -e " + filePathName+".srt";
				ProcessBuilder checkSRTBuilder = new ProcessBuilder("bash", "-c",
						chkSRTExistsCmd);
				checkSRTBuilder.redirectErrorStream(true);
				Process checkSRTProcess;
				checkSRTProcess = checkSRTBuilder.start();
				checkSRTProcess.waitFor();
				if (checkSRTProcess.exitValue() == 0) {
					// .srt file exists for video file, load values into table
					clear();
					BufferedReader br = new BufferedReader(new FileReader(new File(filePathName+".srt")));
					String line;
					while ((line = br.readLine()) != null) {
						int row = Integer.parseInt(line)-1;
						line = br.readLine();
						String startTime = line.substring(3, 8);
						String endTime = line.substring(20,25);
						line = br.readLine();
						String textContent = line;
						br.readLine();
						model.setValueAt(startTime, row, 0);
						model.setValueAt(textContent, row, 1);
					    model.setValueAt(endTime, row, 2);
					    model.addRow(new Object[] { null, null, null });
					}
					br.close();
				}
			} else {
				JOptionPane
				.showMessageDialog(
						null,
						"File doesn't exist!",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void validate() {
		table.selectAll();

		DefaultRowSorter sorter = ((DefaultRowSorter) table.getRowSorter());
		ArrayList list = new ArrayList();
		list.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(list);
		sorter.sort();

		table.clearSelection();

		for (int i = 0; i < table.getRowCount(); i++) {
			// Checks that all rows are filled
			Object first = table.getValueAt(i, 0);
			Object second = table.getValueAt(i, 1);
			Object third = table.getValueAt(i, 2);

			if (first == null || second == null || third == null) {
				table.addRowSelectionInterval(i, i);
			}
		}
		for (int i = 0; i < table.getRowCount() - 1; i++) {
			// Checks if any end time value is greater than the next start time
			// value
			if(!table.isRowSelected(i)){
				if (table.getValueAt(i, 2).toString()
						.compareTo(table.getValueAt(i + 1, 0).toString()) > 0) {
					table.addRowSelectionInterval(i, i);
				} else if (!table.isRowSelected(i + 1)) {
					// Checks if any end time value is smaller than the start
					// time value
					if (table.getValueAt(i, 2).toString()
							.compareTo(table.getValueAt(i, 0).toString()) < 0) {
						table.addRowSelectionInterval(i, i);
					}
				}
			}
			
		}
		
		
	}

}
