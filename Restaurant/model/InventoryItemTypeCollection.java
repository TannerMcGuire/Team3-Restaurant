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
	public static String iname;
	// GUI Components

	// constructor for this class
	// ----------------------------------------------------------
	public InventoryItemTypeCollection() {
		super(myTableName);
		inventoryItemTypeList = new Vector<InventoryItemType>();
	}

	// ----------------------------------------------------------------------------------
	private void addInventoryItemType(InventoryItemType a) {
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
					addInventoryItemType(inventoryItemType);
			}
		else {
			throw new InvalidPrimaryKeyException("No item types matching: " + name + " or " + notes);
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
		else if (key.equals("InventoryItemTypeList"))
			return this;
		return null;
	}

	// ----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		if (key.equals("InventoryManagerView") == true) {
			createAndShowInventoryManagerView();
		} else if (key.equals("IITInfo") == true) {
			iname = (String) value;
			createAndShowVIITPriceView();
		} else if (key.equals("Success") == true) {
			createAndShowYay();
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
	protected void createAndShowView() {

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

	// ------------------------------------------------------------
	private void createAndShowYay() {
		Scene currentScene = (Scene) myViews.get("Yay");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("Yay", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("Yay", currentScene);
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
}
