package myTube;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.labs.repackaged.org.json.JSONArray;

@SuppressWarnings("serial")
public class GetContentsFromKeyword extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		if (req.getParameterMap().containsKey("keyword")) {
			List<Content> contents = ContentManager.getContentsFromKeyword(
					req.getParameter("keyword"));
			resp.setContentType("application/json");
			resp.setCharacterEncoding("utf-8");
			JSONArray jsonContents = new JSONArray();
			for (Content c : contents) {
				jsonContents.put(c.toJson());
			}
			resp.getWriter().print(jsonContents.toString());
		} else {
			resp.getWriter().println("{\"error\": \"No keyword specified\"}");
		}
	}
}
