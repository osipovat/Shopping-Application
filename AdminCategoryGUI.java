package applicationGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.*;
/**
 * The AdminCategoryGUI class represents the administrator window where he can modify the product category
 * @author group_0549
 *
 */
public class AdminCategoryGUI extends JDialog{
	private JPanel panel = new JPanel();
	private JButton addCategory;
	private JButton seeCategories;
	private ProjectVI p;
	private int sessionID;
	
	/**
	 * Constructs a AdminCategoryGUI instance with the specified project
	 * @param p -> ProjectVI
	 */
	public AdminCategoryGUI(ProjectVI p){
		this.p = p;
		
		this.sessionID = p.getUser().getSessionID();
		setLayout(new BorderLayout());
		panel.setLayout(new GridLayout(3,1));
		setTitle("Product Category Controls");
		setSize(300, 300);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		this.setUp();
		add(panel);
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets the main menu bar where you can maintain product category
	 */
	public void setUp(){
		JLabel category = new JLabel("Maintain Category");
		panel.add(category);
		addCategory = new JButton("Add Category");
		this.addCategory();
		panel.add(addCategory);
		seeCategories = new JButton("Generate screen report of categories");
		this.seeCategory();
		panel.add(seeCategories);
	}
		
	/**
	 * Adds category to the product file and to the inventory and sets the panel in order to do these actions
	 */
	public void addCategory(){
		addCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			String text = JOptionPane.showInputDialog(null,"Description: ");
			p.addCategory(text, sessionID);
			}
		});
	}
	
	/**
	 * Method allows to see all the product categories and sets the panel in order to do these actions
	 */
	public void seeCategory(){
		ArrayList<ProductCategory> categories = p.inventory.getCategoryList();
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
	
}
