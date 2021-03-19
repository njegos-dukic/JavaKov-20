package GUI.StartScreen;

import GUI.MainScreen.SistemZaNadzorController;
import izuzeci.*;
import logger.MyLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;
import simulacija.AzuriranjeGUI;
import simulacija.Simulacija;
import utils.LogDetails;
import utils.Putanje;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import static utils.Konstante.INPUT_LIMIT;

public class PocetniEkranController
{
    @FXML private TextField brojOdraslihString;
    @FXML private TextField brojDjeceString;
    @FXML private TextField brojStarihString;
    @FXML private TextField brojKucaString;
    @FXML private TextField brojPunktovaString;
    @FXML private TextField brojVozilaString;
    private static SistemZaNadzorController mainKontroler;
    private static int brojOdraslih = 0;
    private static int brojDjece = 0;
    private static int brojStarih = 0;
    private static int brojKuca = 0;
    private static int brojPunktova = 0;
    private static int brojVozila = 0;

    public static SistemZaNadzorController getMainKontroler()
    {
        return mainKontroler;
    }
    public static int getBrojOdraslih()
    {
        return brojOdraslih;
    }
    public static int getBrojDjece()
    {
        return brojDjece;
    }
    public static int getBrojStarih()
    {
        return brojStarih;
    }
    public static int getBrojKuca()
    {
        return brojKuca;
    }
    public static int getBrojPunktova()
    {
        return brojPunktova;
    }
    public static int getBrojVozila()
    {
        return brojVozila;
    }

    @FXML void startTheApplication(ActionEvent event) throws IOException // FXMLLoader.load() baca IOException
    {
        boolean success = true;
        try
        {
            readTextFields();

            if (brojOdraslih + brojStarih < brojKuca)
                throw new NedovoljnoStanovnika();

            else if (brojKuca <= 0)
                throw new NedovoljnoKuca();

            else if (brojVozila <= 0)
                throw new NedovoljnoVozila();

            else if (brojPunktova <= 0)
                throw new NedovoljnoPunktova();

            else if (brojStarih > INPUT_LIMIT || brojOdraslih > INPUT_LIMIT || brojDjece > INPUT_LIMIT || brojPunktova > INPUT_LIMIT || brojKuca > INPUT_LIMIT || brojVozila > INPUT_LIMIT)
                throw new PrekoracenjeGranica();
            else if (brojOdraslih < 0 || brojDjece < 0 || brojStarih < 0 || brojKuca < 0 || brojPunktova < 0 || brojVozila < 0)
                throw new IllegalArgumentException();
        }

        catch (NumberFormatException e)
        {
            clearTextFields();
            showPopUpWarning("Unesite cjelobrojne podatke.", "Neispravno uneseni podaci.", "Upozorenje");
            MyLogger.log(Level.INFO, LogDetails.INVALID_DATA);
            success = false;
        }

        catch (NedovoljnoStanovnika e)
        {
            clearTextFields();
            showPopUpWarning("Molimo unesite više odraslih i starih osoba nego kuća.", "Neispravno uneseni podaci.", "Upozorenje");
            MyLogger.log(Level.INFO, LogDetails.INS_PEOPLE);
            success = false;
        }

        catch (NedovoljnoKuca e)
        {
            clearTextFields();
            showPopUpWarning("Molimo unesite barem jednu kuću.", "Neispravno uneseni podaci.", "Upozorenje");
            MyLogger.log(Level.INFO, LogDetails.INS_HOUSES);
            success = false;
        }

        catch (NedovoljnoVozila e)
        {
            clearTextFields();
            showPopUpWarning("Molimo unesite barem jedno vozilo.", "Neispravno uneseni podaci.", "Upozorenje");
            MyLogger.log(Level.INFO, LogDetails.INS_VEHICLES);
            success = false;
        }

        catch (NedovoljnoPunktova e)
        {
            clearTextFields();
            showPopUpWarning("Molimo unesite barem jedan punk.", "Neispravno uneseni podaci.", "Upozorenje");
            MyLogger.log(Level.INFO, LogDetails.INS_CONTROL);
            success = false;
        }

        catch (PrekoracenjeGranica e)
        {
            clearTextFields();
            showPopUpWarning("Molimo unesite do 50 objekata i stanovnika svake vrste.", "Neispravno uneseni podaci.", "Upozorenje");
            MyLogger.log(Level.INFO, LogDetails.OVF_BUILDINGS);
            success = false;
        }

        catch (IllegalArgumentException e)
        {
            clearTextFields();
            showPopUpWarning("Molimo unesite pozitivne vrijednosti.", "Neispravno uneseni podaci.", "Upozorenje");
            MyLogger.log(Level.INFO, LogDetails.OVF_BUILDINGS);
            success = false;
        }

        catch (Exception e)
        {
            clearTextFields();
            showPopUpWarning("Nevalidan unos.", "Neispravno uneseni podaci.", "Upozorenje");
            MyLogger.log(Level.INFO, LogDetails.OVF_BUILDINGS);
            success = false;
        }

        if (success)
            setScene(event);
    }

    @FXML void exit(ActionEvent event)
    {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    private void setScene(ActionEvent event) throws IOException
    {
        new File(Putanje.APP_FOLDER).mkdir();
        ((Button) event.getSource()).getScene().getWindow().hide();
        Stage mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../MainScreen/SistemZaNadzor.fxml"));
        Parent root = loader.load();
        Scene sc = new Scene(root);
        sc.setFill(Color.TRANSPARENT);
        mainStage.setScene(sc);
        mainStage.setTitle("JavaKov-20");
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.getIcons().add(new Image("/resursi/coronavirus.png"));
        mainStage.setFullScreen(true);
        Main.myStage = mainStage;
        mainStage.show();
        mainKontroler = loader.getController();
        mainKontroler.iscrtajGrid();
        Simulacija sim = Simulacija.dohvatiSimulaciju();
        AzuriranjeGUI.kreirajAzuriranje().start();
    }

    private void readTextFields()
    {
        brojOdraslih = Integer.parseInt(brojOdraslihString.getText());
        brojDjece = Integer.parseInt(brojDjeceString.getText());
        brojStarih = Integer.parseInt(brojStarihString.getText());
        brojKuca = Integer.parseInt(brojKucaString.getText());
        brojPunktova = Integer.parseInt(brojPunktovaString.getText());
        brojVozila = Integer.parseInt(brojVozilaString.getText());
    }

    private void clearTextFields()
    {
        brojOdraslihString.clear();
        brojDjeceString.clear();
        brojStarihString.clear();
        brojKucaString.clear();
        brojPunktovaString.clear();
        brojVozilaString.clear();
    }

    private void showPopUpWarning(String content, String header, String title)
    {
        Alert newPopUp = new Alert(Alert.AlertType.WARNING,content, ButtonType.OK);
        newPopUp.setResizable(false);
        newPopUp.setHeaderText(header);
        newPopUp.setTitle(title);
        newPopUp.showAndWait();
    }
}




