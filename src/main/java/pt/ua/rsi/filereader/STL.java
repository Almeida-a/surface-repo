package pt.ua.rsi.filereader;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.j3d.loaders.stl.STLFileReader;
import pt.ua.rsi.datastructs.MeshObject;
import pt.ua.rsi.datastructs.PointCloudObject;

import java.io.File;
import java.io.IOException;

public class STL implements Format3D {

    // Singleton class
    private static STL instance;

    public DicomObject parseDicom(String filePath) throws IOException {

        // Dicom object
        DicomObject dicomObject = new BasicDicomObject();
        // File Reader object
        STLFileReader stlFileReader = new STLFileReader(
                new File(filePath)
        );

        dicomObject.putInt(Tag.NumberOfSurfaces, VR.UL, stlFileReader.getNumOfFacets()[0]);
        dicomObject.putSequence(Tag.SurfaceSequence);

        // Add placeholders for facets data
        double [] normal = new double[3];
        double [][] vertexes = new double[3][3];

        // Fetch object3D data (only read the first object)
        for (int i = 0; i < stlFileReader.getNumOfFacets()[0]; i++) {
            if (!stlFileReader.getNextFacet(normal, vertexes)) {
                System.out.println("No more facets!");
                break;
            }
            // Hardcoded color data, we are not extracting it from the file (maybe we will in the future)
            dicomObject.putInt(Tag.RecommendedDisplayGrayscaleValue, VR.US, 0x7FFF);
            dicomObject.putInt(Tag.RecommendedDisplayCIELabValue, VR.US, 0x7FFF);
            dicomObject.putInt(Tag.SurfaceNumber, VR.UL, i);
            dicomObject.putString(Tag.SurfaceProcessing, VR.CS, "YES");
            dicomObject.putFloat(Tag.RecommendedPresentationOpacity, VR.FL, (float) 0.9);
            dicomObject.putString(Tag.RecommendedPresentationType, VR.CS, "SURFACE");
            dicomObject.putString(Tag.FiniteVolume, VR.CS, "YES");
            dicomObject.putString(Tag.Manifold, VR.CS, "UNKNOWN");

            dicomObject.putSequence(Tag.SurfacePointsSequence);
            dicomObject.putFloat(Tag.NumberOfSurfacePoints, VR.UL, (float) 3.0);
            dicomObject.putFloat(Tag.PointCoordinatesData, VR.OF, (float) -1.0);

            dicomObject.putSequence(Tag.SurfacePointsNormalsSequence);
            dicomObject.putInt(Tag.NumberOfVectors, VR.UL, 1);
            dicomObject.putInt(Tag.VectorDimensionality, VR.US, 3);
            dicomObject.putFloat(Tag.VectorAccuracy, VR.FL, (float) 0.9);
            dicomObject.putString(Tag.VectorCoordinateData, VR.OF, String.format("{},{},{}",
                    normal[0], normal[1], normal[2])
            );

            dicomObject.putSequence(Tag.SurfaceMeshPrimitivesSequence);
            dicomObject.putSequence(Tag.TriangleStripSequence); // empty
            dicomObject.putSequence(Tag.TriangleFanSequence); // empty
            dicomObject.putSequence(Tag.LineSequence); // empty
            dicomObject.putSequence(Tag.FacetSequence); // empty
            dicomObject.putString(new int[]{0x66,0x41}, VR.LO, "");
            dicomObject.putString(new int[]{0x66,0x42}, VR.LO, "");
            dicomObject.putString(new int[]{0x66,0x43}, VR.LO, String.format("{}", (Object) vertexes));

            dicomObject.putSequence(Tag.SurfaceProcessingAlgorithmIdentificationSequence); // empty
        }

        stlFileReader.close();

        return dicomObject;
    }

    public PointCloudObject readFilePointCloud(String filePath) {
        return null;
    }

    @Override
    public MeshObject readFileMesh(String filePath) {
        return null;
    }

    private STL() {}

    public static STL getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new STL();
        return instance;
    }

}
