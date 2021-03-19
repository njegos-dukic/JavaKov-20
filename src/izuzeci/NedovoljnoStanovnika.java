package izuzeci;

public class NedovoljnoStanovnika extends Exception
{
    private final static String EXC_MESSAGE = "Nedovoljno stanovnika uneseno.";

    public NedovoljnoStanovnika()
    {
        super(EXC_MESSAGE);
    }

    public NedovoljnoStanovnika(String msg)
    {
        super(msg);
    }
}
