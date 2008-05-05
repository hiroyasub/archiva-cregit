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
name|database
operator|.
name|constraints
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
name|AbstractArchivaDatabaseTestCase
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
name|ArchivaDAO
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
name|model
operator|.
name|ArchivaArtifact
import|;
end_import

begin_comment
comment|/**  * ArtifactsByRepositoryConstraintTest  *   * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactsByRepositoryConstraintTest
extends|extends
name|AbstractArchivaDatabaseTestCase
block|{
specifier|private
name|ArtifactDAO
name|artifactDao
decl_stmt|;
specifier|public
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
name|ArchivaDAO
name|dao
init|=
operator|(
name|ArchivaDAO
operator|)
name|lookup
argument_list|(
name|ArchivaDAO
operator|.
name|ROLE
argument_list|,
literal|"jdo"
argument_list|)
decl_stmt|;
name|artifactDao
operator|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
expr_stmt|;
block|}
specifier|private
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
parameter_list|)
block|{
name|ArchivaArtifact
name|artifact
init|=
name|artifactDao
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setLastModified
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setRepositoryId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
specifier|public
name|void
name|testQueryAllArtifactsInRepo
parameter_list|()
throws|throws
name|Exception
block|{
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
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"1.0"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"1.0.1"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-two"
argument_list|,
literal|"1.0.2"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setRepositoryId
argument_list|(
literal|"different-repo"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
literal|"Artifacts By Repository"
argument_list|,
literal|3
argument_list|,
operator|new
name|ArtifactsByRepositoryConstraint
argument_list|(
literal|"test-repo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testQueryArtifactsInRepoWithWhenGathered
parameter_list|()
throws|throws
name|Exception
block|{
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
name|ArchivaArtifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"1.0"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"1.0.1"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"1.0.2"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-one"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setRepositoryId
argument_list|(
literal|"different-repo"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-two"
argument_list|,
literal|"1.1-SNAPSHOT"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-three"
argument_list|,
literal|"2.0-beta-1"
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
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
literal|"Artifacts By Repository and When Gathered"
argument_list|,
literal|5
argument_list|,
operator|new
name|ArtifactsByRepositoryConstraint
argument_list|(
literal|"test-repo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertConstraint
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|count
parameter_list|,
name|ArtifactsByRepositoryConstraint
name|constraint
parameter_list|)
throws|throws
name|Exception
block|{
name|List
name|results
init|=
name|artifactDao
operator|.
name|queryArtifacts
argument_list|(
name|constraint
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|msg
operator|+
literal|": Not Null"
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|msg
operator|+
literal|": Results.size"
argument_list|,
name|count
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

