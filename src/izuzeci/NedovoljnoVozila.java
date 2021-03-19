package izuzeci;

public class NedovoljnoVozila extends Exception
{
    private final static String EXC_MESSAGE = "Nedovoljno vozila uneseno.";

    public NedovoljnoVozila()
    {
        super(EXC_MESSAGE);
    }

    public NedovoljnoVozila(String msg)
    {
        super(msg);
    }
}
