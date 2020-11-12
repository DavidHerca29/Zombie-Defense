package Código;

import javax.swing.*;

public class Guerrero extends Personaje{
    private boolean habAtacarDoble=false;
    private boolean habAmortiguar=false;
    private boolean evadirAtaque=false;

    public Guerrero(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Guerrero1.png"), 180,60, 0, new Bate(),1,3);
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
    public void aumentarNivel(int masExperiencia) {
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

    @Override
    public void resetTurno() {
        if (habAtacarDoble){
            setAtaquesPorTurno(2);
        }
        else {
            setAtaquesPorTurno(1);
        }
        setDesplazamientoPorTurno(3);
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
        actualizarArma();
    }
    public void actualizarArma() {
        if (this.getNivel()==3)
            this.setArma(new Maza());
        else if (this.getNivel()==4)
            this.setArma(new Sierra());
    }
}
