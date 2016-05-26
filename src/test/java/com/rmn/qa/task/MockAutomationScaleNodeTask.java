package com.rmn.qa.task;

import java.util.Date;
import java.util.List;

import org.openqa.grid.internal.ProxySet;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.beust.jcommander.internal.Lists;
import com.rmn.qa.MockVmManager;
import com.rmn.qa.NodesCouldNotBeStartedException;
import com.rmn.qa.RegistryRetriever;
import com.rmn.qa.aws.VmManager;

/**
 * Created by matthew on 3/18/16.
 */
public class MockAutomationScaleNodeTask extends AutomationScaleNodeTask {

	private ProxySet proxySet;
	private Iterable<DesiredCapabilities> desiredCapabilities;
	private List<Boolean> nodesStarted = Lists.newArrayList();
	private List<String> browserStarted = Lists.newArrayList();
	private List<Platform> platformStarted = Lists.newArrayList();
	private List<Integer> numThreadsStarted = Lists.newArrayList();
	private boolean nodeOldEnoughToStart = true;

	public MockAutomationScaleNodeTask(RegistryRetriever retriever, MockVmManager vmManager) {
		super(retriever, vmManager);
	}

	@Override
	public ProxySet getProxySet() {
		return proxySet;
	}

	public void setProxySet(ProxySet proxySet) {
		this.proxySet = proxySet;
	}

	@Override
	Iterable<DesiredCapabilities> getDesiredCapabilities() {
		return desiredCapabilities;
	}

	public void setDesiredCapabilities(Iterable<DesiredCapabilities> desiredCapabilities) {
		this.desiredCapabilities = desiredCapabilities;
	}

	@Override
	void startNodes(VmManager vmManager, int browsersToStart, String browser, Platform platform) throws NodesCouldNotBeStartedException {
		nodesStarted.add(true);
		numThreadsStarted.add(browsersToStart);
		browserStarted.add(browser);
		platformStarted.add(platform);
	}

	@Override
	boolean isNodeOldEnoughToCreateNewNode(Date nowDate, Date nodePendingDate) {
		return nodeOldEnoughToStart;
	}

	public void setNodeOldEnoughToStart(boolean nodeOldEnoughToStart) {
		this.nodeOldEnoughToStart = nodeOldEnoughToStart;
	}

	public List<Boolean> getNodesStarted() {
		return nodesStarted;
	}

	public List<String> getBrowserStarted() {
		return browserStarted;
	}

	public List<Platform> getPlatformStarted() {
		return platformStarted;
	}

	public List<Integer> getNumThreadsStarted() {
		return numThreadsStarted;
	}
}

