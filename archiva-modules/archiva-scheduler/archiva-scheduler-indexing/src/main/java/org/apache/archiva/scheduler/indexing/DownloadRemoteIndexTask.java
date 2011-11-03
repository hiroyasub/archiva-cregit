begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|indexing
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|beans
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|RemoteRepository
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
name|admin
operator|.
name|model
operator|.
name|remote
operator|.
name|RemoteRepositoryAdmin
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
name|time
operator|.
name|StopWatch
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
name|index
operator|.
name|context
operator|.
name|IndexingContext
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
name|index
operator|.
name|updater
operator|.
name|IndexUpdateRequest
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
name|index
operator|.
name|updater
operator|.
name|IndexUpdater
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
name|index
operator|.
name|updater
operator|.
name|ResourceFetcher
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
name|events
operator|.
name|TransferEvent
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
name|events
operator|.
name|TransferListener
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
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|shared
operator|.
name|http
operator|.
name|HttpConfiguration
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
name|shared
operator|.
name|http
operator|.
name|HttpMethodConfiguration
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
specifier|public
class|class
name|DownloadRemoteIndexTask
implements|implements
name|Runnable
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|RemoteRepository
name|remoteRepository
decl_stmt|;
specifier|private
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
decl_stmt|;
specifier|private
name|WagonFactory
name|wagonFactory
decl_stmt|;
specifier|private
name|NetworkProxy
name|networkProxy
decl_stmt|;
specifier|private
name|boolean
name|fullDownload
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|runningRemoteDownloadIds
decl_stmt|;
specifier|private
name|IndexUpdater
name|indexUpdater
decl_stmt|;
specifier|public
name|DownloadRemoteIndexTask
parameter_list|(
name|DownloadRemoteIndexTaskRequest
name|downloadRemoteIndexTaskRequest
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|runningRemoteDownloadIds
parameter_list|)
block|{
name|this
operator|.
name|remoteRepository
operator|=
name|downloadRemoteIndexTaskRequest
operator|.
name|getRemoteRepository
argument_list|()
expr_stmt|;
name|this
operator|.
name|wagonFactory
operator|=
name|downloadRemoteIndexTaskRequest
operator|.
name|getWagonFactory
argument_list|()
expr_stmt|;
name|this
operator|.
name|networkProxy
operator|=
name|downloadRemoteIndexTaskRequest
operator|.
name|getNetworkProxy
argument_list|()
expr_stmt|;
name|this
operator|.
name|fullDownload
operator|=
name|downloadRemoteIndexTaskRequest
operator|.
name|isFullDownload
argument_list|()
expr_stmt|;
name|this
operator|.
name|runningRemoteDownloadIds
operator|=
name|runningRemoteDownloadIds
expr_stmt|;
name|this
operator|.
name|indexUpdater
operator|=
name|downloadRemoteIndexTaskRequest
operator|.
name|getIndexUpdater
argument_list|()
expr_stmt|;
name|this
operator|.
name|remoteRepositoryAdmin
operator|=
name|downloadRemoteIndexTaskRequest
operator|.
name|getRemoteRepositoryAdmin
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// so short lock : not sure we need it
synchronized|synchronized
init|(
name|this
operator|.
name|runningRemoteDownloadIds
init|)
block|{
if|if
condition|(
name|this
operator|.
name|runningRemoteDownloadIds
operator|.
name|contains
argument_list|(
name|this
operator|.
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
comment|// skip it as it's running
name|log
operator|.
name|info
argument_list|(
literal|"skip download index remote for repo {} it's already running"
argument_list|,
name|this
operator|.
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|this
operator|.
name|runningRemoteDownloadIds
operator|.
name|add
argument_list|(
name|this
operator|.
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|File
name|tempIndexDirectory
init|=
literal|null
decl_stmt|;
name|StopWatch
name|stopWatch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|stopWatch
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"start download remote index for remote repository "
operator|+
name|this
operator|.
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|IndexingContext
name|indexingContext
init|=
name|remoteRepositoryAdmin
operator|.
name|createIndexContext
argument_list|(
name|this
operator|.
name|remoteRepository
argument_list|)
decl_stmt|;
comment|// create a temp directory to download files
name|tempIndexDirectory
operator|=
operator|new
name|File
argument_list|(
name|indexingContext
operator|.
name|getIndexDirectoryFile
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|,
literal|".tmpIndex"
argument_list|)
expr_stmt|;
name|File
name|indexCacheDirectory
init|=
operator|new
name|File
argument_list|(
name|indexingContext
operator|.
name|getIndexDirectoryFile
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|,
literal|".indexCache"
argument_list|)
decl_stmt|;
name|indexCacheDirectory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
name|tempIndexDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|tempIndexDirectory
argument_list|)
expr_stmt|;
block|}
name|tempIndexDirectory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|tempIndexDirectory
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|String
name|baseIndexUrl
init|=
name|indexingContext
operator|.
name|getIndexUpdateUrl
argument_list|()
decl_stmt|;
specifier|final
name|Wagon
name|wagon
init|=
name|wagonFactory
operator|.
name|getWagon
argument_list|(
operator|new
name|URL
argument_list|(
name|this
operator|.
name|remoteRepository
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getProtocol
argument_list|()
argument_list|)
decl_stmt|;
name|setupWagonReadTimeout
argument_list|(
name|wagon
argument_list|)
expr_stmt|;
comment|// TODO transferListener
name|wagon
operator|.
name|addTransferListener
argument_list|(
operator|new
name|DownloadListener
argument_list|()
argument_list|)
expr_stmt|;
name|ProxyInfo
name|proxyInfo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|networkProxy
operator|!=
literal|null
condition|)
block|{
name|proxyInfo
operator|=
operator|new
name|ProxyInfo
argument_list|()
expr_stmt|;
name|proxyInfo
operator|.
name|setHost
argument_list|(
name|this
operator|.
name|networkProxy
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setPort
argument_list|(
name|this
operator|.
name|networkProxy
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setUserName
argument_list|(
name|this
operator|.
name|networkProxy
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setPassword
argument_list|(
name|this
operator|.
name|networkProxy
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|AuthenticationInfo
name|authenticationInfo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|remoteRepository
operator|.
name|getUserName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|authenticationInfo
operator|=
operator|new
name|AuthenticationInfo
argument_list|()
expr_stmt|;
name|authenticationInfo
operator|.
name|setUserName
argument_list|(
name|this
operator|.
name|remoteRepository
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|authenticationInfo
operator|.
name|setPassword
argument_list|(
name|this
operator|.
name|remoteRepository
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|wagon
operator|.
name|connect
argument_list|(
operator|new
name|Repository
argument_list|(
name|this
operator|.
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|baseIndexUrl
argument_list|)
argument_list|,
name|authenticationInfo
argument_list|,
name|proxyInfo
argument_list|)
expr_stmt|;
name|File
name|indexDirectory
init|=
name|indexingContext
operator|.
name|getIndexDirectoryFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|indexDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|indexDirectory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|ResourceFetcher
name|resourceFetcher
init|=
operator|new
name|WagonResourceFetcher
argument_list|(
name|log
argument_list|,
name|tempIndexDirectory
argument_list|,
name|wagon
argument_list|)
decl_stmt|;
name|IndexUpdateRequest
name|request
init|=
operator|new
name|IndexUpdateRequest
argument_list|(
name|indexingContext
argument_list|,
name|resourceFetcher
argument_list|)
decl_stmt|;
name|request
operator|.
name|setForceFullUpdate
argument_list|(
name|this
operator|.
name|fullDownload
argument_list|)
expr_stmt|;
name|request
operator|.
name|setLocalIndexCacheDir
argument_list|(
name|indexCacheDirectory
argument_list|)
expr_stmt|;
name|this
operator|.
name|indexUpdater
operator|.
name|fetchAndUpdateIndex
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|stopWatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"time to download remote repository index for repository {}: {} s"
argument_list|,
name|this
operator|.
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
operator|(
name|stopWatch
operator|.
name|getTime
argument_list|()
operator|/
literal|1000
operator|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
catch|catch
parameter_list|(
name|WagonFactoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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
name|deleteDirectoryQuiet
argument_list|(
name|tempIndexDirectory
argument_list|)
expr_stmt|;
name|this
operator|.
name|runningRemoteDownloadIds
operator|.
name|remove
argument_list|(
name|this
operator|.
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"end download remote index for remote repository "
operator|+
name|this
operator|.
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|deleteDirectoryQuiet
parameter_list|(
name|File
name|f
parameter_list|)
block|{
try|try
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"skip error delete "
operator|+
name|f
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setupWagonReadTimeout
parameter_list|(
name|Wagon
name|wagon
parameter_list|)
block|{
try|try
block|{
name|HttpConfiguration
name|httpConfiguration
init|=
operator|new
name|HttpConfiguration
argument_list|()
operator|.
name|setAll
argument_list|(
operator|new
name|HttpMethodConfiguration
argument_list|()
operator|.
name|setReadTimeout
argument_list|(
name|remoteRepository
operator|.
name|getRemoteDownloadTimeout
argument_list|()
operator|*
literal|1000
argument_list|)
argument_list|)
decl_stmt|;
name|Method
name|setHttpConfigurationMethod
init|=
name|wagon
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setHttpConfiguration"
argument_list|,
name|HttpConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|setHttpConfigurationMethod
operator|.
name|invoke
argument_list|(
name|wagon
argument_list|,
name|httpConfiguration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"unable to set download remote time out for index {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|DownloadListener
implements|implements
name|TransferListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|reourceName
decl_stmt|;
name|long
name|startTime
decl_stmt|;
specifier|public
name|void
name|transferInitiated
parameter_list|(
name|TransferEvent
name|transferEvent
parameter_list|)
block|{
name|reourceName
operator|=
name|transferEvent
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"initiate transfer of {}"
argument_list|,
name|reourceName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|transferStarted
parameter_list|(
name|TransferEvent
name|transferEvent
parameter_list|)
block|{
name|reourceName
operator|=
name|transferEvent
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|startTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"start transfer of {}"
argument_list|,
name|transferEvent
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|transferProgress
parameter_list|(
name|TransferEvent
name|transferEvent
parameter_list|,
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"transfer of {} : {}/{}"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|transferEvent
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|buffer
operator|.
name|length
argument_list|,
name|length
argument_list|)
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|transferCompleted
parameter_list|(
name|TransferEvent
name|transferEvent
parameter_list|)
block|{
name|reourceName
operator|=
name|transferEvent
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|long
name|endTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"end of transfer file {}: {}s"
argument_list|,
name|transferEvent
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
name|endTime
operator|-
name|startTime
operator|)
operator|/
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|transferError
parameter_list|(
name|TransferEvent
name|transferEvent
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"error of transfer file {}: {}"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|transferEvent
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|transferEvent
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
literal|2
index|]
argument_list|)
argument_list|,
name|transferEvent
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"transfer debug {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|WagonResourceFetcher
implements|implements
name|ResourceFetcher
block|{
name|Logger
name|log
decl_stmt|;
name|File
name|tempIndexDirectory
decl_stmt|;
name|Wagon
name|wagon
decl_stmt|;
specifier|private
name|WagonResourceFetcher
parameter_list|(
name|Logger
name|log
parameter_list|,
name|File
name|tempIndexDirectory
parameter_list|,
name|Wagon
name|wagon
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
name|this
operator|.
name|tempIndexDirectory
operator|=
name|tempIndexDirectory
expr_stmt|;
name|this
operator|.
name|wagon
operator|=
name|wagon
expr_stmt|;
block|}
specifier|public
name|void
name|connect
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|IOException
block|{
comment|//no op
block|}
specifier|public
name|void
name|disconnect
parameter_list|()
throws|throws
name|IOException
block|{
comment|// no op
block|}
specifier|public
name|InputStream
name|retrieve
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
throws|,
name|FileNotFoundException
block|{
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"index update retrieve file, name:{}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|tempIndexDirectory
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|file
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|wagon
operator|.
name|get
argument_list|(
name|name
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|TransferFailedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

