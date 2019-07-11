begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|maven2
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
name|common
operator|.
name|filelock
operator|.
name|DefaultFileLockManager
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
name|filelock
operator|.
name|FileLockManager
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
name|PathUtil
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
name|content
operator|.
name|FilesystemStorage
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
name|content
operator|.
name|RepositoryStorage
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
name|content
operator|.
name|maven2
operator|.
name|MavenRepositoryRequestInfo
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
name|features
operator|.
name|ArtifactCleanupFeature
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
name|features
operator|.
name|IndexCreationFeature
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
name|features
operator|.
name|RepositoryFeature
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
name|features
operator|.
name|StagingRepositoryFeature
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * Maven2 managed repository implementation.  */
end_comment

begin_class
specifier|public
class|class
name|MavenManagedRepository
extends|extends
name|AbstractManagedRepository
block|{
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
name|MavenManagedRepository
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_LAYOUT
init|=
literal|"default"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LEGACY_LAYOUT
init|=
literal|"legacy"
decl_stmt|;
specifier|private
name|ArtifactCleanupFeature
name|artifactCleanupFeature
init|=
operator|new
name|ArtifactCleanupFeature
argument_list|( )
decl_stmt|;
specifier|private
name|IndexCreationFeature
name|indexCreationFeature
decl_stmt|;
specifier|private
name|StagingRepositoryFeature
name|stagingRepositoryFeature
init|=
operator|new
name|StagingRepositoryFeature
argument_list|(  )
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|RepositoryCapabilities
name|CAPABILITIES
init|=
operator|new
name|StandardCapabilities
argument_list|(
operator|new
name|ReleaseScheme
index|[]
block|{
name|ReleaseScheme
operator|.
name|RELEASE
block|,
name|ReleaseScheme
operator|.
name|SNAPSHOT
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
name|DEFAULT_LAYOUT
block|,
name|LEGACY_LAYOUT
block|}
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|,
operator|new
name|String
index|[]
block|{
name|ArtifactCleanupFeature
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|IndexCreationFeature
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|StagingRepositoryFeature
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
specifier|public
name|MavenManagedRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|FilesystemStorage
name|storage
parameter_list|)
block|{
name|super
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
argument_list|,
name|id
argument_list|,
name|name
argument_list|,
name|storage
argument_list|)
expr_stmt|;
name|this
operator|.
name|indexCreationFeature
operator|=
operator|new
name|IndexCreationFeature
argument_list|(
name|this
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|setLocation
argument_list|(
name|storage
operator|.
name|getAsset
argument_list|(
literal|""
argument_list|)
operator|.
name|getFilePath
argument_list|()
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MavenManagedRepository
parameter_list|(
name|Locale
name|primaryLocale
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|FilesystemStorage
name|storage
parameter_list|)
block|{
name|super
argument_list|(
name|primaryLocale
argument_list|,
name|RepositoryType
operator|.
name|MAVEN
argument_list|,
name|id
argument_list|,
name|name
argument_list|,
name|storage
argument_list|)
expr_stmt|;
name|setLocation
argument_list|(
name|storage
operator|.
name|getAsset
argument_list|(
literal|""
argument_list|)
operator|.
name|getFilePath
argument_list|()
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryCapabilities
name|getCapabilities
parameter_list|( )
block|{
return|return
name|CAPABILITIES
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
name|getFeature
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|UnsupportedFeatureException
block|{
if|if
condition|(
name|ArtifactCleanupFeature
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
operator|)
name|artifactCleanupFeature
return|;
block|}
if|else if
condition|(
name|IndexCreationFeature
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
operator|)
name|indexCreationFeature
return|;
block|}
if|else if
condition|(
name|StagingRepositoryFeature
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
operator|)
name|stagingRepositoryFeature
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedFeatureException
argument_list|(  )
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|boolean
name|supportsFeature
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|ArtifactCleanupFeature
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
operator|||
name|IndexCreationFeature
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
operator|||
name|StagingRepositoryFeature
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasIndex
parameter_list|( )
block|{
return|return
name|indexCreationFeature
operator|.
name|hasIndex
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLocation
parameter_list|(
name|URI
name|location
parameter_list|)
block|{
name|URI
name|previousLocation
init|=
name|super
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|Path
name|previousLoc
init|=
name|PathUtil
operator|.
name|getPathFromUri
argument_list|(
name|previousLocation
argument_list|)
decl_stmt|;
name|Path
name|newLoc
init|=
name|PathUtil
operator|.
name|getPathFromUri
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|newLoc
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|equals
argument_list|(
name|previousLoc
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
condition|)
block|{
name|super
operator|.
name|setLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|newLoc
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|newLoc
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
name|error
argument_list|(
literal|"Could not create directory {}"
argument_list|,
name|location
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|FilesystemStorage
name|previous
init|=
operator|(
name|FilesystemStorage
operator|)
name|getStorage
argument_list|()
decl_stmt|;
try|try
block|{
name|FilesystemStorage
name|fs
init|=
operator|new
name|FilesystemStorage
argument_list|(
name|newLoc
argument_list|,
name|previous
operator|.
name|getFileLockManager
argument_list|()
argument_list|)
decl_stmt|;
name|setStorage
argument_list|(
name|fs
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
name|error
argument_list|(
literal|"Could not create new filesystem storage at {}"
argument_list|,
name|newLoc
argument_list|)
expr_stmt|;
try|try
block|{
name|Path
name|tmpDir
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"tmp-repo-"
operator|+
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|FilesystemStorage
name|fs
init|=
operator|new
name|FilesystemStorage
argument_list|(
name|tmpDir
argument_list|,
name|previous
operator|.
name|getFileLockManager
argument_list|()
argument_list|)
decl_stmt|;
name|setStorage
argument_list|(
name|fs
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not setup storage for repository "
operator|+
name|getId
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryRequestInfo
name|getRequestInfo
parameter_list|()
block|{
return|return
operator|new
name|MavenRepositoryRequestInfo
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|MavenManagedRepository
name|newLocalInstance
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|Path
name|basePath
parameter_list|)
throws|throws
name|IOException
block|{
name|FileLockManager
name|lockManager
init|=
operator|new
name|DefaultFileLockManager
argument_list|()
decl_stmt|;
name|FilesystemStorage
name|storage
init|=
operator|new
name|FilesystemStorage
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
name|id
argument_list|)
argument_list|,
name|lockManager
argument_list|)
decl_stmt|;
return|return
operator|new
name|MavenManagedRepository
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|storage
argument_list|)
return|;
block|}
block|}
end_class

end_unit

