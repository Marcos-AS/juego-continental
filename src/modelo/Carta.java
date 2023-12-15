package src.modelo;

public class Carta {

    private int numero;  
    private Palo palo;
    private String rutaImagen;
    private static final int COMODIN = -1; //este valor para que al ordenar cartas queden los comodines primero

    public Carta(int num, Palo palo) {
        this.numero = num;
        this.palo = palo;
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

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}