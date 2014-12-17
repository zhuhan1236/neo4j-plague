package plagueWorld;

import plagueWorld.connection.*;
import plagueWorld.spread.*;

import java.util.List;

/**
 * Created by zhuhan on 14/12/16.
 */
public class Country {
    public String name;
    public boolean infection;
    public double area;
    public int population;
    public String location; //tropical temperate boreal
    public List<Connection> borders;
}
