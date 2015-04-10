# Introduction #

The File System Connector uses the [JCIFS client library](http://jcifs.samba.org) for communicating with Windows File Shares using the Server Message Block (SMB) protocol.  This document describes the mechanism for setting JCIFS configuration properties to alter the JCIFS client library behavior to suit your environment.

For instance, if your file server is particularly sluggish under load, you may wish to increase the SMB client response timeout to avoid dropped requests.

# Details #

The File System Connector provides its own built-in JCIFS configuration that sets the following properties:
```
jcifs.smb.client.responseTimeout=30000
jcifs.smb.client.soTimeout=35000
```

This configuration bumps the SMB client request timeout from the default 10 seconds to 30 seconds (30,000 milliseconds).  It also bumps the name service timeout from the default 5 seconds to 35 seconds.

You can override the built-in JCIFS configuration properties by creating your own configuration properties file in the file system.  The configuration properties file must be plain text, and must be located in the deployed Connector web application directory.

The JCIFS configuration properties are documented [here](http://jcifs.samba.org/src/docs/api).

## Configuring JCIFS on Windows ##

**Note:** `%CATALINA_HOME%` refers to the Apache Tomcat deployment directory for your Connector installation.  If you installed the Connector using the Google Connector Installer (GCI), then `%CATALINA_HOME%` will be the `Tomcat` directory inside the Connector Installation (for instance: `C:\GoogleConnectors\myConnector\Tomcat`).

  1. Shutdown the Tomcat web application server, either via the Services control panel, by using the `Stop_*_Connector_Console` command in the Connector installation directory, or by using Tomcat's `shutdown` or  `stopService` command.
```
cd %CATALINA_HOME%\bin
stopService.bat
```
  1. The JCIFS configuration properties file must be in a `com\google\enterprise\connector\filesystem\config` subdirectory of the `classes` directory for the connector web application.  By default, this directory does not exist, so you must create it.
```
cd %CATALINA_HOME%\webapps\connector-manager\WEB-INF\classes
mkdir com\google\enterprise\connector\filesystem\config
```
  1. Use your favorite plain-text editor to create a `jcifsConfiguration.properties` file with the JCIFS properties you wish to set.  Since this properties file will be used instead of the built-in one, you should probably include the `jcifs.smb.client.responseTimeout` and `jcifs.smb.client.soTimeout` mentioned above, in addition to any other custom properties you wish to configure.  For instance, suppose we wish to increase the SMB response timeout to 60 seconds (60,000 milliseconds):
```
cd com\google\enterprise\connector\filesystem\config
echo jcifs.smb.client.responseTimeout=60000 >> jcifsConfiguration.properties
echo jcifs.smb.client.soTimeout=35000 >> jcifsConfiguration.properties
```
  1. Restart the Tomcat web application server, either via the Services control panel, by using the `Start_*_Connector_Console` command in the Connector installation directory, or by using Tomcat's `startup` command or `startService` command.
```
cd %CATALINA_HOME%\bin
startService.bat
```
## Configuring JCIFS on Unix or Linux ##

**Note:** `${CATALINA_HOME}` refers to the Apache Tomcat deployment directory for your Connector installation.  If you installed the Connector using the Google Connector Installer (GCI), then `${CATALINA_HOME}` will be the
`Tomcat` directory inside the Connector Installation (for instance: `~/GoogleConnectors/myConnector/Tomcat`).

The syntax used here is Bash shell syntax.  You may need to adjust the syntax appropriately if you are using a different command shell.

  1. Shutdown the Tomcat web application server either by using the `Stop_*_Connector_Console` command in the Connector installation directory, or by using Tomcat's `shutdown` command.
```
cd ${CATALINA_HOME}/bin
./shutdown.sh
```
  1. The JCIFS configuration properties file must be in a `com/google/enterprise/connector/filesystem/config` subdirectory of the `classes` directory for the connector web application.  By default, this directory does not exist, so you must create it.
```
cd ${CATALINA_HOME}/webapps/connector-manager/WEB-INF/classes
mkdir -p com/google/enterprise/connector/filesystem/config
```
  1. Use your favorite plain-text editor to create a `jcifsConfiguration.properties` file with the JCIFS properties you wish to set.  Since this properties file will be used instead of the built-in one, you should probably include the `jcifs.smb.client.responseTimeout` and `jcifs.smb.client.soTimeout` mentioned above, in addition to any other custom properties you wish to configure.  For instance, suppose we wish to increase the SMB response timeout to 60 seconds (60,000 milliseconds):
```
cd com/google/enterprise/connector/filesystem/config
echo 'jcifs.smb.client.responseTimeout=60000' >> jcifsConfiguration.properties
echo 'jcifs.smb.client.soTimeout=35000' >> jcifsConfiguration.properties
```
  1. Restart the Tomcat web application server, either by using the `Start_*_Connector_Console` command in the Connector installation directory, or by using Tomcat's `startup` command.
```
cd ${CATALINA_HOME}/bin
./startup.sh 
```