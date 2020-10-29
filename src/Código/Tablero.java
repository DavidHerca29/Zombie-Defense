package Código;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Tablero extends JFrame {
    private JButton[][] cuadradosG = new JButton[12][12];
    private Personaje matrizLogica[][];
    private Color colorNegro = Color.BLACK;
    private Container contenedor;
    private ImageIcon guerreroImg = new ImageIcon("src/Imagenes/Guerrero1.png");
    private ImageIcon arqueroImg = new ImageIcon("src/Imagenes/Arquero.png");
    private ImageIcon granjeroImg = new ImageIcon("src/Imagenes/Personaje con guadaña.png");

    private int idPersonajes = 0;

    public Tablero(){
        setTitle("Zombie Defense");

        LableHandler lableHandler = new LableHandler();

        contenedor = getContentPane();
        contenedor.setLayout(new GridLayout(12,12));
        //contenedor.setVisible(true);

        Guerrero guerrero = new Guerrero(0, 0, guerreroImg, 100, 20, 0, 0, 1, 2, idPersonajes);
        Arquero arquero = new Arquero(0, 4, arqueroImg, 80, 15, 0, 0, 11, 5 ,idPersonajes);

        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 12; j++){
                cuadradosG[i][j] = new JButton();
                contenedor.add(cuadradosG[i][j]);
                cuadradosG[i][j].addActionListener(lableHandler);
                cuadradosG[i][j].setBackground(new Color(45, 106, 31));
            }
        }
        cuadradosG[guerrero.getPosicionLinea()][guerrero.getPosicionColumna()].setIcon(scaleImage(guerrero.getDibujo(), 50, 50));
        cuadradosG[arquero.getPosicionLinea()][arquero.getPosicionColumna()].setIcon(scaleImage(arquero.getDibujo(), 50, 50));

        setSize( 1000, 700);
        setResizable(true);
        setVisible(true);
        setLocationRelativeTo(null);
        //setLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private class LableHandler  implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

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
