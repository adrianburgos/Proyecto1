package semanticos;

import fabrica.Nodo;
import ide.Const;
import java.util.LinkedList;

public class Pila {
    private static LinkedList<Ambito> pila = new LinkedList<Ambito>();
    private static int nuevo;
    
    public static void inicializarPila(Nodo cuerpoGeneral)
    {//instancia las variables globales dentro de la pila
        pila.clear();
        Ambito decGlobales = new Ambito(-1, 0);
        pila.add(decGlobales);
        for (Nodo hijo : cuerpoGeneral.hijos)
        {
            switch (hijo.nombre)
            {
                case Const.declaracion:
                    agregarDeclaracion(hijo);
                    break;
            }
        }//fin foreach
        nuevo = 1;
    }

    public static void agregarDeclaracion(Nodo declaracion)
    {
        String tipo = declaracion.nombre;
        String nombre = declaracion.nombre;
        int tipoInt = Semantico.getTipo(tipo);
        Objeto valor = new Objeto(tipoInt, null);
        if(declaracion.hijos.size() > 0)
        {//la declaracion trae una asignacion o una lista de variables
            
            valor = Semantico.ejecutarValor(declaracion.hijos.get(0));
        }
    }
}
