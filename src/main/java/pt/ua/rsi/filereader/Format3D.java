package pt.ua.rsi.filereader;

import org.dcm4che2.data.DicomObject;
import pt.ua.rsi.datastructs.MeshObject;
import pt.ua.rsi.datastructs.PointCloudObject;

import java.io.IOException;

public interface Format3D {

    MeshObject readFileMesh(String filePath) throws IOException;

    PointCloudObject readFilePointCloud(String filePath);

    DicomObject parseDicom(String filePath) throws IOException;

}
