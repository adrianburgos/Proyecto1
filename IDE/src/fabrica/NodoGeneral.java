package fabrica;

import ide.Const;

public class NodoGeneral{
    
    public static Nodo crearCalcular(Nodo expresion)
    {
        Nodo calcular = new Nodo(Const.valor);
        calcular.hijos.add(expresion);
        return (Nodo) calcular;
    }
}
