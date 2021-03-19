package izuzeci;

public class NedovoljnoPunktova extends Exception
{
    private final static String EXC_MESSAGE = "Nedovoljno punktova uneseno.";

    public NedovoljnoPunktova()
    {
        super(EXC_MESSAGE);
    }

    public NedovoljnoPunktova(String msg)
    {
        super(msg);
    }
}
