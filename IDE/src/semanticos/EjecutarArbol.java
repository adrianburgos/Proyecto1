/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticos;

import Reportes.ErroresGraphik;
import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;

/**
 *
 * @author Adrian
 */
public class EjecutarArbol {
    public static Nodo raiz;
    
    public static void ejecutar(Nodo raizPar)
    {
        raiz = raizPar;
        //incluye e importar
        
        //hereda
        
        //crear tabla de simbolos
        LinkedList<Nodo> clasePrincipal = buscarClasePrincipal(raiz.hijos.get(1));
        Pila.inicializarPila(clasePrincipal.get(0).hijos.get(1));
        //ejecutar principal
        ejecutarPrincipal(clasePrincipal.get(1));
    }
    
    private static void ejecutarPrincipal(Nodo nodo) {
        //se crea el ambito para Principal
        Pila.crearAmbito(Const.tvacio);
        ejecutarCuerpo(nodo.hijos.get(0));
        //Pila.eliminarAmbito();
    }
    
    public static TipoRetorno ejecutarCuerpo(Nodo cuerpo)
    {
        TipoRetorno retorno = new TipoRetorno();
        for(Nodo hijo : cuerpo.hijos)
        {
            switch(hijo.getNombre())
            {
                case Const.asignacion:
                    Semantico.asignacion(hijo);
                    break;
                case Const.declaracion:
                    Semantico.declaracion(hijo);
                    break;
                case Const.llamar:
                    Semantico.llamar(hijo);
                    break;
                case Const.si:
                    retorno = Semantico.si(hijo);
                    if(retorno.retorno || retorno.continuar || retorno.terminar)
                        return retorno;
                    break;
                case Const.seleccion:
                    retorno = Semantico.seleccion(hijo);
                    if(retorno.retorno || retorno.continuar || retorno.terminar)
                        return retorno;
                    break;
                case Const.para:
                    retorno = Semantico.para(hijo);
                    if(retorno.retorno || retorno.continuar || retorno.terminar)
                        return retorno;
                    break;
                case Const.mientras:
                    retorno = Semantico.mientras(hijo);
                    if(retorno.retorno || retorno.continuar || retorno.terminar)
                        return retorno;
                    break;
                case Const.hacer:
                    retorno = Semantico.hacer(hijo);
                    if(retorno.retorno || retorno.continuar || retorno.terminar)
                        return retorno;
                    break;
                case Const.imprimir:
                    Semantico.imprimir(hijo);
                    break;
                case Const.graficar:
                    Semantico.graficar(hijo);
                    break;
                case Const.retornar:
                    Semantico.retornar(hijo);
                    return new TipoRetorno(true, false, false);
                case Const.continuar:
                    return new TipoRetorno(false, true, false);
                case Const.terminar:
                    return new TipoRetorno(false, false, true);
            }
        }
        return retorno;
    }
    
    private static LinkedList<Nodo> buscarClasePrincipal(Nodo lals)
    {
        LinkedList clasePrincipal = null;
        for(Nodo als : lals.hijos)
        {
            for(Nodo nodo : als.hijos.get(1).hijos)
            {
                if(nodo.nombre.equals(Const.principal))
                {
                    clasePrincipal = new LinkedList();
                    clasePrincipal.add(als);
                    clasePrincipal.add(nodo);
                }
            }
        }
        return clasePrincipal;
    }
    
    private static Nodo buscar(Nodo raiz, String nombre)
    {
        Nodo nodo = null;
        for(Nodo hijo : raiz.hijos)
        {
            if (hijo.getNombre().equals(nombre))
                return hijo;
            else
                nodo = buscar(hijo, nombre);
        }
        return nodo;
    }
    
    public static Nodo buscarClase(String nombreAls)
    {
        nombreAls = nombreAls.toLowerCase();
        for(Nodo als : raiz.hijos.get(1).hijos)
            if(als.valor.toLowerCase().equals(nombreAls))
                return als;
        return null;
    }
    
    
    
    public static Nodo buscarFuncion(String nombre, String nombreAls, LinkedList<Objeto> lvalores)
    {
        Nodo arbolAls;
        if(nombreAls == null)
        //se debe de buscar en el arbol normal del ALS que contiene principal
            arbolAls = buscarClasePrincipal(raiz.hijos.get(1)).get(0);
        else
        //se debe de buscar en el arbol del ALS que posee esa funcion
            arbolAls = buscarClase(nombreAls);
        if(arbolAls != null)
        {
            for(Nodo hijo : arbolAls.hijos.get(1).hijos)
            {//se busca en los hijos de LCUERPOALS
                if(hijo.nombre.equals(Const.decfun))
                {//es una declaracion
                    if(hijo.valor.equals(nombre))
                    {//la funcion encontrada tiene el mismo nombre
                        Nodo LPAR = hijo.hijos.get(0);
                        if(LPAR.hijos.size() == lvalores.size())
                        {//la cantidad de parametros de la funcion es igual a los enviados en recorrerFuncion
                            Boolean seEncontro = true;
                            for(int i = 0; i < lvalores.size(); i++)
                            {
                                Nodo parametro = LPAR.hijos.get(i);
                                if (parametro.tipo != lvalores.get(i).tipo)
                                    seEncontro = false;
                                if(parametro.tipo == Const.tals && parametro.tipo == lvalores.get(i).tipo)
                                    if(!parametro.tipoAls.equals(lvalores.get(i).tipoAls))
                                        seEncontro = false;
                            }
                            if (seEncontro)
                                //se retorna la declaracion de la funcion para poder obtener el tipo de la funcion
                                return hijo;
                        }
                    }
                    
                }
            }
        }
        return null;
    }
    
    /**
     * recorre el cuerpo de la funcion agregando sus parametros como variables
     * al ambito correspondiente
     * @param funcion
     * @param lvalores lista de valores que se pasaran a los parametros
     * @return el valor de retorno deseado verificando que sean del mismo tipo
     */
    public static Elemento recorrerFuncion(Nodo funcion, LinkedList<Objeto> lvalores)
    {
        Elemento res = new Elemento(funcion.valor, funcion.tipo, "");
        if(funcion.tipo == Const.tals)
            res.tipoAls = funcion.tipoAls;
        //agregar parametros como variables del ambito correspodiente
        Nodo lpar = funcion.hijos.get(0);
        for(int i = 0; i < lvalores.size(); i++)
        {
            Nodo par = lpar.hijos.get(i);
            Elemento elePar = new Elemento(par.valor, par.tipo, lvalores.get(i).valor);
            if(lvalores.get(i).tipo == Const.tals)
            {
                elePar.tipoAls = lvalores.get(i).tipoAls;
                elePar.objeto = (Ambito) lvalores.get(i).objeto;
            }
            Pila.agregarElemeto(elePar);
        }
        Nodo lcuerpo = funcion.hijos.get(1);
        ejecutarCuerpo(lcuerpo);
        Objeto objRet = Pila.getRetorno();        
        if((objRet.valor != null || objRet.objeto != null) && funcion.tipo != Const.tvacio)
        {
            if(objRet.tipo == res.tipo)
                if(objRet.tipo == Const.tals)
                {
                    if(objRet.tipoAls.equals(res.tipoAls))
                    {
                        res = new Elemento(funcion.valor, funcion.tipo, objRet.valor);
                        res.objeto = (Ambito) objRet.objeto;
                        res.tipoAls = objRet.tipoAls;
                    }
                }
                else
                    res = new Elemento(funcion.valor, funcion.tipo, objRet.valor);
        }
        else
        {// no vino retorno dentro del cuerpo de la funcion
            if(funcion.tipo != Const.tvacio)
            {
                String error = "No vino retorno dentro del cuerpo de la funcion [" + funcion.valor + "]";
                ErroresGraphik.agregarError("Error semantico", error, 0, 0);
            }
        }
        return res;
    }
    
    public static boolean buscarIncluye(String nombre)
    {
        Nodo LIMPORTAR = raiz.hijos.get(0);
        for(Nodo importar : LIMPORTAR.hijos)
            if(importar.nombre.equals(Const.incluirhk) && importar.valor.equals(nombre))
                return true;
        return false;
    }

}
