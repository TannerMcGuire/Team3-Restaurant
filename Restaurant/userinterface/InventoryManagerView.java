// specify the package
package userinterface;

// system imports
import java.text.NumberFormat;
import java.util.Properties;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

// project imports
import impresario.IModel;

/** The class containing the Teller View for the ATM application */
//==============================================================
public class InventoryManagerView extends View {

	// GUI stuff
	private Button addIITButton;
	private Button updateIITButton;
	private Button deleteIITButton;
	private Button addVendorButton;
	private Button modifyVendorButton;
	private Button addVIITButton;
	private Button deleteVIITButton;
	private Button processButton;
	private Button removeItemButton;
	private Button modifyIIstatusButton;
	private Button reorderButton;
	private Button inventoryButton;
	private Button doneButton;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	// ----------------------------------------------------------
	public InventoryManagerView(IModel manager) {

		super(manager, "InventoryManagerView");

		// create a container for showing the contents
		VBox container = new VBox(10);

		container.setPadding(new Insets(15, 5, 5, 5));

		// create a Node (Text) for showing the title
		container.getChildren().add(createTitle());

		// create a Node (GridPane) for showing data entry fields
		container.getChildren().add(createFormContents());

		// Error message area
		container.getChildren().add(createStatusLog("                          "));

		getChildren().add(container);

		populateFields();

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("VendorView", this);
		myModel.subscribe("VendorCollectionView", this);
		myModel.subscribe("InventoryManager", this);
		myModel.subscribe("InventoryItemTypeView", this);
		myModel.subscribe("InventoryItemTypeCollectionView", this);
	}

	// Create the label (Text) for the title of the screen
	// -------------------------------------------------------------
	private Node createTitle() {

		Text titleText = new Text(" 	    Restaurant Inventory	 ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);

		return titleText;
	}

	// Create the main form contents
	// -------------------------------------------------------------
	private GridPane createFormContents() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		//grid.setHgap(10);
		//grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		// data entry fields
		addIITButton = new Button("Add Inventory\nItem Type");
		//addIITButton.setMinWidth(255);
		addIITButton.setMinSize(125, 125);
		addIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("InventoryItemTypeView", "addIIT");
			}
		});
		//grid.add(addIITButton, 1, 0);
		
		updateIITButton = new Button("Update Inventory\nItem Type");
		//updateIITButton.setMinWidth(255);
		updateIITButton.setMinSize(125, 125);
		updateIITButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("InventoryItemTypeView", "updateIIT");
			}
		});
		//grid.add(updateIITButton, 1, 1);
		
		deleteIITButton = new Button("Delete Inventory\nItem Type");
		//deleteIITButton.setMinWidth(255);
		deleteIITButton.setMinSize(125, 125);
		deleteIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("InventoryItemTypeView", "deleteIIT");
			}
		});
		//grid.add(deleteIITButton, 1, 2);
		
		addVendorButton = new Button("Add Vendor");
		//addVendorButton.setMinWidth(255);
		addVendorButton.setMinSize(125, 125);
		addVendorButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("VendorView", "addVendor");
			}
		});
		//grid.add(addVendorButton, 1, 3);
		
		modifyVendorButton = new Button("Modify Vendor");
		//modifyVendorButton.setMinWidth(255);
		modifyVendorButton.setMinSize(125, 125);
		modifyVendorButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("VendorView", "modifyVendor");
			}
		});
		//grid.add(modifyVendorButton, 1, 4);
		
		addVIITButton = new Button("Add Vendor\nInventory\nItem Type");
		//addVIITButton.setMinWidth(255);
		addVIITButton.setMinSize(125, 125);
		addVIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("VendorInventoryItemTypeView", "addVIIT");
			}
		});
		//grid.add(addVIITButton, 1, 5);
		
		deleteVIITButton = new Button("Delete Vendor\nInventory\nItem Type");
		//deleteVIITButton.setMinWidth(255);
		deleteVIITButton.setMinSize(125, 125);
		deleteVIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("VendorInventoryItemTypeView", "deleteVIIT");
			}
		});
		//grid.add(deleteVIITButton, 1, 6);
		
		processButton = new Button("Process Invoice");
		//processButton.setMinWidth(255);
		processButton.setMinSize(125, 125);
		processButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		//grid.add(processButton, 1, 7);
		
		removeItemButton = new Button("Remove Item");
		//removeItemButton.setMinWidth(255);
		removeItemButton.setMinSize(125, 125);
		removeItemButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
			myModel.stateChangeRequest("SubmitBarcodeView", "removeItem");
			}
		});
		//grid.add(removeItemButton, 1, 8);
		
		modifyIIstatusButton = new Button("Modify Inventory\nItem Status");
		//modifyIIstatusButton.setMinWidth(255);
		modifyIIstatusButton.setMinSize(125, 125);
		modifyIIstatusButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("SubmitBarcodeView", "modifyItem");
			}
		});
		//grid.add(modifyIIstatusButton, 1, 9);
		
		reorderButton = new Button("Reorder List");
		//reorderButton.setMinWidth(255);
		reorderButton.setMinSize(125, 125);
		reorderButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		//grid.add(reorderButton, 1, 10);
		
		inventoryButton = new Button("View Inventory");
		//inventoryButton.setMinWidth(255);
		inventoryButton.setMinSize(125, 125);
		inventoryButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("full", "inventory");
			}
		});
		//grid.add(inventoryButton, 1, 11);
		
		doneButton = new Button("DONE");
		doneButton.setTextFill(Color.WHITE);
		//doneButton.setMinWidth(255);
		doneButton.setMinSize(500, 75);
		doneButton.setStyle("-fx-background-color: black");
		doneButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});
		//grid.add(doneButton, 1, 12);

		VBox btnContainer = new VBox(10);
		HBox btnContainer1 = new HBox(0);
		HBox btnContainer2 = new HBox(0);
		HBox btnContainer3 = new HBox(0);
		HBox btnContainer4 = new HBox(0);
		btnContainer.setAlignment(Pos.BOTTOM_CENTER);
		btnContainer1.getChildren().add(addIITButton);
		btnContainer1.getChildren().add(updateIITButton);
		btnContainer1.getChildren().add(deleteIITButton);
		btnContainer1.getChildren().add(addVendorButton);
		btnContainer2.getChildren().add(modifyVendorButton);
		btnContainer2.getChildren().add(addVIITButton);
		btnContainer2.getChildren().add(deleteVIITButton);
		btnContainer2.getChildren().add(processButton);
		btnContainer3.getChildren().add(removeItemButton);
		btnContainer3.getChildren().add(modifyIIstatusButton);
		btnContainer3.getChildren().add(reorderButton);
		btnContainer3.getChildren().add(inventoryButton);
		btnContainer4.getChildren().add(doneButton);
		grid.add(btnContainer1, 0, 1);
		grid.add(btnContainer2, 0, 2);
		grid.add(btnContainer3, 0, 3);
		grid.add(btnContainer4, 0, 4);
		
		grid.setMinSize(500, 500);
		return grid;
	}

	// Create the status log field
	// -------------------------------------------------------------
	private MessageView createStatusLog(String initialMessage) {

		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	// -------------------------------------------------------------
	public void populateFields() {

	}

	// This method processes events generated from our GUI components.
	// Make the ActionListeners delegate to this method
	// -------------------------------------------------------------
	public void processAction(Event evt) {
//		// DEBUG: System.out.println("TellerView.actionPerformed()");
//
//		clearErrorMessage();
//
		if(evt.getSource() == processButton) {
			myModel.stateChangeRequest("VendorView", "processInvoice");
		}
		
		if(evt.getSource() == reorderButton) {
			myModel.stateChangeRequest("reorder", null);
		}

	}

	/**
	 * Process userid and pwd supplied when done button is hit. Action is to pass
	 * this info on to the teller object
	 */
	// ----------------------------------------------------------
	private void processUserIDAndPassword(String useridString, String passwordString) {
//		Properties props = new Properties();
//		props.setProperty("ID", useridString);
//		props.setProperty("Password", passwordString);
//
//		// clear fields for next time around
//		userid.setText("");
//		password.setText("");
//
//		myModel.stateChangeRequest("Login", props);
	}

	// ---------------------------------------------------------
	public void updateState(String key, Object value) {
		// STEP 6: Be sure to finish the end of the 'perturbation'
		// by indicating how the view state gets updated.
		if (key.equals("LoginError") == true) {
			// display the passed text
			displayErrorMessage((String) value);
		}

	}

	/**
	 * Display error message
	 */
	// ----------------------------------------------------------
	public void displayErrorMessage(String message) {
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Clear error message
	 */
	// ----------------------------------------------------------
	public void clearErrorMessage() {
		statusLog.clearErrorMessage();
	}

}
