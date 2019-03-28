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
	private TextField userid;
	private PasswordField password;
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
		myModel.subscribe("", this);
	}

	// Create the label (Text) for the title of the screen
	// -------------------------------------------------------------
	private Node createTitle() {

		Text titleText = new Text("       Restaurant Inventory          ");
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
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		// data entry fields
		addIITButton = new Button("Add Inventory Item Type");
		addIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(addIITButton, 1, 0);
		
		updateIITButton = new Button("Update Inventory Item Type");
		updateIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(updateIITButton, 1, 1);
		
		deleteIITButton = new Button("Delete Inventory Item Type");
		deleteIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(deleteIITButton, 1, 2);
		
		addVendorButton = new Button("Add Vendor");
		addVendorButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(addVendorButton, 1, 3);
		
		modifyVendorButton = new Button("Modify Vendor");
		modifyVendorButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(modifyVendorButton, 1, 4);
		
		addVIITButton = new Button("Add Vendor Inventory Item Type");
		addVIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(addVIITButton, 1, 5);
		
		deleteVIITButton = new Button("Delete Vendor Inventory Item Type");
		deleteVIITButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(deleteVIITButton, 1, 6);
		
		processButton = new Button("Process Invoice");
		processButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(processButton, 1, 7);
		
		removeItemButton = new Button("Remove Item");
		removeItemButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(removeItemButton, 1, 8);
		
		modifyIIstatusButton = new Button("Modify Inventory Item Status");
		modifyIIstatusButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(modifyIIstatusButton, 1, 9);
		
		reorderButton = new Button("Reorder List");
		reorderButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(reorderButton, 1, 10);
		
		inventoryButton = new Button("View Inventory");
		inventoryButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(inventoryButton, 1, 11);
		
		doneButton = new Button("DONE");
		doneButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(doneButton, 1, 12);

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
		// DEBUG: System.out.println("TellerView.actionPerformed()");

		clearErrorMessage();

		String useridEntered = userid.getText();

		if ((useridEntered == null) || (useridEntered.length() == 0)) {
			displayErrorMessage("Please enter a user id!");
			userid.requestFocus();
		} else {
			String passwordEntered = password.getText();
			processUserIDAndPassword(useridEntered, passwordEntered);
		}

	}

	/**
	 * Process userid and pwd supplied when done button is hit. Action is to pass
	 * this info on to the teller object
	 */
	// ----------------------------------------------------------
	private void processUserIDAndPassword(String useridString, String passwordString) {
		Properties props = new Properties();
		props.setProperty("ID", useridString);
		props.setProperty("Password", passwordString);

		// clear fields for next time around
		userid.setText("");
		password.setText("");

		myModel.stateChangeRequest("Login", props);
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
