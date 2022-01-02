package agenteviajero;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class AgenteViajero {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        List<Integer> route = new ArrayList<>();

        //Matriz del Proiblema
        List<String[]> matrixProblem = new ArrayList<>();
        
        //Node List Problem
        List<String[]> nodeList = new ArrayList<>();
        
        //Preparamos el archivo de donde vamos a tomar los datos
        ReadFile rf = new ReadFile("eil101.tsp");

        //Leemos los datos del archivo
        ////matrixProblem = rf.getMatrix();
        
        nodeList = rf.getNodesList();
        matrixProblem = rf.getMatrixByNode(nodeList);
        

        //Si la matriz no es vacía comenzamos con la solución del problema
        if (matrixProblem.isEmpty()) {
            System.out.println("Sin datos para el ");
        } else {
            NearestNeighbor nn = new NearestNeighbor(matrixProblem);
            //Ejecución Lineal
            nn.TraversalNodeByNode();
            //Ejecución en paralelo
            //nn.ThreadTraversalNodeByNode();
        }

    }

}
