package Código;

import javax.swing.*;

public class Personaje {
    private int posicionLinea;
    private int posicionColumna;
    private ImageIcon dibujo;
    private int salud;
    private int ataque;
    private int armadura;
    private int experiencia;
    private int nivel;
    private int rango;
    private int ID;

    public Personaje(int posicionLinea, int posicionColumna, ImageIcon dibujo, int salud, int ataque, int armadura, int experiencia, int nivel, int rango, int ID) {
        this.posicionLinea = posicionLinea;
        this.posicionColumna = posicionColumna;
        this.dibujo = dibujo;
        this.salud = salud;
        this.ataque = ataque;
        this.armadura = armadura;
        this.experiencia = experiencia;
        this.nivel = nivel;
        this.rango = rango;
        this.ID = ID;
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
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

    public int getPosicionLinea() {
        return posicionLinea;
    }

    public void setPosicionLinea(int posicionLinea) {
        this.posicionLinea = posicionLinea;
    }

    public int getPosicionColumna() {
        return posicionColumna;
    }

    public void setPosicionColumna(int posicionColumna) {
        this.posicionColumna = posicionColumna;
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

    public int getArmadura() {
        return armadura;
    }

    public void setArmadura(int armadura) {
        this.armadura = armadura;
    }
    public void calcularNivel(){}
}
