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
name|archiva
operator|.
name|repository
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
name|RepositoryProvider
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Provider for the maven2 repository implementations  */
end_comment

begin_class
specifier|public
class|class
name|MavenRepositoryProvider
implements|implements
name|RepositoryProvider
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
name|MavenRepositoryProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Set
argument_list|<
name|RepositoryType
argument_list|>
name|TYPES
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(  )
decl_stmt|;
static|static
block|{
name|TYPES
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
name|Override
specifier|public
name|Set
argument_list|<
name|RepositoryType
argument_list|>
name|provides
parameter_list|( )
block|{
return|return
name|TYPES
return|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|createManagedInstance
parameter_list|(
name|ManagedRepositoryConfiguration
name|cfg
parameter_list|)
block|{
name|MavenManagedRepository
name|repo
init|=
operator|new
name|MavenManagedRepository
argument_list|(
name|cfg
operator|.
name|getId
argument_list|()
argument_list|,
name|cfg
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|cfg
operator|.
name|getLocation
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|repo
operator|.
name|setLocation
argument_list|(
operator|new
name|URI
argument_list|(
name|cfg
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|repo
operator|.
name|setLocation
argument_list|(
operator|new
name|URI
argument_list|(
literal|"file://"
operator|+
name|cfg
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not set repository uri "
operator|+
name|cfg
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cfg
operator|.
name|getRefreshCronExpression
argument_list|()
name|StagingRepositoryFeature
name|feature
operator|=
name|repo
operator|.
name|getFeature
argument_list|(
name|StagingRepositoryFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteRepository
name|createRemoteInstance
parameter_list|(
name|RemoteRepositoryConfiguration
name|configuration
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

