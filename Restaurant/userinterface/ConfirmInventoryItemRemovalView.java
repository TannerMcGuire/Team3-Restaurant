package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;

import java.awt.*;

public class ConfirmInventoryItemRemovalView extends View {

    private TextField _barcode;
    private TextField _inventoryItemTypeName;
    private TextField _vendorId;
    private TextField _dateReceived;
    private TextField _dateOfLastUse;
    private TextField _notes;

    private Button _yesBtn;
    private Button _noBtn;

    protected MessageView _statusLog;

    public ConfirmInventoryItemRemovalView(IModel model) {
        super(model,"ConfirmInventoryItemRemovalView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);
    }

    private Node createTitle()
    {
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

        //InventoryItem inventoryItem = myModel.getSelectedInventoryItem();

        _barcode = new TextField("BARCODE"/*inventoryItem.getState("Barcode").toString()*/);
        _inventoryItemTypeName = new TextField("INVENTORY ITEM TYPE NAME"/*inventoryItem.getState("InventoryItemTypeName").toString()*/);
        _vendorId = new TextField("VENDOR ID"/*inventoryItem.getState("VendorId").toString()*/);
        _dateReceived = new TextField("DATE RECEIVED"/*inventoryItem.getState("DateReceived").toString()*/);
        _dateOfLastUse = new TextField("DATE OF LAST USE"/*inventoryItem.getState("DateOfLastUse").toString()*/);
        _notes = new TextField("NOTES"/*inventoryItem.getState("Notes").toString()*/);

        grid.add(_barcode, 2, 1, 2, 1);
        grid.add(_inventoryItemTypeName, 2, 2, 2, 1);
        grid.add(_vendorId, 2, 3, 2, 1);
        grid.add(_dateReceived, 2, 4, 2, 1);
        grid.add(_dateOfLastUse, 2, 5, 2, 1);
        grid.add(_notes, 2, 6, 2, 1);


        HBox buttonContainer = new HBox();

        _yesBtn = new javafx.scene.control.Button("DONE");
        _yesBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("InventoryItemRemoval", null);

            }
        });

        _noBtn = new Button("BACK");
        _noBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("BACK", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(_noBtn);
        btnContainer.getChildren().add(_yesBtn);

        vBox.getChildren().add(grid);
        vBox.getChildren().add(btnContainer);


        return vBox;

    }

    protected MessageView createStatusLog(String initialMessage)
    {
        _statusLog = new MessageView(initialMessage);

        return _statusLog;
    }

    public void updateState(String key, Object value)
    {
    }
}
