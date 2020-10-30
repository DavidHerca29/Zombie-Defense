package Código;

import javax.swing.*;

public class Casilla extends JButton {
    private int estado; // 0 no esta bloqueado, 1 base, 2 spawning point, 3  bloqueado, 4 personaje, 5 zombie

    public Casilla(int estado) {
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
