package git.adam.shannag.utils;

import java.util.HashMap;
import java.util.Map;

public class Serializer {
    public static grpc.shared.NullableString toNullableString(String input) {
        if (input == null) {
            return grpc.shared.NullableString
                    .newBuilder()
                    .setNoValue(grpc.shared.Empty.getDefaultInstance())
                    .build();
        }
        return grpc.shared.NullableString
                .newBuilder()
                .setValue(input)
                .build();
    }

    public static Map<String, String> handleMaybeNullMap(Map<String, String> input) {
        if (input == null) {
            return new HashMap<>();
        }
        return input;
    }
}
