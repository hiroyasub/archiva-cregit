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
name|archiva
operator|.
name|metadata
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
name|archiva
operator|.
name|metadata
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
name|archiva
operator|.
name|metadata
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
name|archiva
operator|.
name|metadata
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
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|ProjectBuildMetadata
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
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|memory
operator|.
name|MemoryMetadataRepository
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
name|maven2
operator|.
name|MavenProjectFacet
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
name|maven2
operator|.
name|MavenProjectParent
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
name|codehaus
operator|.
name|plexus
operator|.
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_class
specifier|public
class|class
name|ShowArtifactActionTest
extends|extends
name|PlexusInSpringTestCase
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
name|TEST_GROUP_ID
init|=
literal|"groupId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ARTIFACT_ID
init|=
literal|"artifactId"
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
specifier|static
specifier|final
name|String
name|TEST_PACKAGING
init|=
literal|"packaging"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ISSUE_URL
init|=
literal|"http://jira.codehaus.org/browse/MRM"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ISSUE_SYSTEM
init|=
literal|"jira"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CI_SYSTEM
init|=
literal|"continuum"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CI_URL
init|=
literal|"http://vmbuild.apache.org/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_URL
init|=
literal|"url"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_NAME
init|=
literal|"name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_DESCRIPTION
init|=
literal|"description"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_PARENT_GROUP_ID
init|=
literal|"parentGroupId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_PARENT_ARTIFACT_ID
init|=
literal|"parentArtifactId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_PARENT_VERSION
init|=
literal|"parentVersion"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ORGANIZATION_NAME
init|=
literal|"organizationName"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ORGANIZATION_URL
init|=
literal|"organizationUrl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_LICENSE_URL
init|=
literal|"licenseUrl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_LICENSE_NAME
init|=
literal|"licenseName"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_LICENSE_URL_2
init|=
literal|"licenseUrl_2"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_LICENSE_NAME_2
init|=
literal|"licenseName_2"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO
init|=
literal|"test-repo"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SCM_CONNECTION
init|=
literal|"scmConnection"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SCM_DEV_CONNECTION
init|=
literal|"scmDevConnection"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_SCM_URL
init|=
literal|"scmUrl"
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
specifier|private
name|ShowArtifactAction
name|action
decl_stmt|;
specifier|private
name|MemoryMetadataRepository
name|metadataRepository
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
block|{
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_VERSION
argument_list|)
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactUniqueSnapshot
parameter_list|()
block|{
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_SNAPSHOT_VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|setArtifactVersions
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_SNAPSHOT_VERSION
argument_list|,
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
block|{
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_TS_SNAPSHOT_VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|setArtifactVersions
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_TS_SNAPSHOT_VERSION
argument_list|,
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
block|{
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
name|String
name|result
init|=
name|action
operator|.
name|artifact
argument_list|()
decl_stmt|;
comment|// Actually, it'd be better to have an error:
name|assertError
argument_list|(
name|result
argument_list|)
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
name|testGetArtifactNotInObservableRepos
parameter_list|()
block|{
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|OTHER_TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_VERSION
argument_list|)
argument_list|)
expr_stmt|;
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
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_VERSION
argument_list|)
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactSeenInBothObservableRepo
parameter_list|()
block|{
name|setObservableRepos
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_REPO
argument_list|,
name|OTHER_TEST_REPO
argument_list|)
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|OTHER_TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_VERSION
argument_list|)
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetArtifactCanOnlyObserveInOneOfTwoRepos
parameter_list|()
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
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|OTHER_TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|setProjectBuild
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|createProjectModel
argument_list|(
name|TEST_VERSION
argument_list|)
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
operator|.
name|isEmpty
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
name|assertTrue
argument_list|(
name|action
operator|.
name|getSnapshotVersions
argument_list|()
operator|.
name|isEmpty
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
name|setObservableRepos
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repoIds
parameter_list|)
block|{
name|UserRepositoriesStub
name|repos
init|=
operator|(
name|UserRepositoriesStub
operator|)
name|lookup
argument_list|(
name|UserRepositories
operator|.
name|class
argument_list|)
decl_stmt|;
name|repos
operator|.
name|setObservableRepositoryIds
argument_list|(
name|repoIds
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
name|assertDefaultModel
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ARTIFACT_ID
argument_list|,
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|version
argument_list|,
name|model
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_URL
argument_list|,
name|model
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_NAME
argument_list|,
name|model
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_DESCRIPTION
argument_list|,
name|model
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ORGANIZATION_NAME
argument_list|,
name|model
operator|.
name|getOrganization
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ORGANIZATION_URL
argument_list|,
name|model
operator|.
name|getOrganization
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|model
operator|.
name|getLicenses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
name|l
init|=
name|model
operator|.
name|getLicenses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TEST_LICENSE_NAME
argument_list|,
name|l
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_LICENSE_URL
argument_list|,
name|l
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|l
operator|=
name|model
operator|.
name|getLicenses
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_LICENSE_NAME_2
argument_list|,
name|l
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_LICENSE_URL_2
argument_list|,
name|l
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ISSUE_SYSTEM
argument_list|,
name|model
operator|.
name|getIssueManagement
argument_list|()
operator|.
name|getSystem
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ISSUE_URL
argument_list|,
name|model
operator|.
name|getIssueManagement
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_CI_SYSTEM
argument_list|,
name|model
operator|.
name|getCiManagement
argument_list|()
operator|.
name|getSystem
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_CI_URL
argument_list|,
name|model
operator|.
name|getCiManagement
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_SCM_CONNECTION
argument_list|,
name|model
operator|.
name|getScm
argument_list|()
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_SCM_DEV_CONNECTION
argument_list|,
name|model
operator|.
name|getScm
argument_list|()
operator|.
name|getDeveloperConnection
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_SCM_URL
argument_list|,
name|model
operator|.
name|getScm
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_PACKAGING
argument_list|,
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_PARENT_GROUP_ID
argument_list|,
name|model
operator|.
name|getParentProject
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_PARENT_ARTIFACT_ID
argument_list|,
name|model
operator|.
name|getParentProject
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_PARENT_VERSION
argument_list|,
name|model
operator|.
name|getParentProject
argument_list|()
operator|.
name|getVersion
argument_list|()
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
name|ProjectBuildMetadata
name|createProjectModel
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|ProjectBuildMetadata
name|model
init|=
operator|new
name|ProjectBuildMetadata
argument_list|()
decl_stmt|;
name|model
operator|.
name|setId
argument_list|(
name|version
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
name|organization
init|=
operator|new
name|Organization
argument_list|()
decl_stmt|;
name|organization
operator|.
name|setName
argument_list|(
name|TEST_ORGANIZATION_NAME
argument_list|)
expr_stmt|;
name|organization
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
name|organization
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
name|MavenProjectFacet
name|mavenProjectFacet
init|=
operator|new
name|MavenProjectFacet
argument_list|()
decl_stmt|;
name|mavenProjectFacet
operator|.
name|setGroupId
argument_list|(
name|TEST_GROUP_ID
argument_list|)
expr_stmt|;
name|mavenProjectFacet
operator|.
name|setArtifactId
argument_list|(
name|TEST_ARTIFACT_ID
argument_list|)
expr_stmt|;
name|mavenProjectFacet
operator|.
name|setPackaging
argument_list|(
name|TEST_PACKAGING
argument_list|)
expr_stmt|;
name|MavenProjectParent
name|parent
init|=
operator|new
name|MavenProjectParent
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
name|mavenProjectFacet
operator|.
name|setParent
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|model
operator|.
name|addFacet
argument_list|(
name|mavenProjectFacet
argument_list|)
expr_stmt|;
return|return
name|model
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
name|metadataRepository
operator|=
operator|(
name|MemoryMetadataRepository
operator|)
name|action
operator|.
name|getMetadataRepository
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

