package GUI.AmbulanceControl;

import enumi.TipElementa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import logger.MyLogger;
import objekti.Ambulanta;
import simulacija.Serijalizacija;
import simulacija.Simulacija;
import utils.Konstante;
import utils.LogDetails;
import utils.OrderedPair;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import static GUI.MainScreen.SistemZaNadzorController.getVelicinaGrada;

public class AmbulanceControlController
{
    @FXML
    private VBox vbox;

    @FXML
    private Button dugmeNova;

    public void addAll()
    {
        vbox.getChildren().clear();

        for (Ambulanta a : Simulacija.dohvatiSimulaciju().getAmbulante())
        {
            TextFlow flow = new TextFlow();

            Text text1 = new Text(" AMB: ");
            text1.setStyle("-fx-font-size: 12;" +
                    "-fx-font-weight: bold;");

            Text text2 = new Text("(" + a.getLokacijaX() + ", " + a.getLokacijaY() + ")");
            text2.setStyle("-fx-font-size: 12;");

            Text text3 = new Text("       \tKAPACITET: ");
            text3.setStyle("-fx-font-size: 12;" +
                    "-fx-font-weight: bold;");

            Text text4 = new Text(Integer.toString(a.getKapacitet()));
            text4.setStyle("-fx-font-size: 12;");

            flow.getChildren().addAll(text1, text2, text3, text4);
            vbox.getChildren().addAll(flow);
        }
    }

    @FXML
    void nova(ActionEvent event)
    {
        if (Serijalizacija.getSerialized())
        {
            MyLogger.log(Level.INFO, LogDetails.NEW_AMBULANCE_WHILE_DES);
            return;
        }

        int ukupnoNepopunjenih = 0;
        for (Ambulanta a : Simulacija.dohvatiSimulaciju().getAmbulante())
            if (a.getKapacitet() != 0)
                ukupnoNepopunjenih++;

        try
        {
            if (ukupnoNepopunjenih < Konstante.AMBULANTE_LIMIT)
            {
                int x = getVelicinaGrada();
                int y = getVelicinaGrada();
                boolean k1 = ThreadLocalRandom.current().nextBoolean();
                boolean k2 = ThreadLocalRandom.current().nextBoolean();

                if (k1 && k2)
                {
                    x = 1;
                    y = ThreadLocalRandom.current().nextInt(3, getVelicinaGrada() - 1);
                }

                else if (!k1 && k2)
                {
                    x = getVelicinaGrada();
                    y = ThreadLocalRandom.current().nextInt(3, getVelicinaGrada() - 1);
                }

                else if (k1 && !k2)
                {
                    x = ThreadLocalRandom.current().nextInt(3, getVelicinaGrada() - 1);
                    y = 1;

                }

                else if (!k1 && !k2)
                {
                    x = ThreadLocalRandom.current().nextInt(3, getVelicinaGrada() - 1);
                    y = getVelicinaGrada();
                }

                if (Simulacija.dohvatiSimulaciju().objekatNaPolju(new OrderedPair(x, y, TipElementa.OBJEKAT)))
                    nova(event);

                Simulacija.dohvatiSimulaciju().getAmbulante().add(new Ambulanta(x, y));
                Simulacija.dohvatiSimulaciju().getLokacijeObjekata().add(new OrderedPair(x, y, TipElementa.OBJEKAT));
                Simulacija.dohvatiSimulaciju().dodajZauzetoPolje(new OrderedPair(x, y, TipElementa.OBJEKAT));
                addAll();

                return;
            }

            else
            {
                MyLogger.log(Level.INFO, LogDetails.TOO_MANY_AMBULANCES);
                return;
            }
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.ERROR_NEW_AMBULANCE);
        }
    }

    @FXML
    void zatvori(ActionEvent event) { ((Button) event.getSource()).getScene().getWindow().hide(); }
}
