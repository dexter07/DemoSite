package org.broadleafcommerce.load;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author Jeff Fischer
 */
public class PrimeCacheService {

    Log LOG = LogFactory.getLog(PrimeCacheService.class);

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Value("${weighted.jmeter.location}")
    protected String weightedJmeterLocation;

    @Transactional("blTransactionManager")
    public void prime() {
        File file = new File(weightedJmeterLocation);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            boolean eof = false;
            int count = 0;
            while (!eof) {
                String temp = reader.readLine();
                if (temp == null) {
                    eof = true;
                } else {
                    if (count > 0) {
                        String[] vals = temp.split(",");
                        catalogService.findCategoryByURI(vals[0]);
                        catalogService.findProductByURI(vals[0] + vals[1]);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(count + ": cached category(" + vals[0] + ") and product(" + vals[1] + ")");
                        }
                    }
                }
                if (count % 100 == 0) {
                    em.clear();
                }
                count++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable e){
                    //do nothing
                }
            }
        }
    }
}
