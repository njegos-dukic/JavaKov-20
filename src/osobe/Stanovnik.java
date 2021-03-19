package osobe;

import enumi.Generacija;
import enumi.Pol;
import enumi.SmjerKretanja;
import enumi.TipElementa;
import javafx.scene.image.ImageView;
import logger.MyLogger;
import objekti.Ambulanta;
import objekti.Kuca;
import simulacija.Alarm;
import simulacija.Simulacija;
import utils.Konstante;
import utils.LogDetails;
import utils.OrderedPair;
import vozila.AmbulantnaKola;
import java.io.FileWriter;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import static GUI.MainScreen.SistemZaNadzorController.getVelicinaGrada;
import static enumi.SmjerKretanja.*;
import static simulacija.NajkracaPutanja.najkracaPutanja;

public abstract class Stanovnik extends Thread implements Serializable
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;

    protected int ID;
    protected String ime;
    protected String prezime;
    protected int godine;
    protected int godinaRodjenja;
    protected Pol pol;
    protected int IDKuce;
    protected int lokacijaKuceX;
    protected int lokacijaKuceY;
    protected double temperatura;
    protected transient ImageView ikonica = new ImageView("/resursi/loading.png");
    protected int radijusKretanja;
    protected int radijusGore;
    protected int radijusDole;
    protected int radijusLijevo;
    protected int radijusDesno;
    protected int lokacijaX;
    protected int lokacijaY;
    protected boolean kretanjeOmoguceno = true;
    protected SmjerKretanja smjerGUI;
    protected ArrayList<Integer> putanja = new ArrayList<>();
    protected boolean zarazen = false;
    protected boolean potencijalan = false;
    protected boolean aktivan = false;
    protected boolean izlijecenNaPutuDoKuce = false;
    protected ArrayList<Double> poslednjeTemperature = new ArrayList<>(3);
    protected Ambulanta trenutnaAmbulanta = null;
    protected int brojacNeuspjesnihPomjeranja = 0;
    protected boolean dead = false;
    protected Generacija generacija;

    public int getLokacijaX() { return lokacijaX; }
    public int getLokacijaY() { return lokacijaY; }
    public ImageView getIkonica() { return ikonica; }
    public SmjerKretanja getSmjerGUI() { return smjerGUI; }
    public int getID() { return ID; }
    public String getIme() { return ime; }
    public String getPrezime() { return prezime; }
    public Generacija getGeneracija() { return generacija; }
    public Pol getPol() { return pol; }
    public int getIDKuce() { return IDKuce; }
    public double getTemperatura() { return temperatura; }
    public boolean getZarazen() { return zarazen; }
    public boolean getPotencijalan() { return potencijalan; }
    public boolean getIzlijecenNaPutuDoKuce() { return izlijecenNaPutuDoKuce; }
    public void setIDKuce(int IDKuce) { this.IDKuce = IDKuce; }
    public void setLokacijaKuceX(int x) { this.lokacijaKuceX = x; }
    public void setLokacijaKuceY(int y) { this.lokacijaKuceY = y; }
    public void setZarazen(boolean value) { this.zarazen = value; }
    public void zaustavi() { dead = true; }
    public void setPotencijalan(boolean value)
    {
        if (value && izlijecenNaPutuDoKuce)
            izlijecenNaPutuDoKuce = false;

        this.potencijalan = value;
    }

    @Override
    public void run()
    {
        while(!dead)
        {
            try
            {
                if(aktivan)
                    pomjeri();

                Thread.sleep(Konstante.MOVE_INTERVAL);
            }
            catch (Exception e)
            {
                MyLogger.log(Level.INFO, LogDetails.RACE_CONDITION_CELL);
            }
        }
    }

    private synchronized boolean poljeDostupno(OrderedPair zeljenaLokacija, SmjerKretanja smjer)
    {
        if (lokacijaKuceX == zeljenaLokacija.getX() && lokacijaKuceY == zeljenaLokacija.getY())
            return true;

        boolean returnValue;

        switch (smjer)
        {
            case DESNO:
                if (lokacijaX == getVelicinaGrada() - 1)
                    return false;
                break;

            case LIJEVO:
                if (lokacijaX == 2)
                    return false;
                break;

            case GORE:
                if (lokacijaY == 2)
                    return false;
                break;

            case DOLE:
                if (lokacijaY == getVelicinaGrada() - 1)
                    return false;
                break;
        }

        returnValue = !Simulacija.dohvatiSimulaciju().objekatNaPolju(zeljenaLokacija) || vlastitaKuca(zeljenaLokacija);
        returnValue = returnValue && provjeriRadijus(zeljenaLokacija, smjer);

        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
        {
            returnValue = returnValue && !this.drugiStanovnikURadijusu(zeljenaLokacija, s);
            returnValue = returnValue && !this.drugiStanovnikNaPolju(zeljenaLokacija, s);
        }

        return returnValue;
    }

    private synchronized boolean drugiStanovnikNaPolju(OrderedPair zeljenaLokacija, Stanovnik s)
    {
        return zeljenaLokacija.equalsWithoutType(new OrderedPair(s.lokacijaX, s.lokacijaY, TipElementa.OSOBA));
    }

    private synchronized boolean drugiStanovnikURadijusu(OrderedPair zeljenaLokacija, Stanovnik s)
    {
        if (s.uKuci())
            return false;

        else if (this.IDKuce == s.IDKuce) // Ukucani iz iste kuce.
            return false;

        else if (((generacija.equals(Generacija.ODRASLI) || generacija.equals(Generacija.DIJETE)) && s.getGeneracija().equals(Generacija.DIJETE))
                || (generacija.equals(Generacija.DIJETE) && (s.getGeneracija().equals(Generacija.ODRASLI) || s.getGeneracija().equals(Generacija.DIJETE))))
            return false;

        return (zeljenaLokacija.daLiJeURadijusu(new OrderedPair(s.lokacijaX, s.lokacijaY, TipElementa.OSOBA), 2));
    }

    private boolean vlastitaKuca(OrderedPair zeljenaLokacija)
    {
        return zeljenaLokacija.equalsWithoutType(new OrderedPair(this.lokacijaKuceX, this.lokacijaKuceY, TipElementa.OBJEKAT));
    }

    private synchronized boolean provjeriRadijus(OrderedPair novaLokacija, SmjerKretanja smjer)
    {
        if (smjer.equals(DESNO))
            if (Math.abs(novaLokacija.getX() - this.lokacijaKuceX) <= radijusDesno)
                return true;

        if (smjer.equals(LIJEVO))
            if (Math.abs(novaLokacija.getX() - this.lokacijaKuceX) <= radijusLijevo)
                return true;

        if (smjer.equals(DOLE))
            if (Math.abs(novaLokacija.getY() - this.lokacijaKuceY) <= radijusDole)
                return true;

        if (smjer.equals(GORE))
            return Math.abs(novaLokacija.getY() - this.lokacijaKuceY) <= radijusGore;

        return false;
    }

    public synchronized void pomjeri()
    {
        if (!kretanjeOmoguceno && uKuci())
        {
            smjerGUI = MIROVANJE;
            return;
        }

        else if (!zarazen && uKuci() && !kretanjeOmoguceno)
        {
            kretanjeOmoguceno = true;
            return;
        }

        else if (!kretanjeOmoguceno && !uKuci())
        {
            if (potencijalan && putanja.size() != 0)
            {
                int p = putanja.get(0);

                if(poljeDostupno(p))
                {
                    pomjeri(p);

                    if (uKuci())
                        putanja.clear();

                    if (putanja.size() > 0)
                        putanja.remove(0);
                }
            }

            return;
        }

        else if (izlijecenNaPutuDoKuce && putanja.size() != 0)
        {
            int p = putanja.get(0);

            if (poljeDostupno(p))
            {
                pomjeri(p);

                if (uKuci())
                    putanja.clear();

                if (putanja.size() > 0)
                    putanja.remove(0);

                if (putanja.size() == 0)
                {
                    postaviRegularanIkonicu();
                    postaviIzlijecene();
                    izlijecenNaPutuDoKuce = false;
                }
            }

            return;
        }

        else if (putanja.size() != 0)
        {
            int p = putanja.get(0);

            if (poljeDostupno(p))
            {
                pomjeri(p);

                if (uKuci())
                    putanja.clear();

                if (putanja.size() > 0)
                    putanja.remove(0);

                if (putanja.size() == 0)
                {
                    postaviRegularanIkonicu();
                    izlijecenNaPutuDoKuce = false;
                }
            }

            return;
        }

        SmjerKretanja smjer = randomPomjeraj();

        if (zarazen)
            smjer = MIROVANJE;

        if (smjer.equals(DESNO) && poljeDostupno(new OrderedPair(lokacijaX + 1, lokacijaY, TipElementa.OSOBA), DESNO))
        {
            lokacijaX++;
            Simulacija.dohvatiSimulaciju().dodajZauzetoPolje(new OrderedPair(lokacijaX + 1, lokacijaY, TipElementa.OSOBA));
            Simulacija.dohvatiSimulaciju().ukloniStanovnikaSaZauzetogPolja(new OrderedPair(lokacijaX, lokacijaY, TipElementa.OSOBA));
            smjerGUI = DESNO;
        }

        else if (smjer.equals(SmjerKretanja.LIJEVO) && poljeDostupno(new OrderedPair(lokacijaX - 1, lokacijaY, TipElementa.OSOBA), LIJEVO))
        {
            lokacijaX--;
            Simulacija.dohvatiSimulaciju().dodajZauzetoPolje(new OrderedPair(lokacijaX - 1, lokacijaY, TipElementa.OSOBA));
            Simulacija.dohvatiSimulaciju().ukloniStanovnikaSaZauzetogPolja(new OrderedPair(lokacijaX, lokacijaY, TipElementa.OSOBA));
            smjerGUI = LIJEVO;
        }

        else if (smjer.equals(SmjerKretanja.GORE) && poljeDostupno(new OrderedPair(lokacijaX, lokacijaY - 1, TipElementa.OSOBA), GORE))
        {
            lokacijaY--;
            Simulacija.dohvatiSimulaciju().dodajZauzetoPolje(new OrderedPair(lokacijaX, lokacijaY - 1, TipElementa.OSOBA));
            Simulacija.dohvatiSimulaciju().ukloniStanovnikaSaZauzetogPolja(new OrderedPair(lokacijaX, lokacijaY, TipElementa.OSOBA));
            smjerGUI = GORE;
        }

        else if (smjer.equals(SmjerKretanja.DOLE) && poljeDostupno(new OrderedPair(lokacijaX, lokacijaY + 1, TipElementa.OSOBA), DOLE))
        {
            lokacijaY++;
            Simulacija.dohvatiSimulaciju().dodajZauzetoPolje(new OrderedPair(lokacijaX, lokacijaY + 1, TipElementa.OSOBA));
            Simulacija.dohvatiSimulaciju().ukloniStanovnikaSaZauzetogPolja(new OrderedPair(lokacijaX, lokacijaY, TipElementa.OSOBA));
            smjerGUI = DOLE;
        }

        else if (smjer.equals(SmjerKretanja.MIROVANJE))
            smjerGUI = MIROVANJE;
    }

    private boolean poljeDostupno(int lokacija)
    {
        int y = lokacija / getVelicinaGrada();
        int x = lokacija % getVelicinaGrada();

        if (lokacija % getVelicinaGrada() == 0)
        {
            x = getVelicinaGrada();
            y -= 1;
        }

        if (x == lokacijaKuceX && y == lokacijaKuceY)
            return true;

        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            if (s.getLokacijaX() == x && s.getLokacijaY() == y && !s.getZarazen())
            {
                brojacNeuspjesnihPomjeranja++;
                s.vratiKuci();
                if (brojacNeuspjesnihPomjeranja == Konstante.DEADLOCK_CONSTANT)
                {
                    brojacNeuspjesnihPomjeranja = 0;
                    return true;
                }
                return false;
            }

        return true;
    }

    public void postaviMjerenjeUBolnici(double t)
    {
        if (poslednjeTemperature.size() == 3)
            poslednjeTemperature.remove(0);

        poslednjeTemperature.add(t);

        if (poslednjeTemperature.size() == 3)
        {
            double avg = 0;
            for (int i = 0; i < 3; i++)
                avg += poslednjeTemperature.get(i);

            if (avg / (double) 3 < Konstante.TEMPERATURE_LIMIT)
            {
                Simulacija.dohvatiSimulaciju().decrementTrenutniBrojZarazenih();
                Simulacija.dohvatiSimulaciju().incrementBrojIzlijecenih();
                zarazen = false;
                potencijalan = false;
                poslednjeTemperature.clear();
                postaviIzlijecenIkonicu();
                kretanjeOmoguceno = true;
                izlijecenNaPutuDoKuce = true;
                putanja = najkracaPutanja(trenutnaLokacija(), lokacijaKuce());

                if (trenutnaAmbulanta != null)
                {
                    trenutnaAmbulanta.povecajKapacitetZaJednoMjesto();
                    trenutnaAmbulanta = null;
                }
            }
        }
    }

    private void postaviIzlijecene()
    {
        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            if (s.getIDKuce() == getIDKuce() && getID() != s.getID())
            {
                s.setPotencijalan(false);
                s.setZarazen(false);
                s.postaviRegularanIkonicu();
                s.omoguciKretanje();
            }
    }

    public synchronized void pomjeri(int lokacija)
    {
        int y = lokacija / getVelicinaGrada();
        int x = lokacija % getVelicinaGrada();

        if ((x < 1 || x > getVelicinaGrada()) || (y < 1 || y > getVelicinaGrada()))
            return;

        if (x < lokacijaX)
            smjerGUI = LIJEVO;

        else if (x > lokacijaX)
            smjerGUI = DESNO;

        else if (y > lokacijaY)
            smjerGUI = DOLE;

        else if (y < lokacijaY)
            smjerGUI = GORE;

        else if (x == lokacijaX && y == lokacijaY)
            smjerGUI = MIROVANJE;

        if (lokacija % getVelicinaGrada() == 0)
        {
            x = getVelicinaGrada();
            y -= 1;
        }

        if(x <= 0)
            x = 1;

        if(y <= 0)
            y = 1;

        lokacijaX = x;
        lokacijaY = y;
    }

    public void deserijalizuj()
    {
        postaviRegularanIkonicu();

        if (zarazen)
            postaviZarazenIkonicu();

        else if (potencijalan)
            postaviPotencijalniIkonicu();

        else if (izlijecenNaPutuDoKuce)
            postaviIzlijecenIkonicu();

        aktivan = false;
        this.start();
    }

    public boolean pokupiVozilom(Ambulanta ciljnaAmbulanta)
    {
        int velicinaGrada = getVelicinaGrada();
        trenutnaAmbulanta = ciljnaAmbulanta;

        AmbulantnaKola kola = null;
        int lokacijaKola = 0;

        for (AmbulantnaKola ak : Simulacija.dohvatiSimulaciju().getAmbulantnaKola())
            if (!ak.getUPutu())
            {
                kola = ak;
                break;
            }

        if (kola == null)
        {
            MyLogger.log(Level.INFO, LogDetails.NO_VEHICLES);
            new Alarm(this);
            return false;
        }

        ciljnaAmbulanta.smanjiKapacitetZaJednoMjesto();
        kola.pokreni();
        kola.setUPutu(true);
        lokacijaKola = kola.getLokacijaY() * velicinaGrada + kola.getLokacijaX();

        ArrayList<Integer> putanjaDoZarazenog = najkracaPutanja(lokacijaKola, trenutnaLokacija());
        kola.posaljiPoStanovnika(this, ciljnaAmbulanta, putanjaDoZarazenog);
        return true;
    }

    public void vratiStanovnikaUAmbulantu(Ambulanta ciljnaAmbulanta, AmbulantnaKola kola)
    {
        lokacijaX = ciljnaAmbulanta.getLokacijaX();
        lokacijaY = ciljnaAmbulanta.getLokacijaY();
        kola.vratiUAmbulantu(ciljnaAmbulanta);
    }

    public void postaviKucuPriInicijalizaciji(Kuca k)
    {
        this.lokacijaX = k.getLokacijaX();
        this.lokacijaY = k.getLokacijaY();
    }

    public void postaviTemperaturu(double t) { temperatura = t; }

    public boolean uKuci()
    {
        return new OrderedPair(this.lokacijaX, this.lokacijaY, TipElementa.OSOBA).equalsWithoutType(new OrderedPair(this.lokacijaKuceX, this.lokacijaKuceY, TipElementa.OBJEKAT));
    }

    public boolean uAmbulanti()
    {
        for (Ambulanta a : Simulacija.dohvatiSimulaciju().getAmbulante())
            if (new OrderedPair(this.lokacijaX, this.lokacijaY, TipElementa.OSOBA).equalsWithoutType(new OrderedPair(a.getLokacijaX(), a.getLokacijaY(), TipElementa.OBJEKAT)))
                return true;

        return false;
    }

    public boolean uKolima()
    {
        for (AmbulantnaKola a : Simulacija.dohvatiSimulaciju().getAmbulantnaKola())
            if (new OrderedPair(this.lokacijaX, this.lokacijaY, TipElementa.OSOBA).equalsWithoutType(new OrderedPair(a.getLokacijaX(), a.getLokacijaY(), TipElementa.OBJEKAT)))
                return true;

        return false;
    }

    public void omoguciKretanje()
    {
        kretanjeOmoguceno = true;
    }

    public void zaustaviKretanje()
    {
        kretanjeOmoguceno = false;
    }

    public boolean naObjektu()
    {
        OrderedPair lokacija = new OrderedPair(lokacijaX, lokacijaY, TipElementa.OSOBA);
        for (OrderedPair o : Simulacija.dohvatiSimulaciju().getLokacijeObjekata())
            if (lokacija.equalsWithoutType(o))
                return true;

        return false;
    }

    public void vratiKuci()
    {
        int lokacijaKuce = lokacijaKuceY * getVelicinaGrada() + lokacijaKuceX;
        putanja.clear();
        putanja = najkracaPutanja(trenutnaLokacija(), lokacijaKuce); // Dijkstra.najkracaPutanja(trenutnaLokacija(), lokacijaKuce);

        if (putanja == null)
        {
            putanja = new ArrayList<>();
            return;
        }

        putanja.add(lokacijaKuce);
    }

    public short trenutnaLokacija()
    {
        return Integer.valueOf((lokacijaY) * getVelicinaGrada() + lokacijaX).shortValue();
    }

    private int lokacijaKuce()
    {
        return lokacijaKuceY * getVelicinaGrada() + lokacijaKuceX;
    }

    public static SmjerKretanja randomPomjeraj()
    {
        switch (ThreadLocalRandom.current().nextInt(0, 5))
        {
            case (0) -> { return SmjerKretanja.DOLE; }
            case (1) -> { return SmjerKretanja.GORE; }
            case (2) -> { return SmjerKretanja.LIJEVO; }
            case (3) -> { return SmjerKretanja.DESNO; }
            default -> { return SmjerKretanja.MIROVANJE; }
        }
    }

    public void pokreni()
    {
        aktivan = true;
    }

    abstract public void postaviRegularanIkonicu();
    abstract public void postaviZarazenIkonicu();
    abstract public void postaviPotencijalniIkonicu();
    abstract public void postaviIzlijecenIkonicu();
    abstract public void postaviRadijuse();
    abstract protected void postaviRadijuseVertikalno();
    abstract protected void postaviRadijuseHorizontalno();

    public void info(String filename)
    {
        try
        {
            FileWriter stanovnikWriter = new FileWriter(filename, true);
            stanovnikWriter.append("Stanovnik:");
            stanovnikWriter.append("\n\tID: " + ID);
            stanovnikWriter.append("\n\tIme: " + ime);
            stanovnikWriter.append("\n\tPrezime: " + prezime);
            stanovnikWriter.append("\n\tGodine: " + godine);
            stanovnikWriter.append("\n\tGodina rodjenja: " + godinaRodjenja);
            stanovnikWriter.append("\n\tPol: " + pol.toString());
            stanovnikWriter.append("\n\tID Kuce: " + IDKuce);
            stanovnikWriter.append("\n\tLokacija kuce X: " + lokacijaKuceX);
            stanovnikWriter.append("\n\tLokacija kuce Y: " + lokacijaKuceY);
            stanovnikWriter.append("\n\tTemperatura: " + temperatura);
            stanovnikWriter.append("\n\tRadijus kretanja: " + radijusKretanja);
            stanovnikWriter.append("\n\tRadijus gore: " + radijusGore);
            stanovnikWriter.append("\n\tRadijus dole: " + radijusDole);
            stanovnikWriter.append("\n\tRadijus lijevo: " + radijusLijevo);
            stanovnikWriter.append("\n\tRadijus desno: " + radijusDesno);
            stanovnikWriter.append("\n\tLokacija X: " + lokacijaX);
            stanovnikWriter.append("\n\tLokacija Y: " + lokacijaY);
            stanovnikWriter.append("\n\tKretanje omoguceno: " + kretanjeOmoguceno);
            stanovnikWriter.append("\n\tSmjerGUI: " + smjerGUI.toString());
            stanovnikWriter.append("\n\tPutanja: " + putanja);
            stanovnikWriter.append("\n\tZarazen: " + zarazen);
            stanovnikWriter.append("\n\tPotencijalan: " + potencijalan);
            stanovnikWriter.append("\n\tAktivan: " + aktivan);
            stanovnikWriter.append("\n\tIzlijecen na putu do kuce: " + izlijecenNaPutuDoKuce);
            stanovnikWriter.append("\n\tPoslednje temperature: " + poslednjeTemperature);

            if (trenutnaAmbulanta != null)
                stanovnikWriter.append("\n\tTrenutna ambulanta: " + trenutnaAmbulanta.getLokacija());
            else
                stanovnikWriter.append("\n\tTrenutna ambulanta: " + 0);

            stanovnikWriter.append("\n\tNedostupno polje: " + brojacNeuspjesnihPomjeranja);
            stanovnikWriter.append("\n\n");
            stanovnikWriter.flush();
            stanovnikWriter.close();
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.PEOPLE_INFO_EXC);
        }
    }
}