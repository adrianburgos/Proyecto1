package Generadores;

import java.io.File;

public class GenerarArchivos {
    
    public static void main(String[] args) {
        generarLexico("src/Analisis/LexicoHaskellTerminal.jflex");
        generarSintactico();
    }

    private static void generarLexico(String ruta)
    {
        File file = new File(ruta);
        jflex.Main.generate(file);
    }
    
    private static void generarSintactico()
    {
        generarSintacticoHaskell();
    }
    
    private static void generarSintacticoHaskell()
    {
        String[] cadena = new String[7];
        cadena[0] = "-destdir";
        cadena[1] = "src/Analisis";
        cadena[2] = "-symbols";
        cadena[3] = "symsHT";
        cadena[4] = "-parser";
        cadena[5] = "SintacticoHaskellTerminal";
        cadena[6] = "src/Analisis/SintacticoHaskellTerminal.cup";
        try {
            java_cup.Main.main(cadena);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
