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
name|web
operator|.
name|rss
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|ArtifactDAO
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
name|database
operator|.
name|Constraint
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
name|database
operator|.
name|ObjectNotFoundException
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
name|ArchivaArtifact
import|;
end_import

begin_comment
comment|/**  * Stub used for RssFeedServlet unit test.  *   * @version  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactDAOStub
implements|implements
name|ArtifactDAO
block|{
specifier|public
name|ArchivaArtifact
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
parameter_list|,
name|String
name|repositoryId
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|deleteArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|ArchivaArtifact
name|getArtifact
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
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|List
name|queryArtifacts
parameter_list|(
name|Constraint
name|constraint
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|ArchivaArtifact
argument_list|>
argument_list|()
decl_stmt|;
name|Date
name|whenGathered
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|whenGathered
operator|.
name|setTime
argument_list|(
literal|123456789
argument_list|)
expr_stmt|;
name|ArchivaArtifact
name|artifact
init|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"1.0"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"1.1"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"2.0"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-two"
argument_list|,
literal|"1.0.1"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-two"
argument_list|,
literal|"1.0.2"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-two"
argument_list|,
literal|"1.0.3-SNAPSHOT"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-three"
argument_list|,
literal|"2.0-SNAPSHOT"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-four"
argument_list|,
literal|"1.1-beta-2"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"test-repo"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
return|return
name|artifacts
return|;
block|}
specifier|public
name|ArchivaArtifact
name|saveArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

