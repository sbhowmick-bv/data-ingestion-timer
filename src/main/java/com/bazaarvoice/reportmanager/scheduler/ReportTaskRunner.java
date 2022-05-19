package com.bazaarvoice.reportmanager.scheduler;

import com.bazaarvoice.reportmanager.job.DataWriter;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class ReportTaskRunner extends TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportTaskRunner.class);

    private final DataWriter dataWriter;

    public ReportTaskRunner(DataWriter dataWriter){
        this.dataWriter = dataWriter;
    }

    @UnitOfWork
    public void run(){
        long start = System.currentTimeMillis();
        LOGGER.info("Timer Started {} job", getName());
        this.dataWriter.dataWrite();
        LOGGER.info("Completed {} job and took {} seconds", getName(), (System.currentTimeMillis() - start)/1000);
    }

    private String getName() {
        return "Report Task Runner";
    }

}
