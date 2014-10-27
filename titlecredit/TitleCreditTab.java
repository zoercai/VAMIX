package titlecredit;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * 
 * This is the class acting as a container for the AddCredit and AddTitle
 * instances. It is added to the Title/Credit tab.
 * 
 * @author zoe
 *
 */
public class TitleCreditTab extends JPanel {

	AddTitle title = new AddTitle();
	AddCredit credit = new AddCredit();

	public TitleCreditTab() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(Box.createVerticalStrut(10));

		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder titleTitle = BorderFactory.createTitledBorder(blackline,
				"Add Title");
		titleTitle.setTitleJustification(TitledBorder.CENTER);
		title.setBorder(titleTitle);
		add(title);

		this.add(Box.createVerticalStrut(10));

		TitledBorder creditTitle = BorderFactory.createTitledBorder(blackline,
				"Add Credit");
		creditTitle.setTitleJustification(TitledBorder.CENTER);
		credit.setBorder(creditTitle);
		add(credit);
	}

}
