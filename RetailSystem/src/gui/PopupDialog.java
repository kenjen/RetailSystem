package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import tableModels.UneditableTableModel;
import data.Finance;

public class PopupDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton btnOk = new JButton("Ok");
	private JButton btnAdd = new JButton("Add expense");
	private JButton btnCancel = new JButton("Cancel");
	private UtilDateModel model = new UtilDateModel();
	private JDatePanelImpl datePanel = new JDatePanelImpl(model);
	private JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
	private ProfitLossPanel panel;
	private JTextField txtAmount = new JTextField(20);
	private JTextArea txtDescription = new JTextArea(3,20);
	private JLabel lblError = new JLabel();
	private Timer timer;

	public PopupDialog(String text) {	
		JPanel contentPanePanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(contentPanePanel);
		scrollPane.setMaximumSize(new Dimension(1024,768));
		contentPanePanel.setLayout(new MigLayout());
		getContentPane().setLayout(new GridBagLayout());
		btnOk.addActionListener(this);
		
		JLabel lblText = new JLabel(text);
		contentPanePanel.add(lblText, "wrap, push");
		contentPanePanel.add(btnOk,"pushx, alignx center");
		setContentPane(scrollPane);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public PopupDialog(ProfitLossPanel panel){
		this.panel=panel;
		JPanel contentPanePanel = new JPanel();
		contentPanePanel.setLayout(new MigLayout());
		getContentPane().setLayout(new GridBagLayout());
		btnAdd.addActionListener(this);
		btnCancel.addActionListener(this);
		lblError.setVisible(false);
		
		contentPanePanel.add(new JLabel("Amount: "));
		contentPanePanel.add(txtAmount,"wrap");
		contentPanePanel.add(new JLabel("Description: "));
		contentPanePanel.add(txtDescription,"wrap");
		contentPanePanel.add(new JLabel("Date: "));
		contentPanePanel.add(datePicker,"wrap");
		contentPanePanel.add(lblError,"span, pushx, alignx center, wrap");
		contentPanePanel.add(btnAdd,"span, split 2, center");
		contentPanePanel.add(btnCancel);
		setContentPane(contentPanePanel);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnOk || e.getSource() == btnCancel)
		this.setVisible(false);		
		if(e.getSource() == btnAdd){
			
			//get values from the user
			String description = txtDescription.getText();
			double amount = 0;
			try{
				amount = Double.parseDouble(txtAmount.getText());
			}catch(NumberFormatException nfe){
				displayErrorMessage("Invalid Input detected!", Color.red);
				txtAmount.requestFocus();
				txtAmount.selectAll();
				return;
			}
			Date selectedDate = (Date) datePicker.getModel().getValue();
			
			//verify if user values are correct
			if(amount <= 0){
				displayErrorMessage("Amount must be greater than 0!", Color.red);
				txtAmount.requestFocus();
				txtAmount.selectAll();
				return;
			}
			if(txtDescription.getText().isEmpty()){
				displayErrorMessage("Missing description!", Color.red);
				txtDescription.requestFocus();
				return;
			}
			if(selectedDate == null){
				displayErrorMessage("Please select a date!", Color.red);
				return;
			}
			//recreate the table in ProfitLossPanel 
			String columnNames[] = {"Id","Admin name","Date","Comments","Amount"};
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			Finance finance = new Finance(amount,selectedDate, description,true);
			Shop.getFinancialRecords().add(finance);
			
			Object[] object = new Object[5];
			object[0] = finance.getId();
			object[1] = GUIBackBone.getLoggedStaffMember().getName()+", "+GUIBackBone.getLoggedStaffMember().getSurname();
			object[2] = sdf.format(finance.getDate());
			object[3] = finance.getDescription();
			object[4] = finance.getAmount();
			
			this.panel.getTableData().add(object);
			UneditableTableModel model = new UneditableTableModel(this.panel.getTableData().toArray(new Object[Shop.getFinancialRecords().size()][]), columnNames);
			JTable table = new JTable(model);
			this.panel.getScrollPane().getViewport().add(table);
			this.panel.generateFinanceData();
			this.panel.loadChartData();
			this.panel.createChart();
			this.panel.getLblWrittenReport().setText(this.panel.generateHtmlString());
			this.setVisible(false);	
		}
	}
	
	/**
	 * Shows the lblError text for 4 seconds. 
	 * @param error Text for the error message
	 * @param color Color of the message
	 */
	public void displayErrorMessage(String error, Color color){
		if(lblError.isVisible() == false){
			lblError.setForeground(color);
			lblError.setText(error);
			lblError.setVisible(true);
			timer = new Timer(2000, new ActionListener(){
	
				@Override
				public void actionPerformed(ActionEvent e) {
					lblError.setVisible(false);
					timer.stop();
				}
				
			});
			timer.start();
		}
	}

}
