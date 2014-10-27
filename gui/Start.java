package gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 
 * This is the class responsible for the look and feel, as well as starting the
 * application by creating an instance of MainFrame.
 * 
 * If GTK is not available, Nimbus is used.
 * 
 * @author zoe
 *
 */
public class Start {

	public static void main(final String[] args) {

		// REFERENCE java nimbus page
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
					UIManager.put("Slider.paintValue", Boolean.FALSE);
				} catch (Exception e) {
					for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							try {
								UIManager.setLookAndFeel(info.getClassName());
							} catch (ClassNotFoundException
									| InstantiationException
									| IllegalAccessException
									| UnsupportedLookAndFeelException e1) {
								e1.printStackTrace();
							}
							break;
						}
					}
				}
				new MainFrame();
			}
		});
	}
}
