package userinterface;

//system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import model.InventoryManager;

import java.util.Properties;
import java.util.Vector;
import database.*;

// project imports
import impresario.IModel;

public class VendorInventoryItemTypeView extends View{

	// GUI components
	protected TextField vendorName;
	protected TextField phoneNumber;

	protected Button backButton; // doneButton
	protected Button submitButton; // new button

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	// ----------------------------------------------------------
	public VendorInventoryItemTypeView(IModel item) {
		super(item, "VendorInventoryItemTypeView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a address for this panel
		container.getChildren().add(createTitle());

		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		populateFields();

		// myModel.subscribe("ServiceCharge", this);
		// myModel.subscribe("UpdateStatusMessage", this);
	}

	// Create the address container
	// -------------------------------------------------------------
	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);

		Text addressText = new Text(" Restaurant Inventory ");
		addressText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		addressText.setWrappingWidth(300);
		addressText.setTextAlignment(TextAlignment.CENTER);
		addressText.setFill(Color.DARKGREEN);
		container.getChildren().add(addressText);

		return container;
	}

	// Create the main form content
	// -------------------------------------------------------------
	private VBox createFormContent() {
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text prompt = new Text("VENDOR SEARCH");
		prompt.setWrappingWidth(400);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 0, 2, 1);

		Text vendorNameLabel = new Text(" Vendor Name : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		vendorNameLabel.setFont(myFont);
		vendorNameLabel.setWrappingWidth(160);
		vendorNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(vendorNameLabel, 0, 1);

		vendorName = new TextField();
		vendorName.setEditable(true);
		grid.add(vendorName, 1, 1);

		Text phoneNumberLabel = new Text(" Vendor Phone Number : ");
		phoneNumberLabel.setFont(myFont);
		phoneNumberLabel.setWrappingWidth(160);
		phoneNumberLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(phoneNumberLabel, 0, 2);

		phoneNumber = new TextField();
		phoneNumber.setEditable(true);
		grid.add(phoneNumber, 1, 2);

		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);
		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					processAction(e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttons.getChildren().add(submitButton);

		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("InventoryManagerView", null);
			}
		});
		buttons.getChildren().add(backButton);

		vbox.getChildren().add(grid);
		vbox.getChildren().add(buttons);

		return vbox;
	}

	// Create the status log field
	// -------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage) {
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	// -------------------------------------------------------------
	public void populateFields() {
		vendorName.setText("");
		phoneNumber.setText("");
	}

	// This method processes events generated from our GUI components.
	// Make the ActionListeners delegate to this method
	// -------------------------------------------------------------
	public void processAction(Event evt) {

		clearErrorMessage();

		String vendorEntered = vendorName.getText();
		String phoneEntered = phoneNumber.getText();

		if (((vendorEntered == null) || (vendorEntered.length() == 0))
				&& ((phoneEntered == null) || (phoneEntered.length() == 0))) {
			displayErrorMessage("Please enter a part of vendor name or vendor phone number");
			vendorName.requestFocus();
		} else
			processVendorInfo(vendorEntered, phoneEntered);
	}

	/**
	 * Process vendor name and phone number supplied when Submit button is hit. Action is to pass
	 * this info on to the manager object
	 */
//----------------------------------------------------------
	private void processVendorInfo(String vendorString, String phoneString) {
		
		Properties props = new Properties();
		props.setProperty("Name", vendorString);
		props.setProperty("PhoneNumber", phoneString);
		
		// clear fields for next time around
		vendorName.setText("");
		phoneNumber.setText("");

		myModel.stateChangeRequest("VendorInfo", props);
	}

	/**
	 * Update method
	 */
	// ---------------------------------------------------------

	public void updateState(String key, Object value) {
		clearErrorMessage();
		if (key.equals("ServiceCharge") == true) {

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
	 * Display info message
	 */
	// ----------------------------------------------------------
	public void displayMessage(String message) {
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	// ----------------------------------------------------------
	public void clearErrorMessage() {
		statusLog.clearErrorMessage();
	}
}
