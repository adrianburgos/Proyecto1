package Generadores;

import java.io.File;

public class GenerarArchivos {

    public static void generarLexico(String ruta)
    {
        File file = new File(ruta);
        jflex.Main.generate(file);
    }
    
    public static void generarSintactico()
    {
        generarSintacticoHaskell();
    }
    
    private static void generarSintacticoHaskell()
    {
        String[] cadena = new String[7];
        cadena[0] = "-destdir";
        cadena[1] = "src/Analisis";
        cadena[2] = "-symbols";
        cadena[3] = "SimbolosHaskell";
        cadena[4] = "-parser";
        cadena[5] = "SintacticoHaskell";
        cadena[6] = "src/Analisis/SintacticoHaskell.cup";
        try {
            java_cup.Main.main(cadena);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
