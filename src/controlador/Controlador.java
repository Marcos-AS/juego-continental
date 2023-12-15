package src.controlador;

import java.util.ArrayList;

import src.main.Observable;
import src.main.Observer;
import src.modelo.Carta;
import src.modelo.Palo;
import src.modelo.Partida;
import src.modelo.jugadorActual;


public class Controlador implements Observable{
    private ArrayList<Observer> observadores = new ArrayList<>();
    

//PRIVATE----------------------------------------------------------------------
    private String transformarNumCarta(int numCarta) {
        String num = ((Integer) numCarta).toString();
        if(numCarta <= 1 || numCarta >= 11) { 
            switch(num) {
                case "1":
                    num = "A";
                    break;
                case "11":
                    num = "J";
                    break;
                case "12":
                    num = "Q";
                    break;
                case "13":
                    num = "K";
                    break;
                case "-1":
                    num = "COMODIN";
                    break;
            }
        }
        return num;
    }  

    private ArrayList<String> recorrerMano(ArrayList<Carta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (Carta c : mano) {
    		String numString = transformarNumCarta(c.getNumero());
            Palo palo = c.getPalo();
            String carta = "";
            if (palo == Palo.COMODIN) {
                carta = "COMODIN";
            } else {
                carta = numString + " de " + palo;
            }
            manoString.add(carta);
        }
        return manoString;
    }

//PUBLIC---------------------------------------------------------------------
	public Controlador() {
		//this.miJuego.addObserver(this);
	}

    public ArrayList<String> enviarManoJugador(Partida p, String nombreJugador) {
        ArrayList<Carta> mano = null;
        ArrayList<String> manoString = null;
        try {
            jugadorActual j = p.getJugador(nombreJugador);
            mano = j.getMano();            
        } catch (Exception e) {
            e.printStackTrace();
        }
        manoString = recorrerMano(mano);
        return manoString;
    }

    public ArrayList<String> enviarMazo(Partida p) {
        ArrayList<Carta> mazo = p.getMazo();
        return recorrerMano(mazo);
    }
 
    public ArrayList<ArrayList<String>> enviarJuegosJugador(jugadorActual j) {
        ArrayList<ArrayList<Carta>> juegos = null;
        ArrayList<ArrayList<String>> juegosString = new ArrayList<>();
        juegos = j.getJuegos();
        for (ArrayList<Carta> juego : juegos) {
            juegosString.add(recorrerMano(juego));
        }
        return juegosString;
    }

    public boolean bajarJuego(jugadorActual j, Object[] cartasABajar) {
        boolean puedeBajar = false;
        if (cartasABajar.length >= 6) {
            if(j.bajarJuego(cartasABajar)) {
                puedeBajar = true;
                j.setPuedeBajar();
            }
        }
        return puedeBajar;
    }

    public boolean cortar(Partida p, jugadorActual j) {
        boolean corte = false;
        if (j.comprobarCorte()) {
            if (j.getMano().size()==1) {
                j.tirarAlPozo(0);
            }
            p.incrementarRonda();
            corte = true;
        }
        return corte;
    }

    // public void tratarDeBajarParaCortar(int[] triosYEscalerasQueFaltan) {
    //     while ((triosYEscalerasQueFaltan[0] != 0 && triosYEscalerasQueFaltan[1] != 0) && eleccion != 2) {
    //         bajarJuego(j);
    //         faltaParaCortar = j.comprobarQueFaltaParaCortar();
    //         vista.mostrarLoQueFaltaParaCortar(faltaParaCortar);
    //         eleccion = vista.preguntarSiDeseaContinuar();
    //     }
    //     if (faltaParaCortar[0] == 0 && faltaParaCortar[1] == 0) {
    //         corte = cortar(p, j);
    //     } else {
    //         vista.mostrarNoPuedeCortar();
    //     }     
    //     return corte;
    // }

    public String enviarPrimeraCartaPozo(Partida p) {
        Carta c = p.sacarPrimeraDelPozo();
        String carta = transformarNumCarta(c.getNumero()) + " de " + c.getPalo().name();
        return carta;
    }

    public int transformarLetraCarta(String letraCarta) {
        int numCarta = 0;
        switch(letraCarta) {
            case "J":
                numCarta = 11;
                break;
            case "Q":
                numCarta = 12;
                break;
            case "K":
                numCarta = 13;
                break;
            case "A":
                numCarta = 1;
                break;
            case "COMODIN":
                numCarta = -1;
                break;
        }
        return numCarta;
    } 

    public void cambiarEstadoPartida(){};

    //GETTERS, SETTERS, OBSERVER
    @Override
    public void addObserver(Observer observador) {
        observadores.add(observador);
    }
}