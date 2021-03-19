package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Main extends Application
{
    public static Stage myStage;

    public void start(Stage primaryStage) throws IOException
    {
        setAndLaunchStartScreen(primaryStage);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private void setAndLaunchStartScreen(Stage primaryStage) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../GUI/StartScreen/PocetniEkran.fxml"));
        Parent root = loader.load(); // FXMLLoader.load(getClass().getResource("SistemZaNadzor.fxml"));
        Scene sc = new Scene(root);
        primaryStage.setScene(sc);
        primaryStage.setTitle("JavaKov-20");
        sc.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().add(new Image("/resursi/coronavirus.png"));
        primaryStage.show();
    }
}
