package chessapp.client.main;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;

public class JsonResponseWriterHelper {
	public static void writeResponse(HttpServletResponse response, JsonObjectBuilder objectBuilder) {
		try(Writer writer = new StringWriter()) {
		    Json.createWriter(writer).write(objectBuilder.build());
		    response.getWriter().write(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
