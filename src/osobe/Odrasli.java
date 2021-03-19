package osobe;

import GUI.MainScreen.SistemZaNadzorController;
import enumi.Generacija;
import enumi.Pol;
import enumi.SmjerKretanja;
import javafx.scene.image.ImageView;
import utils.Konstante;
import java.io.Serial;
import java.util.concurrent.ThreadLocalRandom;
import static GUI.MainScreen.SistemZaNadzorController.getVelicinaGrada;

public class Odrasli extends Stanovnik
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;
    private static int brojacOdraslih = 1;

    public Odrasli()
    {
        ID = Konstante.ID_ODRASLIH + brojacOdraslih;
        generacija = Generacija.ODRASLI;
        ime = "Odrasli";
        prezime = "" + brojacOdraslih;
        godine = ThreadLocalRandom.current().nextInt(18, 65);
        godinaRodjenja = 2020 - godine;
        temperatura = 0;

        if (ThreadLocalRandom.current().nextBoolean())
            pol = Pol.MUSKI;
        else
            pol = Pol.ZENSKI;

        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/man.png");
        else
            ikonica = new ImageView("/resursi/woman.png");

        radijusKretanja = (int) Math.round(SistemZaNadzorController.getVelicinaGrada() * 0.25);
        smjerGUI = SmjerKretanja.MIROVANJE;
        brojacOdraslih++;
    }

    public void postaviRadijuseHorizontalno()
    {
        if (lokacijaKuceX - radijusKretanja <= 1)
        {
            radijusLijevo = lokacijaKuceX - 2;
            radijusDesno = 2 * radijusKretanja - radijusLijevo;
            return;
        }
        else
            radijusLijevo = radijusKretanja;

        if (lokacijaKuceX + radijusKretanja >= getVelicinaGrada())
        {
            radijusDesno = getVelicinaGrada() - lokacijaKuceX - 1;
            radijusLijevo = 2 * radijusKretanja - radijusDesno;
        }
        else
            radijusDesno = radijusKretanja;
    }

    public void postaviRadijuseVertikalno()
    {
        if (lokacijaKuceY - radijusKretanja <= 1)
        {
            radijusGore = lokacijaKuceY - 2;
            radijusDole = 2 * radijusKretanja - radijusGore;
            return;
        }
        else
            radijusGore = radijusKretanja;

        if (lokacijaKuceY + radijusKretanja >= getVelicinaGrada())
        {
            radijusDole = getVelicinaGrada() - lokacijaKuceY - 1;
            radijusGore = 2 * radijusKretanja - radijusDole;
        }
        else
            radijusDole = radijusKretanja;
    }

    public void postaviRadijuse()
    {
        postaviRadijuseVertikalno();
        postaviRadijuseHorizontalno();
    }

    public void postaviZarazenIkonicu()
    {
        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/man_infected.png");
        else
            ikonica = new ImageView("/resursi/woman_infected.png");
    }

    public void postaviPotencijalniIkonicu()
    {
        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/man_potential.png");
        else
            ikonica = new ImageView("/resursi/woman_potential.png");
    }

    public void postaviIzlijecenIkonicu()
    {
        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/man_cured.png");
        else
            ikonica = new ImageView("/resursi/woman_cured.png");
    }

    public void postaviRegularanIkonicu()
    {
        if (pol.equals(Pol.MUSKI))
            ikonica = new ImageView("/resursi/man.png");
        else
            ikonica = new ImageView("/resursi/woman.png");
    }
}
