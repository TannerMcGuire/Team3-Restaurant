package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class VendorTableModel {
	private final SimpleStringProperty vendorId;
	private final SimpleStringProperty name;
	private final SimpleStringProperty phoneNumber;
	private final SimpleStringProperty status;

	// ----------------------------------------------------------------------------
	public VendorTableModel(Vector<String> vendorData) {
		vendorId = new SimpleStringProperty(vendorData.elementAt(0));
		name = new SimpleStringProperty(vendorData.elementAt(1));
		phoneNumber = new SimpleStringProperty(vendorData.elementAt(2));
		status = new SimpleStringProperty(vendorData.elementAt(3));
	}

	public String getVendorId() {
		return vendorId.get();
	}

	public void setVendorId(String num) {
		vendorId.set(num);
	}
	
	public String getName() {
		return name.get();
	}

	public void setName(String vname) {
		name.set(vname);
	}
	
	public String getPhoneNumber() {
		return phoneNumber.get();
	}

	public void setPhoneNumber(String num) {
		phoneNumber.set(num);
	}
	
	public String getStatus() {
		return status.get();
	}

	public void setStatus(String a) {
		status.set(a);
	}

}
