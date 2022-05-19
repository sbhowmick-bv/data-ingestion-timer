package com.bazaarvoice.reportmanager;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class DatabaseConfiguration {

    @NotNull
    @JsonProperty
    private String driverClass;

    @NotNull
    @JsonProperty
    private String user;

    @NotNull
    @JsonProperty
    private String password;

    @NotNull
    @JsonProperty
    private String validationQuery;

    @NotNull
    @JsonProperty
    private String url;

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
