# Introduction #
There are certain advanced properties that can change connector's behaviour.

For Google Search Appliance versions 6.14 and later, the advanced properties are available to edit directly on the Connector's configuration page.  For earlier versions, the advanced properties may be set by manually editing the `connectorInstance.xml` file found in the connector instance directory.

By default all the advanced properties are disabled (commented out), so that their built-in default values are used.  To edit the advanced properties, first "uncomment" the property by removing the XML comment characters (`<!--` and `-->`) surrounding the property definition (not those surrounding the comments).  Then, set the desired property value.  For instance, to enable preservation of last access time for files and directories accessed via SMB, you would set the `lastAccessResetFlagForSmb` property to `true`:

Property disabled:
```
    <!-- 
    Flag to reset the last access time of a SMB file after crawling.
    This should be used when the start path of the url starts with "smb://".
    -->
    <!--  
    <property name="lastAccessResetFlagForSmb" value="false"/>
    -->
```

Property enabled and set to `true`:
```
    <!-- 
    Flag to reset the last access time of a SMB file after crawling.
    This should be used when the start path of the url starts with "smb://".
    -->
    <property name="lastAccessResetFlagForSmb" value="true"/>
```


The following section explains purpose of each of these properties and enlists all the possible values along with default value for each of them.

Please note that these advanced properties in this form are available only in File System Connector version 2.8 and above.

# Details #
```
    <!--   
    Security level to be considered for fetching ACL for a file
    There are 4 possible values for this parameter. These are in the order of
    Stronger to weaker security levels.
        FILEANDSHARE
        SHARE 
        FILE
        FILEORSHARE

    Default value is 'FILEANDSHARE'  since it is the most restrictive of all
    4 scenarios.
    -->
    <!--
      <property name="aceSecurityLevel" value="FILEANDSHARE"/>
    -->
    
    <!-- 
    Flag to mark all crawled documents as 'public'.
    Please note that this property is tightly coupled with 'pushAclFlag' property.
    You cannot keep both these values true. So if you want documents to be sent as
    public, then you must mark 'pushAclFlag' as 'false' 
    -->
    <!--
    <property name="markDocumentPublicFlag" value="false"/>
    -->
    
    <!-- 
    Flag to reset the last access time of a SMB file after crawling.
    This should be used when the start path of the url starts with "smb://".
    -->
    <!--  
    <property name="lastAccessResetFlagForSmb" value="false"/>
    -->
    
    <!-- 
    Flag to reset the last access time of a local windows file after crawling.
    This should be used when the start path of the url is a windows 
    style file path. E.g. "c://share/data"
    -->
    <!-- 
    <property name="lastAccessResetFlagForLocalWindows" value="false"/>
    -->
    
    <!-- 
    This flag is to specify whether connector should send ACL information with
    each document to the GSA. If this is set to true, then connector will send
    security information for each file such as which users and groups have access
    to the file. This information will be used while serving the results to the users.     
    Please note that this property is tightly coupled with  'markDocumentPublicFlag' property.
    You cannot keep both these values true. So if you want to send ACL information 
    with each file, then you must mark 'markDocumentPublicFlag' as 'false' 
    -->
    <!--
    <property name="pushAclFlag" value="true"/>
    -->

    <!-- 
    Represents the format in which ACE entries should be fed to GSA for groups.
    Possible values are:
        group@domain
        domain\group
        group   (only group name without domain information)

    The default value for version 2.8 is "group".
    The default value for version 3.0 is "domain\group".
    -->

    <!--
    <property name="groupAclFormat" value="domain\group"/>
    -->

    <!-- 
    Represents the format in which ACE entries should be fed to GSA for users.
    Possible values are:
        user@domain
        domain\user
        user   (only user name without domain information)

    The default value for version 2.8 is "user".
    The default value for version 3.0 is "domain\user".
    -->
    <!--
    <property name="userAclFormat" value="domain\user"/>
    -->

    <!-- THE FOLLOWING APPLY TO FILE SYSTEM CONNECTOR VERSION 2.8.
         THEY ARE OBSOLETE FOR VERSIONS 3.0 AND GREATER.
         For version 3, use the Retry Delay setting on the Connector
         configuration page.
    -->

    <!-- 
    No. of milliseconds to sleep after a scan
    When this delay is applied depends on the 'introduceDelayAfterEveryScan'
    parameter described below.
    -->
    <!--
    <property name="delayBetweenTwoScansInMillis" value="60000"/>
    -->
  
    <!-- 
    Flag that decides whether to add delay after every scan or only after scan
    that results in no changes. 
    -->
    <!--
    <property name="introduceDelayAfterEveryScan" value="false"/>
    -->

    <!-- 
    Size of the queue that holds the changes detected by the file system
    connector. 
    -->
    <!--
    <property name="queueSize" value="1000"/>
    -->
```