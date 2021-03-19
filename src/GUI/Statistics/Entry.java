package GUI.Statistics;

public class Entry
{
    private final String tip;
    private final int brojZarazenih;
    private final int brojMuskaraca;
    private final int brojZena;

    public Entry(String tip, int muskarci, int zene)
    {
        this.tip = tip;
        this.brojZarazenih = muskarci + zene;
        this.brojMuskaraca = muskarci;
        this.brojZena = zene;
    }

    public String getTip()
    {
        return tip;
    }
    public int getBrojZarazenih()
    {
        return brojZarazenih;
    }
    public int getBrojMuskaraca()
    {
        return brojMuskaraca;
    }
    public int getBrojZena()
    {
        return brojZena;
    }
}
