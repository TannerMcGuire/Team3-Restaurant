package userinterface;

import impresario.IModel;
import impresario.IView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import model.InventoryItemType;
import model.InventoryItemTypeCollection;

import java.util.Observable;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryItemTypeChangesScreen extends View {

    private TextField _itemTypeName;
    private TextField _units;
    private TextField _unitMeasure;
    private TextField _validityDays;
    private TextField _reorderPoint;
    private TextField _notes;

    private ComboBox<String> _status;
    //private TextField _status;

    private Button _doneBtn;
    private Button _backBtn;

    protected MessageView _statusLog;

    public InventoryItemTypeChangesScreen(IModel model) {
        super(model, "InventoryItemTypeChangesScreen");


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

        javafx.scene.text.Text titleText = new Text(" Inventory Item Type Information ");
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

        Text prompt = new Text(" Please enter new Inventory Item Type Information ");
        prompt.setWrappingWidth(500);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text labelItemTypeName = new Text(" ItemTypeName : ");
        Text labelUnits = new Text(" Units : ");
        Text labelUnitMeasure = new Text(" UnitMeasure : ");
        Text labelValidityDays = new Text(" ValidityDays : ");
        Text labelReorderPoint = new Text(" ReorderPoint : ");
        Text labelNotes = new Text(" Notes : ");

        Text labelStatus = new Text(" Status : ");

        grid.add(labelItemTypeName, 0, 1, 2, 1);
        grid.add(labelUnits, 0, 2, 2, 1);
        grid.add(labelUnitMeasure, 0, 3, 2, 1);
        grid.add(labelValidityDays, 0, 4, 2, 1);
        grid.add(labelReorderPoint, 0, 5, 2, 1);
        grid.add(labelNotes, 0, 6, 2, 1);
       // grid.add(labelStatus, 0, 7, 2, 1);

        InventoryItemType selectedItem = ((InventoryItemTypeCollection)myModel.getState("InventoryItemTypeCollection")).getSelectInventoryItemType();

        _itemTypeName = new TextField(selectedItem.getState("ItemTypeName").toString());
        _units = new TextField(selectedItem.getState("Units").toString());
        _unitMeasure = new TextField(selectedItem.getState("UnitMeasure").toString());
        _validityDays = new TextField(selectedItem.getState("ValidityDays").toString());
        _reorderPoint = new TextField(selectedItem.getState("ReorderPoint").toString());
        _notes = new TextField(selectedItem.getState("Notes").toString());
        //_status = new TextField(selectedItem.getState("Status").toString());

//        ObservableList<String> options = FXCollections.observableArrayList("Active", "Inactive");
//
//        _status = new ComboBox<String>(options);

        grid.add(_itemTypeName, 2, 1, 2, 1);
        grid.add(_units, 2, 2, 2, 1);
        grid.add(_unitMeasure, 2, 3, 2, 1);
        grid.add(_validityDays, 2, 4, 2, 1);
        grid.add(_reorderPoint, 2, 5, 2, 1);
        grid.add(_notes, 2, 6, 2, 1);
       // grid.add(_status, 2, 7, 2, 1);

        HBox buttonContainer = new HBox();

        _doneBtn = new Button("DONE");
        _doneBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processInventoryItemTypeChange();
                myModel.stateChangeRequest("DONE", null);

            }
        });

        _backBtn = new Button("BACK");
        _backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("BACK", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(_backBtn);
        btnContainer.getChildren().add(_doneBtn);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);


        return vbox;
    }

    public void processInventoryItemTypeChange() {

        if (!isAlphaNumeric(_itemTypeName.getText()) || !isAlphaNumeric(_units.getText()) ||
            !isAlphaNumeric(_unitMeasure.getText()) || !isAlphaNumeric(_reorderPoint.getText()) ||
            !isAlphaNumeric(_notes.getText()) /*|| !isAlphaNumeric(_status.getText())*/) {

            displayMessage("Please enter only alphanumerical values.");
            return ;
        }
//        if (_status.getSelectionModel().isEmpty()) {
//            displayMessage("Please select a status.");
//            return ;
//        }
        if (!isValidityDaysOk()) {
            displayMessage("Validity Days must be greater than -2.");
            return ;
        }

        Properties properties = new Properties();

        properties.setProperty("ItemTypeName", _itemTypeName.getText());
        properties.setProperty("Units", _units.getText());
        properties.setProperty("UnitMeasure", _unitMeasure.getText());
        properties.setProperty("ValidityDays", _validityDays.getText());
        properties.setProperty("ReorderPoint", _reorderPoint.getText());
        properties.setProperty("Notes", _notes.getText());
        properties.setProperty("Status", "Active");

        displayMessage("Inventory Item Type updated.");

        myModel.stateChangeRequest("Submit new IIT Info", properties);


    }



    public void displayMessage(String message) {
        _statusLog.displayMessage(message);
    }

    private boolean isAlphaNumeric(String s){
        if (s.length() == 0)
            return false;
        String pattern= "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    private boolean isValidityDaysOk() {

        String pattern = "-?[1-9]\\d*|0";
        String toCheck = _validityDays.getText();

        if (!toCheck.matches(pattern)) {
            
            return false;
        }

        if (toCheck.startsWith("-") && !isAlphaNumeric(toCheck.substring(1, toCheck.length()))) {
            
            return false;
        }

        int number = Integer.parseInt(_validityDays.getText());
        if (number < -1) {
            
            return false;
        }
        return true;
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