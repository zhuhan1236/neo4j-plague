package model;

import java.io.File;
import java.util.ArrayList;
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
	private static final String DB_PATH = "target/neo4j-hello-db";
	GraphDatabaseService graphDb;
	Relationship relationship;
	
	private static enum RelTypes implements RelationshipType
    {
        Arrived
    }
	//get all nodes in G
	public ArrayList<Country> getAllNodesinDatabase(){
		ExecutionEngine engine = new ExecutionEngine( graphDb );
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
                newC.name = (String) node.getProperty("name");
                ac.add(newC);
            }
            // END SNIPPET: items
        }
		return ac;
	}
	
	
	public ArrayList<Connection> getAllConnectionOfCountry(long cId){
		Node n = graphDb.getNodeById(cId);
		Iterator<Relationship> rs = n.getRelationships().iterator();
		ArrayList<Connection> ac = new ArrayList<>();
		String m;
		while (rs.hasNext()){
			Relationship temp = rs.next();
			Node leftN = temp.getEndNode();
			Node rightN = temp.getStartNode();
			m = (String) temp.getProperty("connectionMethod");
			if (m == "Air"){
				AirConnection acn = new AirConnection();
				acn.leftCountry = new Country();
				acn.leftCountry.name = (String) leftN.getProperty("countryName");
				acn.rightCountry = new Country();
				acn.rightCountry.name = (String) rightN.getProperty("countryName");
				ac.add(acn);
			}else if(m =="Land"){
				LandConnection lcn = new LandConnection();
				lcn.leftCountry = new Country();
				lcn.leftCountry.name = (String) leftN.getProperty("countryName");
				lcn.rightCountry = new Country();
				lcn.rightCountry.name = (String) rightN.getProperty("countryName");
				ac.add(lcn);
			}else if(m == "Ocean"){
				OceanConnection ocn = new OceanConnection();
				ocn.leftCountry = new Country();
				ocn.leftCountry.name = (String) leftN.getProperty("countryName");
				ocn.rightCountry = new Country();
				ocn.rightCountry.name = (String) rightN.getProperty("countryName");
				ac.add(ocn);
			}
		}
		return ac;
	}
	
	
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
		c.name = (String) countryNodes.get(0).getProperty( "countryName" );
		
		return c;
	}
	
	public boolean startDatabase(){
		try ( Transaction tx = graphDb.beginTx() )
        {
            
            tx.success();
        }
		return false;
	}
	
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
	
	public void updateNode(long cId, String p,String value){
		Node cNode = graphDb.getNodeById( cId );
		cNode.setProperty(p, value);
	}
	
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
	
	public void updateRelationship(long id,String p,String value){
		try ( Transaction tx = graphDb.beginTx() )
        {
            Relationship r = graphDb.getRelationshipById(id);
            r.setProperty(p, value);
            tx.success();
        }
	}
	
	public void createDb()
    {
        deleteFileOrDirectory( new File( DB_PATH ) );
        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb

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
	
}
