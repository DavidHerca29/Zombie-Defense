package Código;

import javax.swing.*;

public class Agente extends Personaje{
    private boolean moverseDoble=false;
    private boolean regeneracion=false;
    private boolean duplicarExperiencia=false;
    // DEBO PONER LA IMAGEN POR CADA PERSONAJE


    public Agente(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Agente.jpg"),110, 35, 0, new Pistola(), 1, 1);
        this.setDibujo(scaleImage(getDibujo(),50,50));
    }

    public boolean isMoverseDoble() {
        return moverseDoble;
    }

    public void setMoverseDoble(boolean moverseDoble) {
        this.moverseDoble = moverseDoble;
    }

    public boolean isRegeneracion() {
        return regeneracion;
    }

    public void setRegeneracion(boolean regeneracion) {
        this.regeneracion = regeneracion;
    }

    public boolean isDuplicarExperiencia() {
        return duplicarExperiencia;
    }

    public void setDuplicarExperiencia(boolean duplicarExperiencia) {
        this.duplicarExperiencia = duplicarExperiencia;
    }

    @Override
    public void resetTurno() {
        if (moverseDoble){
            setDesplazamientoPorTurno(2);
        }
        else {
            setDesplazamientoPorTurno(1);
        }
        setAtaquesPorTurno(1);
    }

    @Override
    public void actualizarNivel() {
        if (this.getNivel()>=2){
            this.setDuplicarExperiencia(true);
            if (this.getNivel()>=3){
                this.setRegeneracion(true);
                if (this.getNivel()==4)
                    this.setMoverseDoble(true);
            }
        }
    }
}
