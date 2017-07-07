package applicationGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
 * The AdminCenterGUI class represents the administrator window where he can modify the distribution centers
 * @author group_0549
 *
 */
public class AdminCenterGUI extends JDialog{
	private JPanel panel = new JPanel();
	private JButton addCenter;
	private JButton seeCenters;
	private JButton addCity;
	private JButton addRoute;
	private ProjectVI p;
	private int sessionID;
	
	/**
	 * Constructs a AdminCenterGUI instance with the specified project
	 * @param p -> ProjectVI
	 */
	public AdminCenterGUI(ProjectVI p){
		this.p = p;
		this.sessionID = p.getUser().getSessionID();
		setLayout(new BorderLayout());
		panel.setLayout(new GridLayout(3,1));
		setTitle("Distribution Center Controls");
		setSize(300, 300);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		this.setUp();
		add(panel);
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets the menu bar where you can maintain distribution center
	 */
	public void setUp(){
		JLabel center1 = new JLabel("Maintain Distribution Center");
		panel.add(center1);
		addCenter = new JButton("Add Distribution Center");
		panel.add(addCenter);
		this.addDC();
		addCity = new JButton("Add City");
		panel.add(addCity);
		this.addCity();
		addRoute = new JButton("Add Delivery Route");
		panel.add(addRoute);
		this.addRoute();
		seeCenters = new JButton("Generate screen report of distribution centers");
		panel.add(seeCenters);
		this.seeCenter();
	}
		
	/**
	 * Adds distribution center to the file and to the inventory and sets the panel in order to do these actions
	 */
	public void addDC(){
		addCenter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			String text = JOptionPane.showInputDialog(null,"City: ");
			p.addDistributionCenter(text, sessionID);
			}
		});
	}
	
	/**
	 * Allows the administrator to see all the distribution centers and sets the panel in order to do these actions
	 */
	public void seeCenter(){
		ArrayList<DistributionCenter> centers = p.inventory.distributionManager.getDistributionCentres();
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
	
	/**
	 * Adds the city to the file and to the inventory and sets the panel in order to do these actions
	 */
	public void addCity(){
		addCity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
		
			String texty = JOptionPane.showInputDialog(null,"City: ");
			((Administrator)p.getUser()).AddCity(texty);
			p.del.setCities(((Administrator)p.getUser()).getCity());
			}
		});
	}
	
	/**
	 * Adds the route to the inventory and sets the panel in order to do these actions
	 */
	public void addRoute(){
		addRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			
			JPanel add = new JPanel();
			add.setLayout(new BoxLayout(add, BoxLayout.PAGE_AXIS));
			ArrayList<JTextField> texts = new ArrayList<JTextField>();
			ArrayList<String> info = new ArrayList<String>();
			String[] labels = {"CityA: ", "CityB: ", "Distance: "};
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
			p.addRoute(info.get(0), info.get(1), Integer.parseInt(info.get(2)), sessionID);
			}
		});
	}
	
}
