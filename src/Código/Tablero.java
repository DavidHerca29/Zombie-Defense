package Código;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Tablero extends JFrame {

    private Casilla[][] cuadradosG = new Casilla[12][12];
    private Personaje matrizLogica[][];
    private Color colorNegro = Color.BLACK;
    private JPanel panelContenedor = new JPanel();

    private ArrayList<Personaje> personajes = new ArrayList<Personaje>();
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private ArrayList<Casilla> spawningPoints = new ArrayList<Casilla>();

    private LableHandler lableHandler = new LableHandler();

    private int idPersonajes = 0;

    public Tablero(){
        setSize( 1100, 700);
        setResizable(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Zombie Defense");

        panelContenedor.setLayout(new GridLayout(12,12));
        panelContenedor.setBounds(210, 15, 870, 640);
        panelContenedor.setBorder(BorderFactory.createLineBorder(new Color(82, 35, 35),10));

        Guerrero guerrero = new Guerrero(7, 0);
        Arquero arquero = new Arquero(6, 0);
        Agente agente = new Agente(8,0);
        personajes.add(guerrero);
        personajes.add(arquero);
        personajes.add(agente);


        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 12; j++){
                if (i==11 && j==0){
                    cuadradosG[i][j] = new Base(i,j);
                }
                else if (i==0 && j==11){
                    cuadradosG[0][11] = new SpawningPoint(0,11);
                }
                else {
                    cuadradosG[i][j] = new Casilla(i,j);
                }
                panelContenedor.add(cuadradosG[i][j]);
                cuadradosG[i][j].addActionListener(lableHandler);
            }
        }
        for (int i=0; i<personajes.size(); i++){
            cuadradosG[personajes.get(i).getPosicionLinea()][personajes.get(i).getPosicionColumna()] = new JugadorCasilla(personajes.get(i).getPosicionLinea(),personajes.get(i).getPosicionColumna(), personajes.get(i));
        }
        panelContenedor.setVisible(true);
        panelContenedor = ActualizarTablero();

        add(panelContenedor);
    }
    private JPanel ActualizarTablero(){
        JPanel tab = new JPanel();
        tab.setLayout(new GridLayout(12,12));
        tab.setBounds(210, 15, 870, 640);
        tab.setBorder(BorderFactory.createLineBorder(new Color(82, 35, 35),10));
        for (int i=0;i<12;i++){
            for (int j=0;j<12;j++){
                tab.add(cuadradosG[i][j]);
                cuadradosG[i][j].addActionListener(lableHandler);
            }
        }
        return tab;
    }
    private class LableHandler  implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object botonPresionado = e.getSource();
            for (int i=0; i<12; i++){
                for (int j=0;j<12;j++){
                    if (botonPresionado==cuadradosG[i][j]){
                        System.out.println(cuadradosG[i][j].getX() + " , " + cuadradosG[i][j].getY());
                        cuadradosG[i][j].setBorder(BorderFactory.createLineBorder(Color.RED));
                    }
                }
            }
        }
    }
    private class PanelStats extends JPanel{

    }
}
