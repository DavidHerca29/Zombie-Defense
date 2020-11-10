package Código;

import javax.swing.*;
import java.awt.*;

public abstract class Zombie {
    private int posX;
    private int posY;
    private ImageIcon dibujo;
    private int salud;
    private int ataque;
    private int rangoVision;
    private int rangoAtaque;

    public Zombie(int posX, int posY, ImageIcon dibujo, int salud, int ataque, int rangoVision, int rangoAtaque) {
        this.posX = posX;
        this.posY = posY;
        this.dibujo = dibujo;
        this.salud = salud;
        this.ataque = ataque;
        this.rangoVision = rangoVision;
        this.rangoAtaque = rangoAtaque;
    }

    public int getRangoAtaque() {
        return rangoAtaque;
    }

    public void setRangoAtaque(int rangoAtaque) {
        this.rangoAtaque = rangoAtaque;
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
    public abstract void recibirDano(int danoRecibido);

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

    public ImageIcon getDibujo() {
        return dibujo;
    }

    public void setDibujo(ImageIcon dibujo) {
        this.dibujo = dibujo;
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getRangoVision() {
        return rangoVision;
    }

    public void setRangoVision(int rangoVision) {
        this.rangoVision = rangoVision;
    }
}
