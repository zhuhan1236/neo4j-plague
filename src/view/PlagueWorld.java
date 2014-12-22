package view;

import model.OurNeo4j;
import plagueWorld.Country;
import plagueWorld.connection.*;
import plagueWorld.spread.AirSpread;
import plagueWorld.spread.InCountrySpread;
import plagueWorld.spread.LandSpread;
import plagueWorld.spread.WaterSpread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuhan on 14/12/18.
 */
public class PlagueWorld {
    public static void main(String[] args){
        PlagueWorld plagueWorld = new PlagueWorld();
        plagueWorld.gui = new PlagueWorldGUI();
        plagueWorld.gui.init(plagueWorld);
    }

    public String infectSource;
    PlagueWorldGUI gui;
    public int stepTime;
    public HashMap<String, Country> allCountry = new HashMap<>();
    public HashMap<String, Country> infectedCountry = new HashMap<>();
    public int time = 0;

    public void updateInfectList(){
        String[] data = new String[allCountry.size()];
        int i = 0;
        for(String key : infectedCountry.keySet()){
            Country tempCountry = infectedCountry.get(key);
            String tempStr = tempCountry.name + "                    ";
            tempStr = tempStr.substring(0, 20);
            tempStr += Integer.toString(tempCountry.infectedPopulation) + "/" + Integer.toString(tempCountry.population);
            data[i++] = tempStr;
        }
        for(String key : allCountry.keySet()){
            if(infectedCountry.get(key) != null) continue;
            Country tempCountry = allCountry.get(key);
            String tempStr = tempCountry.name + "                    ";
            tempStr = tempStr.substring(0, 20);
            tempStr += Integer.toString(tempCountry.infectedPopulation) + "/" + Integer.toString(tempCountry.population);
            data[i++] = tempStr;
        }
        gui.infectLi.setListData(data);
    }

    public void updateConnectionList(){
        int connectionNum = 0;
        for(String key : allCountry.keySet()){
            connectionNum += allCountry.get(key).borders.size()-1;
        }
        String[] data = new String[connectionNum];
        int i = 0;
        for(String key : allCountry.keySet()){
            List<Connection> tempConnection = allCountry.get(key).borders;
            for(Connection con : tempConnection){
                Country anotherCountry;
                if((anotherCountry = con.getAnotherCountry(allCountry.get(key))) == null) continue;
                String tempStr = allCountry.get(key).name + "                ";
                tempStr = tempStr.substring(0, 20);
                if (con instanceof AirConnection){
                    tempStr += "flight to" + "                       ";
                }
                else if(con instanceof LandConnection){
                    tempStr += "next to" + "                      ";
                }
                else if(con instanceof OceanConnection){
                    tempStr += "ship to" + "                      ";
                }
                tempStr = tempStr.substring(0, 40);
                tempStr += anotherCountry.name;
                data[i++] = tempStr;
            }
        }
        gui.connectionLi.setListData(data);
    }

    public boolean loadDatabase(){
        OurNeo4j neo4j = new OurNeo4j();
        neo4j.loadDb();

        ArrayList<Country> country = neo4j.getAllNodesinDatabase();
        for(Country c : country){
            allCountry.put(c.name, c);
            c.borders = neo4j.getAllConnectionOfCountry(c.id);

            Connection tempConnection = new SelfConnection();
            tempConnection.leftCountry = c;
            tempConnection.rightCountry = c;
            c.borders.add(tempConnection);

            for(Connection con : c.borders){
                if(con instanceof AirConnection){
                    con.plagueSpread = new AirSpread();
                }
                else if(con instanceof LandConnection){
                    con.plagueSpread = new LandSpread();
                }
                else if(con instanceof OceanConnection){
                    con.plagueSpread = new WaterSpread();
                }
                else if(con instanceof SelfConnection){
                    con.plagueSpread = new InCountrySpread();
                }
                else{
                    System.out.println("error in connection");
                }
            }
        }

        neo4j.shutDownDb();
        updateInfectList();
        updateConnectionList();
        return true;
    }

    public boolean infectCountry(String country){
        this.infectSource = country;
        Country source;
        if((source = allCountry.get(infectSource)) != null){
            source.infectedPopulation = 1;
            infectedCountry.put(infectSource, source);
            updateInfectList();
            return true;
        }
        return false;
    }

    public void spread(){
        HashMap<String, Country> newInfectedMap = new HashMap<>();
        for(String key : infectedCountry.keySet()){
            Country tempCountry = infectedCountry.get(key);
            List<Connection> connections = tempCountry.borders;
            for(Connection c : connections){
                Country anotherCountry;
                if((anotherCountry = c.getAnotherCountry(tempCountry)) != null){
                    int infectedInc = c.plagueSpread.spread(c.getFrequency(),
                            tempCountry.population, tempCountry.infectedPopulation);
                    if((anotherCountry.infectedPopulation == 0) && (infectedInc > 0)){
                        newInfectedMap.put(anotherCountry.name, anotherCountry);
                    }
                    anotherCountry.infectedPopulation += infectedInc;
                    anotherCountry.infectedPopulation = anotherCountry.infectedPopulation > anotherCountry.population ? anotherCountry.population : anotherCountry.infectedPopulation;
                }
                else{
                    if(tempCountry.infectedPopulation == 0) continue;

                    int infectedInc = ((InCountrySpread)(c.plagueSpread)).spread(
                            tempCountry.populationDensity,
                            tempCountry.location, tempCountry.population,
                            tempCountry.infectedPopulation);
                    tempCountry.infectedPopulation += infectedInc;
                    tempCountry.infectedPopulation = tempCountry.infectedPopulation > tempCountry.population ? tempCountry.population : tempCountry.infectedPopulation;
                }
            }
        }

        if(newInfectedMap.size() > 0){
            String msg = "";
            for(String key : newInfectedMap.keySet()){
                infectedCountry.put(key, newInfectedMap.get(key));
                msg += key + ", ";
            }
            msg = msg.substring(0, msg.length() - 2);
            msg += " infected!";
            gui.showDialog(msg);
        }
    }

    public boolean canSpread(){
        if(infectedCountry.size() < allCountry.size()){
            return true;
        }
        for(String key : infectedCountry.keySet()){
            if(infectedCountry.get(key).infectedPopulation < infectedCountry.get(key).population){
                return true;
            }
        }
        return false;
    }

    public void go(int time){
        this.stepTime = time;
        for(int i = 0;i < stepTime;++i){
            if(canSpread()){
                spread();
                this.time++;
            }
            else{
                gui.showDialog("All people infected in the world at day " + Integer.toString(this.time) + "!");
                gui.setTime(this.time);
                updateInfectList();
                return;
            }
        }
        updateInfectList();
        gui.setTime(this.time);
    }
}