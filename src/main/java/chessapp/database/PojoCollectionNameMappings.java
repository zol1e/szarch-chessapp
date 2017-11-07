package chessapp.database;

import java.util.HashMap;
import java.util.Map;

import chessapp.shared.entities.AbstractEntity;
import chessapp.shared.entities.ChessGame;
import chessapp.shared.entities.User;

public class PojoCollectionNameMappings {

	private static Map<Class<? extends AbstractEntity>, String> pojoMappings = new HashMap<Class<? extends AbstractEntity>, String>();

	static {
		pojoMappings.put(User.class, "Users");
		pojoMappings.put(ChessGame.class, "ChessGames");
	}

	public static String getCollectionName(Class<? extends AbstractEntity> entity) {
		return pojoMappings.get(entity);
	}
	
}