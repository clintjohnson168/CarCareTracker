import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm implements ActionListener, KeyListener{
	private static String user, password;
	private static boolean isLogedIn = false;
	private static JTextField userText = new JTextField(20);
	private static JPasswordField passwordText = new JPasswordField(20);
	private static JFrame frame;
	private static JButton loginButton = new JButton("login");
	private static int loginAttempts = 0;
	
	public LoginForm() {
		frame = new JFrame("Log In");
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		placeComponents(panel);
		frame.pack();
		
		frame.setVisible(true);
		frame.setSize(300, 150);
	}
	//public static void main(String [] args) {}
	
	private void placeComponents(JPanel panel) {
		panel.setLayout(null);
		//Add User Label and text box
		JLabel userLabel = new JLabel("User");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);
		//userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		panel.add(userText);

		//Add password label and text box
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		panel.add(passwordLabel);
		//passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		panel.add(passwordText);

		//Add login button
		loginButton = new JButton("login");
		loginButton.setBounds(180, 80, 80, 25);
		loginButton.addActionListener(this);
		loginButton.setActionCommand("login");
		panel.add(loginButton);
		
		frame.getRootPane().setDefaultButton(loginButton);
		
		//pack the JFrame and set visibility
		
		//JButton registerButton = new JButton("register");
		//registerButton.setBounds(180, 80, 80, 25);
		//panel.add(registerButton);
	}

	public void actionPerformed(ActionEvent	e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("login")) {	
			loginAttempts++;
			user = userText.getText();
			if(loginAttempts > 5) {
				System.out.println("Number of login attempts exceted.\nClosing program.");
				System.exit(0);
			}
			password = String.valueOf(passwordText.getPassword());
			if(!user.isEmpty() && !password.isEmpty()) {
				if(loginAttempts > 5) {
					System.out.println("\nNumber of login attempts exceted.\nClosing program.");
					System.exit(0);
				}
				else {
					//System.out.println("Stored login information");
					isLogedIn = true;
					passwordText.setText("");					
				}
			}
			else {
				System.out.println("Please enter username or password");
			}	
		} 
		else
			System.out.println("Data not stored");
	}
	
	public void keyPressed(KeyEvent e) {
		 if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			 loginButton.doClick();
		 }
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}

	public boolean getIsLogedIn() {
		return isLogedIn;
	}
	
	public void setIsLogedIn(boolean flag) {
		isLogedIn = flag;
	}
	
	public void close() {
		frame.setVisible(false);
		loginButton.removeActionListener(this);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
