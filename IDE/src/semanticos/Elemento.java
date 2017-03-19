package semanticos;

import java.util.LinkedList;

public class Elemento {
    public String nombre;
    public String valor;
    public int tipo;
    public int pos;//posicion dentro de la pila
    public LinkedList<Elemento> objeto = null;

    public Elemento(String nombre, int tipo, String valor)
    {
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
        this.pos = -1;
        objeto = null;
    }
}
