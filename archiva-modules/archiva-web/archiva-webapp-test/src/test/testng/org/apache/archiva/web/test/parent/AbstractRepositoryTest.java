begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|test
operator|.
name|parent
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|Assert
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryTest
extends|extends
name|AbstractArchivaTest
block|{
comment|// Repository Groups
specifier|public
name|void
name|goToRepositoryGroupsPage
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getTitle
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Apache Archiva \\ Administration - Repository Groups"
argument_list|)
condition|)
block|{
name|clickLinkWithText
argument_list|(
literal|"Repository Groups"
argument_list|)
expr_stmt|;
block|}
name|assertRepositoryGroupsPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertRepositoryGroupsPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Repository Groups"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Administration - Repository Groups"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Identifier*:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"repositoryGroup.id"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Add Group"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Groups"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertAddedRepositoryLink
parameter_list|(
name|String
name|repositoryGroupName
parameter_list|)
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Repository Groups"
argument_list|)
expr_stmt|;
name|String
name|repositoryGroupUrlValue
init|=
literal|"repository/"
operator|+
name|repositoryGroupName
operator|+
literal|"/"
decl_stmt|;
name|String
name|baseUrlValue
init|=
literal|"archiva"
decl_stmt|;
name|String
name|repositoryGroupLink
init|=
name|baseUrl
operator|.
name|replaceFirst
argument_list|(
name|baseUrlValue
argument_list|,
name|repositoryGroupUrlValue
argument_list|)
decl_stmt|;
name|assertTextPresent
argument_list|(
name|repositoryGroupLink
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertAddedRepositoryToRepositoryGroups
parameter_list|(
name|String
name|repositoryName
parameter_list|)
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Repository Groups"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|repositoryName
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Archiva Managed Internal Repository"
argument_list|)
expr_stmt|;
name|assertAddedRepositoryLink
argument_list|(
name|repositoryName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertDeleteRepositoryGroupPage
parameter_list|(
name|String
name|repositoryName
parameter_list|)
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Delete Repository Group"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"WARNING: This operation can not be undone."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Are you sure you want to delete the following repository group?"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"ID:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|repositoryName
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Confirm"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addRepositoryGroup
parameter_list|(
name|String
name|repoGroupName
parameter_list|)
block|{
name|goToRepositoryGroupsPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"repositoryGroup.id"
argument_list|,
name|repoGroupName
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Add Group"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addRepositoryToRepositoryGroup
parameter_list|(
name|String
name|repositoryGroupName
parameter_list|,
name|String
name|repositoryName
parameter_list|)
block|{
name|goToRepositoryGroupsPage
argument_list|()
expr_stmt|;
name|String
name|s
init|=
name|getSelenium
argument_list|()
operator|.
name|getBodyText
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"No Repository Groups Defined."
argument_list|)
condition|)
block|{
name|setFieldValue
argument_list|(
literal|"repositoryGroup.id"
argument_list|,
name|repositoryGroupName
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Add Group"
argument_list|)
expr_stmt|;
comment|//assertAddedRepositoryLink( repositoryGroupName );
name|selectValue
argument_list|(
literal|"addRepositoryToGroup_repoId"
argument_list|,
name|repositoryName
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Add Repository"
argument_list|)
expr_stmt|;
name|assertAddedRepositoryToRepositoryGroups
argument_list|(
name|repositoryName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//assertAddedRepositoryLink( repositoryGroupName );
name|selectValue
argument_list|(
literal|"addRepositoryToGroup_repoId"
argument_list|,
name|repositoryName
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Add Repository"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|deleteRepositoryInRepositoryGroups
parameter_list|()
block|{
name|goToRepositoryGroupsPage
argument_list|()
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|click
argument_list|(
literal|"xpath=//div[@id='contentArea']/div[2]/div/div[3]/div[1]/a/img"
argument_list|)
expr_stmt|;
name|waitPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|deleteRepositoryGroup
parameter_list|(
name|String
name|repositoryName
parameter_list|)
block|{
name|getSelenium
argument_list|()
operator|.
name|click
argument_list|(
literal|"xpath=//div[@id='contentArea']/div[2]/div/div[1]/div/a/img"
argument_list|)
expr_stmt|;
name|waitPage
argument_list|()
expr_stmt|;
name|assertDeleteRepositoryGroupPage
argument_list|(
name|repositoryName
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Confirm"
argument_list|)
expr_stmt|;
block|}
comment|///////////////////////////////
comment|// proxy connectors
comment|///////////////////////////////
specifier|public
name|void
name|goToProxyConnectorsPage
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Proxy Connectors"
argument_list|)
expr_stmt|;
name|assertProxyConnectorsPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertProxyConnectorsPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Proxy Connectors"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Administration - Proxy Connectors"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Proxy Connectors"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Archiva Managed Internal Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Proxy Connector"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Central Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Java.net Repository for Maven 2"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertAddProxyConnectorPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Add Proxy Connector"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Admin: Add Proxy Connector"
argument_list|)
expr_stmt|;
name|String
name|proxy
init|=
literal|"Network Proxy*:,Managed Repository*:,Remote Repository*:,Policies:,Return error when:,On remote error:,Releases:,Snapshots:,Checksum:,Cache failures:,Properties:,No properties have been set.,Black List:,No black list patterns have been set.,White List:,No white list patterns have been set."
decl_stmt|;
name|String
index|[]
name|arrayProxy
init|=
name|proxy
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|arrayproxy
range|:
name|arrayProxy
control|)
name|assertTextPresent
argument_list|(
name|arrayproxy
argument_list|)
expr_stmt|;
comment|/*String proxyElements = "addProxyConnector_connector_proxyId,addProxyConnector_connector_sourceRepoId,addProxyConnector_connector_targetRepoId,policy_propagate-errors-on-update,policy_propagate-errors,policy_releases,policy_snapshots,policy_checksum,policy_cache-failures,propertiesEntry,propertiesValue,blackListEntry,whiteListEntry"; 		String[] arrayProxyElements = proxyElements.split( "," ); 		for ( String arrayproxyelements : arrayProxyElements ) 			assertTextPresent( arrayproxyelements );*/
name|assertButtonWithValuePresent
argument_list|(
literal|"Add Property"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Add Pattern"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Add Proxy Connector"
argument_list|)
expr_stmt|;
block|}
comment|// this only fills in the values of required fields in adding Proxy Connectors
specifier|public
name|void
name|addProxyConnector
parameter_list|(
name|String
name|networkProxy
parameter_list|,
name|String
name|managedRepo
parameter_list|,
name|String
name|remoteRepo
parameter_list|)
block|{
name|goToProxyConnectorsPage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Add"
argument_list|)
expr_stmt|;
name|assertAddProxyConnectorPage
argument_list|()
expr_stmt|;
name|selectValue
argument_list|(
literal|"connector.proxyId"
argument_list|,
name|networkProxy
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"connector.sourceRepoId"
argument_list|,
name|managedRepo
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"connector.targetRepoId"
argument_list|,
name|remoteRepo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleteProxyConnector
parameter_list|()
block|{
name|goToProxyConnectorsPage
argument_list|()
expr_stmt|;
name|clickLinkWithXPath
argument_list|(
literal|"//div[@id='contentArea']/div[2]/div[1]/div[2]/div[1]/a[3]/img"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Delete Proxy Connectors"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Delete"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Proxy Connectors"
argument_list|)
expr_stmt|;
block|}
comment|///////////////////////////////
comment|// network proxies
comment|///////////////////////////////
specifier|public
name|void
name|goToNetworkProxiesPage
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Network Proxies"
argument_list|)
expr_stmt|;
name|assertNetworkProxiesPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertNetworkProxiesPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Network Proxies"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Administration - Network Proxies"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Network Proxies"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Add Network Proxy"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertAddNetworkProxy
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Add Network Proxy"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Admin: Add Network Proxy"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Add network proxy:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Identifier*:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Protocol*:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Hostname*:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Port*:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Username:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Password:"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Save Network Proxy"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addNetworkProxy
parameter_list|(
name|String
name|identifier
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|hostname
parameter_list|,
name|String
name|port
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
comment|//goToNetworkProxiesPage();
name|clickLinkWithText
argument_list|(
literal|"Add Network Proxy"
argument_list|)
expr_stmt|;
name|assertAddNetworkProxy
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"proxy.id"
argument_list|,
name|identifier
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"proxy.protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"proxy.host"
argument_list|,
name|hostname
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"proxy.port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"proxy.username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"proxy.password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Save Network Proxy"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|editNetworkProxies
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
comment|//goToNetworkProxiesPage();
name|clickLinkWithText
argument_list|(
literal|"Edit Network Proxy"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
name|fieldName
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Save Network Proxy"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleteNetworkProxy
parameter_list|()
block|{
comment|//goToNetworkProxiesPage();
name|clickLinkWithText
argument_list|(
literal|"Delete Network Proxy"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Delete Network Proxy"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"WARNING: This operation can not be undone."
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Delete"
argument_list|)
expr_stmt|;
block|}
comment|// remote repositories
specifier|public
name|void
name|assertAddRemoteRepository
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Add Remote Repository"
argument_list|)
expr_stmt|;
name|String
name|remote
init|=
literal|"Identifier*:,Name*:,URL*:,Username:,Password:,Timeout in seconds:,Type:"
decl_stmt|;
name|String
index|[]
name|arrayRemote
init|=
name|remote
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|arrayremote
range|:
name|arrayRemote
control|)
name|assertTextPresent
argument_list|(
name|arrayremote
argument_list|)
expr_stmt|;
name|String
name|remoteElements
init|=
literal|"addRemoteRepository_repository_id,addRemoteRepository_repository_name,addRemoteRepository_repository_url,addRemoteRepository_repository_username,addRemoteRepository_repository_password,addRemoteRepository_repository_timeout,addRemoteRepository_repository_layout"
decl_stmt|;
name|String
index|[]
name|arrayRemoteElements
init|=
name|remoteElements
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|arrayremotelement
range|:
name|arrayRemoteElements
control|)
name|assertElementPresent
argument_list|(
name|arrayremotelement
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertDeleteRemoteRepositoryPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Delete Remote Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Admin: Delete Remote Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"WARNING: This operation can not be undone."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Are you sure you want to delete the following remote repository?"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Confirm"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addRemoteRepository
parameter_list|(
name|String
name|identifier
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|timeout
parameter_list|,
name|String
name|type
parameter_list|)
block|{
comment|//goToRepositoriesPage();
name|assertAddRemoteRepository
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"addRemoteRepository_repository_id"
argument_list|,
name|identifier
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"addRemoteRepository_repository_name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"addRemoteRepository_repository_url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"addRemoteRepository_repository_username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"addRemoteRepository_repository_password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"addRemoteRepository_repository_timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"addRemoteRepository_repository_layout"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Add Repository"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleteRemoteRepository
parameter_list|()
block|{
name|goToRepositoriesPage
argument_list|()
expr_stmt|;
name|clickLinkWithXPath
argument_list|(
literal|"//div[@id='contentArea']/div/div[8]/div[1]/a[2]"
argument_list|)
expr_stmt|;
name|assertDeleteRemoteRepositoryPage
argument_list|()
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Confirm"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|editRemoteRepository
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|goToRepositoriesPage
argument_list|()
expr_stmt|;
name|clickLinkWithXPath
argument_list|(
literal|"//div[@id='contentArea']/div/div[8]/div[1]/a[1]"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
name|fieldName
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Update Repository"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|editManagedRepository
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|goToRepositoriesPage
argument_list|()
expr_stmt|;
name|clickLinkWithXPath
argument_list|(
literal|"//div[@id='contentArea']/div/div[5]/div[1]/a[1]/img"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Edit Managed Repository"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
name|fieldName
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|//TODO
name|clickButtonWithValue
argument_list|(
literal|"Update Repository"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deleteManagedRepository
parameter_list|()
block|{
name|clickLinkWithXPath
argument_list|(
literal|"//div[@id='contentArea']/div/div[5]/div[1]/a[2]"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Delete Managed Repository"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Delete Configuration Only"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getRepositoryDir
parameter_list|()
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|String
name|artifactFilePath
init|=
name|f
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
return|return
name|artifactFilePath
operator|+
literal|"/target/"
return|;
block|}
comment|/////////////////////////////////////////////
comment|// Repository Scanning
comment|/////////////////////////////////////////////
specifier|public
name|void
name|goToRepositoryScanningPage
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Repository Scanning"
argument_list|)
expr_stmt|;
name|assertRepositoryScanningPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertRepositoryScanningPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Repository Scanning"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Administration - Repository Scanning"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Scanning - File Types"
argument_list|)
expr_stmt|;
name|String
name|artifactsTypes
init|=
literal|"**/*.pom,**/*.jar,**/*.ear,**/*.war,**/*.car,**/*.sar,**/*.mar,**/*.rar,**/*.dtd,**/*.tld,**/*.tar.gz,**/*.tar.bz2,**/*.zip"
decl_stmt|;
name|String
index|[]
name|arrayArtifactTypes
init|=
name|artifactsTypes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arrayArtifactTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
literal|"//div[@id='contentArea']/div/div[1]/table."
operator|+
name|i
operator|+
literal|".0"
argument_list|)
argument_list|,
name|arrayArtifactTypes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|String
name|autoremove
init|=
literal|"**/*.bak,**/*~,**/*-"
decl_stmt|;
name|String
index|[]
name|arrayAutoremove
init|=
name|autoremove
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arrayAutoremove
operator|.
name|length
condition|;
name|i
operator|++
control|)
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
literal|"//div[@id='contentArea']/div/div[2]/table."
operator|+
name|i
operator|+
literal|".0"
argument_list|)
argument_list|,
name|arrayAutoremove
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|String
name|ignored
init|=
literal|"**/.htaccess,**/KEYS,**/*.rb,**/*.sh,**/.svn/**,**/.DAV/**"
decl_stmt|;
name|String
index|[]
name|arrayIgnored
init|=
name|ignored
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arrayIgnored
operator|.
name|length
condition|;
name|i
operator|++
control|)
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
literal|"//div[@id='contentArea']/div/div[3]/table."
operator|+
name|i
operator|+
literal|".0"
argument_list|)
argument_list|,
name|arrayIgnored
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|String
name|indexableContent
init|=
literal|"**/*.txt,**/*.TXT,**/*.block,**/*.config,**/*.pom,**/*.xml,**/*.xsd,**/*.dtd,**/*.tld"
decl_stmt|;
name|String
index|[]
name|arrayIndexableContent
init|=
name|indexableContent
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arrayIndexableContent
operator|.
name|length
condition|;
name|i
operator|++
control|)
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
literal|"//div[@id='contentArea']/div/div[4]/table."
operator|+
name|i
operator|+
literal|".0"
argument_list|)
argument_list|,
name|arrayIndexableContent
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
comment|/////////////////////////////////////////////
comment|// Database
comment|/////////////////////////////////////////////
specifier|public
name|void
name|goToDatabasePage
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Database"
argument_list|)
expr_stmt|;
name|assertDatabasePage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertDatabasePage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Database"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Administration - Database"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Database - Unprocessed Artifacts Scanning"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Cron:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"database_cron"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Update Cron"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Update Database Now"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Database - Unprocessed Artifacts Scanning"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Database - Artifact Cleanup Scanning"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

