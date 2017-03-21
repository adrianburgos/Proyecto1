package semanticos;

import java.util.LinkedList;

public class Ambito {
    public int padre = -1;
    public int actual = 0;
    public LinkedList<Elemento> elementos = new LinkedList<>();

    public Ambito(int padre, int actual)
    {
        this.padre = padre;
        this.actual = actual;
    }
    
    public Elemento buscar(String nombre)
    {
        for(Elemento ele : elementos)
            if(ele.nombre.equals(nombre))
                return ele;
        return null;
    }
}
