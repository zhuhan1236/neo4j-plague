package view;

import model.OurNeo4j;

import java.io.File;

/**
 * Created by zhuhan on 14/12/22.
 */
public class Init {
    public static void main(String[] args){
        OurNeo4j neo4j = new OurNeo4j();
        File dir = new File("./data/plague");
        if (!dir.exists()){
            dir.mkdirs();
            neo4j.createDb();
            neo4j.loadCountryByCSV("file:./map/country.csv");
            neo4j.loadRelationshipByCSV("file:./map/connection.csv");
            System.out.println("create neo4j database successfully!");
        }
        neo4j.shutDownDb();
    }
}
