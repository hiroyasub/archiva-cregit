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
name|consumers
operator|.
name|database
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
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
name|RepositoryProblemDAO
import|;
end_import

begin_comment
comment|/**  * Test for DatabaseCleanupRemoveArtifactConsumerTest  *   */
end_comment

begin_class
specifier|public
class|class
name|DatabaseCleanupRemoveArtifactConsumerTest
extends|extends
name|AbstractDatabaseCleanupTest
block|{
specifier|private
name|MockControl
name|artifactDAOControl
decl_stmt|;
specifier|private
name|ArtifactDAO
name|artifactDAOMock
decl_stmt|;
specifier|private
name|MockControl
name|repositoryProblemDAOControl
decl_stmt|;
specifier|private
name|RepositoryProblemDAO
name|repositoryProblemDAOMock
decl_stmt|;
specifier|private
name|DatabaseCleanupRemoveArtifactConsumer
name|dbCleanupRemoveArtifactConsumer
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
name|dbCleanupRemoveArtifactConsumer
operator|=
operator|new
name|DatabaseCleanupRemoveArtifactConsumer
argument_list|()
expr_stmt|;
name|artifactDAOControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArtifactDAO
operator|.
name|class
argument_list|)
expr_stmt|;
name|artifactDAOMock
operator|=
operator|(
name|ArtifactDAO
operator|)
name|artifactDAOControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|repositoryProblemDAOControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|RepositoryProblemDAO
operator|.
name|class
argument_list|)
expr_stmt|;
name|repositoryProblemDAOMock
operator|=
operator|(
name|RepositoryProblemDAO
operator|)
name|repositoryProblemDAOControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|dbCleanupRemoveArtifactConsumer
operator|.
name|setArtifactDAO
argument_list|(
name|artifactDAOMock
argument_list|)
expr_stmt|;
name|dbCleanupRemoveArtifactConsumer
operator|.
name|setRepositoryProblemDAO
argument_list|(
name|repositoryProblemDAOMock
argument_list|)
expr_stmt|;
name|dbCleanupRemoveArtifactConsumer
operator|.
name|setRepositoryFactory
argument_list|(
name|repositoryFactory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIfArtifactWasNotDeleted
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
init|=
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
literal|"do-not-cleanup-artifact-test"
argument_list|,
name|TEST_VERSION
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|artifactDAOControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|repositoryProblemDAOControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|dbCleanupRemoveArtifactConsumer
operator|.
name|processArchivaArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifactDAOControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|repositoryProblemDAOControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testIfArtifactWasDeleted
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
init|=
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|artifactDAOMock
operator|.
name|deleteArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifactDAOControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|dbCleanupRemoveArtifactConsumer
operator|.
name|processArchivaArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifactDAOControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

