package applicationGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import application.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.synth.SynthSeparatorUI;

/**
 * The MainGUI class is the main window of the application, representing our store.
 * @author group_0549
 *
 */
public class MainGUI extends JFrame implements ActionListener {
	//private CustomerRegister cr = new CustomerRegister(this, "Login");
	public int sessionID = -1;
	public String username = "";
	private JMenuBar menubar;
	private JMenuItem signUpMenu;
	public JMenuItem loginMenu; 
	private JButton home = new JButton("Home");
    private JMenu category = new JMenu("Category");
	private JButton searchButton = new JButton("GO!");
	private JTextField search = new JTextField(50);
	private JPanel productDisplay = new JPanel();
	private List<JLabel> productDisp = new ArrayList<JLabel>();
	private JPanel refine = new JPanel();
	JTextField minPrice = new JTextField(5);
	JTextField maxPrice = new JTextField(5);
	public ProjectVI p = new ProjectVI();
	private ProductManager pm = p.inventory.productManager;
	private UserManager um = p.inventory.shopperManager;
	public LoginGUI lg = new LoginGUI(p, this);
	public MenuBar admin = new MenuBar(p, this);
	public ShopperMenuBar shopper = new ShopperMenuBar(p, this);
	private Integer[] quantities = {1,2,3,4,5,6,7,8,9,10};
	private HashMap<String, JButton> itemToAdd = new HashMap<String, JButton>();
	private HashMap<String, JComboBox<Integer>> quantToAdd = new HashMap<String, JComboBox<Integer>>();

	/**
	 * Constructs a MainGUI instance with a login option and a view of all available products
	 */
	public MainGUI(){
		search.addActionListener(this);
		searchButton.addActionListener(this);
		home.setFont(category.getFont());
		home.setOpaque(false);
		home.setContentAreaFilled(false);
		home.setBorderPainted(false);
		home.addActionListener(this);
		
		//pm.loadFile();
		File dir = new File("src/DataFiles");
		File[] listing = dir.listFiles();
		if (listing!=null) {
			for (File userFile: listing) {
				if (!userFile.getName().equals("DistributionCentres.csv")&&!userFile.getName().equals("City.csv")&&!userFile.getName().equals("Products.csv")&&!userFile.getName().equals("Shoppers.csv")&&!userFile.getName().replaceFirst("[.][^.]+$", "").equals("")) um.loadFile(userFile.getName().replaceFirst("[.][^.]+$", ""));
			}
		}
		setLayout(new BorderLayout(50,50));
		
		this.createMenu();
		setTitle("The Final Project Store"); //sets name of application
		
		//set Frame to size of computer screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		
		createRefiner();
		setProducts();
//		add(admin, BorderLayout.NORTH); //add admin toolbar
//		add(shopper, BorderLayout.NORTH);
	}
	
	/**
	 * Creates the menu bar for MainGUI, which consists of a home button, product category menu, search bar, sign up
	 * button, and login button.
	 */
	public void createMenu(){
		menubar = new JMenuBar();
		
        signUpMenu = new JMenuItem("Sign Up");     
        this.signUp();
        loginMenu = new JMenuItem("Log In");
        loginMenu.setName("Log In");
        this.login();
        loginMenu.setHorizontalAlignment(SwingConstants.RIGHT);
        search.setMaximumSize(search.getPreferredSize());
        menubar.add(home);
        menubar.add(category);
        for (ProductCategory pc: pm.getCategoryList()) {
        	JMenuItem cat = new JMenuItem(pc.getDescription());
        	cat.addActionListener(this);
        	cat.setActionCommand("cat");
        	cat.setName(pc.getDescription());
        	category.add(cat);
        }
        menubar.add(Box.createGlue());
        JLabel searchLabel = new JLabel("Search : ");
		menubar.add(searchLabel);
        menubar.add(search);
        menubar.add(searchButton);
        menubar.add(Box.createHorizontalStrut(20));
        menubar.add(signUpMenu);
        menubar.add(loginMenu);
        
        setJMenuBar(menubar);    
	}
	
	/**
	 * Creates refiner for the MainGUI (refine price range)
	 */
	public void createRefiner() {
		JLabel byPrice = new JLabel("Refine by price :");
		refine.add(byPrice);
		JPanel priceInput = new JPanel();
		JButton refinePrice = new JButton(">");
		refinePrice.setPreferredSize(new Dimension(20,20));
		refinePrice.addActionListener(this);
		refinePrice.setActionCommand("range");
		priceInput.add(minPrice);
		priceInput.add(new JLabel("to"));
		priceInput.add(maxPrice);
		priceInput.add(refinePrice);
		refine.add(priceInput);
		
		add(refine, BorderLayout.WEST);
	}

	/**
	 * Opens dialog to sign up
	 */
	public void signUp(){
		signUpMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ShopperRegistration sr = new ShopperRegistration(p);
				sr.setVisible(true);
			}
		});
	}
	
	/**
	 * Opens dialog to login
	 */
	public void login(){
		loginMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(loginMenu.getText() == "Log In"){
					lg.setVisible(true);
				}else{
					//logs out user
					p.logout(sessionID);
					loginMenu.setText("Log In");
					admin.setVisible(false);
					shopper.setVisible(false);
					sessionID = -1;
					setProducts();
				}
			}
		});
	}
	

	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	/**
	 * Refreshes the products on the screen according to who is logged in
	 */
	public void setProducts(){
		remove(productDisplay);
		productDisplay.removeAll();
		productDisp.clear();
		itemToAdd.clear();
		quantToAdd.clear();
		JPanel prod;
		JLabel info;
		JButton addToCart;
		JComboBox<Integer> quant;
		if (goodSearch(search.getText())) {
			String input = search.getText();
			if (!input.trim().replaceAll("[^\\d]", "").equals("")) {
				for (int i=1; i<= pm.getProductList().size(); i++) {
					if (Integer.parseInt(input.trim().replaceAll("[^\\d]", "")) == i) {
						if (pm.getProductList().get(i-1).getQuantity()-pm.getProductList().get(i-1).getReservedQuantity()>0) {
							ImageIcon prodImage = new ImageIcon("src/Products/"+pm.getProductList().get(i-1).getDescription()+".jpg");
							prod = new JPanel();
							prod.setLayout(new BoxLayout(prod, BoxLayout.PAGE_AXIS));
							info = new JLabel();
							info.setIcon(new ImageIcon(getScaledImage(prodImage.getImage(), 160, 160)));
							info.setHorizontalTextPosition(JLabel.CENTER);
							if (pm.getProductList().get(i-1).getQuantity()-pm.getProductList().get(i-1).getReservedQuantity() <= 3) {
								info.setText("<html><div style='text-align: center;'>"+pm.getProductList().get(i-1).getDescription()+"<br>"+NumberFormat.getCurrencyInstance().format(pm.getProductList().get(i-1).getPrice())+"<br><font color='red'><i>ONLY "+pm.getProductList().get(i-1).getQuantity()+" LEFT!</i></font></html>");
							}
							else {
								info.setText("<html><div style='text-align: center;'>"+pm.getProductList().get(i-1).getDescription()+"<br>"+NumberFormat.getCurrencyInstance().format(pm.getProductList().get(i-1).getPrice())+"</html>");
							}
							info.setVerticalTextPosition(JLabel.BOTTOM);
							prod.add(info);
							if (sessionID % 2 == 1) {
								quant = new JComboBox<Integer>(quantities);
								quantToAdd.put(pm.getProductList().get(i-1).getDescription(), quant);
								addToCart = new JButton("Add to cart");
								addToCart.setName(pm.getProductList().get(i-1).getDescription());
								addToCart.addActionListener(this);
								itemToAdd.put(pm.getProductList().get(i-1).getDescription(), addToCart);
								prod.add(quant);
								prod.add(addToCart);
							}
							info.setToolTipText("Login to buy products!");
							prod.setName(Double.toString(pm.getProductList().get(i-1).getPrice()));
							productDisplay.add(prod);
							break;
						}
					}
				}
			}
			else {
				for (Product pr: pm.getProductList()) {
					if (pr.getDescription().toLowerCase().trim().replaceAll("[^a-zA-Z]", "").contains(input.toLowerCase().trim().replaceAll("[^a-zA-Z]", ""))) {
						if (pr.getQuantity()-pr.getReservedQuantity()>0) {
							ImageIcon prodImage = new ImageIcon("src/Products/"+pr.getDescription()+".jpg");
							prod = new JPanel();
							prod.setLayout(new BoxLayout(prod, BoxLayout.PAGE_AXIS));
							info = new JLabel();
							info.setIcon(new ImageIcon(getScaledImage(prodImage.getImage(), 160, 160)));
							info.setHorizontalTextPosition(JLabel.CENTER);
							if (pr.getQuantity()-pr.getReservedQuantity() <= 3) {
								info.setText("<html><div style='text-align: center;'>"+pr.getDescription()+"<br>"+NumberFormat.getCurrencyInstance().format(pr.getPrice())+"<br><font color='red'><i>ONLY "+(pr.getQuantity()-pr.getReservedQuantity())+" LEFT!</i></font></html>");
							}
							else {
								info.setText("<html><div style='text-align: center;'>"+pr.getDescription()+"<br>"+NumberFormat.getCurrencyInstance().format(pr.getPrice())+"</html>");
							}
							info.setVerticalTextPosition(JLabel.BOTTOM);
							prod.add(info);
							if (sessionID % 2 == 1) {
								quant = new JComboBox<Integer>(quantities);
								quantToAdd.put(pr.getDescription(), quant);
								addToCart = new JButton("Add to cart");
								addToCart.setName(pr.getDescription());
								addToCart.addActionListener(this);
								itemToAdd.put(pr.getDescription(), addToCart);
								prod.add(quant);
								prod.add(addToCart);
							}
							info.setToolTipText("Login to buy products!");
							prod.setName(Double.toString(pr.getPrice()));
							productDisplay.add(prod);
						}
					}
				}
				outer: for (ProductCategory pc: pm.getCategoryList()) {
					if (pc.getDescription().toLowerCase().trim().replaceAll("[^a-zA-Z]", "").contains(input.toLowerCase().trim().replaceAll("[^a-zA-Z]", ""))) {
						for (Product pr: pm.getCategoryProductList().get(pc)) {
							if (pr.getQuantity()-pr.getReservedQuantity()>0) {
								ImageIcon prodImage = new ImageIcon("src/Products/"+pr.getDescription()+".jpg");
								prod = new JPanel();
								prod.setLayout(new BoxLayout(prod, BoxLayout.PAGE_AXIS));
								info = new JLabel();
								info.setIcon(new ImageIcon(getScaledImage(prodImage.getImage(), 160, 160)));
								info.setHorizontalTextPosition(JLabel.CENTER);
								if (pr.getQuantity()-pr.getReservedQuantity() <= 3) {
									info.setText("<html><div style='text-align: center;'>"+pr.getDescription()+"<br>"+NumberFormat.getCurrencyInstance().format(pr.getPrice())+"<br><font color='red'><i>ONLY "+(pr.getQuantity()-pr.getReservedQuantity())+" LEFT!</i></font></html>");
								}
								else {
									info.setText("<html><div style='text-align: center;'>"+pr.getDescription()+"<br>"+NumberFormat.getCurrencyInstance().format(pr.getPrice())+"</html>");
								}
								info.setVerticalTextPosition(JLabel.BOTTOM);
								prod.add(info);
								if (sessionID % 2 == 1) {
									quant = new JComboBox<Integer>(quantities);
									quantToAdd.put(pr.getDescription(), quant);
									addToCart = new JButton("Add to cart");
									addToCart.setName(pr.getDescription());
									addToCart.addActionListener(this);
									itemToAdd.put(pr.getDescription(), addToCart);
									prod.add(quant);
									prod.add(addToCart);
								}
								info.setToolTipText("Login to buy products!");
								prod.setName(Double.toString(pr.getPrice()));
								productDisplay.add(prod);
							}
						}
						break outer;
					}
				}
			}
		}
		else if (search.getText().equals("") || search.getText().equals(null)) {
			for (Product pr: pm.getProductList()) {
				if (pr.getQuantity()-pr.getReservedQuantity()>0) {
					ImageIcon prodImage = new ImageIcon("src/Products/"+pr.getDescription()+".jpg");
					prod = new JPanel();
					prod.setLayout(new BoxLayout(prod, BoxLayout.PAGE_AXIS));
					info = new JLabel();
					info.setIcon(new ImageIcon(getScaledImage(prodImage.getImage(), 160, 160)));
					info.setHorizontalTextPosition(JLabel.CENTER);
					if (pr.getQuantity()-pr.getReservedQuantity() <= 3) info.setText("<html><div style='text-align: center;'>"+pr.getDescription()+"<br>"+NumberFormat.getCurrencyInstance().format(pr.getPrice())+"<br><font color='red'><i>ONLY "+(pr.getQuantity()-pr.getReservedQuantity())+" LEFT!</i></font></html>");
					else info.setText("<html><div style='text-align: center;'>"+pr.getDescription()+"<br>"+NumberFormat.getCurrencyInstance().format(pr.getPrice())+"</html>");
					info.setVerticalTextPosition(JLabel.BOTTOM);
					prod.add(info);
					if (sessionID % 2 == 1) {
						quant = new JComboBox<Integer>(quantities);
						quantToAdd.put(pr.getDescription(), quant);
						addToCart = new JButton("Add to cart");
						addToCart.setName(pr.getDescription());
						addToCart.addActionListener(this);
						itemToAdd.put(pr.getDescription(), addToCart);
						prod.add(quant);
						prod.add(addToCart);
					}
					info.setToolTipText("Login to buy products!");
					prod.setName(Double.toString(pr.getPrice()));
					productDisplay.add(prod);
				}
			}
		}
		else {
			prod = new JPanel();
			info = new JLabel();
			info.setText("<html><div style='text-align: center;'><font color='red'><i>NO RESULTS! TRY A NEW SEARCH!</i></font></html>");
			prod.add(info);
			productDisplay.add(prod);
		}
		add(productDisplay);
		revalidate();
		repaint();
	}
	
	private boolean goodSearch(String input) {
		if (!input.trim().replaceAll("[^\\d]", "").equals("")) {
			for (int i=1; i<= pm.getProductList().size(); i++) {
				if (Integer.parseInt(input.trim().replaceAll("[^\\d]", "")) == i) return true;
			}
		}
		else if (!input.toLowerCase().trim().replaceAll("[^a-zA-Z]", "").equals("")) {
			for (Product p: pm.getProductList()) {
				if (p.getDescription().toLowerCase().trim().replaceAll("[^a-zA-Z]", "").contains(input.toLowerCase().trim().replaceAll("[^a-zA-Z]", ""))) return true;
			}
			for (ProductCategory pc: pm.getCategoryList()) {
				if (pc.getDescription().toLowerCase().trim().replaceAll("[^a-zA-Z]", "").contains(input.toLowerCase().trim().replaceAll("[^a-zA-Z]", ""))) return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets the products by price range if specified in the refiner
	 */
	public void setProductRange() {
		setProducts();
		if (minPrice.getText().trim().equals("") && !maxPrice.getText().trim().equals("") && maxPrice.getText().trim().matches("[0-9.]*")) {
			String max = maxPrice.getText().trim();
			for (Component prod: productDisplay.getComponents()) {
				if (Double.parseDouble(prod.getName()) > Double.parseDouble(max)) {
					prod.setVisible(false);
				}
			}
		}
		else if (!minPrice.getText().trim().equals("") && maxPrice.getText().trim().equals("") && minPrice.getText().trim().matches("[0-9.]*")) {
			String min = minPrice.getText().trim();
			for (Component prod: productDisplay.getComponents()) {
				if (Double.parseDouble(prod.getName()) < Double.parseDouble(min)) {
					prod.setVisible(false);
				}
			}
		}
		else if (!minPrice.getText().trim().equals("") && !maxPrice.getText().trim().equals("") && minPrice.getText().trim().matches("[0-9.]*") && maxPrice.getText().trim().matches("[0-9.]*")) {
			String min = minPrice.getText().trim();
			String max = maxPrice.getText().trim();
			for (Component prod: productDisplay.getComponents()) {
				if (Double.parseDouble(prod.getName()) < Double.parseDouble(min) || Double.parseDouble(prod.getName()) > Double.parseDouble(max)) {
					prod.setVisible(false);
				}
			}
		}
		revalidate();
		repaint();
	}
	
	/**
	 * Adds toolbar according to who is logged in
	 * @param b -> true if admin, false if shopper
	 */
	public void addToolBar(boolean b){
		if(b){
			add(admin, BorderLayout.NORTH);//add admin toolbar
		}else{
			add(shopper, BorderLayout.NORTH);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		if (command.equals("GO!")) setProducts();
		else if (command.equals("Home")) {
			search.setText("");
			setProducts();
		}
		else if (command.equals("cat")) {
			search.setText(((Component)e.getSource()).getName());
			setProducts();
		}
		else if (command.equals("range")) setProductRange();
		else if (command.equals("Add to cart")) {
			Product pro = null;
			for (Product product: p.inventory.getProductList()) {
				if (product.getDescription().equals(((Component)e.getSource()).getName())) pro = product;
			}
			Integer quantity = quantities[quantToAdd.get(((Component)e.getSource()).getName()).getSelectedIndex()];
			if (pro!=null) {
				try {
					((Shopper)p.getUser()).addToCart(pro, quantity);
					JOptionPane.showMessageDialog(null,"Successfully added!");
				} catch (OutOfStockException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,"You are ordering "+(quantity-pro.getQuantity()-pro.getReservedQuantity())+" too many! Please lower the quantity.");
				}
			}
		}
		else setProducts();
	}
	
	public static void main(String[] args){
		MainGUI main = new MainGUI();
		main.setVisible(true);
		main.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
}