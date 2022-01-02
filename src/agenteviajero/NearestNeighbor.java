package agenteviajero;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author MambaNegraPC
 */
public class NearestNeighbor {

    private List<String[]> matrixProblem = new ArrayList<>();

    public NearestNeighbor(List<String[]> matrixProblem) {
        this.matrixProblem = matrixProblem;
    }

    public List<Integer> nodeTraversal(int node) {

        List<Integer> route = new ArrayList<>();

        int i = 0,
                minimumnode = 0,
                nextnode = node;
        
        float item,
              minimum = 0,
                costo = 0;
        
        //Iniciamos recorrido en el nodo propuesto
        while (i <= matrixProblem.size()) {

            //Cerrar ciclo
            if (i == matrixProblem.size()) {
                route.add(route.get(0));//Se cierra donde se inicia recorrido
                costo = costo + Float.parseFloat(matrixProblem.get(nextnode)[route.get(0)]);//Sumamos el costo total
                route.add(Math.round(costo));//Guardamos el costo total de la ruta en la última posición  

            } else {

                for (int j = 0; j < matrixProblem.get(nextnode).length; j++) {

                    //Evitamos visitar el mismo nodo
                    if (!(nextnode == j)) {

                        //Evitamos visitar nodos ya visitados
                        if (!route.contains(j)) {
                            
                            //String[] A = new String[101];
                            //A = matrixProblem.get(nextnode);
                            
                            item = Float.parseFloat(matrixProblem.get(nextnode)[j]);

                            if (minimum == 0) {
                                minimum = item;//Inicializamos minimum con el primer valor encontrado, para tener punto de referencia 
                                minimumnode = j;//Obtenemos el siguiente punto de partida, en caso de no encontrar uno mejor
                            } else {
                                //Buscamos y guardamos el siguiente nodo con menor costo 
                                if (item < minimum) {
                                    minimum = item;//Guardamos el costo 
                                    minimumnode = j;//Obtenemos el siguiente punto de partida
                                }
                            }

                        }
                    }
                }

                costo = costo + minimum;//Sumamos el costo total
                minimum = 0; //Reiniciamos el valor mínimo 
                route.add(nextnode);//Guardamos Recorrido
                nextnode = minimumnode;//Establecemos el siguiente punto de partida
            }

            i++; //Contador para salir del while

        }
        return route;
    }

    public void TraversalNodeByNode() {
        
        //Inicio de ejecución 
        long Inicioexec = System.currentTimeMillis();
        
        List<Integer> route = new ArrayList<>();
        for (int i = 0; i < matrixProblem.size(); i++) {
            route = this.nodeTraversal(i);
            
            
            System.out.println("Path and cost for node: " + ( i + 1));
            System.out.print( "[");
            for (Iterator<Integer> iterator = route.iterator(); iterator.hasNext();) {
                
                Integer next = iterator.next();
                System.out.print(next + 1 + ",");
            }
            System.out.print( "]\n");
        }
        
        //Obtenemos y guardamos tiempo de ejecución
        long Totalexec = System.currentTimeMillis() - Inicioexec;
        System.out.println("Tiempo ejecución:" + Totalexec);
    }

    public void ThreadTraversalNodeByNode() throws InterruptedException, ExecutionException {
        
        //Inicio de ejecución 
        long Inicioexec = System.currentTimeMillis();
        
        int 
            batch_hilos,
            j = 0,
            nodo = 0,
            hilos_optimos = 24,
            ejecutar_hilos,
            Control_hilos,
            totalhilos = 0; //Flag que indica si los hilos se acabaron de ejecutar;
        
        boolean finish; //Flag para controlar ejecución del while
        
        //matrixProblem.size()
        if (matrixProblem.size() <= hilos_optimos) {
            batch_hilos = 1;
        } else {
            batch_hilos = (int) Math.ceil( (double) matrixProblem.size() / (double) hilos_optimos);
        }
        
        Control_hilos = matrixProblem.size();

        while (j < batch_hilos) {
            
            //Obtenemos el número de hilos que vamos a ejecutar x ciclo
            if (matrixProblem.size() <= hilos_optimos) {
                ejecutar_hilos = matrixProblem.size();
            }else{
                if (Control_hilos >  hilos_optimos) {
                    ejecutar_hilos = hilos_optimos;
                }else{
                    ejecutar_hilos = Control_hilos;
                }
                Control_hilos = Control_hilos - hilos_optimos;
            }

            //Creamos un pool de hilos 
            ExecutorService execute = Executors.newFixedThreadPool(ejecutar_hilos);
            
            //Creamos Matriz con # de hilos que necesitamos 
            Future[] threads = new Future[ejecutar_hilos];
            
            //Ejecutamos los procesos
            for (int i = 0; i < ejecutar_hilos; i++) {
                //Creamos un hilo por cada nodo
                threads[i] = execute.submit(new ThreadTravel(matrixProblem, nodo));
                nodo++;
            }
            
            finish = true;
            
            while (finish) {
                //Esperamos a que terminen de ejecutar todos los hilos
                for (int x = 0; x < threads.length; x++) {
                    if (threads[x].isDone()) {
                        totalhilos++;
                    }
                }
                
                //Salimos del while hasta que se terminen de ejecutar los hilos
                if (totalhilos == ejecutar_hilos) {
                    finish = false;
                }
                
                //Reiniciamos la variable para volver a revisar
                totalhilos = 0;
            }
            
            //imprimimos respuestas
            for (int x = 0; x < threads.length; x++) {
                
                List<Integer> route = new ArrayList<>();
                
                route = (List<Integer>) threads[x].get();
                
                System.out.println("Ruta y costo para nodo: " + route.get(0).toString() );
                System.out.println(route);
            }

            j++;

        }
        
        //Obtenemos y guardamos tiempo de ejecución
        long Totalexec = System.currentTimeMillis() - Inicioexec;
        System.out.println("Tiempo ejecución:" + Totalexec);

    }

}
