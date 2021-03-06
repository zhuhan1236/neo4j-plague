package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.IteratorUtil;

import plagueWorld.Country;
import plagueWorld.connection.AirConnection;
import plagueWorld.connection.Connection;
import plagueWorld.connection.LandConnection;
import plagueWorld.connection.OceanConnection;

public class OurNeo4j {
	private static final String DB_PATH = "data/plague";
	private HashMap<String, Country> allCountry = new HashMap<>();
	GraphDatabaseService graphDb;
	Relationship relationship;
	ExecutionEngine engine;
	
	private static enum RelTypes implements RelationshipType
    {
        Arrived
    }
	//get all nodes in G
	//pass
	public ArrayList<Country> getAllNodesinDatabase(){
		//engine = new ExecutionEngine( graphDb );
		ExecutionResult result;
		ArrayList<Country> ac = new ArrayList<>();
		try ( Transaction ignored = graphDb.beginTx() )
        {
            result = engine.execute( "match (n) return n" );
            // END SNIPPET: execute
            // START SNIPPET: items
            Iterator<Node> n_column = result.columnAs( "n" );
            for ( Node node : IteratorUtil.asIterable( n_column ) )
            {
                // note: we're grabbing the name property from the node,
                // not from the n.name in this case.
                Country newC = new Country();
                //need id shuxing
                newC.name = (String) node.getProperty("countryName");
				newC.id = node.getId();
				newC.population = Integer.parseInt((String) node.getProperty("population"));
				newC.location = (String) node.getProperty("location");
				newC.populationDensity = (String) node.getProperty("density");
                ac.add(newC);
				allCountry.put(newC.name, newC);
            }
            // END SNIPPET: items
        }
		return ac;
	}
	
	
	public ArrayList<Connection> getAllConnectionOfCountry(long cId){
		Iterator<Relationship> rs;
		ArrayList<Connection> ac = new ArrayList<>();
		String m;
		try ( Transaction tx = graphDb.beginTx()){
			Node n = graphDb.getNodeById(cId);
			rs = n.getRelationships().iterator();
			
			while (rs.hasNext()){
				Relationship temp = rs.next();
				Node leftN = temp.getStartNode();
				Node rightN = temp.getEndNode();
				m = (String) temp.getProperty("type");
				if (m.equals("air")){
					AirConnection acn = new AirConnection();
					acn.leftCountry = allCountry.get(leftN.getProperty("countryName"));
					acn.rightCountry = allCountry.get(rightN.getProperty("countryName"));
					acn.flightFrequency = Double.parseDouble((String)temp.getProperty("property"));
					ac.add(acn);
				}else if(m.equals("land")){
					LandConnection lcn = new LandConnection();
					lcn.leftCountry = allCountry.get(leftN.getProperty("countryName"));
					lcn.rightCountry = allCountry.get(rightN.getProperty("countryName"));
					lcn.contactFrequency = Double.parseDouble((String)temp.getProperty("property"));
					ac.add(lcn);
				}else if(m.equals("ocean")){
					OceanConnection ocn = new OceanConnection();
					ocn.leftCountry = allCountry.get(leftN.getProperty("countryName"));
					ocn.rightCountry = allCountry.get(rightN.getProperty("countryName"));
					ocn.shipFrequency = Double.parseDouble((String)temp.getProperty("property"));
					ac.add(ocn);
				}
			}
			tx.success();
		}
		return ac;
	}
	
	//pass
	public Country getNodeByName(String cName){
		Label label = DynamicLabel.label( "Country" );
		ArrayList<Node> countryNodes = new ArrayList<>();
		try ( Transaction tx = graphDb.beginTx();
                ResourceIterator<Node> countries = graphDb
                      .findNodesByLabelAndProperty( label, "countryName", cName )
                      .iterator() )
        {
            while ( countries.hasNext() ){
            	countryNodes.add( countries.next() );
            }
            countries.close();
            
        }
		Country c = new Country();
		try ( Transaction tx = graphDb.beginTx()){
			c.name = (String) countryNodes.get(0).getProperty( "countryName" );
			tx.success();
		}
		
		return c;
	}
	
//	public boolean startDatabase(){
//		try ( Transaction tx = graphDb.beginTx() )
//        {
//            
//            tx.success();
//        }
//		return false;
//	}
	
	//pass
	public long createNode(String cName){
		Node cNode;
		try ( Transaction tx = graphDb.beginTx() )
        {
			Label label = DynamicLabel.label( "Country" );
			cNode = graphDb.createNode( label );
			cNode.setProperty( "countryName", cName );
            tx.success();
        }
		
		return cNode.getId();
	}
	
	//pass
	public void updateNode(long cId, String p,String value){
		try ( Transaction tx = graphDb.beginTx() ){
			Node cNode = graphDb.getNodeById( cId );
				cNode.setProperty(p, value);
				tx.success();
		}
	}
	
	//pass
	public long createRelationship(long id1, long id2){
		Relationship r;
		try ( Transaction tx = graphDb.beginTx() )
        {
            Node a = graphDb.getNodeById(id1);
            Node b = graphDb.getNodeById(id2);
            r = a.createRelationshipTo(b,RelTypes.Arrived);
            tx.success();
        }
		return r.getId();
	}
	
	//pass
	public void updateRelationship(long id,String p,String value){
		try ( Transaction tx = graphDb.beginTx() )
        {
            Relationship r = graphDb.getRelationshipById(id);
            r.setProperty(p, value);
            tx.success();
        }
	}
	
	//pass	
	public void createDb()
    {
        deleteFileOrDirectory( new File( DB_PATH ) );
        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb
        engine = new ExecutionEngine( graphDb );
        // START SNIPPET: transaction
        IndexDefinition indexDefinition;
        try ( Transaction tx = graphDb.beginTx() )
        {   
        	Schema schema = graphDb.schema();
        	indexDefinition = schema.indexFor( DynamicLabel.label( "Country" ) )
                        .on( "countryName" )
                        .create();

            // START SNIPPET: transaction
            tx.success();
        }
        
        try ( Transaction tx = graphDb.beginTx() )
        {
            Schema schema = graphDb.schema();
            schema.awaitIndexOnline( indexDefinition, 10, TimeUnit.SECONDS );
        }
        
        // END SNIPPET: transaction
    }
	
	public void loadCountryByCSV(String fileName){
		try ( Transaction tx = graphDb.beginTx() )
		{
			engine.execute("LOAD CSV WITH HEADERS FROM '" + fileName + "' AS line CREATE (:Country { countryName: line.name, population: line.population, location:line.location,density:line.density})");
			tx.success();
		}
	}
	
//	public void loadPlagueByCVS(String fileName){
//		try ( Transaction tx = graphDb.beginTx() )
//		{
//			engine.execute("LOAD CSV FROM '" + fileName + "' AS line CREATE (:Plague { countryName: line[0], population: line[1], location:line[2],density:line[3]})");
//			tx.success();
//		}
//	}
	//fileName needs to add "file://"
	
	public void loadRelationshipByCSV(String fileName){
		try ( Transaction tx = graphDb.beginTx() )
		{
			engine.execute("LOAD CSV WITH HEADERS FROM '" + fileName + "' AS line MATCH (c1:Country { countryName: line.country1}), (c2:Country { countryName: line.country2}) CREATE (c1)-[:Arrived {type:line.type,property:line.property}]->(c2)");
			tx.success();
		}
	}
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
	
	private static void deleteFileOrDirectory( File file )
    {
        if ( file.exists() )
        {
            if ( file.isDirectory() )
            {
                for ( File child : file.listFiles() )
                {
                    deleteFileOrDirectory( child );
                }
            }
            file.delete();
        }
    }
	
	//pass
	public void shutDownDb(){
		 System.out.println();
	     System.out.println( "Shutting down database ..." );
	     // START SNIPPET: shutdownServer
	     graphDb.shutdown();
	     // END SNIPPET: shutdownServer
	}
	
	//pass
	public void deleteNodeById(long id){
		try ( Transaction tx = graphDb.beginTx() ){
			graphDb.getNodeById(id).delete();
			tx.success();
		}
	}
	
	//pass
	public void loadDb(){
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( graphDb );
        engine = new ExecutionEngine( graphDb );
	}
}
