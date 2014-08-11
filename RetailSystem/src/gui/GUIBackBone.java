package gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.*;

public class GUIBackBone  extends JFrame{
	
	//Create Panels for Tabbed Pane
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	
	JLabel label1 = new JLabel();
	JLabel label2 = new JLabel();

			
	//create the tabbed Pane
	JTabbedPane tabbedPane = new JTabbedPane();
	
	public GUIBackBone(){
		
		panel1.add(label1);
		panel2.add(label2);
		
		tabbedPane.add("Label1", panel1);
		tabbedPane.add("label2", panel2);
		
		add(tabbedPane);
	}
}
