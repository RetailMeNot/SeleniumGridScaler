package com.rmn.qa.task;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.rmn.qa.AutomationContext;
import com.rmn.qa.AutomationUtils;
import com.rmn.qa.BaseTest;
import com.rmn.qa.MockRequestMatcher;
import com.rmn.qa.MockVmManager;

import junit.framework.Assert;

/**
 * Created by matthew on 3/18/16.
 */
public class AutomationScaleNodeTaskTest extends BaseTest {

	@Test
	public void testUnsupportedBrowserNoNodesStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "safari");
		task.setDesiredCapabilities(Arrays.asList(capabilities));
		task.run();
		Assert.assertEquals("No nodes should have been started", 0, task.getNodesStarted().size());
	}

	@Test
	public void testUnsupportedPlatformNoNodesStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setPlatform(Platform.MAC);
		task.setDesiredCapabilities(Arrays.asList(capabilities));
		task.run();
		Assert.assertEquals("No nodes should have been started", 0, task.getNodesStarted().size());
	}

	@Test
	public void testNodesPendingStartupNoNodesStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setPlatform(Platform.LINUX);
		task.setDesiredCapabilities(Arrays.asList(capabilities));
		String nodePendingStartup = "foo";
		AutomationContext.getContext().addPendingNode(nodePendingStartup);
		task.run();
		Assert.assertEquals("No nodes should have been started", 0, task.getNodesStarted().size());
	}

	@Test
	public void testNodesPendingStartupExpire() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setPlatform(Platform.LINUX);
		task.setDesiredCapabilities(Arrays.asList(capabilities));
		String nodePendingStartup = "foo";
		AutomationContext.getContext().addPendingNode(nodePendingStartup);
		task.run();
		Assert.assertEquals("No nodes should have been started", 0, task.getNodesStarted().size());
		task.nodeToCreation.put(nodePendingStartup, AutomationUtils.modifyDate(new Date(), -15, Calendar.MINUTE));
		task.run();
		Assert.assertEquals("No nodes should have been started", 0, task.getNodesStarted().size());
		Assert.assertFalse("Pending node should be cleared as enough time has elapsed", task.nodeToCreation.containsKey(nodePendingStartup));
		task.run();
		Assert.assertEquals("Nodes should have been started", 1, task.getNodesStarted().size());
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(0));
	}

	@Test
	public void testNullPlatformNodeStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		task.setDesiredCapabilities(Arrays.asList(capabilities));
		task.run();
		Assert.assertEquals("Nodes should have been started", 1, task.getNodesStarted().size());
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(0));
		Assert.assertEquals("Started browser should be correct", "chrome", task.getBrowserStarted().get(0));
		Assert.assertEquals("Started platform should be correct", Platform.ANY, task.getPlatformStarted().get(0));
		Assert.assertEquals("Number of nodes started should be correct", 1, task.getNumThreadsStarted().get(0).intValue());
	}

	@Test
	public void testAnyPlatformNodeStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.ANY);
		task.setDesiredCapabilities(Arrays.asList(capabilities));
		task.run();
		Assert.assertEquals("Nodes should have been started", 1, task.getNodesStarted().size());
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(0));
		Assert.assertEquals("Started browser should be correct", "chrome", task.getBrowserStarted().get(0));
		Assert.assertEquals("Started platform should be correct", Platform.ANY, task.getPlatformStarted().get(0));
		Assert.assertEquals("Number of nodes started should be correct", 1, task.getNumThreadsStarted().get(0).intValue());
	}

	@Test
	// If multiple requests have different platforms from the same family, only one type of platform should start
	public void testMultiplePlatformsStartOneTypeOfNode() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.LINUX);
		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities2.setCapability(CapabilityType.PLATFORM, Platform.UNIX);
		task.setDesiredCapabilities(Arrays.asList(capabilities, capabilities2));
		task.run();
		Assert.assertEquals("Nodes should have been started", 1, task.getNodesStarted().size());
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(0));
		Assert.assertEquals("Started browser should be correct", "chrome", task.getBrowserStarted().get(0));
		Assert.assertEquals("Started platform should be correct", Platform.UNIX, task.getPlatformStarted().get(0));
		Assert.assertEquals("Number of nodes started should be correct", 2, task.getNumThreadsStarted().get(0).intValue());
	}
	@Test
	// If multiple requests have different platforms from the same family, only one type of platform should start
	public void testDifferentPlatformsStartDifferentNodes() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.LINUX);
		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities2.setCapability(CapabilityType.PLATFORM, Platform.WINDOWS);
		task.setDesiredCapabilities(Arrays.asList(capabilities, capabilities2));
		task.run();
		Assert.assertEquals("Nodes should have been started", 2, task.getNodesStarted().size());
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(0));
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(1));
		Assert.assertEquals("Started browser should be correct", "chrome", task.getBrowserStarted().get(0));
		Assert.assertEquals("Started browser should be correct", "chrome", task.getBrowserStarted().get(1));
		Assert.assertEquals("Started platform should be correct", 1, task.getPlatformStarted().stream().filter(platform -> platform == Platform.WINDOWS).count());
		Assert.assertEquals("Started platform should be correct", 1, task.getPlatformStarted().stream().filter(platform -> platform == Platform.UNIX).count());
		Assert.assertEquals("Number of nodes started should be correct", 1, task.getNumThreadsStarted().get(0).intValue());
		Assert.assertEquals("Number of nodes started should be correct", 1, task.getNumThreadsStarted().get(1).intValue());
	}

	@Test
	public void testLinuxPlatformNodeStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.LINUX);
		task.setDesiredCapabilities(Arrays.asList(capabilities));
		task.run();
		Assert.assertEquals("Nodes should have been started", 1, task.getNodesStarted().size());
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(0));
		Assert.assertEquals("Started browser should be correct", "chrome", task.getBrowserStarted().get(0));
		Assert.assertEquals("Started platform should be correct", Platform.UNIX, task.getPlatformStarted().get(0));
		Assert.assertEquals("Number of nodes started should be correct", 1, task.getNumThreadsStarted().get(0).intValue());
	}

	@Test
	public void testFamilyPlatformNodeStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.LINUX);
		task.setDesiredCapabilities(Arrays.asList(capabilities));
		task.run();
		Assert.assertEquals("Nodes should have been started", 1, task.getNodesStarted().size());
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(0));
		Assert.assertEquals("Started browser should be correct", "chrome", task.getBrowserStarted().get(0));
		Assert.assertEquals("Started platform should be correct", Platform.UNIX, task.getPlatformStarted().get(0));
		Assert.assertEquals("Number of nodes started should be correct", 1, task.getNumThreadsStarted().get(0).intValue());
	}

	@Test
	public void testMultipleNodesStartedAtOnce() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.ANY);
		task.setDesiredCapabilities(Arrays.asList(capabilities, capabilities));
		task.run();
		Assert.assertEquals("Nodes should have been started", 1, task.getNodesStarted().size());
		Assert.assertTrue("Nodes should have been started", task.getNodesStarted().get(0));
		Assert.assertEquals("Started browser should be correct", "chrome", task.getBrowserStarted().get(0));
		Assert.assertEquals("Started platform should be correct", Platform.ANY, task.getPlatformStarted().get(0));
		Assert.assertEquals("Number of nodes started should be correct", 2, task.getNumThreadsStarted().get(0).intValue());
	}

	@Test
	public void testUnsupportedPlatformNodeNotStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.MAC);
		task.setDesiredCapabilities(Arrays.asList(capabilities, capabilities));
		task.run();
		Assert.assertEquals("No nodes should have been started", 0, task.getNodesStarted().size());
	}

	@Test
	public void testNotEnoughTimePassNoNewNodeStarted() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		task.setNodeOldEnoughToStart(false);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		task.setDesiredCapabilities(Arrays.asList(capabilities, capabilities));
		task.run();
		Assert.assertEquals("No nodes should have been started", 0, task.getNodesStarted().size());
	}

	@Test
	public void testPendingBrowserDoesntMatchNoNewNodes() {
		MockVmManager manageEc2 = new MockVmManager();
		MockRequestMatcher matcher = new MockRequestMatcher();
		matcher.setThreadsToReturn(10);
		MockAutomationScaleNodeTask task = new MockAutomationScaleNodeTask(null, manageEc2);
		task.setNodeOldEnoughToStart(false);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		task.setDesiredCapabilities(Arrays.asList(capabilities, capabilities));
		task.run();
		task.setNodeOldEnoughToStart(true);
		DesiredCapabilities nonMatchingBrowser = new DesiredCapabilities();
		nonMatchingBrowser.setBrowserName("firefox");
		task.setDesiredCapabilities(Arrays.asList(nonMatchingBrowser));
		Assert.assertEquals("No nodes should have been started", 0, task.getNodesStarted().size());
	}


}
