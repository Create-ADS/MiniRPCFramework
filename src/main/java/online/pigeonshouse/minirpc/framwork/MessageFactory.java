package online.pigeonshouse.minirpc.framwork;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessageFactory {
    private static final Map<Integer,Class<? extends Message>> MAP = new HashMap<>();

    public static void register(Message message) {
        MAP.put(message.getType(), message.getClass());
    }

    public static Class<? extends Message> valueOf(Integer type) {
        return MAP.get(type);
    }

    @Getter
    private final Integer type;

    public MessageFactory(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageFactory that = (MessageFactory) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
