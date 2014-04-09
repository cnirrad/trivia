package trivia.service;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class SeedDataService {

    private static final Logger logger = LoggerFactory.getLogger(SeedDataService.class);

    @Autowired
    private EntityManager entityManager;

    @Value("classpath:seed.sql")
    private Resource seedData;

    @Transactional
    public void load() throws IOException {
        logger.debug("Loading seed data....");
        Query q = entityManager.createNativeQuery(IOUtils.toString(seedData.getInputStream()));
        q.executeUpdate();
    }
}
