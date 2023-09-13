package git.adam.shannag.listener;

import git.adam.shannag.utils.Serializer;
import grpc.event.KeycloakEventServiceGrpc;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;

import static git.adam.shannag.utils.EventProtoMapper.toProto;


public class Provider implements EventListenerProvider {
    private KeycloakEventServiceGrpc.KeycloakEventServiceBlockingStub stub;

    public Provider(KeycloakEventServiceGrpc.KeycloakEventServiceBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public void onEvent(Event event) {
        if (EventType.REGISTER.equals(event.getType())) {
            grpc.event.Event protoEvent = grpc.event.Event.newBuilder()
                    .setTime(event.getTime())
                    .setType(toProto(event.getType()))
                    .setRealmId(Serializer.toNullableString(event.getRealmId()))
                    .setClientId(Serializer.toNullableString(event.getClientId()))
                    .setUserId(Serializer.toNullableString(event.getUserId()))
                    .setSessionId(Serializer.toNullableString(event.getSessionId()))
                    .setIpAddress(Serializer.toNullableString(event.getIpAddress()))
                    .setError(Serializer.toNullableString(event.getError()))
                    .putAllDetails(Serializer.handleMaybeNullMap(event.getDetails()))
                    .build();

            grpc.event.EventRequest request = grpc.event.EventRequest.newBuilder()
                    .setEvent(protoEvent)
                    .build();

            stub.onEvent(request);
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
