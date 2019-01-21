import java.io.IOException;
import java.sql.*;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import oracle.dbtools.plugin.api.di.annotations.Provides;
import oracle.dbtools.plugin.api.http.annotations.*;
import oracle.dbtools.plugin.api.routes.*;

/**
 * This example plugin {@link HttpServlet} demonstrates:
 * <ul>
 * <li>Using the injected {@link Connection} to query the database.</li>
 * <li>Using the injected {@link PathTemplates} service to decode the parameters
 * of the servlet's {@link PathTemplateMatch}.</li>
 * </ul>
 * 
 * <h4>Testing the Servlet</h4> Invoke the servlet with the following URL:
 * 
 * <pre>
 *  http://<i>server</i>/ords/<i>schema</i>/demos/plugin?who=<i>somebody</i>
 * </pre>
 * 
 * where:
 * <ul>
 * <li><i>server</i> is the hostname and port of the server.</li>
 * <li><i>schema</i> is the name of the REST enabled database schema.</li>
 * <li><i>somebody</i> is any value you wish, e.g. a person's name.</li>
 * <ul>
 * For example:
 * 
 * <pre>
 *  http://localhost:8080/ords/test_schema/demos/plugin?who=Scott
 * </pre>
 * 
 * @author cdivilly
 *
 */
@Provides
@Dispatches(@PathTemplate("/demos/plugin"))
public class PluginDemo extends HttpServlet {
  @Inject
  PluginDemo(Connection conn, PathTemplates pathTemplates) {
    this.conn = conn;
    this.pathTemplates = pathTemplates;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    final PathTemplateMatch match = pathTemplates.matchedTemplate(request);
    try {
      String who = match.parameters().get("who");
      who = null == who ? "anonymous" : who;
      PreparedStatement ps = conn
          .prepareStatement("select sys_context('USERENV','CURRENT_USER') from dual");
      ResultSet rs = ps.executeQuery();
      rs.next();
      String user = rs.getString(1);
      response.getWriter().println(user + " says hello to: " + who);
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new ServletException(e);
    }
  }

  private final Connection    conn;
  private final PathTemplates pathTemplates;
}