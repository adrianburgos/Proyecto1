package semanticos;

import fabrica.Nodo;

public class CargarFunciones {
    public static Nodo raiz = null;
    public static void cargar(Nodo parRaiz)
    {
        if(raiz == null)
            raiz = (Nodo) parRaiz;
        else
            raiz.hijos.addAll(parRaiz.hijos);
    }
}
