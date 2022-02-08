package pt.ua.rsi.datastructs;

public class Point {
    double x;
    double y;
    double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(double[] coordinates) {
        x = coordinates[0];
        y = coordinates[1];
        z = coordinates[2];
    }

    public static Point getMidPoint(Point p1, Point p2) {

        return new Point(
                (p1.getX() + p2.getX()) / 2,
                (p1.getY() + p2.getY()) / 2,
                (p1.getZ() + p2.getZ()) / 2
        );
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0 && Double.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "[" +
                  x +
                ", " + y +
                ", " + z +
                "]";

/*
        StringBuilder n = new StringBuilder();

        if(this.x % 1 ==0){
            n.append((int)x + ".");
        }else{
            n.append(String.format("%.3f",x));
        }
        if(this.y % 1 ==0){
            n.append((int)y+".");
        }else{
            n.append(String.format("%.3f",y));
        }
        if(this.z % 1 ==0){
            n.append((int)z+".");
        }else{
            n.append(String.format("%.3f",z));
        }
*/
        //n.append("\\");
        //return n.toString();
    }

    public float[] getRaw(){
        float[] arr =  new float[3];
        arr[0] = (float)x;
        arr[1] = (float)y;
        arr[2] = (float)z;
        return arr;
    }
}
