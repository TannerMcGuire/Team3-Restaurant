package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
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

import model.InventoryItemType;
import model.InventoryItemTypeCollection;

import java.util.Enumeration;
import java.util.Observable;
import java.util.Vector;

public class InventoryItemTypeSelectionScreen extends View {

	protected TableView<InventoryItemTypeTableModel> _tableOfInventoryItemType;

	protected MessageView _statusLog;
	protected Button _doneBtn;
	protected Button _backBtn;

	public InventoryItemTypeSelectionScreen(IModel model) {
		super(model, "InventoryItemTypeSelectionScreen");

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

	protected void populateFields() {
		getEntryTableModelValues();
	}

	protected void getEntryTableModelValues() {

		ObservableList<InventoryItemTypeTableModel> tableData = FXCollections.observableArrayList();
		try {
			InventoryItemTypeCollection inventoryItemTypeCollection = (InventoryItemTypeCollection) myModel
					.getState("InventoryItemTypeCollection");

			Vector entryList = (Vector) inventoryItemTypeCollection.getState("InventoryItemTypes");
			Enumeration entries = entryList.elements();

			while (entries.hasMoreElements() == true) {
				InventoryItemType nextInventoryItemType = (InventoryItemType) entries.nextElement();
				Vector<String> view = nextInventoryItemType.getEntryListView();

				// add this list entry to the list
				InventoryItemTypeTableModel nextTableRowData = new InventoryItemTypeTableModel(view);
				tableData.add(nextTableRowData);

			}

			_tableOfInventoryItemType.setItems(tableData);
		} catch (Exception e) {// SQLException e) {
			// Need to handle this exception
		}
	}

	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);

		Text titleText = new Text(" ELEMENTS THAT CORRESPOND TO YOUR CRITERIA ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(500);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);

		return container;
	}

	private VBox createFormContent() {
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text prompt = new Text("LIST OF INVENTORY ITEM TYPE");
		prompt.setWrappingWidth(500);
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setFill(Color.BLACK);
		grid.add(prompt, 0, 0, 2, 1);

		_tableOfInventoryItemType = new TableView<InventoryItemTypeTableModel>();
		_tableOfInventoryItemType.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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

		_tableOfInventoryItemType.getColumns().addAll(nameColumn, unitColumn, measureColumn, validityColumn,
				orderColumn, noteColumn, statusColumn);

//		_tableOfInventoryItemType.setOnMousePressed(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				if (event.isPrimaryButtonDown() && event.getClickCount() >= 2) {
//					processInventoryItemTypeSelected();
//				}
//			}
//		});
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(115, 150);
		scrollPane.setContent(_tableOfInventoryItemType);

		_doneBtn = new Button("Submit");
		_doneBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		_doneBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				if (_tableOfInventoryItemType.getSelectionModel().getSelectedItem() == null) {
					// UPDATE WRONG STATUS MESSAGE
				} else {
					myModel.stateChangeRequest("MODIFY IIT",
							_tableOfInventoryItemType.getSelectionModel().getSelectedItem().getItemTypeName());
				}
			}
		});

		_backBtn = new Button("Back");
		_backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		_backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				myModel.stateChangeRequest("BACK", null);
			}
		});

		HBox btnContainer = new HBox(10);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().add(_doneBtn);
		btnContainer.getChildren().add(_backBtn);

		vbox.getChildren().add(grid);
		vbox.getChildren().add(scrollPane);
		vbox.getChildren().add(btnContainer);

		return vbox;
	}

	protected MessageView createStatusLog(String initialMessage) {
		_statusLog = new MessageView(initialMessage);

		return _statusLog;
	}

	public void updateState(String key, Object value) {
	}
}