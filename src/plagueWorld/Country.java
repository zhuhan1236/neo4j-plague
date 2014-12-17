package plagueWorld;

import plagueWorld.connection.*;
import plagueWorld.spread.*;

import java.util.List;

/**
 * Created by zhuhan on 14/12/16.
 */
public class Country {
    public String name;
    public int population;
    public int infectedPopulation;
    public String location; //tropical temperate boreal
    public String populationDensity; //high mid low
    public List<Connection> borders;
}
