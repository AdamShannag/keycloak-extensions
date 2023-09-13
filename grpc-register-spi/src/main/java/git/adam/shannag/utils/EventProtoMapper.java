package git.adam.shannag.utils;

import org.jboss.logging.Logger;
import org.keycloak.events.EventType;

public class EventProtoMapper {

    private static final Logger log = Logger.getLogger(EventProtoMapper.class);

    public static grpc.event.Event.EventType toProto(EventType t) {
        grpc.event.Event.EventType rt = grpc.event.Event.EventType.UNKNOWN;
        try {
            if (t == null) {
                log.warn(String.format("eventTypeToProto: t is null '%s', returning UNKNOWN.", t.name()));
                return rt;
            }
            rt = grpc.event.Event.EventType.valueOf(t.name());
        } catch (IllegalArgumentException ex) {
            log.warn(String.format("eventTypeToProto: Unknown value '%s', returning UNKNOWN.", t.name()));
        }
        return rt;
    }
}
