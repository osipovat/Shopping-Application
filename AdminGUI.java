package applicationGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import application.*;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
//need to change from frame to panel
/**
 * The AdminGUI class represents the administrator window
 * @author group_0549
 *
 */
public class AdminGUI extends JFrame{
	private JLabel label;
	private JTextField text;
	private JPanel panel = new JPanel();
	private JButton jaddProduct;
	private JPanel center = new JPanel();
	private JPanel east = new JPanel();
	private JButton addCategory;
	private JButton addCenter;
	private JButton seeCategories;
	private Inventory inventory;
	private JButton seeCenters;
	private Project project;
	
	//private Administrator admin;
	/**
	 * Constructs a AdminGUI instance with the specified inventory
	 * @param inentory -> inventory
	 */
	public AdminGUI(Inventory inventory){ //Administrator admin){
		this.inventory = inventory;
		//this.admin = admin;
		this.inventory.setDistributionManager(this.inventory);
		setLayout(new BorderLayout());
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		label = new JLabel("Administrator Controls");
		add(label, BorderLayout.NORTH);
		
		JLabel products = new JLabel("Maintain Products");
		jaddProduct = new JButton("Add Product");
		JLabel change = new JLabel("Change product's");
		panel.add(products);
		panel.add(jaddProduct);
		panel.add(change);
	
		String[] labels = {"ID: ", "Image's URL: ", "Price: ", "Description: ", "Availability: "};
		for (int i = 0, n = labels.length; i < n; i++) {
			JPanel panel2 = new JPanel(new FlowLayout());
			JLabel label2 = new JLabel(labels[i]);
			JTextField text2 = new JTextField(10);
			panel2.add(label2);
			panel2.add(text2);
			panel.add(panel2);  
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
		
		JButton jApply = new JButton("Apply");
		panel.add(jApply);
		add(panel, BorderLayout.WEST);
		
		center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
		JLabel category = new JLabel("Maintain Category");
		center.add(category);
		addCategory = new JButton("Add Category");
		center.add(addCategory);
		seeCategories = new JButton("Generate screen report of categories");
		center.add(seeCategories);
		add(center, BorderLayout.CENTER);
		
		east.setLayout(new BoxLayout(east, BoxLayout.PAGE_AXIS));
		JLabel center1 = new JLabel("Maintain Distribution Center");
		east.add(center1);
		addCenter = new JButton("Add Distribution Center");
		east.add(addCenter);
		seeCenters = new JButton("Generate screen report of distribution centers");
		east.add(seeCenters);
		add(east, BorderLayout.EAST);
		
		//set Frame to size of computer screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addProduct();
		addCategory();
		addDC();
		seeCategory();
		seeCenter();
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
			String[] labels = {"Category: ", "Price: ", "Description: ", "Total Availability: "};
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
			}
		});
	}
	
	/**
	 * Adds product category to the file and to the inventory and sets the panel in order to do these actions
	 */
	public void addCategory(){
		addCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			String text = JOptionPane.showInputDialog(null,"Description: ");
			ProductCategory pc = new ProductCategory(text);
			inventory.addProductCategory(pc);
			//project.addCategory(text, admin.getSessionID());
			System.out.println(inventory.getCategoryProductList());
			//dispose();
			
			// username.setText("");
			// password.setText("");
			}
		});
	}
	
	/**
	 * Adds distribution center to the file and to the inventory and sets the panel in order to do these actions
	 */
	public void addDC(){
		addCenter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			String text = JOptionPane.showInputDialog(null,"City: ");
			DistributionCenter dc = new DistributionCenter(text, inventory);
		
			inventory.distributionManager.addDistributionCenter(dc, text);
			//project.addDistributionCenter(text, admin.getSessionID());
			System.out.println(inventory.distributionManager.getDistributionCentres());
			//dispose();
			
			// username.setText("");
			// password.setText("");
			}
		});
	}

	/**
	 * Allows the administrator to see all categories and sets the panel in order to do these actions
	 */
	public void seeCategory(){
		ArrayList<ProductCategory> categories = this.inventory.getCategoryList();
		seeCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			String string = "";
			for (int i = 0; i < categories.size(); i++){
				string += categories.get(i).getDescription() + ", ";
			}
			JOptionPane.showMessageDialog(null,string);
			}
		});
	}
	
	/**
	 * Allows administrator to see all the distribution centers and sets the panel in order to do these actions
	 */
	public void seeCenter(){
		ArrayList<DistributionCenter> centers = this.inventory.distributionManager.getDistributionCentres();
		System.out.println(centers);
		seeCenters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			String string = "";
			for (int i = 0; i < centers.size(); i++){
				string += centers.get(i).getCity() + ", ";
			}
			JOptionPane.showMessageDialog(null,string);
			}
		});
	}
	
	public static void main(String[] args){
		Inventory inv = new Inventory();
//		ArrayList<ProductCategory> categ = new ArrayList<ProductCategory>();
//		categ.add(new ProductCategory("TSHIRT"));
//		categ.add(new ProductCategory("JEANS"));
//		categ.add(new ProductCategory("SKIRT"));
//		inv.setCategoryList(categ);
		new AdminGUI(inv);
	}
}
