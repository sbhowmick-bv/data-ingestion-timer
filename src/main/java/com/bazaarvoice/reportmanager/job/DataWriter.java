package com.bazaarvoice.reportmanager.job;

import com.bazaarvoice.reportmanager.ReportManagerConfiguration;
import com.bazaarvoice.reportmanager.dao.FeedImportStatisticsDaoImpl;
import com.bazaarvoice.reportmanager.models.FeedImportStatistics;
import com.bazaarvoice.reportmanager.resource.SMSPilotResource;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataWriter.class);

    private final FeedImportStatisticsDaoImpl feedImportStatisticsDao;
    private final ReportManagerConfiguration configuration;


    public DataWriter(ReportManagerConfiguration configuration, FeedImportStatisticsDaoImpl feedImportStatisticsDao) {
        this.configuration = configuration;
        this.feedImportStatisticsDao = feedImportStatisticsDao;
    }

    public int findIndex(){
        return 1;
    }

    /**
     * This method is responsible for writing the report-data into the database
     */

    @UnitOfWork
    public void dataWrite() {

        LOGGER.info("Timer Job Started:...");
        LOGGER.info("Step by step reports will be written to database");

        try {
            SMSPilotResource smsPilotResource = new SMSPilotResource(this.configuration);
            List<List> multipleRecordsList;
            List<FeedImportStatistics> insertList = new ArrayList<>();
            List temp;
            multipleRecordsList = smsPilotResource.prepareDataForFeedImport(feedImportStatisticsDao.findAll().isEmpty()); //historical data needed if the table is empty
            FeedImportStatistics feedImportStatisticsModel;


            for(int i=0;i<multipleRecordsList.size();i++){
                temp = multipleRecordsList.get(i);
                feedImportStatisticsModel = new FeedImportStatistics();
                feedImportStatisticsModel.setClientName(temp.get(0).toString());
                feedImportStatisticsModel.setRunningDate(temp.get(1).toString());
                feedImportStatisticsModel.setJobStatus(temp.get(2).toString());
                feedImportStatisticsModel.setInteractionsParsed((int)temp.get(3));
                feedImportStatisticsModel.setInteractionsRejected((int)temp.get(4));
                feedImportStatisticsModel.setFeedType(temp.get(5).toString());
                feedImportStatisticsModel.setEnvironment(temp.get(6).toString());
                insertList.add(feedImportStatisticsModel);
            }
            persist(insertList);


        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    //Save data
    private void persist(List statLists) {
        LOGGER.info("Persisting data into the database");
        for (Object feedImportStatistics : statLists) {
            feedImportStatisticsDao.saveReport((FeedImportStatistics) feedImportStatistics);
        }
    }
}
