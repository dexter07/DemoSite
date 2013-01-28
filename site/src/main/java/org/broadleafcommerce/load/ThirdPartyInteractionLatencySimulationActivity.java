package org.broadleafcommerce.load;

import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;

/**
 * @author Jeff Fischer
 */
public class ThirdPartyInteractionLatencySimulationActivity extends BaseActivity {

    private long waitTime = 1000L;

    @Override
    public ProcessContext execute(ProcessContext context) throws Exception {
        try {
            Thread.sleep(waitTime);
        } catch (Throwable e) {
            //do nothing
        }

        return context;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }
}
