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
import model.InventoryItem;
import model.InventoryItemCollection;
import model.InventoryManager;

//==============================================================================
public class InventoryItemCollectionView extends View {
	protected TableView<InventoryItemTableModel> tableOfInventoryItems;
	protected Button backButton;
	protected Button submitButton;
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
		// System.out.println("here " + myModel.getState("his"));
	}

	// --------------------------------------------------------------------------
	protected void populateFields() {
		getEntryTableModelValues();
	}

	// --------------------------------------------------------------------------
	protected void getEntryTableModelValues() {

		ObservableList<InventoryItemTableModel> tableData = FXCollections.observableArrayList();
		try {
			InventoryItemCollection itemCollection = (InventoryItemCollection) myModel.getState("InventoryItemList");

			Vector entryList = (Vector) itemCollection.getState("InventoryItem");
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

		Text title = new Text("FULL LIST OF INVENTORY ITEMS");
		title.setWrappingWidth(350);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFill(Color.BLACK);
		grid.add(title, 0, 0, 2, 1);

		tableOfInventoryItems = new TableView<InventoryItemTableModel>();
		tableOfInventoryItems.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn barcodeColumn = new TableColumn("Barcode");
		barcodeColumn.setMinWidth(50);
		barcodeColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("barcode"));

		TableColumn nameColumn = new TableColumn("Inventory Item Name");
		nameColumn.setMinWidth(200);
		nameColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("itemName"));

		TableColumn vendorColumn = new TableColumn("Vendor ID");
		vendorColumn.setMinWidth(20);
		vendorColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("vendorID"));

		TableColumn receivedColumn = new TableColumn("Date Received");
		receivedColumn.setMinWidth(120);
		receivedColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("dateReceived"));

		TableColumn useColumn = new TableColumn("Date of Last Use");
		useColumn.setMinWidth(120);
		useColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("dateLastUsed"));

		TableColumn noteColumn = new TableColumn("Notes");
		noteColumn.setMinWidth(150);
		noteColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("notes"));

		TableColumn statusColumn = new TableColumn("Status");
		statusColumn.setMinWidth(50);
		statusColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTableModel, String>("status"));

		tableOfInventoryItems.getColumns().addAll(barcodeColumn, nameColumn, vendorColumn, receivedColumn, useColumn,
				noteColumn, statusColumn);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(300, 200);
		scrollPane.setContent(tableOfInventoryItems);

		if (myModel.getState("his").equals("removeItem")) {

			submitButton = new Button("Submit");
			submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			submitButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					processAction(e);
				}
			});

			backButton = new Button("Back");
			backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					new model.InventoryManager();
				}
			});

			HBox btnContainer = new HBox(10);
			btnContainer.setAlignment(Pos.CENTER);
			btnContainer.getChildren().add(submitButton);
			btnContainer.getChildren().add(backButton);

			vbox.getChildren().add(grid);
			vbox.getChildren().add(scrollPane);
			vbox.getChildren().add(btnContainer);
		} else {
			backButton = new Button("Back");
			backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			backButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					new model.InventoryManager();
				}
			});

			HBox btnContainer = new HBox(10);
			btnContainer.setAlignment(Pos.CENTER);
			btnContainer.getChildren().add(backButton);

			vbox.getChildren().add(grid);
			vbox.getChildren().add(scrollPane);
			vbox.getChildren().add(btnContainer);
		}
		return vbox;
	}

	// --------------------------------------------------------------------------
	public void updateState(String key, Object value) {
	}

	// ----------------------------------------------------------
	public void processAction(Event evt) {
		InventoryItemTableModel itemSelected = tableOfInventoryItems.getSelectionModel().getSelectedItem();
		if (itemSelected != null) {
			String selectedBarcode = itemSelected.getBarcode();
			processBarcode(selectedBarcode);
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
	/*
	 * //--------------------------------------------------------------------------
	 * public void mouseClicked(MouseEvent click) { if(click.getClickCount() >= 2) {
	 * processInventoryItemTypeSelected(); } }
	 */

}
