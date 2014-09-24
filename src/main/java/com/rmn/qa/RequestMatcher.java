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

/**
 * Interface used to determine current load
 */
public interface RequestMatcher {

    /**
     * Returns the number of free threads which satisfy the conditions of the parameters
     * @param proxySet Set of current registered proxy objects
     * @param runRequest Request used to match against
     * @return
     */
    int getNumFreeThreadsForParameters(ProxySet proxySet, AutomationRunRequest runRequest);

    /**
     * Returns the number of in progress tests which match the request passed in
     * @param proxySet Set of current registered proxy objects
     * @param runRequest Request used to match against
     * @return
     */
    int getNumInProgressTests(ProxySet proxySet, AutomationRunRequest runRequest);

}
