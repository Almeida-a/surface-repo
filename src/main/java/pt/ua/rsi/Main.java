package pt.ua.rsi;


import org.dcm4che2.data.*;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import pt.ua.rsi.filereader.STL;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        writeDicomMeshFile();
//        readDicomFiles();
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

        // Extract Surface Mesh
        DicomObject dataObj = stl.parseDicom(files.get(0));
        // Add scan data alongside header data
        Iterator<DicomElement> iterator = dataObj.iterator();  // FIXME surface sequence data is lost, somehow
        while (iterator.hasNext()) {
            dcmObj.add(iterator.next());
        }

        // Set up file and write contents
        baseDir = "src/main/resources/Dicom/";
        File meshFile = new File(baseDir + "mesh.dcm");
        DicomOutputStream outputStream = new DicomOutputStream(meshFile);
        // FIXME this doesn't write the content to the requested file
        outputStream.writeDicomFile(dcmObj);

    }

    private static void readDicomFiles() throws IOException {

        ArrayList<String> dicomFiles = new ArrayList<>();

        String baseDir = "src/main/resources/Dicom/";

        dicomFiles.add(baseDir + "1.dcm");
        dicomFiles.add(baseDir + "2.dcm");
        dicomFiles.add(baseDir + "mesh.dcm");

        DicomObject dcmObj;
        DicomInputStream din = null;
        din = new DicomInputStream(
                new File(dicomFiles.get(2))
        );
        dcmObj = din.readDicomObject();

        Iterator<DicomElement> dicomElementIterator = dcmObj.iterator();
        SpecificCharacterSet cs = new SpecificCharacterSet("UTF-8");
        DicomElement element;

        // Values for holding each dicom object attribute
        String value;
        int tag;
        VR vr;
        boolean bigEndian;

        while (dicomElementIterator.hasNext()) {

            element = dicomElementIterator.next();

            value = element.getValueAsString(cs, 0);
            tag = element.tag();
            vr = element.vr();
            bigEndian = element.bigEndian();

            System.out.println("Placeholder code.");

        }
        din.close();
    }

}
