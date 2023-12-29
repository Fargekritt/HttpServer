package no.lullinj.http;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class Cookie {
    private String name;
    private String value;

    //Optional values
    private Optional<String> expire;

    private boolean httpOnly;

    private Optional<String> domain;

    private Optional<String> maxAge;

    private boolean partitioned;

    private Optional<String> path;

    private Optional<String> sameSite;

    private boolean secure;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setExpire(String expire) {
        this.expire = Optional.of(expire);
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public void setDomain(@NotNull String domain) {
        this.domain = Optional.of(domain);
    }

    public void setMaxAge(@NotNull String maxAge) {
        this.maxAge = Optional.of(maxAge);
    }

    public void setPartitioned(boolean partitioned) {
        this.partitioned = partitioned;
    }

    public void setPath(@NotNull String path) {
        this.path = Optional.of(path);
    }

    public void setSameSite(@NotNull String sameSite) {
        this.sameSite = Optional.of(sameSite);
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Optional<String> getExpire() {
        return expire;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public Optional<String> getDomain() {
        return domain;
    }

    public Optional<String> getMaxAge() {
        return maxAge;
    }

    public boolean isPartitioned() {
        return partitioned;
    }

    public Optional<String> getPath() {
        return path;
    }

    public Optional<String> getSameSite() {
        return sameSite;
    }

    public boolean isSecure() {
        return secure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cookie cookie = (Cookie) o;
        return httpOnly == cookie.httpOnly && partitioned == cookie.partitioned && secure == cookie.secure && Objects.equals(name, cookie.name) && Objects.equals(value, cookie.value) && Objects.equals(expire, cookie.expire) && Objects.equals(domain, cookie.domain) && Objects.equals(maxAge, cookie.maxAge) && Objects.equals(path, cookie.path) && Objects.equals(sameSite, cookie.sameSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, expire, httpOnly, domain, maxAge, partitioned, path, sameSite, secure);
    }
}
