package plagueWorld.spread;

/**
 * Created by zhuhan on 14/12/16.
 */
public class WaterSpread extends Spread{

    public int spread(double shipFrequency, int population, int infectedPopulation){
        if(java.lang.Math.random() > shipFrequency)
            return 0;

        double probability = 1 - (double)infectedPopulation / population;
        probability = 1 - Math.pow(probability, (40 + 20 * waterSpreadLevel));

        if(java.lang.Math.random() > probability)
            return 0;

        return 1;
    }

}
