package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.InventoryItem;
import model.InventoryItemType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ModifyInventoryItemView extends View {
    private TextField barcode;
    private TextField inventoryItemTypeName;
    private TextField vendorId;
    private TextField dateReceived;
    private TextField dateOfLastUse;
    private TextField notes;
    private ComboBox<String> status;

    private Button modifyBtn;
    private Button backBtn;

    protected MessageView _statusLog;

    public ModifyInventoryItemView(IModel model) {
        super(model, "ModifyInventoryItemView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);
    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        javafx.scene.text.Text titleText = new Text(" Inventory Item Information ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(500);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    private VBox createFormContent() {

        VBox vBox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text(" Are you sure you want to use this item ? ");
        prompt.setWrappingWidth(500);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text labelBarcode = new Text(" Barcode : ");
        Text labelInventoryItemTypeName = new Text(" Inventory Item Type Name : ");
        Text labelVendorId = new Text(" Vendor Id : ");
        Text labelDateReceived = new Text(" Date of reception : ");
        Text labelDateOfLastUse = new Text(" Date of last use : ");
        Text labelNotes = new Text(" Notes : ");

        grid.add(labelBarcode, 0, 1, 2, 1);
        grid.add(labelInventoryItemTypeName, 0, 2, 2, 1);
        grid.add(labelVendorId, 0, 3, 2, 1);
        grid.add(labelDateReceived, 0, 4, 2, 1);
        grid.add(labelDateOfLastUse, 0, 5, 2, 1);
        grid.add(labelNotes, 0, 6, 2, 1);

        InventoryItem inventoryItem = (InventoryItem) myModel.getState("SelectedInventoryItem");

        barcode = new TextField(inventoryItem.getState("Barcode").toString());
        barcode.setDisable(true);
        inventoryItemTypeName = new TextField(inventoryItem.getState("InventoryItemTypeName").toString());
        inventoryItemTypeName.setDisable(true);
        vendorId = new TextField(inventoryItem.getState("VendorId").toString());
        vendorId.setDisable(true);
        dateReceived = new TextField(inventoryItem.getState("DateReceived").toString());
        dateReceived.setDisable(true);
        dateOfLastUse = new TextField(inventoryItem.getState("DateOfLastUse").toString());
        dateOfLastUse.setDisable(true);
        notes = new TextField(inventoryItem.getState("Notes").toString());
        notes.setDisable(true);

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Returned to Vendor",
                        "Expired",
                        "Damaged",
                        "Lost"
                );
        status = new ComboBox<String>(options);

        grid.add(barcode, 2, 1, 2, 1);
        grid.add(inventoryItemTypeName, 2, 2, 2, 1);
        grid.add(vendorId, 2, 3, 2, 1);
        grid.add(dateReceived, 2, 4, 2, 1);
        grid.add(dateOfLastUse, 2, 5, 2, 1);
        grid.add(notes, 2, 6, 2, 1);
        grid.add(status, 2, 7, 2, 1);

        HBox buttonContainer = new HBox();

        modifyBtn = new javafx.scene.control.Button("MODIFY");
        modifyBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (status.getSelectionModel().isEmpty()) {
                    displayMessage("Please select a status.");
                } else {
                    displayMessage("Inventory Item updated.");
                    myModel.stateChangeRequest("InventoryItemModified", status.getValue());
                }
            }
        });

        backBtn = new Button("BACK");
        backBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("BACK", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        btnContainer.getChildren().add(backBtn);
        btnContainer.getChildren().add(modifyBtn);

        vBox.getChildren().add(grid);
        vBox.getChildren().add(btnContainer);

        return vBox;

    }

    private boolean isExpired(InventoryItem inventoryItemSelected) {

        try {

            Date dateReceived = new SimpleDateFormat("MM/dd/yyyy")
                    .parse(inventoryItemSelected.getState("DateReceived").toString());

            InventoryItemType inventoryItemType = new InventoryItemType(
                    inventoryItemSelected.getState("InventoryItemTypeName").toString());

            int validityDays = Integer.parseInt(inventoryItemType.getState("ValidityDays").toString());

            Date todayDate = new Date();

            if (todayDate.after(addDays(dateReceived, validityDays))) {
                inventoryItemSelected.expired();
                return false;
            }

        } catch (InvalidPrimaryKeyException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); // minus number would decrement the days
        return cal.getTime();
    }

    public void displayMessage(String message) {
        _statusLog.displayMessage(message);
    }

    protected MessageView createStatusLog(String initialMessage) {
        _statusLog = new MessageView(initialMessage);

        return _statusLog;
    }

    public void updateState(String key, Object value) {
    }

}
