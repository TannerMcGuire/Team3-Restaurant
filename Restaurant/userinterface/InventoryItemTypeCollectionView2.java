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

// project imports
import impresario.IModel;
import model.InventoryItemType;
import model.InventoryItemTypeCollection;
import model.InventoryManager;

//==============================================================================
public class InventoryItemTypeCollectionView2 extends View {
	protected TableView<InventoryItemTypeTableModel> tableOfItemTypes;
	protected Button submitButton;
	protected Button backButton;

	protected MessageView statusLog;

	// --------------------------------------------------------------------------
	public InventoryItemTypeCollectionView2(IModel wsc) {
		super(wsc, "InventoryItemTypeCollectionView2");

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
	}

	// --------------------------------------------------------------------------
	protected void populateFields() {
		getEntryTableModelValues();
	}

	// --------------------------------------------------------------------------
	protected void getEntryTableModelValues() {

		ObservableList<InventoryItemTypeTableModel> tableData = FXCollections.observableArrayList();
		try {
			InventoryItemTypeCollection itemCollection = (InventoryItemTypeCollection) myModel
					.getState("InventoryItemTypeList");

			Vector entryList = (Vector) itemCollection.getState("InventoryItemType");
			Enumeration entries = entryList.elements();

			while (entries.hasMoreElements() == true) {
				InventoryItemType nextInventoryItemType = (InventoryItemType) entries.nextElement();
				Vector<String> view = nextInventoryItemType.getEntryListView();

				// add this list entry to the list
				InventoryItemTypeTableModel nextTableRowData = new InventoryItemTypeTableModel(view);
				tableData.add(nextTableRowData);

			}

			tableOfItemTypes.setItems(tableData);
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

		Text title = new Text("LIST OF INVENTORY ITEM TYPES");
		title.setWrappingWidth(350);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFill(Color.BLACK);
		grid.add(title, 0, 0, 2, 1);

		Text prompt = new Text("Select proper InventoryItemType by double clicking or selecting and clicking submit");
		prompt.setWrappingWidth(350);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 1, 2, 1);

		tableOfItemTypes = new TableView<InventoryItemTypeTableModel>();
		tableOfItemTypes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn nameColumn = new TableColumn("ItemTypeName");
		nameColumn.setMinWidth(200);
		nameColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("itemTypeName"));

		TableColumn unitColumn = new TableColumn("Units");
		unitColumn.setMinWidth(30);
		unitColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("units"));

		TableColumn measureColumn = new TableColumn("Unit of Measure");
		measureColumn.setMinWidth(150);
		measureColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("unitMeasure"));

		TableColumn validityColumn = new TableColumn("Validity Days");
		validityColumn.setMinWidth(150);
		validityColumn
				.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("validityDays"));

		TableColumn orderColumn = new TableColumn("Reorder Point");
		orderColumn.setMinWidth(50);
		orderColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("reorderPoint"));

		TableColumn noteColumn = new TableColumn("Notes");
		noteColumn.setMinWidth(150);
		noteColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("notes"));

		TableColumn statusColumn = new TableColumn("Status");
		statusColumn.setMinWidth(50);
		statusColumn.setCellValueFactory(new PropertyValueFactory<InventoryItemTypeTableModel, String>("status"));

		tableOfItemTypes.getColumns().addAll(nameColumn, unitColumn, measureColumn, validityColumn, orderColumn,
				noteColumn, statusColumn);

		tableOfItemTypes.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() >= 2) {
					processInventoryItemTypeSelected();
				}
			}
		});
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(300, 200);
		scrollPane.setContent(tableOfItemTypes);

		submitButton = new Button("Submit");
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				processInventoryItemTypeSelected();
			}
		});

		backButton = new Button("Back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// myModel.stateChangeRequest("InventoryManagerView", null);
				new model.InventoryManager();
			}
		});

		HBox btnContainer = new HBox(100);
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
	protected void processInventoryItemTypeSelected() {
		InventoryItemTypeTableModel selectedItem = tableOfItemTypes.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			String selectedItemTypeName = selectedItem.getItemTypeName();
			try {
				InventoryItemType i = new InventoryItemType(selectedItemTypeName);
				i.delete();
				displayMessage(selectedItemTypeName + " successfully deleted");
			} catch (InvalidPrimaryKeyException e) {
				e.printStackTrace();
			}
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
