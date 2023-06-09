begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|repository
operator|.
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|repository
operator|.
name|metadata
operator|.
name|storage
operator|.
name|ArtifactMappingProvider
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
name|repository
operator|.
name|ManagedRepositoryContent
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
name|RepositoryContent
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
name|Artifact
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
name|ItemSelector
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
name|LayoutException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|javax
operator|.
name|inject
operator|.
name|Named
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
comment|/**  * RemoteDefaultRepositoryContentTest  */
end_comment

begin_class
specifier|public
class|class
name|RemoteDefaultRepositoryContentTest
extends|extends
name|AbstractRepositoryContentTest
block|{
annotation|@
name|Inject
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|ArtifactMappingProvider
argument_list|>
name|artifactMappingProviders
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"repositoryPathTranslator#maven2"
argument_list|)
name|RepositoryPathTranslator
name|pathTranslator
decl_stmt|;
specifier|private
name|RemoteDefaultRepositoryContent
name|repoContent
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepository
name|repository
init|=
name|createRemoteRepository
argument_list|(
literal|"testRemoteRepo"
argument_list|,
literal|"Unit Test Remote Repo"
argument_list|,
literal|"http://repo1.maven.org/maven2/"
argument_list|)
decl_stmt|;
name|repoContent
operator|=
operator|new
name|RemoteDefaultRepositoryContent
argument_list|()
expr_stmt|;
name|repoContent
operator|.
name|setArtifactMappingProviders
argument_list|(
name|artifactMappingProviders
argument_list|)
expr_stmt|;
name|repoContent
operator|.
name|setPathTranslator
argument_list|(
name|pathTranslator
argument_list|)
expr_stmt|;
comment|//repoContent = (RemoteRepositoryContent) lookup( RemoteRepositoryContent.class, "default" );
name|repoContent
operator|.
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Artifact
name|createArtifact
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
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|toPath
parameter_list|(
name|Artifact
name|reference
parameter_list|)
throws|throws
name|LayoutException
block|{
name|ItemSelector
name|selector
init|=
name|toItemSelector
argument_list|(
name|reference
operator|.
name|getAsset
argument_list|( )
operator|.
name|getPath
argument_list|( )
argument_list|)
decl_stmt|;
return|return
name|repoContent
operator|.
name|toPath
argument_list|(
name|selector
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ItemSelector
name|toItemSelector
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
name|repoContent
operator|.
name|toItemSelector
argument_list|(
name|path
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ManagedRepositoryContent
name|getManaged
parameter_list|( )
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|RepositoryContent
name|getContent
parameter_list|( )
block|{
return|return
name|repoContent
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|toPath
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
block|{
return|return
name|repoContent
operator|.
name|toPath
argument_list|(
name|selector
argument_list|)
return|;
block|}
block|}
end_class

end_unit

