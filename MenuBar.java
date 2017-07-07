package applicationGUI;

import application.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import application.Inventory;

/**
 * The MenuBar class represents the panel with the menu bar
 * @author group_0549
 *
 */
public class MenuBar extends JPanel {
	private JButton p = new JButton("Maintain Products");
	private JButton pc = new JButton("Maintain Product Categories");
	private JButton dc = new JButton("Maintain Distribution Centers");
	private JButton sr = new JButton("Sales Report");
	private JLabel welcome = new JLabel("Welcome ");
	private AdminProductsGUI adminp;
	private AdminCategoryGUI adminc;
	private AdminCenterGUI admind;
	private SalesReportGUI salesr;
	private ProjectVI proj;
	private MainGUI main;
	
	/**
	 * Constructs a MenuBar instance with the specified project and main frame
	 * @param p -> ProjectVI
	 * @param frame -> main gui class
	 */
	public MenuBar(ProjectVI p, MainGUI frame){
		super(new BorderLayout());
		this.proj = p;
		this.main = frame;
		JToolBar toolBar = new JToolBar("Administrator Controls");
		toolBar.add(welcome);
		addButtons(toolBar);

		setPreferredSize(new Dimension(450, 130));
		add(toolBar, BorderLayout.PAGE_START);
		setVisible(false);
	}

	/**
	 * Adds the buttons to the tool bar
	 * @param tbar -> the main tool bar
	 */
	public void addButtons(JToolBar tbar){
		tbar.add(p);
		products();
		tbar.add(pc);
		productCategories();
		tbar.add(dc);
		distributionCenters();
		tbar.add(sr);
		salesReport();
	}
	
	/**
	 * Allows to access maintain the products
	 */
	public void products(){
		p.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				adminp = new AdminProductsGUI(proj);
				adminp.setVisible(true);
			}
		});
	}
	
	/**
	 * Allows to access the  maintain product categories
	 */
	public void productCategories(){
		pc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				adminc = new AdminCategoryGUI(proj);
				adminc.setVisible(true);
			}
		});
	}
	
	/**
	 * Allows to access maintain distribution center 
	 */
	public void distributionCenters(){
		dc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				admind = new AdminCenterGUI(proj);
				admind.setVisible(true);
			}
		});
	}
	
	/**
	 * Allows to access the sales report
	 */
	public void salesReport(){
		sr.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				salesr = new SalesReportGUI(main.p.inventory);
				salesr.setVisible(true);
			}
		});
	}
	
	/**
	 * Changes the welcome, user statement
	 */
	public void changeWelcome(){
		this.welcome.setText("Welcome, " + /*main.p.getUser().getUsername() + */"  ");
	}
}
