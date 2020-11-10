package Código;

import javax.swing.*;

public class ZombieBasico extends Zombie{
    public ZombieBasico(int posicionLinea, int posicionColumna) {
        super(posicionLinea, posicionColumna, new ImageIcon("src/Imagenes/Zombie Animado.png"), 80, 20, 3,1);
        this.setDibujo(scaleImage(getDibujo(),50,50));
    }

    @Override
    public void recibirDano(int danoRecibido) {
        setSalud(getSalud()-danoRecibido);
    }
}
