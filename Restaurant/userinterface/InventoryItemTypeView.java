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
import model.InventoryItemTypeCollection;
import model.InventoryManager;
import model.Vendor;
import model.VendorCollection;
import model.VendorInventoryItemType;
import model.VendorInventoryItemTypeCollection;

import java.util.Properties;
import java.util.Vector;

import exception.InvalidPrimaryKeyException;
// project imports
import impresario.IModel;

/** The class containing the Vendor View for the Restaurant application */
//==============================================================
public class InventoryItemTypeView extends View {

	// GUI components
	protected TextField name;
	protected TextField unit;
	protected TextField measure;
	protected TextField days;
	protected TextField reorder;
	protected TextField notes;
	protected ComboBox<String> activeInactive;

	private String history = "";

	protected Button doneButton; // doneButton
	protected Button submitButton; // new button

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	// ----------------------------------------------------------
	public InventoryItemTypeView(IModel account) {
		super(account, "InventoryItemTypeView");

		history = (String) myModel.getState("his");
		// System.out.println(history + " iitv");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title for this panel
		container.getChildren().add(createTitle());

		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		myModel.subscribe("InventoryManager", this);
	}

	// Create the title container
	// -------------------------------------------------------------
	private Node createTitle() // DONE
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);

		Text titleText = new Text(" Restaurant Inventory ");// change
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
		grid.setMinSize(550, 460);
		grid.add(name, 1, 1);

		if (history.equals("addIIT")) {
			Text unitsLabel = new Text(" Units : ");
			unitsLabel.setFont(myFont);
			unitsLabel.setWrappingWidth(150);
			unitsLabel.setTextAlignment(TextAlignment.RIGHT);
			grid.add(unitsLabel, 0, 2);
			unit = new TextField();

			grid.add(unit, 1, 2);

			Text measureLabel = new Text(" Unit of Measure : ");
			measureLabel.setFont(myFont);
			measureLabel.setWrappingWidth(150);
			measureLabel.setTextAlignment(TextAlignment.RIGHT);
			grid.add(measureLabel, 0, 3);
			measure = new TextField();

			grid.add(measure, 1, 3);

			Text daysLabel = new Text(" Validity Days (-1 if not expiration): ");
			daysLabel.setFont(myFont);
			daysLabel.setWrappingWidth(150);
			daysLabel.setTextAlignment(TextAlignment.RIGHT);
			grid.add(daysLabel, 0, 4);
			days = new TextField();

			grid.add(days, 1, 4);

			Text orderLabel = new Text(" Reorder Point : ");
			orderLabel.setFont(myFont);
			orderLabel.setWrappingWidth(150);
			orderLabel.setTextAlignment(TextAlignment.RIGHT);
			grid.add(orderLabel, 0, 5);
			reorder = new TextField();

			grid.add(reorder, 1, 5);

			Text noteLabel = new Text(" Notes : ");
			noteLabel.setFont(myFont);
			noteLabel.setWrappingWidth(150);
			noteLabel.setTextAlignment(TextAlignment.RIGHT);
			grid.add(noteLabel, 0, 6);
			notes = new TextField();

			grid.add(notes, 1, 6);
		} else {
			Text noteLabel = new Text(" Notes : ");
			noteLabel.setFont(myFont);
			noteLabel.setWrappingWidth(150);
			noteLabel.setTextAlignment(TextAlignment.RIGHT);
			grid.add(noteLabel, 0, 2);
			notes = new TextField();

			grid.add(notes, 1, 2);
		}
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
				// myModel.stateChangeRequest("InventoryManagerView", null);
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
	// ----------------------------------------------------------
	public void processAction(Event evt) {

		clearErrorMessage();

		String nameEntered = name.getText();
		String notesEntered = notes.getText();

		if (history.equals("addIIT")) {
			String unitsEntered = unit.getText();
			String measureEntered = measure.getText();
			String daysEntered = days.getText();
			String orderEntered = reorder.getText();
			if ((nameEntered.equals("")) || (nameEntered.length() == 0) || (nameEntered.length() > 32)) {
				displayErrorMessage("Please enter a valid item name");
				name.requestFocus();
			} else if ((!(unitsEntered.matches("[0-9]+")) || Integer.parseInt(unitsEntered) < 0)) {
				displayErrorMessage("Please enter positive integer for units");
				unit.requestFocus();
			} else if ((measureEntered.equals("")) || (measureEntered.length() == 0) || (nameEntered.length() > 15)) {
				displayErrorMessage("Please enter type of measure ie bottle");
				measure.requestFocus();
			} else if ((daysEntered.equals("")) || !(daysEntered.matches("-?[1-9]\\d*"))
					|| (Integer.parseInt(daysEntered) < -1)) {
				displayErrorMessage("Please enter positive integer for validity of item");
				days.requestFocus();
			} else if ((orderEntered.equals("")) || !(orderEntered.matches("^\\$?[0-9]+\\.[0-9]+$"))
					|| (Double.parseDouble(orderEntered) < 0)) {
				displayErrorMessage("Please enter positive decimal for a reorder point");
				reorder.requestFocus();
			} else if ((notesEntered.length() > 30)) {
				displayErrorMessage("Maximum notes length: 30 characters.");
				notes.requestFocus();
			} else {
				processInventoryItemType(nameEntered, unitsEntered, measureEntered, daysEntered, orderEntered,
						notesEntered);
			}
		} else if ((nameEntered.equals("")) && (notesEntered.equals(""))
				&& (!history.equals("deleteVIIT") && !history.equals("addVIIT"))) {
			listAll();
		} else if (history.equals("deleteVIIT") || history.equals("addVIIT")) {
			Vendor v = (Vendor) myModel.getState("id");
			listByVendor((String) v.getState("ID"));
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
		if (history.equals("deleteIIT")) {
			InventoryItemTypeCollection ic = new InventoryItemTypeCollection();
			try {
				ic.findInventoryItemType(props);
				ic.createAndShowView2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			InventoryItemTypeCollection ic = new InventoryItemTypeCollection();
			try {
				ic.setManager((InventoryManager) ((VendorCollection) myModel).getManager());
				// System.out.println(ic.getManager().getState("his")+" ic");
				ic.findInventoryItemType(props);
				ic.createAndShowView();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void listAll() {
		InventoryItemTypeCollection ic = new InventoryItemTypeCollection();
		ic.setManager((InventoryManager) myModel.getState("self"));
		try {
			ic.findAllInventoryItemTypes();
			ic.createAndShowView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listByVendor(String vendID) {
		VendorInventoryItemTypeCollection vic = new VendorInventoryItemTypeCollection();
		InventoryItemTypeCollection iitc = new InventoryItemTypeCollection();
		vic.setManager((InventoryManager) myModel.getState("self"));
		Properties prop = new Properties();
		prop.setProperty("VendorID", vendID);
		VendorCollection vc = new VendorCollection();
		vc.setVendor(prop);
		if (history.equals("deleteVIIT")) {
			try {
				vic.findVendorInventoryItemType(prop);
				Vector<VendorInventoryItemType> list = (Vector<VendorInventoryItemType>) vic.getState("itemName");
				System.out.println(list);
				for (int i = 0; i < list.size(); i++) {
					prop.setProperty("ItemTypeName", (String) list.get(i).getState("InventoryItemTypeName"));
					iitc.findInventoryItemTypesWithNameLike(prop.getProperty("ItemTypeName"));
				}
				iitc.setManager((InventoryManager) myModel.getState("self"));
				iitc.createAndShowView();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				iitc.setV(vc.getV());
				iitc.setManager((InventoryManager) myModel.getState("self"));
				iitc.findAllInventoryItemTypes();
				iitc.createAndShowView();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void processInventoryItemType(String nameString, String unitString, String measureString, String dayString,
			String orderString, String noteString) {
		Properties props = new Properties();
		props.setProperty("ItemTypeName", nameString);
		props.setProperty("Units", unitString);
		props.setProperty("UnitMeasure", measureString);
		props.setProperty("ValidityDays", dayString);
		props.setProperty("ReorderPoint", orderString);
		props.setProperty("Notes", noteString);
		props.setProperty("Status", "Active");

		// clear fields for next time around
		name.setText("");
		unit.setText("");
		measure.setText("");
		days.setText("");
		reorder.setText("");
		notes.setText("");

		InventoryItemType i = new InventoryItemType(props);
		i.update();
		displayMessage("Successfully added to database");
		myModel.stateChangeRequest("Login", props);
	}

	@Override
	public void updateState(String key, Object value) {

	}

}