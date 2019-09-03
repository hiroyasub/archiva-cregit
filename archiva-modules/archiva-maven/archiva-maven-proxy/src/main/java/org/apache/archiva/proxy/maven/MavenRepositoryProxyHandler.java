begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|maven
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|NetworkProxyConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
operator|.
name|RepositoryURL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|DefaultRepositoryProxyHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|NotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|NotModifiedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|ProxyException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|model
operator|.
name|NetworkProxy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|model
operator|.
name|ProxyConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|*
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|storage
operator|.
name|StorageAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|ConnectionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|ResourceDoesNotExistException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|Wagon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|WagonException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|authentication
operator|.
name|AuthenticationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|authentication
operator|.
name|AuthenticationInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|proxy
operator|.
name|ProxyInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|repository
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * DefaultRepositoryProxyHandler  * TODO exception handling needs work - "not modified" is not really an exceptional case, and it has more layers than  * your average brown onion  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryProxyConnectors#maven"
argument_list|)
specifier|public
class|class
name|MavenRepositoryProxyHandler
extends|extends
name|DefaultRepositoryProxyHandler
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|RepositoryType
argument_list|>
name|REPOSITORY_TYPES
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|REPOSITORY_TYPES
operator|.
name|add
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
specifier|private
name|WagonFactory
name|wagonFactory
decl_stmt|;
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|ProxyInfo
argument_list|>
name|networkProxyMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|super
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|updateWagonProxyInfo
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|proxyList
parameter_list|)
block|{
name|this
operator|.
name|networkProxyMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|NetworkProxyConfiguration
argument_list|>
name|networkProxies
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getNetworkProxies
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|proxyEntry
range|:
name|proxyList
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|proxyEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|NetworkProxy
name|networkProxyDef
init|=
name|proxyEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|ProxyInfo
name|proxy
init|=
operator|new
name|ProxyInfo
argument_list|()
decl_stmt|;
name|proxy
operator|.
name|setType
argument_list|(
name|networkProxyDef
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setHost
argument_list|(
name|networkProxyDef
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setPort
argument_list|(
name|networkProxyDef
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setUserName
argument_list|(
name|networkProxyDef
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setPassword
argument_list|(
operator|new
name|String
argument_list|(
name|networkProxyDef
operator|.
name|getPassword
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|networkProxyMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setNetworkProxies
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|proxies
parameter_list|)
block|{
name|super
operator|.
name|setNetworkProxies
argument_list|(
name|proxies
argument_list|)
expr_stmt|;
name|updateWagonProxyInfo
argument_list|(
name|proxies
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param connector      * @param remoteRepository      * @param tmpResource      * @param checksumFiles      * @param url      * @param remotePath      * @param resource      * @param workingDirectory      * @param repository      * @throws ProxyException      * @throws NotModifiedException      */
specifier|protected
name|void
name|transferResources
parameter_list|(
name|ProxyConnector
name|connector
parameter_list|,
name|RemoteRepositoryContent
name|remoteRepository
parameter_list|,
name|StorageAsset
name|tmpResource
parameter_list|,
name|StorageAsset
index|[]
name|checksumFiles
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|remotePath
parameter_list|,
name|StorageAsset
name|resource
parameter_list|,
name|Path
name|workingDirectory
parameter_list|,
name|ManagedRepositoryContent
name|repository
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|NotModifiedException
block|{
name|Wagon
name|wagon
init|=
literal|null
decl_stmt|;
try|try
block|{
name|RepositoryURL
name|repoUrl
init|=
name|remoteRepository
operator|.
name|getURL
argument_list|()
decl_stmt|;
name|String
name|protocol
init|=
name|repoUrl
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
name|NetworkProxy
name|networkProxy
init|=
literal|null
decl_stmt|;
name|String
name|proxyId
init|=
name|connector
operator|.
name|getProxyId
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|proxyId
argument_list|)
condition|)
block|{
name|networkProxy
operator|=
name|getNetworkProxy
argument_list|(
name|proxyId
argument_list|)
expr_stmt|;
block|}
name|WagonFactoryRequest
name|wagonFactoryRequest
init|=
operator|new
name|WagonFactoryRequest
argument_list|(
literal|"wagon#"
operator|+
name|protocol
argument_list|,
name|remoteRepository
operator|.
name|getRepository
argument_list|()
operator|.
name|getExtraHeaders
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|networkProxy
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No network proxy with id {} found for connector {}->{}"
argument_list|,
name|proxyId
argument_list|,
name|connector
operator|.
name|getSourceRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|connector
operator|.
name|getTargetRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wagonFactoryRequest
operator|=
name|wagonFactoryRequest
operator|.
name|networkProxy
argument_list|(
name|networkProxy
argument_list|)
expr_stmt|;
block|}
name|wagon
operator|=
name|wagonFactory
operator|.
name|getWagon
argument_list|(
name|wagonFactoryRequest
argument_list|)
expr_stmt|;
if|if
condition|(
name|wagon
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
literal|"Unsupported target repository protocol: "
operator|+
name|protocol
argument_list|)
throw|;
block|}
if|if
condition|(
name|wagon
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
literal|"Unsupported target repository protocol: "
operator|+
name|protocol
argument_list|)
throw|;
block|}
name|boolean
name|connected
init|=
name|connectToRepository
argument_list|(
name|connector
argument_list|,
name|wagon
argument_list|,
name|remoteRepository
argument_list|)
decl_stmt|;
if|if
condition|(
name|connected
condition|)
block|{
name|transferArtifact
argument_list|(
name|wagon
argument_list|,
name|remoteRepository
argument_list|,
name|remotePath
argument_list|,
name|repository
argument_list|,
name|resource
operator|.
name|getFilePath
argument_list|()
argument_list|,
name|workingDirectory
argument_list|,
name|tmpResource
argument_list|)
expr_stmt|;
comment|// TODO: these should be used to validate the download based on the policies, not always downloaded
comment|// to
comment|// save on connections since md5 is rarely used
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|checksumFiles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|ext
init|=
literal|"."
operator|+
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|checksumFiles
index|[
name|i
index|]
operator|.
name|getName
argument_list|( )
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|transferChecksum
argument_list|(
name|wagon
argument_list|,
name|remoteRepository
argument_list|,
name|remotePath
argument_list|,
name|repository
argument_list|,
name|resource
operator|.
name|getFilePath
argument_list|()
argument_list|,
name|ext
argument_list|,
name|checksumFiles
index|[
name|i
index|]
operator|.
name|getFilePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NotFoundException
name|e
parameter_list|)
block|{
name|urlFailureCache
operator|.
name|cacheFailure
argument_list|(
name|url
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|NotModifiedException
name|e
parameter_list|)
block|{
comment|// Do not cache url here.
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
name|urlFailureCache
operator|.
name|cacheFailure
argument_list|(
name|url
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|WagonFactoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|wagon
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|wagon
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConnectionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to disconnect wagon."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|transferArtifact
parameter_list|(
name|Wagon
name|wagon
parameter_list|,
name|RemoteRepositoryContent
name|remoteRepository
parameter_list|,
name|String
name|remotePath
parameter_list|,
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|Path
name|resource
parameter_list|,
name|Path
name|tmpDirectory
parameter_list|,
name|StorageAsset
name|destFile
parameter_list|)
throws|throws
name|ProxyException
block|{
name|transferSimpleFile
argument_list|(
name|wagon
argument_list|,
name|remoteRepository
argument_list|,
name|remotePath
argument_list|,
name|repository
argument_list|,
name|resource
argument_list|,
name|destFile
operator|.
name|getFilePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>      * Quietly transfer the checksum file from the remote repository to the local file.      *</p>      *      * @param wagon            the wagon instance (should already be connected) to use.      * @param remoteRepository the remote repository to transfer from.      * @param remotePath       the remote path to the resource to get.      * @param repository       the managed repository that will hold the file      * @param resource         the local file that should contain the downloaded contents      * @param ext              the type of checksum to transfer (example: ".md5" or ".sha1")      * @throws ProxyException if copying the downloaded file into place did not succeed.      */
specifier|protected
name|void
name|transferChecksum
parameter_list|(
name|Wagon
name|wagon
parameter_list|,
name|RemoteRepositoryContent
name|remoteRepository
parameter_list|,
name|String
name|remotePath
parameter_list|,
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|Path
name|resource
parameter_list|,
name|String
name|ext
parameter_list|,
name|Path
name|destFile
parameter_list|)
throws|throws
name|ProxyException
block|{
name|String
name|url
init|=
name|remoteRepository
operator|.
name|getURL
argument_list|()
operator|.
name|getUrl
argument_list|()
operator|+
name|remotePath
operator|+
name|ext
decl_stmt|;
comment|// Transfer checksum does not use the policy.
if|if
condition|(
name|urlFailureCache
operator|.
name|hasFailedBefore
argument_list|(
name|url
argument_list|)
condition|)
block|{
return|return;
block|}
try|try
block|{
name|transferSimpleFile
argument_list|(
name|wagon
argument_list|,
name|remoteRepository
argument_list|,
name|remotePath
operator|+
name|ext
argument_list|,
name|repository
argument_list|,
name|resource
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Checksum {} Downloaded: {} to move to {}"
argument_list|,
name|url
argument_list|,
name|destFile
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NotFoundException
name|e
parameter_list|)
block|{
name|urlFailureCache
operator|.
name|cacheFailure
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Transfer failed, checksum not found: {}"
argument_list|,
name|url
argument_list|)
expr_stmt|;
comment|// Consume it, do not pass this on.
block|}
catch|catch
parameter_list|(
name|NotModifiedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Transfer skipped, checksum not modified: {}"
argument_list|,
name|url
argument_list|)
expr_stmt|;
comment|// Consume it, do not pass this on.
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
name|urlFailureCache
operator|.
name|cacheFailure
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Transfer failed on checksum: {} : {}"
argument_list|,
name|url
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// Critical issue, pass it on.
throw|throw
name|e
throw|;
block|}
block|}
comment|/**      * Perform the transfer of the remote file to the local file specified.      *      * @param wagon            the wagon instance to use.      * @param remoteRepository the remote repository to use      * @param remotePath       the remote path to attempt to get      * @param repository       the managed repository that will hold the file      * @param origFile         the local file to save to      * @throws ProxyException if there was a problem moving the downloaded file into place.      */
specifier|protected
name|void
name|transferSimpleFile
parameter_list|(
name|Wagon
name|wagon
parameter_list|,
name|RemoteRepositoryContent
name|remoteRepository
parameter_list|,
name|String
name|remotePath
parameter_list|,
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|Path
name|origFile
parameter_list|,
name|Path
name|destFile
parameter_list|)
throws|throws
name|ProxyException
block|{
assert|assert
operator|(
name|remotePath
operator|!=
literal|null
operator|)
assert|;
comment|// Transfer the file.
try|try
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|origFile
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Retrieving {} from {}"
argument_list|,
name|remotePath
argument_list|,
name|remoteRepository
operator|.
name|getRepository
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|wagon
operator|.
name|get
argument_list|(
name|addParameters
argument_list|(
name|remotePath
argument_list|,
name|remoteRepository
operator|.
name|getRepository
argument_list|()
argument_list|)
argument_list|,
name|destFile
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
comment|// You wouldn't get here on failure, a WagonException would have been thrown.
name|log
operator|.
name|debug
argument_list|(
literal|"Downloaded successfully."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Retrieving {} from {} if updated"
argument_list|,
name|remotePath
argument_list|,
name|remoteRepository
operator|.
name|getRepository
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|success
operator|=
name|wagon
operator|.
name|getIfNewer
argument_list|(
name|addParameters
argument_list|(
name|remotePath
argument_list|,
name|remoteRepository
operator|.
name|getRepository
argument_list|()
argument_list|)
argument_list|,
name|destFile
operator|.
name|toFile
argument_list|()
argument_list|,
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|origFile
argument_list|)
operator|.
name|toMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
literal|"Failed to the modification time of "
operator|+
name|origFile
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|success
condition|)
block|{
throw|throw
operator|new
name|NotModifiedException
argument_list|(
literal|"Not downloaded, as local file is newer than remote side: "
operator|+
name|origFile
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|destFile
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Downloaded successfully."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NotFoundException
argument_list|(
literal|"Resource ["
operator|+
name|remoteRepository
operator|.
name|getURL
argument_list|()
operator|+
literal|"/"
operator|+
name|remotePath
operator|+
literal|"] does not exist: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|WagonException
name|e
parameter_list|)
block|{
comment|// TODO: shouldn't have to drill into the cause, but TransferFailedException is often not descriptive enough
name|String
name|msg
init|=
literal|"Download failure on resource ["
operator|+
name|remoteRepository
operator|.
name|getURL
argument_list|()
operator|+
literal|"/"
operator|+
name|remotePath
operator|+
literal|"]:"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|+=
literal|" (cause: "
operator|+
name|e
operator|.
name|getCause
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
throw|throw
operator|new
name|ProxyException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Using wagon, connect to the remote repository.      *      * @param connector        the connector configuration to utilize (for obtaining network proxy configuration from)      * @param wagon            the wagon instance to establish the connection on.      * @param remoteRepository the remote repository to connect to.      * @return true if the connection was successful. false if not connected.      */
specifier|protected
name|boolean
name|connectToRepository
parameter_list|(
name|ProxyConnector
name|connector
parameter_list|,
name|Wagon
name|wagon
parameter_list|,
name|RemoteRepositoryContent
name|remoteRepository
parameter_list|)
block|{
name|boolean
name|connected
init|=
literal|false
decl_stmt|;
specifier|final
name|ProxyInfo
name|networkProxy
init|=
name|connector
operator|.
name|getProxyId
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|this
operator|.
name|networkProxyMap
operator|.
name|get
argument_list|(
name|connector
operator|.
name|getProxyId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|networkProxy
operator|!=
literal|null
condition|)
block|{
comment|// TODO: move to proxyInfo.toString()
name|String
name|msg
init|=
literal|"Using network proxy "
operator|+
name|networkProxy
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|networkProxy
operator|.
name|getPort
argument_list|()
operator|+
literal|" to connect to remote repository "
operator|+
name|remoteRepository
operator|.
name|getURL
argument_list|()
decl_stmt|;
if|if
condition|(
name|networkProxy
operator|.
name|getNonProxyHosts
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|+=
literal|"; excluding hosts: "
operator|+
name|networkProxy
operator|.
name|getNonProxyHosts
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|networkProxy
operator|.
name|getUserName
argument_list|()
argument_list|)
condition|)
block|{
name|msg
operator|+=
literal|"; as user: "
operator|+
name|networkProxy
operator|.
name|getUserName
argument_list|()
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
name|AuthenticationInfo
name|authInfo
init|=
literal|null
decl_stmt|;
name|String
name|username
init|=
literal|""
decl_stmt|;
name|String
name|password
init|=
literal|""
decl_stmt|;
name|RepositoryCredentials
name|repCred
init|=
name|remoteRepository
operator|.
name|getRepository
argument_list|()
operator|.
name|getLoginCredentials
argument_list|()
decl_stmt|;
if|if
condition|(
name|repCred
operator|!=
literal|null
operator|&&
name|repCred
operator|instanceof
name|PasswordCredentials
condition|)
block|{
name|PasswordCredentials
name|pwdCred
init|=
operator|(
name|PasswordCredentials
operator|)
name|repCred
decl_stmt|;
name|username
operator|=
name|pwdCred
operator|.
name|getUsername
argument_list|()
expr_stmt|;
name|password
operator|=
name|pwdCred
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
operator|new
name|String
argument_list|(
name|pwdCred
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|username
argument_list|)
operator|&&
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|password
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using username {} to connect to remote repository {}"
argument_list|,
name|username
argument_list|,
name|remoteRepository
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
name|authInfo
operator|=
operator|new
name|AuthenticationInfo
argument_list|()
expr_stmt|;
name|authInfo
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|authInfo
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
comment|// Convert seconds to milliseconds
name|long
name|timeoutInMilliseconds
init|=
name|remoteRepository
operator|.
name|getRepository
argument_list|()
operator|.
name|getTimeout
argument_list|()
operator|.
name|toMillis
argument_list|()
decl_stmt|;
comment|// Set timeout  read and connect
comment|// FIXME olamy having 2 config values
name|wagon
operator|.
name|setReadTimeout
argument_list|(
operator|(
name|int
operator|)
name|timeoutInMilliseconds
argument_list|)
expr_stmt|;
name|wagon
operator|.
name|setTimeout
argument_list|(
operator|(
name|int
operator|)
name|timeoutInMilliseconds
argument_list|)
expr_stmt|;
try|try
block|{
name|Repository
name|wagonRepository
init|=
operator|new
name|Repository
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|remoteRepository
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|wagon
operator|.
name|connect
argument_list|(
name|wagonRepository
argument_list|,
name|authInfo
argument_list|,
name|networkProxy
argument_list|)
expr_stmt|;
name|connected
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConnectionException
decl||
name|AuthenticationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not connect to {}: {}"
argument_list|,
name|remoteRepository
operator|.
name|getRepository
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|connected
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|connected
return|;
block|}
specifier|public
name|WagonFactory
name|getWagonFactory
parameter_list|()
block|{
return|return
name|wagonFactory
return|;
block|}
specifier|public
name|void
name|setWagonFactory
parameter_list|(
name|WagonFactory
name|wagonFactory
parameter_list|)
block|{
name|this
operator|.
name|wagonFactory
operator|=
name|wagonFactory
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|RepositoryType
argument_list|>
name|supports
parameter_list|()
block|{
return|return
name|REPOSITORY_TYPES
return|;
block|}
block|}
end_class

end_unit

