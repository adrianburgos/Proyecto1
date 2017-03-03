package fabrica;

import ide.Const;
import java.util.LinkedList;

public class Nodo {
    private int nombre;
    private int tipo; //cadena numero booleano
    private String valor;
    private String visibilidad;
    public LinkedList<Nodo> hijos = new LinkedList();
    protected int fila, columna;

    public Nodo(int nombre, int tipo, String valor, String visibilidad, int fila, int columna) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = visibilidad;
        this.fila = fila;
        this.columna = columna;
    }

    public Nodo(int nombre, int tipo, String valor, int fila, int columna) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = Const.publico;
        this.fila = fila;
        this.columna = columna;
    }

    
    
    public Nodo() {
        nombre = 0;
        tipo = 0;
        valor = "";
        visibilidad = Const.publico;
        fila = 0;
        columna = 0;
    }
    
    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
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
