package Código;

import javax.swing.*;
import java.awt.*;

public abstract class Arma {
    private int dano;
    private int ruido;
    private int rango;

    public Arma(int dano, int ruido, int rango) {
        this.dano = dano;
        this.ruido = ruido;
        this.rango = rango;
    }
    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public int getRuido() {
        return ruido;
    }

    public void setRuido(int ruido) {
        this.ruido = ruido;
    }
}
class Arco extends Arma{

    public Arco() {
        super(62, 0, 6);
    }
}
class Sierra extends Arma{

    public Sierra() {
        super(115, 8, 2);
    }
}
class Rifle extends Arma{
    public Rifle() {
        super(98, 5, 6);
    }
}
class Pistola extends Arma{
    public Pistola() {
        super(75, 4, 4);
    }
}
class Bate extends Arma{
    public Bate() {
        super(68, 3, 1);
    }
}
class Francotirador extends Arma{
    public Francotirador() {
        super(150, 12, 10);
    }
}
class Maza extends Arma{
    public Maza() {
        super(92, 3, 1);
    }
}