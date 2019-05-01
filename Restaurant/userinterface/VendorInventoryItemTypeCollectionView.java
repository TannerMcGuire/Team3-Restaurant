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
import model.InventoryItemType;
import model.InventoryItemTypeCollection;
import model.InventoryManager;
import model.VendorCollection;
import model.VendorInventoryItemType;
import model.VendorInventoryItemTypeCollection;

//==============================================================================
public class VendorInventoryItemTypeCollectionView extends View {
	protected TableView<VendorInventoryItemTypeTableModel> tableOfVendorItemTypes;
	protected Button submitButton;
	protected Button backButton;
	private InventoryManager manager;

	protected MessageView statusLog;

	// --------------------------------------------------------------------------
	public VendorInventoryItemTypeCollectionView(IModel wsc) {
		super(wsc, "VendorInventoryItemTypeCollectionView");

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
		
		myModel.subscribe("VendorInventoryItemTypeCollectionView", this);
		
	}

	// --------------------------------------------------------------------------
	protected void populateFields() {
		getEntryTableModelValues();
	}

	// --------------------------------------------------------------------------
	protected void getEntryTableModelValues() {

		ObservableList<VendorInventoryItemTypeTableModel> tableData = FXCollections.observableArrayList();
		try {
			VendorInventoryItemTypeCollection vendorItemCollection = (VendorInventoryItemTypeCollection) myModel.getState("VendorInventoryItemTypeList");

			Vector entryList = (Vector) vendorItemCollection.getState("VendorInventoryItemType");
			Enumeration entries = entryList.elements();

			while (entries.hasMoreElements() == true) {
				VendorInventoryItemType nextVendorInventoryItemType = (VendorInventoryItemType) entries.nextElement();
				Vector<String> view = nextVendorInventoryItemType.getEntryListView();

				// add this list entry to the list
				VendorInventoryItemTypeTableModel nextTableRowData = new VendorInventoryItemTypeTableModel(view);
				tableData.add(nextTableRowData);

			}
			//vendorItemCollection.printVendorItems();//debug
			//System.out.println((String) myModel.getState("his"));
			tableOfVendorItemTypes.setItems(tableData);
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

		Text title = new Text("LIST OF VENDOR INVENTORY ITEM TYPES");
		title.setWrappingWidth(350);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFill(Color.BLACK);
		grid.add(title, 0, 0, 2, 1);
		
		Text prompt = new Text("Select VendorInventoryItemType to delete by double clicking or selecting and clicking submit");
		prompt.setWrappingWidth(350);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 1, 2, 1);

		tableOfVendorItemTypes = new TableView<VendorInventoryItemTypeTableModel>();
		tableOfVendorItemTypes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn iDColumn = new TableColumn("Item Type ID");
		iDColumn.setMinWidth(70);
		iDColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("id"));

		TableColumn vendorIDColumn = new TableColumn("Vendor ID");
		vendorIDColumn.setMinWidth(30);
		vendorIDColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("vendorID"));

		TableColumn nameColumn = new TableColumn("Inventory Item Type Name");
		nameColumn.setMinWidth(200);
		nameColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("inventoryItemTypeName"));
		
		TableColumn priceColumn = new TableColumn("Price");
		priceColumn.setMinWidth(50);
		priceColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("vendorPrice"));
		
		TableColumn dateColumn = new TableColumn("Last Updated");
		dateColumn.setMinWidth(150);
		dateColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("dateOfLastUpdate"));

		tableOfVendorItemTypes.getColumns().addAll(iDColumn, vendorIDColumn, nameColumn, priceColumn, dateColumn);

		tableOfVendorItemTypes.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() >= 2) {
					processVendorInventoryItemTypeSelected();
				}
			}
		});
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(300, 200);
		scrollPane.setContent(tableOfVendorItemTypes);

		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				processVendorInventoryItemTypeSelected();
			}
		});
		
		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				//myModel.stateChangeRequest("InventoryManagerView", null);
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

		return vbox;
	}

	// --------------------------------------------------------------------------
	public void updateState(String key, Object value) {
		
	}

	// --------------------------------------------------------------------------
	protected void processVendorInventoryItemTypeSelected() {
		VendorInventoryItemTypeTableModel selectedItem = tableOfVendorItemTypes.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			String selectedID = selectedItem.getId();
			String selectedName = selectedItem.getInventoryItemTypeName();
			Properties prop = new Properties();
			prop.setProperty("Id", selectedID);
			prop.setProperty("InventoryItemTypeName", selectedName);
			VendorInventoryItemType viit = new VendorInventoryItemType(prop);
			viit.delete(prop);
			displayMessage("VendorInventoryItemType Removed");
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