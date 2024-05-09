package src.modelo;

import java.io.Serializable;

public final class Carta implements ifCarta, Serializable {

    private final int numero;
    private final Palo palo;
    private String rutaImagen;
    private static final int COMODIN = -1; //este valor para que al ordenar cartas queden los comodines primero

    public Carta(int num, Palo palo) {
        numero = num;
        this.palo = palo;
    }

    //SETTERS Y GETTERS
    @Override
    public int getComodin() {
        return COMODIN;
    }

    @Override
    public Palo getPalo() {
        return palo;
    }

    @Override
    public int getNumero() {
        return numero;
    }

    @Override
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}