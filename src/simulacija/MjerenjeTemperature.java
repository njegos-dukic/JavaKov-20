package simulacija;

import enumi.TipElementa;
import objekti.KontrolniPunkt;
import osobe.Stanovnik;
import utils.Konstante;
import utils.OrderedPair;

public class MjerenjeTemperature extends Thread
{
    private static MjerenjeTemperature mjerenjeTemperature = null; // Singleton
    public static boolean mjerenjeTemperatureAktivno = false;

    private MjerenjeTemperature() {}

    public static MjerenjeTemperature kreirajMjerenjeTemperature()
    {
        if (mjerenjeTemperature == null)
            mjerenjeTemperature = new MjerenjeTemperature();

        mjerenjeTemperatureAktivno = true;
        return mjerenjeTemperature;
    }

    @Override
    public void run()
    {
        while(mjerenjeTemperatureAktivno)
            mjeriTemperaturu();
    }

    public static void mjeriTemperaturu()
    {
        for (KontrolniPunkt k : Simulacija.dohvatiSimulaciju().getPunktovi())
        {
            OrderedPair lokacijaPunkta = new OrderedPair(k.getLokacijaX(), k.getLokacijaY(), TipElementa.OBJEKAT);

            for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            {
                if (s.uKuci() || s.getIzlijecenNaPutuDoKuce())
                    continue;

                OrderedPair lokacijaStanovnika = new OrderedPair(s.getLokacijaX(), s.getLokacijaY(), TipElementa.OSOBA);
                if (lokacijaPunkta.daLiJeURadijusu(lokacijaStanovnika, 1) && !s.uKuci())
                    if (KontrolniPunkt.ocitajTemperaturu(s) > Konstante.TEMPERATURE_LIMIT)
                    {
                        if (s.getPotencijalan())
                            return;

                        Alarm.raiseAlarm(s);
                    }
            }
        }
    }

    public static void zaustaviMjerenjeTemperature() { mjerenjeTemperatureAktivno = false; }

    public static boolean jeLiMjerenjeTemperatureAktivno() { return mjerenjeTemperatureAktivno; }
}
