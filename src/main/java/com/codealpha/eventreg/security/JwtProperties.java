package com.codealpha.eventreg.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secret;
    private String issuer;
    private long accessTokenMinutes = 60;

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public long getAccessTokenMinutes() { return accessTokenMinutes; }
    public void setAccessTokenMinutes(long accessTokenMinutes) { this.accessTokenMinutes = accessTokenMinutes; }
}
