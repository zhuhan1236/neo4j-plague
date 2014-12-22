package plagueWorld.spread;

/**
 * Created by zhuhan on 14/12/16.
 */
public abstract class Spread {
    public static int coldResistance = 1;
    public static int hotResistance = 1;
    public static int airSpreadLevel = 1;
    public static int waterSpreadLevel = 1;
    public static int landSpreadLevel = 1;
    public static int inCountrySpreadLevel = 1;

    public abstract int spread(double f, int population, int infectedPopulation);
}
