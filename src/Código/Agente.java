package Código;

import javax.swing.*;

public class Agente extends Personaje{
    private boolean moverseDoble=false;
    private boolean regeneracion=false;
    private boolean duplicarExperiencia=false;
    // DEBO PONER LA IMAGEN POR CADA PERSONAJE


    public Agente(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Agente.jpg"),110, 10, 0, new Pistola());
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
