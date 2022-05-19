package com.bazaarvoice.reportmanager;

import com.bazaarvoice.reportmanager.dao.FeedImportStatisticsDaoImpl;
import com.bazaarvoice.reportmanager.job.DataWriter;
import com.bazaarvoice.reportmanager.managed.JobsManaged;
import com.bazaarvoice.reportmanager.models.FeedImportStatistics;
import com.bazaarvoice.reportmanager.scheduler.ReportTaskRunner;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


import java.util.concurrent.TimeUnit;

public class ReportService extends Application<ReportManagerConfiguration> {

    public static void main(String[] args)
            throws Exception {
        new ReportService().run(args);
    }

    /**
     * Hibernate bundle.
     */
    private final HibernateBundle<ReportManagerConfiguration> hibernateBundle
            = new HibernateBundle<ReportManagerConfiguration>(FeedImportStatistics.class) {

        @Override
        public DataSourceFactory getDataSourceFactory(ReportManagerConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public String getName() {
        return "ReportService";
    }

    @Override
    public void initialize(Bootstrap<ReportManagerConfiguration> bootstrap) {

        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(ReportManagerConfiguration configuration, Environment environment) {

        //dao initialization
        final FeedImportStatisticsDaoImpl feedImportStatisticsDao = new FeedImportStatisticsDaoImpl(hibernateBundle.getSessionFactory());

        //task and scheduler initialization
        final DataWriter dataWriter = new DataWriter(configuration,feedImportStatisticsDao);
        ReportTaskRunner reportTaskRunner = new UnitOfWorkAwareProxyFactory(hibernateBundle).create(ReportTaskRunner.class, DataWriter.class, dataWriter);

        environment.lifecycle().manage(new JobsManaged(reportTaskRunner));

    }
}
