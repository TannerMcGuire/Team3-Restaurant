package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model) {
		if (viewName.equals("InventoryManagerView") == true) {
			return new InventoryManagerView(model);
		} else if (viewName.equals("VendorInventoryItemTypeView") == true) {
			return new VendorInventoryItemTypeView(model);
		} else if (viewName.equals("VendorCollectionView") == true) {
			return new VendorCollectionView(model);
		} else if (viewName.equals("VendorView") == true) {
			return new VendorView(model);
		}
		return null;
	}

	/*
	 * public static Vector createVectorView(String viewName, IModel model) {
	 * if(viewName.equals("SOME VIEW NAME") == true) { //return [A NEW VECTOR VIEW
	 * OF THAT NAME TYPE] } else return null; }
	 */

}
