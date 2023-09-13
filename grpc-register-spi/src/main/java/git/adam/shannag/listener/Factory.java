package git.adam.shannag.listener;


import git.adam.shannag.environment.Environment;
import grpc.event.KeycloakEventServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class Factory implements EventListenerProviderFactory {
    private static final Logger log = Logger.getLogger(Factory.class);
    ManagedChannel channel = null;
    KeycloakEventServiceGrpc.KeycloakEventServiceBlockingStub stub = null;

    Environment env;

    private final String SPI_ID = "GRPC-REGISTER-SPI";

    public Factory() {
        this.env = new Environment();
    }

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        if (stub == null) {
            if (configureGRPCChannel()) {
                log.info(String.format("%s: creating new blocking stub", getId()));
                stub = KeycloakEventServiceGrpc.newBlockingStub(channel);
            }
        }
        return new Provider(stub);
    }

    public void init(Config.Scope scope) {
        log.info(String.format("%s: init called, creating the channel", getId()));

    }

    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        log.info(String.format("%s: post-init called", getId()));
    }

    public void close() {
        log.info(String.format("%s: close called", getId()));
        if (channel != null) {
            channel.shutdownNow();
        }
    }

    @Override
    public String getId() {
        return SPI_ID;
    }

    private boolean configureGRPCChannel() {
        log.info(String.format("%s: configuring channel without TLS", getId()));
        channel = ManagedChannelBuilder
                .forAddress(env.getEndpointHost(), env.getEndpointPort())
                .enableRetry()
                .usePlaintext()
                .build();

        if (channel == null) {
            log.error("GRPC channel not configured, returning null as provider. Provider configuration failed.");
            return false;
        }
        return true;
    }
}
