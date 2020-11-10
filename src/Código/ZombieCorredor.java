package Código;

import javax.swing.*;

public class ZombieCorredor extends Zombie{
    public ZombieCorredor(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Corredor.jpg"), 100, 50, 7, 2);
        this.setDibujo(scaleImage(getDibujo(),50,50));
    }
    @Override
    public void recibirDano(int danoRecibido) {
        danoRecibido = (int) (danoRecibido*0.95);
        setSalud(getSalud()-danoRecibido);
    }
}
