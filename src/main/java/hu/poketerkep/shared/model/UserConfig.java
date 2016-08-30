package hu.poketerkep.shared.model;


import hu.poketerkep.shared.model.helpers.LastUsed;

public class UserConfig implements LastUsed {
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
