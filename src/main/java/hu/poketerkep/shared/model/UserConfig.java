package hu.poketerkep.shared.model;


import hu.poketerkep.shared.model.helpers.LastUsed;

import java.util.Comparator;

public class UserConfig implements LastUsed {
    public static final Comparator<UserConfig> LAST_USED_COMPARATOR = (o1, o2) -> Long.compare(o1.lastUsed, o2.lastUsed);
    private String userName;
    private long lastUsed;
    private boolean banned;

    public UserConfig(String userName, long lastUsed, boolean banned) {
        this.userName = userName;
        this.lastUsed = lastUsed;
        this.banned = banned;
    }

    public UserConfig() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "userName='" + userName + '\'' +
                ", lastUsed=" + lastUsed +
                ", banned=" + banned +
                '}';
    }
}
