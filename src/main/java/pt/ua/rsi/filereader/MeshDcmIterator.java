package pt.ua.rsi.filereader;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import pt.ua.rsi.datastructs.MeshObject;
import pt.ua.rsi.datastructs.Point;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MeshDcmIterator implements Iterator<MeshObject.Facet> {
    // Based on this tutorial: https://www.geeksforgeeks.org/java-implementing-iterator-and-iterable-interface/

    private int indexer;
    private final ByteOrder bo;
    private final ArrayList<Point> pointsList = new ArrayList<>();
    private final ArrayList<byte[]> primitiveList = new ArrayList<>();

    public MeshDcmIterator(DicomInputStream stream) throws IOException {

        indexer = 1;

        DicomObject dcmObj = stream.readDicomObject();

        // Get byte order
        bo = dcmObj.bigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;

        Iterator<DicomElement> iterator = dcmObj.iterator();
        DicomElement dicomElement;
        int tag;
        byte[] data;

        while (iterator.hasNext()) {
            dicomElement = iterator.next();
            tag = dicomElement.tag();

            if (tag == Tag.PointCoordinatesData) {

                // Initialize the point list (by reading (0066, 0016))
                data = dicomElement.getBytes();

                assert data.length % 12 == 0: "This string should contain a multiple of " +
                        "12 bytes (3 floats of 32 bit length)";

                // Store coordinates of the point in bytes form (3 times 32-bit float)
                ByteBuffer pointBytes;
                // Store float value
                float x, y, z;

                for (int i = 0; i < data.length; i+=12) {

                    // Thanks to http://stackoverflow.com/questions/13469681/ddg#13469763
                    pointBytes = ByteBuffer.wrap(Arrays.copyOfRange(data, i, i+12)).order(bo);

                    // Get point coordinates
                    x = pointBytes.getFloat(1);
                    y = pointBytes.getFloat(2);
                    z = pointBytes.getFloat(3);

                    // Insert in list
                    pointsList.add(new Point(x, y, z));
                }

            } else if (tag == 0x0066_0040) {
                // Action carried out for each item (0066, 0040) of facet sequence (0066, 0034)

                // Get primitiveCount primitives list, from (0066, 0034)
                primitiveList.add(dicomElement.getBytes());

            }
        }

    }

    @Override
    public boolean hasNext() {
        // Check if there are more primitives
        // Nuance: since indexer starts counting from 1, its last valid value is list.size and not list.size - 1
        return indexer <= primitiveList.size();
    }

    @Override  // Return current data and update pointer
    public MeshObject.Facet next() {

        // Get data from primitiveList
        byte[] data = primitiveList.get(indexer);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(bo);

        // Translate data into coordinates array
        float[][] vertexes = new float[3][3];
        for (int i = 0; i < 3; i++) {
            // Parse each word (point indices) in data to point object
            vertexes[i] = pointsList.get(byteBuffer.getInt(i)).getRaw();
        }

        MeshObject.Facet facet = new MeshObject.Facet(null, vertexes);

        indexer++;  // update pointer
        return facet;
    }

}
