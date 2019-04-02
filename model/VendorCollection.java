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
public class VendorCollection extends EntityBase implements IView {
	private static final String myTableName = "Vendor";

	private Vector<Vendor> vendorList;
	// GUI Components

	// constructor for this class
	// ----------------------------------------------------------
	public VendorCollection() {
		super(myTableName);
		vendorList = new Vector<Vendor>();
	}

	// ----------------------------------------------------------------------------------
	private void addVendor(Vendor a) {
		// patronlist.add(a);
		int index = findIndexToAdd(a);
		vendorList.insertElementAt(a, index); // To build up a collection sorted on some key
	}

	// ----------------------------------------------------------------------------------
	public void findVendor(Properties prop) throws Exception {
		String name = prop.getProperty("Name");
		String number = prop.getProperty("PhoneNumber");
		String query;
		if (name != null) {
			query = "SELECT * FROM " + myTableName + " WHERE (Name LIKE '%" + name + "%')";
		} else {
			query = "SELECT * FROM " + myTableName + " WHERE (PhoneNumber = " + number + ")";
		}
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextVendorData = (Properties) allDataRetrieved.elementAt(cnt);
				Vendor vendor = new Vendor(nextVendorData);

				if (vendor != null)
					addVendor(vendor);
			}
		else {
			throw new InvalidPrimaryKeyException("No vendors matching: " + name + " or " + number);
		}
	}

	// ----------------------------------------------------------------------------------
	private int findIndexToAdd(Vendor a) {
		// users.add(u);
		int low = 0;
		int high = vendorList.size() - 1;
		int middle;

		while (low <= high) {
			middle = (low + high) / 2;

			Vendor midSession = vendorList.elementAt(middle);

			int result = Vendor.compare(a, midSession);

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
		if (key.equals("Vendors"))
			return vendorList;
		else if (key.equals("VendorList"))
			return this;
		return null;
	}

	// ----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		if (key.equals("InventoryManagerView") == true) {
			createAndShowInventoryManagerView();
		}
		myRegistry.updateSubscribers(key, this);
	}

	// ----------------------------------------------------------
	public Vendor retrieve(String vendorId) {
		Vendor retValue = null;
		for (int cnt = 0; cnt < vendorList.size(); cnt++) {
			Vendor nextVendor = vendorList.elementAt(cnt);
			String nextVendorID = (String) nextVendor.getState("Id");
			if (nextVendorID.equals(vendorId) == true) {
				retValue = nextVendor;
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

		Scene localScene = myViews.get("VendorCollectionView");

		if (localScene == null) {
			// create our new view
			View newView = ViewFactory.createView("VendorCollectionView", this);
			localScene = new Scene(newView);
			myViews.put("VendorCollectionView", localScene);
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

	// -----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}

	// -----------------------------------------------------------------------------------
	public void printVendors() {
		System.out.println("================= Vendor List ================");
		for (int cnt = 0; cnt < vendorList.size(); cnt++) {
			System.out.println(vendorList.get(cnt));
		}
		System.out.println("==============================================");
	}
}
