package online.pigeonshouse.minirpc.api.service;

import lombok.Getter;
import online.pigeonshouse.minirpc.api.MiniType;

import java.util.Objects;

@Getter
public class MiniObject extends MiniElement {
    private MiniType TYPE;
    private Object value;

    public static MiniObject createNull() {
        return new MiniObject();
    }

    public MiniObject(MiniObject value) {
        super(value.getClassName(), value.getUUID());
        TYPE = MiniType.MINI_OBJ;
        this.value = value;
    }

    public MiniObject(Integer integer) {
        super(null, null);
        this.value = integer;
        TYPE = MiniType.INTERGER;
    }

    public MiniObject(Long aLong) {
        super(null, null);
        this.value = aLong;
        TYPE = MiniType.LONG;
    }

    public MiniObject(Double aDouble) {
        super(null, null);
        this.value = aDouble;
        TYPE = MiniType.DOUBLE;
    }

    public MiniObject(String string) {
        super(null, null);
        this.value = string;
        TYPE = MiniType.STRING;
    }

    public MiniObject(Boolean aBoolean) {
        super(null, null);
        this.value = aBoolean;
        TYPE = MiniType.BOOLEAN;
    }

    @Deprecated
    public MiniObject(MiniType type, Object value) {
        super(null, null);
        this.value = value;
        TYPE = type;
    }

    private MiniObject() {
        super(null, null);
        TYPE = MiniType.NULL;
    }

    public MiniObject(String objClassName, String objUUID) {
        super(objClassName, objUUID);
        TYPE = MiniType.MINI_OBJ;
    }

    public boolean isPrimitive() {
        return TYPE != MiniType.MINI_OBJ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MiniObject object = (MiniObject) o;
        return TYPE == object.TYPE && Objects.equals(value, object.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), TYPE, value);
    }

    @Override
    public String toString() {
        return "MiniObject{" +
                "TYPE=" + TYPE +
                ", objClassName='" + objClassName + '\'' +
                ", objUUID='" + objUUID + '\'' +
                '}';
    }
}
