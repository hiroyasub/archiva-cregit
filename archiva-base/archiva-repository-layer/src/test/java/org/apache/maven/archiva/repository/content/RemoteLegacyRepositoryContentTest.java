begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
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
name|archiva
operator|.
name|model
operator|.
name|ArtifactReference
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
name|repository
operator|.
name|RemoteRepositoryContent
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
name|repository
operator|.
name|layout
operator|.
name|LayoutException
import|;
end_import

begin_comment
comment|/**  * RemoteLegacyRepositoryContentTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RemoteLegacyRepositoryContentTest
extends|extends
name|AbstractLegacyRepositoryContentTestCase
block|{
specifier|private
name|RemoteRepositoryContent
name|repoContent
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|RemoteRepositoryConfiguration
name|repository
init|=
name|createRemoteRepository
argument_list|(
literal|"testRemoteLegacyRepo"
argument_list|,
literal|"Unit Test Remote Legacy Repo"
argument_list|,
literal|"http://repo1.maven.org/maven/"
argument_list|)
decl_stmt|;
name|repository
operator|.
name|setLayout
argument_list|(
literal|"legacy"
argument_list|)
expr_stmt|;
name|repoContent
operator|=
operator|(
name|RemoteRepositoryContent
operator|)
name|lookup
argument_list|(
name|RemoteRepositoryContent
operator|.
name|class
argument_list|,
literal|"legacy"
argument_list|)
expr_stmt|;
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
name|ArtifactReference
name|toArtifactReference
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
name|toArtifactReference
argument_list|(
name|path
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
return|return
name|repoContent
operator|.
name|toPath
argument_list|(
name|reference
argument_list|)
return|;
block|}
block|}
end_class

end_unit

