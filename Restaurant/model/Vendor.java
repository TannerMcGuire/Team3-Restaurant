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

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Vendor for the database application */
//==============================================================
public class Vendor extends EntityBase implements IView {
	private static final String myTableName = "Vendor";

	protected Properties dependencies;

	// GUI Components

	public String updateStatusMessage = "";

	// constructor for this class
	// ----------------------------------------------------------
	public Vendor(String Id) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (Id = " + Id + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one vendor at least
		if (allDataRetrieved != null) {
			int size = allDataRetrieved.size();

			// There should be EXACTLY one vendor. More than that is an error
			if (size != 1) {
				throw new InvalidPrimaryKeyException("Multiple vendors matching id : " + Id + " found.");
			} else {
				// copy all the retrieved data into persistent state
				Properties retrievedVendorData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedVendorData.propertyNames();
				while (allKeys.hasMoreElements() == true) {
					String nextKey = (String) allKeys.nextElement();
					String nextValue = retrievedVendorData.getProperty(nextKey);
					// Id = Integer.parseInt(retrievedVendorData.getProperty("Id"));

					if (nextValue != null) {
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		}
		// If no vendor found for this ID, throw an exception
		else {
			throw new InvalidPrimaryKeyException("No vendor matching id : " + Id + " found.");
		}
	}

	// Can also be used to create a NEW Vendor (if the system it is part of
	// allows for a new vendor to be set up)
	// ----------------------------------------------------------
	public Vendor(Properties props) {
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
		else if (key.equals("ID"))
			return persistentState.getProperty("Id");
		else
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
	public static int compare(Vendor a, Vendor b) {
		String aID = (String) a.getState("Id");
		String bID = (String) b.getState("Id");

		return aID.compareTo(bID);
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
				updateStatusMessage = "Vendor data for vendor ID : " + persistentState.getProperty("Id")
						+ " updated successfully in database!";
			} else {
				VendorCollection vc = new VendorCollection();
				try {
					vc.findAllVendors();

					Vector list = (Vector) vc.getState("Vendors");
					for (int i = 0; i < list.size(); i++) {
						Vendor old = vc.retrieve(Integer.toString(i + 1));
						if (old.persistentState.getProperty("Name").equals(persistentState.getProperty("Name")))
							if (old.persistentState.getProperty("PhoneNumber")
									.equals(persistentState.getProperty("PhoneNumber"))) {
								updateStatusMessage = "Vendor already exists!";
							}
					}
					if (updateStatusMessage.equals("Vendor already exists!")) {

					} else {
						Integer Id = insertAutoIncrementalPersistentState(mySchema, persistentState);
						persistentState.setProperty("Id", "" + Id.intValue());
						updateStatusMessage = "Vendor data for new vendor : " + persistentState.getProperty("Id")
								+ " shelved successfully in database!";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException ex) {
			updateStatusMessage = "Error in shelving vendor data in database!";
		}
		// System.out.println("updateStateInDatabase " + updateStatusMessage);
	}

	/**
	 * This method is needed solely to enable the Vendor information to be
	 * displayable in a table
	 *
	 */
	// --------------------------------------------------------------------------
	public Vector<String> getEntryListView() {
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("Id"));
		v.addElement(persistentState.getProperty("Name"));
		v.addElement(persistentState.getProperty("PhoneNumber"));
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
		return persistentState.getProperty("Name") + ", " + persistentState.getProperty("PhoneNumber") + ": "
				+ persistentState.getProperty("Status");

	}
}