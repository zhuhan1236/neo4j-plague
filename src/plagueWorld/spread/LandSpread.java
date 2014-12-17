package plagueWorld.spread;

/**
 * Created by zhuhan on 14/12/17.
 */
public class LandSpread extends Spread{

    public int spread(double contactFrequency, int population, int infectedPopulation){
        double probability = 1 - (double)infectedPopulation / population;
        probability = 1 - Math.pow(probability, (contactFrequency + 10 * landSpreadLevel));

        if(java.lang.Math.random() > probability)
            return 0;

        return 1;
    }

}
