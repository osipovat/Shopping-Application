package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectVI extends Project{
	/** 
	 * As a general purchase procedure:
	 *	1) A shopper logs in
	 *	2) Shopper adds items to the cart
	 *	3) If available, items get added accordingly
	 *	4) Checkout (method in API, or button in GUI) generates a new order (and automatically computes 
	 *     shipping and generates a new invoice).
     *
	 *	Now - a word on inventory maintenance - explained via an example.
     *
	 *	Say shopper s adds product p in his cart; the desired quantity comes from warehouse A and warehouse B because none 
	 *  of them has enough.
	 *	Please observe at this stage the shopper has not checked out yet; the shopper is still logged in to the system. 
	 *	To make it more practical, say warehouse A has 10 pieces, warehouse B has 7 pieces, the shopper has added to cart 15 pieces.
	 *	As long as the shopper is logged in, other shoppers should be able to see only 2 pieces available.
     *
	 *	At this point, we have two cases:
	 *	1) Shopper hits checkout. The system should do the following:
	 *	-reduce the inventory (permanently) to 2 pieces.
	 *	-compute the nearest warehouse to the shopper's city. Let this warehouse be the warehouse in city C.
	 *	-the shipping has to be computed from C.
	 *	-So here what should happen is: the company has to move the merchandise from warehouse A (10 units) and from warehouse B 
	 *   (5 units) [or, if you desire so, 7 units from warehouse B and 8 units from warehouse A] to the warehouse C. 
	 *   This operation is "invisible" for us (that is you can assume the merchandise moves using procedures to be programmed in the future)
     *
	 *	2) Shopper hits logout WITHOUT doing a checkout. Please store the shopping cart and recover the inventory. 
	 *     So to make it clear, the shopping cart contains 15 units from product p, the shopper is logged out, other shoppers 
	 *     see 17 available (in total).
     *
	 *	3) The shopper logs in again. If in the mean time the merchandise sold out (in full or in part) the product in question 
	 *     must be removed from shopping cart (if you are in GUI, the user should get a notification message). If the merchandise is 
	 *     still there go to point 1 above.
	 */


	
	
	/**
	 * This method attempts to add quantity units from the product identified by prodID to the cart.
	 * Returns true if the operation succeeds, false, if then operation fails.
	 * 
	 * @param productID
	 * @param quantity
	 * @param sessionID
	 * @param custID
	 * @return
	 */
	public boolean addToShoppingCart(int productID, int quantity, int sessionID, int custID) {
		// your code goes here
		
		for (Shopper s : super.inventory.getShopperList()) {
			if (s.getSessionID() == sessionID) {	// check for log out
				if (s.getShopperID() == custID) 	{// find out shopper
					Product  p = this.inventory.getProduct(productID);
					try {
						if (p.getQuantity() - p.getReservedQuantity() >= quantity) {	// there is enough quantity to add to a shopping cart
							s.getShoppingCart().add(p, quantity);
							p.setReservedQuantity(quantity);
							return true;			
						}
						
					} catch (OutOfStockException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}
	
	/** 
	 * Returns the shopping cart content of the shopper userName.
	 * 
	 * An item in the shopping cart must look like this sample:
	 * prodID: 111
	 * center: (An arraylist with components) "Toronto","Barrie"
	 * quantity: (An arraylist with components) 2,3
	 * 
	 * The sample means this particular item on the shopping cart is the product with product ID=111,
	 * the total quantity in the cart is 5, this quantity is taken 2 pieces from the warehouse located in Toronto
	 * and 3 pieces from the warehouse located in Barrie.
	 *  
	 * @param userName
	 * @return
	 */
	public List<ShoppingCartItem> getShoppingCart(String userName, Shopper sho) {
		// your code goes here
		int cost;
		String shoppercity;
		ShoppingCartItem item;
		List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>();
		HashMap<Product, Integer> contents;
		String city;
		List<String> cities;
		List<Integer> quantities;
		int demand;	// amount for purchase

		for (Shopper s: this.inventory.getShopperList()) {
			//System.out.println(this.inventory.getShopperList()==null);
			//System.out.println(s.getCustomer()==null);
			if (s.getCustomer() != null) {
				if (s.getCustomer().getFirstName().equals(userName)) {	// find out a shopper
					shoppercity=s.getCustomer().getCity();
					contents = s.getShoppingCart().getCart();
					for (Product p : contents.keySet()) {
						demand = contents.get(p);
						cities = new ArrayList<String>();
						quantities = new ArrayList<Integer>();
						for (DistributionCenter dc : this.inventory.getDistributionCenters()) {
							if (demand > 0) {	// available quantity in dc
								cities.add(dc.getCity());
								quantities.add(dc.getQuantity().get(p));
								demand -= dc.getQuantity().get(p);
								
							}
							else if (demand == 0)
								break;
						}
						item = new ShoppingCartItem(String.valueOf(p.getID()), cities, quantities);
						items.add(item);
					}
				}
			}
		}
		return items;
	}
	

	/**
	 * This method checks out the shopping cart, generates an order and returns order ID.
	 * It is the equivalent of the checkout button in your GUI screen.
	 * @param sessionID
	 * @param custID
	 * @return
	 */
	public int checkout(int custID, int sessionID) {
		List<ShoppingCartItem> cart;
		String distribution = null;
		List<String> route;
		int orderID;
		Product p;
		int shippingCost=0;
		int itemIndex=0;
		for (Shopper s : this.inventory.getShopperList()) { 
			if (s.getSessionID() == sessionID && s.getShopperID() == custID) { // found customer
				//System.out.println(s.getCustomer()==null);
				cart = this.getShoppingCart(s.getCustomer().getFirstName(), s); // found shopping cart
			
				orderID = s.getShoppingCart().checkOut();
				try {
					route=getDeliveryRoute(orderID,sessionID);
					distribution = route.get(route.size()-1);
					shippingCost=super.cost;
					s.getCustomer().getInvoices().get(s.getCustomer().getInvoices().size()-1).setdeliveryCost(shippingCost);					
				} catch (HeapEmptyException | HeapFullException e) {
					e.printStackTrace();
				}
				for (ShoppingCartItem item: cart) {
					p = this.inventory.getProduct(Integer.parseInt(item.getProdID()));
					//this.inventory.productManager.addToSales(p, item.getQuantity().get(itemIndex));
					itemIndex+=1;
					this.inventory.changeQuantity(p, s.getShoppingCart().getCart().get(p), false);
					int backOrderedQuan;
					DistributionCenter shortest = this.inventory.searchDC(distribution);	// closest warehouse to customer
					backOrderedQuan = p.getReservedQuantity()-shortest.getQuantity().get(p); //track how more we need			
					if (backOrderedQuan > 0) {
						s.getShoppingCart().getCart().replace(p, 0); 	// shopping cart quantity set 0
						this.inventory.changeQuantityDC(p, 0, this.inventory.searchDC(distribution));
						for(String c : item.center){	
							DistributionCenter d1 = this.inventory.searchDC(c);
							if (!d1.getCity().equals(shortest.getCity())) {
								if (d1.getQuantity().get(p) >= backOrderedQuan) {
									//d1.getQuantity().replace(p, d1.getQuantity().get(p)-backOrderedQuan);
									this.inventory.changeQuantityDC(p, d1.getQuantity().get(p)-backOrderedQuan, d1);
									backOrderedQuan =0;
									break;
									
								} else {
									backOrderedQuan -= d1.getQuantity().get(p);
									//d1.getQuantity().replace(p, 0);	
									this.inventory.changeQuantityDC(p, 0, d1);
								}
							}
														
						}															
					}
					else {
						s.getShoppingCart().getCart().replace(p, 0);
						this.inventory.changeQuantityDC(p, shortest.getQuantity().get(p)-p.getReservedQuantity(), shortest);
					}
				}
				return orderID;
			}
		}
		return 0;
	}
	
	
	public class ShoppingCartItem {
		private String prodID;
		private List<String> center;
		private List<Integer> quantity;
		
		public ShoppingCartItem(String prodID, List<String> center, List<Integer> quantity) {
			this.prodID = prodID;
			this.center = center;
			this.quantity = quantity;
		}

		public String getProdID() {
			return prodID;
		}

		public void setProdID(String prodID) {
			this.prodID = prodID;
		}

		public List<String> getCenter() {
			return center;
		}

		public void setCenter(List<String> center) {
			this.center = center;
		}

		public List<Integer> getQuantity() {
			return quantity;
		}

		public void setQuantity(List<Integer> quantity) {
			this.quantity = quantity;
		}
		
		public String toString() {
			return prodID + " " + center + " " + quantity +"";
		}
		
		
	}

}