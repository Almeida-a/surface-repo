package pt.ua.rsi;

public class Utils {

    public static float[] doublesToFloat(double[] doubles) {
        float[] inFloatForm = new float[doubles.length];
        for (int i = 0; i < doubles.length; i++)
            inFloatForm[i] = (float) doubles[i];
        return inFloatForm;
    }

}
