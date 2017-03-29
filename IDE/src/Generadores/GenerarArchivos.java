package Generadores;

import java.io.File;

public class GenerarArchivos {
    
    public static void main(String[] args) {
        generarLexico("src/Analisis/terminal/LexicoHaskellTerminal.jflex");
        generarLexico("src/Analisis/haskell/LexicoHaskell.jflex");
        generarLexico("src/Analisis/graphik/LexicoGraphik.jflex");
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
        generarSintacticoGraphik();
        generarSintacticoHaskellTerminal();
    }
    
    private static void generarSintacticoHaskellTerminal()
    {
        String[] cadena = new String[7];
        cadena[0] = "-destdir";
        cadena[1] = "src/Analisis/terminal";
        cadena[2] = "-symbols";
        cadena[3] = "symsHT";
        cadena[4] = "-parser";
        cadena[5] = "SintacticoHaskellTerminal";
        cadena[6] = "src/Analisis/terminal/SintacticoHaskellTerminal.cup";
        try {
            java_cup.Main.main(cadena);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void generarSintacticoHaskell() {
        String[] cadena = new String[7];
        cadena[0] = "-destdir";
        cadena[1] = "src/Analisis/haskell";
        cadena[2] = "-symbols";
        cadena[3] = "symsH";
        cadena[4] = "-parser";
        cadena[5] = "SintacticoHaskell";
        cadena[6] = "src/Analisis/haskell/SintacticoHaskell.cup";
        try {
            java_cup.Main.main(cadena);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void generarSintacticoGraphik() {
        String[] cadena = new String[7];
        cadena[0] = "-destdir";
        cadena[1] = "src/Analisis/graphik";
        cadena[2] = "-symbols";
        cadena[3] = "symsG";
        cadena[4] = "-parser";
        cadena[5] = "SintacticoGraphik";
        cadena[6] = "src/Analisis/graphik/SintacticoGraphik.cup";
        try {
            java_cup.Main.main(cadena);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
