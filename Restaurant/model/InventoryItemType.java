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

import impresario.IView;

import model.InventoryManager;
import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Vendor for the database application */
//==============================================================
public class InventoryItemType extends EntityBase implements IView {
	private static final String myTableName = "InventoryItemType";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";

	// constructor for this class
	// ----------------------------------------------------------
	public InventoryItemType(String name) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (ItemTypeName = '" + name + "')";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one vendor at least
		if (allDataRetrieved != null) {
			int size = allDataRetrieved.size();

			// There should be EXACTLY one vendor. More than that is an error
			if (size != 1) {
				throw new InvalidPrimaryKeyException("Multiple items matching name : " + name + " found.");
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
		// If no vendor found for this ID, throw an exception
		else {
			throw new InvalidPrimaryKeyException("No inventory matching name : " + name + " found.");
		}
	}

	// ----------------------------------------------------------
	public InventoryItemType() {
		super(myTableName);

		setDependencies();
		persistentState = new Properties();

	}

	// Can also be used to create a NEW Vendor (if the system it is part of
	// allows for a new vendor to be set up)
	// ----------------------------------------------------------
	public InventoryItemType(Properties props) {
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
	public static int compare(InventoryItemType a, InventoryItemType b) {
		String aID = (String) a.getState("ItemTypeName");
		String bID = (String) b.getState("ItemTypeName");

		return aID.compareTo(bID);
	}

	// -----------------------------------------------------------------------------------
	public void delete() {
		persistentState.setProperty("Status", "Inactive");
		update();
	}
	
	// -----------------------------------------------------------------------------------
	public void update() {
		updateStateInDatabase();
	}

	// -----------------------------------------------------------------------------------
	private void updateStateInDatabase() {
		try {
			if (persistentState.getProperty("ItemTypeName") != null) {
				if (!(InventoryManager.history.equals("addIIT"))) {
					Properties whereClause = new Properties();
					whereClause.setProperty("ItemTypeName", persistentState.getProperty("ItemTypeName"));
					updatePersistentState(mySchema, persistentState, whereClause);
					updateStatusMessage = "Inventory Item type data for type name : "
							+ persistentState.getProperty("ItemTypeName") + " updated successfully in database!";
				} else {
					insertPersistentState(mySchema, persistentState);
					updateStatusMessage = "Inventory Item Type data for new Inventory Item Type : "
							+ persistentState.getProperty("ItemTypeName") + " shelved successfully in database!";
				}
			}
		} catch (

		SQLException ex) {
			updateStatusMessage = "Error in shelving inventory item type data in database!";
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

		v.addElement(persistentState.getProperty("ItemTypeName"));
		v.addElement(persistentState.getProperty("Units"));
		v.addElement(persistentState.getProperty("UnitMeasure"));
		v.addElement(persistentState.getProperty("ValidityDays"));
		v.addElement(persistentState.getProperty("ReorderPoint"));
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
		return persistentState.getProperty("ItemTypeName") + ", " + persistentState.getProperty("ValidityDays") + ": "
				+ persistentState.getProperty("Status");

	}
}