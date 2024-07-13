package src.modelo;

import java.io.Serializable;

public final class Carta implements ifCarta, Serializable {

    private final int numero;
    private final Palo palo;
    private static final int COMODIN = -1;
    public Carta(int num, Palo palo) {
        numero = num;
        this.palo = palo;
    }

    //SETTERS Y GETTERS
    @Override
    public Palo getPalo() {
        return palo;
    }

    @Override
    public int getNumero() {
        return numero;
    }

}