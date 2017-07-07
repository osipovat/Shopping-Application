package application;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		Project p = new Project();
//		boolean result = p.addUser("Ilir","pass",true);// the result must be true
//		System.out.println(result);
//		boolean results = p.addUser("Ilir","pass",true);// the result must be false; user "Ilir" exists already
//		System.out.println(results);
//		int mySession = p.login("Ilir","pass"); // expected result is a positive integer (session id)
//		System.out.println(mySession);
//		int catID = p.addCategory("TSHIRT",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
//		System.out.println(catID);
//		int someID = p.addCategory("TSHIRT", mySession); // expected result is -1; "TSHIRT" exists
//		System.out.println(someID);
//		boolean resut = p.addUser("Johnny","pass",false);// expected result true
//		System.out.println(resut);
//		int johnnySession = p.login("Johnny","pass");//expect a session ID, positive integer, not equal to mySesssion
//		System.out.println(johnnySession);
//		int somesID = p.addCategory("TSHIRT",johnnySession);// expect -1 because Johnny is not administrator
//		System.out.println(somesID);
//		p.logout(johnnySession);// now user Johhny cannot do anything else because he logged out. All Johhny data must be saved in the files.
//		p.logout(mySession);// user Ilir is also out
//		int code = p.addCategory("TSHIRT",mySession);//must fail (return -1) because user Ilir logged out
//		System.out.println(code);
		Inventory inventory = new Inventory();
		inventory.setDistributionManager(inventory);
//		//Project project = new Project();
//		UserManager um = new UserManager(inventory);
//		Shopper shopper = new Shopper("Johnny", "pass");
//		Customer customer = new Customer(shopper.getUsername(), shopper.getPassword(), "John", "Toronto", "Spadina");
//		shopper.setCustomer(customer);
//		//System.out.println(customer.getFirstName());
//		Product product = new Product("Tshirt with elephant", new ProductCategory("TSHIRT"), 15, 100);
//		try {
//			customer.getShoppingCart().add(product, 5);
//		} catch (OutOfStockException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//um.saveFile(shopper);
//		//project.addUser(userID, password, admin)
//		//System.out.println(customer.getShoppingCart().getCart());
//	
//		ShippingInvoice invoice = new ShippingInvoice(customer.getShoppingCart());
//		System.out.println(invoice.getProductList());
//		
//		um.saveCart(shopper);
//		um.addInvoice(shopper, invoice);
//		um.addInvoice(shopper, invoice);
//		
		System.out.println(inventory.getProduct(1));
		System.out.println(inventory.distributionManager.getDistributionCentres());
		
	}

}
