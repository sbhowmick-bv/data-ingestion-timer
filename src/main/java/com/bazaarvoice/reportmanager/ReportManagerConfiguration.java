package com.bazaarvoice.reportmanager;

import com.bazaarvoice.reportmanager.config.ReportingParameterConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

public class ReportManagerConfiguration extends Configuration {

    @NotNull
    private Map<String, String> smsPilotResource = Collections.emptyMap();

    @Valid
    private DatabaseConfiguration databaseConfiguration;

    @JsonProperty
    private DatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    @JsonProperty
    @NotNull
    public ReportingParameterConfiguration reportingParameterConfiguration;

    /**
     * A factory used to connect to a relational database management system.
     * Factories are used by Dropwizard to group together related configuration
     * parameters such as database connection driver, URI, password etc.
     */
    @NotNull
    @Valid
    @JsonProperty("database")
    private final DataSourceFactory dataSourceFactory
            = new DataSourceFactory();

    /**
     * A getter for the database factory.
     *
     * @return An instance of database factory deserialized from the
     * configuration file passed as a command-line argument to the application.
     */
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    @JsonProperty("smspilotResource")
    public Map<String, String> getSMSPilotResource() {
        return smsPilotResource;
    }

    @JsonProperty("smspilotResource")
    public void setSMSPilotResource(Map<String, String> smsPilotResource) {
        this.smsPilotResource = smsPilotResource;
    }

    @JsonProperty("reportingParameterConfiguration")
    public ReportingParameterConfiguration getReportingParameterConfiguration() { return reportingParameterConfiguration; }

    public void setReportingParameterConfiguration(ReportingParameterConfiguration reportingParameterConfiguration) { this.reportingParameterConfiguration = reportingParameterConfiguration; }

}
