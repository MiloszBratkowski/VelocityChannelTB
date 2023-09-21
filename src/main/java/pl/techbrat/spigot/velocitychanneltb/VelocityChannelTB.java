package pl.techbrat.spigot.velocitychanneltb;

import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;

import java.io.File;
import java.lang.module.Configuration;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "velocitychanneltb",
        name = "VelocityChannelTB",
        version = "1.0",
        description = "This plugin enabling communication for TB plugins between Spigot servers.",
        authors = {"TechBrat"}
)
public class VelocityChannelTB {
    private Logger logger;
    private ProxyServer server;
    private Path dataDirectory;

    private Configuration config;

    @Inject
    public VelocityChannelTB(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.server = server;
        this.dataDirectory = dataDirectory;
        logger.info("Enabling plugin...");
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("techbrat:channel"));
        logger.info("Velocity channel for velocity-spigot plugins enabled!");
        //makeConfig();


        logger.info("Plugin successfully enabled!");
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getIdentifier().getId().equals("techbrat:channel")) {
            debugLog("Received data from Spigot server: " + new String(event.getData()));
            for (RegisteredServer server : server.getAllServers()) {
                debugLog("Sent data to "+server.getServerInfo().getName());
                server.sendPluginMessage(MinecraftChannelIdentifier.from("techbrat:channel"), event.getData());
            }
        }
    }

    /*private void makeConfig() {
        try {
            if (!dataDirectory.toFile().exists()) {
                dataDirectory.toFile().mkdir();
                logger.info("Created config folder.");
            }

            File config = new File(dataDirectory.toFile(), "config.yml");

            if (!config.exists()) {

                getClass().getResourceAsStream("config.yml").transferTo(Files.newOutputStream(config.toPath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public boolean isDebug() {
        return false;
    }

    public boolean debugLog(String debug) {
        if (isDebug()) {
            logger.info(debug);
        }
        return isDebug();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}
