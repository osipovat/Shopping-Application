package applicationGUI;

import application.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.*;

/**
 * The CustomerRegister class represents the customer window where he can register
 * @author group_0549
 *
 */
public class CustomerRegister extends JDialog{//JDialog implements ActionListener{
	private JLabel label;
	private JTextField[] textFields;
	private JPanel panel = new JPanel();
	private JButton save, cancel;
	private ProjectVI p;
	private Shopper shopper;
	JComboBox<String> choose;
	
	/**
	 * Constructs a CustomerRegister instance with the specified project and shopper
	 * @param shopper -> shopper
	 * @param p -> ProjectVI
	 */
	public CustomerRegister(Shopper shopper, ProjectVI p){
		this.p = p;
		this.shopper = shopper;
		
		setTitle("Customer Registration");
		this.createForm();
		
		setSize(500, 500);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		add(panel);
		setVisible(false);
	}
	
	/**
	 * Creates a registration for for the customer
	 */
	public void createForm(){
		GridLayout gLayout = new GridLayout(12, 1);
		panel.setLayout(gLayout);
		panel.setSize(500, 500);
		
		textFields = new JTextField[11];
		for(int i = 0; i < textFields.length; i++)
			textFields[i] = new JTextField(20);

		String[] info = {"First Name", "Street","City"};
		
		for(int i = 0; i < info.length-1; i++){
			addLabel(info[i],panel);
			addBox(textFields[i], panel);
		}
		addLabel(info[2],panel);
		
		String[] citiesList = new String[p.inventory.shopperManager.getCities().size()];
		for (int i=0; i < citiesList.length;i++) 
			citiesList[i] = p.inventory.shopperManager.getCities().get(i);
		
		choose = new JComboBox<String>(citiesList);
		
		JPanel p = new JPanel();
		p.add(choose);
		panel.add(p);
		
		save = new JButton("Save");
		actionsave();
		cancel = new JButton("Cancel");
		actioncancel();
		addButton(save, panel);
		addButton(cancel, panel);
		
		this.add(panel);
	}
	
	/**
	 * Adds label to the panel
	 * @param labelText -> label
	 * @param panel -> panel
	 */
	private void addLabel(String labelText, JPanel panel) {
		JLabel label = new JLabel(labelText);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(label);
		panel.add(p);
	}
	
	/**
	 * Adds box to the panel
	 * @param tField -> textField
	 * @param panel -> panel
	 */
	private void addBox(JTextField tField, JPanel panel) {
		JPanel p = new JPanel();
		p.add(tField);
		panel.add(p);
	}
	
	/**
	 * Adds button to the panel
	 * @param b -> button
	 * @param panel -> panel
	 */
	private void addButton( JButton b, JPanel panel) {
		JPanel p = new JPanel();
		p.add(b);
		panel.add(p);
	}

	public static void main(String[] args){
		//new CustomerRegister();
		
	}

	/**
	 * Saves the customer
	 */
	public void actionsave(){
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String[] info = new String[11];
				for(int i = 0; i < textFields.length; i++){
					info[i] = textFields[i].getText();
				}
				if(info[0].isEmpty()){
					JOptionPane.showMessageDialog(null,"Please type in your first name");
				}
				else if(shopper.getCustomer() == null){
					//create new customer
					String userID = shopper.getUsername();
					String pw = shopper.getPassword();
					String firstN = info[0];
					String street = info[1];
					String city = choose.getSelectedItem().toString();
					System.out.println(city);
					
					Customer customer = new Customer(userID, pw, firstN, city, street);
					//register customer
					shopper.setCustomer(customer);
				}
				
				CloseDialog();
			}
		});
	}
	
	/**
	 * Closes the dialog
	 */
	protected void CloseDialog() {
		// TODO Auto-generated method stub
		this.dispose();
	}

	/**
	 * Cancels the action
	 */
	public void actioncancel(){
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				CloseDialog();
			}
		});
	}
}
