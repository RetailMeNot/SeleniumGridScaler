package com.rmn.qa.task;

import com.google.common.annotations.VisibleForTesting;
import com.rmn.qa.AutomationConstants;
import com.rmn.qa.AutomationRequestMatcher;
import com.rmn.qa.AutomationRunRequest;
import com.rmn.qa.RegistryRetriever;
import com.rmn.qa.aws.AwsVmManager;
import com.rmn.qa.aws.VmManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import org.openqa.grid.internal.Registry;
import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomationAutoGridScalerTask extends AbstractAutomationCleanupTask {

    private static final Logger log = LoggerFactory.getLogger(AutomationAutoGridScalerTask.class);
    @VisibleForTesting
    static final String NAME = "Grid Auto Scaling Task";
    private VmManager ec2;
    private AutomationRequestMatcher requestMatcher;

    public AutomationAutoGridScalerTask(RegistryRetriever registryRetriever, VmManager ec2) {
        super(registryRetriever);
        this.ec2 = ec2;
        this.requestMatcher = new AutomationRequestMatcher();
    }

    @Override
    public void doWork() {
        // check number of free nodes for each browser type
        int freeChromeThreads = requestMatcher.getNumFreeThreadsForParameters(getRegistry().getAllProxies(), new AutomationRunRequest(AutomationAutoGridScalerTask.class.getSimpleName(), null, BrowserType.CHROME));
        int freeFirefoxThreads = requestMatcher.getNumFreeThreadsForParameters(getRegistry().getAllProxies(), new AutomationRunRequest(AutomationAutoGridScalerTask.class.getSimpleName(), null, BrowserType.FIREFOX));
        int freeIEThreads = requestMatcher.getNumFreeThreadsForParameters(getRegistry().getAllProxies(), new AutomationRunRequest(AutomationAutoGridScalerTask.class.getSimpleName(), null, "internetexplorer"));
        int freePhantomjsThreads = requestMatcher.getNumFreeThreadsForParameters(getRegistry().getAllProxies(), new AutomationRunRequest(AutomationAutoGridScalerTask.class.getSimpleName(), null, BrowserType.PHANTOMJS));

        
        int freeNodesChromeThreshold = Integer.parseInt(AwsVmManager.getAWSProperties().getProperty(AutomationConstants.FREE_NODES_CHROME_THRESHOLD));
        int freeNodesFirefoxThreshold = Integer.parseInt(AwsVmManager.getAWSProperties().getProperty(AutomationConstants.FREE_NODES_FIREFOX_THRESHOLD));
        int freeNodesIEThreshold = Integer.parseInt(AwsVmManager.getAWSProperties().getProperty(AutomationConstants.FREE_NODES_IE_THRESHOLD));
        int freeNodesPhantomjsThreshold = Integer.parseInt(AwsVmManager.getAWSProperties().getProperty(AutomationConstants.FREE_NODES_PHANTOMJS_THRESHOLD));

        // for each browser type check against the threshhold
        if (freeChromeThreads < freeNodesChromeThreshold) {
            startNewNodes(freeNodesChromeThreshold, BrowserType.CHROME);
        } else {
            log.info("Not starting new nodes for chrome since there are {}.", freeChromeThreads);
        }

        if (freeFirefoxThreads < freeNodesFirefoxThreshold) {
            startNewNodes(freeNodesFirefoxThreshold, BrowserType.FIREFOX);
        } else {
            log.info("Not starting new nodes for firefox since there are {}.", freeFirefoxThreads);
        }

        if (freeIEThreads < freeNodesIEThreshold) {
            startNewNodes(freeNodesIEThreshold, "internetexplorer");
        } else {
            log.info("Not starting new nodes for IE since there are {}.", freeNodesIEThreshold);
        }

        if (freePhantomjsThreads < freeNodesPhantomjsThreshold) {
            startNewNodes(freeNodesPhantomjsThreshold, BrowserType.PHANTOMJS);
        } else {
            log.info("Not starting new nodes for PhamtomJS since there are {}.", freeNodesPhantomjsThreshold);
        }

    }

    @Override
    public String getDescription() {
        return NAME;
    }

    private Registry getRegistry() {
        return this.registryRetriever.retrieveRegistry();
    }

    private void startNewNodes(int nrOfNewNodes, String browser) {
        log.info("Requesting {} new nodes of type {}", nrOfNewNodes, browser);
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        int returnCode = 0;
        String uuid = UUID.randomUUID().toString();
        try {
            url = new URL(String.format("http://localhost:4444/grid/admin/AutomationTestRunServlet?uuid=%s1&threadCount=%d&browser=%s",
                    uuid, nrOfNewNodes, browser));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
            returnCode = conn.getResponseCode();
        } catch (IOException e) {
            log.error("Error invoking call to create new instances", e);
        }
        if (returnCode >= 400) {
            log.error("Error creating new nodes, will retry on next call, return code of call was {}", returnCode);
        }
    }

}
