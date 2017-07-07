package applicationGUI;

import application.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class LoginGUI extends JDialog{
	private JButton blogin = new JButton("Login");
	private JPanel panel = new JPanel();
	private JTextField username = new JTextField(15);
	private JPasswordField password = new JPasswordField(15);

	private ProjectVI p;
	public int session;
	private MainGUI main;
	public LoginGUI(ProjectVI p, MainGUI f){
		
		this.main = f;
		this.p = p;
		//this.shopper = new ShopperMenuBar(main, p.user.getUsername());
		this.setTitle("Log in");
		setSize(300, 200);
		setLocation(500, 280);
		panel.setLayout(null);
		
		JLabel userLabel = new JLabel("Username");
		userLabel.setBounds(10, 30, 160, 20);
		panel.add(userLabel);
		

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 65, 160, 20);
		panel.add(passwordLabel);
		
		username.setBounds(90, 30, 150,20);
		password.setBounds(90,65,150,20);
		blogin.setBounds(110,100,80,20);
		
		panel.add(blogin);;
		panel.add(username);
		panel.add(password);
		
		this.actionlogin();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		getContentPane().add(panel);
		
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
	}
	
	public void actionlogin(){
		blogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String user = username.getText();
				String pw = new String(password.getPassword());
				File f = new File("src/DataFiles/" + user +".csv");

				if (!f.exists()) //file does not exists
				{
					JOptionPane.showMessageDialog(null,"Sorry, this username does not exist. Please try again");
					username.setText("");
					password.setText("");
				}else{ //must initialize inventory

					if(p.login(user, pw) == -1){
						JOptionPane.showMessageDialog(null,"Wrong password. Please try again");
						password.setText("");
					}else{
						//should log in

						session = p.login(user, pw);
						main.sessionID = session;

						username.setText("");
						password.setText("");
						main.loginMenu.setText("Log Out");
						main.loginMenu.setName("Log Out");

						main.addToolBar(session % 2 == 0);
						if(session % 2 == 0){ //check if user is admin or shopper
							main.admin.changeWelcome();
							main.admin.setVisible(true);
						} else{
							
							main.shopper.changeWelcome();
							main.shopper.setVisible(true);

						}
						CloseDialog();

					}
				}
				main.setProducts();
			}
		});
	}
	
	protected void CloseDialog() {
		this.dispose();
	}

}
