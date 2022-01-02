package agenteviajero;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFileChooser;

/**
 *
 * @author MambaNegraPC
 */
public class ReadFile {

    private String pathfile;

    public ReadFile(String pathfile) {

        this.pathfile = pathfile;

        if (pathfile.isEmpty()) {
            this.pathfile = getPath();
        }

    }

    public ArrayList<String[]> getMatrix() {

        List<String[]> matrix = new ArrayList<String[]>();

        try {

            File matrizFile = new File(pathfile);
            Scanner Sc = new Scanner(matrizFile);

            //Leer Linea
            while (Sc.hasNextLine()) {
                //Separar columnas
                String[] data = Sc.nextLine().split(",");
                //Guardar datos en Lista
                matrix.add(data);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error al leer el archivo ");
        }

        return (ArrayList<String[]>) matrix;

    }

    public ArrayList<String[]> getNodesList() {
        List<String[]> matrix = new ArrayList<String[]>();
        boolean startToRead = false;

        try {

            File matrizFile = new File(pathfile);
            Scanner Sc = new Scanner(matrizFile);
            
            //Leer Linea
            while (Sc.hasNextLine()) {
                
                String line = Sc.nextLine().toString();
                
                if (line.equals("EOF")){
                    startToRead = false;
                } 
                
                if (startToRead) {
                    //Separar columnas
                    String[] data = line.split(" ");
                    //Guardar datos en Lista
                    matrix.add(data);
                }
                
                if (line.equals("NODE_COORD_SECTION")) {
                    startToRead = true;
                }
                
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error al leer el archivo ");
        }

        return (ArrayList<String[]>) matrix;
    }
    
    public ArrayList<String[]> getMatrixByNode(List<String[]> nodeList){
        List<String[]> matrix = new ArrayList<String[]>();
        
        float distance;
        float source_x,source_y,destination_x,destination_y;
        int source_i,destination_j;
        
        for (int i = 0; i < nodeList.size(); i++) {
            
            source_i = Integer.parseInt(nodeList.get(i)[0]);
            source_x = Float.parseFloat(nodeList.get(i)[1]);
            source_y = Float.parseFloat(nodeList.get(i)[2]);
            
            String[] distances = new String[nodeList.size()];
            
            for (int j = 0; j < nodeList.size(); j++) {
                
                destination_j = Integer.parseInt(nodeList.get(j)[0]);
                destination_x = Float.parseFloat(nodeList.get(j)[1]);
                destination_y = Float.parseFloat(nodeList.get(j)[2]);
                
                if (source_i==destination_j) {
                    distance = 0;
                }else{
                    distance = (float) Math.sqrt(
                                        Math.pow((destination_x - source_x),2) 
                                      + Math.pow((destination_y - source_y),2)
                                     );
                }    
                
                distances[j] = Float.toString(distance);
                
            }
            
            matrix.add(distances);
        }
        
        return (ArrayList<String[]>) matrix;
    }

    private String getPath() {

        String path = "";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(fileChooser);

        try {
            path = fileChooser.getSelectedFile().getAbsolutePath();
        } catch (NullPointerException e) {
            System.out.println("No se ha seleccionado ningún fichero");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return path;
    }

}
