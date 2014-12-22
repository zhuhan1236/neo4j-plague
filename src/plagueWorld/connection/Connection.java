package plagueWorld.connection;

import plagueWorld.Country;
import plagueWorld.spread.*;
/**
 * Created by zhuhan on 14/12/16.
 */
public abstract class Connection {
    public Country leftCountry;
    public Country rightCountry;
    public Spread plagueSpread;

    public Country getAnotherCountry(Country c){
        if (leftCountry.id == rightCountry.id) return null;
        if (leftCountry.id == c.id) return rightCountry;
        return leftCountry;
    }

    public abstract double getFrequency();
}