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
public class InventoryItemTypeCollection extends EntityBase implements IView {
	private static final String myTableName = "InventoryItemType";

	private Vector<InventoryItemType> inventoryItemTypeList;
	private InventoryItemType _selectedInventoryItemType;
	private Vendor v;
	public static String iname;
	private InventoryManager manager;
	// GUI Components

	// constructor for this class
	// ----------------------------------------------------------
	public InventoryItemTypeCollection() {
		super(myTableName);
		inventoryItemTypeList = new Vector<InventoryItemType>();
		_selectedInventoryItemType = new InventoryItemType();
	}

	// ----------------------------------------------------------------------------------
	public void addInventoryItemType(InventoryItemType a) {
		// patronlist.add(a);
		int index = findIndexToAdd(a);
		inventoryItemTypeList.insertElementAt(a, index); // To build up a collection sorted on some key
	}

	// ----------------------------------------------------------------------------------
	public void findInventoryItemType(Properties prop) throws Exception {
		String name = prop.getProperty("ItemTypeName");
		String notes = prop.getProperty("Notes");
		String query;
		if (name != null) {
			query = "SELECT * FROM " + myTableName + " WHERE (ItemTypeName LIKE '%" + name + "%')";
		} else {
			query = "SELECT * FROM " + myTableName + " WHERE (Notes LIKE '%" + notes + "%')";
		}

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextIITData = (Properties) allDataRetrieved.elementAt(cnt);
				InventoryItemType inventoryItemType = new InventoryItemType(nextIITData);

				if (inventoryItemType != null)
					if (!(inventoryItemType.getState("Status").equals("Inactive")))
						addInventoryItemType(inventoryItemType);
			}
		else {
			throw new InvalidPrimaryKeyException("No item types matching: " + name + " or " + notes);
		}
	}

	// ----------------------------------------------------------------------------------
	public void findAllInventoryItemTypes() throws Exception {
		String query;
		query = "SELECT * FROM " + myTableName;

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextIITData = (Properties) allDataRetrieved.elementAt(cnt);
				InventoryItemType inventoryItemType = new InventoryItemType(nextIITData);

				if (inventoryItemType != null)
					addInventoryItemType(inventoryItemType);
			}
		else {
			throw new InvalidPrimaryKeyException("No item types found");
		}
	}

	// ----------------------------------------------------------------------------------
	public void findInventoryItemTypesWithNameLike(String name) throws InvalidPrimaryKeyException {

		String query = "SELECT * FROM " + myTableName + " WHERE (ItemTypeName LIKE '%" + name + "%');";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null) {

			for (int index = 0; index < allDataRetrieved.size(); index++) {

				Properties inventoryItemTypeProperties = (Properties) allDataRetrieved.elementAt(index);

				InventoryItemType inventoryItemType = new InventoryItemType(inventoryItemTypeProperties);

				inventoryItemTypeList.add(inventoryItemType);
			}
		} else {
			throw new InvalidPrimaryKeyException("No InventoryItemTypes with name like : " + name + ".");
		}
	}

	// ----------------------------------------------------------------------------------
	public void findInventoryItemTypesWithNotesLike(String notes) throws InvalidPrimaryKeyException {

		String query = "SELECT * FROM " + myTableName + " WHERE (Notes LIKE '%" + notes + "%');";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null) {

			for (int index = 0; index < allDataRetrieved.size(); index++) {

				Properties inventoryItemTypeProperties = (Properties) allDataRetrieved.elementAt(index);

				InventoryItemType inventoryItemType = new InventoryItemType(inventoryItemTypeProperties);

				inventoryItemTypeList.add(inventoryItemType);
			}
		} else {
			throw new InvalidPrimaryKeyException("No InventoryItemTypes with Notes like : " + notes + ".");
		}
	}

	// ----------------------------------------------------------------------------------
	public void findInventoryItemTypesWithNameAndNotesLike(String name, String notes)
			throws InvalidPrimaryKeyException {

		String query = "SELECT * FROM " + myTableName + " WHERE (ItemTypeName LIKE '%" + name + "%') AND (Notes LIKE '%"
				+ notes + "%');";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null) {

			for (int index = 0; index < allDataRetrieved.size(); index++) {

				Properties inventoryItemTypeProperties = (Properties) allDataRetrieved.elementAt(index);

				InventoryItemType inventoryItemType = new InventoryItemType(inventoryItemTypeProperties);

				inventoryItemTypeList.add(inventoryItemType);
			}
		} else {
			throw new InvalidPrimaryKeyException(
					"No InventoryItemTypes with ItemTypeName like : " + name + " and Notes like : " + notes + ".");
		}
	}

	// ----------------------------------------------------------------------------------
	public void findActiveInventoryItemTypes()
			throws InvalidPrimaryKeyException {

		String query = "SELECT * FROM " + myTableName + " WHERE Status = 'Active';";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null) {

			for (int index = 0; index < allDataRetrieved.size(); index++) {

				Properties inventoryItemTypeProperties = (Properties) allDataRetrieved.elementAt(index);

				InventoryItemType inventoryItemType = new InventoryItemType(inventoryItemTypeProperties);

				inventoryItemTypeList.add(inventoryItemType);
			}
		} else {
			throw new InvalidPrimaryKeyException(
					"No InventoryItemTypes active ItemTypeName");
		}
	}

	// ----------------------------------------------------------------------------------
	private int findIndexToAdd(InventoryItemType a) {
		// users.add(u);
		int low = 0;
		int high = inventoryItemTypeList.size() - 1;
		int middle;

		while (low <= high) {
			middle = (low + high) / 2;

			InventoryItemType midSession = inventoryItemTypeList.elementAt(middle);

			int result = InventoryItemType.compare(a, midSession);

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
		if (key.equals("InventoryItemType"))
			return inventoryItemTypeList;
		else if (key.equals("InventoryItemTypes"))
			return inventoryItemTypeList;
		else if (key.equals("InventoryItemTypeCollection"))
			return this;
		else if (key.equals("InventoryItemTypeList"))
			return this;
		else if (key.equals("id")) {
			return v.getState("ID");
		} else if (key.equals("his")) {
			//System.out.println(manager);
			return manager.getState("his");
		}
		return null;
	}

	// ----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		if (key.equals("InventoryManagerView") == true) {
			createAndShowInventoryManagerView();
		} else if (key.equals("IITInfo") == true) {
			iname = (String) value;
				createAndShowVIITPriceView();
		} else if (key.equals("deleteVIIT") == true) {
			Properties prop = new Properties();
			prop.setProperty("InventoryItemTypeName", (String) value);
			VendorInventoryItemTypeCollection viit = new VendorInventoryItemTypeCollection();
			try {
				viit.findVendorInventoryItemType(prop);
			} catch (Exception e) {
				e.printStackTrace();
			}
			viit.createAndShowView();
		} else if (key.equals("IITchange") == true) {
			iname = (String) value;
			try {
				createAndShowInventoryItemTypeView(new InventoryItemType(iname));
			} catch (InvalidPrimaryKeyException e) {
				e.printStackTrace();
			}
		} else if (key.equals("MODIFY IIT")) {
			try {
				_selectedInventoryItemType = new InventoryItemType((String) value);
			} catch (InvalidPrimaryKeyException e) {
				e.printStackTrace();
			}
			createAndShowEnterInventoryItemTypeChangesScreen();
		} else if (key.equals("Submit new IIT Info")) {
			InventoryItemType inventoryItemType = new InventoryItemType();
			inventoryItemType.stateChangeRequest("SUBMIT", (Properties) value);
		}
		myRegistry.updateSubscribers(key, this);
	}

	// ----------------------------------------------------------
	public InventoryItemType retrieve(String name) {
		InventoryItemType retValue = null;
		for (int cnt = 0; cnt < inventoryItemTypeList.size(); cnt++) {
			InventoryItemType nextIIT = inventoryItemTypeList.elementAt(cnt);
			String nextIITID = (String) nextIIT.getState("Id");
			if (nextIITID.equals(name) == true) {
				retValue = nextIIT;
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
	public InventoryItemType getSelectInventoryItemType() {
		return _selectedInventoryItemType;
	}

	// ------------------------------------------------------
	public void createAndShowView() {

		Scene localScene = myViews.get("InventoryItemTypeCollectionView");

		if (localScene == null) {
			// create our new view
			View newView = ViewFactory.createView("InventoryItemTypeCollectionView", this);
			localScene = new Scene(newView);
			myViews.put("InventoryItemTypeCollectionView", localScene);
		}
		// make the view visible by installing it into the frame
		swapToView(localScene);

	}

	// ------------------------------------------------------
	public void createAndShowView2() {

		Scene localScene = myViews.get("InventoryItemTypeCollectionView2");

		if (localScene == null) {
			// create our new view
			View newView = ViewFactory.createView("InventoryItemTypeCollectionView2", this);
			localScene = new Scene(newView);
			myViews.put("InventoryItemTypeCollectionView2", localScene);
		}
		// make the view visible by installing it into the frame
		swapToView(localScene);

	}

	// ------------------------------------------------------
	public void createAndShowVendorInventoryItemTypeCollection() {

		Scene localScene = myViews.get("VendorInventoryItemTypeCollectionView");

		if (localScene == null) {
			// create our new view
			View newView = ViewFactory.createView("VendorInventoryItemTypeCollectionView", this);
			localScene = new Scene(newView);
			myViews.put("VendorInventoryItemTypeCollectionView", localScene);
		}
		// make the view visible by installing it into the frame
		swapToView(localScene);

	}

	// ------------------------------------------------------------
	public void createAndShowInventoryItemTypeSelectionScreens() {
		Scene currentScene = (Scene) myViews.get("InventoryItemTypeSelectionScreen");

		if (currentScene == null) {

			View newView = ViewFactory.createView("InventoryItemTypeSelectionScreen", this);
			currentScene = new Scene(newView);
			myViews.put("InventoryItemTypeSelectionScreen", currentScene);
		}

		swapToView(currentScene);
	}

	// ------------------------------------------------------------
	public void createAndShowEnterInventoryItemTypeChangesScreen() {
		Scene currentScene = (Scene) myViews.get("InventoryItemTypeChangesScreen");

		if (currentScene == null) {

			View newView = ViewFactory.createView("InventoryItemTypeChangesScreen", this);
			currentScene = new Scene(newView);
			myViews.put("InventoryItemTypeChangesScreen", currentScene);
		}

		swapToView(currentScene);
	}

	// ------------------------------------------------------------
	private void createAndShowInventoryItemTypeView(InventoryItemType i) {
		Scene currentScene = (Scene) myViews.get("InventoryItemTypeView");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("InventoryItemTypeView", i);
			currentScene = new Scene(newView);
			myViews.put("InventoryItemTypeView", currentScene);
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

	// ------------------------------------------------------------
	private void createAndShowVIITPriceView() {
		Scene currentScene = (Scene) myViews.get("VIITPriceView");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("VIITPriceView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("VIITPriceView", currentScene);
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
		System.out.println("================= Inventory Item Type List ================");
		for (int cnt = 0; cnt < inventoryItemTypeList.size(); cnt++) {
			System.out.println(inventoryItemTypeList.get(cnt));
		}
		System.out.println("===========================================================");
	}

	public void setManager(InventoryManager manager) {
		this.manager = manager;
	}

	public void setV(Vendor vend) {
		v = vend;
	}
	
	public InventoryManager getManager() {
		return manager;
	}
}