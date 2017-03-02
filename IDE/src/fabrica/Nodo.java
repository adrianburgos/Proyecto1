package fabrica;

import ide.Const;
import java.util.LinkedList;

public class Nodo {
    protected String nombre;
    protected String valor;
    protected String visibilidad;
    public LinkedList<Nodo> hijos = new LinkedList();
    protected int fila, columna;
    
    public Nodo (String nombre, String valor, int fila, int columna) {
        this.nombre = nombre;
        this.valor = valor;
        this.visibilidad = Const.publico;
        this.fila = fila;
        this.columna = columna;
    }
    
        public Nodo(int fila, int columna, Nodo op1, Nodo op2) {
        this.nombre = Const.mas;
        this.valor = Const.mas;
        this.visibilidad = Const.publico;
        this.fila = fila;
        this.columna = columna;
        this.hijos.add(op1);
        this.hijos.add(op2);
    }

    public Nodo() {
        nombre = "";
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
