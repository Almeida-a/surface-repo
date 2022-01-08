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

import static pt.ua.rsi.Utils.doublesToFloat;

public class STL implements Format3D {

    // Singleton class
    private static STL instance;

    /**
     * filepath: path to the .stl file
     * */
    public DicomObject parseDicom(String filePath) throws IOException {

        // Dicom object
        DicomObject dicomObject = new BasicDicomObject();
        DicomObject nestedDcmObj = new BasicDicomObject();
        DicomObject nested2DcmObj = new BasicDicomObject();
        // File Reader object
        STLFileReader stlFileReader = new STLFileReader(
                new File(filePath)
        );

        dicomObject.putInt(Tag.NumberOfSurfaces, VR.UL, stlFileReader.getNumOfFacets()[0]);
        dicomObject.putSequence(Tag.SurfaceSequence, stlFileReader.getNumOfFacets()[0]);

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
            nestedDcmObj.putInt(Tag.RecommendedDisplayGrayscaleValue, VR.US, 0x7FFF);
            nestedDcmObj.putInt(Tag.RecommendedDisplayCIELabValue, VR.US, 0x7FFF);
            nestedDcmObj.putInt(Tag.SurfaceNumber, VR.UL, i);
            nestedDcmObj.putString(Tag.SurfaceProcessing, VR.CS, "NO");
            nestedDcmObj.putFloat(Tag.RecommendedPresentationOpacity, VR.FL, (float) 0.9);
            nestedDcmObj.putString(Tag.RecommendedPresentationType, VR.CS, "SURFACE");
            nestedDcmObj.putString(Tag.FiniteVolume, VR.CS, "YES");
            nestedDcmObj.putString(Tag.Manifold, VR.CS, "UNKNOWN");

            nestedDcmObj.putSequence(Tag.SurfacePointsSequence, 2);
            nested2DcmObj.putInt(Tag.NumberOfSurfacePoints, VR.UL, 3);
            nested2DcmObj.putFloat(Tag.PointCoordinatesData, VR.OF, (float) -1.0);
            nestedDcmObj.putNestedDicomObject(Tag.SurfacePointsSequence, nested2DcmObj);
            nested2DcmObj.clear();

            nestedDcmObj.putSequence(Tag.SurfacePointsNormalsSequence, 4);
            nested2DcmObj.putInt(Tag.NumberOfVectors, VR.UL, 1);
            nested2DcmObj.putInt(Tag.VectorDimensionality, VR.US, 3);
            nested2DcmObj.putFloat(Tag.VectorAccuracy, VR.FL, (float) 0.9);
            nested2DcmObj.putFloats(Tag.VectorCoordinateData, VR.OF, doublesToFloat(normal.clone()));
            nestedDcmObj.putNestedDicomObject(Tag.SurfacePointsNormalsSequence, nested2DcmObj);
            nested2DcmObj.clear();

            nestedDcmObj.putSequence(Tag.SurfaceMeshPrimitivesSequence, 0); // empty
            nestedDcmObj.putSequence(Tag.TriangleStripSequence, 0); // empty
            nestedDcmObj.putSequence(Tag.TriangleFanSequence, 0); // empty
            nestedDcmObj.putSequence(Tag.LineSequence, 0); // empty
            nestedDcmObj.putSequence(Tag.FacetSequence, 3);
            nestedDcmObj.putString(0x0066_0041, VR.LO, "");
            nestedDcmObj.putString(0x0066_0042, VR.LO, "");
            nestedDcmObj.putString(0x0066_0043, VR.LO, String.format("{}", (Object) vertexes));

            nestedDcmObj.putSequence(Tag.SurfaceProcessingAlgorithmIdentificationSequence, 0); // empty

            // Add sequence item to main object
            dicomObject.putNestedDicomObject(Tag.SurfaceSequence, nestedDcmObj);
            nestedDcmObj.clear(); // clear item to hold next facet's data
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
