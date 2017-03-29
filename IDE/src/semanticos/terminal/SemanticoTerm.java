package semanticos.terminal;

import Reportes.ErroresGraphik;
import Reportes.ErroresHaskell;
import fabrica.Nodo;
import ide.Const;
import semanticos.Objeto;
import semanticos.Semantico;
import static semanticos.Semantico.getTipo;
import static semanticos.Semantico.quitarComillas;
import static semanticos.Semantico.getTipo;

public class SemanticoTerm {
    public static Objeto ejecutarValor(Nodo valor)
    {
        Objeto valIzq, valDer;
        Objeto res = new Objeto();
        switch(valor.getNombre())
        {
            case Const.mod:
            case Const.sqrt:
                valIzq = ejecutarValor(valor.hijos.get(0));
                valDer = ejecutarValor(valor.hijos.get(1));
                res = ejecutarOpTerm(valIzq, valDer, valor.getNombre());
                break;
            default:
                //retornar el valor
                return new Objeto(valor.tipo, valor.valor);
        }
        if (res.tipo == Const.terror)
        {
            ErroresGraphik.agregarError("Error semantico", res.valor, 0, 0);
            res.tipo = Const.terror;
            res.valor = "";
        }
        return res;
    }

    private static Objeto ejecutarOpTerm(Objeto valIzq, Objeto valDer, String operador) {
        Objeto res = new Objeto();
        switch (operador)
        {
            case Const.mod:
                switch (valDer.tipo + valIzq.tipo)
                {
                    case 6://decimal mod decimal
                        res.tipo = Const.tdecimal;
                        res.valor = Double.valueOf(valIzq.valor) % Double.valueOf(valDer.valor) + "";
                        break;
                    case 10://decimal + caracter
                        res.tipo = Const.tdecimal;
                        double dec1 = 0;
                        double dec2 = 0;
                        if(valIzq.tipo == 1)
                        {
                            dec1 = Double.valueOf(valIzq.valor);
                            dec2 = (int) valDer.valor.charAt(0);
                        }
                        else
                        {
                            dec1 = (int) valIzq.valor.charAt(0);
                            dec2 = Double.valueOf(valDer.valor);
                        }
                        res.valor = dec1 % dec2 + "";
                        break;
                    case 18://decimal mod Bool
                    case 13://decimal mod cadena
                    case 14://caracter mod caracter
                    case 17://caracter mod cadena
                    case 20://cadena mod cadena
                    case 22://caracter mod Bool
                    case 25://cadena mod Bool
                    case 30://Bool + Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] mod [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case mod
            case Const.sqrt:
                switch (valDer.tipo + valIzq.tipo)
                {
                    case 6://decimal sqrt decimal
                        res.tipo = Const.tdecimal;
                        if(Double.valueOf(valIzq.valor) != 0)
                            res.valor = Math.pow(Double.valueOf(valDer.valor) , 1/Double.valueOf(valIzq.valor)) + "";
                        else
                        {
                            res.tipo = Const.terror;
                            res.valor = "Sqrt entre 0";
                        }
                        break;
                    case 10://decimal + caracter
                        res.tipo = Const.tdecimal;
                        double dec1 = 0;
                        double dec2 = 0;
                        if(valIzq.tipo == 1)
                        {
                            dec1 = Double.valueOf(valIzq.valor);
                            dec2 = (int) valDer.valor.charAt(0);
                        }
                        else
                        {
                            dec1 = (int) valIzq.valor.charAt(0);
                            dec2 = Double.valueOf(valDer.valor);
                        }
                        if(dec1 != 0)
                            res.valor =  Math.pow(dec2, 1/dec1) + "";
                        else
                        {
                            res.tipo = Const.terror;
                            res.valor = "Sqrt entre 0";
                        }
                        break;
                    case 18://decimal sqrt Bool
                    case 13://decimal sqrt cadena
                    case 14://caracter sqrt caracter
                    case 17://caracter sqrt cadena
                    case 20://cadena sqrt cadena
                    case 22://caracter sqrt Bool
                    case 25://cadena sqrt Bool
                    case 30://Bool + Bool
                        res.tipo = Const.terror;
                        res.valor = "No se pudo operar [" + getTipo(valIzq.tipo) + "] sqrt [" + getTipo(valDer.tipo) + "]";
                        break;
                }
                break;//case sqrt
        }
        return res;
    }

    static Objeto succ(Nodo succ) {
        Objeto res;
        res = EjecutarTerm.ejecutarInst(succ.hijos.get(0));
        res.valor = Double.valueOf(res.valor) + 1 + "";
        return res;
    }

    static Objeto decc(Nodo decc) {
        Objeto res;
        res = EjecutarTerm.ejecutarInst(decc.hijos.get(0));
        res.valor = Double.valueOf(res.valor) - 1 + "";
        return res;
    }

    static Objeto decList(Nodo list) {
        Objeto res = new Objeto();
        Nodo dim1 = list.hijos.get(0);
        Objeto valores = new Objeto();
        switch(dim1.nombre)
        {
            case Const.lista:
                valores = recorrerLista(dim1);
                res.tipo = valores.tipo;
                res.lvalores.addAll(valores.lvalores);
                res.dim.add(valores.dim.get(0));
                break;
            case Const.lcorchetes:
                valores = recorrerCorchetes(dim1);
                res.tipo = valores.tipo;
                res.lvalores.addAll(valores.lvalores);
                res.dim.add(res.lvalores.size());
                res.dim.add(valores.dim.get(0));
                break;
        }
        return res;
    }

    private static Objeto recorrerLista(Nodo dim) {
        Objeto valores = new Objeto();
        Integer tamDimen = 0;
        for(Nodo val : dim.hijos)
        {
            Objeto res = EjecutarTerm.ejecutarInst(val);
            tamDimen++;
            valores.tipo = res.tipo;
            valores.lvalores.add(res);
        }
        valores.dim.add(tamDimen);
        return valores;
    }

    private static Objeto recorrerCorchetes(Nodo dim) {
        Objeto valores = new Objeto();
        Objeto valor = new Objeto();
        Integer tamDimen = -1;
        for(Nodo val : dim.hijos)
        {
            valor = recorrerLista(val);
            if(tamDimen == -1)
            {
                valores.tipo = valor.tipo;
                tamDimen = valor.dim.get(0);
                valores.lvalores.add(valor);
            }
            else
                if(tamDimen == valor.dim.get(0))
                    if(valores.tipo == valor.tipo)
                        valores.lvalores.add(valor);
                    else
                    {
                        String error = "Los datos de la lista son de distintos tipos [" + Semantico.getTipo(valores.tipo) + "] y [" + Semantico.getTipo(valor.tipo) + "]";
                        ErroresHaskell.agregarError("Error semantico", error, 0, 0);
                    }
                else
                {
                    String error = "Las dimensiones de la lista no coinciden [" + tamDimen + "] con [" + valor.dim.get(0) + "]";
                    ErroresHaskell.agregarError("Error semantico", error, 0, 0);
                }
        }
        valores.dim.add(tamDimen);
        return valores;
    }

    static Objeto sum(Nodo raiz) {
        
    }

    static Objeto product(Nodo raiz) {
        
    }

    static Objeto max(Nodo raiz) {
        
    }

    static Objeto length(Nodo raiz) {
        
    }
}
