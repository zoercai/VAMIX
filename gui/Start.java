package gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Start {

	public static void main(final String[] args) {

		// REFERENCE java nimbus page
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					/*for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
						
					}*/
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
					UIManager.put("Slider.paintValue", Boolean.FALSE);
				} catch (Exception e) {
					// If Nimbus is not available, you can set the GUI to
					// another look and feel.
				}
				new MainFrame();
			}
		});
	}
}
