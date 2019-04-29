// specify the package
package userinterface;

// system imports
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
import model.InventoryItemType;
import model.Vendor;
import model.VendorInventoryItemType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import exception.InvalidPrimaryKeyException;
// project imports
import impresario.IModel;

/** The class containing the Vendor View for the Restaurant application */
//==============================================================
public class ProcessInvoiceView extends View {

	// GUI components
	protected TextField name;
	protected TextField barcode;
	protected TextField notes;
	protected TextField date;
	protected TextField dateUsed;
	

	protected Button doneButton; // doneButton
	protected Button submitButton; // new button

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	// ----------------------------------------------------------
	public ProcessInvoiceView(IModel account) {
		super(account, "ProcessInvoiceView");

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

		Text prompt = new Text("Please Inventory Item Information From Invoice");
		prompt.setWrappingWidth(400);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 0, 2, 1);

		//------------------------------------------------------
		
		Text nameLabel = new Text(" Inventory Item Type Name : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		nameLabel.setFont(myFont);
		nameLabel.setWrappingWidth(150);
		nameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(nameLabel, 0, 1);

		name = new TextField();
		grid.add(name, 1, 1);
		
		//------------------------------------------------------

		Text barcodeLabel = new Text(" Item Barcode : ");
		barcodeLabel.setFont(myFont);
		barcodeLabel.setWrappingWidth(150);
		barcodeLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(barcodeLabel, 0, 2);

		barcode = new TextField();
		barcode.setDisable(true);
		grid.add(barcode, 1, 2);
		
		//------------------------------------------------------
		Text dateRecievedLabel = new Text(" Date Recieved : ");
		dateRecievedLabel.setFont(myFont);
		dateRecievedLabel.setWrappingWidth(150);
		dateRecievedLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(dateRecievedLabel, 0, 3);

		date = new TextField();
		date.setDisable(true);
		grid.add(date, 1, 3);
		
		//------------------------------------------------------
		
		Text dateUsedLabel = new Text(" Date of Last Use : ");
		dateUsedLabel.setFont(myFont);
		dateUsedLabel.setWrappingWidth(150);
		dateUsedLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(dateUsedLabel, 0, 4);

		dateUsed = new TextField();
		dateUsed.setDisable(true);
		grid.add(dateUsed, 1, 4);
		
		//------------------------------------------------------
		
		Text notesLabel = new Text(" Notes : ");
		notesLabel.setFont(myFont);
		notesLabel.setWrappingWidth(150);
		notesLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(notesLabel, 0, 5);

		notes = new TextField();
		notes.setDisable(true);
		grid.add(notes, 1, 5);
		
		//------------------------------------------------------

		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);
		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				
				processAction(e);
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

		//------------------------------------------------------
		
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
	
	boolean allowed = false;
	Vendor temp = (Vendor) myModel.getState("selectedVendor");
	String vendorID = (String) temp.getState("Id");
	
	public void processAction(Event evt) {
		
		if (!allowed) { //enter name, check to see if ITT is allowed by vendor
			if ((name.getText() == "") || (name.getText().length() == 0)) {
				displayErrorMessage("Please enter an Inventory Item Type Name name");
				name.requestFocus();
			}
			else {
				try {
					new InventoryItemType(name.getText());
					allowed = true;
					try {
						new VendorInventoryItemType(vendorID, name.getText());
					} catch (InvalidPrimaryKeyException e) {
						displayErrorMessage("Entered item is not carried by vendor.");
						allowed = false;
						e.printStackTrace();
					}
				} catch (InvalidPrimaryKeyException e) {
					displayErrorMessage("Entered item is not carried by store.");
					allowed = false;
					e.printStackTrace();
				}
			}

			if(allowed) {
				displayMessage("Enter item information");
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
				LocalDate now = LocalDate.now();
				String currentDate = dtf.format(now);
				date.setText(currentDate);
				dateUsed.setText(currentDate);
				
				name.setDisable(true);
				barcode.setDisable(false);
				date.setDisable(false);
				dateUsed.setDisable(false);
				notes.setDisable(false);
				barcode.requestFocus();
			}
//			else {
//				displayErrorMessage("Please enter a valid Inventory Item Type Name.");
//			}
		}
		
		else { //ITT is allowed, enter remaining information for inventory item
			clearErrorMessage();
			name.setDisable(true);
			String nameEntered = name.getText();
			String barcodeEntered = barcode.getText();
			String notesEntered = notes.getText();
			

			if (barcodeEntered.length() != 9 || !barcodeEntered.matches("[0-9]+")) {
				displayErrorMessage("Please enter a valid barcode");
				barcode.requestFocus();
			}
			
			else if(!checkDate(date.getText())) {
				displayErrorMessage("Please enter a valid date");
				date.requestFocus();
			}
			
			else if(!checkDate(dateUsed.getText())) {
				displayErrorMessage("Please enter a valid date");
				dateUsed.requestFocus();
			}
			
			else {
				Properties props = new Properties();
				
				props.setProperty("Barcode", barcodeEntered);
				props.setProperty("InventoryItemTypeName", nameEntered);
				props.setProperty("VendorId", vendorID);
				props.setProperty("DateReceived", date.getText());
				props.setProperty("DateOfLastUse", dateUsed.getText());
				props.setProperty("Notes", notesEntered);
				props.setProperty("Status", "Available");
				
				InventoryItem newItem = new InventoryItem(props);
				newItem.update();
				displayMessage("Successfully added item!");
				
				name.clear();
				barcode.clear();
				date.clear();
				dateUsed.clear();
				notes.clear();
				name.setDisable(false);
				barcode.setDisable(true);
				date.setDisable(true);
				dateUsed.setDisable(true);
				notes.setDisable(true);
				allowed = false;
			
			}
		}
	}

	
	@Override
	public void updateState(String key, Object value) {

		
	}
	
	public boolean checkDate(String check) {
		if(check.length() != 10) return false;
		if(!check.matches("^[0-9-]*$")) return false;
		for(int i = 0; i < check.length(); i++) {
			if((i==2||i==5) && check.charAt(i) != '-')
				return false;
		}
		return true;
	}

}