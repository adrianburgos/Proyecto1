/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticos;

import fabrica.Nodo;
import ide.Const;

/**
 *
 * @author Adrian
 */
public class EjecutarArbol {
    public static Nodo raiz;
    
    public static void ejecutar(Nodo raizPar)
    {
        raiz = raizPar;
        //incluye y importar
        
        //crear tabla de simbolos
        //Pila.inicializarPila(raiz.ChildNodes.ElementAt(1));
        
        //ejecutar principal
        ejecutarPrincipal(buscar(raiz, Const.principal));
    }
    
    private static void ejecutarPrincipal(Nodo nodo) {
        //se crea el ambito para Principal
        //Pila.crearAmbito(Const.tipo_void);
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
            }
        }
        return retorno;
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

}
