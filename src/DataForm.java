import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Arrays;

//import com.mysql.jdbc.jdbc2.optional.*; 
//import javax.sql.*;

public class DataForm implements ActionListener {
	public static final int WIDTH = 500;
	public static final int HEIGHT = 400;
	public static final int NUMBER_OF_CHAR = 30;
	
	private static LoginForm login;
	private static MySQLConnection con;
	
	//class variables for form display
	private JTextArea textField;
	private JButton showButton;
	private JButton userQueryButton;
	private JLabel inputLabel;
	private JTextField inputTextField;
	private JPanel statusBar;
	private JLabel statusLabel;
	private Container contentPane = new Container();
	private JFrame frame;
	
	// class variables for database information
	private String[] columns;
	private Object[][] data;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	// class variables for data table information
	private DefaultTableModel model = new DefaultTableModel();
	private JTable dataTable = new JTable(model);
	private JScrollPane scrollPane = new JScrollPane(dataTable);
		
	public DataForm() {
		frame  = new JFrame("Car Database");
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setName("DataExtractor");
		frame.setResizable(false);
		WindowDestroyer listener = new WindowDestroyer();
		frame.addWindowListener(listener);
		contentPane = frame.getContentPane();
		contentPane.setLayout(new FlowLayout());		
		addComponents();
		frame.setVisible(true);
	}
	
	private void addComponents() {
		//Add input label and text fields
		inputLabel = new JLabel("Input: ");
		contentPane.add(inputLabel);		
		inputTextField = new JTextField(NUMBER_OF_CHAR);
		contentPane.add(inputTextField);
		inputTextField.setText(null);
		
		//Add status bar properties
		statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		contentPane.add(statusBar, BorderLayout.SOUTH);
		statusBar.setPreferredSize(new Dimension(contentPane.getWidth(), 16));
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		statusLabel = new JLabel("Status bar");
		//statusLabel.setHorizontalAlignment();
		statusBar.add(statusLabel);
		
		//Add buttons
		showButton = new JButton("View All Cars");
		showButton.addActionListener(this);
		frame.add(showButton);		
		userQueryButton = new JButton("Get data by user query");
		userQueryButton.addActionListener(this);
		frame.add(userQueryButton);

		textField = new JTextArea();
		textField.setEditable(false);
        frame.add(textField);
		
		
		dataTable = new JTable();
		model = new DefaultTableModel();
		frame.add(dataTable, model);
		dataTable.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("View All Cars"))
			getData();
		else if (actionCommand.equals("Get data by user query"))
			getDataByUserQuery();
	}
	
	public void getData() {
		try {
			//crate and run SQL statement query
			stmt = con.getConnection().createStatement();
			String query = "select * from Car";
			rs = stmt.executeQuery(query);
			
			//use meta data to retrieve information form the result set
			ResultSetMetaData meta = rs.getMetaData();
			int numOfColumns = meta.getColumnCount();
			
			//get column names
			columns = new String[numOfColumns];
			for(int i = 0; i < numOfColumns; i ++) {
				columns[i] = meta.getColumnLabel(i+1);
			}
			
			//add rows to the JTable			
			while(rs.next()){
				// get information from result set
				int numOfRows = rs.getRow() - 1;
				Object temp[][] = new Object[numOfRows+1][numOfColumns];
		
				int id = rs.getInt("ID");
				String make = rs.getString("Make");
				String carModel = rs.getString("Model");
				int year = rs.getInt("Year");
				
				// add to 2D data object
				temp[numOfRows][0] = id;
				temp[numOfRows][1] = make;
				temp[numOfRows][2] = carModel;
				temp[numOfRows][3] = year;	

				//merge data with temp array
				for(int i = 0; i < numOfRows; i++) {
					for(int j= 0; j < numOfColumns; j++) {
						if(temp[i][j] == null && data[i][j] != null) {
							temp[i][j] = data[i][j];
						}							
					}
				}
				
				//clone temp to data to save the whole 2D array
				data = temp.clone();				
			}
			
			//crate table with data and columns
			model = new DefaultTableModel(data, columns);
			dataTable = new JTable(model);
			//add table container
			JFrame tableFrame = new JFrame("Result");
			tableFrame.setLayout(new BorderLayout());
			tableFrame.add(new JScrollPane(dataTable));
			tableFrame.pack();
			tableFrame.setLocationRelativeTo(null);
			tableFrame.setVisible(true);
			
		} catch (Exception e) {
			System.out.println("SQLException: " + e.getMessage());
			e.printStackTrace();
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {} 
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {}
				stmt = null;
			}
		}
	}
	

	public void getDataByUserQuery() {
		try {
			//crate and run SQL statement query
			stmt = con.getConnection().createStatement();
			String query = inputTextField.getText();
			rs = stmt.executeQuery(query);
			
			//use meta data to retrieve information form the result set
			ResultSetMetaData meta = rs.getMetaData();
			int numOfColumns = meta.getColumnCount();
			
			//get column names
			columns = new String[numOfColumns];
			for(int i = 0; i < numOfColumns; i ++) {
				columns[i] = meta.getColumnLabel(i+1);
			}
			
			//add rows to the JTable			
			while(rs.next()){
				// get information from result set
				int numOfRows = rs.getRow() - 1;
				Object temp[][] = new Object[numOfRows+1][numOfColumns];
		
				int id = rs.getInt("ID");
				String make = rs.getString("Make");
				String carModel = rs.getString("Model");
				int year = rs.getInt("Year");
				
				// add to 2D data object
				temp[numOfRows][0] = id;
				temp[numOfRows][1] = make;
				temp[numOfRows][2] = carModel;
				temp[numOfRows][3] = year;	

				//merge data with temp array
				for(int i = 0; i < numOfRows; i++) {
					for(int j= 0; j < numOfColumns; j++) {
						if(temp[i][j] == null && data[i][j] != null) {
							temp[i][j] = data[i][j];
						}							
					}
				}
				
				//clone temp to data to save the whole 2D array
				data = temp.clone();
				
			}
			
			//crate table with data and columns
			model = new DefaultTableModel(data, columns);
			dataTable = new JTable(model);
			//add table container
			JFrame tableFrame = new JFrame("Result");
			tableFrame.setLayout(new BorderLayout());
			tableFrame.add(new JScrollPane(dataTable));
			tableFrame.pack();
			tableFrame.setLocationRelativeTo(null);
			tableFrame.setVisible(true);
			
		} catch (Exception e) {
			System.out.println("SQLException: " + e.getMessage());
			e.printStackTrace();
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {} 
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {}
				stmt = null;
			}
		}
	}
	
	public static void main(String[] args) {
		// Get login information by calling Login form
		login = new LoginForm();
		while(true) {
			while(true) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(login.getIsLogedIn()) break;
			}
			
			// using login information, create connection
			con = new MySQLConnection(login.getUser(), login.getPassword());
			if(con.getSucessfulConnection()) break;
			login.setIsLogedIn(false);
		}
		
		//after successful login, close the login in form and start the data form 
		login.close();
		DataForm gui = new DataForm();
		gui.frame.setVisible(true);		
	}
}
