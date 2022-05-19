package com.bazaarvoice.reportmanager.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table
public class FeedImportStatistics {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long importId;
    private String clientName;
    private String feedType;
    private String jobStatus;
    private long interactionsParsed;
    private long interactionsRejected;
    private String runningDate;
    private String environment;

    public FeedImportStatistics() {
    }

    public FeedImportStatistics(
            long importId,
            String clientName,
            String feedType,
            String jobStatus,
            long interactionsParsed,
            long interactionsRejected,
            String runningDate,
            String environment) {

        this.importId = importId;
        this.clientName = clientName;
        this.feedType = feedType;
        this.jobStatus = jobStatus;
        this.interactionsParsed = interactionsParsed;
        this.interactionsRejected = interactionsRejected;
        this.runningDate = runningDate;
        this.environment = environment;
    }

    public long getImportId() {
        return importId;
    }

    public void setImportId(long importId) {
        this.importId = importId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public long getInteractionsParsed() {
        return interactionsParsed;
    }

    public void setInteractionsParsed(long interactionsParsed) {
        this.interactionsParsed = interactionsParsed;
    }

    public long getInteractionsRejected() {
        return interactionsRejected;
    }

    public void setInteractionsRejected(long interactionsRejected) {
        this.interactionsRejected = interactionsRejected;
    }

    public String getRunningDate() {
        return runningDate;
    }

    public void setRunningDate(String runningDate) {
        this.runningDate = runningDate;
    }

    public String getEnvironment() { return environment; }

    public void setEnvironment(String environment) { this.environment = environment; }
}
