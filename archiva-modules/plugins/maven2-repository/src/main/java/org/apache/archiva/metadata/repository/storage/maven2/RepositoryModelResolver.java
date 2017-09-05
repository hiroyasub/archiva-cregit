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
name|ManagedRepository
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
name|common
operator|.
name|utils
operator|.
name|VersionUtil
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
name|maven2
operator|.
name|metadata
operator|.
name|MavenMetadataReader
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
name|model
operator|.
name|ArchivaRepositoryMetadata
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
name|SnapshotVersion
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
name|archiva
operator|.
name|proxy
operator|.
name|common
operator|.
name|WagonFactoryRequest
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
name|xml
operator|.
name|XMLException
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
name|RemoteRepository
argument_list|>
name|remoteRepositories
decl_stmt|;
specifier|private
name|ManagedRepository
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
specifier|private
specifier|static
specifier|final
name|String
name|METADATA_FILENAME
init|=
literal|"maven-metadata.xml"
decl_stmt|;
comment|// key/value: remote repo ID/network proxy
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|networkProxyMap
decl_stmt|;
specifier|private
name|ManagedRepository
name|managedRepository
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
name|ManagedRepository
name|managedRepository
parameter_list|,
name|RepositoryPathTranslator
name|pathTranslator
parameter_list|,
name|WagonFactory
name|wagonFactory
parameter_list|,
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|remoteRepositories
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|networkProxiesMap
parameter_list|,
name|ManagedRepository
name|targetRepository
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|File
argument_list|(
name|managedRepository
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|,
name|pathTranslator
argument_list|)
expr_stmt|;
name|this
operator|.
name|managedRepository
operator|=
name|managedRepository
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
annotation|@
name|Override
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
comment|/**              *              */
comment|// is a SNAPSHOT ? so we can try to find locally before asking remote repositories.
if|if
condition|(
name|StringUtils
operator|.
name|contains
argument_list|(
name|version
argument_list|,
name|VersionUtil
operator|.
name|SNAPSHOT
argument_list|)
condition|)
block|{
name|File
name|localSnapshotModel
init|=
name|findTimeStampedSnapshotPom
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|model
operator|.
name|getParent
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|localSnapshotModel
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|FileModelSource
argument_list|(
name|localSnapshotModel
argument_list|)
return|;
block|}
block|}
for|for
control|(
name|RemoteRepository
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
literal|"Model '{}' successfully retrieved from remote repository '{}'"
argument_list|,
name|model
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"An exception was caught while attempting to retrieve model '{}' from remote repository '{}'.Reason:{}"
argument_list|,
name|model
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
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
name|warn
argument_list|(
literal|"An exception was caught while attempting to retrieve model '{}' from remote repository '{}'.Reason:{}"
argument_list|,
name|model
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
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
specifier|protected
name|File
name|findTimeStampedSnapshotPom
parameter_list|(
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
name|parentDirectory
parameter_list|)
block|{
comment|// reading metadata if there
name|File
name|mavenMetadata
init|=
operator|new
name|File
argument_list|(
name|parentDirectory
argument_list|,
name|METADATA_FILENAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|mavenMetadata
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|ArchivaRepositoryMetadata
name|archivaRepositoryMetadata
init|=
name|MavenMetadataReader
operator|.
name|read
argument_list|(
name|mavenMetadata
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
name|SnapshotVersion
name|snapshotVersion
init|=
name|archivaRepositoryMetadata
operator|.
name|getSnapshotVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|snapshotVersion
operator|!=
literal|null
condition|)
block|{
name|String
name|lastVersion
init|=
name|snapshotVersion
operator|.
name|getTimestamp
argument_list|()
decl_stmt|;
name|int
name|buildNumber
init|=
name|snapshotVersion
operator|.
name|getBuildNumber
argument_list|()
decl_stmt|;
name|String
name|snapshotPath
init|=
name|StringUtils
operator|.
name|replaceChars
argument_list|(
name|groupId
argument_list|,
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|'/'
operator|+
name|artifactId
operator|+
literal|'/'
operator|+
name|version
operator|+
literal|'/'
operator|+
name|artifactId
operator|+
literal|'-'
operator|+
name|StringUtils
operator|.
name|remove
argument_list|(
name|version
argument_list|,
literal|"-"
operator|+
name|VersionUtil
operator|.
name|SNAPSHOT
argument_list|)
operator|+
literal|'-'
operator|+
name|lastVersion
operator|+
literal|'-'
operator|+
name|buildNumber
operator|+
literal|".pom"
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"use snapshot path {} for maven coordinate {}:{}:{}"
argument_list|,
name|snapshotPath
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|File
name|model
init|=
operator|new
name|File
argument_list|(
name|basedir
argument_list|,
name|snapshotPath
argument_list|)
decl_stmt|;
comment|//model = pathTranslator.toFile( basedir, groupId, artifactId, lastVersion, filename );
if|if
condition|(
name|model
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|model
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|XMLException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"fail to read {}, {}"
argument_list|,
name|mavenMetadata
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|ModelResolver
name|newCopy
parameter_list|()
block|{
return|return
operator|new
name|RepositoryModelResolver
argument_list|(
name|managedRepository
argument_list|,
name|pathTranslator
argument_list|,
name|wagonFactory
argument_list|,
name|remoteRepositories
argument_list|,
name|networkProxyMap
argument_list|,
name|targetRepository
argument_list|)
return|;
block|}
comment|// FIXME: we need to do some refactoring, we cannot re-use the proxy components of archiva-proxy in maven2-repository
comment|// because it's causing a cyclic dependency
specifier|private
name|boolean
name|getModelFromProxy
parameter_list|(
name|RemoteRepository
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
throws|,
name|XMLException
throws|,
name|IOException
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
specifier|final
name|NetworkProxy
name|networkProxy
init|=
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
decl_stmt|;
name|wagon
operator|=
name|wagonFactory
operator|.
name|getWagon
argument_list|(
operator|new
name|WagonFactoryRequest
argument_list|(
literal|"wagon#"
operator|+
name|protocol
argument_list|,
name|remoteRepository
operator|.
name|getExtraHeaders
argument_list|()
argument_list|)
operator|.
name|networkProxy
argument_list|(
name|networkProxy
argument_list|)
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
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
condition|)
block|{
comment|// get the metadata first!
name|File
name|tmpMetadataResource
init|=
operator|new
name|File
argument_list|(
name|workingDirectory
argument_list|,
name|METADATA_FILENAME
argument_list|)
decl_stmt|;
name|String
name|metadataPath
init|=
name|StringUtils
operator|.
name|substringBeforeLast
argument_list|(
name|artifactPath
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|"/"
operator|+
name|METADATA_FILENAME
decl_stmt|;
name|wagon
operator|.
name|get
argument_list|(
name|addParameters
argument_list|(
name|metadataPath
argument_list|,
name|remoteRepository
argument_list|)
argument_list|,
name|tmpMetadataResource
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Successfully downloaded metadata."
argument_list|)
expr_stmt|;
name|ArchivaRepositoryMetadata
name|metadata
init|=
name|MavenMetadataReader
operator|.
name|read
argument_list|(
name|tmpMetadataResource
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
comment|// re-adjust to timestamp if present, otherwise retain the original -SNAPSHOT filename
name|SnapshotVersion
name|snapshotVersion
init|=
name|metadata
operator|.
name|getSnapshotVersion
argument_list|()
decl_stmt|;
name|String
name|timestampVersion
init|=
name|version
decl_stmt|;
if|if
condition|(
name|snapshotVersion
operator|!=
literal|null
condition|)
block|{
name|timestampVersion
operator|=
name|timestampVersion
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|timestampVersion
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
expr_stmt|;
comment|// remove SNAPSHOT from end
name|timestampVersion
operator|=
name|timestampVersion
operator|+
name|snapshotVersion
operator|.
name|getTimestamp
argument_list|()
operator|+
literal|"-"
operator|+
name|snapshotVersion
operator|.
name|getBuildNumber
argument_list|()
expr_stmt|;
name|filename
operator|=
name|artifactId
operator|+
literal|"-"
operator|+
name|timestampVersion
operator|+
literal|".pom"
expr_stmt|;
name|artifactPath
operator|=
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
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"New artifactPath :{}"
argument_list|,
name|artifactPath
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Retrieving {} from {}"
argument_list|,
name|artifactPath
argument_list|,
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
name|addParameters
argument_list|(
name|artifactPath
argument_list|,
name|remoteRepository
argument_list|)
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
name|RemoteRepository
name|remoteRepository
parameter_list|)
block|{
name|boolean
name|connected
decl_stmt|;
specifier|final
name|NetworkProxy
name|proxyConnector
init|=
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
decl_stmt|;
name|ProxyInfo
name|networkProxy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|proxyConnector
operator|!=
literal|null
condition|)
block|{
name|networkProxy
operator|=
operator|new
name|ProxyInfo
argument_list|()
expr_stmt|;
name|networkProxy
operator|.
name|setType
argument_list|(
name|proxyConnector
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|networkProxy
operator|.
name|setHost
argument_list|(
name|proxyConnector
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|networkProxy
operator|.
name|setPort
argument_list|(
name|proxyConnector
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|networkProxy
operator|.
name|setUserName
argument_list|(
name|proxyConnector
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|networkProxy
operator|.
name|setPassword
argument_list|(
name|proxyConnector
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
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
name|getUserName
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
literal|"Using username {} to connect to remote repository {}"
argument_list|,
name|username
argument_list|,
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
comment|// FIXME olamy having 2 config values
comment|// Set timeout
name|wagon
operator|.
name|setReadTimeout
argument_list|(
name|timeoutInMilliseconds
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|networkProxy
operator|!=
literal|null
condition|)
block|{
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
block|}
else|else
block|{
name|wagon
operator|.
name|connect
argument_list|(
name|wagonRepository
argument_list|,
name|authInfo
argument_list|)
expr_stmt|;
block|}
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
name|error
argument_list|(
literal|"Could not connect to {}:{} "
argument_list|,
name|remoteRepository
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
comment|/**      *      * @param wagon The wagon instance that should be connected.      * @param remoteRepository The repository from where the checksum file should be retrieved      * @param remotePath The remote path of the artifact (without extension)      * @param resource The local artifact (without extension)      * @param workingDir The working directory where the downloaded file should be placed to      * @param ext The extension of th checksum file      * @return The file where the data has been downloaded to.      * @throws AuthorizationException      * @throws TransferFailedException      * @throws ResourceDoesNotExistException      */
specifier|private
name|File
name|transferChecksum
parameter_list|(
specifier|final
name|Wagon
name|wagon
parameter_list|,
specifier|final
name|RemoteRepository
name|remoteRepository
parameter_list|,
specifier|final
name|String
name|remotePath
parameter_list|,
specifier|final
name|File
name|resource
parameter_list|,
specifier|final
name|File
name|workingDir
parameter_list|,
specifier|final
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
name|workingDir
argument_list|,
name|resource
operator|.
name|getName
argument_list|()
operator|+
name|ext
argument_list|)
decl_stmt|;
name|String
name|remoteChecksumPath
init|=
name|remotePath
operator|+
name|ext
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Retrieving {} from {}"
argument_list|,
name|remoteChecksumPath
argument_list|,
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
name|addParameters
argument_list|(
name|remoteChecksumPath
argument_list|,
name|remoteRepository
argument_list|)
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
throws|throws
name|IOException
block|{
return|return
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"temp"
argument_list|)
operator|.
name|toFile
argument_list|()
return|;
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
literal|"Tried to copy file {} to {} but file with this name already exists."
argument_list|,
name|fileToMove
operator|.
name|getName
argument_list|()
argument_list|,
name|newLocation
operator|.
name|getAbsolutePath
argument_list|()
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
specifier|protected
name|String
name|addParameters
parameter_list|(
name|String
name|path
parameter_list|,
name|RemoteRepository
name|remoteRepository
parameter_list|)
block|{
if|if
condition|(
name|remoteRepository
operator|.
name|getExtraParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|path
return|;
block|}
name|boolean
name|question
init|=
literal|false
decl_stmt|;
name|StringBuilder
name|res
init|=
operator|new
name|StringBuilder
argument_list|(
name|path
operator|==
literal|null
condition|?
literal|""
else|:
name|path
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|remoteRepository
operator|.
name|getExtraParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|question
condition|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|res
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

