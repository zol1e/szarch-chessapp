package database;

import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoInstance {

	private String databaseName;
	
	private String address;
	
	private MongoClient mongoClient;
	
	public MongoInstance(String address, int port, String databaseName) {
		mongoClient = new MongoClient(address, port);
		this.databaseName = databaseName;
		this.address = address;
	}
	
	public MongoDatabase getDatabase() {
		return mongoClient.getDatabase(databaseName);
	}
	
	public MongoCollection<Document> getCollection(String collectionName) {
		return mongoClient.getDatabase(databaseName).getCollection(collectionName);
	}
	
	/**
	 * Insert one document to the collection.
	 * 
	 * @param collectionName
	 * @param document
	 */
	public void insert(String collectionName, Document document) {
		MongoCollection<Document> collection = getCollection(collectionName);
		collection.insertOne(document);
	}
	
	/**
	 * Return all documents for the given collection name.
	 * 
	 * @param collectionName
	 * @return
	 */
	public FindIterable<Document> findAll(String collectionName) {
		return getCollection(collectionName).find();
	}
	
	public void closeConnection() {
		mongoClient.close();
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}
	
}
