package applicationGUI;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import application.*;
import jdk.management.resource.internal.inst.WindowsAsynchronousFileChannelImplRMHooks;

/**
 * This class represents the GUI for the shopper cart
 * @author group_0549
 *
 */
public class ShoppingCartGUI extends JFrame{
	Canvas c = new Canvas();
	JScrollPane scrPane;
	private Shopper shopper;
	private ShoppingCart shoppingCart;
	private HashMap<Product, Integer> cart;
	private ProjectVI project;
	private JPanel panel;
	private JButton checkout = new JButton("Checkout");
	private JButton save = new JButton("Save Cart");
	private JButton cancel = new JButton("Clear Cart");
	private JButton remove;
	private CustomerRegister cr;
	private int orderID;
	
	/**
	 * This method initializes a GUI frame for the shopping cart
	 * @param shop ->the user 
	 * @param p -> the name of the ProjectVI
	 */
	public ShoppingCartGUI(User shop, ProjectVI p){
		this.project = p;
		this.shopper = (Shopper) project.getUser();
		this.cr = new CustomerRegister(shopper, project);
		
		this.shoppingCart = shopper.getShoppingCart();
		this.cart = shoppingCart.getCart();
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JLabel prod;
		
		for (Product pr : this.cart.keySet()){
			String description = pr.getDescription();
			String quantity = String.valueOf(this.cart.get(pr)); 
			String price = String.valueOf(pr.getPrice());
			JPanel info = new JPanel(new FlowLayout());
			prod = new JLabel();
			ImageIcon prodImage = new ImageIcon("src/Products/"+pr.getDescription()+".jpg");
			prod.setIcon(new ImageIcon(getScaledImage(prodImage.getImage(), 160, 160)));
			
			prod.setText("<html><div style='text-align: center;'>"+description+"<br>"+price + "</html>");
			
			
			prod.setHorizontalTextPosition(JLabel.RIGHT);
			info.add(prod);
			
			//change of quantity
			JPanel changeQuantity = new JPanel();
			changeQuantity.setLayout(new BoxLayout(changeQuantity, BoxLayout.PAGE_AXIS));
			JTextField quant = new JTextField(quantity);
			changeQuantity.add(quant);
			JButton saveq = new JButton("save");
			changeQuantity.add(saveq);
			saveq.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					shoppingCart.changeQuantity(pr, Integer.parseInt(quant.getText()));
					quant.setText(quant.getText());
				}
			}); 
			info.add(changeQuantity);
			
			//remove button
			remove = new JButton("Remove");
			info.add(remove);
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					shoppingCart.remove(pr);
					panel.remove(info);
					panel.revalidate();
					panel.repaint();
				}
			});

			panel.add(info);
		}
		
		//save cart button
		panel.add(save);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				project.inventory.shopperManager.saveFile(shopper);
				JOptionPane.showMessageDialog(null, "Shopping cart saved");
			}
		}); 
		
		//cancel button
		panel.add(cancel);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				resetCart();
		
			}
		}); 
		
		//checkout button
		panel.add(checkout);
		checkout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				double amount = 0;
				
				if(cart.isEmpty()){
					JOptionPane.showMessageDialog(null, "Shopping Cart is empty");
				}
				else if(shopper.getCustomer() == null){
					cr.setVisible(true);
				}
				else {
					
					shopper.getCustomer().setCart(shopper.getShoppingCart());
					shopper.getCustomer().setShopperID(shopper.getShopperID());
					
					orderID = project.checkout(shopper.getCustomer().getShopperID(), shopper.getSessionID());
					shopper.cancelCart(); 
					//shoppingCart.setTotal(0);
					
					String product = "";
					for(Product p : cart.keySet()){
						product += p.getDescription() + ", " + cart.get(p) + ", " + p.getPrice() + "\n";
						amount += cart.get(p) * p.getPrice();
					}
					
					JOptionPane.showMessageDialog(null, product + "Order ID: " + String.valueOf(orderID) + "\n Amount: " + String.format("%.2f", amount));
			
					resetCart();
				}
			}
		}); 
		
		add(panel);
		
		setSize(600, 500);
		
		scrPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		add(scrPane);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setTitle("Shopping Cart");
		setVisible(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	/**
	 * This method clears the cart
	 */
	public void resetCart(){
		panel.removeAll();
		panel.repaint();  

		panel.add(save);
		panel.add(cancel);
		panel.add(checkout);
	}
	
	/**
	 * This method scales the image to a specified height and width
	 * @param srcImg -> the name of the source image
	 * @param w -> the name of the width
	 * @param h -> the height of the image
	 * @return 
	 */
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}

}
