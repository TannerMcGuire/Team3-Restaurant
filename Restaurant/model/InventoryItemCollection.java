package model;

//system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

//project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the AccountCollection for the ATM application */
//==============================================================
public class InventoryItemCollection extends EntityBase implements IView {
	private static final String myTableName = "InventoryItem";

	private static final InventoryItem InventoryItem = null;

	private Vector<InventoryItem> InventoryItemList;
//	private InventoryItemType myIIT;
//	public static String v;
//	private Vendor selectedVendor;
//	private InventoryManager manager;

	private InventoryManager manager;

	// GUI Components

	// constructor for this class
	// ----------------------------------------------------------
	public InventoryItemCollection() {
		super(myTableName);
		InventoryItemList = new Vector<InventoryItem>();
	}

	// ----------------------------------------------------------------------------------
	private void addInventoryItem(InventoryItem a) {
		// patronlist.add(a);
		int index = findIndexToAdd(a);
		InventoryItemList.insertElementAt(a, index); // To build up a collection sorted on some key
	}

	// ----------------------------------------------------------------------------------
	public void findInventoryItem(Properties prop) throws Exception {
		String name = prop.getProperty("name");
		String barcode = prop.getProperty("Barcode");
		String query;
		if (name != null) {
			query = "SELECT * FROM " + myTableName + " WHERE (InventoryItemTypeName LIKE '%" + name + "%')";
		} else {
		query = "SELECT * FROM " + myTableName + " WHERE (Barcode.equals(barcode)";
		}
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextInventoryItemData = (Properties) allDataRetrieved.elementAt(cnt);
				InventoryItem InventoryItem = new InventoryItem(nextInventoryItemData);

				if (InventoryItem != null)
					addInventoryItem(InventoryItem);
			}
		else {
			throw new InvalidPrimaryKeyException("No Inventory Items Matching: " + barcode );
		}
	}

	// ----------------------------------------------------------------------------------

	public void findAvailiableInventoryItem(Properties prop) throws Exception {
		String name = prop.getProperty("name");
		String barcode = prop.getProperty("Barcode");
		String query;
		if (name != null) {
			query = "SELECT * FROM " + myTableName + " WHERE InventoryItemTypeName LIKE '%" + name + "%' AND Status ='Available'";
		} else {
		query = "SELECT * FROM " + myTableName + " WHERE (Barcode.equals(barcode) AND Status ='Available'";
		}
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextInventoryItemData = (Properties) allDataRetrieved.elementAt(cnt);
				InventoryItem InventoryItem = new InventoryItem(nextInventoryItemData);

				if (InventoryItem != null)
					addInventoryItem(InventoryItem);
			}
		else {

			throw new InvalidPrimaryKeyException("No Inventory Items matching: " + barcode);
		}
	}
	

	public void findAllII() throws Exception {
		String query;
		query = "SELECT * FROM " + myTableName;

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextInventoryItemData = (Properties) allDataRetrieved.elementAt(cnt);
				InventoryItem InventoryItem = new InventoryItem(nextInventoryItemData);

				if (InventoryItem != null)
					addInventoryItem(InventoryItem);
			}
		else {

			throw new InvalidPrimaryKeyException("No InventoryItems found");

		}
	}
	
	public void findWithName(String name) throws Exception {
		String query = "";
		if (name != null) {
			query = "SELECT * FROM " + myTableName + " WHERE (InventoryItemTypeName LIKE '%" + name + "%' AND Status ='Available')";
		}
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextInventoryItemData = (Properties) allDataRetrieved.elementAt(cnt);
				InventoryItem InventoryItem = new InventoryItem(nextInventoryItemData);

				if (InventoryItem != null)
					addInventoryItem(InventoryItem);
			}
		else {

			throw new InvalidPrimaryKeyException("No available Inventory Items matching: " + name);
		}
	}


	// ----------------------------------------------------------------------------------

	@SuppressWarnings("static-access")
	private int findIndexToAdd(InventoryItem a) {
		// users.add(u);
		int low = 0;
		int high = InventoryItemList.size() - 1;
		int middle;

		while (low <= high) {
			middle = (low + high) / 2;

			InventoryItem midSession = InventoryItemList.elementAt(middle);

			int result = InventoryItem.compare(a, midSession);

			if (result == 0) {
				return middle;
			} else if (result < 0) {
				high = middle - 1;
			} else {
				low = middle + 1;
			}

		}
		return low;
	}

	/**
	 *
	 */
	// ----------------------------------------------------------
	public Object getState(String key) {
		if (key.equals("InventoryItems"))
			return InventoryItemList;
		else if (key.equals("nventoryItemList"))
			return this;
		else if (key.equals("his"))
			return manager.getState("his");
		return null;
	}

	// ----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		if (key.equals("InventoryItemTypeView") == true) {
			String v = (String) value;
			createAndShowIITView();
		} else if (key.equals("IITInfo") == true) {
			if (value != null) {
				boolean flag = inventoryItemFolder((Properties) value);
				if (flag == true) {
					try {
						searchIIT((Properties) value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}/* else if (key.equals("ModifyVendorView") == true) {
			String vendorID = (String) value;
			selectedVendor = retrieve(vendorID);
			createAndShowModifyView();
		} else if (key.equals("ProcessInvoiceView") == true) {
			String vendorID = (String) value;
			selectedVendor = retrieve(vendorID);
			createAndShowProcessInvoiceView();
		}*/
		myRegistry.updateSubscribers(key, this);
	}

	// ----------------------------------------------------------
	public InventoryItem retrieve(String Barcode) {
		InventoryItem retValue = null;
		for (int cnt = 0; cnt < InventoryItemList.size(); cnt++) {
			InventoryItem nextInventoryItem = InventoryItemList.elementAt(cnt);
			String nextBarcode = (String) nextInventoryItem.getState("Barcode");
			if (nextBarcode.equals(Barcode) == true) {
				retValue = nextInventoryItem;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}

	/** Called via the IView relationship */
	// ----------------------------------------------------------
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}

	// ------------------------------------------------------
	protected void createAndShowIITView() {

		Scene localScene = myViews.get("InventoryItemTypeView");

		if (localScene == null) {
			// create our new view
			View newView = ViewFactory.createView("InventoryItemTypeView", this);
			localScene = new Scene(newView);
			myViews.put("InventoryItemTypeView", localScene);
		}
		// make the view visible by installing it into the frame
		swapToView(localScene);

	}

	// ----------------------------------------------------------
	protected void createAndShowView() {

		Scene localScene = myViews.get("InventoryItemCollectionView");

		if (localScene == null) {
			// create our new view
			View newView = ViewFactory.createView("InventoryItemCollectionView", this);
			localScene = new Scene(newView);
			myViews.put("InventoryItemCollectionView", localScene);
		}
		// make the view visible by installing it into the frame
		swapToView(localScene);

	}

	// -----------------------------------------------------------

/*	protected void createAndShowModifyView() {
		// create our new view
		View newView = ViewFactory.createView("ModifyVendorView", selectedVendor);
		Scene newScene = new Scene(newView);

		// make the view visible by installing it into the frame
		swapToView(newScene);
	}
*/
	// -----------------------------------------------------------

/*	protected void createAndShowProcessInvoiceView() {
		// create our new view
		View newView = ViewFactory.createView("ProcessInvoiceView", selectedVendor);
		Scene newScene = new Scene(newView);

		// make the view visible by installing it into the frame
		swapToView(newScene);
	}
*/
	// ----------------------------------------------------------
	public boolean inventoryItemFolder(Properties props) {
		try {
			InventoryItem myII = new InventoryItem(props);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// ----------------------------------------------------------
	private void searchIIT(Properties item) throws Exception {
		InventoryItemTypeCollection iit = new InventoryItemTypeCollection();
		iit.findInventoryItemType(item);
		iit.createAndShowView();
	}

	// -----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}

	// -----------------------------------------------------------------------------------
	public void printInventoryItems() {
		System.out.println("================= Vendor List ================");
		for (int cnt = 0; cnt < InventoryItemList.size(); cnt++) {
			System.out.println(InventoryItemList.get(cnt));
		}
		System.out.println("==============================================");
	}

	public void setManager(InventoryManager manager) {
		this.manager = manager;

		// System.out.println((String) manager.getState("his") + " vc");

	}

	public InventoryManager getManager() {
		return manager;
	}

}