package com.bazaarvoice.reportmanager.managed;

import com.bazaarvoice.reportmanager.scheduler.ReportTaskRunner;
import io.dropwizard.lifecycle.Managed;

import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class JobsManaged implements Managed {

    private Timer timer = new Timer();
    private final ReportTaskRunner reportTaskRunner;

    public JobsManaged(ReportTaskRunner reportTaskRunner) {
        this.reportTaskRunner = reportTaskRunner;
    }

    @Override
    public void start() throws Exception {

        //Every night at 2 AM task will be scheduled
        //todo timezone set and think about the timing
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY,23);
        today.set(Calendar.MINUTE, 45);
        today.set(Calendar.SECOND, 0);

        timer.scheduleAtFixedRate(reportTaskRunner, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));

    }

    @Override
    public void stop() throws Exception {
        timer.cancel();
    }
}
