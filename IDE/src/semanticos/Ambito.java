package semanticos;

import java.util.LinkedList;

public class Ambito {
    public int padre = -1;
    public int actual = 0;
    public LinkedList<Elemento> elementos = new LinkedList<Elemento>();

    public Ambito(int padre, int actual)
    {
        this.padre = padre;
        this.actual = actual;
    }
}
