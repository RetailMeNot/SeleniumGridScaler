/*
 * Copyright (C) 2014 RetailMeNot, Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */

package com.rmn.qa.task;

import com.rmn.qa.RegistryRetriever;
import com.rmn.qa.aws.VmManager;
import org.openqa.grid.internal.ProxySet;

import java.util.Date;

// We're extending the task so we can override the created date behavior
public class MockAutomationHubCleanupTask extends AutomationHubCleanupTask {

    private Object createdDate;
    private ProxySet proxySet;

    public MockAutomationHubCleanupTask(RegistryRetriever retrieveContext, VmManager ec2, String instanceId) {
        super(retrieveContext, ec2, instanceId);
    }

    @Override
    protected Object getCreatedDate() {
        return createdDate;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected ProxySet getProxySet() {
        return proxySet;
    }

    static Date getEndDate() {
        return AutomationHubCleanupTask.endDate;
    }

    /**
     * Clears the start/end date
     */
    static void clearDates() {
        AutomationHubCleanupTask.createdDate = null;
        AutomationHubCleanupTask.endDate = null;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public void setProxySet(ProxySet proxySet) {
        this.proxySet = proxySet;
    }
}
