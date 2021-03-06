package halfpipe.cli;

import halfpipe.configuration.Configuration;
import halfpipe.jersey.JerseyLogger;
import halfpipe.logging.Log;
import halfpipe.jetty.JettyServletEnvironment;
import halfpipe.jetty.ServerFactory;
import org.apache.commons.cli.CommandLine;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 11:44 PM
 */
@Component
@Profile("CLI")
public class HalfpipeServer implements CommandMarker {
    private static final Logger LOG = Log.forThisClass();

    @Inject
    Configuration config;

    @Inject
    ServerFactory serverFactory;

    @Inject
    JerseyLogger jerseyLogger;

    @CliAvailabilityIndicator({"server"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "server", help = "run halfpipe in jetty http server")
    public String server(
            @CliOption(key = {"", "config"}, mandatory = true, help = "config file")
            String config ) throws Exception
    {
        run(null);
        return null;
    }

    public void run(CommandLine commandLine) throws Exception {
        Server server = serverFactory.buildServer(new JettyServletEnvironment());

        jerseyLogger.logEndpoints();

        LOG.trace("staring jetty on port {}", config.http.port.get());
        server.start();
        LOG.trace("waiting for connections on port {}", config.http.port.get());
        server.join();
    }

}
