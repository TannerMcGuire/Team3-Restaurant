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

	private Vector<InventoryItem> inventoryItemList;
	private InventoryItem _selectedInventoryItem;
	public static String iname;
	private InventoryManager manager;
	private InventoryItem myInventoryItem;

	// GUI Components

	// constructor for this class
	// ----------------------------------------------------------
	public InventoryItemCollection() {
		super(myTableName);
		inventoryItemList = new Vector<InventoryItem>();
		_selectedInventoryItem = new InventoryItem();
	}

	// ----------------------------------------------------------------------------------
	private void addInventoryItem(InventoryItem a) {
		// patronlist.add(a);
		int index = findIndexToAdd(a);
		inventoryItemList.insertElementAt(a, index); // To build up a collection sorted on some key
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
			throw new InvalidPrimaryKeyException("No Inventory Items Matching: " + barcode);
		}
	}

	// ----------------------------------------------------------------------------------
	public void findAvailiableInventoryItem(Properties prop) throws Exception {
		String name = prop.getProperty("name");
		String barcode = prop.getProperty("Barcode");
		String query;
		if (name != null) {
			query = "SELECT * FROM " + myTableName + " WHERE InventoryItemTypeName LIKE '%" + name
					+ "%' AND Status ='Available'";
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

	// ----------------------------------------------------------------------------------
	public void findAllInventoryItems() throws Exception {
		String query;
		query = "SELECT * FROM " + myTableName;

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextIIData = (Properties) allDataRetrieved.elementAt(cnt);
				InventoryItem inventoryItem = new InventoryItem(nextIIData);

				if (inventoryItem != null)
					addInventoryItem(inventoryItem);
			}
		else {
			throw new InvalidPrimaryKeyException("No Inventory Items found");
		}
	}

	// ----------------------------------------------------------------------------------
	public void findWithName(String name) throws Exception {
		String query = "";
		if (name != null) {
			query = "SELECT * FROM " + myTableName + " WHERE (InventoryItemTypeName LIKE '%" + name
					+ "%' AND Status ='Available')";
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
	public void findAvailableInventoryItems() throws Exception {
		String query;
		query = "SELECT * FROM " + myTableName + " WHERE (Status = 'Available')";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextIIData = (Properties) allDataRetrieved.elementAt(cnt);
				InventoryItem inventoryItem = new InventoryItem(nextIIData);

				if (inventoryItem != null)
					addInventoryItem(inventoryItem);
			}
		else {
			throw new InvalidPrimaryKeyException("No item types found");
		}
	}

	// ----------------------------------------------------------------------------------
	private int findIndexToAdd(InventoryItem a) {
		// users.add(u);
		int low = 0;
		int high = inventoryItemList.size() - 1;
		int middle;

		while (low <= high) {
			middle = (low + high) / 2;

			InventoryItem midSession = inventoryItemList.elementAt(middle);

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
		if (key.equals("InventoryItem"))
			return inventoryItemList;
		else if (key.equals("InventoryItems"))
			return inventoryItemList;
		else if (key.equals("InventoryItemCollection"))
			return this;
		else if (key.equals("InventoryItemList"))
			return this;
		else if (key.equals("SelectedInventoryItem"))
			return _selectedInventoryItem;
		else if (key.equals("his")) {
			// System.out.println(manager);
			return manager.getState("his");
		}
		return null;
	}

	// ----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		if (key.equals("InventoryManagerView") == true) {
			createAndShowInventoryManagerView();
		} else if (key.equals("BarcodeSearch")) {
			if (value != null) {
				boolean flag = inventoryItemFolder((Properties) value);
				if (flag == true) {
					try {
						InventoryItem a = new InventoryItem((Properties) value);
						setSelectedInventoryItem(a);
						searchInventoryItem((Properties) value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (key.equals("InventoryItemModified")) {
			_selectedInventoryItem.modifyItem((String) value);
		} else if (key.equals("SearchBarcode")) {
			if (value != null) {
				boolean flag = inventoryItemFolder((Properties) value);
				if (flag == true) {
					try {
						InventoryItem a = new InventoryItem((Properties) value);
						setSelectedInventoryItem(a);
						modifyInventoryItem((Properties) value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (key.equals("InventoryItemTypeView") == true) {
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
		}
		myRegistry.updateSubscribers(key, this);
	}

	// ----------------------------------------------------------------
	public boolean inventoryItemFolder(Properties props) {
		try {
			myInventoryItem = new InventoryItem(props);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// ----------------------------------------------------------
	public InventoryItem retrieve(String barcode) {
		InventoryItem retValue = null;
		for (int cnt = 0; cnt < inventoryItemList.size(); cnt++) {
			InventoryItem nextII = inventoryItemList.elementAt(cnt);
			String nextIIID = (String) nextII.getState("Barcode");
			if (nextIIID.equals(barcode) == true) {
				retValue = nextII;
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
	public InventoryItem getSelectInventoryItemType() {
		return _selectedInventoryItem;
	}

	// ------------------------------------------------------
	public void createAndShowView() {

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
	private void searchIIT(Properties item) throws Exception {
		InventoryItemTypeCollection iit = new InventoryItemTypeCollection();
		iit.findInventoryItemType(item);
		iit.createAndShowView();
	}

	// ------------------------------------------------------
	private void searchInventoryItem(Properties item) throws Exception { // FINISH THIS

		createAndShowConfirmInventoryItemRemovalView(item);
	}

	// ------------------------------------------------------
	private void modifyInventoryItem(Properties item) throws Exception { // FINISH THIS

		createAndShowModifyInventoryItemView(item);
	}

	// ------------------------------------------------------
	private void createAndShowModifyInventoryItemView(Properties item) {
		Scene currentScene = (Scene) myViews.get("ModifyInventoryItemView");
		if (currentScene == null) {
			// create our new view
			View newView = ViewFactory.createView("ModifyInventoryItemView", this);
			currentScene = new Scene(newView);
			myViews.put("ModifyInventoryItemView", currentScene);
		}

		swapToView(currentScene);
	}
	
	// ------------------------------------------------------
	private void createAndShowConfirmInventoryItemRemovalView(Properties item) { // FINISH THIS
		Scene currentScene = (Scene) myViews.get("ConfirmInventoryItemRemovalView");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("ConfirmInventoryItemRemovalView", this);
			currentScene = new Scene(newView);
			myViews.put("ConfirmInventoryItemRemovalView ", currentScene);
		}

		swapToView(currentScene);
	}

	// ------------------------------------------------------------
	private void createAndShowInventoryManagerView() {
		Scene currentScene = (Scene) myViews.get("InventoryManagerView");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("InventoryManagerView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("InventoryManagerView", currentScene);
		}

		swapToView(currentScene);

	}

	// -----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}

	// -----------------------------------------------------------------------------------
	public void printItemTypes() {
		System.out.println("================= Inventory Item List ================");
		for (int cnt = 0; cnt < inventoryItemList.size(); cnt++) {
			System.out.println(inventoryItemList.get(cnt));
		}
		System.out.println("======================================================");
	}

	public void setManager(InventoryManager manager) {
		this.manager = manager;
	}

	public void setSelectedInventoryItem(InventoryItem a) {
		_selectedInventoryItem = a;
	}

	public InventoryManager getManager() {
		return manager;
	}
}