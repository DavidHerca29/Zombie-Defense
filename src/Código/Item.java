package Código;

import javax.swing.*;
import java.awt.*;

public abstract class Item {
    private ImageIcon imagen;

    public ImageIcon getImagen() {
        return imagen;
    }

    public void setImagen(ImageIcon imagen) {
        this.imagen = imagen;
    }

    public Item(ImageIcon imagen) {
        this.imagen = imagen;
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
}
class Bebida extends Item{
    private int masSalud = 20;
    public Bebida() {
        super(new ImageIcon("src/Imagenes/Bebida.jpg"));
        this.setImagen(scaleImage(getImagen(),50,50));
    }

    public int getMasSalud() {
        return masSalud;
    }

    public void setMasSalud(int masSalud) {
        this.masSalud = masSalud;
    }
}
class Insignia extends Item{
    private int masExperiencia = 10;
    public Insignia() {
        super(new ImageIcon("src/Imagenes/Insignia.jpg"));
        this.setImagen(scaleImage(getImagen(),50,50));
    }

    public int getMasExperiencia() {
        return masExperiencia;
    }

    public void setMasExperiencia(int masExperiencia) {
        this.masExperiencia = masExperiencia;
    }
}
class Energia extends Item{
    public Energia() {
        super(new ImageIcon("src/Imagenes/Energia.jpg"));
        this.setImagen(scaleImage(getImagen(),50,50));
    }
}