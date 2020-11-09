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
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private ArrayList<SpawningPoint> spawningPoints = new ArrayList<SpawningPoint>();

    private LableHandler lableHandler = new LableHandler();
    private PanelStats statsPanel = new PanelStats();

    private int turno = 0;
    private boolean guerreroSeleccionado = false;
    private boolean arqueroSeleccionado = false;
    private boolean agenteSeleccionado = false;
    private boolean arqueroEncaramado = false;
    private boolean obstaculoEnMedio = false;
    private Casilla respaldoCasilla;

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

        CrearMapaNuevo();
        for (int i=0;i<5;i++){

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
    private void CrearMapaNuevo(){
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 12; j++){
                if (i==11 && j==0){
                    cuadradosGLogico[i][j] = new Base(i,j);
                }
                else if (i==0 && j==11){
                    if (!spawningPoints.isEmpty()){
                        spawningPoints.clear();
                    }
                    cuadradosGLogico[0][11] = new SpawningPoint(0,11);
                    spawningPoints.add((SpawningPoint) cuadradosGLogico[0][11]);
                }
                else {
                    cuadradosGLogico[i][j] = new Casilla(i,j);
                }
                botonesGraficos[i][j] = new JButton(cuadradosGLogico[i][j].getIcon());
                panelContenedor.add(botonesGraficos[i][j]);
                botonesGraficos[i][j].addActionListener(lableHandler);
            }
        }
        AñadirPersonajes();
        AñadirObstaculos();
    }
    private void AñadirObstaculos(){
        int numAleatorioX;
        int numAleatorioY;
        boolean bandCiclo;
        for (int i =0;i<8;i++){
            bandCiclo = true;
            while (bandCiclo) {
                numAleatorioX = (int) Math.floor(Math.random() * (12));
                numAleatorioY = (int) Math.floor(Math.random() * (12));
                if (cuadradosGLogico[numAleatorioX][numAleatorioY].getImagen() == null) {
                    cuadradosGLogico[numAleatorioX][numAleatorioY] = new Montaña(numAleatorioX, numAleatorioY);
                    bandCiclo=false;
                }
            }
        }
        for (int i=0;i<5;i++){
            bandCiclo = true;
            while (bandCiclo){
                numAleatorioX = (int) Math.floor(Math.random() * (12));
                numAleatorioY = (int) Math.floor(Math.random() * (12));
                if (cuadradosGLogico[numAleatorioX][numAleatorioY].getImagen() == null){
                    cuadradosGLogico[numAleatorioX][numAleatorioY] = new Escombro(numAleatorioX, numAleatorioY);
                    bandCiclo=false;
                }
            }
        }
    }
    private void AñadirPersonajes(){
        if (!personajes.isEmpty()){
            personajes.clear();
        }
        Guerrero guerrero = new Guerrero(7, 0);
        Arquero arquero = new Arquero(6, 0);
        Agente agente = new Agente(8,0);
        personajes.add(guerrero);
        personajes.add(arquero);
        personajes.add(agente);
        for (int i=0; i<personajes.size(); i++){
            cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = new JugadorCasilla(personajes.get(i).getX(),personajes.get(i).getY(), personajes.get(i));
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
                            atacarZombie(i,j);
                        }
                        else if (cuadradosGLogico[i][j] instanceof Base){
                            if (agenteSeleccionado || guerreroSeleccionado || arqueroSeleccionado)
                                JOptionPane.showMessageDialog(null,"Un personaje no se puede mover a la Base.");
                        }
                        else if (cuadradosGLogico[i][j] instanceof SpawningPoint){
                            SpawPSelect(i,j);
                        }
                        else if (cuadradosGLogico[i][j] instanceof ItemCasilla){
                            moverAItemCasilla(i,j);
                        }
                        else if (cuadradosGLogico[i][j] instanceof Montaña || cuadradosGLogico[i][j] instanceof Escombro){
                            moverAObstaculo(i,j);
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
    private void moverAItemCasilla(int posItemX,int posItemY){
        int actualX;
        int actualY;
        Item item = ((ItemCasilla) cuadradosGLogico[posItemX][posItemY]).getItem();
        if (arqueroSeleccionado||guerreroSeleccionado||agenteSeleccionado){
            for (int i=0;i<personajes.size();i++){
                if (guerreroSeleccionado){
                    if (personajes.get(i) instanceof Guerrero){
                        if (personajes.get(i).getDesplazamientoPorTurno()>0){
                            actualX = personajes.get(i).getX();
                            actualY = personajes.get(i).getY();
                            if (isValidMove(actualX, actualY, posItemX, posItemY)){
                                if (item instanceof Bebida){
                                    personajes.get(i).setSalud(personajes.get(i).getSalud()+((Bebida) item).getMasSalud());
                                }
                                else if (item instanceof Insignia){
                                    personajes.get(i).setExperiencia(personajes.get(i).getExperiencia()+((Insignia) item).getMasExperiencia());
                                }
                                else {
                                    personajes.get(i).setDesplazamientoPorTurno(personajes.get(i).getDesplazamientoPorTurno()+1);
                                    personajes.get(i).setAtaquesPorTurno(personajes.get(i).getAtaquesPorTurno()+1);
                                }
                                cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = new Casilla(personajes.get(i).getX(), personajes.get(i).getY());
                                personajes.get(i).setX(posItemX);
                                personajes.get(i).setY(posItemY);
                                personajes.get(i).setDesplazamientoPorTurno(personajes.get(i).getDesplazamientoPorTurno()-1);
                                cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = new JugadorCasilla(personajes.get(i).getX(), personajes.get(i).getY(), personajes.get(i));
                                cuadradosGLogico[posItemX][posItemY].setBorder(BorderFactory.createLineBorder(Color.RED));
                            } else {
                                JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "El guerrero ya no tiene movimientos restantes durante este turno.");
                            guerreroSeleccionado = false;
                        }
                    }
                }
                else if (arqueroSeleccionado) {
                    if (personajes.get(i) instanceof Arquero) {
                        if (personajes.get(i).getDesplazamientoPorTurno()>0){
                            actualX = personajes.get(i).getX();
                            actualY = personajes.get(i).getY();
                            if (isValidMove(actualX, actualY, posItemX, posItemY)){
                                if (item instanceof Bebida){
                                    personajes.get(i).setSalud(personajes.get(i).getSalud()+((Bebida) item).getMasSalud());
                                }
                                else if (item instanceof Insignia){
                                    personajes.get(i).setExperiencia(personajes.get(i).getExperiencia()+((Insignia) item).getMasExperiencia());
                                }
                                else {
                                    personajes.get(i).setDesplazamientoPorTurno(personajes.get(i).getDesplazamientoPorTurno()+1);
                                    personajes.get(i).setAtaquesPorTurno(personajes.get(i).getAtaquesPorTurno()+1);
                                }
                                if (arqueroEncaramado){
                                    cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = respaldoCasilla;
                                    personajes.get(i).setX(posItemX);
                                    personajes.get(i).setY(posItemY);
                                    arqueroEncaramado = false;
                                    respaldoCasilla = null;
                                }
                                else {
                                    cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = new Casilla(personajes.get(i).getX(), personajes.get(i).getY());
                                    personajes.get(i).setX(posItemX);
                                    personajes.get(i).setY(posItemY);
                                }
                                cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(i).getX(), personajes.get(i).getY(), personajes.get(i));
                                personajes.get(i).setDesplazamientoPorTurno(personajes.get(i).getDesplazamientoPorTurno() - 1);
                                cuadradosGLogico[posItemX][posItemY].setBorder(BorderFactory.createLineBorder(Color.RED));
                            } else {
                                JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "El Arquero ya no tiene movimientos restantes durante este turno.");
                            arqueroSeleccionado = false;
                        }
                    }
                }
                else {
                    if (personajes.get(i) instanceof Agente) {
                        if (personajes.get(i).getDesplazamientoPorTurno()>0){
                            actualX = personajes.get(i).getX();
                            actualY = personajes.get(i).getY();
                            if (isValidMove(actualX, actualY, posItemX, posItemY)){
                                if (item instanceof Bebida){
                                    personajes.get(i).setSalud(personajes.get(i).getSalud()+((Bebida) item).getMasSalud());
                                }
                                else if (item instanceof Insignia){
                                    personajes.get(i).setExperiencia(personajes.get(i).getExperiencia()+((Insignia) item).getMasExperiencia());
                                }
                                else {
                                    personajes.get(i).setDesplazamientoPorTurno(personajes.get(i).getDesplazamientoPorTurno()+1);
                                    personajes.get(i).setAtaquesPorTurno(personajes.get(i).getAtaquesPorTurno()+1);
                                }
                                cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = new Casilla(personajes.get(i).getX(), personajes.get(i).getY());
                                personajes.get(i).setX(posItemX);
                                personajes.get(i).setY(posItemY);
                                personajes.get(i).setDesplazamientoPorTurno(personajes.get(i).getDesplazamientoPorTurno()-1);
                                cuadradosGLogico[personajes.get(i).getX()][personajes.get(i).getY()] = new JugadorCasilla(personajes.get(i).getX(), personajes.get(i).getY(), personajes.get(i));
                                cuadradosGLogico[posItemX][posItemY].setBorder(BorderFactory.createLineBorder(Color.RED));
                            } else {
                                JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "El Agente ya no tiene movimientos restantes durante este turno.");
                            agenteSeleccionado = false;
                        }
                    }
                }
            }
        }
        else
            JOptionPane.showMessageDialog(null,"Seleccione a un personaje amistoso para recoger el item.");
    }
    private boolean isvalidAttack(int actualX,int actualY,int atacarX,int atacarY, int rango){
        int distanica;
        if (Math.abs(actualX-atacarX) <= rango && actualY==atacarY){
            distanica = Math.abs(actualX-atacarX);
            for (int i=1;i<distanica;i++){
                if (cuadradosGLogico[actualX+i][actualY] instanceof Montaña || cuadradosGLogico[actualX+i][actualY] instanceof Escombro || cuadradosGLogico[actualX+i][actualY] instanceof SpawningPoint) {
                    obstaculoEnMedio = true;
                    return false;
                }
            }
            return true;
        }
        else if (Math.abs(actualY-atacarY) <= rango && actualX==atacarX){
            distanica = Math.abs(actualY-atacarY);
            for (int i=1;i<distanica;i++){
                if (cuadradosGLogico[actualX][actualY+i] instanceof Montaña || cuadradosGLogico[actualX][actualY+i] instanceof Escombro || cuadradosGLogico[actualX][actualY+i] instanceof SpawningPoint) {
                    obstaculoEnMedio = true;
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    private void atacarZombie(int atacarX, int atacarY){
        int actualX;
        int actualY;
        if (arqueroSeleccionado||guerreroSeleccionado||agenteSeleccionado){
            for (int i=0;i<personajes.size();i++){
                if (guerreroSeleccionado){
                    if (personajes.get(i) instanceof Guerrero){
                        if (personajes.get(i).getAtaquesPorTurno()>0){
                            actualX = personajes.get(i).getX();
                            actualY = personajes.get(i).getY();
                            if (isvalidAttack(actualX, actualY, atacarX, atacarY, personajes.get(i).getArma().getRango())){
                                //SIGUE REALIZAR LOS ATAQUES
                                for (int z=0;z<zombies.size();z++){
                                    if (zombies.get(z).getX()==atacarX && zombies.get(z).getY() == atacarY){
                                        zombies.get(z).recibirDano(personajes.get(i).getArma().getDano());
                                        if (zombies.get(z).getSalud()<=0){
                                            cuadradosGLogico[atacarX][atacarY] = new Casilla(atacarX, atacarY);
                                            zombies.remove(z);
                                            statsPanel.registroResultados.append("Se ha inflingido "+personajes.get(i).getArma().getDano()+" a un zombie y lo ha eliminado.\n");
                                        }
                                        else {
                                            statsPanel.registroResultados.append("Se ha inflingido "+personajes.get(i).getArma().getDano()+" a un zombie.\n");
                                        }
                                        //zombies.get(z).getSalud()personajes.get(i).getArma().getDano();
                                    }
                                }
                            }
                            else if (obstaculoEnMedio){
                                JOptionPane.showMessageDialog(null, "Hay un obstaculo que impide realizar el ataque.");
                                obstaculoEnMedio=false;
                            }
                            else {
                                JOptionPane.showMessageDialog(null,"El zombie se encuentra muy lejos para ser atacado por el Guerrrero.");
                            }
                        }
                        else
                            JOptionPane.showMessageDialog(null, "El Guerrero ya no tiene ataques este turno.");
                    }
                }
                else if (arqueroSeleccionado){
                    if (personajes.get(i) instanceof Arquero){
                        if (personajes.get(i).getAtaquesPorTurno()>0){
                            actualX = personajes.get(i).getX();
                            actualY = personajes.get(i).getY();
                            if (isvalidAttack(actualX, actualY, atacarX, atacarY, personajes.get(i).getArma().getRango())){

                            }
                            else if (obstaculoEnMedio){
                                JOptionPane.showMessageDialog(null, "Hay un obstaculo que impide realizar el ataque.");
                                obstaculoEnMedio=false;
                            }
                            else {
                                JOptionPane.showMessageDialog(null,"El zombie se encuentra muy lejos para ser atacado por el Arquero.");
                            }
                        }
                        else
                            JOptionPane.showMessageDialog(null, "El Arquero ya no tiene ataques este turno.");
                    }
                }
                else {
                    if (personajes.get(i) instanceof Agente){
                        if (personajes.get(i).getAtaquesPorTurno()>0){
                            actualX = personajes.get(i).getX();
                            actualY = personajes.get(i).getY();
                            if (isvalidAttack(actualX, actualY, atacarX, atacarY, personajes.get(i).getArma().getRango())){

                            }
                            else if (obstaculoEnMedio){
                                JOptionPane.showMessageDialog(null, "Hay un obstaculo que impide realizar el ataque.");
                                obstaculoEnMedio=false;
                            }
                            else
                                JOptionPane.showMessageDialog(null,"El zombie se encuentra muy lejos para ser atacado por el Agente.");
                        }
                        else
                            JOptionPane.showMessageDialog(null, "El Agente ya no tiene ataques este turno.");
                    }
                }
            }
        }
        else
            JOptionPane.showMessageDialog(null,"Seleccione a un personaje amistoso para atacar a un zombie.");
    }
    private void SpawPSelect(int moverX, int moverY){
        int actualX;
        int actualY;
        if (agenteSeleccionado || guerreroSeleccionado || arqueroSeleccionado){
            for (int k=0;k<personajes.size();k++){
                if (agenteSeleccionado){
                    if (personajes.get(k) instanceof Agente){
                        actualX = personajes.get(k).getX();
                        actualY = personajes.get(k).getY();
                        if (isValidMove(actualX, actualY, moverX, moverY)){
                            JOptionPane.showMessageDialog(null, "El Agente no puede moverse ni atacar un Spawning Point.");
                        }
                    }
                }
                else if (guerreroSeleccionado){
                    if (personajes.get(k) instanceof Guerrero){
                        actualX = personajes.get(k).getX();
                        actualY = personajes.get(k).getY();
                        if (isValidMove(actualX, actualY, moverX, moverY)){
                            JOptionPane.showMessageDialog(null, "El Guerrero no puede moverse ni atacar un Spawning Point.");
                        }
                    }
                }
                else {
                    if (personajes.get(k) instanceof Arquero){
                        actualX = personajes.get(k).getX();
                        actualY = personajes.get(k).getY();
                        if (isValidMove(actualX, actualY, moverX, moverY)){
                            JOptionPane.showMessageDialog(null, "El Arquero no puede moverse ni atacar un Spawning Point.");
                        }
                    }
                }
            }
        }
    }
    private void moverAObstaculo(int moverX, int moverY){
        int actualX;
        int actualY;
        if (guerreroSeleccionado || agenteSeleccionado){
            JOptionPane.showMessageDialog(null, "Solamente el arquero puede escalar montañas.");
        }
        else {
            for (int k = 0; k < personajes.size(); k++) {
                if (personajes.get(k) instanceof Arquero) {
                    if (arqueroSeleccionado){
                        if (personajes.get(k).getDesplazamientoPorTurno()>0) {
                            actualX = personajes.get(k).getX();
                            actualY = personajes.get(k).getY();
                            if (isValidMove(actualX, actualY, moverX, moverY)) {
                                if (((Arquero) personajes.get(k)).isSubirObstaculos()) {
                                    if (!arqueroEncaramado) {
                                        cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = new Casilla(personajes.get(k).getX(), personajes.get(k).getY());
                                        personajes.get(k).setX(moverX);
                                        personajes.get(k).setY(moverY);
                                        personajes.get(k).setDesplazamientoPorTurno(personajes.get(k).getDesplazamientoPorTurno() - 1);
                                        respaldoCasilla = cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()];
                                        cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getX(), personajes.get(k).getY(), personajes.get(k));
                                        cuadradosGLogico[moverX][moverY].setBorder(BorderFactory.createLineBorder(Color.RED));
                                        arqueroEncaramado = true;
                                    }
                                    else {
                                        JOptionPane.showMessageDialog(null,"El Arquero no puede pasar de obstáculo a obstáculo directamente.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "El Arquero aún no posee la habilidad de escalar obstáculos.");
                                }
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Acción inválida. El Arquero solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "El Arquero ya no tiene movimientos restantes durante este turno.");
                            arqueroSeleccionado = false;
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Debe seleccionar un personaje para realizar una acción.");
                    }
                }
            }
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
                    if (personajes.get(k).getDesplazamientoPorTurno()>0) {
                        actualX = personajes.get(k).getX();
                        actualY = personajes.get(k).getY();
                        if (isValidMove(actualX, actualY, moverX, moverY)) {
                            cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = new Casilla(personajes.get(k).getX(), personajes.get(k).getY());
                            personajes.get(k).setX(moverX);
                            personajes.get(k).setY(moverY);
                            personajes.get(k).setDesplazamientoPorTurno(personajes.get(k).getDesplazamientoPorTurno()-1);
                            cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = new JugadorCasilla(personajes.get(k).getX(), personajes.get(k).getY(), personajes.get(k));
                            cuadradosGLogico[moverX][moverY].setBorder(BorderFactory.createLineBorder(Color.RED));
                        } else {
                            JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "El guerrero ya no tiene movimientos restantes durante este turno.");
                        guerreroSeleccionado = false;
                    }
                }
            }
            else if (personajes.get(k) instanceof Arquero){
                if (arqueroSeleccionado){
                    if (personajes.get(k).getDesplazamientoPorTurno()>0) {
                        actualX = personajes.get(k).getX();
                        actualY = personajes.get(k).getY();
                        if (isValidMove(actualX, actualY, moverX, moverY)) {
                            if (arqueroEncaramado){
                                cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = respaldoCasilla;
                                personajes.get(k).setX(moverX);
                                personajes.get(k).setY(moverY);
                                arqueroEncaramado = false;
                                respaldoCasilla = null;
                            }
                            else {
                                cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = new Casilla(personajes.get(k).getX(), personajes.get(k).getY());
                                personajes.get(k).setX(moverX);
                                personajes.get(k).setY(moverY);
                            }
                            cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getX(), personajes.get(k).getY(), personajes.get(k));
                            personajes.get(k).setDesplazamientoPorTurno(personajes.get(k).getDesplazamientoPorTurno() - 1);
                            cuadradosGLogico[moverX][moverY].setBorder(BorderFactory.createLineBorder(Color.RED));
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "El Arquero ya no tiene movimientos restantes durante este turno.");
                        arqueroSeleccionado = false;
                    }
                }
            }
            else {
                if (agenteSeleccionado) {
                    if (personajes.get(k).getDesplazamientoPorTurno() > 0) {
                        actualX = personajes.get(k).getX();
                        actualY = personajes.get(k).getY();
                        if (isValidMove(actualX, actualY, moverX, moverY)) {
                            cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = new Casilla(personajes.get(k).getX(), personajes.get(k).getY());
                            personajes.get(k).setX(moverX);
                            personajes.get(k).setY(moverY);
                            personajes.get(k).setDesplazamientoPorTurno(personajes.get(k).getDesplazamientoPorTurno() - 1);
                            cuadradosGLogico[personajes.get(k).getX()][personajes.get(k).getY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getX(), personajes.get(k).getY(), personajes.get(k));
                            cuadradosGLogico[moverX][moverY].setBorder(BorderFactory.createLineBorder(Color.RED));
                        } else {
                            JOptionPane.showMessageDialog(null, "Acción inválida. Cada personaje solo se puede mover 1 casilla a la vez alrededor de sí mismo.");
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "El agente ya no tiene movimientos restantes durante este turno.");
                        agenteSeleccionado = false;
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

        private JTextArea registroResultados = new JTextArea();
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
            scrollResultado.getViewport().add(registroResultados);
            registroResultados.setVisible(true);
            registroResultados.setEditable(false);
            registroResultados.setFont(new Font("HelveticaNeue-Bold", 10,12));

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

