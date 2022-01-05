package pt.ua.rsi.datastructs;

import java.util.Collection;

public class PointCloudObject {

    private final Collection<Vertex> cloud;

    public PointCloudObject(Collection<Vertex> cloud) {
        this.cloud = cloud;
    }

    public Collection<Vertex> getCloud() {
        return cloud;
    }

    public boolean addVertex(Vertex v) {
        return cloud.add(v);
    }

    public boolean removeVertex(Vertex v) {
        return cloud.remove(v);
    }

    static class Vertex {
        private final double[] coordinates;

        public double[] getCoordinates() {
            return coordinates;
        }

        public Vertex(double[] coordinates) {
            this.coordinates = coordinates;
        }
    }
}
