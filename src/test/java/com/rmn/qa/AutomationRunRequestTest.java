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

import junit.framework.Assert;
import org.junit.Test;
import org.openqa.selenium.remote.CapabilityType;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhardin on 4/24/14.
 */
public class AutomationRunRequestTest {

    @Test
    // Tests that two run request objects match each other
    public void testMatchesOtherRunRequest() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "20";
        String os = "linux";
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        AutomationRunRequest second = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        Assert.assertTrue("Run requests should match",first.matchesCapabilities(second));
    }

    @Test
    // Tests that two run request objects do match each other due to mismatching browsers
    public void testMatchesOtherRunRequestBadBrowser() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "20";
        String os = "linux";
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        AutomationRunRequest second = new AutomationRunRequest(uuid,null,"badBrowser",browserVersion,os);
        Assert.assertFalse("Run requests should NOT match due to browser",first.matchesCapabilities(second));
    }
    @Test
    // Tests that two run request objects do match each other due to mismatching browser versions
    public void testMatchesOtherRunRequestBadBrowserVersion() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "20";
        String os = "linux";
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        AutomationRunRequest second = new AutomationRunRequest(uuid,null,browser,"12432",os);
        Assert.assertFalse("Run requests should NOT match due to browser version", first.matchesCapabilities(second));
    }
    @Test
    // Tests that two run request objects do match each other due to mismatching OS
    public void testMatchesOtherRunRequestBadOs() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "20";
        String os = "linux";
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        AutomationRunRequest second = new AutomationRunRequest(uuid,null,browser,browserVersion,"badOs");
        Assert.assertFalse("Run requests should NOT match due to browser",first.matchesCapabilities(second));
    }

    @Test
    // Tests that two run request objects match each other when option fields are not set on the first object
    public void testMatchesOtherRunRequestOptionalParameters() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = null;
        String os = null;
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        AutomationRunRequest second = new AutomationRunRequest(uuid,null,browser,"20","linux");
        Assert.assertTrue("Run requests should match",first.matchesCapabilities(second));
    }

    @Test
    // Tests that two run request objects do NOT match each other since the optional fields are on the second object
    public void testDoesntMatchesOtherRunRequestNonOptionalParameters() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = null;
        String os = null;
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,"20","linux");
        AutomationRunRequest second = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        Assert.assertFalse("Run requests should match", first.matchesCapabilities(second));
    }

    @Test
    // Tests that two run request objects match each other
    public void testMatchesCapabilities() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "20";
        String os = "linux";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CapabilityType.BROWSER_NAME,browser);
        map.put(CapabilityType.VERSION,browserVersion);
        map.put(CapabilityType.PLATFORM,os);
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        Assert.assertTrue("Capabilities should match",first.matchesCapabilities(map));
    }

    @Test
    // Tests that two run request objects dont match each other due to incorrect browser
    public void testMatchesCapabilitiesBadBrowser() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "20";
        String os = "linux";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CapabilityType.BROWSER_NAME,browser);
        map.put(CapabilityType.VERSION,browserVersion);
        map.put(CapabilityType.PLATFORM,os);
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,"badBrowser",browserVersion,os);
        Assert.assertFalse("Capabilities should match",first.matchesCapabilities(map));
    }

    @Test
    // Tests that two run request objects dont match each other due to incorrect browser version
    public void testMatchesCapabilitiesBadVersion() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "20";
        String os = "linux";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CapabilityType.BROWSER_NAME,browser);
        map.put(CapabilityType.VERSION,browserVersion);
        map.put(CapabilityType.PLATFORM,os);
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,"123",os);
        Assert.assertFalse("Capabilities should match",first.matchesCapabilities(map));
    }

    @Test
    // Tests that two run request objects dont match each other due to incorrect os
    public void testMatchesCapabilitiesBadOs() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "20";
        String os = "linux";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CapabilityType.BROWSER_NAME,browser);
        map.put(CapabilityType.VERSION,browserVersion);
        map.put(CapabilityType.PLATFORM,os);
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,"badOs");
        Assert.assertFalse("Capabilities should match", first.matchesCapabilities(map));
    }

    @Test
    // Tests that two run request objects match each other when option fields are not set on the first object
    public void testMatchesCapabilitiesOptionalParameters() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = null;
        String os = null;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CapabilityType.BROWSER_NAME,browser);
        map.put(CapabilityType.VERSION,"20");
        map.put(CapabilityType.PLATFORM,"badOs");
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        Assert.assertTrue("Capabilities should match",first.matchesCapabilities(map));
    }

    @Test
    // Tests that two run request objects do NOT match each other since the optional fields are on the second object
    public void testMatchesCapabilitiesNonOptionalParameters() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "21";
        String os = "linux";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CapabilityType.BROWSER_NAME,browser);
        map.put(CapabilityType.VERSION,"20");
        map.put(CapabilityType.PLATFORM,"badOs");
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        Assert.assertFalse("Capabilities should NOT match",first.matchesCapabilities(map));
    }

    @Test
    // Tests that two run request objects do NOT match each other since the optional fields are on the second object
    public void testMatchesFactoryMethod() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "25";
        String os = "linux";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(CapabilityType.BROWSER_NAME,browser);
        map.put(CapabilityType.VERSION,browserVersion);
        map.put(CapabilityType.PLATFORM,os);
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,os);
        AutomationRunRequest second = AutomationRunRequest.requestFromCapabilities(map);
        Assert.assertEquals("Factory method should have generated an equal request", first, second);
    }

    @Test
    // Tests that a newly created run is considered a 'new' run
    public void testIsNewRun() {
        AutomationRunRequest first = new AutomationRunRequest(null);
        Assert.assertTrue("Run should be considered new", first.isNewRun());
    }

    @Test
    // Test that an old run is not considered new
    public void testIsNotNewRun() {
        Date oldDate = AutomationUtils.modifyDate(new Date(), -121, Calendar.SECOND);
        AutomationRunRequest first = new AutomationRunRequest(null,null,null,null,null,oldDate);
        Assert.assertFalse("Run should be considered new", first.isNewRun());
    }

    @Test
    // Tests that a platform of 'ANY' matches another otherwise non-matching OS
    public void testOsMatchesAnyRequests() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "25";
        String os = "ANY";
        Map<String,Object> map = new HashMap<>();
        map.put(CapabilityType.BROWSER_NAME,browser);
        map.put(CapabilityType.VERSION,browserVersion);
        map.put(CapabilityType.PLATFORM,os);
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,"linux");
        AutomationRunRequest second = AutomationRunRequest.requestFromCapabilities(map);
        Assert.assertTrue("Requests should be equal", first.matchesCapabilities(second));
        Assert.assertTrue("Requests should be equal", second.matchesCapabilities(first));
    }

    @Test
    // Tests that a platform of 'ANY' matches another otherwise non-matching OS
    public void testOsMatchesAnyCapability() {
        String uuid = "testUuid";
        String browser = "firefox";
        String browserVersion = "25";
        String os = "ANY";
        Map<String,Object> capabilities = new HashMap<>();
        capabilities.put(CapabilityType.BROWSER_NAME, browser);
        capabilities.put(CapabilityType.VERSION, browserVersion);
        capabilities.put(CapabilityType.PLATFORM, os);
        AutomationRunRequest first = new AutomationRunRequest(uuid,null,browser,browserVersion,"linux");
        Assert.assertTrue("Requests should be equal", first.matchesCapabilities(capabilities));
    }
}
