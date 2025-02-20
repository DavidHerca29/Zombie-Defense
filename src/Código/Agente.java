package Código;

import javax.swing.*;

public class Agente extends Personaje{
    private boolean moverseDoble=false;
    private boolean regeneracion=false;
    private boolean duplicarExperiencia=false;
    // DEBO PONER LA IMAGEN POR CADA PERSONAJE


    public Agente(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Agente.jpg"),110, 30, 0, new Pistola(), 1, 1);
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
    public void aumentarNivel(int masExperiencia) {
        if (duplicarExperiencia)
            masExperiencia *=2;
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
        if (moverseDoble){
            setDesplazamientoPorTurno(2);
        }
        else {
            setDesplazamientoPorTurno(1);
        }
        if (regeneracion)
            setSalud(getSalud()+10);
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
        actualizarArma();
    }

    @Override
    public void actualizarArma() {
        if (this.getNivel()==3)
            this.setArma(new Rifle());
    }
}
