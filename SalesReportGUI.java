package applicationGUI;

import application.*;

import java.awt.BorderLayout;
import javax.swing.*;

/**
 * This class represents a sales report GUI frame
 * @author group_0549
 *
 */
public class SalesReportGUI extends JFrame {
	private Inventory inv;
	private JPanel title = new JPanel();
	private JPanel product = new JPanel();
	private JPanel quantity = new JPanel();
	private JPanel salesAmount = new JPanel();
	private JPanel total = new JPanel();
	private double grandTotal = 0;
	
	/**
	 * This method initializes a frame for the sales report
	 * @param inv -> inventory 
	 */
	public SalesReportGUI(Inventory inv) {
		this.inv = inv;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(10,10));
		
		createTitle();
		createContent();
		createTotal();
		setSize(500, 500);
	}
	
	/**
	 * This method creates a title for the sales report
	 */
	public void createTitle() {
		title.setLayout(new BoxLayout(title, BoxLayout.PAGE_AXIS));
		title.add(new JLabel("Sales Report for August 2016"));
		title.add(new JLabel("============================="));
		add(title, BorderLayout.PAGE_START);
	}
	
	/**
	 * This method sets the content for the sales report
	 */
	public void createContent() {
		product.setLayout(new BoxLayout(product, BoxLayout.PAGE_AXIS));
		JPanel productTitle = new JPanel();
		productTitle.add(new JLabel("Product ID"));
		productTitle.add(new JLabel("Product Name"));
		product.add(productTitle);
		JPanel productTitleSeparator = new JPanel();
		productTitleSeparator.add(new JLabel("--------------"));
		productTitleSeparator.add(new JLabel("--------------"));
		product.add(productTitleSeparator);
		for (Product p: inv.productManager.getSales().keySet()) {
			JPanel idName = new JPanel();
			idName.add(new JLabel(Integer.toString(p.getID())));
			idName.add(new JLabel(p.getDescription()));
			product.add(idName);
		}
		add(product, BorderLayout.LINE_START);
		
		quantity.setLayout(new BoxLayout(quantity, BoxLayout.PAGE_AXIS));
		quantity.add(new JLabel("Quantity"));
		quantity.add(new JLabel("--------------"));
		for (Integer quant: inv.productManager.getSales().values()) {
			quantity.add(new JLabel(Integer.toString(quant)));
		}
		add(quantity, BorderLayout.CENTER);
		
		int itemIndex = 0;
		salesAmount.setLayout(new BoxLayout(salesAmount, BoxLayout.PAGE_AXIS));
		salesAmount.add(new JLabel("Sales Amount"));
		salesAmount.add(new JLabel("--------------"));
		Object[] prod = inv.productManager.getSales().keySet().toArray();
		for (Integer quant: inv.productManager.getSales().values()) {
			salesAmount.add(new JLabel(Double.toString(quant*((Product)prod[itemIndex]).getPrice())));
			grandTotal += quant*((Product)prod[itemIndex]).getPrice();
		}
		add(salesAmount, BorderLayout.LINE_END);
	}
	
	/**
	 * This method sets teh total layout for the sales report
	 */
	public void createTotal() {
		total.setLayout(new BorderLayout(10,10));
		JPanel totalTitle = new JPanel();
		totalTitle.setLayout(new BoxLayout(totalTitle, BoxLayout.PAGE_AXIS));
		totalTitle.add(new JLabel("--------------"));
		totalTitle.add(new JLabel("TOTAL :"));
		total.add(totalTitle, BorderLayout.LINE_START);
		
		JPanel totalNum = new JPanel();
		totalNum.setLayout(new BoxLayout(totalNum, BoxLayout.PAGE_AXIS));
		totalNum.add(new JLabel("--------------"));
		totalNum.add(new JLabel(Double.toString(grandTotal)));
		total.add(totalTitle, BorderLayout.LINE_END);
		
		add(total, BorderLayout.PAGE_END);
	}
	
}
