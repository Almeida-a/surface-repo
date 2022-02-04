package pt.ua.rsi.dicoogle.plugin.webservice;

import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ua.dicoogle.sdk.JettyPluginInterface;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.PlatformCommunicatorInterface;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder;

public class SurfaceRepoJettyPlugin implements JettyPluginInterface, PlatformCommunicatorInterface {

    private static final Logger logger = LoggerFactory.getLogger(SurfaceRepoJettyPlugin.class);
    private final SurfaceRepoService webService;
    private boolean enabled;
    private ConfigurationHolder settings;
    private DicooglePlatformInterface platform;

    public SurfaceRepoJettyPlugin() {
        this.webService = new SurfaceRepoService();
        this.enabled = true;
    }

    public void setPlatformProxy(DicooglePlatformInterface pi) {
        this.platform = pi;

        this.webService.setPlatformProxy(pi);
    }

    @Override
    public String getName() {
        return "sample-plugin-jetty";
    }

    @Override
    public boolean enable() {
        this.enabled = true;
        return true;
    }

    @Override
    public boolean disable() {
        this.enabled = false;
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public ConfigurationHolder getSettings() {
        return settings;
    }

    @Override
    public void setSettings(ConfigurationHolder settings) {
        this.settings = settings;
    }

    public HandlerList getJettyHandlers() {
        // encapsulate servlets into holders, then add them to handlers.
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/surfacerepo");
        handler.addServlet(new ServletHolder(this.webService), "/facets");

        // add all handlers to a handler list and return it
        HandlerList l = new HandlerList();
        l.addHandler(handler);

        return l;
    }

}
