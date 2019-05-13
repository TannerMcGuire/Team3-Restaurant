package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

public class InventoryItemTableModel {
	private final SimpleStringProperty barcode;
	private final SimpleStringProperty itemName;
	private final SimpleStringProperty vendorId;
	private final SimpleStringProperty dateReceived;
	private final SimpleStringProperty dateLastUsed;
	private final SimpleStringProperty notes;
	private final SimpleStringProperty status;

	// ----------------------------------------------------------------------------
	public InventoryItemTableModel(Vector<String> iiData) {
		barcode = new SimpleStringProperty(iiData.elementAt(0));
		itemName = new SimpleStringProperty(iiData.elementAt(1));
		vendorId = new SimpleStringProperty(iiData.elementAt(2));
		dateReceived = new SimpleStringProperty(iiData.elementAt(3));
		dateLastUsed = new SimpleStringProperty(iiData.elementAt(4));
		notes = new SimpleStringProperty(iiData.elementAt(5));
		status = new SimpleStringProperty(iiData.elementAt(6));
	}

	public String getBarcode() {
		return barcode.get();
	}
	public void setBarcode(String num) {
		barcode.set(num);
	}
	public String getItemName() {
		return itemName.get();
	}
	public void setItemName(String n) {
		itemName.set(n);
	}
	public String getVendorID() {
		return vendorId.get();
	}
	public void setVendorID(String i) {
		vendorId.set(i);
	}
	public String getDateReceived() {
		return dateReceived.get();
	}
	public void setDateReceived(String d) {
		dateReceived.set(d);
	}
	public String getDateLastUsed() {
		return dateLastUsed.get();
	}
	public void setDateLastUsed(String d) {
		dateLastUsed.set(d);
	}
	public String getNotes() {
		return notes.get();
	}
	public void setNotes(String n) {
		notes.set(n);
	}
	public String getStatus() {
		return status.get();
	}
	public void setStatus(String s) {
		status.set(s);
	}

}