package online.pigeonshouse.minirpc.api.service;

import java.util.Objects;

public class MiniElement {
    public String objClassName;
    public String objUUID;

    public MiniElement(String objClassName, String objUUID) {
        this.objClassName = objClassName;
        this.objUUID = objUUID;
    }

    public void setUUID(String uuid) {
        this.objUUID = uuid;
    }

    public String getUUID() {
        return objUUID;
    }

    public String getClassName() {
        return objClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniElement that = (MiniElement) o;
        return Objects.equals(objClassName, that.objClassName) && Objects.equals(objUUID, that.objUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objClassName, objUUID);
    }
}
