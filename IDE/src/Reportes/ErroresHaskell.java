package Reportes;

import java.io.FileWriter;
import java.io.PrintWriter;

public class ErroresHaskell {
    public static String html;
    private static String errores = "";
    public static int contErrores = 0;

    private static void encabezado()
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
        html += "<th> Tipo </th>\n";
        html += "<th> Descripcion </th>\n";
        html += "<th> Fila </th>\n";
        html += "<th> Columna </th>\n";
        html += "</tr>\n";
    }

    private static void cierres()
    {
        html += "</div>\n";
        html += "</table>\n";
        html += "</body>\n";
        html += "</html>\n";
    }
    
    /**
     * Agrega un error que fue provocado por una ejecucion de Haskell++
     * @param tipo identifica si el errores es léxico, sintático o semántico
     * @param descripcion detalla el motivo del error
     * @param fila la fila en que ocurrio el error
     * @param columna la columna en que ocurrio el error
     */
    public static void agregarError(String tipo, String descripcion, int fila, int columna)
    {
        errores += "<tr>\n";
        errores += "<td>" + tipo + "</td>\n";
        errores += "<td>" + descripcion + "</td>\n";
        errores += "<td>" + fila + "</td>\n";
        errores += "<td>" + columna + "</td>\n";
        errores += "</tr>\n";
        contErrores++;
    }


    /**
     * @return Se obtiene la sintaxis HTML completa para mostrar el reporte de errores
     */
    public static String getHTML()
    {
        encabezado();
        html += errores;
        cierres();
        errores = "";
        return html;
    }
    
    public static void generarErrores(){
        try
        {
            FileWriter fw = new FileWriter("C:\\Users\\AdrianFernando\\Desktop\\erroresHaskell.html");
            PrintWriter pw = new PrintWriter(fw);
            pw.print(getHTML());
            fw.close();

        } catch (Exception e) {
            e.printStackTrace(); 
        }
        html = "";
        errores = "";
        contErrores = 0;
    }
}
