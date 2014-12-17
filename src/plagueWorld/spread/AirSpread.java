package plagueWorld.spread;

/**
 * Created by zhuhan on 14/12/16.
 */
public class AirSpread extends Spread{

    public int spread(double flightFrequency, int population, int infectedPopulation){
        if(java.lang.Math.random() > flightFrequency)
            return 0;

        double probability = 1 - (double)infectedPopulation / population;
        probability = 1 - Math.pow(probability, (20 + 10 * airSpreadLevel));

        if(java.lang.Math.random() > probability)
            return 0;

        return 1;
    }
}
