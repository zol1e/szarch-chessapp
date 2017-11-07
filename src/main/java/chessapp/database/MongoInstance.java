package chessapp.database;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Alkalmazás szintű adatbázis példány.
 * 
 */
public class MongoInstance {

	private String databaseName;

	private String address;

	private MongoClient mongoClient;

	private MongoDatabase database;

	private CodecRegistry pojoCodecRegistry;

	public MongoInstance(String address, int port, String databaseName) {
		mongoClient = new MongoClient(address, port);
		database = database.withCodecRegistry(pojoCodecRegistry);

		this.databaseName = databaseName;
		this.address = address;
	}

	public MongoDatabase getDatabase() {
		return database;
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
