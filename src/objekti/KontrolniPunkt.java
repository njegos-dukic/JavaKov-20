package objekti;

import javafx.scene.image.ImageView;
import logger.MyLogger;
import osobe.Stanovnik;
import utils.LogDetails;

import java.io.FileWriter;
import java.io.Serial;
import java.util.logging.Level;

public class KontrolniPunkt extends Objekat
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;

    public KontrolniPunkt(int x, int y)
    {
        lokacijaX = x;
        lokacijaY = y;
        ikonica = new ImageView("/resursi/control.png");
    }

    public void deserijalizuj()
    {
        ikonica = new ImageView("/resursi/control.png");
    }

    public static double ocitajTemperaturu(Stanovnik s) { return s.getTemperatura(); }

    public void info(String filename)
    {
        try
        {
            FileWriter punktWriter = new FileWriter(filename, true);
            punktWriter.append("Punkt:");
            punktWriter.append("\n\tLokacija punkta X: " + lokacijaX);
            punktWriter.append("\n\tLokacij punkta Y: " + lokacijaY);
            punktWriter.append("\n\n");
            punktWriter.flush();
            punktWriter.close();
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.CONTROL_INFO_EXC);
        }
    }
}
