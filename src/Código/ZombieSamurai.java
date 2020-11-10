package Código;

import javax.swing.*;

public class ZombieSamurai extends Zombie{
    public ZombieSamurai(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Zombie Samurai.jpg"), 140, 60, 3,1);
        this.setDibujo(scaleImage(getDibujo(),80,80));
    }

    @Override
    public void recibirDano(int danoRecibido) {
        danoRecibido = (int) (danoRecibido*0.85);
        setSalud(getSalud()-danoRecibido);
    }
}
