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
name|mock
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
name|repository
operator|.
name|*
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

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryContentProvider#mock"
argument_list|)
specifier|public
class|class
name|RepositoryContentProviderMock
implements|implements
name|RepositoryContentProvider
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|RepositoryType
argument_list|>
name|REPOSITORY_TYPES
init|=
operator|new
name|HashSet
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
name|REPOSITORY_TYPES
operator|.
name|add
argument_list|(
name|RepositoryType
operator|.
name|NPM
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsLayout
parameter_list|(
name|String
name|layout
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|RepositoryType
argument_list|>
name|getSupportedRepositoryTypes
parameter_list|()
block|{
return|return
name|REPOSITORY_TYPES
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supports
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteRepositoryContent
name|createRemoteContent
parameter_list|(
name|RemoteRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryException
block|{
return|return
operator|new
name|RemoteRepositoryContentMock
argument_list|(
name|repository
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepositoryContent
name|createManagedContent
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryException
block|{
return|return
operator|new
name|ManagedRepositoryContentMock
argument_list|(
name|repository
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|RepositoryContent
parameter_list|,
name|V
extends|extends
name|Repository
parameter_list|>
name|T
name|createContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|V
name|repository
parameter_list|)
throws|throws
name|RepositoryException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
