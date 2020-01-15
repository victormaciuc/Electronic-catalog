
import business.MainViewController2;
import configuration.ApplicationContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader messageLoader = new FXMLLoader();
        String fileN = ApplicationContext.getPROPERTIES().getProperty("view.main");
        //messageLoader.setLocation(getClass().getResource("view/MainView2.fxml"));
        messageLoader.setLocation(getClass().getResource(fileN));

        AnchorPane layout = messageLoader.load();
        primaryStage.setTitle("Catalog electronic");
        primaryStage.setScene(new Scene(layout, 1000, 700));
        MainViewController2 mainViewController = messageLoader.getController();
        mainViewController.init();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
