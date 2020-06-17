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
name|UnsupportedFeatureException
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
name|AbstractRemoteRepository
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
name|RemoteIndexFeature
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

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Maven2 remote repository implementation  */
end_comment

begin_class
specifier|public
class|class
name|MavenRemoteRepository
extends|extends
name|AbstractRemoteRepository
implements|implements
name|RemoteRepository
block|{
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MavenRemoteRepository
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
specifier|private
name|RemoteIndexFeature
name|remoteIndexFeature
init|=
operator|new
name|RemoteIndexFeature
argument_list|()
decl_stmt|;
specifier|final
specifier|private
name|IndexCreationFeature
name|indexCreationFeature
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
name|RemoteIndexFeature
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
name|MavenRemoteRepository
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
block|}
specifier|public
name|MavenRemoteRepository
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
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasIndex
parameter_list|( )
block|{
return|return
name|remoteIndexFeature
operator|.
name|hasIndex
argument_list|()
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
name|RemoteIndexFeature
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
name|remoteIndexFeature
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
name|RemoteIndexFeature
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
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|", remoteIndexFeature="
operator|+
name|remoteIndexFeature
operator|.
name|toString
argument_list|()
operator|+
literal|", indexCreationFeature="
operator|+
name|indexCreationFeature
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|MavenRemoteRepository
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
name|MavenRemoteRepository
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

