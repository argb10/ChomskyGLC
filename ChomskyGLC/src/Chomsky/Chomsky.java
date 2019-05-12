package Chomsky;

import java.util.ArrayList;

/**
 *
 * @author Ivan
 */
public class Chomsky {

    /**
     *
     */
    private ArrayList<Implicacion> implicaciones;
    private ArrayList<Implicacion> solucion;

    public void transformacion() {
        solucion = new ArrayList<>();
        EntradaSalida es = new EntradaSalida();
        //lectura de fichero
        implicaciones = es.lecturaFichero();
        System.out.println(" --------------- GRAMATICA INICIAL--------------- ");
        es.devolverResultado(implicaciones);
        //paso 1: crear nuevo estado inicial
        generarS0();
        insertarImplicacionesEnSol();
        //paso 2: eliminar producciones lambda
        eliminarProduccionesLambda();
        System.out.println(" --------------- GRAMATICA SIN LAMBDA --------------- ");
        es.devolverResultado(solucion);
        //paso 3: eliminar reglas unitarias
        eliminarReglasUnitarias();
        System.out.println(" --------------- GRAMATICA SIN REGLAS UNITARIAS --------------- ");
        es.devolverResultado(solucion);
        int contador = 1;
        String imp="X";
//      paso 4: quitar implicantes tamaño superior a 2
        contador = eliminarTermAcompañados(contador,imp);
        System.out.println(" --------------- GRAMATICA SIN TERMINALES ACOMPAÑADOS --------------- ");
        es.devolverResultado(solucion);
        contador = eliminarImpGrandes(contador, imp);
        System.out.println(" --------------- GRAMATICA SIN EXPRESIONES LARGAS --------------- ");
        es.devolverResultado(solucion);
        //imprimir resultado
        System.out.println(" --------------- GRAMATICA EN FORMA NORMAL DE CHOMSKY --------------- ");
        es.devolverResultado(solucion);

    }

    private void generarS0() {
        ArrayList<String> implicacionesDeS0 = new ArrayList<>();
        String implica = "S";
        implicacionesDeS0.add(implica);
        Implicacion S0 = new Implicacion("S0", implicacionesDeS0);
        solucion.add(S0);
    }

    private void insertarImplicacionesEnSol() {
        for (Implicacion aux : implicaciones) {
            solucion.add(aux);
        }
    }

    private void eliminarProduccionesLambda() {
        ArrayList<String> impLambda = new ArrayList<>();
        impLambda.add("#");
        boolean modificado;
        //detectar cuales son las implicaciones que producen lambda
        do {
            modificado = false;
            for (Implicacion imp : solucion) {
                if (imp.generaLambda(impLambda)) {
                    String aux = imp.getImplicante();
                    if (!impLambda.contains(aux)) {
                        impLambda.add(aux);
                        modificado = true;
                    }
                }
            }
        } while (modificado);
        //eliminar producciones Lambda de implicaciones
        for (Implicacion aux : solucion) {
            aux.quitarLambdas(impLambda);
        }
    }

    private void eliminarReglasUnitarias() {
        //eliminar producciones unitarias
        for (int i = solucion.size() - 1; i >= 0; i--) {
            Implicacion aux = solucion.get(i);
            while (aux.tieneUnitaria()) {
                aux.quitarUnitarias(solucion);
            }
        }
    }

    private int eliminarTermAcompañados(int conta, String  imp) {
        ArrayList<Implicacion> nImp = new ArrayList<>();
        for (int i = 0; i < solucion.size(); i++) {
            ArrayList<Implicacion> aux = new ArrayList<>();
            aux = solucion.get(i).eliminarTermAcompañados(conta, imp);
            conta += aux.size();
            for (Implicacion impl : aux) {
                nImp.add(impl);
            }
        }
        for (Implicacion impl : nImp) {
            solucion.add(impl);
        }
        return conta;
    }

    private int eliminarImpGrandes(int conta, String imp) {
        ArrayList<Implicacion> nImp = new ArrayList<>();
        for (int i = 0; i < solucion.size(); i++) {
            ArrayList<Implicacion> aux = new ArrayList<>();
            aux = solucion.get(i).eliminarImpGrandes(imp,conta);
            conta += aux.size();
            for (Implicacion impl : aux) {
                nImp.add(impl);
            }
        }
        for (Implicacion impl : nImp) {
            solucion.add(impl);
        }
        return conta;
    }

}
