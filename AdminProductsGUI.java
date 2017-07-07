package applicationGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.*;

/**
 * The AdminProductGUI class represents the administrator window where he can modify the product
 * @author group_0549
 *
 */
public class AdminProductsGUI extends JDialog{
	private JPanel panel = new JPanel();
	private JButton jaddProduct;
	private JButton jApply;
	private JButton jAppl;
	private ProjectVI p;
	private int sessionID;
	
	/**
	 * Constructs a AdminProductGUI instance with the specified project
	 * @param p -> ProjectVI
	 */
	public AdminProductsGUI(ProjectVI p){
		this.p = p;
		this.sessionID = p.getUser().getSessionID();
		setLayout(new BorderLayout());
		panel.setLayout(new GridLayout(12,1));
		setTitle("Product Controls");
		setSize(350, 400);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		this.setUp();
		add(panel);
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets the menu bar where you can maintain product
	 */
	public void setUp(){
		JLabel products = new JLabel("Maintain Products");
		jaddProduct = new JButton("Add Product");

		JLabel change = new JLabel("Change product's");
		panel.add(products);
		panel.add(jaddProduct);
		addProduct();
		panel.add(change);
		ArrayList<JTextField> inform = new ArrayList<JTextField>();
		String[] labels = {"ID: ", "Image URL: ", "Price: ", "Description: ", "Availability: "};
		for (int i = 0, n = labels.length; i < n; i++) {
			JPanel panel2 = new JPanel(new FlowLayout());
			JLabel label2 = new JLabel(labels[i]);
			JTextField text2 = new JTextField(10);
			
			panel2.add(label2);
			panel2.add(text2);
			panel.add(panel2);  
			inform.add(text2);
		}

		JLabel update = new JLabel("Update quantity in Distribution Center");
		panel.add(update);
		
		JPanel panel3 = new JPanel(new FlowLayout());
		JLabel DC = new JLabel("Distribution Center:");
		JTextField text3 = new JTextField(10);
		panel3.add(DC);
		panel3.add(text3);
		panel.add(panel3); 
		JPanel panel4 = new JPanel(new FlowLayout());
		JLabel quantity = new JLabel("Quantity:");
		JTextField text4 = new JTextField(10);
		panel4.add(quantity);
		panel4.add(text4);
		panel.add(panel4);
		jApply = new JButton("Update");
		panel.add(jApply);
		
		ProductManager pm = p.inventory.productManager;
		System.out.println(pm);
		jApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//ProductManager pm = ((Administrator)p.getUser()).getProductManager();
				if(!inform.get(3).getText().isEmpty() && !inform.get(0).getText().isEmpty()){
					
					Product prod = p.inventory.getProduct(Integer.parseInt(inform.get(0).getText()));
					
					pm.changeProductDescription(prod, inform.get(3).getText());
				
				}
				if(!inform.get(2).getText().isEmpty() && !inform.get(0).getText().isEmpty()){
					
					Product prod = p.inventory.getProduct(Integer.parseInt(inform.get(0).getText()));
					
					pm.changeProductPrice(prod, Double.parseDouble(inform.get(2).getText()));
				
				}
				if(!inform.get(4).getText().isEmpty() && !inform.get(0).getText().isEmpty()){
					
					Product prod = p.inventory.getProduct(Integer.parseInt(inform.get(0).getText()));
					
					pm.changeProductQuantityGui(prod, Integer.parseInt(inform.get(4).getText()));
				}
				if(!text3.getText().isEmpty() && !inform.get(0).getText().isEmpty() && !text4.getText().isEmpty()){
					
					p.updateQuantity(Integer.parseInt(inform.get(0).getText()), text3.getText(), Integer.parseInt(text4.getText()), sessionID);
				}
			
			}
		});
	}
	
	/**
	 * Adds product to the file and to the inventory and sets the panel in order to do these actions
	 */
	public void addProduct(){
		jaddProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			JPanel add = new JPanel();
			add.setLayout(new BoxLayout(add, BoxLayout.PAGE_AXIS));
			ArrayList<JTextField> texts = new ArrayList<JTextField>();
			ArrayList<String> info = new ArrayList<String>();
			String[] labels = {"Category: ", "Price: ", "Description: "};
			for (int i = 0, n = labels.length; i < n; i++) {
				JLabel label2 = new JLabel(labels[i]);
				JTextField text2 = new JTextField(10);
				add.add(label2);
				add.add(text2);
				texts.add(text2);
			}
			int option = JOptionPane.showConfirmDialog(null, add);
			if (option == JOptionPane.OK_OPTION){
				for(int i = 0; i < texts.size(); i++){
				
					String str = texts.get(i).getText();
					info.add(str);
				}
			}
			
			System.out.println(info);
			String category = info.get(0);
			int code = 0;
			for (int i = 0; i < p.inventory.getCategoryList().size(); i ++){
				if (category.equals(p.inventory.getCategoryList().get(i).getDescription())){
					code = p.inventory.getCategoryList().get(i).getCategoryCode();
					
				}
			}
			double price = Double.parseDouble(info.get(1));
			String description = info.get(2);
			p.addProduct(description, code, price, sessionID);
			
			}
		});
	}
	
	
//	public void changeProduct(){
//		jApply.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ae) {
//			
//			}
//		});
//	}
//	
//	public static void main(String[] args){
//		new AdminProductsGUI();
//	}
	
}
