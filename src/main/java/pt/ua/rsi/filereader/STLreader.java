package pt.ua.rsi.filereader;

import org.j3d.loaders.stl.STLFileReader;
import pt.ua.rsi.datastructs.Point;
import pt.ua.rsi.datastructs.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class STLreader {

    File f = new File("/Users/joaorodrigues/Universidade/RSI/resources/Cube_3d_printing_sample.stl");
    //STLFileReader stlf ;//= new STLFileReader(f)

    public int getNumberOfObjects(File f) throws IOException {
        STLFileReader stlf = new STLFileReader(f);
        int numOfObjects = stlf.getNumOfObjects();
        System.out.println("NumOfObjects: " + numOfObjects);
        stlf.close();
        return numOfObjects;
    }

    public int getNumberOfFacets(File f) throws IOException {
        STLFileReader stlf = new STLFileReader(f);
        int numOfObjects = stlf.getNumOfObjects();
        int[] numOfFacets = stlf.getNumOfFacets();
        int numOfTotalFacets = 0;
        for (int i = 0; i < numOfObjects; i++) {
            System.out.println("NumOfFacets" + i + " : " + numOfFacets[i]);
            numOfTotalFacets += numOfFacets[i];

        }
        stlf.close();
        return numOfTotalFacets;
    }

    public int getNumberOfUniquePoints(ArrayList<Point> uniquePoints, ArrayList<Vector> vectorsByPoint) throws IOException {
        STLFileReader stlf = new STLFileReader(f);
        HashSet<Point> numUniquePoints = new HashSet<>();
        HashSet<Vector> numVectorsByPoint = new HashSet<>();
        int numOfObjects = stlf.getNumOfObjects();
        int[] numOfFacets = stlf.getNumOfFacets();
        for (int i = 0; i < numOfObjects; i++) {
            for (int j = 0; j < numOfFacets[i]; j++) {
                double[] firstNormal = new double[3];
                double[][] verticesOfFacet = new double[3][3];
                stlf.getNextFacet(firstNormal, verticesOfFacet);
                //StringBuilder sb = new StringBuilder();
                int k = 0;
                int pr = 0;
                for (double[] row : verticesOfFacet) {
                    k++;
                    //sb.append(k + "º vertix : " + Arrays.toString(row)).append("\n");
                    Point a = new Point((float) row[0], (float) row[1], (float) row[2]);
                    Vector va = new Vector(row[0],row[1],row[2]);
                    numUniquePoints.add(a);
                    numVectorsByPoint.add(va);
                    //numUniquePoints.add(new Point(row[0],row[1],row[2]));
                }
            }
        }
        stlf.close();
        int p = 0;
        for(Point x : numUniquePoints){
            uniquePoints.add(p,x);
            p++;
            System.out.println("Point "+p+" :" +x.toString());
        }
        int v = 0;
        for(Vector y : numVectorsByPoint){
            vectorsByPoint.add(v,y);
            v++;
        }

        return uniquePoints.size();
    }

    public void indexingFacets(ArrayList<Point> uniquePoints, ArrayList<int[]> listOfVertixOfFacets) throws IOException{
        STLFileReader stlf = new STLFileReader(f);
        listOfVertixOfFacets = new ArrayList<>();
        int m;
        int numOfObjects = stlf.getNumOfObjects();
        int[] numOfFacets = stlf.getNumOfFacets();
        for(int i = 0; i < numOfObjects; i++) {
            for (int j = 0; j < numOfFacets[i]; j++) {
                double[] firstNormal1 = new double[3];
                double[][] verticesOfFacet1 = new double[3][3];
                stlf.getNextFacet(firstNormal1, verticesOfFacet1);
                if(i == 0){
                    m = j;
                }else{
                    m = j + (i * numOfFacets[(i-1)]);
                }
                int k = 0;
                int[] index = new int[3];
                //System.out.println("New Facet");
                for (double[] row : verticesOfFacet1) {
                    //  System.out.println(Arrays.toString(row));
                    for(int l = 0; l < uniquePoints.size(); l++) {
                        if (row[0] == uniquePoints.get(l).getX() && row[1] == uniquePoints.get(l).getY() && row[2] == uniquePoints.get(l).getZ()) {
                            index[k] = l;
                            break;
                        }
                    }
                    k++;
                }
                listOfVertixOfFacets.add(m, index);
            }
            //System.out.println("M: "+m);
            //System.out.println("\tIndex List: "+Arrays.toString(index)+"\n");

            //System.out.println(Arrays.toString(firstNormal));

            //System.out.println("Vertix: \n" + sb.toString());
            stlf.close();
        }
    }

    public void vertixNormalsByFacet(ArrayList<int[]> vpf, ArrayList<Vector[]> normalsForFacets, ArrayList<Point> uniquePoints){
        int vf = 0;
        for (int[] vertix : vpf) {
            Vector[] normOfFacet = new Vector[3];
            Vector ij = new Vector(uniquePoints.get(vertix[0]), uniquePoints.get(vertix[1]));
            Vector ik = new Vector(uniquePoints.get(vertix[0]), uniquePoints.get(vertix[2]));
            Vector ji = new Vector(uniquePoints.get(vertix[1]), uniquePoints.get(vertix[0]));
            Vector jk = new Vector(uniquePoints.get(vertix[1]), uniquePoints.get(vertix[2]));
            Vector ki = new Vector(uniquePoints.get(vertix[2]), uniquePoints.get(vertix[0]));
            Vector kj = new Vector(uniquePoints.get(vertix[2]), uniquePoints.get(vertix[1]));
            normOfFacet[0] = ij.crossProduct(ik);
            normOfFacet[1] = ji.crossProduct(jk);
            normOfFacet[2] = ki.crossProduct(kj);
            normalsForFacets.add(vf,normOfFacet);
            vf++;
        }
    }

}
