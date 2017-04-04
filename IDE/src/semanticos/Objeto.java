package semanticos;

import java.util.LinkedList;

public class Objeto {
    public int tipo;
    public String valor;
    public String tipoAls;
    public LinkedList<Integer> dim;
    public LinkedList<Objeto> lvalores;
    public Ambito objeto = null;

    public Objeto(int tipo, String valor)
    {
        this.tipo = tipo;
        this.valor = valor;
        this.tipoAls = "";
        dim = new LinkedList<>();
    }

    public Objeto(int tipo, String valor, String tipoAls) {
        this.tipo = tipo;
        this.valor = valor;
        this.tipoAls = tipoAls;
        dim = new LinkedList<>();
        lvalores = new LinkedList<>();
    }

    public Objeto()
    {
        this.tipo = -1;
        this.valor = " ";
        dim = new LinkedList<>();
        lvalores = new LinkedList<>();
    }
}
