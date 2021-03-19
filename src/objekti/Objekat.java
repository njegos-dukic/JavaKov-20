package objekti;

import javafx.scene.image.ImageView;

import java.io.Serial;
import java.io.Serializable;

public abstract class Objekat implements Serializable
{
    @Serial
    private static final long serialVersionUID = 25_01_2021;

    protected int lokacijaX;
    protected int lokacijaY;
    protected transient ImageView ikonica = new ImageView("/resursi/loading.png");

    public int getLokacijaX() { return lokacijaX; }
    public int getLokacijaY() { return lokacijaY; }
    public ImageView getIkonica() { return ikonica; }
}
