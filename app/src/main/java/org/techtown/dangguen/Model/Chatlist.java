package org.techtown.dangguen.Model;

import java.util.Objects;

public class Chatlist {
    public String id;

    public Chatlist(String id) {
        this.id = id;
    }

    public Chatlist() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chatlist)) return false;
        Chatlist chatlist = (Chatlist) o;
        return getId().equals(chatlist.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
