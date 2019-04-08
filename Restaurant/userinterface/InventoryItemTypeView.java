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
import model.InventoryItemType;
import model.Vendor;

import java.util.Properties;

// project imports
import impresario.IModel;

/** The class containing the Vendor View for the Restaurant application */
//==============================================================
public class InventoryItemTypeView extends View {

	// GUI components
	protected TextField name;
	protected TextField notes;
	protected ComboBox<String> activeInactive;

	protected Button doneButton; // doneButton
	protected Button submitButton; // new button

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	// ----------------------------------------------------------
	public InventoryItemTypeView(IModel account) {
		super(account, "IITView");

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

		Text prompt = new Text("Please Enter Inventory Item Information");
		prompt.setWrappingWidth(400);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 0, 2, 1);

		Text nameLabel = new Text(" Item Type Name : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		nameLabel.setFont(myFont);
		nameLabel.setWrappingWidth(150);
		nameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(nameLabel, 0, 1);

		name = new TextField();
		name.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(name, 1, 1);

		Text noteLabel = new Text(" Notes : ");
		noteLabel.setFont(myFont);
		noteLabel.setWrappingWidth(150);
		noteLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(noteLabel, 0, 2);

		notes = new TextField();
		notes.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(notes, 1, 2);

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
				//myModel.stateChangeRequest("InventoryManagerView", null);
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

		String nameEntered = name.getText();
		String notesEntered = notes.getText();
		
		
		if (((nameEntered == "") || (nameEntered.length() == 0)) 
				&& ((notesEntered == "") || (notesEntered.length() == 0))) {
			displayErrorMessage("Please enter a valid item name or notes");
			name.requestFocus();
		} else {
			processIIT(nameEntered, notesEntered);
		}
	}

	private void processIIT(String nameString, String noteString) {
		Properties props = new Properties();
		props.setProperty("ItemTypeName", nameString);
		props.setProperty("Notes", noteString);

		// clear fields for next time around
		name.setText("");
		notes.setText("");

		myModel.stateChangeRequest("IITInfo", props);
	}

	@Override
	public void updateState(String key, Object value) {

	}

}