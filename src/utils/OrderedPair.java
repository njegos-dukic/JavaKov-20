package utils;

import enumi.TipElementa;
import java.io.Serial;
import java.io.Serializable;

public class OrderedPair implements Serializable
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;
    private final int x;
    private final int y;
    private final TipElementa tip;

    public OrderedPair(int x, int y, TipElementa t)
    {
        this.x = x;
        this.y = y;
        this.tip = t;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public TipElementa getTip() { return tip; }

    public boolean equals(OrderedPair obj)
    {
        return obj.getX() == this.x && obj.getY() == this.y && obj.getTip().equals(this.tip);
    }

    public boolean equalsWithoutType(OrderedPair obj)
    {
        return obj.getX() == this.x && obj.getY() == this.y;
    }

    public boolean daLiJeURadijusu(OrderedPair lokacija, int radijus)
    {
        return Math.abs(lokacija.getX() - this.x) <= radijus && Math.abs(lokacija.getY() - this.y) <= radijus;
    }
}
