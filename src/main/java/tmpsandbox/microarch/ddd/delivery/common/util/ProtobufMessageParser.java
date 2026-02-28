package tmpsandbox.microarch.ddd.delivery.common.util;

import com.google.protobuf.Parser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtobufMessageParser {
    public static <T> T parseEvent(byte[] payload, Parser<T> parser) {
        try {
            var event = parser.parseFrom(payload);
            log.info("Received event {}", event);

            return event;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse protobuf message", e);
        }
    }
}
