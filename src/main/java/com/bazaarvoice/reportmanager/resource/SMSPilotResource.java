package com.bazaarvoice.reportmanager.resource;

import com.bazaarvoice.reportmanager.ReportManagerConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

//xml parsers
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.w3c.dom.NodeList;

public class SMSPilotResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSPilotResource.class);
    private final ReportManagerConfiguration configuration;

    public SMSPilotResource(ReportManagerConfiguration configuration){
        this.configuration = configuration;
    }

    public static AmazonS3 buildS3Connection(){
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
        return s3client;
    }

    public int numberOfInteractions(S3ObjectInputStream objectData) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(objectData);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // never forget this!
        NodeList interList = doc.getElementsByTagName("Interaction");
        return interList.getLength();
    }


/**
     @throws Exception
 */
    public List<List> prepareDataForFeedImport(boolean historicalDataNeeded) throws Exception {

        //declaring all the variables
        int num,firstSlashInd,secondSlashInd,interactionsParsed,interactionsRejected,duplicate=0;
        String bucket,key,feedType,currDate,runningDate,clientName,jobStatus;
        String environment="";
        List<List> multipleRecords = new ArrayList<List>();
        List temp;
        LOGGER.info("Preparing data for feed_import");
        AmazonS3 s3 = buildS3Connection();
        bucket = configuration.getReportingParameterConfiguration().getS3BucketName();
        LOGGER.info("Bucket Name :: "+bucket);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucket);

        //looping over all the s3 bucket objects
        for(S3ObjectSummary s3ObjectSummary : s3.listObjects(listObjectsRequest).getObjectSummaries()){
            key = s3ObjectSummary.getKey();
            firstSlashInd = key.indexOf("/");
            secondSlashInd = key.indexOf("/",firstSlashInd+1);
            runningDate = key.substring(secondSlashInd+1,Math.min(secondSlashInd+1 + 10, key.length()));
            currDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            if(key.endsWith(".xml") && (historicalDataNeeded || currDate.equals(runningDate)) && !(key.startsWith("failed")) && !(key.contains("high-load-client")) && !(key.contains("medium-load-client")) &&!(key.contains("PrivacyReport"))){
                S3Object object = s3.getObject(new GetObjectRequest(bucket, key));
                S3ObjectInputStream objectData = object.getObjectContent();
                num =  numberOfInteractions(objectData);

                clientName = key.substring(firstSlashInd+1,secondSlashInd);
                jobStatus = key.substring(0,firstSlashInd);
                if(key.startsWith("succeed")){
                    interactionsParsed = num;
                    interactionsRejected = 0;
                }
                else{
                    interactionsParsed = 0;
                    interactionsRejected = num;
                }
                feedType = configuration.getReportingParameterConfiguration().getFeedType();
                if(configuration.getReportingParameterConfiguration().getEnvironment().equalsIgnoreCase("PROD")) {
                    if(configuration.getReportingParameterConfiguration().getAwsRegion().startsWith("eu")){
                        environment = "PROD_EU";
                    } else {
                        environment = "PROD_US";
                    }
                } else if(configuration.getReportingParameterConfiguration().getEnvironment().equalsIgnoreCase("QA")){
                    environment = "QA_EU"; //since QA has only one region currently
                }


                //checking and making clientName and runningDate together unique
                duplicate = 0;
                for (int i = 0; i < multipleRecords.size(); i++) {
                    temp = multipleRecords.get(i);
                    if (clientName.equals(temp.get(0).toString()) && runningDate.equals(temp.get(1).toString()) && jobStatus.equals(temp.get(2).toString())) {
                        duplicate=1;
                        multipleRecords.get(i).set(3,(int)temp.get(3)+interactionsParsed);
                        multipleRecords.get(i).set(4,(int)temp.get(4)+interactionsRejected);
                        break;
                    }
                }
                if(!(duplicate==1)){
                    List singleRecord = new ArrayList();
                    singleRecord.add(clientName);
                    singleRecord.add(runningDate);
                    singleRecord.add(jobStatus);
                    singleRecord.add(interactionsParsed);
                    singleRecord.add(interactionsRejected);
                    singleRecord.add(feedType);
                    singleRecord.add(environment);
                    multipleRecords.add(singleRecord);
                }
            }
        }
        return multipleRecords;
    }

}
