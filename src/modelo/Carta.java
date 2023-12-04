package src.modelo;

public class Carta {

    private int numero;  
    private Palo palo;
    private static final int COMODIN = 50;

    public Carta(int num, Palo palo) {
        this.numero = num;
        this.palo = palo;
    }

    public Carta() {
        this.numero = COMODIN;
    }

    //SETTERS Y GETTERS
    public int getComodin() {
        return COMODIN;
    }

    public Palo getPalo() {
        return this.palo;
    }

    public int getNumero() {
        return this.numero;
    }
}