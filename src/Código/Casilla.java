package Código;

import javax.swing.*;
import java.awt.*;

public class Casilla extends JButton {
    private int posX;
    private int posY;
    private ImageIcon imagen;
    // En vez de estado vamos a usar herencia


    public Casilla(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public ImageIcon getImagen() {
        return imagen;
    }

    public void setImagen(ImageIcon imagen) {
        this.imagen = imagen;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
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
class Base extends Casilla{
    public Base(int posX, int posY) {
        super(posX, posY);
        this.setImagen(new ImageIcon("src/Imagenes/HQ.jpg"));
        this.setImagen(this.scaleImage(this.getImagen(),50,50));
        this.setIcon(this.getImagen());
    }
}
class SpawningPoint extends Casilla{
    public SpawningPoint(int posX, int posY) {
        super(posX, posY);
        this.setImagen(new ImageIcon("src/Imagenes/Tumba.jpg"));
        this.setImagen(this.scaleImage(this.getImagen(),50,50));
        this.setIcon(this.getImagen());
    }
}
class Montaña extends Casilla{
    public Montaña(int posX, int posY) {
        super(posX, posY);
        this.setImagen(new ImageIcon("src/Imagenes/Montana.png"));
        this.setImagen(scaleImage(getImagen(),50,50));
        this.setIcon(getImagen());
    }
}
class Escombro extends Casilla{
    public Escombro(int posX, int posY) {
        super(posX, posY);
        this.setImagen(new ImageIcon("src/Imagenes/Escomro.jpg"));
        this.setImagen(scaleImage(getImagen(),50,50));
        this.setIcon(getImagen());
    }
}
class JugadorCasilla extends Casilla{
    private Personaje personaje;
    public JugadorCasilla(int posX, int posY, Personaje personaje) {
        super(posX, posY);
        this.personaje=personaje;
        this.setIcon(personaje.getDibujo());
    }

    public Personaje getPersonaje() {
        return personaje;
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;
    }
}
class ZombieCasilla extends Casilla{
    private Zombie zombie;
    public ZombieCasilla(int posX, int posY, Zombie zombie) {
        super(posX, posY);
        this.zombie = zombie;
        this.setIcon(zombie.getDibujo());
    }

    public Zombie getZombie() {
        return zombie;
    }

    public void setZombie(Zombie zombie) {
        this.zombie = zombie;
    }
}
class ItemCasilla extends Casilla{
    private Item item;
    public ItemCasilla(int posX, int posY, Item item) {
        super(posX, posY);
        this.item=item;
        this.setImagen(this.getItem().getImagen());
        this.setIcon(getImagen());
    }
    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }
}

