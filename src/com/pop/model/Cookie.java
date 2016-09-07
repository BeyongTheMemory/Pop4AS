package com.pop.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.net.HttpCookie;

/**
 * Created by xugang on 16/9/2.
 */
@Table(name = "cookie")
public class Cookie {
    @Column(name = "id", isId = true,autoGen = true)
    private long id;
    @Column(name = "uri")
    private String uri; // cookie add by this uri.
    @Column(name = "sessionId")
    private String sessionId;
    @Column(name = "expiry")
    private long expiry;

    public Cookie(String uri, String sessionId, long expiry) {
        this.uri = uri;
        this.sessionId = sessionId;
        this.expiry = 604800;
    }

    public HttpCookie toHttpCookie() {
        HttpCookie cookie = new HttpCookie(uri, sessionId);
        cookie.setMaxAge((expiry - System.currentTimeMillis()) / 1000L);
        return cookie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public boolean isExpired() {
        return expiry != -1L && expiry < System.currentTimeMillis();
    }
}
