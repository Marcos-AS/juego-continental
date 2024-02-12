package src.modelo;

import java.io.Serializable;

public class Carta implements ifCarta, Serializable {

    private int numero;  
    private Palo palo;
    private String rutaImagen;
    private static final int COMODIN = -1; //este valor para que al ordenar cartas queden los comodines primero

    public Carta(int num, Palo palo) {
        this.numero = num;
        this.palo = palo;
    }

    //SETTERS Y GETTERS
    @Override
    public int getComodin() {
        return COMODIN;
    }

    @Override
    public Palo getPalo() {
        return this.palo;
    }

    @Override
    public int getNumero() {
        return this.numero;
    }

    @Override
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}