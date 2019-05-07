package userinterface;

// system imports
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Vector;

import exception.InvalidPrimaryKeyException;

import java.util.Enumeration;
import java.util.Properties;

// project imports
import impresario.IModel;
import model.Vendor;
import model.VendorCollection;
import model.InventoryManager;
import model.InventoryItem;
import model.InventoryItemCollection;

//==============================================================================
public class InventoryItemCollectionView extends View {
	protected TableView<InventoryItemTableModel> tableOfVendors;
	protected Button submitButton;
	protected Button backButton;
	private InventoryManager manager;

	protected MessageView statusLog;

	// --------------------------------------------------------------------------
	public InventoryItemCollectionView(IModel wsc) {
		super(wsc, "InventoryItemCollectionView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                                            "));

		getChildren().add(container);

		populateFields();

		manager = (InventoryManager) ((InventoryItemCollection) myModel).getManager();

		// System.out.println((String) myModel.getState("his") + " here");
		// myModel.subscribe("InventoryManagerView", this);
	}

	// --------------------------------------------------------------------------
	protected void populateFields() {
		getEntryTableModelValues();
	}

	// --------------------------------------------------------------------------
	protected void getEntryTableModelValues() {

		ObservableList<InventoryItemTableModel> tableData = FXCollections.observableArrayList();
		try {
			InventoryItemCollection InventoryItemCollection = (InventoryItemCollection) myModel.getState("InventoryItemList");

			Vector entryList = (Vector) InventoryItemCollection.getState("InventoryItems");
			Enumeration entries = entryList.elements();

			while (entries.hasMoreElements() == true) {
				InventoryItem nextInventoryItem = (InventoryItem) entries.nextElement();
				Vector<String> view = nextInventoryItem.getEntryListView();

				// add this list entry to the list
				InventoryItemTableModel nextTableRowData = new InventoryItemTableModel(view);
				tableData.add(nextTableRowData);

			}

		
			tableOfInventoryItems.setItems(tableData);
		} catch (Exception e) {// SQLException e) {
			// Need to handle this exception
		}
	}

	// Create the title container
	// -------------------------------------------------------------
	private Node createTitle() {
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

		Text title = new Text("LIST OF INVENTORY ITEMS");
		title.setWrappingWidth(350);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFill(Color.BLACK);
		grid.add(title, 0, 0, 2, 1);

		Text prompt = new Text("Select proper Inventory Item by double clicking or selecting and clicking submit");
		prompt.setWrappingWidth(350);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 1, 2, 1);

		TableView<InventoryItemTableModel> tableOfInventoryItems = new TableView<InventoryItemTableModel>();
		tableOfInventoryItems.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn InventoryItemBarcodeColumn = new TableColumn("Barcode");
		InventoryItemBarcodeColumn.setMinWidth(30);
		InventoryItemBarcodeColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("InventoryItemId"));

		TableColumn nameColumn = new TableColumn("Inventory Item Type Name");
		nameColumn.setMinWidth(200);
		nameColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("InventoryItemTypeName"));

		TableColumn InventoryItemIdColumn = new TableColumn("InventoryItem Id");
		InventoryItemIdColumn.setMinWidth(150);
		InventoryItemIdColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("InventoryItemId"));

		TableColumn DateRecievedColumn = new TableColumn("Current Date");
		DateRecievedColumn.setMinWidth(50);
		DateRecievedColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("DateRecieved"));

		TableColumn dateOfLastUseColumn = new TableColumn("Date of Last Use");
		dateOfLastUseColumn .setMinWidth(50);
		dateOfLastUseColumn .setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("dateOfLastUse"));

		TableColumn statusColumn = new TableColumn("Status");
		statusColumn.setMinWidth(50);
		statusColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("status"));
		
		
		TableColumn notesColumn = new TableColumn("notes");
		notesColumn.setMinWidth(50);
		notesColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("notes"));

		
		tableOfInventoryItems.getColumns().addAll( InventoryItemBarcodeColumn, nameColumn, InventoryItemIdColumn, DateRecievedColumn, dateOfLastUseColumn,  statusColumn, notesColumn);

/*		tableOfInventoryItems.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() >= 2) {

					if (manager.getState("his").equals("addVIIT") || manager.getState("his").equals("deleteVIIT")) {
						processInventoryItemSelected();
					}
					else {
						select();
				}
					}
			}
		});
		*/
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(300, 200);
		scrollPane.setContent(tableOfInventoryItems);

/*		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (manager.getState("his") == "addVIIT" || manager.getState("his").equals("deleteVIIT")) {
					processInventoryItemSelected();
				}

				else {
					select();

				}
			}
		});
*/
		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {

	public void handle(ActionEvent e) {
		// myModel.stateChangeRequest("InventoryManagerView", null);
		new model.InventoryManager();
	}});

	HBox btnContainer = new HBox(
			10);btnContainer.setAlignment(Pos.CENTER);btnContainer.getChildren().add(submitButton);btnContainer.getChildren().add(backButton);

	vbox.getChildren().add(grid);vbox.getChildren().add(scrollPane);vbox.getChildren().add(btnContainer);

	return vbox;
	}

	// --------------------------------------------------------------------------
	public void updateState(String key, Object value) {

	}

	// --------------------------------------------------------------------------

/*	private void select() {
		if (manager.getState("his") == "addInventoryItem") {
			processVendorSelected();
		}
		if (manager.getState("his") == "modifyVendor") {
			modifySelected();
		}
		if (manager.getState("his") == "processInvoice") {
			processInvoice();
		}

	}
*/
	// --------------------------------------------------------------------------
/*	protected void processInventoryItemSelected() {
		InventoryItemTableModel selectedItem = tableOfInventoryItems.getSelectionModel().getSelectedItem();

		if (selectedItem != null) {
			String selectedBarcode = selectedItem.getBarcode();
			myModel.stateChangeRequest("InventoryItemTypeView", selectedBarcode);
		}
	}
	
	// --------------------------------------------------------------------------

	private void modifySelected() {
		VendorTableModel selectedItem = tableOfVendors.getSelectionModel().getSelectedItem();

		if (selectedItem != null) {
			String selectedVendorID = selectedItem.getVendorId();
			Properties prop = new Properties();
			prop.setProperty("Id", selectedItem.getVendorId());
			prop.setProperty("Name", selectedItem.getName());
			prop.setProperty("PhoneNumber", selectedItem.getPhoneNumber());
			prop.setProperty("Status", selectedItem.getStatus());

			manager.stateChangeRequest("ModifyVendorView", prop);
		}
	}

	// --------------------------------------------------------------------------

	private void processInvoice() {
		VendorTableModel selectedItem = tableOfVendors.getSelectionModel().getSelectedItem();

		if (selectedItem != null) {
			Properties prop = new Properties();
			prop.setProperty("Id", selectedItem.getVendorId());
			prop.setProperty("Name", selectedItem.getName());
			prop.setProperty("PhoneNumber", selectedItem.getPhoneNumber());
			prop.setProperty("Status", selectedItem.getStatus());
			manager.stateChangeRequest("ProcessInvoice", prop);
		}
	}
*/
	// --------------------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage) {
		statusLog = new MessageView(initialMessage);

		return statusLog;
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

	// --------------------------------------------------------------------------
/*	public void mouseClicked(MouseEvent click) {
		if (click.getClickCount() >= 2) {
			System.out.print("Double\n");
			if (manager.getState("his").equals("addVIIT")) {
				processVendorSelected();
			}
			if (manager.getState("his").equals("modifyVendor")) {
				modifySelected();
			}
		}
	}
*/
}