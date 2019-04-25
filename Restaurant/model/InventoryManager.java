package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;

import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

public class InventoryManager implements IView, IModel {

	// Impresario
	private Properties dependencies;
	private ModelRegistry myRegistry;

	private Vendor myVendor;
	private InventoryItemType myIIT;
	private InventoryItem myInventoryItem;
	private VendorCollection accounts;
	private Vendor selectedAccount;

	public static String history = "";
	// GUI
	private Hashtable<String, Scene> myViews;
	private Stage myStage;

	// constructor for this class
	// ----------------------------------------------------------
	public InventoryManager() {
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

		// STEP 3.1: Create the Registry object - if you inherit from
		// EntityBase, this is done for you. Otherwise, you do it yourself
		myRegistry = new ModelRegistry("InventoryManager");
		if (myRegistry == null) {
			new Event(Event.getLeafLevelClassName(this), "InventoryManager", "Could not instantiate Registry",
					Event.ERROR);
		}
		// STEP 3.2: Be sure to set the dependencies correctly
		setDependencies();
		// Set up the initial view
		createAndShowInventoryManagerView();
	}

	// Editable to match our system requirements
	// -----------------------------------------------------------------------------------
	private void setDependencies() {
		dependencies = new Properties();
		dependencies.setProperty("ModifyVIIT", "modifyVIIT");
		dependencies.setProperty("Withdraw", "TransactionError");
		dependencies.setProperty("Transfer", "TransactionError");
		dependencies.setProperty("BalanceInquiry", "TransactionError");
		dependencies.setProperty("ImposeServiceCharge", "TransactionError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * Method called from client to get the value of a particular field held by the
	 * objects encapsulated by this object.
	 *
	 * @param key Name of database column (field) for which the client wants the
	 *            value
	 *
	 * @return Value associated with the field
	 */
	// Example for later
	// ----------------------------------------------------------
	public Object getState(String key) {
		if (key.equals("his") == true) {
			return history;
		}
//		else
//		if (key.equals("Name") == true)
//		{
//			if (myAccountHolder != null)
//			{
//				return myAccountHolder.getState("Name");
//			}
//			else
//				return "Undefined";
//		}
		else
			return "";
	}

	// ----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		// STEP 4: Write the sCR method component for the key you
		// just set up dependencies for
		// DEBUG System.out.println("Librarian.sCR: key = " + key);
		// This is where GUI popups change!!!
		if (key.equals("InventoryManagerView") == true) {
			createAndShowInventoryManagerView();
		} else if (key.equals("InventoryItemTypeView") == true) {
			if (((String) value).equals("addIIT") == true) {
				history = "addIIT";
			} else if (((String) value).equals("update") == true) {
				history = "update";
			} else if (((String) value).equals("deleteIIT") == true) {
				history = "deleteIIT";
			}
			createAndShowInventoryItemTypeView();
		} else if (key.equals("MODIFY INVENTORY ITEM TYPE")) {
			history = "";
			createAndShowEnterInventoryItemTypeNameAndNotesScreen();

		} else if (key.equals("SUBMIT IIT NAME AND NOTES")) {
			createAndShowInventoryItemTypeSelectionScreen(((Properties) value).getProperty("ItemTypeName"),
					((Properties) value).getProperty("Notes"));
		} else if (key.equals("BACK")) {
			createAndShowInventoryManagerView();
		} else if (key.equals("VendorInventoryItemTypeView") == true) {
			createAndShowVendorInventoryItemTypeView();
		} else if (key.equals("VendorView") == true) {
			if ((String) value == "modifyVendor") {
				history = (String) value;
				createAndShowVendorView();
			} else if ((String) value == "addVendor") {
				history = (String) value;
				createAndShowVendorView();
			}
		} else if (key.equals("ModifyVendorView") == true) {
			try {
				modifyVendor((Properties) value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (key.equals("VendorInfo") == true) {
			if (value != null) {
				boolean flag = vendorFolder((Properties) value);
				if (flag == true) {
					try {
						searchVendor((Properties) value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (key.equals("IITInfo") == true) {
			if (value != null) {
				boolean flag = inventoryItemTypeFolder((Properties) value);
				if (flag == true) {
					try {
						searchIIT((Properties) value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (key.equals("IITdelete") == true) {
			if (value != null) {
				boolean flag = inventoryItemTypeFolder((Properties) value);
				if (flag == true) {
					try {
						searchIIT((Properties) value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (key.equals("SubmitBarcodeView") == true) {
			if ((String) value == "removeItem") {
				history = (String) value;
				createAndShowSubmitBarcodeView();
			} /*else if ((String) value == "addVendor") {
				history = (String) value;
				createAndShowVendorView();
			}  UPDATE WITH MODIFY ITEM STATUS */

			myRegistry.updateSubscribers(key, this);
		} else if (key.equals("BarcodeSearch") == true) {
			if (value != null) {
				boolean flag = inventoryItemFolder((Properties) value);
				if (flag == true) {
					try {
						searchInventoryItem((Properties) value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// ----------------------------------------------------------
	public boolean vendorFolder(Properties props) {
		try {
			myVendor = new Vendor(props);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// ----------------------------------------------------------
	public boolean inventoryItemTypeFolder(Properties props) {
		try {
			myIIT = new InventoryItemType(props);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean inventoryItemFolder(Properties props) {
		try {
			myInventoryItem = new InventoryItem(props);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/** Called via the IView relationship */
	// ----------------------------------------------------------
	public void updateState(String key, Object value) {
		// DEBUG System.out.println("Librarian.updateState: key: " + key);

		stateChangeRequest(key, value);
	}

	// ----------------------------------------------------------
	private void searchVendor(Properties vend) throws Exception {
		VendorCollection vc = new VendorCollection();
		vc.setManager(this);
		vc.findVendor(vend);
		createAndShowVendorCollectionView(vc);
	}

	// ----------------------------------------------------------
	private void modifyVendor(Properties vend) throws Exception {
		Vendor vendor = new Vendor(vend);
		createAndShowModifyVendorView(vendor);
	}

	// ----------------------------------------------------------
	private void searchIIT(Properties item) throws Exception {
		InventoryItemTypeCollection iit = new InventoryItemTypeCollection();
		iit.findInventoryItemType(item);
		iit.createAndShowView();
	}

	private void searchInventoryItem(Properties item) throws Exception {  //FINISH THIS

		createAndShowConfirmInventoryItemRemovalView (item);
	}

	// ------------------------------------------------------------
	private void createAndShowInventoryManagerView() {
		Scene currentScene = (Scene) myViews.get("InventoryManagerView");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("InventoryManagerView", this);
			currentScene = new Scene(newView);
			myViews.put("InventoryManagerView", currentScene);
		}

		swapToView(currentScene);

	}

	// ------------------------------------------------------------

	private void createAndShowDeleteIITView() {
		Scene localScene = myViews.get("InventoryItemTypeDeleteView");
		if (localScene == null) {
			// create our new view
			View newView = ViewFactory.createView("InventoryItemTypeDeleteView", this);
			localScene = new Scene(newView);
			myViews.put("InventoryItemTypeDeleteView", localScene);
		}

		swapToView(localScene);
	}

	// ------------------------------------------------------------
	private void createAndShowEnterInventoryItemTypeNameAndNotesScreen() {
		// LOAD THE "Enter Inventory Item Type Name and Notes Screen"

		Scene currentScene = (Scene) myViews.get("EnterInventoryItemTypeAndNotesScreen");

		if (currentScene == null) {

			View newView = ViewFactory.createView("EnterInventoryItemTypeAndNotesScreen", this);
			currentScene = new Scene(newView);
			myViews.put("EnterInventoryItemTypeAndNotesScreen", currentScene);
		}

		swapToView(currentScene);
	}

	// ------------------------------------------------------------
	private void createAndShowInventoryItemTypeSelectionScreen(String itemTypeName, String notes) {
		// LOAD THE "Inventory Item Type Selection Screen"

		InventoryItemTypeCollection inventoryItemTypeCollection = new InventoryItemTypeCollection();

		inventoryItemTypeCollection.subscribe("MODIFY IIT", this);
		inventoryItemTypeCollection.subscribe("Submit new IIT info", this);
		inventoryItemTypeCollection.subscribe("BACK", this);

		try {
			if (itemTypeName != null && notes != null) {
				inventoryItemTypeCollection.findInventoryItemTypesWithNameAndNotesLike(itemTypeName, notes);
				inventoryItemTypeCollection.createAndShowInventoryItemTypeSelectionScreens();
			} else if (itemTypeName != null) {
				inventoryItemTypeCollection.findInventoryItemTypesWithNameLike(itemTypeName);
				inventoryItemTypeCollection.createAndShowInventoryItemTypeSelectionScreens();
			} else if (notes != null) {
				inventoryItemTypeCollection.findInventoryItemTypesWithNotesLike(notes);
				inventoryItemTypeCollection.createAndShowInventoryItemTypeSelectionScreens();
			}
		} catch (InvalidPrimaryKeyException e) {

		}

	}

	// ------------------------------------------------------------
	private void createAndShowVendorView() {
		Scene currentScene = (Scene) myViews.get("VendorView");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("VendorView", this);
			currentScene = new Scene(newView);
			myViews.put("VendorView", currentScene);
		}

		swapToView(currentScene);

	}

	// ------------------------------------------------------
	protected void createAndShowVendorCollectionView(VendorCollection v) {

		Scene localScene = myViews.get("VendorCollectionView");

		if (localScene == null) {
			// create our new view
			View newView = ViewFactory.createView("VendorCollectionView", v);
			localScene = new Scene(newView);
			myViews.put("VendorCollectionView", localScene);
		}
		// make the view visible by installing it into the frame
		swapToView(localScene);

	}

	// ------------------------------------------------------------

	private void createAndShowModifyVendorView(Vendor vend) {
		Scene currentScene = (Scene) myViews.get("ModifyVendorView");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("ModifyVendorView", vend);
			currentScene = new Scene(newView);
			myViews.put("ModifyVendorView", currentScene);
		}

		swapToView(currentScene);
	}

	// ------------------------------------------------------------
	private void createAndShowInventoryItemTypeView() {
		Scene currentScene = (Scene) myViews.get("InventoryItemTypeView");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("InventoryItemTypeView", this);
			currentScene = new Scene(newView);
			myViews.put("InventoryItemTypeView", currentScene);
		}

		swapToView(currentScene);

	}

	// ----------------------------------------------------------
	private void createAndShowVendorInventoryItemTypeView() {
		Scene currentScene = (Scene) myViews.get("VendorInventoryItemTypeView");

		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("VendorInventoryItemTypeView", this);
			currentScene = new Scene(newView);
			myViews.put("VendorInventoryItemTypeView", currentScene);
		}

		// make the view visible by installing it into the frame
		swapToView(currentScene);

	}
	// ----------------------------------------------------------
	private void createAndShowSubmitBarcodeView() {
		Scene currentScene = (Scene) myViews.get("SubmitBarcodeView");
		if (currentScene == null) {
			// create our new view
			View newView = ViewFactory.createView("SubmitBarcodeView", this);
			currentScene = new Scene(newView);
			myViews.put("SubmitBarcodeView", currentScene);
		}

		swapToView(currentScene);
	}

	private void createAndShowConfirmInventoryItemRemovalView (Properties item) {			//FINISH THIS
		Scene currentScene = (Scene) myViews.get("ConfirmInventoryItemRemovalView ");
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView("ConfirmInventoryItemRemovalView ", this);
			currentScene = new Scene(newView);
			myViews.put("ConfirmInventoryItemRemovalView ", currentScene);
		}

		swapToView(currentScene);
	}

	// -----------------------------------------------------------------------------
	public void swapToView(Scene newScene) {

		if (newScene == null) {
			System.out.println("InventoryManager.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView", "Missing view for display ", Event.ERROR);
			return;
		}

		myStage.setScene(newScene);
		myStage.sizeToScene();

		// Place in center
		WindowPosition.placeCenter(myStage);

	}

	// -----------------------------------------------------------------------------
	public void subscribe(String key, IView subscriber) {
		myRegistry.subscribe(key, subscriber);

	}

	// -----------------------------------------------------------------------------
	public void unSubscribe(String key, IView subscriber) {
		myRegistry.unSubscribe(key, subscriber);

	}

}