package Código;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tablero extends JFrame {

    private Casilla[][] cuadradosG = new Casilla[12][12];
    private Personaje matrizLogica[][];
    private Color colorNegro = Color.BLACK;
    private JPanel panelContenedor = new JPanel();
    private ImageIcon guerreroImg = new ImageIcon("src/Imagenes/Guerrero1.png");
    private ImageIcon arqueroImg = new ImageIcon("src/Imagenes/Arquero.png");
    private ImageIcon granjeroImg = new ImageIcon("src/Imagenes/Personaje con guadaña.png");
    private ImageIcon baseImg = new ImageIcon("src/Imagenes/HQ.jpg");

    private int idPersonajes = 0;

    public Tablero(){
        setTitle("Zombie Defense");
        // IMAGENES ESCALADAS
        guerreroImg = scaleImage(guerreroImg, 75, 50);
        arqueroImg = scaleImage(arqueroImg, 75, 50);
        baseImg = scaleImage(baseImg, 75, 50);

        LableHandler lableHandler = new LableHandler();

        panelContenedor.setLayout(new GridLayout(12,12));
        panelContenedor.setBounds(210, 15, 870, 640);
        panelContenedor.setBorder(BorderFactory.createLineBorder(new Color(82, 35, 35),10));

        Guerrero guerrero = new Guerrero(0, 0, guerreroImg, 100, 20, 0, 0, 1, 2, idPersonajes);
        Arquero arquero = new Arquero(0, 4, arqueroImg, 80, 15, 0, 0, 11, 5 ,idPersonajes);

        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 12; j++){
                cuadradosG[i][j] = new Casilla(0);
                panelContenedor.add(cuadradosG[i][j]);
                cuadradosG[i][j].addActionListener(lableHandler);
                cuadradosG[i][j].setBackground(new Color(45, 106, 31));
            }
        }
        cuadradosG[11][0].setIcon(baseImg);
        cuadradosG[11][0].setEstado(1);

        cuadradosG[guerrero.getPosicionLinea()][guerrero.getPosicionColumna()].setIcon(guerrero.getDibujo());
        cuadradosG[guerrero.getPosicionLinea()][guerrero.getPosicionColumna()].setEstado(4);
        cuadradosG[arquero.getPosicionLinea()][arquero.getPosicionColumna()].setIcon(arquero.getDibujo());
        cuadradosG[arquero.getPosicionLinea()][arquero.getPosicionColumna()].setEstado(4);

        setSize( 1100, 700);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panelContenedor);
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
    private class PanelTab extends JPanel{

    }
    public ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
}
