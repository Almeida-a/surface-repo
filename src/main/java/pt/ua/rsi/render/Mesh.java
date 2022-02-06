package pt.ua.rsi.render;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import org.dcm4che2.io.DicomInputStream;
import pt.ua.rsi.datastructs.MeshObject;
import pt.ua.rsi.filereader.MeshDcmIterator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Mesh implements GLEventListener {
    // Based on the tutorials from https://www.tutorialspoint.com/jogl/jogl_3d_cube.htm
    public static DisplayMode dm, dm_old;
    private final GLU glu = new GLU();
    private float rquad = .0f;  // TODO figure out what this is used for
//    private MeshDcmIterator primitiveCursor;
//
//    public Mesh() {
//        primitiveCursor = new MeshDcmIterator(stream, seqNumber);
//
//    }

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        // Initialize gl stuff
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, -5.0f );
        // Start drawing the surface (IMPORTANT: assumes primitives are triangles)
        gl.glBegin(GL2.GL_TRIANGLES);

        // Rotate The Cube On X, Y & Z
        gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f);

        // Initialize dicom stream
        DicomInputStream stream = null;
        try {
            stream = new DicomInputStream(new File("src/main/resources/Dicom/mesh.dcm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert stream != null: "Couldn't read file, but program continued!";

        // Get which sequence?
        int seqNumber = 0;

        // Iterate over dicom mesh primitives
        MeshObject.Facet primitive;
        float[][] vertexes;
        try {
            MeshDcmIterator primitiveCursor = new MeshDcmIterator(stream, seqNumber);
            while ( primitiveCursor.hasNext()) {
                primitive = primitiveCursor.next();

                // Vertexes of the triangle primitive
                vertexes = primitive.getVertexes();

                float[] color = randomColor(vertexes);

                // Register data
                gl.glColor3f(color[0], color[1], color[2]);
                gl.glVertex3f(vertexes[0][0], vertexes[1][0], vertexes[2][0]);
                gl.glVertex3f(vertexes[0][1], vertexes[1][1], vertexes[2][1]);
                gl.glVertex3f(vertexes[0][2], vertexes[1][2], vertexes[2][2]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Done drawing the surface
        gl.glEnd();
        gl.glFlush();

        rquad -= 0.15f;

    }

    private static float[] randomColor(float[][] seed) {

        float[] randomColor = new float[3];
        for (int i = 0; i < randomColor.length; i++) {
            randomColor[i] = (float) new Random(Arrays.hashCode(seed[i])).nextDouble();
        }

        return randomColor;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // TODO Auto-generated method stub
        final GL2 gl = drawable.getGL().getGL2();
        if( height <= 0 )
        height = 1;

        final float h = ( float ) width / ( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();

        glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );

        // The canvas
        final GLCanvas glcanvas = new GLCanvas( capabilities );
        Mesh mesh = new Mesh();

        glcanvas.addGLEventListener( mesh );
        glcanvas.setSize( 400, 400 );

        final JFrame frame = new JFrame ( " Mesh" );
        frame.getContentPane().add( glcanvas );
        frame.setSize( frame.getContentPane().getPreferredSize() );
        frame.setVisible( true );
        final FPSAnimator animator = new FPSAnimator(glcanvas, 300,true);

        animator.start();
    }

}
