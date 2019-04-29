package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;
import model.VendorInventoryItemType;

public class VendorInventoryItemTypeTableModel {
	private final SimpleStringProperty id;
	private final SimpleStringProperty vendorID;
	private final SimpleStringProperty inventoryItemTypeName;
	private final SimpleStringProperty vendorPrice;
	private final SimpleStringProperty dateOfLastUpdate;

	// ----------------------------------------------------------------------------
	public VendorInventoryItemTypeTableModel(Vector<String> viitData) {
		id = new SimpleStringProperty(viitData.elementAt(0));
		vendorID = new SimpleStringProperty(viitData.elementAt(1));
		inventoryItemTypeName = new SimpleStringProperty(viitData.elementAt(2));
		vendorPrice = new SimpleStringProperty(viitData.elementAt(3));
		dateOfLastUpdate = new SimpleStringProperty(viitData.elementAt(4));
	}

	public String getId() {
		return id.get();
	}

	public String getVendorID() {
		return vendorID.get();
	}

	public String getInventoryItemTypeName() {
		return inventoryItemTypeName.get();
	}

	public String getVendorPrice() {
		return vendorPrice.get();
	}

	public String getDateOfLastUpdate() {
		return dateOfLastUpdate.get();
	}

	
}
