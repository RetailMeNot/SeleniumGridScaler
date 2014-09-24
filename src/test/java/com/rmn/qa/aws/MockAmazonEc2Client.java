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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;

public class MockAmazonEc2Client extends AmazonEC2Client {

    private DescribeInstancesResult describeInstancesResult;
    private RunInstancesResult runInstancesResult;
    private RunInstancesRequest runInstancesRequest;
    private boolean throwDescribeInstancesError = false;
    private AmazonClientException throwRunInstancesError;
    private boolean throwExceptionsInRunInstancesIndefinitely = false;
    private CreateTagsRequest tagsCreated;
    private TerminateInstancesResult terminateInstancesResult;
    private TerminateInstancesRequest terminateInstancesRequest;

    public MockAmazonEc2Client(AWSCredentials credentials) {
        super(credentials);
    }


    @Override
    public DescribeInstancesResult describeInstances(DescribeInstancesRequest describeInstancesRequest) throws AmazonClientException {
        if(throwDescribeInstancesError) {
            throwDescribeInstancesError = false;
            throw new AmazonClientException("testError");
        }
        return describeInstancesResult;
    }

    public void setDescribeInstancesToThrowError() {
        throwDescribeInstancesError = true;
    }



    public void setDescribeInstances(DescribeInstancesResult resultToReturn) {
        this.describeInstancesResult = resultToReturn;
    }

    @Override
    public void createTags(CreateTagsRequest createTagsRequest) throws AmazonClientException {
        this.tagsCreated = createTagsRequest;
    }

    @Override
    public RunInstancesResult runInstances(RunInstancesRequest runInstancesRequest) throws AmazonServiceException, AmazonClientException {
        if(throwRunInstancesError != null) {
            AmazonClientException exceptionToThrow = throwRunInstancesError;
            if(!throwExceptionsInRunInstancesIndefinitely) {
                throwRunInstancesError = null;
            }
            throw exceptionToThrow;
        }
        this.runInstancesRequest = runInstancesRequest;
        return runInstancesResult;
    }

    public void setRunInstances(RunInstancesResult runInstancesResult) {
        this.runInstancesResult = runInstancesResult;
    }

    public RunInstancesRequest getRunInstancesRequest() {
        return runInstancesRequest;
    }

    @Override
    public TerminateInstancesResult terminateInstances(TerminateInstancesRequest terminateInstancesRequest) throws AmazonServiceException, AmazonClientException {
        this.terminateInstancesRequest = terminateInstancesRequest;
        return terminateInstancesResult;
    }

    public void setTerminateInstancesResult(TerminateInstancesResult terminateInstancesResult) {
        this.terminateInstancesResult = terminateInstancesResult;
    }

    public TerminateInstancesRequest getTerminateInstancesRequest() {
        return terminateInstancesRequest;
    }

    public void setThrowDescribeInstancesError(AmazonClientException t) {
        this.throwRunInstancesError = t;
    }

    public void setThrowExceptionsInRunInstancesIndefinitely() {
        throwExceptionsInRunInstancesIndefinitely = true;
    }
}
