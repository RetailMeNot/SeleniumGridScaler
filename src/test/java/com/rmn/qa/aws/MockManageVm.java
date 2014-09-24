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

package com.rmn.qa.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;

import java.util.Properties;

public class MockManageVm extends AwsVmManager {

    private String userData;

    public MockManageVm() {
        super();
    }

    public MockManageVm(AmazonEC2Client client, Properties properties, String region) {
        super(client,properties,region);
    }

    public MockManageVm(AmazonEC2Client client, Properties properties, String region, BasicAWSCredentials credentials) {
        this(client, properties, region);
        this.credentials = credentials;
    }

    @Override
    String getUserData(String uuid, String hubHostName, String browser, String os, int maxSessions) {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public void setCredentials(BasicAWSCredentials basicAWSCredentials) {
        this.credentials = basicAWSCredentials;
    }
}
