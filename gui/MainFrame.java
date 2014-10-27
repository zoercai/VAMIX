package gui;

import generalOperations.Download;
import generalOperations.Effects;
import generalOperations.Playback;
import generalOperations.TimeBar;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import audio.AudioTab;
import titlecredit.AddTitle;
import titlecredit.TitleCreditTab;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MainFrame {
	JFrame frame;
	JSplitPane main = new JSplitPane();
	
	// left-----------------------------------------------
	private JPanel left = new JPanel(new BorderLayout());

	// Tabs section
	private JTabbedPane tabbedPane = new JTabbedPane();
	private Effects effects;
	private JPanel tab2 = new JPanel(new FlowLayout());
	private JTextField temp = new JTextField("hello");
	private JTextField temp2 = new JTextField("hell2o");
	// left-----------------------------------------------

	// right----------------------------------------------
	private JPanel right = new JPanel(new BorderLayout());

	// General buttons section
	private JButton download;
	private JButton open;
	private JButton help;
	private JPanel general = new JPanel(new FlowLayout());

	// Videos section
	Canvas canvas = new Canvas();
	VideoPlayer videoPlayer;
	EmbeddedMediaPlayer mediaPlayer;
	String _videoLocation;
	
	// Dock section
	private JPanel dock = new JPanel(new BorderLayout());
	private TimeBar timeBar;

	// Playback buttons
	Playback playback;

	// right----------------------------------------------

	public MainFrame() {
		frame = new JFrame("VAMIX");
		left.setPreferredSize(new Dimension(450, 670));
		left.setMinimumSize(new Dimension(500, 670));
		main.setLeftComponent(left);
		right.setMinimumSize(new Dimension(500, 670));
		main.setRightComponent(right);

		
		// Video section
		videoPlayer = new VideoPlayer(canvas,null,null,null,null);
		mediaPlayer = videoPlayer.getMediaPlayer();
		canvas.setBackground(Color.black);
		canvas.setVisible(true);
		right.add(canvas, BorderLayout.CENTER);

		
		// General buttons section
		Icon downloadIcon = new ImageIcon(getClass().getResource(
				"/download.png"));
		download = new JButton(downloadIcon);
		download.setBorder(BorderFactory.createEmptyBorder());
		download.setContentAreaFilled(false);
		general.add(download);
		download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Download download = new Download(frame);
			}
		});

		Icon openIcon = new ImageIcon(getClass().getResource("/open.png"));
		open = new JButton(openIcon);
		open.setBorder(BorderFactory.createEmptyBorder());
		open.setContentAreaFilled(false);
		general.add(open);
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				_videoLocation = fc.getSelectedFile().getAbsolutePath();
				if (mediaPlayer.isPlayable()){
					videoPlayer.cancel();
					//videoPlayer.cancel(true);
				}
				videoPlayer = new VideoPlayer(canvas,_videoLocation,timeBar,playback,effects);
				mediaPlayer = videoPlayer.getMediaPlayer();
				videoPlayer.execute();
				playback.setPlayButton();
			}
		});

		Icon helpIcon = new ImageIcon(getClass().getResource("/help.png"));
		help = new JButton(helpIcon);
		help.setBorder(BorderFactory.createEmptyBorder());
		help.setContentAreaFilled(false);
		general.add(help);

		right.add(general, BorderLayout.NORTH);

		
		// Tabs section
		tab2.add(temp2);
		
		JPanel audioTab = new JPanel();
		AudioTab audio = new AudioTab();
		audioTab.add(audio);
		tabbedPane.addTab("Audio", audio);

		JLabel audioIconLabel = new JLabel("Audio");
		Icon audioIcon = new ImageIcon(getClass().getResource("/audio.png"));
		audioIconLabel.setIcon(audioIcon);
		JLabel subtitleIconLabel = new JLabel("Subtitle");
		Icon subtitleIcon = new ImageIcon(getClass().getResource("/subtitle.png"));
		subtitleIconLabel.setIcon(subtitleIcon);
		JLabel titleIconLabel = new JLabel("Title/Credit");
		Icon titleIcon = new ImageIcon(getClass().getResource("/audio.png"));
		titleIconLabel.setIcon(titleIcon);
		
		JPanel titleTab = new JPanel();
		TitleCreditTab titlecredit = new TitleCreditTab();
		titleTab.add(titlecredit);
		tabbedPane.addTab("Subtitle", tab2);
		tabbedPane.addTab("Title/Credits", titleTab);
		
		effects = new Effects();
		tabbedPane.addTab("Effects", effects);
		left.add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.setTabComponentAt(0, audioIconLabel);
		tabbedPane.setTabComponentAt(1, subtitleIconLabel);
		tabbedPane.setTabComponentAt(2, titleIconLabel);
		
		
		// Dock section
		timeBar = new TimeBar(frame,mediaPlayer);
		videoPlayer.setTimeBar(timeBar);
		dock.add(timeBar, BorderLayout.CENTER);

		playback = new Playback(frame,mediaPlayer,canvas,_videoLocation,timeBar,effects);
		dock.add(playback, BorderLayout.SOUTH);
		
		right.add(dock, BorderLayout.SOUTH);

		
		// Main frame
		frame.setContentPane(main);
		frame.setLocation(100, 100);
		frame.setSize(1250, 670);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		// Create .VAMIX folder
		String homeDir = System.getProperty("user.home");
		new File(homeDir+"/.VAMIX").mkdirs();
	}
}