package pt.ua.rsi.filereader;
//import org.dcm4che3.data.*;
//import org.dcm4che3.util.UIDUtils;
import org.dcm4che2.data.*;
import org.dcm4che2.util.UIDUtils;
import pt.ua.rsi.datastructs.Point;

import java.util.ArrayList;
import java.util.Arrays;

public class DicomValueInsert {
    DicomObject dcmObj;

    public DicomValueInsert(DicomObject dcmObj) {
        this.dcmObj = dcmObj;
    }

    public DicomElement insertPointSequenceDicomObject(ArrayList<Point> uniquePoints,ArrayList<int[]> listVert){
        DicomObject nestedDcmObj = new BasicDicomObject();
        BasicDicomObject nested2DcmObj = new BasicDicomObject();
        BasicDicomObject nested3DcmObj = new BasicDicomObject();
        BasicDicomObject nested4DcmObj;
        DicomElement baseSequence = dcmObj.putSequence(Tag.SurfaceSequence);
        dcmObj.putInt(Tag.NumberOfSurfaces,VR.UL,1);
        nestedDcmObj.putInt(Tag.RecommendedDisplayGrayscaleValue, VR.US, 0x7FFF);
        nestedDcmObj.putInt(Tag.RecommendedDisplayCIELabValue, VR.US, 0x7FFF);
        nestedDcmObj.putInt(Tag.SurfaceNumber,VR.UL,1);
        nestedDcmObj.putString(Tag.SurfaceProcessing, VR.CS, "NO");
        nestedDcmObj.putFloat(Tag.RecommendedPresentationOpacity, VR.FL, (float) 0.9);
        nestedDcmObj.putString(Tag.RecommendedPresentationType, VR.CS, "SURFACE");
        nestedDcmObj.putString(Tag.FiniteVolume, VR.CS, "UNKNOWN");
        nestedDcmObj.putString(Tag.Manifold, VR.CS, "UNKNOWN");
        DicomElement sequence =nestedDcmObj.putSequence(Tag.SurfacePointsSequence,1);
        nestedDcmObj.putSequence(Tag.SurfacePointsNormalsSequence);
        nested2DcmObj.putInt(Tag.NumberOfSurfacePoints,VR.UL,uniquePoints.size());
        nested2DcmObj.putInt(Tag.VectorDimensionality, VR.US, 3);
        //StringBuilder n = new StringBuilder();
        float[] points = new float[uniquePoints.size()*3];
        int i;
        double[] ps = new double[points.length];
        i = 0;
        for(Point p: uniquePoints){
            //n = n.append(x.toString());
            for(float x: p.getRaw()){
                points[i] = x;
                ps[i]=points[i];
                i++;
            }

        }
        System.out.println(Arrays.toString(ps));
        //n.deleteCharAt(n.length()-1);
        //String str = n.toString();
        //System.out.println(n.toString());
        nested2DcmObj.putFloats(Tag.PointCoordinatesData,VR.OF,points);
        sequence.addDicomObject(nested2DcmObj);
        baseSequence.addDicomObject(nestedDcmObj);
        DicomElement sequence2 = nestedDcmObj.putSequence(Tag.SurfaceMeshPrimitivesSequence, 1);
        DicomElement primativeSequence = nested3DcmObj.putSequence(Tag.FacetSequence);
        for(int[] vert : listVert){
            //System.out.println("Value: " + Arrays.toString(vert));
            nested4DcmObj = new BasicDicomObject();
            DicomElement test= nested4DcmObj.putInts(0x0066_0040, VR.UL,vert);
            //System.out.println("Test: " + test);
            primativeSequence.addDicomObject(nested4DcmObj);
        }
        nested3DcmObj.putSequence(Tag.TriangleStripSequence);
        nested3DcmObj.putSequence(Tag.TriangleFanSequence);
        nested3DcmObj.putSequence(Tag.LineSequence);
        nested3DcmObj.putInts(0x0066_0041,VR.UL,null);
        nested3DcmObj.putInts(0x0066_0042,VR.UL,null);
        nested3DcmObj.putInts(0x0066_0043,VR.UL,null);

        sequence2.addDicomObject(nested3DcmObj);


        //System.out.println(dcmObj.toString());

        return sequence;
    }

    /*public DicomElement insertMeshSequenceDicomObject(ArrayList<int[]> listVert){
        DicomElement e = dcmObj.putSequence(Tag.SurfaceMeshPrimitivesSequence);
        DicomElement sequence = dcmObj.putSequence(Tag.FacetSequence, listVert.size());
        DicomObject nestedDcmObj = new BasicDicomObject();
        BasicDicomObject nested2DcmObj = new BasicDicomObject();
        //System.out.println("Tamanho :"+ listVert.size());
        int o = 0;
        for(int[] x : listVert) {
           // System.out.println("Entrou aqui");
            //System.out.println("int[] x " +x.toString());
            //dcmObj.putInts((Tag.PrimitivePointIndexList), VR.OW, x);
            o++;
        }

        //dcmObj.putInts( );
        //dcmObj.

        System.out.println(dcmObj.toString());

        return e;
    }*/
}

