package fabrica;

import ide.Const;
import semanticos.Semantico;

public class NodoGeneral{
    
    public static Nodo crearCuerpo(Nodo nodo)
    {
        Nodo lcuerpo = new Nodo(Const.lcuerpo);
        lcuerpo.hijos.add(nodo);
        return (Nodo) lcuerpo;
    }
    
    public static Nodo crearLdecfun(Nodo nodo)
    {
        Nodo ldecfun = new Nodo(Const.ldecfun);
        ldecfun.hijos.add(nodo);
        return (Nodo) ldecfun;
    }
    
    public static Nodo crearFuncion(String id, Nodo parametros, Nodo cuerpo)
    {
        Nodo decfun = new Nodo(Const.decfun, id);
        decfun.hijos.add(parametros);
        decfun.hijos.add(cuerpo);
        return (Nodo) decfun;
    }
    
    public static Nodo crearLvalor(Nodo nodo)
    {
        Nodo lvalor = new Nodo(Const.lvalor);
        lvalor.hijos.add(nodo);
        return (Nodo) lvalor;
    }
    
    public static Nodo crearLlamado(String nombre, Nodo nodo)
    {
        Nodo id = new Nodo(Const.id, nombre);
        Nodo llamado = new Nodo(Const.llamado);
        llamado.hijos.add(id);
        llamado.hijos.add(nodo); //se agregan la lista de valores
        return (Nodo) llamado;
    }
    
    public static Nodo crearLista(String id, Nodo nodo)
    {
        Nodo lista = new Nodo(Const.list, id);
        lista.hijos.add(nodo);
        return (Nodo) lista;
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
    
    public static Nodo crearIf(Nodo cond, Nodo lcuerpoverdadero, Nodo lcuerpofalso)
    {
        Nodo _if = new Nodo(Const.si);
        _if.hijos.add(cond);
        _if.hijos.add(lcuerpoverdadero);
        if(lcuerpofalso != null)
            _if.hijos.add(lcuerpofalso);
        return (Nodo) _if;
    }
    
    public static Nodo crearHoja(String nombre, String valor)
    {
        return new Nodo(nombre, valor);
    }
    
    public static Nodo crearHoja(String nombre, String valor, int tipo)
    {
        return new Nodo(nombre, valor, tipo);
    }
    
    public static Nodo crearPosLista(Nodo nodo, Nodo pos)
    {
        Nodo poslista = new Nodo(Const.poslista);
        poslista.hijos.add(nodo);
        poslista.hijos.add(pos);
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
    
    public static Nodo crearLista(Nodo nodo)
    {
        Nodo lista = new Nodo(Const.lista);
        lista.hijos.add(nodo);
        return (Nodo) lista;
    }
    
    public static Nodo crearLcorchetes(Nodo nodo)
    {
        Nodo lcorchetes = new Nodo(Const.lcorchetes);
        lcorchetes.hijos.add(nodo);
        return (Nodo) lcorchetes;
    }
/*
    --------------------------METODOS GRAPHIK-------------------------------------
*/
    public static Nodo crearLid(Nodo id)
    {
        Nodo lid = new Nodo(Const.lid);
        lid.hijos.add(id);
        return (Nodo) lid;
    }
    
    public static Nodo crearLimportar(Nodo nodo)
    {
        Nodo limportar = new Nodo(Const.limportar);
        limportar.hijos.add(nodo);
        return (Nodo) limportar;
    }
    
    public static Nodo crearImportar(String id, String ext)
    {
        Nodo limportar = new Nodo(Const.limportar, id + ext);
        return (Nodo) limportar;
    }
    
    public static Nodo crearIncluirhk(String id)
    {
        Nodo incluir = new Nodo(Const.limportar, id);
        return (Nodo) incluir;
    }
    
    public static Nodo crearLals(Nodo als)
    {
        Nodo lals = new Nodo(Const.lals);
        lals.hijos.add(als);
        return (Nodo) lals;
    }
    
    public static Nodo crearAls(String id, String visibilidad, Nodo hereda, Nodo lcuerpoals)
    {
        Nodo als = new Nodo(Const.als, id, Const.tals, visibilidad);
        als.hijos.add(hereda);
        als.hijos.add(lcuerpoals);
        return (Nodo) als;
    }
    
    public static Nodo crearLcuerpoals(Nodo cuerpoals)
    {
        Nodo lcuerpoals = new Nodo(Const.lcuerpoals);
        lcuerpoals.hijos.add(cuerpoals);
        return (Nodo) lcuerpoals;
    }
    
    public static Nodo crearFuncion(String id, String tipo, String visibilidad, Nodo parametros, Nodo cuerpo)
    {
        int t = Semantico.getTipo(tipo);
        Nodo decfun = new Nodo(Const.decfun, id, t, visibilidad);
        decfun.hijos.add(parametros);
        decfun.hijos.add(cuerpo);
        if(t == Const.tals)
            decfun.tipoAls = tipo.toLowerCase();
        return (Nodo) decfun;
    }
    
    public static Nodo crearLpar(String id, String tipo)
    {
        int t = Semantico.getTipo(tipo);
        Nodo par = new Nodo(Const.id, id, t);
        Nodo lals = new Nodo(Const.lpar);
        if(t == Const.tals)
            par.tipoAls = tipo.toLowerCase();
        lals.hijos.add(par);
        return (Nodo) lals;
    }
    
    public static Nodo crearDec(String id, String tipo, String visibilidad, Nodo asig)
    {
        int t = Semantico.getTipo(tipo);
        Nodo dec = new Nodo(Const.declaracion, id, t, visibilidad);
        if(t == Const.tals)
            dec.tipoAls = tipo.toLowerCase();
        if(asig != null)
            dec.hijos.add(asig);
        return (Nodo) dec;
    }
    
    public static Nodo crearLvariables(String id, String visibilidad)
    {
        Nodo nodo = new Nodo(id);
        nodo.setVisibilidad(visibilidad);
        Nodo lvariables = new Nodo(Const.lvariables);
        lvariables.hijos.add(nodo);
        return (Nodo) lvariables;
    }
    
    public static Nodo crearPrincipal(Nodo cuerpo)
    {
        Nodo principal = new Nodo(Const.principal);
        principal.hijos.add(cuerpo);
        return (Nodo) principal;        
    }
    
    public static Nodo crearDefecto(Nodo cuerpo)
    {
        Nodo defecto = new Nodo(Const.defecto);
        defecto.hijos.add(cuerpo);
        return (Nodo) defecto;        
    }
    
    public static Nodo crearSeleccion(Nodo acceso, Nodo lcasos, Nodo defecto)
    {
        Nodo seleccion = new Nodo(Const.seleccion);
        seleccion.hijos.add(acceso);
        seleccion.hijos.add(lcasos);
        if(defecto != null)
            seleccion.hijos.add(defecto);
        return (Nodo) seleccion;        
    }
    
    public static Nodo crearPara(Nodo asigpara, Nodo valor, Nodo val, Nodo cuerpo)
    {
        Nodo para = new Nodo(Const.para);
        para.hijos.add(asigpara);
        para.hijos.add(valor);
        para.hijos.add(val);
        para.hijos.add(cuerpo);
        return (Nodo) para;        
    }
    
    public static Nodo crearAumento(Nodo lid, String op)
    {
        Nodo numero = NodoGeneral.crearHoja(Const.numero, "1");
        Nodo valor;
        if (op.equals(Const.masmas))
            valor = NodoOperacion.crearNodo(Const.mas, lid, numero);
        else
            valor = NodoOperacion.crearNodo(Const.menos, lid, numero);
            
        Nodo aumento = NodoGeneral.crearAsignacion(lid, valor, null);
        return (Nodo) aumento;        
    }

    public static Nodo crearAsignacion(Nodo lid, Nodo valor, Nodo lcorchetes) {
        Nodo asig = new Nodo(Const.asignacion);
        asig.hijos.add(lid);
        asig.hijos.add(valor);
        if(lcorchetes != null)
            asig.hijos.add(lcorchetes);
        return (Nodo) asig;        
    }

    public static Nodo crearMientras(Nodo valor, Nodo cuerpo) {
        Nodo mientras = new Nodo(Const.mientras);
        mientras.hijos.add(valor);
        mientras.hijos.add(cuerpo);
        return (Nodo) mientras;        
    }

    public static Nodo crearHacer(Nodo valor, Nodo cuerpo) {
        Nodo hacer = new Nodo(Const.hacer);
        hacer.hijos.add(valor);
        hacer.hijos.add(cuerpo);
        return (Nodo) hacer;
    }

    public static Nodo crearGraficar(Nodo lid1, Nodo lid2) {
        Nodo graficar = new Nodo(Const.graficar);
        graficar.hijos.add(lid1);
        graficar.hijos.add(lid2);
        return (Nodo) graficar;    
    }
    
    public static Nodo crearImprimir(Nodo valor)
    {
        Nodo imprimir = new Nodo(Const.imprimir);
        imprimir.hijos.add(valor);
        return (Nodo) imprimir;        
    }
    
    public static Nodo crearLlamar(Nodo lid, Nodo lvalor)
    {
        Nodo imprimir = new Nodo(Const.llamar);
        imprimir.hijos.add(lid);
        imprimir.hijos.add(lvalor);
        return (Nodo) imprimir;        
    }
}
