package semanticos;

public class Objeto {
    public int tipo;
    public String valor;

    public Objeto(int tipo, String valor)
    {
        this.tipo = tipo;
        this.valor = valor;
    }

    public Objeto()
    {
        this.tipo = 0;
        this.valor = "";
    }
}
