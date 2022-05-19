package com.bazaarvoice.reportmanager.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportingParameterConfiguration {
    @JsonProperty ("s3BucketName")
    private String s3BucketName;

    @JsonProperty ("awsRegion")
    private String awsRegion;

    @JsonProperty ("feedType")
    private String feedType;

    @JsonProperty ("environment")
    private String environment;

    @JsonProperty
    public String getS3BucketName() {
        return s3BucketName;
    }

    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    @JsonProperty
    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    @JsonProperty
    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    @JsonProperty
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
