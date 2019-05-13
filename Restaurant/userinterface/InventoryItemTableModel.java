package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class InventoryItemTableModel
{
	private final SimpleStringProperty Barcode, InventoryItemTypeName, VendorId, 
		DateRecieved, dateOfLastUse,  status, notes;

	// ----------------------------------------------------------------------------
	public InventoryItemTableModel(Vector<String> vendorData) {
		Barcode = new SimpleStringProperty(vendorData.elementAt(0));
		InventoryItemTypeName = new SimpleStringProperty(vendorData.elementAt(1));
		VendorId = new SimpleStringProperty(vendorData.elementAt(2));
		DateRecieved = new SimpleStringProperty(vendorData.elementAt(3));
		dateOfLastUse = new SimpleStringProperty(vendorData.elementAt(4));
		status = new SimpleStringProperty(vendorData.elementAt(5));
		notes = new SimpleStringProperty(vendorData.elementAt(6));
		
	}

	public String getBarcode()
	{
		return Barcode.get();
	}

	public String getInventoryItemTypeName()
	{
		return InventoryItemTypeName.get();
	}

	public String getVendorId()
	{
		return VendorId.get();
	}

	public String getDateRecieved()
	{
		return DateRecieved.get();
	}

	public String getDateOfLastUse()
	{
		return dateOfLastUse.get();
	}

	public String getStatus()
	{
		return status.get();
	}

	public String getNotes()
	{
		return notes.get();
	}
}
