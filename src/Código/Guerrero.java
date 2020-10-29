package Código;

import javax.swing.*;

public class Guerrero extends Personaje{
    private boolean habAtacarDoble=false;
    private boolean habAmortiguar=false;

    public Guerrero(int posicionLinea, int posicionColumna, ImageIcon dibujo, int salud, int ataque, int armadura, int experiencia, int nivel, int rango, int ID) {
        super(posicionLinea, posicionColumna, dibujo, salud, ataque, armadura, experiencia, nivel, rango, ID);
    }

    public void calcularNivel() {
        if (this.getExperiencia()>25 && this.getExperiencia()<50)
            setNivel(2);
        else if (this.getExperiencia()>=50)
            setNivel(3);
        else
            setNivel(1);
    }

    public boolean isHabAtacarDoble() {
        return habAtacarDoble;
    }

    public void setHabAtacarDoble(boolean habAtacarDoble) {
        this.habAtacarDoble = habAtacarDoble;
    }

    public boolean isHabAmortiguar() {
        return habAmortiguar;
    }

    public void setHabAmortiguar(boolean habAmortiguar) {
        this.habAmortiguar = habAmortiguar;
    }
}
