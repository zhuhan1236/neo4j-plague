package plagueWorld.spread;

/**
 * Created by zhuhan on 14/12/17.
 */
public class InCountrySpread extends Spread{

    public int spread(String density, String location, int population, int infectedPopulation){
        double probability = 0;
        switch (density.charAt(0)){
            case 'h':
                probability = 0.05 + 0.03 * inCountrySpreadLevel;
                break;
            case 'm':
                probability = 0.05 + 0.02 * inCountrySpreadLevel;
                break;
            case 'l':
                probability = 0.05 + 0.01 * inCountrySpreadLevel;
                break;
            default:
                break;
        }

        switch (location.charAt(1)){
            case 'r':
                probability -= (3-hotResistance) * 0.01;
                break;
            case 'e':
                probability -= (3-hotResistance) * 0.005 + (3-coldResistance) * 0.005;
                break;
            case 'o':
                probability -= (3-coldResistance) * 0.01;
        }

        int probInfected = (int)(probability * infectedPopulation);
        return (int)(probInfected * (1 - (float) infectedPopulation / population));
    }

}
