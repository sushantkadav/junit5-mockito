package in.avenues.junit5mockito.util;

public class MathUtil {

    public int add(int a, int b) {
        return a + b;
    }

    public int multiply(int a, int b){
        return a*b;
    }

    public int divide(int a, int b){
        return a/b;
    }

    public double computeCircleRadius(int radius) {
        return Math.PI * radius * radius;
    }
}
