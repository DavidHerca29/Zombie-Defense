package Código;

import javax.swing.*;
import java.awt.*;

public abstract class Personaje {
    private int X;
    private int Y;
    private ImageIcon dibujo;
    private int salud;
    private int armadura;
    private int experiencia;
    private int nivel;
    private int rango;
    private Arma arma;
    private int ataquesPorTurno;
    private int desplazamientoPorTurno;
    //private int ID;


    public Personaje(int X, int Y, ImageIcon dibujo, int salud, int armadura, int experiencia, Arma arma, int ataquesPorTurno, int desplazamientoPorTurno) {
        this.X = X;
        this.Y = Y;
        this.dibujo = dibujo;
        this.salud = salud;
        this.armadura = armadura;
        this.experiencia = experiencia;
        this.nivel = 1;
        this.arma = arma;
        this.desplazamientoPorTurno = desplazamientoPorTurno;
        this.ataquesPorTurno = ataquesPorTurno;
    }

    public int getAtaquesPorTurno() {
        return ataquesPorTurno;
    }

    public void setAtaquesPorTurno(int ataquesPorTurno) {
        this.ataquesPorTurno = ataquesPorTurno;
    }

    public int getDesplazamientoPorTurno() {
        return desplazamientoPorTurno;
    }

    public void setDesplazamientoPorTurno(int desplazamientoPorTurno) {
        this.desplazamientoPorTurno = desplazamientoPorTurno;
    }

    public abstract void resetTurno();
    public ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
    public void aumentarNivel(int masExperiencia){
        this.setExperiencia(this.getExperiencia()+masExperiencia);
        if (this.getExperiencia()<15)
            setNivel(1);
        else if (15<=this.getExperiencia() && this.getExperiencia()<35)
            setNivel(2);
        else if (35<=this.getExperiencia() && this.getExperiencia()<60) {
            setNivel(3);
        }
        else setNivel(4);
        this.actualizarNivel();
    }
    public abstract void actualizarNivel();


    public Arma getArma() {
        return arma;
    }

    public void setArma(Arma arma) {
        this.arma = arma;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void RecibirDano(int danoRecibido){
        danoRecibido = danoRecibido*this.armadura/100;
        this.setSalud(this.getSalud()-danoRecibido);
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        this.X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        this.Y = y;
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

    public int getArmadura() {
        return armadura;
    }

    public void setArmadura(int armadura) {
        this.armadura = armadura;
    }
    public void calcularNivel(){}
}
