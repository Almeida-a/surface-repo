package pt.ua.rsi.filereader;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import pt.ua.rsi.datastructs.MeshObject;
import pt.ua.rsi.datastructs.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MeshDcmIterator implements Iterator<MeshObject.Facet> {
    // Based on this tutorial: https://www.geeksforgeeks.org/java-implementing-iterator-and-iterable-interface/

    private int indexer;
    private final ArrayList<Point> pointsList = new ArrayList<>();
    private final DicomElement primitiveList;

    /**
     *
     * Gets the points that define the lower and upper bound (min and max, respectively)
     *  of the bounding box comprising the existing points.
     * Additionally, it returns the mid-point between those points
     *
     * */
    public Point[] minMidMax() {

        double[] maxXYZ = new double[]{-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE};
        double[] minXYZ = new double[]{Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};

        // Get min coordinates
        for (Point p : pointsList) {
            if (p.getX() < minXYZ[0])
                minXYZ[0] = p.getX();
            else if (p.getX() > maxXYZ[0])
                maxXYZ[0] = p.getX();

            if (p.getY() < minXYZ[1])
                minXYZ[1] = p.getY();
            else if (p.getY() > maxXYZ[1])
                maxXYZ[1] = p.getY();

            if (p.getZ() < minXYZ[2])
                minXYZ[2] = p.getZ();
            else if (p.getZ() > maxXYZ[2])
                maxXYZ[2] = p.getZ();
        }

        Point min = new Point(minXYZ);
        Point max = new Point(maxXYZ);

        return new Point[]{min, Point.getMidPoint(min, max), max};
    }

    public MeshDcmIterator(DicomInputStream stream, int surfaceID) throws IOException {

        indexer = 0;

        DicomObject dcmObj = stream.readDicomObject();

        DicomObject surface = dcmObj.get(Tag.SurfaceSequence).getDicomObject(surfaceID);

        // Get points
        DicomElement pointCoordinatesData = surface.get(Tag.SurfacePointsSequence).getDicomObject(0)
                .get(Tag.PointCoordinatesData);
        float[] data = pointCoordinatesData.getFloats(true);
        for (int i = 0; i < data.length; i+=3) {
            pointsList.add(new Point(data[i], data[i+1], data[i+2]));
        }

        // Get primitives
        primitiveList = surface.get(Tag.SurfaceMeshPrimitivesSequence)
                .getDicomObject(0).get(Tag.FacetSequence);

    }

    @Override
    public boolean hasNext() {
        // Check if there are more primitives
        // Nuance: since indexer starts counting from 1, its last valid value is list.size and not list.size - 1
        return indexer < primitiveList.countItems();
    }

    @Override  // Return current data and update pointer
    public MeshObject.Facet next() {

        // Get data from primitiveList
        int[] points = primitiveList.getDicomObject(indexer).get(0x0066_0040).getInts(true);

        // Translate data into coordinates array
        float[][] vertexes = new float[3][3];
        for (int i = 0; i < 3; i++) {
            // Parse each word (point indices) in data to point object
            vertexes[i] = pointsList.get(points[i]-1).getRaw();
        }

        MeshObject.Facet facet = new MeshObject.Facet(null, vertexes);

        indexer++;  // update pointer
        return facet;
    }

}
