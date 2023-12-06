package dev.ngb.blog.token;

public enum TokenType {
    REFRESH,
    VERIFIED;

    public String getRedisPrefix() {
        return this.name() + "_TOKEN:";
    }
}
