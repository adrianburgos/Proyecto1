/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import ide.Const;
import ide.Fila;
import ide.Principal;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Adrian
 */
public class ReporteProcesar {
    public static String html;
    private static String cuerpo = "";

    private static void encabezado(String tipo)
    {
        html = "<html>\n";
        html += "<head>\n";
        html += "<title> Errores </title>\n";
        html += "</head>\n";
        html += "<body style=\"background-color: black; \">\n";
        html += "<div align = \"center\"  style=\"color:white\">\n";
        html += "<h1> Errores </h1>\n";
        html += "<table border = \"1\" style=\"color:white\">\n";
        html += "<tr>\n";
        html += "<th> " + tipo + " </th>\n";
        html += "<th> Procesar </th>\n";
        html += "</tr>\n";
    }

    private static void cierres()
    {
        html += "</div>\n";
        html += "</table>\n";
        html += "</body>\n";
        html += "</html>\n";
    }
    
    public static void agregarDatos()
    {
        for(Fila f : Principal.procesar)
        {
            cuerpo += "<tr>";
            cuerpo += "<td> " + f.datos.get(0).valor + "</td>";
            cuerpo += "<td> " + f.datos.get(1).valor + "</td>";
            cuerpo += "</tr>";
        }
    }


    /**
     * @return Se obtiene la sintaxis HTML completa para mostrar el reporte de errores
     */
    public static String getHTML(String tipo)
    {
        encabezado(tipo);
        agregarDatos();
        html += cuerpo;
        cierres();
        cuerpo = "";
        return html;
    }
    
    public static void generarProcesar(String tipo){
        try
        {
            FileWriter fw = new FileWriter("C:\\Users\\AdrianFernando\\Desktop\\ReporteProcesar.html");
            PrintWriter pw = new PrintWriter(fw);
            pw.print(getHTML(tipo));
            fw.close();
            Runtime run = Runtime.getRuntime();
            Process pro = null;
            String ejec [] = {"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", "C:\\Users\\AdrianFernando\\Desktop\\ReporteProcesar.html"};
            try {
                pro = run.exec(ejec);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace(); 
        }
        html = "";
        cuerpo = "";
    }
}
