package pt.ua.rsi.datastructs;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector(Point a, Point b){
        this.x = b.x - a.x;
        this.y = b.y - a.y;
        this.z = b.z - a.z;
    }

    public Vector crossProduct(Vector b){

        double x = (this.getY() * b.getZ()) - (this.getZ() * b.getY());
        double y = (this.getZ() * b.getX()) - (this.getX() * b.getZ());
        double z = (this.getX() * b.getY()) - (this.getY() * b.getX());

        return new Vector(x,y,z);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0 && Double.compare(vector.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "{ " +
                "" + x +
                ", " + y +
                ", " + z +
                " }";
    }
}
