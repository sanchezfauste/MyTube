package myTube;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class GetContentFromKey extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		if (req.getParameterMap().containsKey("key")) {
			Content content = ContentManager.getContentFromKey(
					Long.parseLong(req.getParameter("key")));
			if (content != null) {
				resp.getWriter().print(content.toJson().toString());
			} else {
				resp.getWriter().println("{\"error\": \"No matching content with this key\"}");
			}
		} else {
			resp.getWriter().println("{\"error\": \"No key specified\"}");
		}
	}
}
