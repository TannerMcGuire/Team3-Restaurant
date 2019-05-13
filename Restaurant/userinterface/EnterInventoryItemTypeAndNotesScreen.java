package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.InventoryItemTypeCollection;

import java.util.Properties;

public class EnterInventoryItemTypeAndNotesScreen extends View {

	private TextField name;
	private TextField notes;
	private Button _submitBtn;
	private Button _backBtn;

	public EnterInventoryItemTypeAndNotesScreen(IModel librarian) {
		super(librarian, "TitleView");

		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title
		container.getChildren().add(createTitle());

		// Add a textBox
		container.getChildren().add(createFormContent());

		getChildren().add(container);

		// populateFields();
	}

	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);

		Text titleText = new Text(" ENTER NAME AND NOTES ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);

		return container;
	}

	private VBox createFormContent() {

		VBox vBox = new VBox(50);

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

		grid.add(name, 1, 1);

		Text noteLabel = new Text(" Notes : ");
		noteLabel.setFont(myFont);
		noteLabel.setWrappingWidth(150);
		noteLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(noteLabel, 0, 2);
		notes = new TextField();

		grid.add(notes, 1, 2);

		_submitBtn = new Button("Submit");
		_submitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		_submitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (name.getText().isEmpty() && notes.getText().isEmpty()) {
					Properties properties = new Properties();
					properties.setProperty("ItemTypeName", name.getText());
					properties.setProperty("Notes", notes.getText());
					myModel.stateChangeRequest("SUBMIT IIT NAME AND NOTES", properties);
				} else {
					Properties properties = new Properties();
					if (name.getText() != null) {
						properties.setProperty("ItemTypeName", name.getText());
					}

					if (notes.getText() != null) {
						properties.setProperty("Notes", notes.getText());
					}
					myModel.stateChangeRequest("SUBMIT IIT NAME AND NOTES", properties);
				}
			}
		});

		_backBtn = new Button("Back");
		_backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		_backBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("BACK", null);
			}
		});
		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().add(_submitBtn);
		buttons.getChildren().add(_backBtn);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().add(grid);
		vBox.getChildren().add(buttons);

		return vBox;

	}

	public void updateState(String key, Object value) {
	}
}