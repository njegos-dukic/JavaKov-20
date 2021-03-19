package simulacija;

import enumi.TipElementa;
import objekti.Ambulanta;
import utils.OrderedPair;
import java.util.ArrayList;
import static GUI.MainScreen.SistemZaNadzorController.getVelicinaGrada;

public class NajkracaPutanja
{
    private static final int dim = getVelicinaGrada();

    public static ArrayList<Integer> najkracaPutanja(int start, int dest)
    {
        int lokacija = start;
        ArrayList<Integer> putanja = new ArrayList<>();

        while (lokacija % dim != dest % dim)
        {
            if ((lokacija % dim < dest % dim || dest % dim == 0) && lokacija % dim != 0)
                lokacija += 1;

            else if (lokacija % dim != 1)
                lokacija -= 1;

            if (objekatNaPolju(lokacija) && lokacija != dest)
            {
                if (lokacija / dim < dest / dim && lokacija <= dim * dim)
                    lokacija += dim;

                else if (lokacija > dim)
                    lokacija -= dim;
            }

            putanja.add(lokacija);
        }

        while (lokacija / dim != dest / dim)
        {
            if (lokacija / dim < dest / dim && lokacija <= dim * dim)
                lokacija += dim;

            else if (lokacija > dim)
                lokacija -= dim;

            if (objekatNaPolju(lokacija) && lokacija != dest)
            {
                if ((lokacija % dim < dest % dim || dest % dim == 0) && lokacija % dim != 0)
                    lokacija += 1;

                else if (lokacija % dim != 1)
                    lokacija -= 1;
            }

            putanja.add(lokacija);
        }

        if (!putanja.contains(dest))
            putanja.add(dest);

        return putanja;
    }

    private static boolean objekatNaPolju(int lokacija)
    {
        int y = lokacija / getVelicinaGrada();
        int x = lokacija % getVelicinaGrada();

        for (OrderedPair o : Simulacija.dohvatiSimulaciju().getLokacijeObjekata())
            if (o.equalsWithoutType(new OrderedPair(x, y, TipElementa.OBJEKAT)))
                return true;

        return false;
    }
}
