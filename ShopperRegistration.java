package applicationGUI;

import application.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * This class represents the GUI of the Shopper Registration/Sign Up
 * @author group_0549
 *
 */
public class ShopperRegistration extends JDialog{//JDialog implements ActionListener{
	private JLabel label;
	private JTextField username;
	private JPasswordField password1;
	private JPasswordField password2;
	private JPasswordField accesscode = new  JPasswordField(8);
	private JPanel panel = new JPanel();
	private JCheckBox lab;
	private JButton save, cancel;
	private ProjectVI p;
	
	/**
	 * This class initializes the Shopper Registration JDialog
	 * @param p -> the ProjectVI instance
	 */
	public ShopperRegistration(ProjectVI p){
		this.p = p;
		
		setTitle("Registration");
		this.createForm();
		
		setSize(400, 200);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		add(panel);
		setVisible(true);
	}
	
	/**
	 * This method creates the form for the sign up
	 */
	public void createForm(){
		GridLayout gLayout = new GridLayout(5, 1);
		panel.setLayout(gLayout);
		panel.setSize(500, 500);
	
		username = new JTextField(15);
		password1 = new JPasswordField(15);
		password2 = new JPasswordField(15);
		
		addLabel("Username", panel);
		addBox(username, panel);
		
		addLabel("Password", panel);
		addBox(password1, panel);
		
		addLabel("Retype Password", panel);
		addBox(password2, panel);
		
		addCheckBox("Administrator",panel);
		addLabel(" ", panel);
		
		save = new JButton("Save");
		cancel = new JButton("Cancel");
		addButton(save, panel);
		addButton(cancel, panel);
		
		this.actionsave();
		this.actioncancel();
		
		this.add(panel);
	}
	
	/**
	 * This method sets the action listener to the save button so that it signs up the shopper
	 */
	public void actionsave(){
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String user = username.getText();
				String pw1 = new String(password1.getPassword());
				String pw2 = new String(password2.getPassword());
				File f = new File("src/DataFiles/" + user +".csv");
				
				if (f.exists())
				{
					JOptionPane.showMessageDialog(null,"Sorry, this username already exists. Please pick another one");
					username.setText("");
					password1.setText("");
					password2.setText("");
	
				}else if(!pw1.equals(pw2)){
					JOptionPane.showMessageDialog(null,"Passwords do not match. Please try again");
					password1.setText("");
					password2.setText("");
				}else{
					if(lab.isSelected()){
						if(actionchecked()){
							p.addUser(user, pw1, true);
							//System.out.println(p.getUser().getSessionID());
						}else{
							JOptionPane.showMessageDialog(null,"Access denied. Please try again.");
						}
					}else {
						p.addUser(user, pw1, false);
					}
					username.setText("");
					password1.setText("");
					password2.setText("");
					CloseDialog();
				}
			}
		});
	}
	
	/**
	 * This method closes the dialog
	 */
	protected void CloseDialog() {
		// TODO Auto-generated method stub
		this.dispose();
	}

	/**
	 * This method sets the action listener for the cancel button so that it closes the dialog
	 */
	public void actioncancel(){
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				CloseDialog();
			}
		});
	}
	
	/**
	 * This method checks if the administrator box is checked.
	 * @return -> true if the user is an admin, else returns false
	 */
	public boolean actionchecked(){
		JPanel add = new JPanel();
		addLabel("Access Code", add);
		addBox(accesscode, add);
		int option = JOptionPane.showConfirmDialog(null, add);
		String code = new String(accesscode.getPassword());
		if (option == JOptionPane.OK_OPTION){
			if(code.equals("0549")){
				return true;
			}
		}
		return false;
	}
	
	
	private void addLabel(String labelText, JPanel panel) {
		JLabel label = new JLabel(labelText);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(label);
		panel.add(p);
	}
	
	private void addCheckBox(String labelText, JPanel panel) {
		lab = new JCheckBox(labelText);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(lab);
		panel.add(p);
	}
	
	private void addBox(JTextField tField, JPanel panel) {
		JPanel p = new JPanel();
		p.add(tField);
		panel.add(p);
	}
	
	private void addButton( JButton b, JPanel panel) {
		JPanel p = new JPanel();
		p.add(b);
		panel.add(p);
	}

}
