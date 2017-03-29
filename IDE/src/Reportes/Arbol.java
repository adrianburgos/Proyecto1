package Reportes;

import fabrica.Nodo;
import ide.Const;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Arbol {
    public static int contador;
    public static String grafo;
    
    public static String getGrafo(Nodo raiz)
    {
        grafo = "digraph G{\n";
        grafo += "node [shape = box, color = lightgray, fontcolor = black, style = filled];\n";
        grafo += "style = filled;\n";
        grafo += "bgcolor = black;\n";
        grafo += "orientatio = landscape;\n";
        grafo += "center = true;\n";
        grafo += "edge [arrowhead = odot, color = red];\n";
        grafo += "label = \" Dibujar Expresion \";\n";
        if(!raiz.getNombre().equals(raiz.getValor()))
            grafo += "nodo0 [label=\"" + agregarComillas(raiz.getValor()) + "[" + agregarComillas(raiz.getNombre()) + "]\"];\n";
        else
            grafo += "nodo0 [label=\"" + agregarComillas(raiz.getNombre()) + "\"];\n";
            
        contador = 1;
        recorrerAST("nodo0", raiz);
        grafo += "}";
        return grafo;
    }

    private static void recorrerAST(String padre, Nodo nodo)
    {
        for (Nodo hijo : nodo.hijos)
        {
            String nombreHijo = "nodo" + contador;
            String label = "";
            
            if(!hijo.getNombre().equals(hijo.getValor()))
                label += agregarComillas(hijo.getValor()) + "[" + agregarComillas(hijo.getNombre()) + "]";
            else
                label = agregarComillas(hijo.getNombre());
            grafo += nombreHijo + " [label=\"" + label + "\"];\n";
            grafo += padre + " -> " + nombreHijo + ";\n";
            contador++;
            recorrerAST(nombreHijo, hijo);
        }
    }
    
    public static String agregarComillas(String cadena)
    {
        cadena = cadena.replace("\\", "\\\\");
        cadena = cadena.replace("\"", "\\\"");
        return cadena;
    }
	
    public static void dibujar(){
        try
        {
            FileWriter fw = new FileWriter("C:\\Users\\AdrianFernando\\Desktop\\dot.dot");
            PrintWriter pw = new PrintWriter(fw);
            pw.print(grafo);
            fw.close();

            String direccionPng = "C:\\Users\\AdrianFernando\\Desktop\\imagen.png";
            String direccionDot = "C:\\Users\\AdrianFernando\\Desktop\\dot.dot";
            ProcessBuilder pbuilder;

            /*
             * Realiza la construccion del comando    
             * en la linea de comandos esto es: 
             * dot -Tpng -o archivo.png archivo.dot
             */
            pbuilder = new ProcessBuilder( "dot", "-Tpng", "-o", direccionPng, direccionDot );
            pbuilder.redirectErrorStream( true );
            //Ejecuta el proceso
            pbuilder.start();

        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
}
