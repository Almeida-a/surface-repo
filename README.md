##Surface Repository for DICOM

### Usage
Building from source - 
`mvn install`

Run - `java -jar SurfaceRepo-jar-w-dep.java path/to/stlFile.stl`

Outputs:
* _mesh.dcm_ file in the same directory as the jar file;
* Viewer (reads data from _mesh.dcm_)

### Resources

At _src/main/resources/3Ddata_ there are stl files that can be
used to test out the program.