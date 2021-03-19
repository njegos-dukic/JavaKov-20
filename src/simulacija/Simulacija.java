package simulacija;

import GUI.StartScreen.PocetniEkranController;
import enumi.TipElementa;
import file_watcher.FileWatcher;
import logger.MyLogger;
import objekti.Ambulanta;
import objekti.KontrolniPunkt;
import objekti.Kuca;
import osobe.*;
import utils.LogDetails;
import utils.OrderedPair;
import utils.Putanje;
import vozila.AmbulantnaKola;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import static GUI.MainScreen.SistemZaNadzorController.*;
import static GUI.StartScreen.PocetniEkranController.*;
import static GUI.StartScreen.PocetniEkranController.getBrojVozila;
import static objekti.Ambulanta.getBrojAmbulanti;

public class Simulacija extends Thread implements Serializable // Singleton class
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;
    private static Simulacija simulacija = null;

    private final CopyOnWriteArrayList<Kuca> kuce = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<KontrolniPunkt> punktovi = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Ambulanta> ambulante = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<AmbulantnaKola> ambulantnaKola = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Stanovnik> djeca = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Stanovnik> odrasli = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Stanovnik> stari = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Stanovnik> stanovnici = new CopyOnWriteArrayList<>();
    private final int brojStanovnika = PocetniEkranController.getBrojDjece() + PocetniEkranController.getBrojOdraslih() + PocetniEkranController.getBrojStarih();
    private final CopyOnWriteArrayList<OrderedPair> zauzetaPolja = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<OrderedPair> lokacijeObjekata = new CopyOnWriteArrayList<>();
    private final int velicinaGrada = getVelicinaGrada();
    private int trenutniBrojZarazenih = 0;
    private int ukupniBrojIzlijecenih = 0;
    private final LocalDateTime vrijemePocektaSimulacije = LocalDateTime.now();

    public static Simulacija dohvatiSimulaciju() // Singleton
    {
        if (simulacija == null)
            simulacija = new Simulacija();

        return simulacija;
    }

    public static void postaviSimulaciju(Simulacija nova)
    {
        simulacija = nova;
    }

    public CopyOnWriteArrayList<Kuca> getKuce() {
        return kuce;
    }
    public CopyOnWriteArrayList<KontrolniPunkt> getPunktovi() {
        return punktovi;
    }
    public CopyOnWriteArrayList<Ambulanta> getAmbulante() {
        return ambulante;
    }
    public CopyOnWriteArrayList<AmbulantnaKola> getAmbulantnaKola() {
        return ambulantnaKola;
    }
    public CopyOnWriteArrayList<OrderedPair> getLokacijeObjekata() { return lokacijeObjekata; }
    public CopyOnWriteArrayList<Stanovnik> getStanovnici()
    {
        return stanovnici;
    }
    public int getBrojStanovnika() {
        return brojStanovnika;
    }
    public int getTrenutniBrojZarazenih() { return trenutniBrojZarazenih; }
    public int getUkupniBrojIzlijecenih() { return ukupniBrojIzlijecenih; }

    private Simulacija()
    {
        inicijalizujKuce();
        inicijalizujPunktove();
        inicijalizujAmbulante();
        inicijalizujStanovnike();
        inicijalizujKola();
        try
        {
            Files.writeString(Paths.get(Putanje.INFO_FILE_PATH), "0;0");
        }
        catch (IOException e)
        {
            MyLogger.log(Level.INFO, LogDetails.INFO_FILE_EXCEPTION);
        }

        FileWatcher.kreirajPosmatranje().start();
        new Alarm().start();
    }

    public void incrementTrenutniBrojZarazenih()
    {
        trenutniBrojZarazenih++;
        FileWatcher.upisiUFajl(trenutniBrojZarazenih, ukupniBrojIzlijecenih);
    }

    public void decrementTrenutniBrojZarazenih()
    {
        trenutniBrojZarazenih--;
        FileWatcher.upisiUFajl(trenutniBrojZarazenih, ukupniBrojIzlijecenih);
    }

    public void incrementBrojIzlijecenih()
    {
        ukupniBrojIzlijecenih++;
        FileWatcher.upisiUFajl(trenutniBrojZarazenih, ukupniBrojIzlijecenih);
    }

    private void inicijalizujStanovnike()
    {
        for (int i = 0; i < PocetniEkranController.getBrojOdraslih(); i++)
            odrasli.add(new Odrasli());
        for (int i = 0; i < PocetniEkranController.getBrojStarih(); i++)
            stari.add(new Stari());
        for (int i = 0; i < PocetniEkranController.getBrojDjece(); i++)
            djeca.add(new Dijete());

        rasporediPoKucama();

        stanovnici.addAll(djeca);
        stanovnici.addAll(odrasli);
        stanovnici.addAll(stari);

        for (Stanovnik s : stanovnici)
        {
            s.postaviRadijuse();
            s.start();
        }

        AzuriranjeTemperature.kreirajAzuriranjeTemperature().start();
    }

    private void rasporediPoKucama()
    {
        int j = 0;
        int i = 0;

        for (i = 0; i < kuce.size(); i++)
        {
            if (i < odrasli.size())
                kuce.get(i).dodajUkucana(odrasli.get(i));

            else
            {
                kuce.get(i).dodajUkucana(stari.get(j));
                j++;
            }
        }

        if (j == 0)
            for (int k = i; k < odrasli.size(); k++)
                kuce.get(ThreadLocalRandom.current().nextInt(0, PocetniEkranController.getBrojKuca())).dodajUkucana(odrasli.get(k));

        for (j = j; j < stari.size(); j++)
            kuce.get(ThreadLocalRandom.current().nextInt(0, PocetniEkranController.getBrojKuca())).dodajUkucana(stari.get(j));

        for (i = 0; i < djeca.size(); i++)
            kuce.get(ThreadLocalRandom.current().nextInt(0, PocetniEkranController.getBrojKuca())).dodajUkucana(djeca.get(i));
    }

    private void inicijalizujKuce()
    {
        OrderedPair tempPair;
        for (int i = 0; i < PocetniEkranController.getBrojKuca(); i++)
        {
            int x = ThreadLocalRandom.current().nextInt(2, getVelicinaGrada());
            int y = ThreadLocalRandom.current().nextInt(2, getVelicinaGrada());

            tempPair = new OrderedPair(x, y, TipElementa.OBJEKAT);
            int oldI = i;

            if (objekatNaPolju(tempPair))
                i--;

            if (oldI == i)
            {
                lokacijeObjekata.add(tempPair);
                kuce.add(new Kuca(x, y));
            }
        }
    }

    public void pauziraj()
    {
        for (Stanovnik s : stanovnici)
            s.zaustavi();

        for (AmbulantnaKola a : ambulantnaKola)
            a.zaustavi();
    }

    private void inicijalizujPunktove()
    {
        OrderedPair tempPair;
        for (int i = 0; i < PocetniEkranController.getBrojPunktova(); i++)
        {
            int x = ThreadLocalRandom.current().nextInt(2, getVelicinaGrada());
            int y = ThreadLocalRandom.current().nextInt(2, getVelicinaGrada());

            tempPair = new OrderedPair(x, y, TipElementa.OBJEKAT);
            int oldI = i;

            if (objekatNaPolju(tempPair))
                i--;

            if (oldI == i)
            {
                lokacijeObjekata.add(tempPair);
                punktovi.add(new KontrolniPunkt(x, y));
            }
        }
    }

    private void inicijalizujAmbulante()
    {
        ambulante.add(new Ambulanta(1, 1));
        ambulante.add(new Ambulanta(1, getVelicinaGrada()));
        ambulante.add(new Ambulanta(getVelicinaGrada(), 1));
        ambulante.add(new Ambulanta(getVelicinaGrada(), getVelicinaGrada()));

        lokacijeObjekata.add(new OrderedPair(1, 1, TipElementa.OBJEKAT));
        lokacijeObjekata.add(new OrderedPair(1, getVelicinaGrada(), TipElementa.OBJEKAT));
        lokacijeObjekata.add(new OrderedPair(getVelicinaGrada(), 1, TipElementa.OBJEKAT));
        lokacijeObjekata.add(new OrderedPair(getVelicinaGrada(), getVelicinaGrada(), TipElementa.OBJEKAT));
    }

    private void inicijalizujKola()
    {
        for (int i = 0; i < PocetniEkranController.getBrojVozila(); i++)
        {
            AmbulantnaKola a = new AmbulantnaKola(ambulante.get(i % 4));
            ambulantnaKola.add(a);
            a.start();
        }
    }

    public synchronized boolean objekatNaPolju(OrderedPair lokacija)
    {
        for (OrderedPair o : lokacijeObjekata)
            if (o.equalsWithoutType(lokacija))
                return true;

        return false;
    }

    public synchronized void ukloniStanovnikaSaZauzetogPolja(OrderedPair novoSlobodno)
    {
        for (int i = 0; i < zauzetaPolja.size(); i++)
            if (novoSlobodno.equals(zauzetaPolja.get(i)))
            {
                if (i < zauzetaPolja.size())
                    zauzetaPolja.remove(i);
            }
    }

    public synchronized void dodajZauzetoPolje(OrderedPair novoZauzeto) { zauzetaPolja.add(novoZauzeto); }

    public Duration dohvatiVrijemeTrajanjaSimulacije() { return Duration.between(LocalDateTime.now(), vrijemePocektaSimulacije); }

    public void zavrsiSimulaciju()
    {
        Duration vrijemeTrajanjaSimulacije = dohvatiVrijemeTrajanjaSimulacije();
        String stringZaUpis = "Informacije o simulaciji:\n\n";
        String[] split = vrijemeTrajanjaSimulacije.toString().substring(3).split("\\.");
        stringZaUpis += "Vrijeme trajanja simulacije: " + split[0] + "S";
        stringZaUpis += "\nBroj djece: " + getBrojDjece();
        stringZaUpis += "\nBroj odraslih: " + getBrojOdraslih();
        stringZaUpis += "\nBroj starih: " + getBrojStarih();
        stringZaUpis += "\nBroj kuća: " + getBrojKuca();
        stringZaUpis += "\nBroj ambulanti: " + getBrojAmbulanti();
        stringZaUpis += "\nBroj punktova: " + getBrojPunktova();
        stringZaUpis += "\nBroj vozila: " + getBrojVozila();
        try
        {
            stringZaUpis += "\nBroj zaraženih: " + Files.readString(Paths.get(Putanje.INFO_FILE_PATH)).split(";")[0];
            stringZaUpis += "\nBroj Izliječenih: " + Files.readString(Paths.get(Putanje.INFO_FILE_PATH)).split(";")[1];
            String datum = LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth()  ;
            String vrijeme = LocalDateTime.now().getHour() + "-" + LocalDateTime.now().getMinute() + "-" + LocalDateTime.now().getSecond();
            String putanjDoFajla = Putanje.APP_FOLDER + File.separatorChar + "SIM-JavaKov-20 " + datum + " " + vrijeme + ".txt";
            Files.writeString(Paths.get(putanjDoFajla), stringZaUpis);
            Info.dodajInfo(putanjDoFajla);
        }
        catch (IOException e)
        {
            MyLogger.log(Level.INFO, LogDetails.INFO_FILE_EXCEPTION);
        }
    }

    public void info(String filename)
    {
        try
        {
            FileWriter simulacijaWriter = new FileWriter(filename, true);
            simulacijaWriter.append("\n\n\n[DETALJI]\n----------\n\nSimulacija:");
            simulacijaWriter.append("\n\tBroj stanovnika: " + brojStanovnika);
            simulacijaWriter.append("\n\tVelicina grada: " + velicinaGrada);
            simulacijaWriter.append("\n\tTrenutni broj zarazenih: " + trenutniBrojZarazenih);
            simulacijaWriter.append("\n\tUkupni broj izlijecenih: " + ukupniBrojIzlijecenih);
            simulacijaWriter.append("\n\tVrijeme pocetka simulacije: " + vrijemePocektaSimulacije.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
            simulacijaWriter.append("\n\n");
            simulacijaWriter.flush();
            simulacijaWriter.close();
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.SIM_INFO_EXC);
        }
    }

    public void zaustaviSveTredove()
    {
        Alarm.zaustavi();
        AzuriranjeTemperature.zaustaviAzuriranjeTemperature();
        MjerenjeTemperature.zaustaviMjerenjeTemperature();
        AzuriranjeGUI.zaustavi();
        FileWatcher.zaustaviPosmatranje();
    }
}
