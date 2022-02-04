package pt.ua.rsi.dicoogle.plugin.webservice;

import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SurfaceRepoService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(SurfaceRepoService.class);

    private DicooglePlatformInterface platform;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int numberOfFacets = 1;
        // TODO return number of facets in json format

        response.setContentType("application/json");
        JSONObject o = new JSONObject();
        try {
            o.put("NumberOfFacets", numberOfFacets);
        } catch (JSONException e) {
            logger.warn("Failed to construct json:\n", e);
        }
        response.getWriter().print(o);

    }

    public DicooglePlatformInterface getPlatform() {
        return platform;
    }

    public void setPlatformProxy(DicooglePlatformInterface pi) {
        this.platform = pi;
    }
}
