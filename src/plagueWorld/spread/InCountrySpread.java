package plagueWorld.spread;

/**
 * Created by zhuhan on 14/12/17.
 */
public class InCountrySpread extends Spread{

    public int spread(String density, String location, int population, int infectedPopulation){
        double probability = 0;
        switch(density){
            case "hihg":
                probability = 0.05 + 0.03 * inCountrySpreadLevel;
                break;
            case "mid":
                probability = 0.05 + 0.02 * inCountrySpreadLevel
                break;
            case "low":
                probability = 0.05 + 0.01 * inCountrySpreadLevel
                break;
            default:
                break;
        }
        int probInfected = (int)(probability * infectedPopulation);
        int newInfected = (int)(probInfected * (1 - (float) infectedPopulation / population));

        return newInfected;
    }

}
