package chessapp.database;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import chessapp.shared.entities.AbstractEntity;
import chessapp.shared.entities.ChessGame;
import chessapp.shared.entities.User;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Collection;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

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

		// ------------------------
		/*
		 * TODO: Lehet hogy így kell, próbálkozni kell.
		 */
		ClassModel<AbstractEntity> abstractEntityModel = ClassModel.builder(AbstractEntity.class)
				.enableDiscriminator(true).build();
		ClassModel<User> userModel = ClassModel.builder(User.class).enableDiscriminator(true).build();
		ClassModel<ChessGame> chessGameModel = ClassModel.builder(ChessGame.class).enableDiscriminator(true).build();
		PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.register(abstractEntityModel, userModel, chessGameModel).build();
		
		/*
		 * TODO: de lehet, hogy így:
		 */
		
		//PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().register("chessapp.shared.entities").build();

		// -------------------------
		
		pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
		mongoClient = new MongoClient(address, port);
		database = mongoClient.getDatabase(databaseName);
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

	/**
	 * Entitás létrehozásához.
	 */
	public void create(AbstractEntity entity) {
		String collectionName = PojoCollectionNameMappings.getCollectionName(entity.getClass());
		MongoCollection<Document> collection = getCollection(collectionName);
	}

}
