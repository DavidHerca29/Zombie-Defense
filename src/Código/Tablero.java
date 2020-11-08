package Código;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Tablero extends JFrame {

    protected JButton botonTurno = new JButton("Terminar Turno");

    private Casilla[][] cuadradosGLogico = new Casilla[12][12];
    private JButton[][] botonesGraficos = new JButton[12][12];
    private JInternalFrame panelContenedor = new JInternalFrame();

    private ArrayList<Personaje> personajes = new ArrayList<Personaje>();
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private ArrayList<Casilla> spawningPoints = new ArrayList<Casilla>();

    private LableHandler lableHandler = new LableHandler();
    private PanelStats statsPanel = new PanelStats();

    private int turno = 0;
    private boolean guerreroSeleccionado = false;
    private boolean arqueroSeleccionado = false;
    private boolean agenteSeleccionado = false;

    public Tablero(){
        setSize( 1235, 726);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        //setUndecorated(true);
        //setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Zombie Defense");


        panelContenedor.setLayout(new GridLayout(12,12));
        panelContenedor.setBounds(300, -30, 925, 726);
        panelContenedor.setResizable(false);
        panelContenedor.setVisible(true);
        panelContenedor.setEnabled(true);

        Guerrero guerrero = new Guerrero(7, 0);
        Arquero arquero = new Arquero(6, 0);
        Agente agente = new Agente(8,0);
        personajes.add(guerrero);
        personajes.add(arquero);
        personajes.add(agente);


        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 12; j++){
                if (i==11 && j==0){
                    cuadradosGLogico[i][j] = new Base(i,j);
                }
                else if (i==0 && j==11){
                    cuadradosGLogico[0][11] = new SpawningPoint(0,11);
                }
                else {
                    cuadradosGLogico[i][j] = new Casilla(i,j);
                }
                botonesGraficos[i][j] = new JButton(cuadradosGLogico[i][j].getIcon());
                panelContenedor.add(botonesGraficos[i][j]);
                botonesGraficos[i][j].addActionListener(lableHandler);
            }
        }
        for (int i=0; i<personajes.size(); i++){
            cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(i).getX(),personajes.get(i).getY(), personajes.get(i));
        }
        panelContenedor.setVisible(true);
        panelContenedor.setClosable(false);

        ActualizarTablero();
        statsPanel.actualizarPaneles();

        add(panelContenedor);
        add(statsPanel);
        turnoJugador();
    }
    private void turnoJugador(){

    }
    private void ActualizarTablero(){
        for (int i=0;i<12;i++){
            for (int j=0;j<12;j++){
                botonesGraficos[i][j].setIcon(cuadradosGLogico[i][j].getIcon());
                botonesGraficos[i][j].setBorder(cuadradosGLogico[i][j].getBorder());
            }
        }
    }
    private class LableHandler  implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object botonPresionado = e.getSource();
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    if (botonPresionado == botonesGraficos[i][j]) {
                        if (cuadradosGLogico[i][j] instanceof JugadorCasilla) {
                            seleccionarPersonaje(i,j);
                        }
                        else if (cuadradosGLogico[i][j] instanceof ZombieCasilla){
                            //atacarZombie();
                        }
                        else if (cuadradosGLogico[i][j] instanceof Base){
                            //moverABase();
                        }
                        else if (cuadradosGLogico[i][j] instanceof SpawningPoint){
                            //SpawPSelect();
                        }
                        else if (cuadradosGLogico[i][j] instanceof ItemCasilla){
                            //moverAItemCasilla();
                        }
                        else if (cuadradosGLogico[i][j] instanceof Montaña || cuadradosGLogico[i][j] instanceof Escombro){
                            //moverAObstaculo();
                        }
                        else if (agenteSeleccionado || guerreroSeleccionado || arqueroSeleccionado){
                            moverPersonaje(i,j);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Debe seleccionar un personaje para poder realizar una acción.");
                        }
                    }
                    else {
                        if (cuadradosGLogico[i][j] instanceof JugadorCasilla){
                            if (botonPresionado != botonesGraficos[i][j])
                                cuadradosGLogico[i][j].setBorder(UIManager.getBorder("Button.border"));
                        }else
                            cuadradosGLogico[i][j].setBorder(UIManager.getBorder("Button.border"));
                    }
                }
            }
            statsPanel.actualizarPaneles();
            ActualizarTablero();
        }
    }
    private void seleccionarPersonaje(int i, int j){
        if (((JugadorCasilla) cuadradosGLogico[i][j]).getPersonaje() instanceof Guerrero) {
            cuadradosGLogico[i][j].setBorder(BorderFactory.createLineBorder(Color.RED));
            agenteSeleccionado = false;
            guerreroSeleccionado = true;
            arqueroSeleccionado = false;
        } else if (((JugadorCasilla) cuadradosGLogico[i][j]).getPersonaje() instanceof Arquero) {
            cuadradosGLogico[i][j].setBorder(BorderFactory.createLineBorder(Color.RED));
            agenteSeleccionado = false;
            guerreroSeleccionado = false;
            arqueroSeleccionado = true;
        } else {
            cuadradosGLogico[i][j].setBorder(BorderFactory.createLineBorder(Color.RED));
            agenteSeleccionado = true;
            guerreroSeleccionado = false;
            arqueroSeleccionado = false;
        }
    }
    private void moverPersonaje(int moverX, int moverY){
        int actualX;
        int actualY;
        for (int k=0;k<personajes.size();k++){
            if (personajes.get(k) instanceof Guerrero){
                if (guerreroSeleccionado){
                    actualX = personajes.get(k).getX();
                    actualY = personajes.get(k).getY();
                    if (isValidMove(actualX, actualY, moverX, moverY)){
                        cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = new Casilla(personajes.get(k).getX(),personajes.get(k).getY());
                        personajes.get(k).setX(moverX);
                        personajes.get(k).setY(moverY);
                        cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getX(),personajes.get(k).getY(), personajes.get(k));
                        cuadradosGLogico[moverX][moverY].setBorder(BorderFactory.createLineBorder(Color.RED));
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                    }
                }
            }
            else if (personajes.get(k) instanceof Arquero){
                if (arqueroSeleccionado){
                    actualX = personajes.get(k).getX();
                    actualY = personajes.get(k).getY();
                    if (isValidMove(actualX, actualY, moverX, moverY)){
                        cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = new Casilla(personajes.get(k).getX(),personajes.get(k).getY());
                        personajes.get(k).setX(moverX);
                        personajes.get(k).setY(moverY);
                        cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getX(),personajes.get(k).getY(), personajes.get(k));
                        cuadradosGLogico[moverX][moverY].setBorder(BorderFactory.createLineBorder(Color.RED));
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                    }
                }
            }
            else {
                if (agenteSeleccionado){
                    actualX = personajes.get(k).getX();
                    actualY = personajes.get(k).getY();
                    if (isValidMove(actualX, actualY, moverX, moverY)){
                        cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = new Casilla(personajes.get(k).getX(),personajes.get(k).getY());
                        personajes.get(k).setX(moverX);
                        personajes.get(k).setY(moverY);
                        cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getX(),personajes.get(k).getY(), personajes.get(k));
                        cuadradosGLogico[moverX][moverY].setBorder(BorderFactory.createLineBorder(Color.RED));
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                    }
                }
            }
        }
    }
    private boolean isValidMove(int actualX,int actualY,int moverX,int moverY){
        int difX = Math.abs(actualX-moverX);
        int difY = Math.abs(actualY-moverY);
        if (difX <= 1 && difY <= 1){
            return true;
        }
        return false;
    }
    private class TurnoListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Object botonPres = e.getSource();
            if (botonPres == botonTurno){
                botonTurno.setEnabled(false);
                System.out.println("Presionado");
            }
        }
    }
    private class PanelStats extends JPanel{
        private JLabel saludGuerrero = new JLabel("Guerrero: ");
        private JLabel saludArquero = new JLabel("Arquero: ");
        private JLabel saludAgente = new JLabel("Agente: ");
        private JLabel ataqueGuerrero = new JLabel("Guerrero: ");
        private JLabel ataqueArquero = new JLabel("Arquero: ");
        private JLabel ataqueAgente = new JLabel("Agente: ");
        private JLabel nivelGuerrero = new JLabel("Guerrero: ");
        private JLabel nivelArquero = new JLabel("Arquero: ");
        private JLabel nivelAgente = new JLabel("Agente: ");

        private JTextArea resultadosLabel = new JTextArea();
        private JScrollPane scrollResultado = new JScrollPane();
        private TurnoListener turnoListener = new TurnoListener();
        public PanelStats() {
            setBounds(0,0,300, 726);
            setVisible(true);
            setBackground(new Color(28, 89, 95));
            setLayout(null);
            saludAgente.setBounds(5,5,285,20);
            saludArquero.setBounds(5,95,285,20);
            saludGuerrero.setBounds(5,185,285,20);

            saludAgente.setForeground(Color.WHITE);
            saludArquero.setForeground(Color.WHITE);
            saludGuerrero.setForeground(Color.WHITE);
            ataqueAgente.setForeground(Color.WHITE);
            ataqueArquero.setForeground(Color.WHITE);
            ataqueGuerrero.setForeground(Color.WHITE);
            nivelAgente.setForeground(Color.WHITE);
            nivelArquero.setForeground(Color.WHITE);
            nivelGuerrero.setForeground(Color.WHITE);

            saludAgente.setFont(new Font("HelveticaNeue-Bold", 10,12));
            saludArquero.setFont(new Font("HelveticaNeue-Bold", 10,12));
            saludGuerrero.setFont(new Font("HelveticaNeue-Bold", 10,12));
            ataqueAgente.setFont(new Font("HelveticaNeue-Bold", 10,12));
            ataqueArquero.setFont(new Font("HelveticaNeue-Bold", 10,12));
            ataqueGuerrero.setFont(new Font("HelveticaNeue-Bold", 10,12));
            nivelAgente.setFont(new Font("HelveticaNeue-Bold", 10,12));
            nivelArquero.setFont(new Font("HelveticaNeue-Bold", 10,12));
            nivelGuerrero.setFont(new Font("HelveticaNeue-Bold", 10,12));

            ataqueAgente.setBounds(5,35,285,20);
            ataqueArquero.setBounds(5,125,285,20);
            ataqueGuerrero.setBounds(5,215,285,20);

            nivelAgente.setBounds(5,65,285,20);
            nivelArquero.setBounds(5,155,285,20);
            nivelGuerrero.setBounds(5,245,285,20);

            scrollResultado.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            //resultadosLabel.setBounds(10, 350,280, 325);
            scrollResultado.setBounds(10, 350,280, 325);
            scrollResultado.getViewport().setBackground(Color.lightGray);
            scrollResultado.getViewport().add(resultadosLabel);
            resultadosLabel.setVisible(true);
            resultadosLabel.setEditable(false);
            resultadosLabel.setFont(new Font("HelveticaNeue-Bold", 10,12));

            botonTurno.setBackground(new Color(116, 14, 6));
            botonTurno.setForeground(new Color(253, 120, 48));
            botonTurno.setBounds(80, 280, 150, 40);
            botonTurno.setVisible(true);
            botonTurno.addActionListener(turnoListener);

            add(saludAgente);
            add(saludArquero);
            add(saludGuerrero);
            add(ataqueAgente);
            add(ataqueArquero);
            add(ataqueGuerrero);
            add(nivelAgente);
            add(nivelArquero);
            add(nivelGuerrero);
            add(scrollResultado);
            add(botonTurno);
        }
        private void actualizarPaneles(){
            for (int i = 0; i<personajes.size(); i++){
                if (personajes.get(i)instanceof Guerrero){
                    saludGuerrero.setText("Salud del guerrero: "+personajes.get(i).getSalud());
                    ataqueGuerrero.setText("Ataque del guerrero: "+personajes.get(i).getArma().getDano());
                    nivelGuerrero.setText("Nivel del guerrero: "+personajes.get(i).getNivel());
                }
                else if (personajes.get(i)instanceof Arquero){
                    saludArquero.setText("Salud del arquero: "+personajes.get(i).getSalud());
                    ataqueArquero.setText("Ataque del arquero: "+personajes.get(i).getArma().getDano());
                    nivelArquero.setText("Nivel del aruqero: "+personajes.get(i).getNivel());
                }
                else {
                    saludAgente.setText("Salud del agente: "+personajes.get(i).getSalud());
                    ataqueAgente.setText("Ataque del agente: "+personajes.get(i).getArma().getDano());
                    nivelAgente.setText("Nivel del agente: "+personajes.get(i).getNivel());
                    }
            }
        }
    }
}

