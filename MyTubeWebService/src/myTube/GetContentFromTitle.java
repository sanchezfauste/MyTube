package myTube;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class GetContentFromTitle extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		if (req.getParameterMap().containsKey("title")) {
			Content content = ContentManager.getContentFromTitle(
					req.getParameter("title"));
			if (content != null) {
				resp.getWriter().print(content.toJson().toString());
			} else {
				resp.getWriter().println("{\"error\": \"No matching content with this title\"}");
			}
		} else {
			resp.getWriter().println("{\"error\": \"No title specified\"}");
		}
	}
}
