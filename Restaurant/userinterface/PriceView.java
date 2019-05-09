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
import javafx.scene.control.ComboBox;
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
import model.InventoryItemTypeCollection;
import model.VendorCollection;
import model.VendorInventoryItemType;

import java.text.*;
import java.util.Date;
import java.util.Properties;

// project imports
import impresario.IModel;

/** The class containing the Vendor View for the Restaurant application */
//==============================================================
public class PriceView extends View {

	// GUI components
	protected TextField price;

	protected Button doneButton; // doneButton
	protected Button submitButton; // new button

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	// ----------------------------------------------------------
	public PriceView(IModel account) {
		super(account, "PriceView");

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

		Text prompt = new Text("Please Enter Vendor Information");
		prompt.setWrappingWidth(400);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 0, 2, 1);

		Text priceLabel = new Text(" Vendor Price : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		priceLabel.setFont(myFont);
		priceLabel.setWrappingWidth(150);
		priceLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(priceLabel, 0, 1);

		price = new TextField();
		price.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(price, 1, 1);

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

		String priceEntered = price.getText();

		if ((priceEntered == "") || (priceEntered.length() == 0)
				|| !(priceEntered.matches("^\\$?[0-9]+\\.[0-9][0-9]$"))) {
			displayErrorMessage("Please enter a valid price with decimal point");
			price.requestFocus();
		} else {
			processPrice(priceEntered);
		}
	}

	private void processPrice(String priceString) {
		Properties props = new Properties();
		props.setProperty("VendorId", (String) myModel.getState("id"));
		props.setProperty("InventoryItemTypeName", InventoryItemTypeCollection.iname);
		props.setProperty("VendorPrice", priceString);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		props.setProperty("DateofLastUpdate", dateFormat.format(date));

		// clear fields for next time around
		price.setText("");

		VendorInventoryItemType b = new VendorInventoryItemType(props);
		b.update();
		if (b.updateStatusMessage.equals("Error in inputting data in database!"))
			displayErrorMessage("Error in putting data in database");
		else
			displayMessage("Vendor-Inventory Item Type successfully added to database");
	}

	@Override
	public void updateState(String key, Object value) {

	}

}