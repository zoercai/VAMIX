package gui;

import generalOperations.Effects;
import generalOperations.Playback;
import generalOperations.TimeBar;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * 
 * Creates a video player. Uses vlcj to create a video player, and attaches it
 * onto the canvas in the main frame.
 * 
 * @author zoe
 *
 */
public class VideoPlayer extends SwingWorker<Void, Integer> {
	// GUI
	Canvas _canvas;
	MediaPlayerFactory _mediaPlayerFactory;
	CanvasVideoSurface _videoSurface;
	EmbeddedMediaPlayer _mediaPlayer;

	// Logic
	String _videoLocation;
	Boolean _endOfVideo;
	int max; // video length

	TimeBar _timeBar;
	Playback _playback;
	Effects _effects;
	Timer timeBarTimer;

	public VideoPlayer(Canvas canvas, String videoLocation, TimeBar timebar,
			Playback playback, Effects effects) {
		_canvas = canvas;
		_mediaPlayerFactory = new MediaPlayerFactory();
		_videoSurface = _mediaPlayerFactory.newVideoSurface(_canvas);
		_mediaPlayer = _mediaPlayerFactory.newEmbeddedMediaPlayer();
		_mediaPlayer.setVideoSurface(_videoSurface);

		_videoLocation = videoLocation;
		_timeBar = timebar;
		_playback = playback;
		_effects = effects;

		if (_timeBar != null && _playback != null && _effects != null) {
			_timeBar.setMediaPlayer(_mediaPlayer);
			_playback.setMediaPlayer(_mediaPlayer);
			_playback.setVideoLocation(_videoLocation);
			_effects.setMediaPlayer(_mediaPlayer);
		}

		timeBarTimer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int time = (int) _mediaPlayer.getTime();
				_timeBar.setValue(time);
			}
		});
	}

	public EmbeddedMediaPlayer getMediaPlayer() {
		return _mediaPlayer;
	}

	public void setTimeBar(TimeBar timeBar) {
		_timeBar = timeBar;
	}

	public void setPlayback(Playback playback) {
		_playback = playback;
	}

	@Override
	protected Void doInBackground() throws Exception {
		_mediaPlayer.playMedia(_videoLocation);

		// Sets timeBar max value
		_mediaPlayer.parseMedia();
		max = (int) _mediaPlayer.getMediaMeta().getLength();
		_timeBar.setMaximum(max);

		timeBarTimer.start();

		while ((int) _mediaPlayer.getTime() != max) {
			// do nothing

		}
		timeBarTimer.stop();
		return null;
	}

	@Override
	protected void done() {
		_mediaPlayer.stop();
		_timeBar.stop();
		_playback.endReached();
	}

	public void cancel() {
		_mediaPlayer.stop();
		_timeBar.stop();
		timeBarTimer.stop();
	}
}
