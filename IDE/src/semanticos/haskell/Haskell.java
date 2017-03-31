/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticos.haskell;

import Reportes.ErroresGraphik;
import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;
import semanticos.Ambito;
import semanticos.EjecutarArbol;
import semanticos.Elemento;
import semanticos.Objeto;
import semanticos.Pila;
import semanticos.Semantico;
import semanticos.terminal.EjecutarTerm;
import semanticos.terminal.PilaHaskell;
import semanticos.terminal.SemanticoTerm;

/**
 *
 * @author Adrian
 */
public class Haskell {
    
    public static Nodo arbol = new Nodo();
    
    public static Nodo buscarFuncionHaskell(String nombre, LinkedList<Objeto> lvalores)
    {
        for(Nodo funcion : arbol.hijos)
        {
            if(funcion.valor.equals(nombre))
            {
                Nodo lpar = funcion.hijos.get(0);
                if(lpar.hijos.size() == lvalores.size())
                {//la cantidad de parametros coinciden
                    for(int i = 0; i < lvalores.size(); i++)
                    {
                        if(lvalores.get(i).tipo == Const.tnumero)
                            lpar.hijos.get(i).tipo = Const.tdecimal;
                        else
                            lpar.hijos.get(i).tipo = lvalores.get(i).tipo;
                    }
                    return funcion;
                }
            }
        }
        return null;
    }
    
    public static Objeto llamado(Nodo llamado)
    {
        String id = llamado.hijos.get(0).valor;
        Objeto res = new Objeto();
        if(EjecutarArbol.buscarIncluye(id))
        {
            Nodo LVALOR = llamado.hijos.get(1);//se obtiene la lista de valores
            LinkedList<Objeto> lvalores = new LinkedList<>();
            for(Nodo VALOR : LVALOR.hijos)
            {
                Objeto valor = Semantico.ejecutarValor(VALOR);
                lvalores.add(valor);
            }
            Nodo funcion = buscarFuncionHaskell(id, lvalores);
            if(funcion != null)
            {
                Pila.crearAmbito(Const.tvacio, -1);
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
                
                for(Nodo inst : funcion.hijos.get(1).hijos)
                {
                    res = EjecutarTerm.ejecutarInst(inst);
                }
                //Pila.eliminarAmbito();
            }
            else
            {
                String error = "La funcion [" + id + "] no ha sido cargada a memoria";
                ErroresGraphik.agregarError("Error semantico", error, 0, 0);
            }
        }
        else
        {
            String error = "La funcion [" + id + "] no ha sido incluida";
            ErroresGraphik.agregarError("Error semantico", error, 0, 0);
        }
        return res;
    }
    
    public static Objeto llamadoTerm(Nodo llamado)
    {
        String id = llamado.hijos.get(0).valor;
        Objeto res = new Objeto();
        Nodo LVALOR = llamado.hijos.get(1);//se obtiene la lista de valores
        LinkedList<Objeto> lvalores = new LinkedList<>();
        for(Nodo VALOR : LVALOR.hijos)
        {
            Objeto valor = EjecutarTerm.ejecutarInst(VALOR);
            lvalores.add(valor);
        }
        Nodo funcion = buscarFuncionHaskell(id, lvalores);
        if(funcion != null)
        {
            Nodo lpar = funcion.hijos.get(0);
            for(int i = 0; i < lvalores.size(); i++)
            {
                Nodo par = lpar.hijos.get(i);
                Elemento elePar = new Elemento(par.valor, par.tipo, lvalores.get(i).valor);
                elePar.dim = lvalores.get(i).dim;
                elePar.lvalores = lvalores.get(i).lvalores;
                PilaHaskell.agregarElemeto(elePar);
            }

            for(Nodo inst : funcion.hijos.get(1).hijos)
            {
                res = EjecutarTerm.ejecutarInst(inst);
            }
        }
        else
        {
            String error = "La funcion [" + id + "] no ha sido cargada a memoria";
            ErroresGraphik.agregarError("Error semantico", error, 0, 0);
        }
        return res;
    }
}
