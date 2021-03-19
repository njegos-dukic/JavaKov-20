package izuzeci;

public class PrekoracenjeGranica extends Exception
{
    private final static String EXC_MESSAGE = "Neispravne granice.";

    public PrekoracenjeGranica()
    {
        super(EXC_MESSAGE);
    }

    public PrekoracenjeGranica(String msg)
    {
        super(msg);
    }
}
