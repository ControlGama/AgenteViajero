package agenteviajero;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author MambaNegraPC
 */
public class ThreadTravel implements Callable<List<Integer>>{
    
    private List<String[]> matrixProblem = new ArrayList<>();
    private int node;
    
    public ThreadTravel(List<String[]> matrixProblem, int node) {
        this.matrixProblem = matrixProblem;
        this.node = node;
    }
    
    @Override
    public List<Integer> call() throws Exception {
        // TODO Auto-generated method stub
        
        List<Integer> route = new ArrayList<>();
        
        NearestNeighbor nn = new NearestNeighbor(matrixProblem);
        route = nn.nodeTraversal(node);
        
        return route;
    }
    
}
