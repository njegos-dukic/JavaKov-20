package osobe;

import enumi.Generacija;
import enumi.Pol;
import enumi.SmjerKretanja;
import javafx.scene.image.ImageView;
import utils.Konstante;
import java.io.Serial;
import java.util.concurrent.ThreadLocalRandom;

public class Dijete extends Stanovnik
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;
    private static int brojacDjece = 1;

    public Dijete()
    {
        ID = Konstante.ID_DJECE + brojacDjece;
        generacija = Generacija.DIJETE;
        ime = "Dijete";
        prezime = "" + brojacDjece;
        godine = ThreadLocalRandom.current().nextInt(1, 18);
        godinaRodjenja = 2020 - godine;
        temperatura = 0;

        if (ThreadLocalRandom.current().nextBoolean())
            pol = Pol.MUSKI;
        else
            pol = Pol.ZENSKI;

        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/boy.png");
        else
            ikonica = new ImageView("/resursi/girl.png");

        smjerGUI = SmjerKretanja.MIROVANJE;
        radijusKretanja = radijusDesno = radijusGore = radijusDole = radijusLijevo = 30;
        brojacDjece++;
    }

    public void postaviZarazenIkonicu()
    {
        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/boy_infected.png");
        else
            ikonica = new ImageView("/resursi/girl_infected.png");
    }

    public void postaviPotencijalniIkonicu()
    {
        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/boy_potential.png");
        else
            ikonica = new ImageView("/resursi/girl_potential.png");
    }

    public void postaviIzlijecenIkonicu()
    {
        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/boy_cured.png");
        else
            ikonica = new ImageView("/resursi/girl_cured.png");
    }

    public void postaviRegularanIkonicu()
    {
        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/boy.png");
        else
            ikonica = new ImageView("/resursi/girl.png");
    }

    public void postaviRadijuseVertikalno() { return; }
    public void postaviRadijuseHorizontalno() { return; }
    public void postaviRadijuse() { return; }
}
