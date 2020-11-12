package Código;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Tablero extends JFrame {

    protected JButton botonTurno = new JButton("Terminar Turno");

    private final Casilla[][] cuadradosGLogico = new Casilla[12][12];
    private final JButton[][] botonesGraficos = new JButton[12][12];
    private final JInternalFrame panelContenedor = new JInternalFrame();

    private final ArrayList<Personaje> personajes = new ArrayList<Personaje>();
    private final ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private final ArrayList<SpawningPoint> spawningPoints = new ArrayList<SpawningPoint>();

    private final ClickJugadorHandler clickJugadorHandler = new ClickJugadorHandler();
    private final PanelStats statsPanel = new PanelStats();

    private int turno = 0;
    private boolean guerreroSeleccionado = false;
    private boolean arqueroSeleccionado = false;
    private boolean agenteSeleccionado = false;
    private boolean arqueroEncaramado = false;
    private boolean obstaculoEnMedio = false;
    private Casilla respaldoCasilla;

    public Tablero() throws InterruptedException {
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
        panelContenedor.setEnabled(true);// (2,5) y (4,4)     (4,5)
        /*int distancia1 = obtenerDistancia(4,5,2,5);
        int distancia2 = obtenerDistancia(4,5,4,4);
        System.out.println(distancia1);
        System.out.println(distancia2);
        System.out.println(distancia1-distancia2);*/

        CrearMapaNuevo();
        panelContenedor.setVisible(true);
        panelContenedor.setClosable(false);

        ActualizarTablero();
        statsPanel.actualizarPaneles();

        add(panelContenedor);
        add(statsPanel);
        turnoJugador();
    }
    private void turnoJugador() throws InterruptedException {
        turno++;
        if (turno>20){
            botonTurno.setEnabled(false);
            JOptionPane.showMessageDialog(null, "¡¡¡ FELICIDADES, HAS SOBREVIVIDO A 20 OLEADAS !!!");
            for (int f=0;f<12;f++) {
                for (int c = 0; c < 12; c++) {
                    cuadradosGLogico[f][c].setEnabled(false);
                    botonesGraficos[f][c].setEnabled(false);
                }
            }
            // habilitar boton reinicio juego
            statsPanel.registroResultados.append("¡¡¡ FELICIDADES, HAS SOBREVIVIDO A 20 OLEADAS !!!");
        }
        else {

            if (!personajes.isEmpty()) {
                botonTurno.setEnabled(true);
                if (turno % 2 == 0) {
                    nuevoSpawn();
                }
                accionZombies();
                activarSpawningP();
                reiniciarRuido();
            }
            if (!verificarBase()){
                JOptionPane.showMessageDialog(null, "¡¡¡ NOOO UN ZOMBIE HA PENETRADO DENTRO DE LA BASEE !!!");
                for (int f=0;f<12;f++) {
                    for (int c = 0; c < 12; c++) {
                        cuadradosGLogico[f][c].setEnabled(false);
                        botonesGraficos[f][c].setEnabled(false);
                    }
                }
                // habilitar boton reinicio juego
                statsPanel.registroResultados.append("¡¡¡ NOOO UN ZOMBIE HA PENETRADO DENTRO DE LA BASEE !!!");
                botonTurno.setEnabled(false);
            }

        }
        statsPanel.registroResultados.append("Ha iniciado el turno "+turno+".\n");
        for (Personaje personaje : personajes) {
            personaje.resetTurno();
        }
        statsPanel.actualizarPaneles();
    }

    private boolean verificarBase() {
        for (int i=0;i<12;i++){
            for (int j=0;j<12;j++){
                if (cuadradosGLogico[i][j] instanceof Base){
                    panelContenedor.setBounds(300, -30, 925, 726);
                    return true;
                }
            }
        }
        panelContenedor.setBounds(300, -30, 925, 726);
        return false;
    }

    private void nuevoSpawn() {
        int numAleatorioX;
        int numAleatorioY;
        boolean bandCiclo;
        bandCiclo = true;
        while (bandCiclo) {
            numAleatorioX = (int) Math.floor(Math.random() * (12));
            numAleatorioY = (int) Math.floor(Math.random() * (12));
            if (validarCasillaHab(numAleatorioX, numAleatorioY)) {
                cuadradosGLogico[numAleatorioX][numAleatorioY] = new SpawningPoint(numAleatorioX, numAleatorioY);
                spawningPoints.add(new SpawningPoint(numAleatorioX, numAleatorioY));
                bandCiclo=false;
            }
        }
    }

    private void reiniciarRuido() {
        for (Personaje personaje : personajes) {
            personaje.setRuidoActivo(0);
        }
    }
    private int obtenerDistancia(int punto1X, int punto1Y, int punto2X, int punto2Y){
        int dist;
        dist = (int) Math.pow(Math.pow(punto2X-punto1X,2)+Math.pow(punto2Y-punto1Y, 2), 0.5);
        return dist;
    }
    private void accionZombies() throws InterruptedException {
        int indicePersonaje;
        int danoRecibido;
        int indiceBaseX;
        int indiceBaseY;
        for (int z=0;z<zombies.size();z++){
            indicePersonaje = PersonajeRangoAtaqueZ(zombies.get(z).getPosX(), zombies.get(z).getPosY(),zombies.get(z).getRangoAtaque());
            if (indicePersonaje>=0){
                danoRecibido = personajes.get(indicePersonaje).getSalud();
                if (personajes.get(indicePersonaje)instanceof Guerrero){
                    if (((Guerrero) personajes.get(indicePersonaje)).isEvadirAtaque()){
                        if (((Guerrero) personajes.get(indicePersonaje)).evadir())
                            JOptionPane.showMessageDialog(null, "El guerrero ha evadido el ataque de un zombie gracias a su habilidad!");
                        else if (((Guerrero) personajes.get(indicePersonaje)).isHabAmortiguar()) {
                            personajes.get(indicePersonaje).RecibirDano(zombies.get(z).getAtaque() - zombies.get(z).getAtaque() * 20 / 100);
                            registrarAtaqueZombie(zombies.get(z), personajes.get(indicePersonaje), danoRecibido - personajes.get(indicePersonaje).getSalud());
                        }
                        else {
                            personajes.get(indicePersonaje).RecibirDano(zombies.get(z).getAtaque());
                            registrarAtaqueZombie(zombies.get(z), personajes.get(indicePersonaje), danoRecibido - personajes.get(indicePersonaje).getSalud());
                        }

                    }
                    else if (((Guerrero) personajes.get(indicePersonaje)).isHabAmortiguar()){
                        personajes.get(indicePersonaje).RecibirDano(zombies.get(z).getAtaque() - zombies.get(z).getAtaque() * 20 / 100);
                        registrarAtaqueZombie(zombies.get(z), personajes.get(indicePersonaje), danoRecibido - personajes.get(indicePersonaje).getSalud());
                    }
                    else {
                        personajes.get(indicePersonaje).RecibirDano(zombies.get(z).getAtaque());
                        registrarAtaqueZombie(zombies.get(z), personajes.get(indicePersonaje), danoRecibido - personajes.get(indicePersonaje).getSalud());
                    }
                }
                else {
                    personajes.get(indicePersonaje).RecibirDano(zombies.get(z).getAtaque());
                    registrarAtaqueZombie(zombies.get(z), personajes.get(indicePersonaje), danoRecibido - personajes.get(indicePersonaje).getSalud());
                }
                verificarMuertePersonaje();
            }
            else {
                indicePersonaje = PersonajeRangoVisionZ(zombies.get(z).getPosX(), zombies.get(z).getPosY(), zombies.get(z).getRangoVision());
                if (indicePersonaje >= 0) {
                    desplazarHaciaPosicion(z, personajes.get(indicePersonaje).getPosX(), personajes.get(indicePersonaje).getPosY());
                }
                else {
                    indicePersonaje = ruidoMasAlto();
                    if (indicePersonaje>=0){
                        desplazarHaciaPosicion(z, personajes.get(indicePersonaje).getPosX(), personajes.get(indicePersonaje).getPosY());
                    }
                    else {
                        indiceBaseX = ubicarBaseX();
                        indiceBaseY = ubicarBaseY();
                        desplazarHaciaPosicion(z, indiceBaseX, indiceBaseY);
                    }
                }
            }
            ActualizarTablero();
        }
    }

    private int ubicarBaseY() {
        for (int i=0;i<12;i++){
            for (int j=0;j<12;j++){
                if (cuadradosGLogico[i][j] instanceof Base)
                    return j;
            }
        }
        return -1;
    }
    private int ubicarBaseX() {
        for (int i=0;i<12;i++){
            for (int j=0;j<12;j++){
                if (cuadradosGLogico[i][j] instanceof Base)
                    return i;
            }
        }
        return -1;
    }
    private int ruidoMasAlto() {
        int ruido=0;
        int indicePersonaje=-1;
        for (int i=0;i<personajes.size();i++){
            if (personajes.get(i).getRuidoActivo()>ruido){
                ruido = personajes.get(i).getRuidoActivo();
                indicePersonaje=i;
            }
        }
        return indicePersonaje;
    }
    private void verificarMuertePersonaje() {
        int indiceEliminar=-1;
        for (int p=0;p<personajes.size();p++){
            if (personajes.get(p).getSalud()<=0){
                if (personajes.get(p) instanceof Guerrero) {
                    statsPanel.registroResultados.append("El Guerrero ha muerto a mano de los zombies.\n");
                    if (!personajes.isEmpty())
                        JOptionPane.showMessageDialog(null, "El Guerrero ha muerto a mano de los zombies.");
                }
                else if (personajes.get(p) instanceof Arquero) {
                    statsPanel.registroResultados.append("El Arquero ha muerto a mano de los zombies.\n");
                    if (!personajes.isEmpty())
                        JOptionPane.showMessageDialog(null, "El Arquero ha muerto a mano de los zombies.");
                }
                else {
                    statsPanel.registroResultados.append("El Agente ha muerto a mano de los zombies.\n");
                    if (!personajes.isEmpty())
                        JOptionPane.showMessageDialog(null, "El Agente ha muerto a mano de los zombies.");
                }
                cuadradosGLogico[personajes.get(p).getPosX()][personajes.get(p).getPosY()] = new Casilla(personajes.get(p).getPosX(), personajes.get(p).getPosY());
                indiceEliminar = p;
            }
        }
        if (indiceEliminar>=0)
            personajes.remove(indiceEliminar);
        if (personajes.isEmpty()){
            statsPanel.registroResultados.append("¡¡¡NOO, han muerto todos tus personajes!!!");
            // habilitar reinicio de juego
            JOptionPane.showMessageDialog(null, "¡¡¡NOO, han muerto todos tus personajes!!!");
            for (int f=0;f<12;f++) {
                for (int c = 0; c < 12; c++) {
                    cuadradosGLogico[f][c].setEnabled(false);
                    botonesGraficos[f][c].setEnabled(false);
                }
            }
            botonTurno.setEnabled(false);
        }
        ActualizarTablero();
    }
    private void desplazarHaciaPosicion(int z, int posX1, int posY1) {
        int direccion;
        int posZombie;
        cuadradosGLogico[zombies.get(z).getPosX()][zombies.get(z).getPosY()] = new Casilla(zombies.get(z).getPosX(), zombies.get(z).getPosY());
        direccion = obtenerDireccion(zombies.get(z).getPosX(), zombies.get(z).getPosY(), posX1, posY1);
        posZombie = validarPosCasilla(zombies.get(z).getPosX(),zombies.get(z).getPosY());
        switch (direccion){
            case 1:
                switch (posZombie) {
                    // 1 Oeste, 2 Este, 3 Norte, 4 Sur, 5 NorOeste, 6 NorEste, 7 SurOeste, 8 SurEste
                    // 1 superior izquierda. 2 superior derecha. 3 inferior izquierda. 4 inferior derecha. 5 borde superior, 6 borde inferior, 7 borde izquierdo, 8 borde derecho, 9 centro.
                    case 1:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        }
                        break;
                    case 2:
                    case 5:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oeste
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // SurOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        }
                        break;
                    case 3:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        break;
                    case 4:
                    case 6:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oeste
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // NorOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        break;
                    case 8:
                    case 9: // QUEDE EN VALIDAR TODAS LAS POSICIONES DE LOS ZOMBIES PARA QUE NO SE SALGA DEL INDICE
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oeste
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // NorOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // SurOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        } else break;
                    break;
                }
                break;
            case 2:
                switch (posZombie) {
                    case 1:
                    case 5:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // SurEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        }
                        break;
                    case 2:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        }
                        break;
                    case 3:
                    case 6:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // NorEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        break;
                    case 4:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        break;
                    case 8:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        break;
                    case 7:
                    case 9:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // NorEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // SurEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        } else break;
                        break;
                }
                break;
            case 3:
                switch (posZombie) {
                    case 1:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        break;
                    case 2:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oste
                        }
                        break;
                    case 3:
                    case 7:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // NorEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        break;
                    case 4:
                    case 8:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // NorOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oste
                        }
                        break;
                    case 5:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oste
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        break;
                    case 6:
                    case 9:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // NorOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // NorEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oste
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        } else break;
                        break;
                }
                break;
            case 4:
                switch (posZombie) {
                    case 1:
                    case 7:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // SurEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        break;
                    case 2:
                    case 8:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        }
                        else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // SurOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oste
                        }
                        break;
                    case 3:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        break;
                    case 4:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oste
                        }
                        break;
                    case 6:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oste
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        break;
                    case 5:
                    case 9:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // SurEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // SurOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oste
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        } else break;
                        break;
                }
                break;
            case 5:
                switch (posZombie) {
                    case 2:
                    case 5:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oeste
                        }
                        break;
                    case 3:
                    case 7:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        break;
                    case 8:
                    case 6:
                    case 4:
                    case 9:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // NorOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oeste
                        } else break;
                        break;
                    default: break;
                    }
                    break;
            case 6:
                switch (posZombie) {
                    case 1:
                    case 5:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        break;
                    case 4:
                    case 8:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        }
                        break;
                    case 7:
                    case 6:
                    case 3:
                    case 9:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // NorEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() - 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() - 1); // Norte
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        } else break;
                        break;
                    default:break;
                }
                break;
            case 7:
                switch (posZombie) {
                    case 1:
                    case 7:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        }
                        break;
                    case 4:
                    case 6:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oeste
                        }
                        break;
                    case 8:
                    case 5:
                    case 2:
                    case 9:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // SurOeste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() - 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() - 1); // Oeste
                        } else break;
                        break;
                }
                break;
            default:
                switch (posZombie) {
                    case 2:
                    case 8:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        }
                        break;
                    case 3:
                    case 6:
                        if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        }
                        break;
                    case 7:
                    case 5:
                    case 9:
                    case 1:
                        if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // SurEste
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1);
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX() + 1, zombies.get(z).getPosY())) {
                            zombies.get(z).setPosX(zombies.get(z).getPosX() + 1); // Sur
                        } else if (validarCasillaHabZom(zombies.get(z).getPosX(), zombies.get(z).getPosY() + 1)) {
                            zombies.get(z).setPosY(zombies.get(z).getPosY() + 1); // Este
                        } else break;
                        break;
                }
                break;
        }
        actualizarZombies();
    }
    private int obtenerDireccion(int actualX, int actualY, int destinoX, int destinoY) {
        // 1 Oeste, 2 Este, 3 Norte, 4 Sur, 5 NorOeste, 6 NorEste, 7 SurOeste, 8 SurEste
        if (actualX==destinoX && actualY>destinoY)
            return 1;
        else if (actualX==destinoX && actualY<destinoY)
            return 2;
        else if (actualY==destinoY && actualX>destinoX)
            return 3;
        else if (actualY==destinoY && actualX<destinoX)
            return 4;
        else if (actualY>destinoY && actualX>destinoX)
            return 5;
        else if (actualY<destinoY && actualX>destinoX)
            return 6;
        else if (actualY>destinoY)
            return 7;
        else
            return 8;
    }
    private int PersonajeRangoVisionZ(int actualX, int actualY, int rango){
        int distancia;
        int distanciaMenor = rango+1;
        int indicePersonajeCercano=-1;
        for (int p=0;p<personajes.size();p++){
            if (Math.abs(actualX-personajes.get(p).getPosX()) <= rango && Math.abs(actualY-personajes.get(p).getPosY()) <= rango){
                distancia = obtenerDistancia(actualX, actualY, personajes.get(p).getPosX(), personajes.get(p).getPosY());
                if (distancia<distanciaMenor) {
                    distanciaMenor = distancia;
                    indicePersonajeCercano=p;
                }
            }
        }
        return indicePersonajeCercano;
    }
    private void registrarAtaqueZombie(Zombie zombie, Personaje personaje, int dano){
        if (zombie instanceof ZombieBasico){
            if (personaje instanceof Guerrero)
                statsPanel.registroResultados.append("Un zombie básico ha atacado al Guerrero y ha recibido "+dano+" de daño.\n");
            else if (personaje instanceof Arquero)
                statsPanel.registroResultados.append("Un zombie básico ha atacado al Arquero y ha recibido "+dano+" de daño.\n");
            else
                statsPanel.registroResultados.append("Un zombie básico ha atacado al Agente y ha recibido "+dano+" de daño.\n");
        }
        else if (zombie instanceof ZombieCorredor){
            if (personaje instanceof Guerrero)
                statsPanel.registroResultados.append("Un zombie Corredor ha atacado al Guerrero y ha recibido "+dano+" de daño.\n");
            else if (personaje instanceof Arquero)
                statsPanel.registroResultados.append("Un zombie Corredor ha atacado al Arquero y ha recibido "+dano+" de daño.\n");
            else
                statsPanel.registroResultados.append("Un zombie Corredor ha atacado al Agente y ha recibido "+dano+" de daño.\n");
        }
        else {
            if (personaje instanceof Guerrero)
                statsPanel.registroResultados.append("Un zombie Samurai ha atacado al Guerrero y ha recibido "+dano+" de daño.\n");
            else if (personaje instanceof Arquero)
                statsPanel.registroResultados.append("Un zombie Samurai ha atacado al Arquero y ha recibido "+dano+" de daño.\n");
            else
                statsPanel.registroResultados.append("Un zombie Samurai ha atacado al Agente y ha recibido "+dano+" de daño.\n");
        }
    }
    private int validarPosCasilla(int posSpawnX, int posSpawnY){
        // 1 superior izquierda. 2 superior derecha. 3 inferior izquierda. 4 inferior derecha. 5 borde superior, 6 borde inferior, 7 borde izquierdo, 8 borde derecho, 9 centro.
        if (posSpawnX==0 && posSpawnY==0)
            return 1;
        else if (posSpawnX==0 && posSpawnY==11)
            return 2;
        else if (posSpawnX==11 && posSpawnY==0)
            return 3;
        else if (posSpawnX==11 && posSpawnY==11)
            return 4;
        else if (posSpawnX==0 && posSpawnY<11 && posSpawnY >0)
            return 5;
        else if (posSpawnX==11 && posSpawnY<11 && posSpawnY >0)
            return 6;
        else if (posSpawnY==0 && posSpawnX<11 && posSpawnX >0)
            return 7;
        else if (posSpawnY==11 && posSpawnX<11 && posSpawnX >0)
            return 8;
        else
            return 9;
    }
    private boolean validarCasillaHab(int posX, int posY){
        if (cuadradosGLogico[posX][posY] instanceof JugadorCasilla || cuadradosGLogico[posX][posY] instanceof ZombieCasilla || cuadradosGLogico[posX][posY] instanceof ItemCasilla ||cuadradosGLogico[posX][posY] instanceof SpawningPoint || cuadradosGLogico[posX][posY] instanceof Base || cuadradosGLogico[posX][posY] instanceof Montaña || cuadradosGLogico[posX][posY] instanceof Escombro){
            return false;
        }
        else
            return true;
    }
    private boolean validarCasillaHabZom(int posX, int posY){
        if (cuadradosGLogico[posX][posY] instanceof JugadorCasilla || cuadradosGLogico[posX][posY] instanceof ZombieCasilla || cuadradosGLogico[posX][posY] instanceof ItemCasilla ||cuadradosGLogico[posX][posY] instanceof SpawningPoint || cuadradosGLogico[posX][posY] instanceof Montaña || cuadradosGLogico[posX][posY] instanceof Escombro){
            return false;
        }
        else
            return true;
    }
    private Zombie spawnZombieAleatorio(int posZomX, int posZomY){
        int numRandom = (int) Math.floor(Math.random() * (100));
        if (numRandom%3==0)
            return new ZombieBasico(posZomX, posZomY);
        else if (numRandom%3==1)
            return new ZombieCorredor(posZomX, posZomY);
        else
            return new ZombieSamurai(posZomX, posZomY);
    }
    private void  actualizarZombies(){
        for (int z=0;z<zombies.size();z++){
            cuadradosGLogico[zombies.get(z).getPosX()][zombies.get(z).getPosY()] = new ZombieCasilla(zombies.get(z).getPosX(), zombies.get(z).getPosY(), zombies.get(z));
        }
        ActualizarTablero();
    }
    private void activarSpawningP(){
        int posSpawnX;
        int numOpcion;
        int posSpawnY;
        //Zombie zombieNuevo;
        for (int s=0;s<spawningPoints.size();s++) {
            posSpawnX = spawningPoints.get(s).getPosX();
            posSpawnY = spawningPoints.get(s).getPosY();
            if (isSpawining()) {
                numOpcion = validarPosCasilla(posSpawnX, posSpawnY);
                // 1 superior izquierda. 2 superior derecha. 3 inferior izquierda. 4 inferior derecha. 5 borde superior, 6 borde inferior, 7 borde izquierdo, 8 borde derecho, 9 centro.
                switch (numOpcion) {
                    case 1 -> {
                        if (validarCasillaHab(posSpawnX, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY));
                        else
                            break;
                        actualizarZombies();
                    }
                    case 2 -> {
                        if (validarCasillaHab(posSpawnX, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY));
                        else
                            break;
                        actualizarZombies();
                    }
                    case 3 -> {
                        if (validarCasillaHab(posSpawnX - 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY + 1));
                        else
                            break;
                        actualizarZombies();
                    }
                    case 4 -> {
                        if (validarCasillaHab(posSpawnX - 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY - 1));
                        else
                            break;
                        actualizarZombies();
                    }
                    case 5 -> {
                        if (validarCasillaHab(posSpawnX, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY + 1));
                        else
                            break;
                        actualizarZombies();
                    }
                    case 6 -> {
                        if (validarCasillaHab(posSpawnX, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY + 1));
                        else
                            break;
                        actualizarZombies();
                    }
                    case 7 -> {
                        if (validarCasillaHab(posSpawnX + 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY));
                        else break;
                        actualizarZombies();
                    }
                    case 8 -> {
                        if (validarCasillaHab(posSpawnX + 1, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY));
                        else if (validarCasillaHab(posSpawnX, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY));
                        else break;
                        actualizarZombies();
                    }
                    default -> {
                        if (validarCasillaHab(posSpawnX, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY - 1));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY));
                        else if (validarCasillaHab(posSpawnX + 1, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX + 1, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY + 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY + 1));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY));
                        else if (validarCasillaHab(posSpawnX - 1, posSpawnY - 1))
                            zombies.add(spawnZombieAleatorio(posSpawnX - 1, posSpawnY - 1));
                        else break;
                        actualizarZombies();
                    }
                }
            }
        }
    }
    private boolean isSpawining(){
        int numAleatorio = (int) Math.floor(Math.random() * (100));
        if (numAleatorio%3==0){
            return false;
        }
        return true;
    }
    private void ActualizarTablero(){
        for (int i=0;i<12;i++){
            for (int j=0;j<12;j++){
                botonesGraficos[i][j].setIcon(cuadradosGLogico[i][j].getIcon());
                botonesGraficos[i][j].setBorder(cuadradosGLogico[i][j].getBorder());
            }
        }
        panelContenedor.setBounds(300, -30, 925, 726);
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
                botonesGraficos[i][j].addActionListener(clickJugadorHandler);
            }
        }
        AnadirPersonajes();
        AnadirObstaculos();
    }
    private void AnadirObstaculos(){
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
    private void AnadirPersonajes(){
        if (!personajes.isEmpty()){
            personajes.clear();
        }
        Guerrero guerrero = new Guerrero(11, 2);
        Arquero arquero = new Arquero(10, 1);
        Agente agente = new Agente(9,0);
        personajes.add(guerrero);
        personajes.add(arquero);
        personajes.add(agente);
        for (int i=0; i<personajes.size(); i++){
            cuadradosGLogico[personajes.get(i).getPosX()][personajes.get(i).getPosY()] = new JugadorCasilla(personajes.get(i).getPosX(),personajes.get(i).getPosY(), personajes.get(i));
        }
    }
    private class ClickJugadorHandler implements ActionListener {

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
                            actualX = personajes.get(i).getPosX();
                            actualY = personajes.get(i).getPosY();
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
                                cuadradosGLogico[personajes.get(i).getPosX()][personajes.get(i).getPosY()] = new Casilla(personajes.get(i).getPosX(), personajes.get(i).getPosY());
                                personajes.get(i).setPosX(posItemX);
                                personajes.get(i).setPosY(posItemY);
                                personajes.get(i).setDesplazamientoPorTurno(personajes.get(i).getDesplazamientoPorTurno()-1);
                                cuadradosGLogico[personajes.get(i).getPosX()][personajes.get(i).getPosY()] = new JugadorCasilla(personajes.get(i).getPosX(), personajes.get(i).getPosY(), personajes.get(i));
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
                            actualX = personajes.get(i).getPosX();
                            actualY = personajes.get(i).getPosY();
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
                                    cuadradosGLogico[personajes.get(i).getPosX()][personajes.get(i).getPosY()] = respaldoCasilla;
                                    personajes.get(i).setPosX(posItemX);
                                    personajes.get(i).setPosY(posItemY);
                                    arqueroEncaramado = false;
                                    respaldoCasilla = null;
                                }
                                else {
                                    cuadradosGLogico[personajes.get(i).getPosX()][personajes.get(i).getPosY()] = new Casilla(personajes.get(i).getPosX(), personajes.get(i).getPosY());
                                    personajes.get(i).setPosX(posItemX);
                                    personajes.get(i).setPosY(posItemY);
                                }
                                cuadradosGLogico[personajes.get(i).getPosX()][personajes.get(i).getPosY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(i).getPosX(), personajes.get(i).getPosY(), personajes.get(i));
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
                else if(agenteSeleccionado){
                    if (personajes.get(i) instanceof Agente) {
                        if (personajes.get(i).getDesplazamientoPorTurno()>0){
                            actualX = personajes.get(i).getPosX();
                            actualY = personajes.get(i).getPosY();
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
                                cuadradosGLogico[personajes.get(i).getPosX()][personajes.get(i).getPosY()] = new Casilla(personajes.get(i).getPosX(), personajes.get(i).getPosY());
                                personajes.get(i).setPosX(posItemX);
                                personajes.get(i).setPosY(posItemY);
                                personajes.get(i).setDesplazamientoPorTurno(personajes.get(i).getDesplazamientoPorTurno()-1);
                                cuadradosGLogico[personajes.get(i).getPosX()][personajes.get(i).getPosY()] = new JugadorCasilla(personajes.get(i).getPosX(), personajes.get(i).getPosY(), personajes.get(i));
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
    private int PersonajeRangoAtaqueZ(int actualX, int actualY, int rango){
        int distancia;
        for (int p=0;p<personajes.size();p++){
            if (Math.abs(actualX-personajes.get(p).getPosX()) <= rango && actualY==personajes.get(p).getPosY()){
                distancia = Math.abs(actualX-personajes.get(p).getPosX());
                for (int i=1;i<distancia;i++){
                    if (cuadradosGLogico[actualX+i][actualY] instanceof Montaña || cuadradosGLogico[actualX+i][actualY] instanceof Escombro || cuadradosGLogico[actualX+i][actualY] instanceof SpawningPoint) {
                        return -1;
                    }
                }
                return p;
            }
            else if (Math.abs(actualY-personajes.get(p).getPosY()) <= rango && actualX==personajes.get(p).getPosX()){
                distancia = Math.abs(actualY-personajes.get(p).getPosY());
                for (int i=1;i<distancia;i++){
                    if (cuadradosGLogico[actualX][actualY+i] instanceof Montaña || cuadradosGLogico[actualX][actualY+i] instanceof Escombro || cuadradosGLogico[actualX][actualY+i] instanceof SpawningPoint) {
                        return -1;
                    }
                }
                return p;
            }
        }
        return -1;
    }
    private boolean isvalidAttack(int actualX,int actualY,int atacarX,int atacarY, int rango){
        int distancia;
        if (Math.abs(actualX-atacarX) <= rango && actualY==atacarY){
            distancia = Math.abs(actualX-atacarX);
            if (actualX<atacarX) {
                for (int i = 1; i < distancia; i++) {
                    if (cuadradosGLogico[actualX + i][actualY] instanceof Montaña || cuadradosGLogico[actualX + i][actualY] instanceof Escombro || cuadradosGLogico[actualX + i][actualY] instanceof SpawningPoint) {
                        obstaculoEnMedio = true;
                        return false;
                    }
                }
            }
            else {
                for (int i = 1; i < distancia; i++) {
                    if (cuadradosGLogico[actualX - i][actualY] instanceof Montaña || cuadradosGLogico[actualX - i][actualY] instanceof Escombro || cuadradosGLogico[actualX - i][actualY] instanceof SpawningPoint) {
                        obstaculoEnMedio = true;
                        return false;
                    }
                }
            }
            return true;
        }
        else if (Math.abs(actualY-atacarY) <= rango && actualX==atacarX){
            distancia = Math.abs(actualY-atacarY);
            if (actualY<atacarY) {
                for (int i = 1; i < distancia; i++) {
                    if (cuadradosGLogico[actualX][actualY + i] instanceof Montaña || cuadradosGLogico[actualX][actualY + i] instanceof Escombro || cuadradosGLogico[actualX][actualY + i] instanceof SpawningPoint) {
                        obstaculoEnMedio = true;
                        return false;
                    }
                }
            }
            else {
                for (int i = 1; i < distancia; i++) {
                    if (cuadradosGLogico[actualX][actualY - i] instanceof Montaña || cuadradosGLogico[actualX][actualY - i] instanceof Escombro || cuadradosGLogico[actualX][actualY - i] instanceof SpawningPoint) {
                        obstaculoEnMedio = true;
                        return false;
                    }
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
                            actualX = personajes.get(i).getPosX();
                            actualY = personajes.get(i).getPosY();
                            if (isvalidAttack(actualX, actualY, atacarX, atacarY, personajes.get(i).getArma().getRango())){
                                personajes.get(i).setAtaquesPorTurno(personajes.get(i).getAtaquesPorTurno()-1);
                                //SIGUE REALIZAR LOS ATAQUES
                                for (int z=0;z<zombies.size();z++){
                                    if (zombies.get(z).getPosX()==atacarX && zombies.get(z).getPosY() == atacarY){
                                        zombies.get(z).recibirDano(personajes.get(i).getArma().getDano());
                                        personajes.get(i).setRuidoActivo(personajes.get(i).getArma().getRuido());
                                        if (zombies.get(z).getSalud()<=0){
                                            //se añade algoritmo de item
                                            cuadradosGLogico[atacarX][atacarY] = dropItem(atacarX, atacarY);
                                            if (zombies.get(z) instanceof ZombieBasico)
                                                personajes.get(i).aumentarNivel(3);
                                            else if (zombies.get(z) instanceof ZombieCorredor)
                                                personajes.get(i).aumentarNivel(5);
                                            else
                                                personajes.get(i).aumentarNivel(7);
                                            personajes.get(i).actualizarNivel();
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
                            actualX = personajes.get(i).getPosX();
                            actualY = personajes.get(i).getPosY();
                            if (isvalidAttack(actualX, actualY, atacarX, atacarY, personajes.get(i).getArma().getRango())){
                                personajes.get(i).setAtaquesPorTurno(personajes.get(i).getAtaquesPorTurno()-1);
                                for (int z=0;z<zombies.size();z++){
                                    if (zombies.get(z).getPosX()==atacarX && zombies.get(z).getPosY() == atacarY){
                                        if (((Arquero) personajes.get(i)).isDanoCritico())
                                            zombies.get(z).recibirDano(personajes.get(i).getArma().getDano()+personajes.get(i).getArma().getDano()*30/100);
                                        else
                                            zombies.get(z).recibirDano(personajes.get(i).getArma().getDano());
                                        if (((Arquero) personajes.get(i)).isMenosRuido())
                                            personajes.get(i).setRuidoActivo(personajes.get(i).getArma().getRuido()-personajes.get(i).getArma().getRuido()*40/100);
                                        else
                                            personajes.get(i).setRuidoActivo(personajes.get(i).getArma().getRuido());
                                        if (zombies.get(z).getSalud()<=0){
                                            //se añade algoritmo de item
                                            cuadradosGLogico[atacarX][atacarY] = dropItem(atacarX, atacarY);
                                            if (zombies.get(z) instanceof ZombieBasico)
                                                personajes.get(i).aumentarNivel(3);
                                            else if (zombies.get(z) instanceof ZombieCorredor)
                                                personajes.get(i).aumentarNivel(5);
                                            else
                                                personajes.get(i).aumentarNivel(7);
                                            zombies.remove(z);
                                            personajes.get(i).actualizarNivel();
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
                            actualX = personajes.get(i).getPosX();
                            actualY = personajes.get(i).getPosY();
                            if (isvalidAttack(actualX, actualY, atacarX, atacarY, personajes.get(i).getArma().getRango())){
                                personajes.get(i).setAtaquesPorTurno(personajes.get(i).getAtaquesPorTurno()-1);
                                for (int z=0;z<zombies.size();z++){
                                    if (zombies.get(z).getPosX()==atacarX && zombies.get(z).getPosY() == atacarY){
                                        zombies.get(z).recibirDano(personajes.get(i).getArma().getDano());
                                        personajes.get(i).setRuidoActivo(personajes.get(i).getArma().getRuido());
                                        if (zombies.get(z).getSalud()<=0){
                                            //se añade algoritmo de item
                                            cuadradosGLogico[atacarX][atacarY] = dropItem(atacarX, atacarY);
                                            if (zombies.get(z) instanceof ZombieBasico)
                                                personajes.get(i).aumentarNivel(3);
                                            else if (zombies.get(z) instanceof ZombieCorredor)
                                                personajes.get(i).aumentarNivel(5);
                                            else
                                                personajes.get(i).aumentarNivel(7);
                                            zombies.remove(z);
                                            personajes.get(i).actualizarNivel();
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

    private ItemCasilla dropItem(int posX, int posY) {
        int numAleatorio = (int) Math.floor(Math.random() * (100));
        if (numAleatorio%3==0)
            return new ItemCasilla(posX, posY, new Bebida());
        else if (numAleatorio%3==1)
            return new ItemCasilla(posX, posY, new Energia());
        else
            return new ItemCasilla(posX, posY, new Insignia());
    }

    private void SpawPSelect(int moverX, int moverY){
        int actualX;
        int actualY;
        if (agenteSeleccionado || guerreroSeleccionado || arqueroSeleccionado){
            for (int k=0;k<personajes.size();k++){
                if (agenteSeleccionado){
                    if (personajes.get(k) instanceof Agente){
                        actualX = personajes.get(k).getPosX();
                        actualY = personajes.get(k).getPosY();
                        if (isValidMove(actualX, actualY, moverX, moverY)){
                            JOptionPane.showMessageDialog(null, "El Agente no puede moverse ni atacar un Spawning Point.");
                        }
                    }
                }
                else if (guerreroSeleccionado){
                    if (personajes.get(k) instanceof Guerrero){
                        actualX = personajes.get(k).getPosX();
                        actualY = personajes.get(k).getPosY();
                        if (isValidMove(actualX, actualY, moverX, moverY)){
                            JOptionPane.showMessageDialog(null, "El Guerrero no puede moverse ni atacar un Spawning Point.");
                        }
                    }
                }
                else {
                    if (personajes.get(k) instanceof Arquero){
                        actualX = personajes.get(k).getPosX();
                        actualY = personajes.get(k).getPosY();
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
                            actualX = personajes.get(k).getPosX();
                            actualY = personajes.get(k).getPosY();
                            if (isValidMove(actualX, actualY, moverX, moverY)) {
                                if (((Arquero) personajes.get(k)).isSubirObstaculos()) {
                                    if (!arqueroEncaramado) {
                                        cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = new Casilla(personajes.get(k).getPosX(), personajes.get(k).getPosY());
                                        personajes.get(k).setPosX(moverX);
                                        personajes.get(k).setPosY(moverY);
                                        personajes.get(k).setDesplazamientoPorTurno(personajes.get(k).getDesplazamientoPorTurno() - 1);
                                        respaldoCasilla = cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()];
                                        cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getPosX(), personajes.get(k).getPosY(), personajes.get(k));
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
                        actualX = personajes.get(k).getPosX();
                        actualY = personajes.get(k).getPosY();
                        if (isValidMove(actualX, actualY, moverX, moverY)) {
                            cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = new Casilla(personajes.get(k).getPosX(), personajes.get(k).getPosY());
                            personajes.get(k).setPosX(moverX);
                            personajes.get(k).setPosY(moverY);
                            personajes.get(k).setDesplazamientoPorTurno(personajes.get(k).getDesplazamientoPorTurno()-1);
                            cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = new JugadorCasilla(personajes.get(k).getPosX(), personajes.get(k).getPosY(), personajes.get(k));
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
                        actualX = personajes.get(k).getPosX();
                        actualY = personajes.get(k).getPosY();
                        if (isValidMove(actualX, actualY, moverX, moverY)) {
                            if (arqueroEncaramado){
                                cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = respaldoCasilla;
                                personajes.get(k).setPosX(moverX);
                                personajes.get(k).setPosY(moverY);
                                arqueroEncaramado = false;
                                respaldoCasilla = null;
                            }
                            else {
                                cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = new Casilla(personajes.get(k).getPosX(), personajes.get(k).getPosY());
                                personajes.get(k).setPosX(moverX);
                                personajes.get(k).setPosY(moverY);
                            }
                            cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getPosX(), personajes.get(k).getPosY(), personajes.get(k));
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
                        actualX = personajes.get(k).getPosX();
                        actualY = personajes.get(k).getPosY();
                        if (isValidMove(actualX, actualY, moverX, moverY)) {
                            cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = new Casilla(personajes.get(k).getPosX(), personajes.get(k).getPosY());
                            personajes.get(k).setPosX(moverX);
                            personajes.get(k).setPosY(moverY);
                            personajes.get(k).setDesplazamientoPorTurno(personajes.get(k).getDesplazamientoPorTurno() - 1);
                            cuadradosGLogico[personajes.get(k).getPosX()][personajes.get(k).getPosY()] = (JugadorCasilla) new JugadorCasilla(personajes.get(k).getPosX(), personajes.get(k).getPosY(), personajes.get(k));
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
                try {
                    turnoJugador();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
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
                if (personajes.get(i) instanceof Guerrero) {
                    saludGuerrero.setText("Salud del guerrero: " + personajes.get(i).getSalud());
                    ataqueGuerrero.setText("Ataque del guerrero: " + personajes.get(i).getArma().getDano());
                    nivelGuerrero.setText("Nivel del guerrero: " + personajes.get(i).getNivel());
                }
                else if (personajes.get(i) instanceof Arquero) {
                    saludArquero.setText("Salud del arquero: " + personajes.get(i).getSalud());
                    ataqueArquero.setText("Ataque del arquero: " + personajes.get(i).getArma().getDano());
                    nivelArquero.setText("Nivel del aruqero: " + personajes.get(i).getNivel());
                }
                else {
                    saludAgente.setText("Salud del agente: " + personajes.get(i).getSalud());
                    ataqueAgente.setText("Ataque del agente: " + personajes.get(i).getArma().getDano());
                    nivelAgente.setText("Nivel del agente: " + personajes.get(i).getNivel());
                }
            }
        }
    }
}

