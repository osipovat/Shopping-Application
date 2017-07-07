package application;

public class ManagerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Inventory i = new Inventory();
		//ProductManager pm = new ProductManager(i);
		//DistributionManager dm = new DistributionManager(i);
		//pm.readFile();
		System.out.println(i.getProductList().size());
		System.out.println(i.getCategoryList().size());
		i.addProductCategory(new ProductCategory("SHOES"));
		System.out.println(i.getCategoryList().size());
		//System.out.println(pm.getProduct(1));
		//dm.loadFile();
		//System.out.println(dm.getDistributionCentres());
	}

}
