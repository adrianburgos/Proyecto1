package semanticos;

import ide.Const;
import java.util.LinkedList;

public class Elemento {
    public String nombre;
    public String valor;
    public String tipoAls;
    public String visibilidad;
    public int tipo;
    public int pos;//posicion dentro de la pila
    public Ambito objeto = null;
    public LinkedList<Integer> dim;
    public LinkedList<Objeto> lvalores;

    public Elemento(String nombre, int tipo, String valor)
    {
        this.nombre = nombre;
        this.valor = valor;
        visibilidad = Const.publico;
        this.tipo = tipo;
        this.pos = -1;
        objeto = null;
        tipoAls = "";
        dim = new LinkedList<>();
        lvalores = new LinkedList<>();
    }
}
