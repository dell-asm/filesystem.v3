# Overview #
The File System Connector traverses network and local directories and contained subdirectories.
During a full traversal, files and their ACLs are sent to the GSA for indexing
and serve time security checking purposes.
During incremental traversals, the File System Connector notices change that are not
yet reflected in GSA index and updates the GSA accordingly.

Access to SMB file shares is through the JCIFS library.  A supported directory must be
accessible as a Windows share. You can verify access from a Windows system with
File Explorer. Linux users can verify access with the smbclient utility.

Access to NFS file shares is through the WebNFS library.  Access to local file systems
uses the  Java File or JNA interfaces.   Note that the File System Connector on-board
the GSA has local file system access disabled.

The File System Connector marks documents it sends to the GSA as secure
and attempts to include an ACL with each document. The GSA
uses these ACLs to authorize user access when responding to queries.

At this time, ACLs are only supported for Windows File Shares, not NFS or local file systems.

File System Connector version 3.0 and later, when used with GSA version 7.0 and later,
supports deny ACLs and ACL inheritance.  To support ACL inheritance, the connector
sends the ACLs for the file share and all traversed directories to the GSA, in addition
to the ACLs for fed documents.  Changes to ACLs are generally not recognized during
incremental traversals, but will be detected during full traversals.

For File System Connector version 2.x or GSA version 6.x,  deny ACLs and ACL
inheritance are not supported. If a file
has an ACL with a deny ACE, the connector sends the file to the GSA without
an ACL. If the document qualifies as a potential result for a query, the GSA will
delegate authorization checking to the File System Connector.  Due to a known
GSA issue, access will be denied for all users.

**NOTE:** The default ACL format changed between File System Connector version 2
and version 3.  The new default user ACL format is "domain\user".  The new default
group ACL format is "domain\group".  These new defaults will be the most appropriate
formats to use with GSA version 7.0 and later.  The previous ACL formats, "user" and "group"
(without the domain), are unlikely to work with GSA version 7.0.  If, when using
File System Connector version 3, you wish to use the older formats, you must
explicitly enable those formats in the [Advanced Configuration](AdvancedConfiguration.md).

Because the File System Connector sends the GSA secure documents with ACLs you
must configure the GSA to:
  1. Authenticate the user.
  1. Determine the authenticated user's groups.

For detailed information please refer to these links:
  1. [Managing Search for Controlled-Access Content](http://code.google.com/apis/searchappliance/documentation/68/secure_search/secure_search_overview.html)
  1. [Authenticating users](http://code.google.com/apis/searchappliance/documentation/68/secure_search/secure_search_crwlsrv.html#serve_for_controlled_access_content)
  1. [Policy ACLs](http://code.google.com/apis/searchappliance/documentation/68/secure_search/secure_search_crwlsrv.html#PolicyAccessControlLists)

Before configuring a File System Connector you must install one. An Installer
will be provided soon. For manual installation instructions please refer to http://code.google.com/p/google-enterprise-connector-file-system/wiki/ManualInstallation.

The installed File System Connector is managed by a Connector Manager which runs in the
same Tomcat instance and must be registered with the GSA. For information on registering
this Connector Manager please refer to the help links on the
'Connector Admininistration->Connector Managers' screen on your GSA.

Once the Connector Manager has been registered you can configure the File System Connector
itself. For general information on registering connectors please refer to the
help links on the 'Connector Administration->Connectors' screen on your GSA.


The File System Connector configuration includes URL patterns. These patterns
follow the conventions of the patterns supported by the GSA. For more information see
http://code.google.com/apis/searchappliance/documentation/68/admin_crawl/URL_patterns.html

# Form Elements #
The following sections describe the information to enter in the File System Connector configuration form:
  1. **Start paths**: Enter SMB URLs directorys you want the File System Connector to traverse in the boxes (1 URL per box). See http://jcifs.samba.org/src/docs/api/ for additional information on SMB URLs. Do not add domain, user name or password elements to start paths.
  1. **Include patterns**: Enter include patterns for files you want sent to the GSA in the boxes (1 pattern per box). The File System Connector will only send a file if it matches an include pattern.
  1. **Exclude patterns**: Enter exclude patterns for files that match an include pattern that you do not want sent to the GSA (1 pattern per box).
  1. **Domain**: The domain for the Window's user performing the traversal.
  1. **User Name**: The name of a Window's user performing the traversal. This user needs sufficient permission to access all the files and directories being traversed. The user must be authorized to list all the traversed directories. In addition the user must authorized to read data and meta data for all traversed files.
  1. **Password**: The password for the Window's user performing the traversal.
  1. **Full traversal interval**: How often to fully traverse the repository from scratch.  File and directory adds, deletes, and copies and changes to file contents are detected during the connector's incremental traversals (determined by the connector's Retry Delay). However, moved or renamed files and changes to ACLs and other metadata may only be detected during full traversals. Frequent full traversals may overwhelm the Search Appliance, bogging down its feed processing. Long full traversal intervals increase the time it takes for the Search Appliance to notice certain types of changes.