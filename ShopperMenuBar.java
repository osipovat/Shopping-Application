package applicationGUI;

import application.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * This class represents the tool bar for the Shopper
 * @author group_0549
 *
 */
public class ShopperMenuBar extends JPanel{
	private JButton sc = new JButton("Shopping Cart");
	private JButton i = new JButton("Invoices");
	private JLabel welcome = new JLabel("Welcome ");
	private ProjectVI p;
	private MainGUI main;
	private Shopper shop;
	
	/**
	 * This method creates an instance of the shopper menu bar
	 * @param p -> ProjectVI
	 * @param main -> MainGUI frame
	 */
	public ShopperMenuBar(ProjectVI p, MainGUI main){
		super(new BorderLayout());
		this.p=p;
		this.main = main;
		this.shop = (Shopper)p.getUser();
		JToolBar toolBar = new JToolBar("Shopper Account");
		
		setPreferredSize(new Dimension(450, 130));
		add(toolBar, BorderLayout.PAGE_START);
		setVisible(false);
		
		toolBar.add(welcome);
		addButtons(toolBar);
	}
	
	/**
	 * This method creates a welcome sign for the user
	 */
	public void changeWelcome(){
		this.welcome.setText("Welcome, " + main.p.getUser().getUsername() + "  ");
	}
	
	/**
	 * This method adds buttons to the toolbar
	 * @param tbar
	 */
	public void addButtons(JToolBar tbar){
		tbar.add(sc);
		this.actionCart();
		tbar.add(i);
		this.actionInvoice();
	}
	
	/**
	 * This method sets the action listener to the shopping cart button
	 */
	public void actionCart(){
		sc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//main.cart.setVisible(true);
				new ShoppingCartGUI(p.getUser(), p);
			}
		});
	}
	
	/**
	 * This method sets the action listener to the invoice button
	 */
	public void actionInvoice(){
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				System.out.println("user");
				Shopper shop = (Shopper)p.getUser();
				String inv = "";
				
				if (shop.getCustomer() == null){
					JOptionPane.showMessageDialog(null, "No Invoices yet");
				}
				else if (shop.getCustomer() != null){
					for (ShippingInvoice si : shop.getCustomer().getInvoices()){
						inv += si.toString() + "\n ";
					}
					JOptionPane.showMessageDialog(null, inv);
				}
			}
		});
	}
	
	
}
