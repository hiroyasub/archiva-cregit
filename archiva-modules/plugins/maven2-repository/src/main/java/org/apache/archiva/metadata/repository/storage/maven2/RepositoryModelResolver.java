begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|RepositoryPathTranslator
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
name|common
operator|.
name|WagonFactory
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
name|common
operator|.
name|WagonFactoryException
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
name|io
operator|.
name|FileUtils
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
name|lang
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
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|archiva
operator|.
name|configuration
operator|.
name|RemoteRepositoryConfiguration
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
name|model
operator|.
name|Repository
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
name|model
operator|.
name|building
operator|.
name|FileModelSource
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
name|model
operator|.
name|building
operator|.
name|ModelSource
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
name|model
operator|.
name|resolution
operator|.
name|InvalidRepositoryException
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
name|model
operator|.
name|resolution
operator|.
name|ModelResolver
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
name|model
operator|.
name|resolution
operator|.
name|UnresolvableModelException
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
name|TransferFailedException
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
name|authorization
operator|.
name|AuthorizationException
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryModelResolver
implements|implements
name|ModelResolver
block|{
specifier|private
name|File
name|basedir
decl_stmt|;
specifier|private
name|RepositoryPathTranslator
name|pathTranslator
decl_stmt|;
specifier|private
name|WagonFactory
name|wagonFactory
decl_stmt|;
specifier|private
name|List
argument_list|<
name|RemoteRepositoryConfiguration
argument_list|>
name|remoteRepositories
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|targetRepository
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RepositoryModelResolver
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// key/value: remote repo ID/network proxy
name|Map
argument_list|<
name|String
argument_list|,
name|ProxyInfo
argument_list|>
name|networkProxyMap
decl_stmt|;
specifier|public
name|RepositoryModelResolver
parameter_list|(
name|File
name|basedir
parameter_list|,
name|RepositoryPathTranslator
name|pathTranslator
parameter_list|)
block|{
name|this
operator|.
name|basedir
operator|=
name|basedir
expr_stmt|;
name|this
operator|.
name|pathTranslator
operator|=
name|pathTranslator
expr_stmt|;
block|}
specifier|public
name|RepositoryModelResolver
parameter_list|(
name|File
name|basedir
parameter_list|,
name|RepositoryPathTranslator
name|pathTranslator
parameter_list|,
name|WagonFactory
name|wagonFactory
parameter_list|,
name|List
argument_list|<
name|RemoteRepositoryConfiguration
argument_list|>
name|remoteRepositories
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ProxyInfo
argument_list|>
name|networkProxiesMap
parameter_list|,
name|ManagedRepositoryConfiguration
name|targetRepository
parameter_list|)
block|{
name|this
argument_list|(
name|basedir
argument_list|,
name|pathTranslator
argument_list|)
expr_stmt|;
name|this
operator|.
name|wagonFactory
operator|=
name|wagonFactory
expr_stmt|;
name|this
operator|.
name|remoteRepositories
operator|=
name|remoteRepositories
expr_stmt|;
name|this
operator|.
name|networkProxyMap
operator|=
name|networkProxiesMap
expr_stmt|;
name|this
operator|.
name|targetRepository
operator|=
name|targetRepository
expr_stmt|;
block|}
specifier|public
name|ModelSource
name|resolveModel
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|UnresolvableModelException
block|{
name|String
name|filename
init|=
name|artifactId
operator|+
literal|"-"
operator|+
name|version
operator|+
literal|".pom"
decl_stmt|;
comment|// TODO: we need to convert 1.0-20091120.112233-1 type paths to baseVersion for the below call - add a test
name|File
name|model
init|=
name|pathTranslator
operator|.
name|toFile
argument_list|(
name|basedir
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|model
operator|.
name|exists
argument_list|()
condition|)
block|{
for|for
control|(
name|RemoteRepositoryConfiguration
name|remoteRepository
range|:
name|remoteRepositories
control|)
block|{
try|try
block|{
name|boolean
name|success
init|=
name|getModelFromProxy
argument_list|(
name|remoteRepository
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
name|success
operator|&&
name|model
operator|.
name|exists
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Model '"
operator|+
name|model
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"' successfully retrieved from remote repository '"
operator|+
name|remoteRepository
operator|.
name|getId
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"An exception was caught while attempting to retrieve model '"
operator|+
name|model
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"' from remote repository '"
operator|+
name|remoteRepository
operator|.
name|getId
argument_list|()
operator|+
literal|"'."
argument_list|,
name|e
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
return|return
operator|new
name|FileModelSource
argument_list|(
name|model
argument_list|)
return|;
block|}
specifier|public
name|void
name|addRepository
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|InvalidRepositoryException
block|{
comment|// we just ignore repositories outside of the current one for now
comment|// TODO: it'd be nice to look them up from Archiva's set, but we want to do that by URL / mapping, not just the
comment|//       ID since they will rarely match
block|}
specifier|public
name|ModelResolver
name|newCopy
parameter_list|()
block|{
return|return
operator|new
name|RepositoryModelResolver
argument_list|(
name|basedir
argument_list|,
name|pathTranslator
argument_list|)
return|;
block|}
comment|// TODO: we need to do some refactoring, we cannot re-use the proxy components of archiva-proxy in maven2-repository
comment|// because it's causing a cyclic dependency
specifier|private
name|boolean
name|getModelFromProxy
parameter_list|(
name|RemoteRepositoryConfiguration
name|remoteRepository
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|filename
parameter_list|)
throws|throws
name|AuthorizationException
throws|,
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|WagonFactoryException
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
name|File
name|tmpMd5
init|=
literal|null
decl_stmt|;
name|File
name|tmpSha1
init|=
literal|null
decl_stmt|;
name|File
name|tmpResource
init|=
literal|null
decl_stmt|;
name|String
name|artifactPath
init|=
name|pathTranslator
operator|.
name|toPath
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|filename
argument_list|)
decl_stmt|;
name|File
name|resource
init|=
operator|new
name|File
argument_list|(
name|targetRepository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|artifactPath
argument_list|)
decl_stmt|;
name|File
name|workingDirectory
init|=
name|createWorkingDirectory
argument_list|(
name|targetRepository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Wagon
name|wagon
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|protocol
init|=
name|getProtocol
argument_list|(
name|remoteRepository
operator|.
name|getUrl
argument_list|()
argument_list|)
decl_stmt|;
name|wagon
operator|=
name|wagonFactory
operator|.
name|getWagon
argument_list|(
literal|"wagon#"
operator|+
name|protocol
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
name|RuntimeException
argument_list|(
literal|"Unsupported remote repository protocol: "
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
name|tmpResource
operator|=
operator|new
name|File
argument_list|(
name|workingDirectory
argument_list|,
name|filename
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Retrieving "
operator|+
name|artifactPath
operator|+
literal|" from "
operator|+
name|remoteRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|wagon
operator|.
name|get
argument_list|(
name|artifactPath
argument_list|,
name|tmpResource
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Downloaded successfully."
argument_list|)
expr_stmt|;
name|tmpSha1
operator|=
name|transferChecksum
argument_list|(
name|wagon
argument_list|,
name|remoteRepository
argument_list|,
name|artifactPath
argument_list|,
name|tmpResource
argument_list|,
name|workingDirectory
argument_list|,
literal|".sha1"
argument_list|)
expr_stmt|;
name|tmpMd5
operator|=
name|transferChecksum
argument_list|(
name|wagon
argument_list|,
name|remoteRepository
argument_list|,
name|artifactPath
argument_list|,
name|tmpResource
argument_list|,
name|workingDirectory
argument_list|,
literal|".md5"
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|resource
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|intern
argument_list|()
init|)
block|{
name|File
name|directory
init|=
name|resource
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|moveFileIfExists
argument_list|(
name|tmpMd5
argument_list|,
name|directory
argument_list|)
expr_stmt|;
name|moveFileIfExists
argument_list|(
name|tmpSha1
argument_list|,
name|directory
argument_list|)
expr_stmt|;
name|moveFileIfExists
argument_list|(
name|tmpResource
argument_list|,
name|directory
argument_list|)
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|workingDirectory
argument_list|)
expr_stmt|;
block|}
comment|// do we still need to execute the consumers?
return|return
name|success
return|;
block|}
comment|/**      * Using wagon, connect to the remote repository.      *      * @param wagon the wagon instance to establish the connection on.      * @return true if the connection was successful. false if not connected.      */
specifier|private
name|boolean
name|connectToRepository
parameter_list|(
name|Wagon
name|wagon
parameter_list|,
name|RemoteRepositoryConfiguration
name|remoteRepository
parameter_list|)
block|{
name|boolean
name|connected
decl_stmt|;
specifier|final
name|ProxyInfo
name|networkProxy
decl_stmt|;
name|networkProxy
operator|=
name|this
operator|.
name|networkProxyMap
operator|.
name|get
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|networkProxy
operator|!=
literal|null
condition|)
block|{
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
name|getUrl
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
name|AuthenticationInfo
name|authInfo
init|=
literal|null
decl_stmt|;
name|String
name|username
init|=
name|remoteRepository
operator|.
name|getUsername
argument_list|()
decl_stmt|;
name|String
name|password
init|=
name|remoteRepository
operator|.
name|getPassword
argument_list|()
decl_stmt|;
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
literal|"Using username "
operator|+
name|username
operator|+
literal|" to connect to remote repository "
operator|+
name|remoteRepository
operator|.
name|getUrl
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
name|int
name|timeoutInMilliseconds
init|=
name|remoteRepository
operator|.
name|getTimeout
argument_list|()
operator|*
literal|1000
decl_stmt|;
comment|// Set timeout
name|wagon
operator|.
name|setTimeout
argument_list|(
name|timeoutInMilliseconds
argument_list|)
expr_stmt|;
try|try
block|{
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
name|wagonRepository
init|=
operator|new
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
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|remoteRepository
operator|.
name|getUrl
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
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not connect to "
operator|+
name|remoteRepository
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
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
catch|catch
parameter_list|(
name|AuthenticationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not connect to "
operator|+
name|remoteRepository
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
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
specifier|private
name|File
name|transferChecksum
parameter_list|(
name|Wagon
name|wagon
parameter_list|,
name|RemoteRepositoryConfiguration
name|remoteRepository
parameter_list|,
name|String
name|remotePath
parameter_list|,
name|File
name|resource
parameter_list|,
name|File
name|tmpDirectory
parameter_list|,
name|String
name|ext
parameter_list|)
throws|throws
name|AuthorizationException
throws|,
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
block|{
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|tmpDirectory
argument_list|,
name|resource
operator|.
name|getName
argument_list|()
operator|+
name|ext
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Retrieving "
operator|+
name|remotePath
operator|+
literal|" from "
operator|+
name|remoteRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|wagon
operator|.
name|get
argument_list|(
name|remotePath
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Downloaded successfully."
argument_list|)
expr_stmt|;
return|return
name|destFile
return|;
block|}
specifier|private
name|String
name|getProtocol
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|String
name|protocol
init|=
name|StringUtils
operator|.
name|substringBefore
argument_list|(
name|url
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
return|return
name|protocol
return|;
block|}
specifier|private
name|File
name|createWorkingDirectory
parameter_list|(
name|String
name|targetRepository
parameter_list|)
block|{
try|try
block|{
name|File
name|tmpDir
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|".workingdirectory"
argument_list|,
literal|null
argument_list|,
operator|new
name|File
argument_list|(
name|targetRepository
argument_list|)
argument_list|)
decl_stmt|;
name|tmpDir
operator|.
name|delete
argument_list|()
expr_stmt|;
name|tmpDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
return|return
name|tmpDir
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not create working directory for this request"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|moveFileIfExists
parameter_list|(
name|File
name|fileToMove
parameter_list|,
name|File
name|directory
parameter_list|)
block|{
if|if
condition|(
name|fileToMove
operator|!=
literal|null
operator|&&
name|fileToMove
operator|.
name|exists
argument_list|()
condition|)
block|{
name|File
name|newLocation
init|=
operator|new
name|File
argument_list|(
name|directory
argument_list|,
name|fileToMove
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|newLocation
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|newLocation
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to overwrite existing target file: "
operator|+
name|newLocation
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|newLocation
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|fileToMove
operator|.
name|renameTo
argument_list|(
name|newLocation
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to rename tmp file to its final name... resorting to copy command."
argument_list|)
expr_stmt|;
try|try
block|{
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|fileToMove
argument_list|,
name|newLocation
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|newLocation
operator|.
name|exists
argument_list|()
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Tried to copy file "
operator|+
name|fileToMove
operator|.
name|getName
argument_list|()
operator|+
literal|" to "
operator|+
name|newLocation
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" but file with this name already exists."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot copy tmp file "
operator|+
name|fileToMove
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" to its final location"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|fileToMove
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

