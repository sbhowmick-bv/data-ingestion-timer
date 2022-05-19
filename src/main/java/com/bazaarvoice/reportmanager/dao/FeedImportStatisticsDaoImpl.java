package com.bazaarvoice.reportmanager.dao;

import com.bazaarvoice.reportmanager.models.FeedImportStatistics;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FeedImportStatisticsDaoImpl extends AbstractDAO<FeedImportStatistics> {

    /**
     * Loggers
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedImportStatisticsDaoImpl.class);
    /**
     * Constructor.
     *
     * @param sessionFactory Hibernate session factory.
     */
    public FeedImportStatisticsDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveReport (FeedImportStatistics feedImportStatistics) {
        LOGGER.debug("Add new feedImportStatistics {}", feedImportStatistics);
        currentSession().beginTransaction();
        persist(feedImportStatistics);
        currentSession().getTransaction().commit();
    }

    public List<FeedImportStatistics> findAll() {
        currentSession().beginTransaction();
        List<FeedImportStatistics> list = currentSession().createQuery("select fs from FeedImportStatistics fs").list();
        currentSession().getTransaction().commit();
        return list;
    }

}
