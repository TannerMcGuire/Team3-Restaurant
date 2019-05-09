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

//==============================================================================
public class VendorCollectionView extends View {
	protected TableView<VendorTableModel> tableOfVendors;
	protected Button submitButton;
	protected Button backButton;
	private InventoryManager manager;

	protected MessageView statusLog;

	// --------------------------------------------------------------------------
	public VendorCollectionView(IModel wsc) {
		super(wsc, "VendorCollectionView");

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

		manager = (InventoryManager) ((VendorCollection) myModel).getManager();

		// System.out.println((String) myModel.getState("his") + " here");
		// myModel.subscribe("InventoryManagerView", this);
	}

	// --------------------------------------------------------------------------
	protected void populateFields() {
		getEntryTableModelValues();
	}

	// --------------------------------------------------------------------------
	protected void getEntryTableModelValues() {

		ObservableList<VendorTableModel> tableData = FXCollections.observableArrayList();
		try {
			VendorCollection vendorCollection = (VendorCollection) myModel.getState("VendorList");

			Vector entryList = (Vector) vendorCollection.getState("Vendors");
			Enumeration entries = entryList.elements();

			while (entries.hasMoreElements() == true) {
				Vendor nextVendor = (Vendor) entries.nextElement();
				Vector<String> view = nextVendor.getEntryListView();

				// add this list entry to the list
				VendorTableModel nextTableRowData = new VendorTableModel(view);
				tableData.add(nextTableRowData);

			}

			tableOfVendors.setItems(tableData);
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

		Text title = new Text("LIST OF VENDORS");
		title.setWrappingWidth(350);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFill(Color.BLACK);
		grid.add(title, 0, 0, 2, 1);

		Text prompt = new Text("Select proper Vendor by double clicking or selecting and clicking submit");
		prompt.setWrappingWidth(350);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 1, 2, 1);

		tableOfVendors = new TableView<VendorTableModel>();
		tableOfVendors.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn vendorIDColumn = new TableColumn("ID");
		vendorIDColumn.setMinWidth(30);
		vendorIDColumn.setCellValueFactory(new PropertyValueFactory<VendorTableModel, String>("vendorId"));

		TableColumn nameColumn = new TableColumn("Name");
		nameColumn.setMinWidth(200);
		nameColumn.setCellValueFactory(new PropertyValueFactory<VendorTableModel, String>("name"));

		TableColumn phoneNumberColumn = new TableColumn("Phone Number");
		phoneNumberColumn.setMinWidth(150);
		phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<VendorTableModel, String>("phoneNumber"));

		TableColumn statusColumn = new TableColumn("Status");
		statusColumn.setMinWidth(50);
		statusColumn.setCellValueFactory(new PropertyValueFactory<VendorTableModel, String>("status"));

		tableOfVendors.getColumns().addAll(vendorIDColumn, nameColumn, phoneNumberColumn, statusColumn);

		tableOfVendors.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() >= 2) {

					if (manager.getState("his").equals("addVIIT") || manager.getState("his").equals("deleteVIIT")) {
						processVendorSelected();
					} else {
						select();
					}
				}
			}
		});
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(300, 200);
		scrollPane.setContent(tableOfVendors);

		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (manager.getState("his") == "addVIIT" || manager.getState("his").equals("deleteVIIT")) {
					processVendorSelected();
				}

				else {
					select();

				}
			}
		});

		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				// myModel.stateChangeRequest("InventoryManagerView", null);
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

	private void select() {
		if (manager.getState("his") == "addVendor") {
			processVendorSelected();
		}
		if (manager.getState("his") == "modifyVendor") {
			modifySelected();
		}
		if (manager.getState("his") == "processInvoice") {
			processInvoice();
		}

	}

	// --------------------------------------------------------------------------
	protected void processVendorSelected() {
		VendorTableModel selectedItem = tableOfVendors.getSelectionModel().getSelectedItem();

		if (selectedItem != null) {
			String selectedVendorID = selectedItem.getVendorId();
			myModel.stateChangeRequest("InventoryItemTypeView", selectedVendorID);
		}
	}

	// --------------------------------------------------------------------------

	private void modifySelected() {
		VendorTableModel selectedItem = tableOfVendors.getSelectionModel().getSelectedItem();

		if (selectedItem != null) {
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
	public void mouseClicked(MouseEvent click) {
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

}