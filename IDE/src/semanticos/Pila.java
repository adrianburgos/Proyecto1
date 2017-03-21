package semanticos;

import Reportes.ErroresGraphik;
import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;
import static semanticos.Semantico.getTipo;

public class Pila {
    private static LinkedList<Ambito> pila = new LinkedList<>();
    private static int nuevo;
    
    public static void inicializarPila(Nodo cuerpoClase)
    {//instancia las variables globales dentro de la pila
        pila.clear();
        Ambito clasePrincipal = new Ambito(-1, 0);
        inicializarClase(cuerpoClase, clasePrincipal);
        pila.add(clasePrincipal);
        nuevo = 1;
    }
    
    public static void inicializarClase(Nodo cuerpoClase, Ambito ambito)
    {
        //se busca por declaraciones dentro de la clase que contiene el metodo principal
        for (Nodo hijo : cuerpoClase.hijos)
        {
            switch (hijo.nombre)
            {
                case Const.declaracion:
                    Semantico.declaracion(hijo,ambito);
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
    
    public static void agregarElemeto(int tipo, String nombre, Ambito ambito)
    {
        Elemento elemento = new Elemento(nombre, tipo, null);
        ambito.elementos.add(elemento);
        if(tipo == Const.tals)
        {//se deben de agregar los atributos de la clase como atributos del elemento
            
        }
    }
    
    public static Elemento obtenerLid(Nodo lid)
    {
        String var = lid.hijos.get(0).valor;
        Ambito pos = buscarPosicion(nuevo - 1, var);
        //se obtiene el primer elemento de la lista de ids
        Elemento elemento = null;
        if(pos != null)
        {
            elemento = pila.get(pos.padre).elementos.get(pos.actual);
            if(lid.hijos.size() > 1)
            {//la lista de ids esta compuesta por mas de 1 id hay que buscar en el ambito del elemento actual
                Nodo noSeEncontroError = null;
                int i = 1;
                while(i < lid.hijos.size() && noSeEncontroError == null)
                {
                    if(elemento.objeto != null)
                    {//ya se le hizo nuevo al elemento
                        Nodo id = lid.hijos.get(i);
                        elemento = elemento.objeto.buscar(id.valor);
                        if(elemento == null)
                            noSeEncontroError = (Nodo) id;
                        i++;
                    }
                    else
                    {//la variable no ha sido inicializada
                        noSeEncontroError = lid.hijos.get(i-1);
                        String error = "";
                        if(elemento.tipo == Const.tals)
                            error = "La variable [" + elemento.nombre + "] no ha sido inicializado";
                        else
                            error = "La variable [" + elemento.nombre + "] no es un ALS";                            
                        ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                    }
                    noSeEncontroError = lid.hijos.get(i);
                }
                if(noSeEncontroError != null)
                {
                        String error = "La variable [" + elemento.nombre + "] no tiene un atributo [" + noSeEncontroError.valor + "]";
                        ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                }
            }
        }
        return elemento;
    }
    
    public static void asignarNuevo(String nombre)
    {
        //ya se verifico que la variable pueda recibir el valor a asignar
        Ambito pos = buscarPosicion(nuevo - 1, nombre);
        pila.get(pos.padre).elementos.get(pos.actual).objeto = new Ambito(-1, 0);
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
    
    /**
     * Se le asigna el valor a la variable o lista de variables dentro
     * de la tabla de simbolos realizando el casteo necesario 
     * con la comprobacion de tipos
     * @param lid lista de identificadores al que se le asignara el valo
     * @param valor valor a asignar
     */
    public static void asignarValor(Nodo lid, Objeto valor)
    {
        Elemento elemento = obtenerLid(lid);
        if(elemento != null)
        {
            Objeto casteo = implicito(elemento.tipo, valor);
            //verificar que el valor se pueda asignar a la variable que se desea
            if (casteo.tipo != Const.terror)
                elemento.valor = casteo.valor;
            else
                ErroresGraphik.agregarError("Error semantico", casteo.valor, 0, 0);
        }
        else
        {
            String error = "La variable [" + lid.hijos.get(0).valor + "] no ha sido declarada";
            ErroresGraphik.agregarError("Error semantico", error, 0, 0);            
        }
    }
    
    public static void asignarValor(String nombre, Objeto valor, Ambito ambito)
    {
        Elemento elemento = buscar(ambito, nombre);
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
