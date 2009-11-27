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
name|action
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Action
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
name|ProjectModelDAO
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
name|constraints
operator|.
name|ArtifactsRelatedConstraint
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
name|model
operator|.
name|ArchivaArtifactModel
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
name|ArchivaProjectModel
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
name|CiManagement
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
name|IssueManagement
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
name|License
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
name|Organization
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
name|Scm
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
name|VersionedReference
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
name|security
operator|.
name|UserRepositories
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
name|security
operator|.
name|UserRepositoriesStub
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
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|repositories
operator|.
name|ArchivaDAOStub
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_class
specifier|public
class|class
name|ShowArtifactActionTest
extends|extends
name|AbstractActionTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ACTION_HINT
init|=
literal|"showArtifactAction"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VERSION
init|=
literal|"version"
decl_stmt|;
specifier|private
name|ShowArtifactAction
name|action
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SNAPSHOT_VERSION
init|=
literal|"1.0-SNAPSHOT"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_TS_SNAPSHOT_VERSION
init|=
literal|"1.0-20091120.111111-1"
decl_stmt|;
specifier|private
name|ArchivaDAOStub
name|archivaDao
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ALL_TEST_SNAPSHOT_VERSIONS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_TS_SNAPSHOT_VERSION
argument_list|,
literal|"1.0-20091120.222222-2"
argument_list|,
literal|"1.0-20091123.333333-3"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OTHER_TEST_REPO
init|=
literal|"first-repo"
decl_stmt|;
specifier|public
name|void
name|testInstantiation
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|action
operator|==
name|lookup
argument_list|(
name|Action
operator|.
name|class
argument_list|,
name|ACTION_HINT
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactUniqueRelease
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|MockControl
name|artifactDaoMockControl
init|=
name|createArtifactDaoMock
argument_list|(
name|artifacts
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|MockControl
name|projectDaoMockControl
init|=
name|createProjectDaoMock
argument_list|(
name|createProjectModel
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|setActionParameters
argument_list|()
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
name|assertActionSuccess
argument_list|(
name|action
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|artifactDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|projectDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertActionParameters
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|action
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|assertDefaultModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_REPO
argument_list|,
name|action
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependees
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getMailingLists
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactUniqueSnapshot
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_SNAPSHOT_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|MockControl
name|artifactDaoMockControl
init|=
name|createArtifactDaoMock
argument_list|(
name|artifacts
argument_list|,
name|TEST_SNAPSHOT_VERSION
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|MockControl
name|projectDaoMockControl
init|=
name|createProjectDaoMock
argument_list|(
name|createProjectModel
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_SNAPSHOT_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|archivaDao
operator|.
name|setVersions
argument_list|(
name|ALL_TEST_SNAPSHOT_VERSIONS
argument_list|)
expr_stmt|;
name|action
operator|.
name|setGroupId
argument_list|(
name|TEST_GROUP_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setArtifactId
argument_list|(
name|TEST_ARTIFACT_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setVersion
argument_list|(
name|TEST_SNAPSHOT_VERSION
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
name|assertActionSuccess
argument_list|(
name|action
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|artifactDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|projectDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|action
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ARTIFACT_ID
argument_list|,
name|action
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_SNAPSHOT_VERSION
argument_list|,
name|action
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|action
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|assertDefaultModel
argument_list|(
name|model
argument_list|,
name|TEST_SNAPSHOT_VERSION
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_REPO
argument_list|,
name|action
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ALL_TEST_SNAPSHOT_VERSIONS
argument_list|,
name|action
operator|.
name|getSnapshotVersions
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependees
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getMailingLists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactUniqueSnapshotTimestamped
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_TS_SNAPSHOT_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|MockControl
name|artifactDaoMockControl
init|=
name|createArtifactDaoMock
argument_list|(
name|artifacts
argument_list|,
name|TEST_TS_SNAPSHOT_VERSION
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|MockControl
name|projectDaoMockControl
init|=
name|createProjectDaoMock
argument_list|(
name|createProjectModel
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_TS_SNAPSHOT_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|archivaDao
operator|.
name|setVersions
argument_list|(
name|ALL_TEST_SNAPSHOT_VERSIONS
argument_list|)
expr_stmt|;
name|action
operator|.
name|setGroupId
argument_list|(
name|TEST_GROUP_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setArtifactId
argument_list|(
name|TEST_ARTIFACT_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setVersion
argument_list|(
name|TEST_TS_SNAPSHOT_VERSION
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
name|assertActionSuccess
argument_list|(
name|action
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|artifactDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|projectDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|action
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ARTIFACT_ID
argument_list|,
name|action
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_TS_SNAPSHOT_VERSION
argument_list|,
name|action
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|action
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|assertDefaultModel
argument_list|(
name|model
argument_list|,
name|TEST_TS_SNAPSHOT_VERSION
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_REPO
argument_list|,
name|action
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ALL_TEST_SNAPSHOT_VERSIONS
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|ALL_TEST_SNAPSHOT_VERSIONS
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|,
name|action
operator|.
name|getSnapshotVersions
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependees
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getMailingLists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetMissingProject
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|MockControl
name|artifactDaoMockControl
init|=
name|createArtifactDaoMock
argument_list|(
name|Collections
operator|.
expr|<
name|ArchivaArtifact
operator|>
name|emptyList
argument_list|()
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|setActionParameters
argument_list|()
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
name|assertError
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|artifactDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertActionParameters
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertNoOutputFields
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactNoObservableRepos
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|setObservableRepos
argument_list|(
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|setActionParameters
argument_list|()
expr_stmt|;
try|try
block|{
name|action
operator|.
name|artifact
argument_list|()
expr_stmt|;
comment|// Actually, it'd be better to have an error:
comment|//            assertError( result );
comment|//            assertActionParameters( action );
comment|//            assertNoOutputFields();
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testGetArtifactNotInObservableRepos
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|,
name|OTHER_TEST_REPO
argument_list|)
argument_list|)
decl_stmt|;
name|MockControl
name|artifactDaoMockControl
init|=
name|createArtifactDaoMock
argument_list|(
name|artifacts
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|setActionParameters
argument_list|()
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
name|assertError
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|artifactDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertActionParameters
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|assertNoOutputFields
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactOnlySeenInSecondObservableRepo
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|setObservableRepos
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|OTHER_TEST_REPO
argument_list|,
name|TEST_REPO
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|MockControl
name|artifactDaoMockControl
init|=
name|createArtifactDaoMock
argument_list|(
name|artifacts
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|MockControl
name|projectDaoMockControl
init|=
name|createProjectDaoMock
argument_list|(
name|createProjectModel
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|setActionParameters
argument_list|()
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
name|assertActionSuccess
argument_list|(
name|action
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|artifactDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|projectDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertActionParameters
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|action
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|assertDefaultModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_REPO
argument_list|,
name|action
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependees
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getMailingLists
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactSeenInBothObservableRepo
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|setObservableRepos
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|OTHER_TEST_REPO
argument_list|,
name|TEST_REPO
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|,
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|,
name|OTHER_TEST_REPO
argument_list|)
argument_list|)
decl_stmt|;
name|MockControl
name|artifactDaoMockControl
init|=
name|createArtifactDaoMock
argument_list|(
name|artifacts
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|MockControl
name|projectDaoMockControl
init|=
name|createProjectDaoMock
argument_list|(
name|createProjectModel
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|setActionParameters
argument_list|()
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
name|assertActionSuccess
argument_list|(
name|action
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|artifactDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|projectDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertActionParameters
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|action
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|assertDefaultModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_REPO
argument_list|,
name|action
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependees
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getMailingLists
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactCanOnlyObserveInOneOfTwoRepos
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|setObservableRepos
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_REPO
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|,
name|OTHER_TEST_REPO
argument_list|)
argument_list|,
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|MockControl
name|artifactDaoMockControl
init|=
name|createArtifactDaoMock
argument_list|(
name|artifacts
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|MockControl
name|projectDaoMockControl
init|=
name|createProjectDaoMock
argument_list|(
name|createProjectModel
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|setActionParameters
argument_list|()
expr_stmt|;
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
name|assertActionSuccess
argument_list|(
name|action
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|artifactDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|projectDaoMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertActionParameters
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|action
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|assertDefaultModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_REPO
argument_list|,
name|action
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependees
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getMailingLists
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertNoOutputFields
parameter_list|()
block|{
name|assertNull
argument_list|(
name|action
operator|.
name|getModel
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependees
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getMailingLists
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertError
parameter_list|(
name|String
name|result
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|Action
operator|.
name|ERROR
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|action
operator|.
name|getActionErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertDefaultModel
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
block|{
name|assertDefaultModel
argument_list|(
name|model
argument_list|,
name|TEST_VERSION
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setActionParameters
parameter_list|()
block|{
name|action
operator|.
name|setGroupId
argument_list|(
name|TEST_GROUP_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setArtifactId
argument_list|(
name|TEST_ARTIFACT_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setVersion
argument_list|(
name|TEST_VERSION
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertActionParameters
parameter_list|(
name|ShowArtifactAction
name|action
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|action
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ARTIFACT_ID
argument_list|,
name|action
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_VERSION
argument_list|,
name|action
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertActionSuccess
parameter_list|(
name|ShowArtifactAction
name|action
parameter_list|,
name|String
name|result
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|action
operator|.
name|getActionErrors
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|action
operator|.
name|getActionMessages
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ArchivaProjectModel
name|createProjectModel
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
name|ArchivaProjectModel
name|model
init|=
operator|new
name|ArchivaProjectModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|model
operator|.
name|setPackaging
argument_list|(
name|TEST_PACKAGING
argument_list|)
expr_stmt|;
name|model
operator|.
name|setUrl
argument_list|(
name|TEST_URL
argument_list|)
expr_stmt|;
name|model
operator|.
name|setName
argument_list|(
name|TEST_NAME
argument_list|)
expr_stmt|;
name|model
operator|.
name|setDescription
argument_list|(
name|TEST_DESCRIPTION
argument_list|)
expr_stmt|;
name|VersionedReference
name|parent
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|parent
operator|.
name|setGroupId
argument_list|(
name|TEST_PARENT_GROUP_ID
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setArtifactId
argument_list|(
name|TEST_PARENT_ARTIFACT_ID
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setVersion
argument_list|(
name|TEST_PARENT_VERSION
argument_list|)
expr_stmt|;
name|model
operator|.
name|setParentProject
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|CiManagement
name|ci
init|=
operator|new
name|CiManagement
argument_list|()
decl_stmt|;
name|ci
operator|.
name|setSystem
argument_list|(
name|TEST_CI_SYSTEM
argument_list|)
expr_stmt|;
name|ci
operator|.
name|setUrl
argument_list|(
name|TEST_CI_URL
argument_list|)
expr_stmt|;
name|model
operator|.
name|setCiManagement
argument_list|(
name|ci
argument_list|)
expr_stmt|;
name|IssueManagement
name|issue
init|=
operator|new
name|IssueManagement
argument_list|()
decl_stmt|;
name|issue
operator|.
name|setSystem
argument_list|(
name|TEST_ISSUE_SYSTEM
argument_list|)
expr_stmt|;
name|issue
operator|.
name|setUrl
argument_list|(
name|TEST_ISSUE_URL
argument_list|)
expr_stmt|;
name|model
operator|.
name|setIssueManagement
argument_list|(
name|issue
argument_list|)
expr_stmt|;
name|Organization
name|org
init|=
operator|new
name|Organization
argument_list|()
decl_stmt|;
name|org
operator|.
name|setName
argument_list|(
name|TEST_ORGANIZATION_NAME
argument_list|)
expr_stmt|;
name|org
operator|.
name|setUrl
argument_list|(
name|TEST_ORGANIZATION_URL
argument_list|)
expr_stmt|;
name|model
operator|.
name|setOrganization
argument_list|(
name|org
argument_list|)
expr_stmt|;
name|License
name|l
init|=
operator|new
name|License
argument_list|()
decl_stmt|;
name|l
operator|.
name|setName
argument_list|(
name|TEST_LICENSE_NAME
argument_list|)
expr_stmt|;
name|l
operator|.
name|setUrl
argument_list|(
name|TEST_LICENSE_URL
argument_list|)
expr_stmt|;
name|model
operator|.
name|addLicense
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|l
operator|=
operator|new
name|License
argument_list|()
expr_stmt|;
name|l
operator|.
name|setName
argument_list|(
name|TEST_LICENSE_NAME_2
argument_list|)
expr_stmt|;
name|l
operator|.
name|setUrl
argument_list|(
name|TEST_LICENSE_URL_2
argument_list|)
expr_stmt|;
name|model
operator|.
name|addLicense
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|Scm
name|scm
init|=
operator|new
name|Scm
argument_list|()
decl_stmt|;
name|scm
operator|.
name|setConnection
argument_list|(
name|TEST_SCM_CONNECTION
argument_list|)
expr_stmt|;
name|scm
operator|.
name|setDeveloperConnection
argument_list|(
name|TEST_SCM_DEV_CONNECTION
argument_list|)
expr_stmt|;
name|scm
operator|.
name|setUrl
argument_list|(
name|TEST_SCM_URL
argument_list|)
expr_stmt|;
name|model
operator|.
name|setScm
argument_list|(
name|scm
argument_list|)
expr_stmt|;
return|return
name|model
return|;
block|}
specifier|private
name|MockControl
name|createArtifactDaoMock
parameter_list|(
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
parameter_list|,
name|int
name|count
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
return|return
name|createArtifactDaoMock
argument_list|(
name|artifacts
argument_list|,
name|TEST_VERSION
argument_list|,
name|count
argument_list|)
return|;
block|}
specifier|private
name|MockControl
name|createArtifactDaoMock
parameter_list|(
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
parameter_list|,
name|String
name|version
parameter_list|,
name|int
name|count
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
comment|// testing deeper than normal with the mocks as we intend to replace RepositoryBrowsing, not just the database
comment|// underneath it - those sections will be adjusted with a mock content repository later
name|MockControl
name|control
init|=
name|MockControl
operator|.
name|createNiceControl
argument_list|(
name|ArtifactDAO
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArtifactDAO
name|dao
init|=
operator|(
name|ArtifactDAO
operator|)
name|control
operator|.
name|getMock
argument_list|()
decl_stmt|;
name|archivaDao
operator|.
name|setArtifactDao
argument_list|(
name|dao
argument_list|)
expr_stmt|;
name|ArtifactsRelatedConstraint
name|c
init|=
operator|new
name|ArtifactsRelatedConstraint
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|dao
operator|.
name|queryArtifacts
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|control
operator|.
name|setReturnValue
argument_list|(
name|artifacts
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
return|return
name|control
return|;
block|}
specifier|private
name|MockControl
name|createProjectDaoMock
parameter_list|(
name|ArchivaProjectModel
name|project
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|MockControl
name|control
init|=
name|MockControl
operator|.
name|createNiceControl
argument_list|(
name|ProjectModelDAO
operator|.
name|class
argument_list|)
decl_stmt|;
name|ProjectModelDAO
name|dao
init|=
operator|(
name|ProjectModelDAO
operator|)
name|control
operator|.
name|getMock
argument_list|()
decl_stmt|;
name|archivaDao
operator|.
name|setProjectDao
argument_list|(
name|dao
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|dao
operator|.
name|getProjectModel
argument_list|(
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|,
name|project
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
return|return
name|control
return|;
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
return|return
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|TEST_REPO
argument_list|)
return|;
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
parameter_list|,
name|String
name|repoId
parameter_list|)
block|{
name|ArchivaArtifactModel
name|model
init|=
operator|new
name|ArchivaArtifactModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|model
operator|.
name|setRepositoryId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
return|return
operator|new
name|ArchivaArtifact
argument_list|(
name|model
argument_list|)
return|;
block|}
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
name|action
operator|=
operator|(
name|ShowArtifactAction
operator|)
name|lookup
argument_list|(
name|Action
operator|.
name|class
argument_list|,
name|ACTION_HINT
argument_list|)
expr_stmt|;
name|archivaDao
operator|=
operator|(
name|ArchivaDAOStub
operator|)
name|lookup
argument_list|(
name|ArchivaDAO
operator|.
name|class
argument_list|,
literal|"jdo"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

