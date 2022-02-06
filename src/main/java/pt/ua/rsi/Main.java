package pt.ua.rsi;


import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import pt.ua.rsi.datastructs.Point;
import pt.ua.rsi.filereader.DicomValueInsert;
import pt.ua.rsi.filereader.STL;
import pt.ua.rsi.filereader.STLreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    private static ArrayList<int[]> b = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        DicomObject dc = new BasicDicomObject();
        DicomValueInsert z = new DicomValueInsert(dc);
        STLreader a = new STLreader();
//        writeDicomMeshFile();
 //       readDicomFiles();



        ArrayList<Point> puta = new ArrayList<>();
        puta.add(new Point (-35, 60, 20));
        puta.add(new Point (-55, 60, 20));
        puta.add(new Point (-35, 40, 20));
        puta.add(new Point (-55, 40, 20));
        puta.add(new Point (-35, 40, 0));
        puta.add(new Point (-55, 40, 0));
        puta.add(new Point (-35, 60, 0));
        puta.add(new Point (-55, 60, 0));


        a.indexingFacets(puta, b);
        System.out.println("Tamanho Main:" + b.size());
        writeDicomMeshFile(puta,b);
        //DicomElement sequence = z.insertPointSequenceDicomObject(puta,b);
        //System.out.println(sequence);
        //DicomElement sequence2 = z.insertMeshSequenceDicomObject(b);




    }

    private static void read3DFiles() throws IOException {
        ArrayList<String> files = new ArrayList<>();

        files.add("src/main/resources/3Ddata/wolf_fenrir.stl");
        files.add("src/main/resources/3Ddata/hellboy.stl");
        files.add("src/main/resources/3Ddata/3DBenchy_Single_Window.stl");

        STL stl = STL.getInstance();

        // Perform the read operation
        stl.readFileMesh(files.get(1));
    }

    private static void writeDicomMeshFile(ArrayList<Point> pointsList, ArrayList<int[]> indexVert) throws IOException {
        // TODO find a way to check validity of the generated dicom file

        // 3D files' path string
        /*ArrayList<String> files = new ArrayList<>();
        String baseDir = "src/main/resources/3Ddata/";
        files.add(baseDir + "wolf_fenrir.stl");
        files.add(baseDir + "hellboy.stl");
        files.add(baseDir + "3DBenchy_Single_Window.stl");*/
        String f = "src/main/resources/3Ddata/Cube_3d_printing_sample.stl";
        // Singletons
        MetaDicom meta = MetaDicom.getInstance();
        STL stl = STL.getInstance();

        // Create DICOM object with header data / metadata
        DicomObject dcmObj = meta.generateMetadata();
        DicomValueInsert dcmInsert = new DicomValueInsert(dcmObj);
        dcmInsert.insertPointSequenceDicomObject(pointsList,indexVert);

        // Extract Surface Mesh into dcmObj
        //stl.parseDicom(dcmObj, f);

        // Set up file and write contents
        String baseDir = "src/main/resources/Dicom/";
        File meshFile = new File(baseDir + "mesh.dcm");
        DicomOutputStream outputStream = new DicomOutputStream(meshFile);
        System.out.println(dcmObj);
        outputStream.writeDicomFile(dcmObj);
        outputStream.close();


//        if(dcmObj)

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
