package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model) {
		if (viewName.equals("InventoryManagerView") == true) {
			return new InventoryManagerView(model);
		} else if (viewName.equals("VendorInventoryItemTypeView") == true) {
			return new VendorInventoryItemTypeView(model);
		} else if (viewName.equals("VendorInventoryItemTypeCollectionView") == true) {
			return new VendorInventoryItemTypeCollectionView(model);
		} else if (viewName.equals("VendorCollectionView") == true) {
			return new VendorCollectionView(model);
		} else if (viewName.equals("VendorView") == true) {
			return new VendorView(model);
		} else if (viewName.equals("ModifyVendorView") == true) {
			return new ModifyVendorView(model);
		} else if (viewName.equals("InventoryItemTypeView") == true) {
			return new InventoryItemTypeView(model);
		} else if (viewName.equals("InventoryItemTypeCollectionView") == true) {
			return new InventoryItemTypeCollectionView(model);
		} else if (viewName.equals("InventoryItemTypeCollectionView2") == true) {
			return new InventoryItemTypeCollectionView2(model);
		} else if (viewName.equals("ProcessInvoiceView")) {
			return new ProcessInvoiceView(model);
		} else if (viewName.equals("VIITPriceView") == true) {
			return new PriceView(model);
		} else if (viewName.equals("Yay") == true) {
			return new YayView(model);
		} else if (viewName.equals("InventoryItemTypeSelectionScreen")) {
			return new InventoryItemTypeSelectionScreen(model);
		} else if (viewName.equals("EnterInventoryItemTypeAndNotesScreen")) {
			return new EnterInventoryItemTypeAndNotesScreen(model);
		} else if (viewName.equals("InventoryItemTypeChangesScreen")) {
			return new InventoryItemTypeChangesScreen(model);
		} else if (viewName.equals("SubmitBarcodeView") == true) {
			return new SubmitBarcodeView(model);
		} else if (viewName.equals("ConfirmInventoryItemRemovalView") == true) {
			return new ConfirmInventoryItemRemovalView(model);
		} else if (viewName.equals("ModifyInventoryItemView") == true) {
			return new ModifyInventoryItemView(model);
		} else if (viewName.equals("InventoryItemCollectionView") == true) {
			return new InventoryItemCollectionView(model);
		} return null;
	}

	/*
	 * public static Vector createVectorView(String viewName, IModel model) {
	 * if(viewName.equals("SOME VIEW NAME") == true) { //return [A NEW VECTOR VIEW
	 * OF THAT NAME TYPE] } else return null; }
	 */

}