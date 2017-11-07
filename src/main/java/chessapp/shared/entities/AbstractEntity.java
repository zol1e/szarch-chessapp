package chessapp.shared.entities;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

/**
 * Azért van, hogy általános módon megoldható legyen a db szintű tárolás.
 * Ide jönnek majd az általánosan kezelhető dolgok.
 * Például create date, id stb.
 */

@BsonDiscriminator
public class AbstractEntity {

}
