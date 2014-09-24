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

package com.rmn.qa;

import org.openqa.grid.internal.ProxySet;

import java.util.HashMap;
import java.util.Map;

public class MockRequestMatcher implements RequestMatcher {

    private int threadsToReturn;
    private Map<String,Integer> inProgressTests = new HashMap<String, Integer>();

    @Override
    public int getNumFreeThreadsForParameters(ProxySet proxySet, AutomationRunRequest request) {
        return threadsToReturn;
    }

    public void setThreadsToReturn(int threadsToReturn) {
        this.threadsToReturn = threadsToReturn;
    }

    @Override
    public int getNumInProgressTests(ProxySet proxySet, AutomationRunRequest request) {
        Integer returnValue =  inProgressTests.get(request.getBrowser());
        return (returnValue == null) ? 0 : returnValue;
    }

    public void setInProgressTests(String browser, int inProgressTests) {
        this.inProgressTests.put(browser,inProgressTests);
    }
}
