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
name|repository
operator|.
name|storage
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
name|storage
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

begin_comment
comment|/**  *  * Just a helper class, mainly used for unit tests.  *  *  */
end_comment

begin_class
specifier|public
class|class
name|BasicManagedRepository
extends|extends
name|AbstractManagedRepository
block|{
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BasicManagedRepository
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArtifactCleanupFeature
name|artifactCleanupFeature
init|=
operator|new
name|ArtifactCleanupFeature
argument_list|(  )
decl_stmt|;
name|StagingRepositoryFeature
name|stagingRepositoryFeature
init|=
operator|new
name|StagingRepositoryFeature
argument_list|( )
decl_stmt|;
specifier|static
specifier|final
name|StandardCapabilities
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
literal|"default"
block|}
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[]
block|{
name|ArtifactCleanupFeature
operator|.
name|class
operator|.
name|toString
argument_list|()
block|,
name|IndexCreationFeature
operator|.
name|class
operator|.
name|toString
argument_list|()
block|,
name|StagingRepositoryFeature
operator|.
name|class
operator|.
name|toString
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
literal|true
argument_list|)
decl_stmt|;
specifier|public
name|BasicManagedRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|RepositoryStorage
name|repositoryStorage
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
name|repositoryStorage
argument_list|)
expr_stmt|;
name|initFeatures
argument_list|()
expr_stmt|;
block|}
specifier|public
name|BasicManagedRepository
parameter_list|(
name|Locale
name|primaryLocale
parameter_list|,
name|RepositoryType
name|type
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|RepositoryStorage
name|repositoryStorage
parameter_list|)
block|{
name|super
argument_list|(
name|primaryLocale
argument_list|,
name|type
argument_list|,
name|id
argument_list|,
name|name
argument_list|,
name|repositoryStorage
argument_list|)
expr_stmt|;
name|initFeatures
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initFeatures
parameter_list|()
block|{
name|IndexCreationFeature
name|indexCreationFeature
init|=
operator|new
name|IndexCreationFeature
argument_list|(
name|this
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|addFeature
argument_list|(
name|artifactCleanupFeature
argument_list|)
expr_stmt|;
name|addFeature
argument_list|(
name|indexCreationFeature
argument_list|)
expr_stmt|;
name|addFeature
argument_list|(
name|stagingRepositoryFeature
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasIndex
parameter_list|( )
block|{
return|return
literal|true
return|;
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
name|Override
specifier|public
name|RepositoryRequestInfo
name|getRequestInfo
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Creates a filesystem based repository instance. The path is built by basePath/repository-id      *      * @param id The repository id      * @param name The name of the repository      * @param repositoryPath The path to the repository      * @return The repository instance      * @throws IOException      */
specifier|public
specifier|static
name|BasicManagedRepository
name|newFilesystemInstance
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|Path
name|repositoryPath
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
name|repositoryPath
argument_list|,
name|lockManager
argument_list|)
decl_stmt|;
return|return
operator|new
name|BasicManagedRepository
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

