package semanticos.terminal;

import Reportes.ErroresGraphik;
import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;
import semanticos.Elemento;
import semanticos.Objeto;
import semanticos.Pila;
import semanticos.Semantico;
import semanticos.haskell.Haskell;

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
            case Const.id:
                Objeto res = Semantico.ejecutarValor(raiz);
                if(res.tipo == 0 || res.tipo == Const.terror)
                {
                    Elemento x = PilaHaskell.buscar(raiz.valor);
                    if(x != null)
                    {
                        res = new Objeto(x.tipo, x.valor);
                        res.dim = x.dim;
                        res.lvalores = x.lvalores;
                        return res;
                    }
                    else
                    {
                        String error = "La variable [" + raiz.valor + "] no ha sido declarada";
                        ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                        return new Objeto();
                    }
                }
                else
                    return res;
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
            case Const.poslista:
                return SemanticoTerm.poslista(raiz);
            case Const.llamado:
                return Haskell.llamadoTerm(raiz);
            case Const.porcentaje:
                return porcentaje;
            case Const.list:
                res = SemanticoTerm.decList(raiz);
                PilaHaskell.agregarElemeto(raiz.valor, res);
                return res;
            case Const.si:
                return SemanticoTerm.si(raiz);
        }
        return new Objeto();
    }
}
