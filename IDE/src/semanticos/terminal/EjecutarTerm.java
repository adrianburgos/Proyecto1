package semanticos.terminal;

import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;
import semanticos.Objeto;
import semanticos.Pila;
import semanticos.Semantico;

public class EjecutarTerm {
    public static Nodo raiz;
    public static Objeto porcentaje = new Objeto();
    
    public static void ejecutar(Nodo raizPar)
    {
        raiz = raizPar;
        porcentaje = ejecutarInst(raiz);
        
    }

    public static Objeto ejecutarInst(Nodo raiz) {
        switch(raiz.nombre)
        {
            case Const.mas:
            case Const.menos:
            case Const.por:
            case Const.dividido:
            case Const.pot:
            case Const.numero:
            case Const.caracter:
                return Semantico.ejecutarValor(raiz);
            case Const.mod:
            case Const.sqrt:
                return SemanticoTerm.ejecutarValor(raiz);
            case Const.succ:
                return SemanticoTerm.succ(raiz);
            case Const.decc:
                return SemanticoTerm.decc(raiz);
            case Const.sum:
                return SemanticoTerm.sum(raiz);
            case Const.product:
                return SemanticoTerm.product(raiz);
            case Const.max:
                return SemanticoTerm.max(raiz);
            case Const.length:
                return SemanticoTerm.length(raiz);
            case Const.porcentaje:
                return porcentaje;
            case Const.list:
                Objeto res = SemanticoTerm.decList(raiz);
                PilaHaskell.agregarElemeto(raiz.valor, res);
                return res;
        }
        return new Objeto();
    }
}
