/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticos;

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
                case Const.imprimir:
                    Semantico.imprimir(hijo);
                    break;
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

}
