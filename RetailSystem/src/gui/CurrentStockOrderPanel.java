package gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class CurrentStockOrderPanel extends JDialog implements ActionListener  {

	public CurrentStockOrderPanel() {
	}
	
	public CurrentStockOrderPanel(ArrayList<Object[]> list) {
		JPanel contentPanePanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(contentPanePanel);
		scrollPane.setMaximumSize(new Dimension(1024,768));
		contentPanePanel.setLayout(new MigLayout());
		setLayout(new GridBagLayout());
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(this);
		
		JLabel lblText = new JLabel(text);
		contentPanePanel.add(lblText, "wrap, push");
		contentPanePanel.add(btnOk,"pushx, alignx center");
		setContentPane(scrollPane);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

}
