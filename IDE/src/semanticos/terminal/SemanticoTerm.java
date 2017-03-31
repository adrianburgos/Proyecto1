package semanticos.terminal;

import Reportes.ErroresGraphik;
import Reportes.ErroresHaskell;
import fabrica.Nodo;
import ide.Const;
import semanticos.EjecutarArbol;
import semanticos.Elemento;
import semanticos.Objeto;
import semanticos.Pila;
import semanticos.Semantico;
import static semanticos.Semantico.ejecutarValor;
import static semanticos.Semantico.getTipo;
import static semanticos.Semantico.quitarComillas;
import static semanticos.Semantico.getTipo;
import semanticos.TipoRetorno;

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
        res.tipo = Const.tdecimal;
        return res;
    }

    static Objeto decc(Nodo decc) {
        Objeto res;
        res = EjecutarTerm.ejecutarInst(decc.hijos.get(0));
        res.valor = Double.valueOf(res.valor) - 1 + "";
        res.tipo = Const.tdecimal;
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

    static Objeto sum(Nodo nodo) {
        Objeto res = new Objeto();
        Nodo sum = nodo.hijos.get(0);
        Objeto valores = new Objeto();
        double suma = 0;
        switch(sum.nombre)
        {
            case Const.lista:
                valores = recorrerLista(sum);
                break;
            case Const.lcorchetes:
                valores = recorrerCorchetes(sum);
                break;
            case Const.id:
                Elemento ele = PilaHaskell.buscar(sum.valor);
                valores.dim = ele.dim;
                valores.lvalores = ele.lvalores;
                break;
        }
        if(valores.dim.size() > 0)
        {
            for(Objeto obj : valores.lvalores)
            {
                if(obj.lvalores != null)
                    for(Objeto obj2 : obj.lvalores)
                        if(obj.tipo == Const.tcaracter)
                            suma += Double.valueOf((int) obj2.valor.charAt(0));
                        else
                            suma += Double.valueOf(obj2.valor);
                else
                    if(obj.tipo == Const.tcaracter)
                        suma += Double.valueOf((int) obj.valor.charAt(0));
                    else
                        suma += Double.valueOf(obj.valor);
                    
            }
        }
        else
        {
            String error = "[" + sum.valor + "] no es una Lista";
            ErroresHaskell.agregarError("Error semantico", error, 0, 0);
        }
        res.valor = suma + "";
        res.tipo = Const.tdecimal;
        return res;
    }

    static Objeto product(Nodo nodo) {
        Objeto res = new Objeto();
        Nodo product = nodo.hijos.get(0);
        Objeto valores = new Objeto();
        double prod = 1;
        switch(product.nombre)
        {
            case Const.lista:
                valores = recorrerLista(product);
                break;
            case Const.lcorchetes:
                valores = recorrerCorchetes(product);
                break;
            case Const.id:
                Elemento ele = PilaHaskell.buscar(product.valor);
                valores.dim = ele.dim;
                valores.lvalores = ele.lvalores;
                break;
        }
        if(valores.dim != null )
        {
            for(Objeto obj : valores.lvalores)
            {
                if(obj.lvalores != null)
                    for(Objeto obj2 : obj.lvalores)
                        if(obj2.tipo == Const.tcaracter)
                            prod *= Double.valueOf((int) obj2.valor.charAt(0));
                        else
                            prod *= Double.valueOf(obj2.valor);
                else
                    if(obj.tipo == Const.tcaracter)
                        prod *= Double.valueOf((int) obj.valor.charAt(0));
                    else
                        prod *= Double.valueOf(obj.valor);
                    
            }
        }
        else
        {
            String error = "[" + product.valor + "] no es una Lista";
            ErroresHaskell.agregarError("Error semantico", error, 0, 0);
        }
        res.valor = prod + "";
        res.tipo = Const.tdecimal;
        return res;
    }

    static Objeto max(Nodo nodo) {
        Objeto res = new Objeto();
        Nodo max = nodo.hijos.get(0);
        Objeto valores = new Objeto();
        boolean esChar = false;
        double mayor = -99999;
        switch(max.nombre)
        {
            case Const.lista:
                valores = recorrerLista(max);
                break;
            case Const.lcorchetes:
                valores = recorrerCorchetes(max);
                break;
            case Const.id:
                Elemento ele = PilaHaskell.buscar(max.valor);
                valores.dim = ele.dim;
                valores.lvalores = ele.lvalores;
                break;
        }
        if(valores.dim != null )
        {
            for(Objeto obj : valores.lvalores)
            {
                if(obj.lvalores != null)
                {
                    for(Objeto obj2 : obj.lvalores)
                        if(obj2.tipo == Const.tcaracter)
                        {
                            esChar = true;
                            if(mayor < Double.valueOf((int) obj2.valor.charAt(0)))
                                mayor = Double.valueOf((int) obj2.valor.charAt(0));
                        }
                        else
                            if(mayor < Double.valueOf(obj2.valor))
                                mayor = Double.valueOf(obj2.valor);
                }
                else
                    if(obj.tipo == Const.tcaracter)
                    {
                        esChar = true;
                        if(mayor < Double.valueOf((int) obj.valor.charAt(0)))
                            mayor = Double.valueOf((int) obj.valor.charAt(0));
                    }
                    else
                        if(mayor < Double.valueOf(obj.valor))
                            mayor = Double.valueOf(obj.valor);
            }
        }
        else
        {
            String error = "[" + max.valor + "] no es una Lista";
            ErroresHaskell.agregarError("Error semantico", error, 0, 0);
        }
        if(esChar)
        {
            res.tipo = Const.tcaracter;
            res.valor = (char) Math.round(mayor) + "";
        }
        else
        {
            res.valor = mayor + "";
            res.tipo = Const.tdecimal;
        }
        return res;
    }

    static Objeto length(Nodo nodo) {
        Objeto res = new Objeto();
        Nodo len = nodo.hijos.get(0);
        Objeto valores = new Objeto();
        boolean esChar = false;
        double tam = 1;
        switch(len.nombre)
        {
            case Const.lista:
                valores = recorrerLista(len);
                break;
            case Const.lcorchetes:
                valores = recorrerCorchetes(len);
                break;
            case Const.id:
                Elemento ele = PilaHaskell.buscar(len.valor);
                valores.dim = ele.dim;
                valores.lvalores = ele.lvalores;
                break;
        }
        tam = valores.lvalores.size();
        if(valores.dim != null )
        {
            if(valores.lvalores.get(0).lvalores != null)
                tam *= valores.lvalores.get(0).lvalores.size();
        }
        else
        {
            String error = "[" + len.valor + "] no es una Lista";
            ErroresHaskell.agregarError("Error semantico", error, 0, 0);
        }
        res.valor = tam + "";
        res.tipo = Const.tdecimal;
        return res;
    }

    static Objeto poslista(Nodo nodo) {
        Objeto res = new Objeto();
        Nodo lista = nodo.hijos.get(0);
        Nodo pos = nodo.hijos.get(1);
        Objeto list = new Objeto();
        Objeto valor1 = new Objeto();
        Objeto valor2 = null;
        switch(lista.nombre)
        {
            case Const.lista:
                list = recorrerLista(lista);
                break;
            case Const.lcorchetes:
                list = recorrerCorchetes(lista);
                break;
            case Const.id:
                Elemento ele = PilaHaskell.buscar(lista.valor);
                list.dim = ele.dim;
                list.lvalores = ele.lvalores;
                break;
        }
        valor1 = EjecutarTerm.ejecutarInst(pos.hijos.get(0));
        if(pos.hijos.size() == 2)
            valor2 = EjecutarTerm.ejecutarInst(pos.hijos.get(1));
        int pos1 = (int) Math.round(Double.valueOf(valor1.valor));
        res = list.lvalores.get(pos1);
        if(res.lvalores != null && valor2 != null)
        {
            int pos2 = (int) Math.round(Double.valueOf(valor2.valor));
            res = res.lvalores.get(pos2);
        }
        return res;
    }

    static Objeto si(Nodo hijo) {
        Objeto retorno = new Objeto();
        Objeto obj = new Objeto();
        Nodo valor = hijo.hijos.get(0);
        Objeto condicion = ejecutarValor(valor);
        if (condicion.tipo == Const.tbool)
        {
            Pila.crearAmbito();
            if (condicion.valor.equals(Const.verdadero))
                retorno = EjecutarTerm.ejecutarInst(hijo.hijos.get(1));
            else
                if (hijo.hijos.size() == 3)
                    retorno = EjecutarTerm.ejecutarInst(hijo.hijos.get(2));
            Pila.eliminarAmbito();
        }
        else
            ErroresGraphik.agregarError("Error semantico", "La condicion de SI en Haskell no es de tipo Bool", 0,0);

        return retorno;
    }
}
