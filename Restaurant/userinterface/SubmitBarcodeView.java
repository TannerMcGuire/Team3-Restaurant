package userinterface;

// system imports
import exception.InvalidPrimaryKeyException;
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
import model.InventoryItem;

import java.util.Properties;

// project imports
import impresario.IModel;
import sun.dc.pr.PRError;

/** The class containing the Submit Barcode View for the Restaurant application */
//==============================================================
public class SubmitBarcodeView extends View {

	// GUI components
	protected TextField barcode;

	protected Button doneButton; // doneButton
	protected Button submitButton; // new button

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	// ----------------------------------------------------------
	public SubmitBarcodeView(IModel account) {
		super(account, "SubmitBarcodeView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title for this panel
		container.getChildren().add(createTitle());

		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		myModel.subscribe("UpdateStatusMessage", this);
	}

	// Create the title container
	// -------------------------------------------------------------
	private Node createTitle() // DONE
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);

		Text titleText = new Text(" Restaurant Inventory ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);

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

		Text prompt = new Text("Please Enter Inventory Item Barcode");
		prompt.setWrappingWidth(400);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 0, 2, 1);

		Text barcodeLabel = new Text(" Barcode : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		barcodeLabel.setFont(myFont);
		barcodeLabel.setWrappingWidth(150);
		barcodeLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(barcodeLabel, 0, 1);

		barcode = new TextField();
		barcode.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(barcode, 1, 1);

		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);
		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
				/*if(myModel.getState("his") == "takeOutItem") {
					processAction(e);
				}
				if(myModel.getState("his") == "modifyStatus") {
					processSubmitModify(e);
				} */
			}
		});
		buttons.getChildren().add(submitButton);

		doneButton = new Button("Back");
		doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		doneButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage();
				new model.InventoryManager();
			}
		});
		buttons.getChildren().add(doneButton);

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
	// ----------------------------------------------------------
	public void processAction(Event evt) {

		clearErrorMessage();

		String barcodeEntered = barcode.getText();

		if ((barcodeEntered == "") || (barcodeEntered.length() != 9)){
			displayErrorMessage("Please enter a valid barcode");
			barcode.requestFocus();
		}
		else {
			processBarcode(barcodeEntered);
		}
	}

	private void processBarcode(String barcodeString) {

		int barcode = Integer.parseInt(barcodeString);

		try {
			InventoryItem inventoryItem = new InventoryItem(barcode);

			Properties props = new Properties();

			props.setProperty("Barcode", barcodeString);
			props.setProperty("InventoryItemTypeName", inventoryItem.getState("InventoryItemTypeName").toString());
			props.setProperty("VendorId", inventoryItem.getState("VendorId").toString());
			props.setProperty("DateReceived", inventoryItem.getState("DateReceived").toString());
			props.setProperty("DateOfLastUse", inventoryItem.getState("DateOfLastUse").toString());
			props.setProperty("Notes", inventoryItem.getState("Notes").toString());
			props.setProperty("Status", inventoryItem.getState("Status").toString());

			myModel.stateChangeRequest("BarcodeSearch", props);
		} catch (InvalidPrimaryKeyException e) {
			e.printStackTrace();
		}


	}
	
	/*private void processSubmitModify(Event evt) {
		
		clearErrorMessage();
		String barcodeEntered = barcode.getText();
		String phoneEntered = phoneNum.getText();
		
		if ((barcodeEntered == "") || (barcodeEntered.length() == 0) && 
			((phoneEntered == "") || (phoneEntered.length() < 12))) {
			displayErrorMessage("Please enter a valid vendor barcode or phone number");
			barcode.requestFocus();
		}
		else {
			processModify(barcodeEntered, phoneEntered);
		}
	}
	
	private void processModify(String barcode, String phone) {
		Properties props = new Properties();
		props.setProperty("Name", barcode);
		props.setProperty("PhoneNumber", phone);
		this.barcode.clear();
		phoneNum.clear();
		clearErrorMessage();
		myModel.stateChangeRequest("InventoryItemInfo", props);
	} */


	@Override
	public void updateState(String key, Object value) {

	}

}