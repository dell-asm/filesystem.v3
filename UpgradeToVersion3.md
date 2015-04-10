## Introduction ##

File System Connector version 3 is a significant re-engineering of the File System Connector implementation.  The most notable improvements are:

  * The connector moves from a Diffing Connector model to a Lister/Retriever model.  This is a major architectural change for the connector.
  * Vastly improved feed and indexing performance.
  * Support for ACL inheritance and deny for SMB file systems.
  * The connector pays attention to the Traversal Schedule.

## Prerequisites ##

Before applying an update you should have the following available to you:
  * Administrative access to the GSA 6.14 or above that the Connector is feeding.
  * Access to the computer running the Connector and its Tomcat application server.  You will need sufficient access rights to start and stop Tomcat and modify files in its deployment directory.
  * A binary distribution of the Connector Manager version 3 release, available on the [Connector Manager Downloads](http://code.google.com/p/google-enterprise-connector-manager/downloads/list) page.
  * A binary distribution of the File System Connector version 3 release, available on the [File System Connector Downloads](http://code.google.com/p/google-enterprise-connector-file-system/downloads/list) page.
  * Familiarity with the command line environment of the deployment computer (cmd.exe on Windows or a shell environment on Unix/Linux).

**Note:** These instructions use environment variable syntax as an allusion. These environment variables may not exist in your environment.  You may choose to define them appropriately, or you must substitute the actual paths when entering the commands.

## Preparing the GSA for the Upgrade ##

  1. Disable Traversals for your connector.  On the GSA admin console, navigate to the **Connector Administration > Connectors** page and click **Edit** for your File System Connector instance.  Check **Disable Traversal** and save the configuration.
  1. Delete the feed for your connector.  On the GSA admin console, navigate to the **Crawl and Index > Feeds** page, then click on the **Delete** link for your connector's feed.
  1. **Optional** Upgrade your GSA to version 7.0.
  1. Enable crawling of Retriever URLs.  On the GSA admin console, navigate to the **Crawl and Index > Crawl URLs** page.  Add the following item to the **Follow and Crawl Only URLs with the Following Patterns** section:<br />`/connector-manager/getDocumentContent`
  1. If using GSA version 6.14, you must enable HTTP Basic Authentication to allow crawling of non-public Retriever URLs.  To do so, navigate to the **Crawl and Index > Crawler Access** page.  Add the following rule to the **Users and Passwords for Crawling** section:
```
For URLs Matching Pattern: /connector-manager/getDocumentContent
Username: anything
In Domain: <leave this blank>
Password: anything
Confirm Password: <same as Password>
Make Public: <leave this unchecked>
```

## Upgrade to Connector Manager version 3 ##

File System Connector version 3 **requires** Connector Manager version 3 to operate.  Please follow [these instructions to upgrade to Connector Manager version 3](http://code.google.com/p/google-enterprise-connector-manager/wiki/UpgradeToVersion3), before upgrading to File System Connector version 3.

## Upgrading the File System Connector deployed on Windows ##

**Note:** `%CATALINA_HOME%` refers to the Apache Tomcat deployment directory for your Connector installation.  If you installed the Connector using the Google Connector Installer (GCI), then `%CATALINA_HOME%` will be the `Tomcat` directory inside the Connector Installation (for instance: `C:\GoogleConnectors\myConnector\Tomcat`).

**Note:** `%CONNECTOR_NAME%` refers to the name of your File System Connector instance as it appears in the GSA admin console's **Connector Administration > Connectors** page.

**Note:** `%RELEASE_DIR%` refers to the directory containing the File System Connector version 3 binary distribution.

You will need to stop and restart the Tomcat web application server during the upgrade.  The instructions here assume that Tomcat is running as a **Windows Service**.  The instructions for doing so differ if Tomcat is running as a console application.  You would typically stop and start the service via the `Windows->Settings->Control Panel->Administrative Tools->Services` control panel.

  1. Download the _Google File System Connector version 3 Binary Distribution_ from the [Downloads page](http://code.google.com/p/google-enterprise-connector-file-system/downloads/list), and unzip it.
```
cd %RELEASE_DIR%
unzip connector-filesystem-3.0.0.zip
```
  1. Shutdown the Tomcat web application server, either via the Services control panel, by using the `Stop_*_Connector_Console` command in the Connector installation directory, or by using Tomcat's `shutdown` or  `stopService` command.
```
cd %CATALINA_HOME%\bin
stopService.bat
```
  1. If upgrading from File System Connector version 2.6.4 or earlier, you **must** delete the connector's Java bean configuration  file `connectorInstance.xml`.  If upgrading from File System Connector version 2.8, this file may remain, however any custom configurations of the `delayBetweenTwoScansInMillis`, `introduceDelayAfterEveryScan`, or `queueSize` advanced configuration properties will be ignored.
```
cd %CATALINA_HOME%\webapps\connector-manager\WEB-INF\connectors\FileConnectorType\%CONNECTOR_NAME%
del connectorInstance.xml
```
  1. **Optional:** Remove the connector's `queue` and `snapshots` directories.  These will no longer be used and the contents may be quite large.
```
cd %CATALINA_HOME%\webapps\connector-manager\WEB-INF\connectors\FileConnectorType\%CONNECTOR_NAME%
rmdir /s /q queue
rmdir /s /q snapshots
```
  1. Deploy the new connector JAR files over the old installation.  **WARNING:** Some versions of the Google Connector Installer mark the installed connector JAR files as Read-Only.  Write permission for these files must be restored before deploying the new version.
```
cd %CATALINA_HOME%\webapps\connector-manager\WEB-INF\lib
attrib -r *.jar
copy /y %RELEASE_DIR%\connector-filesystem.jar .
copy /y %RELEASE_DIR%\Lib\*.jar .
```
  1. Restart the Tomcat web application server, either via the Services control panel, by using the `Start_*_Connector_Console` command in the Connector installation directory, or by using Tomcat's `startup` command or `startService` command.
```
cd %CATALINA_HOME%\bin
startService.bat
```
## Upgrading the File System Connector deployed on Unix or Linux ##

**Note:** `${CATALINA_HOME}` refers to the Apache Tomcat deployment directory for your Connector installation.  If you installed the Connector using the Google Connector Installer (GCI), then `${CATALINA_HOME}` will be the
`Tomcat` directory inside the Connector Installation (for instance: `~/GoogleConnectors/myConnector/Tomcat`).

**Note:** `${CONNECTOR_NAME}` refers to the name of your File System Connector instance as it appears in the GSA admin console's **Connector Administration > Connectors** page.

**Note:** `${RELEASE_DIR}` refers to the directory containing the File System Connector version 3 binary distribution.

The syntax used here is Bash shell syntax.  You may need to adjust the syntax appropriately if you are using a different command shell.

  1. Download the _Google File System Connector version 3 Binary Distribution_ from the [Downloads page](http://code.google.com/p/google-enterprise-connector-file-system/downloads/list), and unzip it.
```
cd ${RELEASE_DIR}
wget http://google-enterprise-connector-file-system.googlecode.com/files/connector-filesystem-3.0.0.zip
unzip connector-filesystem-3.0.0.zip
```
  1. Shutdown the Tomcat web application server either by using the `Stop_*_Connector_Console` command in the Connector installation directory, or by using Tomcat's `shutdown` command.
```
cd ${CATALINA_HOME}/bin
./shutdown.sh
```
  1. If upgrading from File System Connector version 2.6.4 or earlier, you **must** delete the connector's Java bean configuration  file `connectorInstance.xml`.  If upgrading from File System Connector version 2.8, this file may remain, however any custom configurations of the `delayBetweenTwoScansInMillis`, `introduceDelayAfterEveryScan`, or `queueSize` advanced configuration properties will be ignored.
```
cd ${CATALINA_HOME}/webapps/connector-manager/WEB-INF/connectors/FileConnectorType/${CONNECTOR_NAME}
rm connectorInstance.xml
```
  1. **Optional:** Remove the connector's `queue` and `snapshots` directories.  These will no longer be used and the contents may be quite large.
```
cd ${CATALINA_HOME}/webapps/connector-manager/WEB-INF/connectors/FileConnectorType/${CONNECTOR_NAME}
rm -rf queue snapshots
```
  1. Deploy the new connector JAR files over the old installation.  **WARNING:** Some versions of the Google Connector Installer mark the installed connector JAR files as Read-Only.  Write permission for these files must be restored before deploying the new version.
```
cd ${CATALINA_HOME}/webapps/connector-manager/WEB-INF/lib
chmod +w *.jar
cp -p ${RELEASE_DIR}/connector-filesystem.jar .
cp -p ${RELEASE_DIR}/Lib/*.jar .
```
  1. Restart the Tomcat web application server, either by using the `Start_*_Connector_Console` command in the Connector installation directory, or by using Tomcat's `startup` command.
```
cd ${CATALINA_HOME}/bin
./startup.sh 
```

## The Default User and Group ACL Formats Have Changed ##

The default ACL format changed between File System Connector version 2 and version 3. The new default user ACL format is "domain\user". The new default group ACL format is "domain\group". These new defaults will be the most appropriate formats to use with GSA version 7.0 and later. The previous ACL formats, "user" and "group" (without the domain), are unlikely to work with GSA version 7.0.  If the Authentication mechanism for this connector returns a domain element, then the user and group ACLs **must** also include the domain.

If you have previously explicitly configured the user and group ACL formats in the [Advanced Configuration](AdvancedConfiguration.md), that explicit configuration will still be honored (although you may wish to change it if upgrading the GSA as well).  If you have not explicitly configured the user and group ACL formats, the new default format will be used.

If, when using File System Connector version 3, you wish to use the unadorned "user" and "group" formats, you must explicitly enable those formats in the [Advanced Configuration](AdvancedConfiguration.md) before re-enabling traversal.

## Re-enable File System Connector Traversals ##

Enable Traversals for your connector.  On the GSA admin console, navigate to the **Connector Administration > Connectors** page and click **Edit** for your File System Connector instance.  Uncheck **Disable Traversal**.

If you had a set the `delayBetweenTwoScansInMillis`  advanced configuration property in a previous version of the connector, you my wish to adjust the **Retry Delay** value accordingly.  The **Retry Delay** governs the delay between traversals of the repository.  Most traversals are _incremental traversals_ and look only for items that have changed since the last traversal, based upon the file's last-modified timestamp.  Some traversals are _full traversals_ (see below), which feed all appropriate contents of the repository to the Search Appliance.

You may also wish to configure the forced **Full Traversal Interval** for the needs of your organization.  File and directory adds, deletes, and copies and changes to file contents are detected during the connector's incremental traversals.  However, moved or renamed files and changes to ACLs and other metadata may only be detected during full traversals.  Frequent full traversals may overwhelm the Search Appliance, bogging down its feed processing.  Long full traversal intervals increase the time it takes for the Search Appliance to notice certain types of changes.

A full traversal may also be triggered manually at any time by resetting the connector in the Search Appliance Admin Console.

This version of the connector actually pays attention to the **Connector Schedule**,  so you may wish to configure that as well.

Finally, save the connector configuration.