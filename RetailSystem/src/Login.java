import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;


public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtLoginHere;
	private JTextField txtTestEdit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//frame again
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		txtLoginHere = new JTextField();
		txtLoginHere.setHorizontalAlignment(SwingConstants.CENTER);
		txtLoginHere.setText("Login Here");
		contentPane.add(txtLoginHere, BorderLayout.CENTER);
		txtLoginHere.setColumns(10);
		
		txtTestEdit = new JTextField();
		txtTestEdit.setHorizontalAlignment(SwingConstants.CENTER);
		txtTestEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtTestEdit.setText("Please Login");
		contentPane.add(txtTestEdit, BorderLayout.NORTH);
		txtTestEdit.setColumns(10);
	}

}
