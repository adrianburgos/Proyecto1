/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticos.terminal;

import Reportes.ErroresGraphik;
import ide.Const;
import java.util.LinkedList;
import semanticos.Ambito;
import semanticos.Elemento;
import semanticos.Objeto;
import semanticos.Semantico;
import static semanticos.Semantico.getTipo;

/**
 *
 * @author Adrian
 */
public class PilaHaskell {
    public static LinkedList<Ambito> pila = new LinkedList<>();
    private static int nuevo;
    
    public static void agregarParametro(String nombre, int tipo, String valor)
    {
        Elemento elemento = new Elemento(nombre, tipo, valor);
        pila.get(pila.size() - 1).elementos.add(elemento);
    }
    
    public static void agregarElemeto(Elemento elemento)
    {
        pila.get(pila.size() - 1).elementos.add(elemento);
    }
    
    public static void agregarElemeto(String nombre, Objeto obj)
    {
        Elemento elemento = new Elemento(nombre, obj.tipo, obj.valor);
        elemento.dim = (LinkedList <Integer>) obj.dim;
        elemento.lvalores = (LinkedList<Objeto>) obj.lvalores;
        pila.get(pila.size() - 1).elementos.add(elemento);
    }
    
    public static void asignarValor(String nombre, Objeto valor)
    {
        Ambito pos = buscarPosicion(nuevo - 1, nombre);
        if(pos != null)
        {
            Elemento elemento = pila.get(pos.padre).elementos.get(pos.actual);
            Objeto casteo = implicito(elemento.tipo, elemento.tipoAls, valor);
            //verificar que el valor se pueda asignar a la variable que se desea
            if (casteo.tipo != Const.terror)
            {
                elemento.valor = casteo.valor;
                if(casteo.tipo == Const.tals)
                {
                    elemento.tipoAls = casteo.tipoAls;
                    elemento.objeto = (Ambito) casteo.objeto;
                }
            }
            else
                ErroresGraphik.agregarError("Error semantico", casteo.valor, 0, 0);
        }
        else
        {
            String error = "La variable [" + nombre + "] no ha sido declarada";
            ErroresGraphik.agregarError("Error semantico", error, 0, 0);
        }
        
    }
    
    public static Objeto implicito(int tipoVar, String tipoAls, Objeto valor)
    {
        if(tipoVar == valor.tipo)
        {
            if(tipoVar == Const.tals)
            {
                if(valor.tipoAls.equals(tipoAls))
                    return (Objeto) valor;
                else
                {
                    String error = "No se pudo asignar [" + tipoAls + "] = [" + valor.tipoAls + "]";
                    return new Objeto(Const.terror, error);
                }
            }
            else
                return (Objeto) valor;
                
        }
        switch(tipoVar)
        {
            case Const.tcadena:
                switch (valor.tipo)
                {
                    case Const.tnumero:
                    case Const.tdecimal:
                    case Const.tcaracter:
                        return new Objeto(Const.tcadena, valor.valor);
                    case Const.tbool:
                        if(valor.valor.equals(Const.verdadero))
                            return new Objeto(Const.tcadena, "1");
                        else
                            return new Objeto(Const.tcadena, "0");
                }
                break;
            case Const.tnumero:
                switch (valor.tipo)
                {
                    case Const.tcaracter:
                        return new Objeto(Const.tnumero, (int)valor.valor.charAt(0) + "");
                    case Const.tbool:
                        if (valor.valor.equals(Const.verdadero))
                            return new Objeto(Const.tnumero, "1");
                        else
                            return new Objeto(Const.tnumero, "0");
                }
            case Const.tdecimal:
                switch (valor.tipo)
                {
                    case Const.tcaracter:
                        return new Objeto(Const.tdecimal, (int)valor.valor.charAt(0) + "");
                    case Const.tnumero:
                        return new Objeto(Const.tdecimal, Double.valueOf(valor.valor) + "");
                    case Const.tbool:
                        if (valor.valor.equals(Const.verdadero))
                            return new Objeto(Const.tdecimal, "1");
                        else
                            return new Objeto(Const.tdecimal, "0");
                }
            case Const.tcaracter:
                switch (valor.tipo)
                {
                    case Const.tnumero:
                        int valint = Integer.valueOf(valor.valor);
                        char val;
                        if(valint > 0 && valint < 256)
                        {
                            val = (char) valint;
                            return new Objeto(Const.tcaracter, val + "");
                        }
                        else
                        {
                            String error = "No se pudo asignar [" + getTipo(tipoVar) + "] = [" + getTipo(valor.tipo) + " = " + valint + "]";
                            return new Objeto(Const.terror, error);
                        }
                    case Const.tbool:
                        if (valor.valor.equals(Const.verdadero))
                            return new Objeto(Const.tcaracter, "1");
                        else
                            return new Objeto(Const.tcaracter, "0");
                }
                break;
        }
        String error = "No se pudo asignar [" + getTipo(tipoVar) + "] = [" + getTipo(valor.tipo) + "]";
        return new Objeto(Const.terror, error);
    }
    
    

    // <editor-fold desc="seccion de busqueda">
    public static Elemento buscar(String nombre)
    {
        return buscar(nuevo - 1, nombre);
    }//fin buscar

    private static Elemento buscar(int ambito, String nombre)
    {
        if (ambito == -1)
            return null;
        Ambito amb = pila.get(ambito);
        for (Elemento elemento : amb.elementos)
            if (elemento.nombre.equals(nombre))
                return elemento;
        return buscar(amb.padre, nombre);
    }
    
    private static Elemento buscar(Ambito ambito, String nombre)
    {
        for (Elemento elemento : ambito.elementos)
            if (elemento.nombre.equals(nombre))
                return elemento;
        return null;
    }
    
    
    /**
     * en padre se retorna el ambito en el que esta la variable y en actual
     * se encuentra su posicion dentro del ambito
     * @param ambito ambito en el cual se empieza a buscar la variable
     * @param nombre nombre de la variable a encontrar
     * @return 
     */
    private static Ambito buscarPosicion(int ambito, String nombre)
    {
        if (ambito == -1)
            return null;
        Ambito amb = pila.get(ambito);
        int cont = 0;
        for (Elemento elemento : amb.elementos)
        {
            if (elemento.nombre.equals(nombre))
                return new Ambito(ambito, cont);
            cont++;
        }
        return buscarPosicion(amb.padre, nombre);
    }
    
    public static void crearAmbito(int tipo)
    {//crea un ambito para una funcion o procedimiento donde se le asigna un elemento de retorno
        //se crea un elemento para almacenar el retorno de las funciones
        Ambito ambito = new Ambito(0, nuevo);
        Elemento ele = new Elemento(Const.retornar, tipo, null);
        ambito.elementos.add(ele);
        pila.add((Ambito)ambito);
        nuevo++;
    }
    
    public static void crearAmbito(int tipo, int padre)
    {//crea un ambito para una funcion o procedimiento donde se le asigna un elemento de retorno
        //se crea un elemento para almacenar el retorno de las funciones
        Ambito ambito = new Ambito(padre, nuevo);
        Elemento ele = new Elemento(Const.retornar, tipo, null);
        ambito.elementos.add(ele);
        pila.add((Ambito)ambito);
        nuevo++;
    }
    
    public static void crearAmbito()
    {//para las sentecias de control
        Ambito ambito = new Ambito(nuevo - 1, nuevo);
        Elemento ele = new Elemento(Const.retornar, Const.tvacio, null);
        ambito.elementos.add(ele);
        pila.add((Ambito)ambito);
        nuevo++;
    }
    
    public static void eliminarAmbito()
    {
        pila.remove(pila.size() - 1);
        nuevo--;
    }
    
    public static Objeto getRetorno()
    {
        int tipoRetorno = pila.get(pila.size() - 1).elementos.get(0).tipo;
        String valorRetorno = pila.get(pila.size() - 1).elementos.get(0).valor;
        String tipoAls = pila.get(pila.size() - 1).elementos.get(0).tipoAls;
        if(tipoRetorno == Const.tals)
        {
            Objeto res = new Objeto(tipoRetorno, valorRetorno, tipoAls);
            res.objeto = (Ambito) pila.get(pila.size() - 1).elementos.get(0).objeto;
            return res;
        }
        else
            return new Objeto(tipoRetorno, valorRetorno);
    }
    
    public static void setRetorno(Objeto retorno)
    {
        Elemento ele = new Elemento(Const.retornar, retorno.tipo, retorno.valor);
        if(retorno.tipo == Const.tals)
        {
            ele.objeto = (Ambito) retorno.objeto;
            ele.tipoAls = retorno.tipoAls;
        }
        pila.get(pila.size() - 1).elementos.remove(0);
        pila.get(pila.size() - 1).elementos.add(0,ele);
    }
    // </editor-fold>
    
    public static String recorrerPila()
    {
        String s = "";
        for (Ambito ambito : pila)
        {
            s += "------- Ambito " + ambito.actual + " (" + ambito.padre + ")-------\n";
            for (Elemento ele : ambito.elementos)
            {
                s += "[" + Semantico.getTipo(ele.tipo) + "] " + ele.nombre + " = ";
                if(ele.objeto != null)
                    s += recorrerPila(ele.objeto, 1);
                else
                    if(ele.dim.size() > 0)
                    {
                        s += "Dimensiones: [";
                        for(Integer dim : ele.dim)
                            s += dim + ", ";
                        s = s.substring(0, s.length() - 2);
                        s += "]\n";
                    }
                else
                    if(ele.valor == null)
                        s+= "null\n";
                    else
                        s += ele.valor + "\n";
            }
        }
        return s;
    }
    
    private static String recorrerPila(Ambito ambito, int profundidad)
    {
        String s = "\n";
        String tabs = "";
        for (int i = 0; i < profundidad; i++)
            tabs += "\t";
        for (Elemento ele : ambito.elementos)
        {
            s += tabs + "[" + Semantico.getTipo(ele.tipo) + "] " + ele.nombre + " = ";
            if(ele.objeto != null)
                s += recorrerPila(ele.objeto, profundidad + 1);
            else
                if(ele.valor == null)
                    s+= "null\n";
                else
                    s += ele.valor + "\n";
        }
        return s;
    }
}
