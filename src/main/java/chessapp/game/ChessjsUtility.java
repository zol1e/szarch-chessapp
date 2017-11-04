package chessapp.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.*;

public class ChessjsUtility {

	private ScriptEngine engine;
	
	private ScriptContext ctx;
	
	private Invocable inv;
	
	public void ChessjsUtility() {
		try {
			ScriptEngineManager factory = new ScriptEngineManager();
			engine = factory.getEngineByName("JavaScript");
			engine.eval(
					Files.newBufferedReader(Paths.get("./src/main/webapp/chessjs/chess.js"), StandardCharsets.UTF_8));
			
		    ctx = new SimpleScriptContext();
		    ScriptContext defaultCtx = engine.getContext();
		    Bindings engineBindings = defaultCtx.getBindings(ScriptContext.ENGINE_SCOPE);
		    ctx.setBindings(engineBindings, ScriptContext.ENGINE_SCOPE);
		    
			inv = (Invocable) engine;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeRandomMoveFromPosition(String fen) {
		Object game = engine.get("game");
		/*try {
			inv.invokeMethod(game, "load", fen);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
}
