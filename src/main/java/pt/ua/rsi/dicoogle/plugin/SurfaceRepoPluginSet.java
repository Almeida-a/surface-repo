package pt.ua.rsi.dicoogle.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ua.dicoogle.sdk.JettyPluginInterface;
import pt.ua.dicoogle.sdk.PluginSet;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder;
import pt.ua.rsi.dicoogle.plugin.webservice.SurfaceRepoJettyPlugin;

import java.util.Collection;
import java.util.Collections;

public class SurfaceRepoPluginSet implements PluginSet {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(SurfaceRepoPluginSet.class);

    // List of plugins
    private final SurfaceRepoJettyPlugin webService;

    // Additional resources added here
    private ConfigurationHolder settings;

    public SurfaceRepoPluginSet() {

        logger.info("Initializing SurfaceRepo Plugin Set");

        // Construct all plugins here
        this.webService = new SurfaceRepoJettyPlugin();

        logger.info("SurfaceRepo Plugin Set is ready");
    }

    @Override
    public Collection<? extends JettyPluginInterface> getJettyPlugins() {
        return Collections.singleton(this.webService);
    }

    @Override
    public String getName() {
        return "Surface Repository Plugin";
    }

    @Override
    public void setSettings(ConfigurationHolder configurationHolder) {
        settings = configurationHolder;
    }

    @Override
    public ConfigurationHolder getSettings() {
        return settings;
    }
}
