package plagueWorld.spread;

/**
 * Created by zhuhan on 14/12/17.
 */
public class InCountrySpread extends Spread{

    public int spread(String density, String location, int population, int infectedPopulation){
        double probability = 0;
        switch (density){
            case "high":
                probability = 0.05 + 0.03 * inCountrySpreadLevel;
                break;
            case "mid":
                probability = 0.05 + 0.02 * inCountrySpreadLevel;
                break;
            case "low":
                probability = 0.05 + 0.01 * inCountrySpreadLevel;
                break;
            default:
                break;
        }

        switch (location){
            case "tropical":
                probability -= (3-hotResistance) * 0.01;
                break;
            case "temperate":
                probability -= (3-hotResistance) * 0.005 + (3-coldResistance) * 0.005;
                break;
            case "boreal":
                probability -= (3-coldResistance) * 0.01;
        }

        double probInfected = probability * infectedPopulation;
        if(probInfected < 1){
            return java.lang.Math.random() < probInfected ? 1 : 0;
        }
        return (int)(probInfected * (1 - (float) infectedPopulation / population));
    }

    public int spread(double f, int population, int infectedPopulation){
        return 0;
    }
}
