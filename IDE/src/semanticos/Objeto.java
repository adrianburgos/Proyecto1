package semanticos;

import java.util.LinkedList;

public class Objeto {
    public int tipo;
    public String valor;
    public LinkedList<Integer> dim;

    public Objeto(int tipo, String valor)
    {
        this.tipo = tipo;
        this.valor = valor;
        dim = new LinkedList<>();
    }

    public Objeto()
    {
        this.tipo = 0;
        this.valor = "";
        dim = new LinkedList<>();
    }
}
