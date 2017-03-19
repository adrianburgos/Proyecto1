package semanticos;

import Reportes.ErroresGraphik;
import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;
import static semanticos.Semantico.getTipo;

public class Pila {
    private static LinkedList<Ambito> pila = new LinkedList<Ambito>();
    private static int nuevo;
    
    public static void inicializarPila(Nodo cuerpoClase)
    {//instancia las variables globales dentro de la pila
        pila.clear();
        Ambito clase = new Ambito(-1, 0);
        pila.add(clase);
        nuevo = 1;
        //se busca por declaraciones dentro de la clase que contiene el metodo principal
        for (Nodo hijo : cuerpoClase.hijos)
        {
            switch (hijo.nombre)
            {
                case Const.declaracion:
                    Semantico.declaracion(hijo);
                    break;
            }
        }//fin foreach
    }
    
    public static void agregarParametro(String nombre, int tipo, String valor)
    {
        Elemento elemento = new Elemento(nombre, tipo, valor);
        pila.get(pila.size() - 1).elementos.add(elemento);
    }

    public static void agregarElemeto(int tipo, String nombre)
    {
        Elemento elemento = new Elemento(nombre, tipo, null);
        pila.get(pila.size() - 1).elementos.add(elemento);
        if(tipo == Const.tals)
        {//se deben de agregar los atributos de la clase como atributos del elemento
            
        }
    }
    
    public static void asignarNuevo(String nombre)
    {
        //ya se verifico que la variable pueda recibir el valor a asignar
        Ambito pos = buscarPosicion(nuevo - 1, nombre);
        pila.get(pos.padre).elementos.get(pos.actual).objeto = new LinkedList<>();
    }
    
    public static void asignarValor(String nombre, Objeto valor)
    {
        Ambito pos = buscarPosicion(nuevo - 1, nombre);
        Elemento elemento = pila.get(pos.padre).elementos.get(pos.actual);
        Objeto casteo = implicito(elemento.tipo, valor);
        //verificar que el valor se pueda asignar a la variable que se desea
        if (casteo.tipo != Const.terror)
            elemento.valor = casteo.valor;
        else
            ErroresGraphik.agregarError("Error semantico", casteo.valor, 0, 0);
    }
    
    public static Objeto implicito(int tipoVar, Objeto valor)
    {
        if(tipoVar == valor.tipo)
            return new Objeto(valor.tipo, valor.valor);
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
    
    public static String recorrerPila()
        {
            String s = "";
            for (Ambito ambito : Pila.pila)
            {
                s += "------- Ambito " + ambito.actual + " -------\n";
                for (Elemento ele : ambito.elementos)
                {
                    s += "[" + Semantico.getTipo(ele.tipo) + "] " + ele.nombre + " = ";
                    if(ele.valor == null)
                        s+= "null\n";
                    else
                        s += ele.valor + "\n";
                }
            }
            return s;
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
    // </editor-fold>
}
