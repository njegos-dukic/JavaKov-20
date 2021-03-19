package simulacija;

import GUI.StartScreen.PocetniEkranController;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logger.MyLogger;
import objekti.Ambulanta;
import osobe.Stanovnik;
import utils.Konstante;
import utils.LogDetails;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;

public class Alarm extends Thread
{
    private Stanovnik bolesnaOsoba;
    private static final ConcurrentLinkedDeque<Alarm> pristigliAlarmi = new ConcurrentLinkedDeque<>();
    private static boolean alarmAktivan = true;

    public Alarm() { }
    
    public Alarm (Stanovnik osoba)
    {
        bolesnaOsoba = osoba;
        pristigliAlarmi.push(this);
    }

    public static ConcurrentLinkedDeque<Alarm> getAlarmi()
    {
        return pristigliAlarmi;
    }
    public Stanovnik getBolesnaOsoba()
    {
        return bolesnaOsoba;
    }

    public static void zaustavi() { alarmAktivan = false; }
    public static void pokreni() { alarmAktivan = true; }

    @Override
    public void run()
    {
        while (true)
            try
            {
                ringAlarm();
            }
            catch (Exception e)
            {
                MyLogger.log(Level.INFO, LogDetails.ALARM_RING_EXCEPTION);
            }
    }

    public static void raiseAlarm(Stanovnik stanovnik)
    {
        if (!naSteku(stanovnik) && !stanovnik.getZarazen() && !stanovnik.getPotencijalan())
        {
            new Alarm(stanovnik);
            Simulacija.dohvatiSimulaciju().incrementTrenutniBrojZarazenih();
            stanovnik.setZarazen(true);
            stanovnik.postaviZarazenIkonicu();

            postaviPotencijalne(stanovnik);
        }
    }

    public static void resolveAlarm()
    {
        if (pristigliAlarmi.isEmpty())
            return;

        Stanovnik stanovnik = pristigliAlarmi.pop().getBolesnaOsoba();
        Ambulanta slobodnaAmbulanta = null;

        for (Ambulanta a : Simulacija.dohvatiSimulaciju().getAmbulante())
            if (a.getKapacitet() > 0)
            {
                slobodnaAmbulanta = a;
                break;
            }

        if (slobodnaAmbulanta == null)
        {
            MyLogger.log(Level.INFO, LogDetails.NO_AMBULANCES);
            new Alarm(stanovnik);
            return;
        }

        boolean resolved = stanovnik.pokupiVozilom(slobodnaAmbulanta);

        if (resolved)
            resolveAlarm();
    }

    private static void postaviPotencijalne(Stanovnik stanovnik)
    {
        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            if (s.getIDKuce() == stanovnik.getIDKuce() && stanovnik.getID() != s.getID())
            {
                s.setPotencijalan(true);
                s.setZarazen(false);
                s.postaviPotencijalniIkonicu();
                s.zaustaviKretanje();
                s.vratiKuci();
            }
    }

    private static boolean naSteku(Stanovnik stanovnik)
    {
        if (pristigliAlarmi.isEmpty())
            return false;

        for (Alarm a : pristigliAlarmi)
            if (a.bolesnaOsoba.getID() == stanovnik.getID())
                return true;

        return false;
    }

    public synchronized static void ringAlarm() throws Exception
    {
        while(alarmAktivan)
            if (!pristigliAlarmi.isEmpty())
            {
                Platform.runLater(() ->
                {
                    PocetniEkranController.getMainKontroler().getMainPane().setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(7), new BorderWidths(4))));
                });
                Thread.sleep(Konstante.ALARM_SLEEP);

                Platform.runLater(() ->
                {
                    PocetniEkranController.getMainKontroler().getMainPane().setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(7), new BorderWidths(4))));
                });
                Thread.sleep(Konstante.ALARM_SLEEP);
            }
    }
}
