package objekti;

import javafx.scene.image.ImageView;
import logger.MyLogger;
import osobe.Stanovnik;
import simulacija.Simulacija;
import utils.Konstante;
import utils.LogDetails;

import java.io.FileWriter;
import java.io.Serial;
import java.util.logging.Level;

public class Kuca extends Objekat
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;

    public int IDKuce;
    private int brojacUkucana = 0;
    private static int brojacKuca = 1;

    public Kuca(int x, int y)
    {
        lokacijaX = x;
        lokacijaY = y;
        ikonica = new ImageView("/resursi/house.png");
        IDKuce = Konstante.ID_KUCA + brojacKuca;
        brojacKuca++;
    }

    public Kuca(int x, int y, int ID, int brojacUkucana)
    {
        lokacijaX = x;
        lokacijaY = y;
        ikonica = new ImageView("/resursi/house.png");
        brojacUkucana = brojacUkucana;
        IDKuce = ID;
        brojacKuca++;
        Simulacija.dohvatiSimulaciju().getKuce().add(this);
    }

    public void deserijalizuj()
    {
        ikonica = new ImageView("/resursi/house.png");
        brojacKuca++;
    }

    public void dodajUkucana(Stanovnik s)
    {
        brojacUkucana++;
        s.setIDKuce(IDKuce);
        s.postaviKucuPriInicijalizaciji(this);
        s.setLokacijaKuceX(this.lokacijaX);
        s.setLokacijaKuceY(this.lokacijaY);
    }

    public static void setBrojacKuca(int value)
    {
        brojacKuca = value;
    }

    public void info(String filename)
    {
        try
        {
            FileWriter kuceWriter = new FileWriter(filename, true);
            kuceWriter.append("Kuca:");
            kuceWriter.append("\n\tLokacija kuce X: " + lokacijaX);
            kuceWriter.append("\n\tLokacij kuce Y: " + lokacijaY);
            kuceWriter.append("\n\tID Kuce: " + IDKuce);
            kuceWriter.append("\n\tBrojac ukucana: " + brojacUkucana);
            kuceWriter.append("\n\tBrojac kuca: " + brojacKuca);
            kuceWriter.append("\n\n");
            kuceWriter.flush();
            kuceWriter.close();
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.HOUSE_INFO_EXC);
        }
    }
}
