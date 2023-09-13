package git.adam.shannag.environment;

import org.jboss.logging.Logger;

public class Environment {
    private final String endpointHostKey = "REGISTER_SPI_GRPC_ENDPOINT_HOST";
    private final String endpointPortKey = "REGISTER_SPI_GRPC_ENDPOINT_PORT";

    private static final Logger log = Logger.getLogger(Environment.class);


    public String getEndpointHostKey() {
        return endpointHostKey;
    }

    public String getEndpointPortKey() {
        return endpointPortKey;
    }

    public String getEndpointHost() {
        String host = System.getenv(this.endpointHostKey);
        if (host.isEmpty()) {
            log.warn(String.format("Failed to read %s, using default value 127.0.0.1 instead.", this.endpointHostKey));
            host = "127.0.0.1";
        }
        return host;
    }

    public int getEndpointPort() {
        int port = 5000;
        try {
            port = Integer.parseInt(System.getenv(this.endpointPortKey));
        } catch (NumberFormatException ex) {
            log.warn(String.format("Failed to read %s, using default value 5000 instead.", this.endpointPortKey));
        }
        return port;
    }
}
