package Chomsky;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;

import static java.util.Collections.list;

/**
 * @author Ivan
 */
public class Implicacion {

    private String implicante;
    private ArrayList<String> implicados;
    private ArrayList<String> unitarias;

    public Implicacion(String izquierda, ArrayList<String> derecha) {
        this.implicante = izquierda;
        this.implicados = derecha;
        this.unitarias = new ArrayList<>();
    }

    public String getImplicante() {
        return implicante;
    }

    public ArrayList<String> getImplicados() {
        return implicados;
    }

    public void addImplicado(String implicado) {
        this.implicados.add(implicado);
    }

    boolean generaLambda(ArrayList<String> impLambda) {
        boolean genera = false;
        for (String lambda : impLambda) {
            for (String s : implicados) {
                if (s.indexOf(lambda) != -1 && !genera) {
                    genera = true;
                }
            }
        }
        return genera;
    }

    void quitarLambdas(ArrayList<String> impLambda) {
        //insertar nuevos implicados
        String nuevo = new String();
        for (String lambda : impLambda) {
            for (int i = 0; i < implicados.size(); i++) {
                String s = implicados.get(i);
                int indice = s.indexOf(lambda);
                // si es unitario y pertenece al conjunto que genera lambda, se inserta lambda
                if (indice != -1 && s.length() == 1 && implicados.get(i) != "#") {
                    if (!implicados.contains("#")) {
                        implicados.add("#");
                    }
                } //si no es unitario y pertenece al conjunto que genera lambda, se genera una nueva secuencia sin el caracter
                else if (indice != -1 && s.length() != 1 && implicados.get(i) != "#") {
                    if (indice != 0 && indice != s.length() - 1) {
                        String n1 = s.substring(0, indice);
                        String n2 = s.substring(indice + 1, s.length());
                        nuevo = n1.concat(n2);
                    } else if (indice == 0) {
                        nuevo = s.substring(1);
                    } else if (indice == s.length() - 1) {
                        nuevo = s.substring(0, indice);
                    }
                    if (!implicados.contains(nuevo)) {
                        implicados.add(nuevo);
                    }
                }

            }
        }
        // eliminar caracter lambda
        int tamaño = implicados.size();
        if (this.implicante != "S0") {
            for (int i = 0; i < tamaño; i++) {
                String s = implicados.get(i);
                if (s.equals("#")) {
                    int index = implicados.indexOf(s);
                    implicados.remove(index);
                    tamaño--;
                }
            }
        }
    }

    //comprueba si implicado es unitario, si lo es, lo añade en una lista
    //se comprueba a la vez si esa lista esta vacia, si lo esta es que no tiene unitarias
    public boolean tieneUnitaria() {
        for (int i = 0; i < implicados.size(); i++) {
            String s = implicados.get(i);
            if (s.length() == 1 && s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
                if (!unitarias.contains(s)) {
                    unitarias.add(s);
                }
            }
        }
        return !(unitarias.isEmpty());
    }

    public void quitarUnitarias(ArrayList<Implicacion> implicaciones) {
        //eliminar redundantes
        for (int i = 0; i < unitarias.size(); i++) {
            String s = unitarias.get(i);
            if (s.equals(implicante)) {
                int index = implicados.indexOf(s);
                implicados.remove(index);
                unitarias.remove(i);
            }
        }
        //eliminar resto unitarias
        for (int i = 0; i < unitarias.size(); i++) {
            String s = unitarias.get(i);
            for (Implicacion implicacion : implicaciones) {
                if (s.equals(implicacion.implicante)) {
                    ArrayList<String> nuevosimplicados = implicacion.getImplicados();
                    for (String implicado : nuevosimplicados) {
                        this.implicados.add(implicado);
                    }
                    int index = implicados.indexOf(s);
                    implicados.remove(index);
                    unitarias.remove(i);
                }

            }
        }
    }

    ArrayList<Implicacion> eliminarTermAcompañados(int contador, String imp) {
        ArrayList<Implicacion> nuevasImplicaciones = new ArrayList<>();
        for (int j = 0; j < this.implicados.size(); j++) {
            String s = this.implicados.get(j);
            if (s.length() >= 2) {
                for (int i = 0; i < s.length(); i++) {
                    if (Character.isLowerCase(s.charAt(i))) { //compruebo que el caracter es un terminal
                        String antiguo = s.substring(i, i + 1);
                        ArrayList<String> implicantesNuevos = new ArrayList<>();
                        implicantesNuevos.add(antiguo);
                        String nuevo = imp + contador;
                        s = s.substring(0, i) + nuevo + s.substring(i + 1, s.length());
                        Implicacion implicacion = new Implicacion(nuevo, implicantesNuevos); //genero una nueva produccion
                        nuevasImplicaciones.add(implicacion);
                        contador++;
                    }
                }
                this.implicados.set(j, s);
            }
        }
        return nuevasImplicaciones;
    }

    ArrayList<Implicacion> eliminarImpGrandes(String imp, int subindex) {

        ArrayList<Implicacion> nuevasImplicaciones = new ArrayList<>();
        for (int i = 0; i < this.implicados.size(); i++) {
            int count = 0;
            String s= this.implicados.get(i);
            for (int j = 0; j < s.length(); j++)//no necesito comprobar casos: a, AB porque el count no llega a 3
            {
                if (count == 3)//reemplazar si tenemos 3 variables
                {
                    count = 0;
                    String nuevo = reemplazar(s, nuevasImplicaciones, imp, subindex);//esta funcion reemplaza dos variables
                    subindex++;
                }// OJO AL TERMINAR REEMPLAZAR HAY QUE VOLVER SOBRE EL MISMO STRING
                if (!Character.isLetter(s.charAt(j)))//si es una letra aumentar
                {
                    count++;
                }
            }
             this.implicados.set(i, s);
        }
        return nuevasImplicaciones;
    }


     private String reemplazar(String sOld, ArrayList<Implicacion> nuevasImplicaciones, String cab, int index) {
        int i = 0;
        int j;
        String primero = new String();
        String segundo = new String();
        primero  = "";
        segundo = "";
        boolean buscasig = true;
        for (j = 0; j < sOld.length() && buscasig == true; j++) { //buscar el primer implicado
            primero += sOld.charAt(j);                            //Se guarda la letra

            if (Character.isDigit(sOld.charAt(j++))) { //se comprueba que el siguiente sea un digito
                buscasig = true;                       //Si es digito se guarda
            }else{
                buscasig = false;                       //Si no es, se sigue con el segundo implicado
            }
        }

        buscasig = true;

        for(; j < sOld.length() && buscasig == true; j++){ //buscar el segundo implicado (igual al anterior)
            segundo += sOld.charAt(j);

            if (Character.isDigit(sOld.charAt(j++))) {
                buscasig = true;
            }else{
                buscasig = false;
            }
        }

        int k = j;

        if (k != sOld.length() - 1) {
            String implicante = cab + index;
            String implicado = sOld.substring(i, k);
            ArrayList<String> nuevosImplicados = new ArrayList<>();
            nuevosImplicados.add(implicado);
            Implicacion imp = new Implicacion(implicante, nuevosImplicados);
            nuevasImplicaciones.add(imp);
        }
        return sOld;
    }

}
