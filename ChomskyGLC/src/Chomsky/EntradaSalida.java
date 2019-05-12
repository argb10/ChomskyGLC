package Chomsky;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Ivan
 */
public class EntradaSalida {

    public static ArrayList<Implicacion> lecturaFichero() {
        ArrayList<Implicacion> resultado= new ArrayList<Implicacion>();
        File ficheroentrada = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            ficheroentrada = new File("entradaSalida.txt");
            fr = new FileReader(ficheroentrada);
            br = new BufferedReader(fr);

            //lectura de fichero
            String linea;
            while((linea=br.readLine())!= null){
                String[] a = linea.split(" ");
                String implicante=null;
                ArrayList<String> implicados=new ArrayList<String>();
                for(int i=0; i < a.length;i++){
                    if(i==0)
                        implicante=a[i];

                    else
                        implicados.add(a[i]);
                }
                Implicacion imp=new Implicacion(implicante,implicados);
                resultado.add(imp);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(null != fr)
                    fr.close();
            }
            catch(Exception e2){
                e2.printStackTrace();
            }
        }
        return resultado;
    }
    public void devolverResultado(ArrayList<Implicacion> resultado){
        for(int i=0;i<resultado.size();i++){
            System.out.print(resultado.get(i).getImplicante() + " -> ");
            for(int j=0;j<resultado.get(i).getImplicados().size();j++){
                if(j!=(resultado.get(i).getImplicados().size()-1)){
                    System.out.print(resultado.get(i).getImplicados().get(j) + "|");
                }else{
                    System.out.print(resultado.get(i).getImplicados().get(j));
                }
            }
            System.out.print("\n");
        }
    }
    
}

    

