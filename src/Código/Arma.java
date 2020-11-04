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
class Hacha extends Arma{

    public Hacha() {
        super(60, 2, 1);
    }
}
class Arco extends Arma{

    public Arco() {
        super(42, 0, 6);
    }
}
class Sierra extends Arma{

    public Sierra() {
        super(85, 8, 2);
    }
}
class Pistola extends Arma{
    public Pistola() {
        super(55, 6, 7);
    }
}
class Bate extends Arma{
    public Bate() {
        super(38, 3, 1);
    }
}
class Francotirador extends Arma{
    public Francotirador() {
        super(120, 12, 10);
    }
}
class Maza extends Arma{
    public Maza() {
        super(48, 3, 1);
    }
}