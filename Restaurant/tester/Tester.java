// specify the package
package tester;

// system imports
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.FileOutputStream;
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// project imports
import event.Event;
import event.EventLog;
import common.PropertyFile;
import model.InventoryManager;

import userinterface.MainStageContainer;
import userinterface.WindowPosition;


/** The class containing the main program  for the Restaurant application */
//==============================================================
public class Tester extends Application
{

	private InventoryManager myManager;		// the main behavior for the application

	/** Main frame of the application */
	private Stage mainStage;


	// start method for this class, the main application object
	//----------------------------------------------------------
	public void start(Stage primaryStage)
	{
	   System.out.println("Restaurant Version 1.0");
	   System.out.println("Copyright Team 3 2019");

           // Create the top-level container (main frame) and add contents to it.
	   MainStageContainer.setStage(primaryStage, "Restaurant Inventory Version 1.0");
	   mainStage = MainStageContainer.getInstance();

	   // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT
	   // 'X' IN THE WINDOW), and show it.
           mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
           });

           try
	   {
		myManager = new InventoryManager();
	   }
	   catch(Exception exc)
	   {
		System.err.println("Tester.Tester - could not create Manager!");
		new Event(Event.getLeafLevelClassName(this), "Tester.<init>", "Unable to create Manager object", Event.ERROR);
		exc.printStackTrace();
	   }


  	   WindowPosition.placeCenter(mainStage);

           mainStage.show();
	}


	/** 
	 * The "main" entry point for the application. Carries out actions to
	 * set up the application
	 */
	//----------------------------------------------------------
    	public static void main(String[] args)
	{

		launch(args);
	}

}
/*
 * Contributing members Tanner McGuire, Derek Hanley, David Kayltyuk, Bastien Montauge, Ryan BeGell
 */