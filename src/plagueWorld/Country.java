package plagueWorld;

import plagueWorld.connection.*;
import plagueWorld.spread.*;

import java.util.List;

/**
 * Created by zhuhan on 14/12/16.
 */
public class Country {
    public long id;
    public String name;
    public int population;
    public int infectedPopulation = 0;
    public String location; //tropical temperate boreal
    public String populationDensity; //high mid low
    public List<Connection> borders;
}
