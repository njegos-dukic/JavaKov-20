package izuzeci;

public class NedovoljnoKuca extends Exception
{
    private final static String EXC_MESSAGE = "Nedovoljno kuca uneseno.";

    public NedovoljnoKuca()
    {
        super(EXC_MESSAGE);
    }

    public NedovoljnoKuca(String msg)
    {
        super(msg);
    }
}
