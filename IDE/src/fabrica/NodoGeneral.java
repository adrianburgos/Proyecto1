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
        Nodo ldecfun = new Nodo(Const.ldecfun);
        ldecfun.hijos.add(nodo);
        return (Nodo) ldecfun;
    }
    
    public static Nodo crearLvalor(Nodo nodo)
    {
        Nodo lvalor = new Nodo(Const.lvalor);
        lvalor.hijos.add(nodo);
        return (Nodo) lvalor;
    }
    
    public static Nodo crearLlamado(String id, Nodo nodo)
    {
        Nodo llamado = new Nodo(Const.llamado, id);
        llamado.hijos.add(nodo);
        return (Nodo) llamado;
    }
    
    public static Nodo crearCaso(Nodo valor, Nodo lcuerpo)
    {
        Nodo caso = new Nodo(Const.caso);
        caso.hijos.add(valor);
        caso.hijos.add(lcuerpo);
        return (Nodo) caso;
    }
    
    public static Nodo crearLcasos(Nodo nodo)
    {
        Nodo lcasos = new Nodo(Const.lcasos);
        lcasos.hijos.add(nodo);
        return (Nodo) lcasos;
    }
    
    public static Nodo crearCase(Nodo valor, Nodo lcasos)
    {
        Nodo caso = new Nodo(Const._case);
        caso.hijos.add(valor);
        caso.hijos.add(lcasos);
        return (Nodo) caso;
    }
    
    public static Nodo crearIf(Nodo cond, Nodo lcuerpoverdadero, Nodo lcuerpofalso)
    {
        Nodo _if = new Nodo(Const._if);
        _if.hijos.add(cond);
        _if.hijos.add(lcuerpoverdadero);
        _if.hijos.add(lcuerpofalso);
        return (Nodo) _if;
    }
    
    public static Nodo crearHoja(String nombre, String valor)
    {
        return new Nodo(nombre, valor);
    }
    
    public static Nodo crearPosLista(Nodo nodo)
    {
        Nodo poslista = new Nodo(Const.poslista);
        poslista.hijos.add(nodo);
        return (Nodo) poslista;
    }

    public static Nodo crearRevers(Nodo PARLIST) {
        Nodo oplist = new Nodo(Const.revers);
        oplist.hijos.add(PARLIST);
        return (Nodo) oplist;
    }

    public static Nodo crearImpr(Nodo PARLIST) {
        Nodo oplist = new Nodo(Const.impr);
        oplist.hijos.add(PARLIST);
        return (Nodo) oplist;
    }

    public static Nodo crearPar(Nodo PARLIST) {
        Nodo oplist = new Nodo(Const.par);
        oplist.hijos.add(PARLIST);
        return (Nodo) oplist;
    }

    public static Nodo crearAsc(Nodo PARLIST) {
        Nodo oplist = new Nodo(Const.asc);
        oplist.hijos.add(PARLIST);
        return (Nodo) oplist;
    }

    public static Nodo crearDesc(Nodo PARLIST) {
        Nodo oplist = new Nodo(Const.desc);
        oplist.hijos.add(PARLIST);
        return (Nodo) oplist;
    }

    public static Nodo crearSum(Nodo PARLIST) {
        Nodo oplist = new Nodo(Const.sum);
        oplist.hijos.add(PARLIST);
        return (Nodo) oplist;
    }

    public static Nodo crearProduct(Nodo PARLIST) {
        Nodo oplist = new Nodo(Const.product);
        oplist.hijos.add(PARLIST);
        return (Nodo) oplist;
    }

    public static Nodo crearLength(Nodo PARLIST) {
        Nodo oplist = new Nodo(Const.length);
        oplist.hijos.add(PARLIST);
        return (Nodo) oplist;
    }

    public static Nodo crearMax(Nodo PARLIST) {
        Nodo max = new Nodo(Const.max);
        max.hijos.add(PARLIST);
        return (Nodo) max;
    }

    public static Nodo crearMin(Nodo PARLIST) {
        Nodo min = new Nodo(Const.min);
        min.hijos.add(PARLIST);
        return (Nodo) min;
    }

    public static Nodo crearDecc(Nodo VALOR) {
        Nodo decc = new Nodo(Const.decc);
        decc.hijos.add(VALOR);
        return (Nodo) decc;
    }

    public static Nodo crearSucc(Nodo VALOR) {
        Nodo succ = new Nodo(Const.succ);
        succ.hijos.add(VALOR);
        return (Nodo) succ;
    }
    
    public static Nodo crearLcar(Nodo nodo)
    {
        Nodo lcar = new Nodo(Const.lcar);
        lcar.hijos.add(nodo);
        return (Nodo) lcar;
    }
}
