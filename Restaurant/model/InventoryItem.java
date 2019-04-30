// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import model.InventoryManager;

/** The class containing the Vendor for the database application */
//==============================================================
public class InventoryItem extends EntityBase {
	private static final String myTableName = "InventoryItem";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";

	// constructor for this class
	// ----------------------------------------------------------
	public InventoryItem(int barcode) throws InvalidPrimaryKeyException {
		//UPDATE FOR USE CASE 8
		//----METHOD FOR INVENTORYITEM CONSTRUCTOR BELOW----
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (Barcode = " + barcode + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one vendor at least
		if (allDataRetrieved != null) {
			int size = allDataRetrieved.size();

			// There should be EXACTLY one InventoryItem. More than that is an error
			if (size != 1) {
				throw new InvalidPrimaryKeyException("Multiple or no items matching barcode : " + barcode + " found.");
			} else {
				// copy all the retrieved data into persistent state
				Properties retrievedVendorData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedVendorData.propertyNames();
				while (allKeys.hasMoreElements() == true) {
					String nextKey = (String) allKeys.nextElement();
					String nextValue = retrievedVendorData.getProperty(nextKey);

					if (nextValue != null) {
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		}
		// If no InventoryItem found for this barcode, throw an exception
		else {
			throw new InvalidPrimaryKeyException("No inventory matching barcode : " + barcode + " found.");
		}
	}

	// ----------------------------------------------------------
	public InventoryItem() {
		super(myTableName);

		setDependencies();
		persistentState = new Properties();

	}

	// Can also be used to create a NEW Vendor (if the system it is part of
	// allows for a new vendor to be set up)
	// ----------------------------------------------------------
	public InventoryItem(Properties props) {
		super(myTableName);

		setDependencies();
		persistentState = new Properties();
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true) {
			String nextKey = (String) allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null) {
				persistentState.setProperty(nextKey, nextValue);
			}
		}
	}

	// -----------------------------------------------------------------------------------
	private void setDependencies() {
		dependencies = new Properties();

		myRegistry.setDependencies(dependencies);
	}

	// ----------------------------------------------------------
	public Object getState(String key) {
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;
		return persistentState.getProperty(key);
	}

	// ----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		if (key.equals("SUBMIT")) {
			processUpdate((Properties) value);
		}

		myRegistry.updateSubscribers(key, this);
	}

	// ----------------------------------------------------------------
	private void processUpdate(Properties properties) {
		persistentState = properties;

		update();
	}

	/** Called via the IView relationship */
	// ----------------------------------------------------------
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}

	// -----------------------------------------------------------------------------------
	/*public static int compare(InventoryItem a, InventoryItem b) {
		String aID = (String) a.getState("barcode");
		String bID = (String) b.getState("barcode");
		return aID.compareTo(bID);
	} */

	// -----------------------------------------------------------------------------------
	public void takeOut() {
		persistentState.setProperty("Status", "Used");
		update();
	}
	
	// -----------------------------------------------------------------------------------
	public void update() {
		updateStateInDatabase();
	}

	// -----------------------------------------------------------------------------------
	private void updateStateInDatabase() {
		try {
			if (persistentState.getProperty("Barcode") != null) {
				if (!(InventoryManager.history.equals("processInvoice"))) {
					Properties whereClause = new Properties();
					whereClause.setProperty("InventoryItemTypeName", persistentState.getProperty("InventoryItemTypeName"));
					updatePersistentState(mySchema, persistentState, whereClause);
					updateStatusMessage = "Inventory Item type data for barcode : "
							+ persistentState.getProperty("Barcode") + " updated successfully in database!";
				} else {					
					insertPersistentState(mySchema, persistentState);
					updateStatusMessage = "Inventory Item Type data for new Inventory Item : "
							+ persistentState.getProperty("Barcode") + " shelved successfully in database!";
				}
			}
		} catch (

		SQLException ex) {
			updateStatusMessage = "Error in shelving inventory item data in database!";
		}
		System.out.println("updateStateInDatabase " + updateStatusMessage);
	}
 
	/**
	 * This method is needed solely to enable the Vendor information to be
	 * displayable in a table
	 *
	 */
	// --------------------------------------------------------------------------
	public Vector<String> getEntryListView() {
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("Barcode"));
		v.addElement(persistentState.getProperty("InventoryItemTypeName"));
		v.addElement(persistentState.getProperty("VendorId"));
		v.addElement(persistentState.getProperty("DateReceived"));
		v.addElement(persistentState.getProperty("DateofLastUse"));
		v.addElement(persistentState.getProperty("Notes"));
		v.addElement(persistentState.getProperty("Status"));

		return v;
	}

	// -----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}

	// -----------------------------------------------------------------------------------
	public String toString() {
		return persistentState.getProperty("InventoryItemTypeName") + ", " + persistentState.getProperty("VendorId") + ": "
				+ persistentState.getProperty("Status");

	}
} 