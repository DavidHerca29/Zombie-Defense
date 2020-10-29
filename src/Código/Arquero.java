package Código;

import javax.swing.*;

public class Arquero extends Personaje{
    private boolean masRango= false;

    public Arquero(int posicionLinea, int posicionColumna, ImageIcon dibujo, int salud, int ataque, int armadura, int experiencia, int nivel, int rango, int ID) {
        super(posicionLinea, posicionColumna, dibujo, salud, ataque, armadura, experiencia, nivel, rango, ID);
    }

    public boolean isMasRango() {
        return masRango;
    }

    public void setMasRango(boolean masRango) {
        this.masRango = masRango;
    }
}
