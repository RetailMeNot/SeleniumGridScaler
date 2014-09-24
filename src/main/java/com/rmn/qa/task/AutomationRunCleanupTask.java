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

import com.google.common.annotations.VisibleForTesting;
import com.rmn.qa.AutomationContext;
import com.rmn.qa.AutomationRunContext;
import com.rmn.qa.RegistryRetriever;
import org.openqa.grid.internal.ProxySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task to cleanup any registered {@link com.rmn.qa.AutomationRunRequest runs} that no longer have any running tests.
 * This will happen after the configured amount of time has passed since the run has started
 * @author mhardin
 */
public class AutomationRunCleanupTask extends AbstractAutomationCleanupTask {

    private static final Logger log = LoggerFactory.getLogger(AutomationRunCleanupTask.class);
    @VisibleForTesting
    static final String NAME = "Run Cleanup Task";

    /**
     * Constructs a run cleanup task with the specified context retrieval mechanism
     * @param registryRetriever
     */
    public AutomationRunCleanupTask(RegistryRetriever registryRetriever) {
        super(registryRetriever);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return AutomationRunCleanupTask.NAME;
    }

    // Basically we want to continuously monitor all runs that are registered and ensure that at least
    // one test is being run that belongs to that test run.  If there are any orphaned runs, we will
    // go ahead and remove it from AutomationRunContext providing they are old enough
    /**
     * {@inheritDoc}
     */
    @Override
    public void doWork() {
        log.info("Performing cleanup on runs.");
        AutomationRunContext context = AutomationContext.getContext();
        context.cleanUpRunRequests(getProxySet());
    }

    /**
     * Returns the {@link org.openqa.grid.internal.ProxySet ProxySet} to be used for cleanup purposes.
     * @return
     */
    @VisibleForTesting
    protected ProxySet getProxySet() {
        return registryRetriever.retrieveRegistry().getAllProxies();
    }
}
