1.) In your Amazon Web Services console, click the 'VPC' link.  This should take you to the VPC Dashboard.

2.) Click 'Start VPC Wizard' at the top of the page.  You should now be on the Step 1: Select a VPC Configuration page.

3.) Select the 'VPC with Public and Private Subnets' page.  Click the 'Select' button on the right.  You should now be on Step 2

4.) Give the VPC a name, such as "Selenium VPC".  Name the Public subnet to be "Selenium Public Subnet" and the Private subnet to be "Selenium Private Subnet".  Leave all other values as default.

5.) Click 'Create VPC' and then wait a few moments for the wizard to complete.  Click the OK button after it finishes.

6.) In the VPC Dashboard, click on 'Your VPCs'.  Find the VPC you created above, and note the VPC ID.

7.) In the VPC Dashboard, click on 'Subnets'.  Find the private subnet you created above, and note its subnet ID (e.g. subnet-9ed94444).  This will be the subnet you use to launch your nodes in in your properties file.  The value will be for the <region>\_linux\_node\_ami value, where <region> is the name of your region, like east or west.  So if you're in the 'east' region, the key would be east\_linux\_node\_ami.

8.) Select the public subnet you created named 'Selenium Public Subnet' and click on 'Inbound Rules'.  You will want to add a rule by clicking 'Edit' and then 'Add another rule'.  Type should be 'SSH (22)' for SSH access. If your externally facing IP address was 43.68.39.29, you would put 43.68.39.29/30 for the Source, so basically add "/30" to your externally facing IP address.  If you need access to any additional ports, you should add them here.

9.) In the VPC Dashboard, click on 'Security Groups'. Find the security group for your VPC ID in step 6.  This will be used for the <region>\_security\_group value in your configuration file.

10.) Click 'Services' at the top, then click on 'EC2'.  Now click on 'Instances' in the left hand side.  Find the running instance that represents the NAT the wizard created (should be associated with your VPC ID from step 6).  Select the instance, and there should be a public ip address under 'Public IP' in the 'Description' section at the bottom.  **This will be the IP address that you must get whitelisted in your networking setup in order for the nodes to be able to hit your test environment.  This is very important or your tests will not be able to run successfully.**