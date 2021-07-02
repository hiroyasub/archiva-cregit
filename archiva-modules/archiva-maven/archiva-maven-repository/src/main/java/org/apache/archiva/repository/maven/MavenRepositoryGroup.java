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
name|EditableRepositoryGroup
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
name|ReleaseScheme
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
name|RepositoryCapabilities
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
name|RepositoryType
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
name|StandardCapabilities
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
name|base
operator|.
name|group
operator|.
name|AbstractRepositoryGroup
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
name|storage
operator|.
name|fs
operator|.
name|FilesystemStorage
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

begin_class
specifier|public
class|class
name|MavenRepositoryGroup
extends|extends
name|AbstractRepositoryGroup
implements|implements
name|EditableRepositoryGroup
block|{
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
name|MavenManagedRepository
operator|.
name|DEFAULT_LAYOUT
block|,
name|MavenManagedRepository
operator|.
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
name|IndexCreationFeature
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MavenRepositoryGroup
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|IndexCreationFeature
name|indexCreationFeature
decl_stmt|;
specifier|public
name|MavenRepositoryGroup
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
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|MavenRepositoryGroup
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
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Path
name|getRepositoryPath
parameter_list|()
block|{
return|return
name|getStorage
argument_list|()
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
return|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|setCapabilities
argument_list|(
name|CAPABILITIES
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
name|addFeature
argument_list|(
name|this
operator|.
name|indexCreationFeature
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|MavenRepositoryGroup
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
name|MavenRepositoryGroup
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

