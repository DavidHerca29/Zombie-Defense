package Código;

import javax.swing.*;

public class Guerrero extends Personaje{
    private boolean habAtacarDoble=false;
    private boolean habAmortiguar=false;
    private boolean evadirAtaque=false;

    public Guerrero(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Guerrero1.png"), 150,5, 0, new Maza(),1,1);
        this.setDibujo(scaleImage(getDibujo(),50,50));
    }
    // DEBO PONER LA IMAGEN POR CADA PERSONAJE


    public boolean isEvadirAtaque() {
        return evadirAtaque;
    }

    public void setEvadirAtaque(boolean evadirAtaque) {
        this.evadirAtaque = evadirAtaque;
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

    @Override
    public void resetTurno() {
        if (habAtacarDoble){
            setAtaquesPorTurno(2);
        }
        else {
            setAtaquesPorTurno(1);
        }
        setDesplazamientoPorTurno(1);
    }

    @Override
    public void actualizarNivel() {
        if (this.getNivel()>=2){
            this.setEvadirAtaque(true);
            if (this.getNivel()>=3){
                this.setHabAmortiguar(true);
                if (this.getNivel()==4)
                    this.setHabAtacarDoble(true);
            }
        }
    }
}
