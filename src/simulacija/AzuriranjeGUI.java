package simulacija;

import GUI.MainScreen.SistemZaNadzorController;
import GUI.StartScreen.PocetniEkranController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import logger.MyLogger;
import objekti.Ambulanta;
import objekti.KontrolniPunkt;
import objekti.Kuca;
import osobe.Stanovnik;
import utils.Konstante;
import utils.LogDetails;
import vozila.AmbulantnaKola;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import static GUI.MainScreen.SistemZaNadzorController.getVelicinaGrada;

public class AzuriranjeGUI extends Thread
{
    private static AzuriranjeGUI azuriranje = null; // Singleton
    private static CopyOnWriteArrayList<Kuca> kuce = Simulacija.dohvatiSimulaciju().getKuce();
    private static CopyOnWriteArrayList<KontrolniPunkt> punktovi = Simulacija.dohvatiSimulaciju().getPunktovi();
    private static CopyOnWriteArrayList<Ambulanta> ambulante = Simulacija.dohvatiSimulaciju().getAmbulante();
    private static CopyOnWriteArrayList<AmbulantnaKola> kola = Simulacija.dohvatiSimulaciju().getAmbulantnaKola();
    private static final SistemZaNadzorController mainKontroler = PocetniEkranController.getMainKontroler();
    private final GridPane mapaGrada = mainKontroler.getMapaGrada();
    private final VBox skrolLista = mainKontroler.getListaStanovnika();
    private static boolean zatvorenaAplikacija = false;
    private static boolean pauzirano = false;
    private static final int i = 0;

    private AzuriranjeGUI() { }

    public static AzuriranjeGUI kreirajAzuriranje()
    {
        if (azuriranje == null)
            azuriranje = new AzuriranjeGUI();

        return azuriranje;
    }

    @Override
    public void run()
    {
        ispisiPopulaciju();
        while (!zatvorenaAplikacija)
        {
            iscrtajGUI();

            try
            {
                Thread.sleep(Konstante.GUI_UPDATE_WAIT);
            }
            catch (Exception c)
            {
                MyLogger.log(Level.INFO, LogDetails.GUI_EXCEPTION);
            }
        }
    }

    public void iscrtajGUI()
    {
        Platform.runLater(() ->
        {
            Node infoNode = mapaGrada.getChildren().get(0);
            mapaGrada.getChildren().clear();
            mapaGrada.getChildren().add(0, infoNode);

            skrolLista.getChildren().clear();

            for (Kuca k : kuce)
            {
                k.getIkonica().setFitWidth((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 5);
                k.getIkonica().setFitHeight((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 5);
                k.getIkonica().setPreserveRatio(true);
                mapaGrada.add(k.getIkonica(), k.getLokacijaX(), k.getLokacijaY());
            }

            for (KontrolniPunkt p : punktovi)
            {
                p.getIkonica().setFitWidth((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 5);
                p.getIkonica().setFitHeight((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 5);
                p.getIkonica().setPreserveRatio(true);
                mapaGrada.add(p.getIkonica(), p.getLokacijaX(), p.getLokacijaY());
            }

            for (Ambulanta a : ambulante)
            {
                a.getIkonica().setFitWidth((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 5);
                a.getIkonica().setFitHeight((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 5);
                a.getIkonica().setPreserveRatio(true);
                mapaGrada.add(a.getIkonica(), a.getLokacijaX(), a.getLokacijaY());
            }

            for (AmbulantnaKola ak : kola)
            {
                if (ak.naObjektu())
                    continue;

                ak.getIkonica().setFitWidth((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 5);
                ak.getIkonica().setFitHeight((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 5);
                ak.getIkonica().setPreserveRatio(true);
                mapaGrada.add(ak.getIkonica(), ak.getLokacijaX(), ak.getLokacijaY());
            }

            for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            {
                skrolLista.getChildren().addAll(textFlow(s));

                if (s.naObjektu() || s.uKolima())
                    continue;

                s.getIkonica().setFitWidth((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 10);
                s.getIkonica().setFitHeight((int) mapaGrada.getWidth() / (double) getVelicinaGrada() - 10);
                s.getIkonica().setPreserveRatio(true);
                if (s.getLokacijaX() >= 1 && s.getLokacijaX() <= getVelicinaGrada() && s.getLokacijaY() >= 1 && s.getLokacijaY() <= getVelicinaGrada())
                    mapaGrada.add(s.getIkonica(), s.getLokacijaX(), s.getLokacijaY());
            }
        });
    }

    public static void ispisiPopulaciju()
    {
        Platform.runLater(() -> mainKontroler.getBrojStanovnika().setText(Integer.toString(Simulacija.dohvatiSimulaciju().getBrojStanovnika())));
    }

    public static void zaustavi() { zatvorenaAplikacija = true; }

    public static void obrisiTextBoxove()
    {
        mainKontroler.getBrojIzlijecenih().setText("");
        mainKontroler.getBrojZarazenih().setText("");
        Platform.runLater(() ->
        {
            mainKontroler.getBrojStanovnika().setText("");
        });
    }

    public static void deserijalizuj(Simulacija sim)
    {
        ispisiPopulaciju();
        kuce = Simulacija.dohvatiSimulaciju().getKuce();
        punktovi = Simulacija.dohvatiSimulaciju().getPunktovi();
        ambulante = Simulacija.dohvatiSimulaciju().getAmbulante();
        kola = Simulacija.dohvatiSimulaciju().getAmbulantnaKola();
    }

    public static void pauziraj()
    {
        pauzirano = true;
    }

    public static void nastavi()
    {
        pauzirano = false;
    }

    private TextFlow textFlow(Stanovnik s)
    {
        TextFlow flow = new TextFlow();

        Text text1 = new Text(" ST: ");
        text1.setStyle("-fx-font-size: 12;" +
                "-fx-font-weight: bold;");

        Text text2 = new Text(s.getIme() + " " + s.getPrezime());
        text2.setStyle("-fx-font-size: 12;");

        Text text3 = new Text("  ID: ");
        text3.setStyle("-fx-font-size: 12;" +
                "-fx-font-weight: bold;");

        Text text4 = new Text(Integer.toString(s.getID()));
        text4.setStyle("-fx-font-size: 12;");

        Text text5 = new Text("  SMJER: ");
        text5.setStyle("-fx-font-size: 12;" +
                "-fx-font-weight: bold;");

        Text text6 = new Text(s.getSmjerGUI().toString());
        text6.setStyle("-fx-font-size: 12;");

        Text text7 = new Text("  LOKACIJA: ");
        text7.setStyle("-fx-font-size: 12;" +
                "-fx-font-weight: bold;");

        Text text8 = new Text("(" + s.getLokacijaX() + ", " + s.getLokacijaY() + ")");
        text8.setStyle("-fx-font-size: 12;");

        flow.getChildren().addAll(text1, text2, text3, text4, text7, text8, text5, text6);

        if (s.getZarazen())
        {
            Text text9 = new Text(" [Zaražen]");
            text9.setStyle("-fx-font-size: 10;" +
                    "-fx-font-weight: bold;" +
                    "-fx-fill: red");

            flow.getChildren().add(text9);
        }

        else if (s.getPotencijalan())
        {
            Text text9 = new Text(" [Karantin]");
            text9.setStyle("-fx-font-size: 10;" +
                    "-fx-font-weight: bold;" +
                    "-fx-fill: goldenrod");

            flow.getChildren().add(text9);
        }

        else if (s.getIzlijecenNaPutuDoKuce())
        {
            Text text9 = new Text(" [Izliječen]");
            text9.setStyle("-fx-font-size: 10;" +
                    "-fx-font-weight: bold;" +
                    "-fx-fill: green");

            flow.getChildren().add(text9);
        }

        return flow;
    }
}
