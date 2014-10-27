package generalOperations;

import gui.VideoPlayer;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class Playback extends JPanel{
	VideoPlayer _videoPlayer;
	EmbeddedMediaPlayer _mediaPlayer;
	JFrame _frame;
	Canvas _canvas;
	String _videoLocation;
	TimeBar _timeBar;
	Effects _effects;
	
	Boolean _paused = false;
	Boolean _endOfVideo = false;
	Boolean _forward = false;
	Boolean _rewind = false;
	
	Timer timer = new Timer();
	TimerTask forwardTask = new ForwardTask();
	TimerTask backwardTask = new BackwardTask();

	// Playback buttons
	private JButton play;
	private JButton stop;
	private JButton sound;
	private JButton forward;
	private JButton back;
	private JPanel volumePanel = new JPanel();
	private JSlider volume = new JSlider(0, 450, 50);
	
	public Playback(JFrame frame, EmbeddedMediaPlayer videoPlayer, Canvas canvas,String videoLocation,TimeBar timebar,Effects effects){
		_mediaPlayer = videoPlayer;
		_frame = frame;
		_canvas = canvas;
		_videoLocation = videoLocation;
		_timeBar = timebar;
		_effects = effects;
		
		Icon stopIcon = new ImageIcon(getClass().getResource("/stop.png"));
		stop = new JButton(stopIcon);
		stop.setBorder(BorderFactory.createEmptyBorder());
		stop.setContentAreaFilled(false);
		this.add(stop);

		Icon backIcon = new ImageIcon(getClass().getResource("/rewind.png"));
		back = new JButton(backIcon);
		back.setBorder(BorderFactory.createEmptyBorder());
		back.setContentAreaFilled(false);
		this.add(back);

		Icon startIcon = new ImageIcon(getClass().getResource(
				"/play-button.png"));
		play = new JButton(startIcon);
		play.setBorder(BorderFactory.createEmptyBorder());
		play.setContentAreaFilled(false);
		this.add(play);

		Icon forwardIcon = new ImageIcon(getClass().getResource("/forward.png"));
		forward = new JButton(forwardIcon);
		forward.setBorder(BorderFactory.createEmptyBorder());
		forward.setContentAreaFilled(false);
		this.add(forward);

		// Volume Control
		Icon soundIcon = new ImageIcon(getClass().getResource("/sound.png"));
		sound = new JButton(soundIcon);
		sound.setBorder(BorderFactory.createEmptyBorder());
		sound.setContentAreaFilled(false);
		//volumePanel.add(sound);
		volumePanel.add(volume);
		this.add(volumePanel);
		
		play.addActionListener(new playListener());
		stop.addActionListener(new stopListener());
		sound.addActionListener(new soundListener());
		forward.addMouseListener(new forwardListener());
		back.addMouseListener(new rewindListener());
		volume.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int value = volume.getValue();
				_mediaPlayer.setVolume(volume.getValue());
			}
		});
	}
	
	public void endReached () {
		_mediaPlayer.stop();
		_paused = true;
		_endOfVideo = true;
		play.setIcon(new ImageIcon(getClass().getResource("/play-button.png")));
	}
	
	public void setMediaPlayer(EmbeddedMediaPlayer mediaPlayer){
		_mediaPlayer = mediaPlayer;
	}
	
	public void setVideoLocation(String videoLocation){
		_videoLocation = videoLocation;
	}
	
	public void setPlayButton(){
		play.setIcon(new ImageIcon(getClass().getResource("/pause.png")));
		_paused = false;
	}
	
	private class ForwardTask extends TimerTask {
	    public void run() {
	        _mediaPlayer.skip(10000);
	    }
	}
	
	private class BackwardTask extends TimerTask {
	    public void run() {
	    	_mediaPlayer.skip(-10000);
	    }
	}
	
	private class forwardListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			forwardTask = new ForwardTask();
			timer.scheduleAtFixedRate(forwardTask, 0, 500);
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
			forwardTask.cancel();
		}
	}

	private class rewindListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			backwardTask = new BackwardTask();
			timer.scheduleAtFixedRate(backwardTask, 0, 500);
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
			backwardTask.cancel();
		}
	}

	private class playListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (_videoLocation==null){
				JOptionPane.showMessageDialog(_frame,"Open a media file by click the Open button");
			} else if (_endOfVideo==true){
				_endOfVideo = false;
				_paused = false;
				_videoPlayer = new VideoPlayer(_canvas,_videoLocation,_timeBar,Playback.this,_effects);
				_mediaPlayer = _videoPlayer.getMediaPlayer();
				_videoPlayer.execute();
				play.setIcon(new ImageIcon(getClass().getResource("/pause.png")));
			} else if (_paused == true){
				_paused = false;
				play.setIcon(new ImageIcon(getClass().getResource("/pause.png")));
				_mediaPlayer.play();
			} else {
				_paused = true;
				play.setIcon(new ImageIcon(getClass().getResource("/play-button.png")));
				_mediaPlayer.pause();
			}
			
		}
	}

	private class stopListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			_mediaPlayer.stop();
			_paused = true;
			//_endOfVideo = true;
			play.setIcon(new ImageIcon(getClass().getResource("/play-button.png")));
		}
	}
	
	private class soundListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			_mediaPlayer.mute();
		}
	}
	
	
}
