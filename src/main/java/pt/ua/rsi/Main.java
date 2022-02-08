package pt.ua.rsi;


import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import pt.ua.rsi.datastructs.Point;
import pt.ua.rsi.datastructs.Vector;
import pt.ua.rsi.filereader.DicomValueInsert;
import pt.ua.rsi.filereader.STL;
import pt.ua.rsi.filereader.STLreader;
import pt.ua.rsi.render.Mesh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    private static ArrayList<int[]> b = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        String stlPath = args[0];//"Cube_3d_printing_sample.stl";//"src/main/resources/3Ddata/Cube_3d_printing_sample.stl";
        String meshDcmPath = "mesh.dcm";//"src/main/resources/Dicom/mesh.dcm";

        STLreader a = new STLreader(
                new File(stlPath)
        );

        ArrayList<Point> pointArray = new ArrayList<>();
        ArrayList<Vector> vectorArray = new ArrayList<>();
        a.getNumberOfUniquePoints(pointArray, vectorArray);
        a.indexingFacets(pointArray, b);

        writeDicomMeshFile(pointArray,b);

        Mesh.render(new File(meshDcmPath));

    }

    private static void writeDicomMeshFile(ArrayList<Point> pointsList, ArrayList<int[]> indexVert) throws IOException {

        // Singletons
        MetaDicom meta = MetaDicom.getInstance();

        // Create DICOM object with header data / metadata
        DicomObject dcmObj = meta.generateMetadata();
        DicomValueInsert dcmInsert = new DicomValueInsert(dcmObj);
        dcmInsert.insertPointSequenceDicomObject(pointsList,indexVert);

        // Set up file and write contents
        String baseDir = "";//"src/main/resources/Dicom/";
        File meshFile = new File(baseDir+"mesh.dcm");
        DicomOutputStream outputStream = new DicomOutputStream(meshFile);
        System.out.println(dcmObj);
        outputStream.writeDicomFile(dcmObj);
        outputStream.close();

    }

    private static void readDicomFiles() throws IOException {

        ArrayList<String> dicomFiles = new ArrayList<>();

        String baseDir = "src/main/resources/Dicom/";

        dicomFiles.add(baseDir + "1.dcm");
        dicomFiles.add(baseDir + "2.dcm");
        dicomFiles.add(baseDir + "mesh.dcm");
        dicomFiles.add(baseDir + "mesh_originals/E05185_50kV_80uA_360d_0.8r_2a_2300ms_Random_Egg1__rec0002.dcm");

        DicomObject dcmObj;
        DicomInputStream din;
        din = new DicomInputStream(
                new File(dicomFiles.get(3))
        );
        dcmObj = din.readDicomObject();

        Iterator<DicomElement> dicomElementIterator = dcmObj.iterator();
        DicomElement element;

        while (dicomElementIterator.hasNext()) {

            element = dicomElementIterator.next();

        }
        din.close();
    }

}
