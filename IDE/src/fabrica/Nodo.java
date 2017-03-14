package fabrica;

import ide.Const;
import java.util.LinkedList;

public class Nodo {
    private String nombre;
    private int tipo; //cadena numero booleano
    private String valor;
    private String visibilidad;
    public LinkedList<Nodo> hijos = new LinkedList();
    protected int fila, columna;

    
    
    public Nodo(String nombre, String valor, String visibilidad, int tipo, int fila, int columna) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = visibilidad;
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Coloca la visibilidad como publica
     * @param nombre nombre del nodo
     * @param valor valor del nodo
     * @param tipo tipo de nodo
     * @param fila
     * @param columna 
     */
    public Nodo(String nombre, String valor, int tipo, int fila, int columna) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = Const.publico;
        this.fila = fila;
        this.columna = columna;
    }

    public Nodo(String nombre, String valor, int tipo, String visibilidad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = visibilidad;
        this.fila = 0;
        this.columna = 0;
    }
    
    public Nodo(String nombre, String valor, int tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = Const.publico;
        this.fila = 0;
        this.columna = 0;
    }
    
    public Nodo(String nombre, String valor) {
        this.nombre = nombre;
        this.tipo = -1;
        this.valor = valor;
        this.visibilidad = Const.publico;
        this.fila = 0;
        this.columna = 0;
    }
    
    public Nodo(String nombre) {
        this.nombre = nombre;
        this.tipo = -1;
        this.valor = nombre;
        this.visibilidad = Const.publico;
        this.fila = 0;
        this.columna = 0;
    }

    public Nodo() {
        nombre = "";
        tipo = 0;
        valor = "";
        visibilidad = Const.publico;
        fila = 0;
        columna = 0;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }    
}
