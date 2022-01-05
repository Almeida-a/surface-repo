package pt.ua.rsi.datastructs;

import java.util.ArrayList;
import java.util.Collection;

public class MeshObject {

    private final Collection<Facet> facets;

    public MeshObject(Collection<Facet> faces) {
        this.facets = faces;
    }

    public MeshObject() {
        facets = new ArrayList<>();
    }

    public Collection<Facet> getFacets() {
        return facets;
    }

    public boolean addFacet(Facet facet) {
        return facets.add(facet);
    }

    public boolean removeFacet(Facet facet) {
        return facets.remove(facet);
    }

    public static class Facet {

        // Surface's normal vector 3D coordinates
        private final double[] normal;
        // Face vertexes 3D coordinates
        private final double[][] vertexes;

        public Facet(double[] normal, double[][] vertexes) {
            this.normal = normal;
            this.vertexes = vertexes;
        }

        public double[] getNormal() {
            return normal;
        }

        public double[][] getVertexes() {
            return vertexes;
        }
    }
}
