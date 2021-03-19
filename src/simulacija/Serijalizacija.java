package simulacija;

import enumi.TipElementa;
import file_watcher.FileWatcher;
import logger.MyLogger;
import objekti.Ambulanta;
import objekti.KontrolniPunkt;
import objekti.Kuca;
import osobe.Stanovnik;
import utils.LogDetails;
import utils.OrderedPair;
import utils.Putanje;
import vozila.AmbulantnaKola;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class Serijalizacija
{
    private static boolean serialized = false;

    public static void serialize()
    {
        if (serialized)
        {
            MyLogger.log(Level.INFO, LogDetails.ALREADY_SERIALIZED);
            return;
        }

        try
        {
            Files.deleteIfExists(Path.of(Putanje.PUTANJA_DO_SERIALIZATION));
            FileOutputStream file = new FileOutputStream(Putanje.PUTANJA_DO_SERIALIZATION);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(Simulacija.dohvatiSimulaciju());
            out.close();
            file.close();

            Simulacija.dohvatiSimulaciju().pauziraj();
            Alarm.zaustavi();
            serialized = true;
        }

        catch(IOException ex)
        {
            MyLogger.log(Level.INFO, LogDetails.SER_EXCEPTION);
        }
    }

    public static boolean deserialize()
    {
        if (!serialized)
        {
            MyLogger.log(Level.INFO, LogDetails.NOT_SERIALIZED);
            return false;
        }

        try
        {
            FileInputStream file = new FileInputStream(Putanje.PUTANJA_DO_SERIALIZATION);
            ObjectInputStream in = new ObjectInputStream(file);
            Simulacija.postaviSimulaciju((Simulacija)in.readObject());
            in.close();
            file.close();

            for (Kuca k : Simulacija.dohvatiSimulaciju().getKuce())
            {
                Simulacija.dohvatiSimulaciju().getLokacijeObjekata().add(new OrderedPair(k.getLokacijaX(), k.getLokacijaY(), TipElementa.OBJEKAT));
                k.deserijalizuj();
            }

            for (Ambulanta a : Simulacija.dohvatiSimulaciju().getAmbulante())
            {
                Simulacija.dohvatiSimulaciju().getLokacijeObjekata().add(new OrderedPair(a.getLokacijaX(), a.getLokacijaY(), TipElementa.OBJEKAT));
                a.deserijalizuj();
            }

            for (KontrolniPunkt kp : Simulacija.dohvatiSimulaciju().getPunktovi())
            {
                Simulacija.dohvatiSimulaciju().getLokacijeObjekata().add(new OrderedPair(kp.getLokacijaX(), kp.getLokacijaY(), TipElementa.OBJEKAT));
                kp.deserijalizuj();
            }

            for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            {
                s.deserijalizuj();
                for (Alarm a : Alarm.getAlarmi())
                    if (a.getBolesnaOsoba().getID() == s.getID())
                    {
                        Alarm.getAlarmi().remove(a);
                        new Alarm(s);
                    }
            }

            for (AmbulantnaKola ak : Simulacija.dohvatiSimulaciju().getAmbulantnaKola())
                ak.deserijalizuj();

            Alarm.pokreni();
            FileWatcher.upisiUFajl(Simulacija.dohvatiSimulaciju().getTrenutniBrojZarazenih(), Simulacija.dohvatiSimulaciju().getUkupniBrojIzlijecenih());

            AzuriranjeGUI.deserijalizuj(Simulacija.dohvatiSimulaciju());
            serialized = false;
            return true;
        }

        catch(IOException ex)
        {
            MyLogger.log(Level.INFO, LogDetails.DES_EXCEPTION_IO);
        }

        catch(ClassNotFoundException ex)
        {
            MyLogger.log(Level.INFO, LogDetails.DES_EXCEPTION_CNFE);
        }

        return false;
    }

    public static boolean getSerialized() { return serialized; }
}
