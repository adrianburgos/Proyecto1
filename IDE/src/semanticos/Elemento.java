package semanticos;

import java.util.LinkedList;

public class Elemento {
    public String nombre;
    public String valor;
    public String tipoAls;
    public int tipo;
    public int pos;//posicion dentro de la pila
    public Ambito objeto = null;

    public Elemento(String nombre, int tipo, String valor)
    {
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
        this.pos = -1;
        objeto = null;
        tipoAls = "";
    }
}
