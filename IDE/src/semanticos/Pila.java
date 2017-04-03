package semanticos;

import Reportes.ErroresGraphik;
import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;
import static semanticos.Semantico.getTipo;

public class Pila {
    public static LinkedList<Ambito> pila = new LinkedList<>();
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

    public static void agregarElemeto(int tipo, String nombre, String tipoAls, String visibilidad)
    {
        Elemento elemento = new Elemento(nombre, tipo, null);
        elemento.tipoAls = tipoAls;
        elemento.visibilidad = visibilidad;
        pila.get(pila.size() - 1).elementos.add(elemento);
    }
    
    public static void agregarElemeto(Elemento elemento)
    {
        pila.get(pila.size() - 1).elementos.add(elemento);
    }
    
    public static void agregarElemeto(int tipo, String nombre, Ambito ambito, String tipoAls)
    {
        Elemento elemento = new Elemento(nombre, tipo, null);
        elemento.tipoAls = tipoAls;
        ambito.elementos.add(elemento);
    }
    
    public static void agregarElemeto(Elemento ele, Ambito ambito)
    {
        ambito.elementos.add(ele);
    }
    
    public static Elemento obtenerLid(Nodo lid)
    {
        Elemento elemento = null;
        Ambito pos = null;
        if(lid.hijos.get(0).hijos.isEmpty())
        {
            //se obtiene el primer elemento de la lista de ids
            String var = lid.hijos.get(0).valor;
            pos = buscarPosicion(nuevo - 1, var);
            if(pos != null)
                elemento = pila.get(pos.padre).elementos.get(pos.actual);
            else
            {
                String error = "La variable [" + var + "] no ha sido declarada";
                ErroresGraphik.agregarError("Error semantico", error, 0, 0);
            }
        }
        else
        {
            Nodo funcionALlamar = lid.hijos.get(0);
            Nodo LVALOR = funcionALlamar.hijos.get(0);//se obtiene la lista de valores
            LinkedList<Objeto> lvalores = new LinkedList();
            for(Nodo VALOR : LVALOR.hijos)
            {
                Objeto valor = Semantico.ejecutarValor(VALOR);
                lvalores.add(valor);
            }
            Nodo funcion = EjecutarArbol.buscarFuncion(funcionALlamar.valor, null, lvalores);
            if(funcion != null)
            {
                if(funcion.tipo == Const.tals)
                    crearAmbito(funcion.tipo, funcion.tipoAls);
                else
                    crearAmbito(funcion.tipo);
                Elemento resFun = EjecutarArbol.recorrerFuncion(funcion, lvalores);
                elemento = new Elemento(funcion.valor, resFun.tipo, resFun.valor);
                if(resFun.tipo == Const.tals)
                {
                    elemento.tipoAls = funcion.tipoAls;
                    elemento.objeto = (Ambito) resFun.objeto;
                }
                eliminarAmbito();
            }
            else
            {
                String valores = "";
                if (lvalores.size() > 0)
                {
                    for(Objeto val : lvalores)
                        valores += getTipo(val.tipo) + ", ";
                    valores = valores.substring(0, valores.length() - 2);
                }
                ErroresGraphik.agregarError("Error semantico", "La funcion " + funcionALlamar.valor+ "(" + valores + ") no esta declara", 0, 0);
                return new Elemento("", -1, "");
            }
        }
        if(lid.hijos.size() > 1)
        {//la lista de ids esta compuesta por mas de 1 id hay que buscar en el ambito del elemento actual
            Nodo noSeEncontroError = null;
            int i = 1;
            while(i < lid.hijos.size() && noSeEncontroError == null && !lid.hijos.get(i).nombre.equals(Const.lcorchetes))
            {
                String als = elemento.tipoAls;
                if(elemento.objeto != null)
                {//ya se le hizo nuevo al elemento
                    Nodo id = lid.hijos.get(i);
                    if(id.hijos.isEmpty())
                    {
                        elemento = elemento.objeto.buscar(id.valor);
                        if(elemento == null)
                            noSeEncontroError = (Nodo) id;
                        else
                        {//preguntar por visibilidad
                            if(!elemento.visibilidad.equals(Const.publico))
                            {
                                String error = "El Als [" + als + "] no tiene un atributo [" + elemento.valor + "] publico";
                                ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                            }
                        }
                    }
                    else
                    {//se desea ejecutar una funcion
                        //cargar el ambito de la clase (tipo ALS) de elemento dentro de la pila
                        Nodo funcionALlamar = lid.hijos.get(i);
                        Nodo LVALOR = funcionALlamar.hijos.get(0);//se obtiene la lista de valores
                        LinkedList<Objeto> lvalores = new LinkedList();
                        for(Nodo VALOR : LVALOR.hijos)
                        {
                            Objeto valor = Semantico.ejecutarValor(VALOR);
                            lvalores.add(valor);
                        }
                        Nodo funcion = EjecutarArbol.buscarFuncion(funcionALlamar.valor, elemento.tipoAls, lvalores);
                        //preguntar por la visibilidad de la funcion
                        if(funcion.visibilidad.equals(Const.publico))
                        {
                            if(null != funcion)
                            {
                                elemento.objeto.actual = nuevo;
                                nuevo++;
                                pila.add(elemento.objeto);
                                if(funcion.tipo == Const.tals)
                                    crearAmbito(funcion.tipo, funcion.tipoAls, elemento.objeto.actual);
                                else
                                    crearAmbito(funcion.tipo, elemento.objeto.actual);
                                Elemento resFun = EjecutarArbol.recorrerFuncion(funcion, lvalores);
                                elemento = new Elemento(funcion.valor, resFun.tipo, resFun.valor);
                                if(resFun.tipo == Const.tals)
                                    elemento.tipoAls = funcion.tipoAls;
                                eliminarAmbito();
                                eliminarAmbito();
                            }
                            else
                            {
                                String valores = "";
                                if (lvalores.size() > 0)
                                {
                                    for(Objeto val : lvalores)
                                        valores += getTipo(val.tipo) + ", ";
                                    valores = valores.substring(0, valores.length() - 2);
                                }
                                ErroresGraphik.agregarError("Error semantico", "La funcion " + funcionALlamar.valor+ "(" + valores + ") no esta declara", 0, 0);
                                return null;
                            }
                        }
                        else
                        {//preguntar por visibilidad
                            if(!elemento.visibilidad.equals(Const.publico))
                            {
                                String error = "El Als [" + als + "] no tiene una funcion [" + elemento.valor + "] publico";
                                ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                            }
                        }
                    }
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
            }
            if(noSeEncontroError != null)
            {
                    String error = "La variable [" + elemento.nombre + "] no tiene un atributo [" + noSeEncontroError.valor + "]";
                    ErroresGraphik.agregarError("Error semantico", error, 0, 0);
            }
        }
        
        return elemento;
    }
    
    public static void asignarNuevo(Elemento elemento, String tipoAls)
    {
        if(elemento.tipo == Const.tals)
        {
            Ambito ambito = elemento.objeto;
            ambito = new Ambito(-1, 0);
            //buscar el nodo que corresponde al objeto que se le desea hacer nuevo
            if(elemento.tipoAls.equals(tipoAls))
            {
                Nodo als = EjecutarArbol.buscarClase(elemento.tipoAls);
                if(als != null)
                {
                    inicializarClase(als.hijos.get(1), ambito);
                    elemento.objeto = ambito;
                }
                else
                {
                    String error = "El ALS [" + elemento.tipoAls + "] no existe";
                    ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                }
            }
            else
            {
                String error = "La variable [" + elemento.nombre + "] no es de tipo [" + tipoAls + "]";
                ErroresGraphik.agregarError("Error semantico", error, 0, 0);
            }
        }
        else
        {
            String error = "La variable [" + elemento.nombre + "] no es un ALS";
            ErroresGraphik.agregarError("Error semantico", error, 0, 0);
        }
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
    
    /**
     * Se le asigna el valor a la variable o lista de variables dentro
     * de la pila realizando el casteo necesario 
     * con la comprobacion de tipos
     * @param lid lista de identificadores al que se le asignara el valo
     * @param valor valor a asignar
     */
    public static void asignarValor(Nodo lid, Objeto valor)
    {
        Elemento elemento = obtenerLid(lid);
        if(elemento != null)
        {
            Objeto casteo = implicito(elemento.tipo, elemento.tipoAls, valor);
            //verificar que el valor se pueda asignar a la variable que se desea
            if (casteo.tipo != Const.terror)
            {
                
                Nodo ultimo = lid.hijos.get(lid.hijos.size() - 1);
                if(ultimo.nombre.equals(Const.lcorchetes))
                {
                    int map = mapeo(ultimo, elemento);
                    elemento.lvalores.get(map).valor = casteo.valor;
                }
                else
                {
                    elemento.valor = casteo.valor;
                    if(elemento.tipo == Const.tals)
                    {
                        elemento.tipoAls = casteo.tipoAls;
                        elemento.objeto = (Ambito) casteo.objeto;
                    }
                }
            }
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
        Objeto casteo = implicito(elemento.tipo, elemento.tipoAls, valor);
        //verificar que el valor se pueda asignar a la variable que se desea
        if (casteo.tipo != Const.terror)
        {
            elemento.valor = casteo.valor;
            if(elemento.tipo == Const.tals)
            {
                elemento.tipoAls = casteo.tipoAls;
                elemento.objeto = (Ambito) casteo.objeto;
            }
        }
        else
            ErroresGraphik.agregarError("Error semantico", casteo.valor, 0, 0);
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
                    case Const.tdecimal:
                        Double d = Double.valueOf(valor.valor);
                        int i = d.intValue();
                        if(i - d == 0)
                            return new Objeto(Const.tnumero, i + "");
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
        for (Ambito ambito : pila)
        {
            s += "------- Ambito " + ambito.actual + " (" + ambito.padre + ")-------\n";
            for (Elemento ele : ambito.elementos)
            {
                s += "[" + Semantico.getTipo(ele.tipo) + "] " + ele.nombre + " = ";
                if(ele.objeto != null)
                    s += recorrerPila(ele.objeto, 1);
                else
                    if(ele.dim != null && ele.dim.size() > 0)
                    {
                        for(Integer i : ele.dim)
                            s += "[" + i + "] ";
                        s += "\n\t{";
                        for(Objeto obj : ele.lvalores)
                            s += obj.valor + ", ";
                        s = s.substring(0,s.length() - 2);
                        s += "}\n";
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
                if(ele.dim != null && ele.dim.size() > 0)
                    {
                        for(Integer i : ele.dim)
                            s += "[" + i + "] ";
                        s += "\n\t" + tabs + "{";
                        for(Objeto obj : ele.lvalores)
                            s += obj.valor + ", ";
                        s = s.substring(0,s.length() - 2);
                        s += "}\n";
                    }
                    else
                        if(ele.valor == null)
                            s+= "null\n";
                        else
                            s += ele.valor + "\n";
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
    
    public static void crearAmbito(int tipo, int padre)
    {//crea un ambito para una funcion o procedimiento donde se le asigna un elemento de retorno
        //se crea un elemento para almacenar el retorno de las funciones
        Ambito ambito = new Ambito(padre, nuevo);
        Elemento ele = new Elemento(Const.retornar, tipo, null);
        ambito.elementos.add(ele);
        pila.add((Ambito)ambito);
        nuevo++;
    }
    
    public static void crearAmbito(int tipo, String tipoAls)
    {//crea un ambito para una funcion o procedimiento donde se le asigna un elemento de retorno
        //se crea un elemento para almacenar el retorno de las funciones
        Ambito ambito = new Ambito(0, nuevo);
        Elemento ele = new Elemento(Const.retornar, tipo, null);
        ele.tipoAls = tipoAls;
        ambito.elementos.add(ele);
        pila.add((Ambito)ambito);
        nuevo++;
    }
    
    public static void crearAmbito(int tipo, String tipoAls, int padre)
    {//crea un ambito para una funcion o procedimiento donde se le asigna un elemento de retorno
        //se crea un elemento para almacenar el retorno de las funciones
        Ambito ambito = new Ambito(padre, nuevo);
        Elemento ele = new Elemento(Const.retornar, tipo, null);
        ele.tipoAls = tipoAls;
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
    
    public static void colocarRetornar(Elemento retornar)
    {
        Elemento elemento = pila.get(pila.size() - 1).elementos.get(0);
        elemento = (Elemento) retornar;
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

    public static void asignarArreglo(Elemento ele, Nodo asig) {
        ele.dim = new LinkedList<>();
        Nodo LCORCHETES = asig.hijos.get(0);
        Nodo VAL = asig.hijos.get(1);
        int tam = 1;
        for(Nodo dim : LCORCHETES.hijos)
        {
            Objeto obj = Semantico.ejecutarValor(dim);
            if(obj.tipo == Const.tnumero)
            {
                tam *= Integer.valueOf(obj.valor);
                ele.dim.add(Integer.valueOf(obj.valor));
            }
            else
            {
                String error = "La variable [" + ele.nombre + "] no posee una dimension entera [" + Semantico.getTipo(obj.tipo) + "]";
                ErroresGraphik.agregarError("Error semantico", error, 0, 0);
            }
        }
        if(VAL.hijos.isEmpty() || !resolverArreglo(ele, VAL, 0))
        {
            ele.lvalores.clear();
            for(int i = 0; i < tam; i++)
            {
                Objeto obj = new Objeto();
                obj.tipo = ele.tipo;
                obj.valor = null;
                ele.lvalores.add(obj);
            }
        }
    }

    private static boolean resolverArreglo(Elemento ele, Nodo nodo, int pos) {
        boolean estaBien = true;
        if(ele.dim.size() - 1 >= pos && ele.dim.get(pos) == nodo.hijos.size())
        {
            if(nodo.nombre.equals(Const.lvalor))
            {
                for(Nodo val : nodo.hijos)
                {
                    Objeto obj = Semantico.ejecutarValor(val);
                    Objeto casteo = implicito(ele.tipo, "", obj);
                    if(casteo.tipo != Const.terror)
                        ele.lvalores.add(casteo);
                    else
                    {
                        String error = "Al arreglo [" + ele.nombre + "] Se le asigno un valor erroneo [" + obj.valor + "]";
                        ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                        estaBien = false;
                    }
                }
            }
            else
            {
                for(Nodo val : nodo.hijos)
                    estaBien = resolverArreglo(ele, val, pos + 1);
            }
        }
        else
        {
            String error = "No se puede asignar la dimension [" + (pos + 1) + "] del arreglo [" + ele.nombre + "] no es de tamaño [" + nodo.hijos.size() + "]";
            ErroresGraphik.agregarError("Error semantico", error, 0, 0);
            estaBien = false;
        }
        return estaBien;
    }
    
    public static int mapeo(Nodo lcorchetes, Elemento ele)
    {
        Nodo num = lcorchetes.hijos.get(0);
        int pos = 0;
        Objeto val = Semantico.ejecutarValor(num);
            if(val.tipo == Const.tnumero)
                pos = Integer.valueOf(val.valor);
        for(int i = 1; i < lcorchetes.hijos.size(); i++)
        {
            num = lcorchetes.hijos.get(i);
            val = Semantico.ejecutarValor(num);
            if(val.tipo == Const.tnumero)
            {
                if(Integer.valueOf(val.valor) < ele.dim.get(i) && Integer.valueOf(val.valor) >= 0)
                    pos = pos * ele.dim.get(i) + Integer.valueOf(val.valor);
                else
                {
                    String error = "[" + val.valor + "] sobrepasa la dimension [" + (i + 1) + "] de [" + ele.nombre + "]";
                    ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                    pos = 0;
                }
            }
            else
            {
                String error = "La dimension [" + (i + 1) + "] del arreglo [" + ele.nombre + "] no es de tipo entero.";
                ErroresGraphik.agregarError("Error semantico", error, 0, 0);
                pos = 0;
            }
        }
        return pos;
    }
}
