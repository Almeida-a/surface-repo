package pt.ua.rsi.filereader;

import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import pt.ua.rsi.datastructs.MeshObject;
import pt.ua.rsi.datastructs.Point;

import java.util.ArrayList;
import java.util.Iterator;

public class MeshDcmIterator implements Iterator<MeshObject.Facet> {
    // Based on this tutorial: https://www.geeksforgeeks.org/java-implementing-iterator-and-iterable-interface/

    private int indexer;
    private DicomInputStream stream;
    private ArrayList<Point> pointsList;

    public MeshDcmIterator(DicomInputStream stream) {
        // TODO initialize cursor: get first primitive on the dicom file
        indexer = 0;
        this.stream = stream;
    }

    @Override
    public boolean hasNext() {
        // TODO: 05/02/2022 Check if there are more primitives
        return false;
    }

    @Override  // Return current data and update pointer
    public MeshObject.Facet next() {
        // TODO: 05/02/2022 Get facet (using indexer) and update indexer
        return null;
    }

}
