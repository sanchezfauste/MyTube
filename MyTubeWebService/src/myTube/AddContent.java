package myTube;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class AddContent extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		StringBuilder jsonString = new StringBuilder();
		BufferedReader br = req.getReader();
		String line;
		while ((line = br.readLine()) != null) {
            jsonString.append(line);
        }
		br.close();
		Content content;
		try {
			JSONObject jsonContent = new JSONObject(jsonString.toString());
			content = ContentManager.addContent(
					jsonContent.getString("title"), 
					jsonContent.getString("description"),
					jsonContent.getString("host"),
					jsonContent.getInt("port"),
					jsonContent.getString("registryName"));
		} catch (JSONException ex) {
			content = null;
		}
		if (content != null) {
			resp.getWriter().print(content.toJson().toString());
		} else {
			resp.getWriter().println("{\"error\": \"This content already exists\"}");
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		resp.getWriter()
				.print("<form action=\"/addcontent\" method=\"post\">\n"
						+ "Title:<br>\n"
						+ "<input type=\"text\" name=\"title\" value=\"\">\n"
						+ "<br>\n"
						+ "Description:<br>\n"
						+ "<input type=\"text\" name=\"description\" value=\"\">\n"
						+ "<br>\n"
						+ "Host:<br>\n"
						+ "<input type=\"text\" name=\"host\" value=\"\">\n"
						+ "<br>\n"
						+ "Port:<br>\n"
						+ "<input type=\"text\" name=\"port\" value=\"\">\n"
						+ "<br>\n"
						+ "RegistryName:<br>\n"
						+ "<input type=\"text\" name=\"registryName\" value=\"\">\n"
						+ "<br><br>\n"
						+ "<input type=\"submit\" value=\"Submit\">\n"
						+ "</form>\n");
	}
}
