package fabrica;

import ide.Const;

public class NodoOperacion {
    
    public static Nodo crearNodo(String nombre, Nodo op1, Nodo op2)
    {
        switch(nombre)
        {
            case Const.mas:
                return crearSuma(op1, op2);
            case Const.menos:
                return crearResta(op1, op2);
            case Const.por:
                return crearMultiplicacion(op1, op2);
            case Const.dividido:
                return crearDivision(op1, op2);
            case Const.mod:
                return crearModulo(op1, op2);
            case Const.pot:
                return crearPotencia(op1, op2);
            case Const.sqrt:
                return crearRaiz(op1, op2);
            case Const.unario:
                return crearUnario(op1);
            case Const.and:
                return crearAnd(op1, op2);
            case Const.or:
                return crearOr(op1, op2);
        }
        System.out.println("crearNodoOperacion retorno null");
        return null;
    }
    
    private static Nodo crearSuma(Nodo op1, Nodo op2)
    {
        Nodo suma = new Nodo(Const.mas, 1, 0, 0);
        suma.hijos.add(op1);
        suma.hijos.add(op2);
        return (Nodo) suma;
    }
    
    private static Nodo crearResta(Nodo op1, Nodo op2)
    {
        Nodo resta = new Nodo(Const.menos, 1, 0, 0);
        resta.hijos.add(op1);
        resta.hijos.add(op2);
        return (Nodo) resta;
    }
    private static Nodo crearMultiplicacion(Nodo op1, Nodo op2)
    {
        Nodo multiplicacion = new Nodo(Const.por, 1, 0, 0);
        multiplicacion.hijos.add(op1);
        multiplicacion.hijos.add(op2);
        return (Nodo) multiplicacion;
    }
    private static Nodo crearDivision(Nodo op1, Nodo op2)
    {
        Nodo division = new Nodo(Const.dividido, 1, 0, 0);
        division.hijos.add(op1);
        division.hijos.add(op2);
        return (Nodo) division;
    }
    
    private static Nodo crearModulo(Nodo op1, Nodo op2)
    {
        Nodo modulo = new Nodo(Const.mod, 1, 0, 0);
        modulo.hijos.add(op1);
        modulo.hijos.add(op2);
        return (Nodo) modulo;
    }
    
    private static Nodo crearPotencia(Nodo op1, Nodo op2)
    {
        Nodo potencia = new Nodo(Const.pot, 1, 0, 0);
        potencia.hijos.add(op1);
        potencia.hijos.add(op2);
        return (Nodo) potencia;
    }
    
    private static Nodo crearRaiz(Nodo op1, Nodo op2)
    {
        Nodo raiz = new Nodo(Const.sqrt, 1, 0, 0);
        raiz.hijos.add(op1);
        raiz.hijos.add(op2);
        return (Nodo) raiz;
    }

    private static Nodo crearUnario(Nodo op1) {
        Nodo raiz = new Nodo(Const.unario);
        raiz.hijos.add(op1);
        return (Nodo) raiz;
    }
    
    private static Nodo crearAnd(Nodo op1, Nodo op2) {
        Nodo raiz = new Nodo(Const.and);
        raiz.hijos.add(op1);
        raiz.hijos.add(op2);
        return (Nodo) raiz;
    }
    
    private static Nodo crearOr(Nodo op1, Nodo op2) {
        Nodo raiz = new Nodo(Const.or);
        raiz.hijos.add(op1);
        raiz.hijos.add(op2);
        return (Nodo) raiz;
    }
    
    public static Nodo crearRelacional(String op, Nodo op1, Nodo op2) {
        Nodo raiz = new Nodo(op);
        raiz.hijos.add(op1);
        raiz.hijos.add(op2);
        return (Nodo) raiz;
    }
}
