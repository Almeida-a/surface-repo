package pt.ua.rsi;

import org.dcm4che2.data.*;
import org.dcm4che2.util.UIDUtils;

import java.io.File;
import java.time.Instant;
import java.util.Date;

public class MetaDicom {

    private static MetaDicom instance;

    private MetaDicom() {}

    public static MetaDicom getInstance() {
        if (instance == null) {
            instance = new MetaDicom();
        }
        return instance;
    }

    public DicomObject generateMetadata() {

        DicomObject dcmObj = new BasicDicomObject();
        DicomObject nestedDcmObj = new BasicDicomObject();

        // Transfer Syntax
        dcmObj.putString(Tag.TransferSyntaxUID, VR.UI, UID.ExplicitVRLittleEndian);

        // Patient
        dcmObj.putString(Tag.PatientName, VR.PN, "Antonio");
        dcmObj.putString(Tag.PatientID, VR.LO, "12345");
        dcmObj.putDate(Tag.PatientBirthDate, VR.DA, Date.from(Instant.EPOCH)); // Some random time
        dcmObj.putString(Tag.PatientSex, VR.CS, "M");

        // General Study
        dcmObj.putDate(Tag.StudyDate, VR.DA, Date.from(Instant.parse("2018-11-30T18:35:24.00Z")));
        dcmObj.putDate(Tag.StudyTime, VR.TM, Date.from(Instant.now()));
        dcmObj.putString(Tag.AccessionNumber, VR.SS, "12121212");
        dcmObj.putString(Tag.ReferringPhysicianName, VR.PN, "Dr. Silva");
        dcmObj.putString(Tag.StudyInstanceUID, VR.UI, UIDUtils.createUID());
        dcmObj.putString(Tag.StudyID, VR.SH, "123456");

        // General series
        dcmObj.putString(Tag.Modality, VR.CS, "OSS");
        dcmObj.putString(Tag.SeriesInstanceUID, VR.UI, UIDUtils.createUID());
        dcmObj.putInt(Tag.SeriesNumber, VR.IS, 2);
        // Not necessarily required, but, let's say that the right arm is scanned, then:
        dcmObj.putString(Tag.Laterality, VR.CS, "R");

        // OSS Series
        dcmObj.putSequence(Tag.ReferencedPerformedProcedureStepSequence, 2); // Seq
        dcmObj.putString(Tag.ReferencedSOPClassUID, VR.UI, UIDUtils.createUID());
        dcmObj.putString(Tag.ReferencedSOPInstanceUID, VR.UI, UIDUtils.createUID());
        dcmObj.putSequence(Tag.ReferencedSurfaceSequence, 2); // Seq
        dcmObj.putString(Tag.ReferencedSOPClassUID, VR.UI, UIDUtils.createUID());
        dcmObj.putString(Tag.ReferencedSOPInstanceUID, VR.UI, UIDUtils.createUID());

        // General Equipment
        dcmObj.putString(Tag.Manufacturer, VR.LO, "SIEMENS");
        // Enhanced General Equipment
        dcmObj.putString(Tag.ManufacturerModelName, VR.LO, "GENESIS_SIGNA");
        dcmObj.putString(Tag.DeviceSerialNumber, VR.LO, "35016");
        dcmObj.putString(Tag.SoftwareVersions, VR.LO, "08");

        // Scan Procedure (Common for both types of scan, Point Cloud and Mesh)
        dcmObj.putDate(Tag.AcquisitionDateTime, VR.DT, Date.from(Instant.now()));
        dcmObj.putString(Tag.AcquisitionNumber, VR.IS, "1");
        dcmObj.putString(Tag.InstanceNumber, VR.IS, "41");
        dcmObj.putSequence(0x0080_0001, 0);
        // empty
        dcmObj.putSequence(0x0080_0002, 0);  // Tag (0080, 0002)
        // empty TODO check is it allowed?
        dcmObj.putSequence(0x0080_0003, 1);  // Tag (0080, 0003)
        nestedDcmObj.putDouble(0x0080_0004, VR.FD, 123);
        dcmObj.putNestedDicomObject(0x0080_0003, nestedDcmObj);
        nestedDcmObj.clear();

        return dcmObj;
    }

    public DicomObject readMetadata(File dicomFile) {
        // TODO implement

        return null;
    }

    // TODO should I create a metadata validity verifier?

}
