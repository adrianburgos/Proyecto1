package semanticos;

public class Elemento {
    public String nombre;
    public String valor;
    public int tipo;
    public int pos;

    public Elemento(String nombre, int tipo, String valor)
    {
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
        this.pos = -1;
    }
}
