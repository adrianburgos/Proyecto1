package fabrica;

import ide.Const;

public class NodoGeneral{
    
    public static Nodo crearCuerpo(Nodo nodo)
    {
        Nodo lcuerpo = new Nodo(Const.lcuerpo);
        lcuerpo.hijos.add(nodo);
        return (Nodo) lcuerpo;
    }
    
    public static Nodo crearDecFun(Nodo nodo)
    {
        Nodo decfun = new Nodo(Const.decfun);
        decfun.hijos.add(nodo);
        return (Nodo) decfun;
    }
}
