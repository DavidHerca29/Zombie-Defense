package Código;

import javax.swing.*;

public class Arquero extends Personaje{
    private boolean menosRuido= false;
    private boolean subirObstaculos = false;
    private boolean danoCritico=false;
    // DEBO PONER LA IMAGEN POR CADA PERSONAJE


    public Arquero(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Arquero.png"), 70,20,0, new Arco(),1,1);
        this.setDibujo(scaleImage(getDibujo(),50,50));
    }

    public boolean isDanoCritico() {
        return danoCritico;
    }

    public void setDanoCritico(boolean danoCritico) {
        this.danoCritico = danoCritico;
    }

    public boolean isMenosRuido() {
        return menosRuido;
    }

    public void setMenosRuido(boolean menosRuido) {
        this.menosRuido = menosRuido;
    }

    public boolean isSubirObstaculos() {
        return subirObstaculos;
    }

    public void setSubirObstaculos(boolean subirObstaculos) {
        this.subirObstaculos = subirObstaculos;
    }

    @Override
    public void resetTurno() {
        this.setAtaquesPorTurno(1);
        this.setDesplazamientoPorTurno(1);
    }

    @Override
    public void actualizarNivel() {
        if (this.getNivel()>=2){
            this.setDanoCritico(true);
            if (this.getNivel()>=3){
                this.setSubirObstaculos(true);
                if (this.getNivel()==4)
                    this.setMenosRuido(true);
            }
        }
    }
}
