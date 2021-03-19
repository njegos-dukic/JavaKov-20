package simulacija;

import logger.MyLogger;
import osobe.Stanovnik;
import utils.Konstante;
import utils.LogDetails;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class AzuriranjeTemperature extends Thread
{
    private static AzuriranjeTemperature temperatura = null; // Singleton
    private static boolean azuriranjeTemperature = true;
    public static void zaustaviAzuriranjeTemperature() { azuriranjeTemperature = false; }

    private AzuriranjeTemperature() {}

    public static AzuriranjeTemperature kreirajAzuriranjeTemperature()
    {
        if (temperatura == null)
            temperatura = new AzuriranjeTemperature();

        return temperatura;
    }

    @Override
    public void run()
    {
        while(azuriranjeTemperature)
        {
            try
            {
                azurirajTemperaturu();
            }
            catch (Exception e)
            {
                MyLogger.log(Level.INFO, LogDetails.TEMPERATURE_UPDATE_EXCEPTION);
            }
        }
    }

    private void azurirajTemperaturu() throws InterruptedException
    {
        Thread.sleep(Konstante.INITIAL_TIME);

        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
        {
            double temp = ThreadLocalRandom.current().nextInt(Konstante.LOWER_TEMP, Konstante.UPPER_TEMP) / (double) 10;

            s.postaviTemperaturu(temp);

            if (s.getZarazen() && s.uAmbulanti())
                s.postaviMjerenjeUBolnici(temp);
        }

        Thread.sleep(Konstante.TEMP_UPDATE);
    }

    public static boolean jeLiAzuririranjeTemperatureAktivno()
    {
        return azuriranjeTemperature;
    }
}
