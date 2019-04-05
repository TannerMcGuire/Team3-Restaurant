package model;

//system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

//project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

/**
 * This class contains the Vendor Inventory Item Type for database application
 **/
public class VendorInventoryItemType extends EntityBase {
	private static final String myTableName = "VendorInventoryItemType";

	protected Properties dependencies;

	// GUI

	private String updateStatusMessage = "";

	// constructor
	// --------------------------------------------
	public VendorInventoryItemType(String Id) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();

		String query = "SELECT * FROM " + myTableName + " WHERE (Id = " + Id + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one book at least
		if (allDataRetrieved != null) {
			int size = allDataRetrieved.size();

			// There should be EXACTLY one book. More than that is an error
			if (size != 1) {
				throw new InvalidPrimaryKeyException("Multiple items matching id : " + Id + " found.");
			} else {
				// copy all the retrieved data into persistent state
				Properties retrievedVIITData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedVIITData.propertyNames();
				while (allKeys.hasMoreElements() == true) {
					String nextKey = (String) allKeys.nextElement();
					String nextValue = retrievedVIITData.getProperty(nextKey);
				
					if (nextValue != null) {
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		}
		// If no book found for this ID, throw an exception
		else {
			throw new InvalidPrimaryKeyException("No vendor inventory item types matching id : " + Id + " found.");
		}
	}


	public VendorInventoryItemType(Properties props) {
		super(myTableName);
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

		myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	// ----------------------------------------------------------
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}

	// -----------------------------------------------------------------------------------
	public void update() {
		updateStateInDatabase();
	}

	// -----------------------------------------------------------------------------------
	private void updateStateInDatabase() {
		try {
			if (persistentState.getProperty("Id") != null) {
				Properties whereClause = new Properties();
				whereClause.setProperty("Id", persistentState.getProperty("Id"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "VendorInventoryItemType for id : " + persistentState.getProperty("Id")
						+ " updated successfully in database!";
			} else {
				Integer viitId = insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("Id", "" + viitId.intValue());
				updateStatusMessage = "VendorInventoryItemType for new vendor inventory item type : " + persistentState.getProperty("Id")
						+ " successfully added to database!";
			}
		} catch (SQLException ex) {
			updateStatusMessage = "Error in inputting data in database!";
		}
		System.out.println("updateStateInDatabase " + updateStatusMessage);
	}

	/**
	 * This method is needed solely to enable the Book information to be displayable
	 * in a table
	 *
	 */
	// --------------------------------------------------------------------------
	public Vector<String> getEntryListView() {
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("Id"));
		v.addElement(persistentState.getProperty("VendorId"));
		v.addElement(persistentState.getProperty("InventoryItemTypeName"));
		v.addElement(persistentState.getProperty("VendorPrice"));
		v.addElement(persistentState.getProperty("DateofLastUpdate"));

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
		return persistentState.getProperty("InventoryItemTypeName") + ", $" +persistentState.getProperty("VendorPrice")
			+ ", " + persistentState.getProperty("DateofLastUpdate");
	}

}
