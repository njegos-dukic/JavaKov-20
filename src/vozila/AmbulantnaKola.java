package vozila;

import enumi.TipElementa;
import javafx.scene.image.ImageView;
import logger.MyLogger;
import objekti.Ambulanta;
import osobe.Stanovnik;
import simulacija.Simulacija;
import utils.Konstante;
import utils.LogDetails;
import utils.OrderedPair;
import java.io.FileWriter;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import static GUI.MainScreen.SistemZaNadzorController.getVelicinaGrada;
import static simulacija.NajkracaPutanja.*;
import java.util.logging.Level;

public class AmbulantnaKola extends Thread implements Serializable
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;
    private int lokacijaX;
    private int lokacijaY;
    private final int lokacijaAmbulanteX;
    private final int lokacijaAmbulanteY;
    private Ambulanta ambulanta;
    private transient ImageView ikonica = new ImageView("/resursi/ambulance.png");
    private ArrayList<Integer> putanja = new ArrayList<>();
    private boolean uPutu = false;
    private boolean aktivan = false;
    private Stanovnik trenutnoPrevozimo = null;
    private boolean dead = false;

    @Override
    public void run()
    {
        while(!dead)
        {
            if(aktivan)
                pomjeri();

            try
            {
                Thread.sleep(Konstante.CAR_MOVE_INTERVAL);
            }

            catch (Exception e)
            {
                MyLogger.log(Level.INFO, LogDetails.CAR_MOVE_EXCEPTION);
            }
        }
    }

    public AmbulantnaKola(Ambulanta a)
    {
        ambulanta = a;
        lokacijaX = lokacijaAmbulanteX = a.getLokacijaX();
        lokacijaY = lokacijaAmbulanteY = a.getLokacijaY();
    }

    public void deserijalizuj()
    {
        ikonica = new ImageView("/resursi/ambulance.png");
        aktivan = false;
        this.start();
    }

    public void zaustavi() { dead = true; }

    public int getLokacijaX() { return lokacijaX; }
    public int getLokacijaY() { return lokacijaY; }
    public ImageView getIkonica() { return ikonica; }
    public boolean getUPutu() { return uPutu; }
    public void setUPutu(boolean value) { uPutu = value; }

    public synchronized void pomjeri()
    {
        if (!uPutu && uAmbulanti())
            return;

        else if (putanja.size() != 0)
        {
            int p = putanja.get(0);

            pomjeri(p);

            if (putanja.size() > 0)
                putanja.remove(0);

            return;
        }

        else if (trenutnoPrevozimo != null && uPutu && putanja.size() == 0)
        {
            trenutnoPrevozimo.vratiStanovnikaUAmbulantu(ambulanta, this);
            trenutnoPrevozimo = null;
        }

        else if (trenutnoPrevozimo == null && uPutu && putanja.size() == 0)
        {
            uPutu = false;
        }
    }

    public synchronized void pomjeri(int lokacija)
    {
        int x = lokacija % getVelicinaGrada();
        int y = lokacija / getVelicinaGrada();

        if (lokacija % getVelicinaGrada() == 0)
        {
            x = getVelicinaGrada();
            y -= 1;
        }

        lokacijaX = x;
        lokacijaY = y;
    }

    public void posaljiPoStanovnika(Stanovnik stanovnik, Ambulanta ciljnaAmbulanta, ArrayList<Integer> putanjaDoZarazenogIn)
    {
        trenutnoPrevozimo = stanovnik;
        ambulanta = ciljnaAmbulanta;
        aktivan = uPutu = true;
        putanja.addAll(putanjaDoZarazenogIn);
    }

    public void vratiUAmbulantu(Ambulanta ciljnaAmbulanta)
    {
        int lokacijaAmbulante = ciljnaAmbulanta.getLokacija();
        putanja.clear();
        putanja = najkracaPutanja(trenutnaLokacija(), lokacijaAmbulante);
    }

    public int trenutnaLokacija()
    {
        return lokacijaY * getVelicinaGrada() + lokacijaX;
    }

    public boolean uAmbulanti()
    {
        for (Ambulanta a : Simulacija.dohvatiSimulaciju().getAmbulante())
            if (a.getLokacijaX() == lokacijaX && a.getLokacijaY() == lokacijaY)
                return true;

        return false;
    }

    public boolean naObjektu()
    {
        OrderedPair lokacija = new OrderedPair(lokacijaX, lokacijaY, TipElementa.OSOBA);
        for (OrderedPair o : Simulacija.dohvatiSimulaciju().getLokacijeObjekata())
            if (lokacija.equalsWithoutType(o))
                return true;

        return false;
    }

    public void pokreni()
    {
        aktivan = true;
    }

    public void info(String filename)
    {
        try
        {
            FileWriter kolaWriter = new FileWriter(filename, true);
            kolaWriter.append("Kola:");
            kolaWriter.append("\n\tLokacija X: " + lokacijaX);
            kolaWriter.append("\n\tLokacija Y: " + lokacijaY);
            kolaWriter.append("\n\tLokacija ambulante X: " + lokacijaAmbulanteX);
            kolaWriter.append("\n\tLokacija ambulante Y: " + lokacijaAmbulanteY);
            kolaWriter.append("\n\tAmbulanta: " + ambulanta.getLokacija());
            kolaWriter.append("\n\tPutanja: " + putanja);
            kolaWriter.append("\n\tU putu: " + uPutu);
            kolaWriter.append("\n\tAktivan: " + aktivan);
            if (trenutnoPrevozimo != null)
                kolaWriter.append("\n\tTrenutno prevozi stanovnika: " + trenutnoPrevozimo.getID());
            else
                kolaWriter.append("\n\tTrenutno prevozi stanovnika: " + null);
            kolaWriter.append("\n\n");
            kolaWriter.flush();
            kolaWriter.close();
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.VEHICLE_INFO_EXC);
        }
    }
}
