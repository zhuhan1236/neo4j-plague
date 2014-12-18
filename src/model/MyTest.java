package model;

import java.util.ArrayList;

import plagueWorld.Country;
import plagueWorld.connection.Connection;

public class MyTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OurNeo4j o = new OurNeo4j();
		//o.createDb();
		o.loadDb();
		System.out.println("load ok!");
		//long id = o.createNode("America");
		//System.out.println(id);
		//o.deleteNodeById(2);
		//ArrayList<Country> ac = o.getAllNodesinDatabase();
//		for (int i = 0;i < ac.size();i++){
//			System.out.println(ac.get(i).name);
//		}
//		System.out.println(ac.size());
		//Country cc = o.getNodeByName("China");
		//System.out.println(cc.name);
//		o.updateNode(1, "countryName", "Japan");
//		ArrayList<Country> ac = o.getAllNodesinDatabase();
//		for (int i = 0;i < ac.size();i++){
//			System.out.println(ac.get(i).name);
//		}
//		long rid = o.createRelationship(0, 1);
//		System.out.println("rid: " + rid);
		o.updateRelationship(0, "connectionMethod", "Air");
		
		ArrayList<Connection> acc = o.getAllConnectionOfCountry(0);
		System.out.println(acc.size());
		for (int i = 0;i < acc.size();i++){
			System.out.println(acc.get(i).leftCountry.name);
			System.out.println(acc.get(i).rightCountry.name);
		}
		o.shutDownDb();
	}

}
