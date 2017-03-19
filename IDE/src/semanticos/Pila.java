package semanticos;

import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;

public class Pila {
    private static LinkedList<Ambito> pila = new LinkedList<Ambito>();
    private static int nuevo;
    
    public static void inicializarPila(Nodo cuerpoClase)
    {//instancia las variables globales dentro de la pila
        pila.clear();
        Ambito clase = new Ambito(-1, 0);
        pila.add(clase);
        //se busca por declaraciones dentro de la clase que contiene el metodo principal
        for (Nodo hijo : cuerpoClase.hijos)
        {
            switch (hijo.nombre)
            {
                case Const.declaracion:
                    //Semantico.declaracion(hijo);
                    break;
            }
        }//fin foreach
        nuevo = 1;
    }
    
    public static void agregarParametro(String nombre, int tipo, String valor)
    {
        Elemento elemento = new Elemento(nombre, tipo, valor);
        pila.get(pila.size() - 1).elementos.add(elemento);
    }

    public static void agregarElemeto(Nodo declaracion)
    {
        
    }
    
    public static void asignarValor(String nombre, Objeto valor)
    {
        //se debe preguntar antes si el valor se puede asignar a esa variable
        Ambito pos = buscarPosicion(nuevo - 1, nombre);
        pila.get(pos.padre).elementos.get(pos.actual).valor = valor.valor;
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
