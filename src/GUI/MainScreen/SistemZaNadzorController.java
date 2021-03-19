package GUI.MainScreen;

import GUI.AmbulanceControl.AmbulanceControlController;
import GUI.Statistics.StatistikaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logger.MyLogger;
import main.Main;
import osobe.Stanovnik;
import simulacija.*;
import utils.Konstante;
import utils.LogDetails;
import vozila.AmbulantnaKola;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class SistemZaNadzorController
{
    @FXML private VBox listaStanovnika;
    @FXML private Label brojStanovnika;
    @FXML private Label brojZarazenih;
    @FXML private Label brojIzlijecenih;
    @FXML private GridPane mapaGrada;
    @FXML private BorderPane mainPane;

    private static final int velicinaGrada = ThreadLocalRandom.current().nextInt(Konstante.MAP_LOWER_BOUND, Konstante.MAP_UPPER_BOUND);

    public static int getVelicinaGrada() { return velicinaGrada; }
    public BorderPane getMainPane() { return mainPane; }
    public GridPane getMapaGrada() { return mapaGrada; }
    public VBox getListaStanovnika() { return listaStanovnika; }
    public Label getBrojStanovnika() { return brojStanovnika; }
    public Label getBrojZarazenih() { return brojZarazenih; }
    public Label getBrojIzlijecenih() { return brojIzlijecenih; }
    public void setBrojZarazenih(String zarazeni) { brojZarazenih.setText(zarazeni); }
    public void setBrojIzlijecenih(String izlijeceni) { brojIzlijecenih.setText(izlijeceni); }

    @FXML public void omoguciKretanje(ActionEvent event)
    {
        if (Serijalizacija.getSerialized())
        {
            MyLogger.log(Level.INFO, LogDetails.START_WHILE_DES);
            return;
        }

        if (!MjerenjeTemperature.jeLiMjerenjeTemperatureAktivno())
            MjerenjeTemperature.kreirajMjerenjeTemperature().start();

        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            s.pokreni();

        for (AmbulantnaKola k : Simulacija.dohvatiSimulaciju().getAmbulantnaKola())
            k.pokreni();
    }

    @FXML public void posaljiVozilo(ActionEvent event)
    {
        if (Serijalizacija.getSerialized())
        {
            MyLogger.log(Level.INFO, LogDetails.VEHICLE_WHILE_DES);
            return;
        }

        Alarm.resolveAlarm();
    }

    @FXML public void pogledajStanjeAmbulanti(ActionEvent event) throws Exception
    {
        AmbulanceControlController kontroler;
        Stage mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../AmbulanceControl/AmbulanceControl.fxml"));
        Parent root = loader.load();
        Scene sc = new Scene(root);
        sc.setFill(Color.TRANSPARENT);
        mainStage.setScene(sc);
        mainStage.setTitle("JavaKov-20");
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.getIcons().add(new Image("/resursi/coronavirus.png"));
        mainStage.setFullScreen(false);
        kontroler = loader.getController();
        kontroler.addAll();
        mainStage.show();
    }

    @FXML public void zaustaviSimulaciju(ActionEvent event)
    {
        Serijalizacija.serialize();
    }

    @FXML public void ponovoPokreni(ActionEvent event)
    {
        if (!Serijalizacija.deserialize())
            return;

        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            s.pokreni();

        for (AmbulantnaKola k : Simulacija.dohvatiSimulaciju().getAmbulantnaKola())
            k.pokreni();
    }

    @FXML public void pogledajStatistku(ActionEvent event) throws IOException
    {
        StatistikaController kontroler;
        Stage mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Statistics/Statistika.fxml"));
        Parent root = loader.load(); // FXMLLoader.load(getClass().getResource("SistemZaNadzor.fxml"));
        Scene sc = new Scene(root);
        sc.setFill(Color.TRANSPARENT);
        mainStage.setScene(sc);
        mainStage.setTitle("JavaKov-20");
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.getIcons().add(new Image("/resursi/coronavirus.png"));
        mainStage.setFullScreen(false);
        kontroler = loader.getController();
        kontroler.popuniInfo();
        mainStage.show();
    }

    @FXML public void zavrsiSimulaciju(ActionEvent event)
    {
        if (Serijalizacija.getSerialized())
            Serijalizacija.deserialize();

        Simulacija.dohvatiSimulaciju().zaustaviSveTredove();
        Simulacija.dohvatiSimulaciju().zavrsiSimulaciju();
        ((Button) event.getSource()).getScene().getWindow().hide();
        System.exit(0);
    }

    @FXML public void exitGUI(MouseEvent e)
    {
        if (Serijalizacija.getSerialized())
            Serijalizacija.deserialize();

        Simulacija.dohvatiSimulaciju().zaustaviSveTredove();
        Simulacija.dohvatiSimulaciju().zavrsiSimulaciju();
        ((Circle) e.getSource()).getScene().getWindow().hide();
        System.exit(0);
    }

    @FXML public void fullScreenToggle(MouseEvent e)
    {
        Main.myStage.setFullScreen(!Main.myStage.isFullScreen());
    }

    @FXML public void minimize(MouseEvent e)
    {
        if (!Main.myStage.isIconified())
            Main.myStage.setIconified(true);
    }

    public void iscrtajGrid()
    {
        for (int i = 0; i < velicinaGrada; i++)
        {
            RowConstraints row = new RowConstraints();
            ColumnConstraints column = new ColumnConstraints();
            row.setValignment(VPos.CENTER);
            column.setHalignment(HPos.CENTER);
            row.setPercentHeight(100 / (double) velicinaGrada);
            column.setPercentWidth(100 / (double) velicinaGrada);
            mapaGrada.getRowConstraints().add(row);
            mapaGrada.getColumnConstraints().add(column);
        }
    }
}
