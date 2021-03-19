package objekti;

import GUI.StartScreen.PocetniEkranController;
import javafx.scene.image.ImageView;
import logger.MyLogger;
import utils.LogDetails;

import java.io.FileWriter;
import java.io.Serial;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import static GUI.MainScreen.SistemZaNadzorController.getVelicinaGrada;

public class Ambulanta extends Objekat
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;
    private int kapacitet;
    private static int brojacAmbulanti = 0;

    public Ambulanta(int x, int y)
    {
        lokacijaX = x;
        lokacijaY = y;
        ikonica = new ImageView("/resursi/hospital.png");
        kapacitet = (PocetniEkranController.getBrojDjece() + PocetniEkranController.getBrojOdraslih() + PocetniEkranController.getBrojStarih()) * ThreadLocalRandom.current().nextInt(10, 16) / 100;
        brojacAmbulanti += 1;
    }

    public void deserijalizuj()
    {
        ikonica = new ImageView("/resursi/hospital.png");
    }

    public static int getBrojAmbulanti() { return brojacAmbulanti; }

    public int getKapacitet()
    {
        return kapacitet;
    }

    public void smanjiKapacitetZaJednoMjesto()
    {
        if (kapacitet > 0)
            kapacitet -= 1;
    }

    public int getLokacija()
    {
        return lokacijaY * getVelicinaGrada() + lokacijaX;
    }

    public void povecajKapacitetZaJednoMjesto()
    {
        kapacitet += 1;
    }

    public void info(String filename)
    {
        try
        {
            FileWriter ambulanteWriter = new FileWriter(filename, true);
            ambulanteWriter.append("Ambulanta:");
            ambulanteWriter.append("\n\tLokacija ambulante X: " + lokacijaX);
            ambulanteWriter.append("\n\tLokacija ambulante Y: " + lokacijaY);
            ambulanteWriter.append("\n\tKapacitet: " + kapacitet);
            ambulanteWriter.append("\n\tBrojac ambulanti: " + brojacAmbulanti);
            ambulanteWriter.append("\n\n");
            ambulanteWriter.flush();
            ambulanteWriter.close();
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.AMBULANCE_INFO_EXC);
        }
    }
}
