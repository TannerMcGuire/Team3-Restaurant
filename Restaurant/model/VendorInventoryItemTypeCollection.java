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
public class VendorInventoryItemTypeCollection extends EntityBase implements IView {
	private static final String myTableName = "VendorInventoryItemType";

	private Vector<VendorInventoryItemType> vendorItemList;
	private InventoryManager manager;

	// GUI Components

	// constructor for this class
	// ----------------------------------------------------------
	public VendorInventoryItemTypeCollection() {
		super(myTableName);
		vendorItemList = new Vector<VendorInventoryItemType>();
	}

	// ----------------------------------------------------------------------------------
	private void addVendorItem(VendorInventoryItemType a) {
		// patronlist.add(a);
		int index = findIndexToAdd(a);
		vendorItemList.insertElementAt(a, index); // To build up a collection sorted on some key
	}

	// ----------------------------------------------------------------------------------
	public void findVendorInventoryItemType(Properties prop) throws Exception {
		String name = prop.getProperty("InventoryItemTypeName");
		String id = prop.getProperty("VendorID");
		String query;
		if (name != null) {
			query = "SELECT * FROM " + myTableName + " WHERE (InventoryItemTypeName LIKE '%" + name + "%')";
		} else {
			query = "SELECT * FROM " + myTableName + " WHERE (VendorId = " + id + ")";
		}

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextVendorItemData = (Properties) allDataRetrieved.elementAt(cnt);
				VendorInventoryItemType vendorItem = new VendorInventoryItemType(nextVendorItemData);

				if (vendorItem != null)
					addVendorItem(vendorItem);
			}
		else {
			throw new InvalidPrimaryKeyException("No vendor inventory items matching: " + name + " or " + id);
		}
	}

	// ----------------------------------------------------------------------------------
	public void findAllVendorInventoryItemTypes() throws Exception {
		String query;
		query = "SELECT * FROM " + myTableName;

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextVendorItemData = (Properties) allDataRetrieved.elementAt(cnt);
				VendorInventoryItemType vendorItem = new VendorInventoryItemType(nextVendorItemData);

				if (vendorItem != null)
					addVendorItem(vendorItem);
			}
		else {
			throw new InvalidPrimaryKeyException("No vendor inventory item types found");
		}
	}

	// ----------------------------------------------------------------------------------
	private int findIndexToAdd(VendorInventoryItemType a) {
		// users.add(u);
		int low = 0;
		int high = vendorItemList.size() - 1;
		int middle;

		while (low <= high) {
			middle = (low + high) / 2;

			VendorInventoryItemType midSession = vendorItemList.elementAt(middle);

			int result = VendorInventoryItemType.compare(a, midSession);

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
		if (key.equals("VendorInventoryItemType")) {
			return vendorItemList;
		} else if (key.equals("VendorInventoryItemTypeList"))
			return this;
		else if (key.equals("itemName")) {
			Vector<VendorInventoryItemType> list = new Vector<VendorInventoryItemType>();
			for (int i = 0; i <  vendorItemList.size(); i++) {
				list.add(vendorItemList.elementAt(i));
			}
			return list;
		}
		else if (key.equals("his"))
			return " ";
		else if (key.equals("self"))
			return manager;
		return null;
	}

	// ----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
//		if (key.equals("InventoryItemTypeView") == true) {
//			v = (String) value;
//			createAndShowIITView();
//		} else if (key.equals("IITInfo") == true) {
//			if (value != null) {
//				boolean flag = inventoryItemTypeFolder((Properties) value);
//				if (flag == true) {
//					try {
//						searchIIT((Properties) value);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} else if (key.equals("ModifyVendorView") == true){
//			String vendorID = (String) value;
//			selectedVendor = retrieve(vendorID);
//			createAndShowModifyView();
//		}
		myRegistry.updateSubscribers(key, this);
	}

	// ----------------------------------------------------------
	public VendorInventoryItemType retrieve(String Id) {
		VendorInventoryItemType retValue = null;
		for (int cnt = 0; cnt < vendorItemList.size(); cnt++) {
			VendorInventoryItemType nextVendorItem = vendorItemList.elementAt(cnt);
			String nextVendorItemID = (String) nextVendorItem.getState("Id");
			if (nextVendorItemID.equals(Id) == true) {
				retValue = nextVendorItem;
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

	// ----------------------------------------------------------
	protected void createAndShowView() {

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

	// -----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}

	// -----------------------------------------------------------------------------------
	public void printVendorItems() {
		System.out.println("================= Vendor Inventory Item Type List ================");
		for (int cnt = 0; cnt < vendorItemList.size(); cnt++) {
			System.out.println(vendorItemList.get(cnt));
		}
		System.out.println("==================================================================");
	}

	public void setManager(InventoryManager manager) {
		this.manager = manager;
	}

	public InventoryManager getManager() {
		return manager;
	}

}