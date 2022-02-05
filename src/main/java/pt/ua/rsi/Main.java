package pt.ua.rsi;


import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import pt.ua.rsi.filereader.STL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws IOException {

//        writeDicomMeshFile();
        readDicomFiles();
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

    private static void writeDicomMeshFile() throws IOException {
        // TODO find a way to check validity of the generated dicom file

        // 3D files' path string
        ArrayList<String> files = new ArrayList<>();
        String baseDir = "src/main/resources/3Ddata/";
        files.add(baseDir + "wolf_fenrir.stl");
        files.add(baseDir + "hellboy.stl");
        files.add(baseDir + "3DBenchy_Single_Window.stl");

        // Singletons
        MetaDicom meta = MetaDicom.getInstance();
        STL stl = STL.getInstance();

        // Create DICOM object with header data / metadata
        DicomObject dcmObj = meta.generateMetadata();

        // Extract Surface Mesh into dcmObj
        stl.parseDicom(dcmObj, files.get(0));

        // Set up file and write contents
        baseDir = "src/main/resources/Dicom/";
        File meshFile = new File(baseDir + "mesh.dcm");
        DicomOutputStream outputStream = new DicomOutputStream(meshFile);
        outputStream.writeDicomFile(dcmObj);

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
