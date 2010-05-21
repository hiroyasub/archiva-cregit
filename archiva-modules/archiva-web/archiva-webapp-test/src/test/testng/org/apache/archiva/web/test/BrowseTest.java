begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|test
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|web
operator|.
name|test
operator|.
name|parent
operator|.
name|AbstractBrowseTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|annotations
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"browse"
block|}
argument_list|,
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactNullValues"
block|}
argument_list|)
specifier|public
class|class
name|BrowseTest
extends|extends
name|AbstractBrowseTest
block|{
specifier|public
name|void
name|testBrowseArtifactPageTabs
parameter_list|()
block|{
name|goToBrowsePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_GROUPID"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_ARTIFACTID"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_VERSION"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Info"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Dependencies"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Dependency Tree"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Used By"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Mailing Lists"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Metadata"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBrowseArtifact
parameter_list|()
block|{
name|goToBrowsePage
argument_list|()
expr_stmt|;
name|assertBrowsePage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testClickArtifactFromBrowse
parameter_list|()
block|{
name|goToBrowsePage
argument_list|()
expr_stmt|;
name|assertBrowsePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
name|getProperty
argument_list|(
literal|"ARTIFACT_ARTIFACTID"
argument_list|)
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Browse Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifacts"
argument_list|)
expr_stmt|;
block|}
comment|// MRM-1278
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"requiresUpload"
block|}
argument_list|)
specifier|public
name|void
name|testCorrectRepositoryInBrowse
parameter_list|()
block|{
name|String
name|releasesRepo
init|=
name|getProperty
argument_list|(
literal|"RELEASES_REPOSITORY"
argument_list|)
decl_stmt|;
comment|// create releases repository first
name|goToRepositoriesPage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Add"
argument_list|)
expr_stmt|;
name|addManagedRepository
argument_list|(
name|getProperty
argument_list|(
literal|"RELEASES_REPOSITORY"
argument_list|)
argument_list|,
literal|"Releases Repository"
argument_list|,
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target/repository/releases"
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|,
literal|""
argument_list|,
literal|"Maven 2.x Repository"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Releases Repository"
argument_list|)
expr_stmt|;
name|String
name|snapshotsRepo
init|=
name|getProperty
argument_list|(
literal|"SNAPSHOTS_REPOSITORY"
argument_list|)
decl_stmt|;
name|String
name|path
init|=
literal|"src/test/resources/snapshots/org/apache/maven/archiva/web/test/foo-bar/1.0-SNAPSHOT/foo-bar-1.0-SNAPSHOT.jar"
decl_stmt|;
comment|// TODO: do this differently as uploading doesn't work on browsers other than *chrome (below as well)
comment|// upload a snapshot artifact to repository 'releases'
name|addArtifact
argument_list|(
literal|"archiva"
argument_list|,
literal|"archiva-webapp"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|,
name|path
argument_list|,
name|releasesRepo
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact 'archiva:archiva-webapp:1.0-SNAPSHOT' was successfully deployed to repository '"
operator|+
name|releasesRepo
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|goToBrowsePage
argument_list|()
expr_stmt|;
name|assertBrowsePage
argument_list|()
expr_stmt|;
name|assertGroupsPage
argument_list|(
literal|"archiva/"
argument_list|)
expr_stmt|;
name|assertArtifactsPage
argument_list|(
literal|"archiva-webapp/"
argument_list|)
expr_stmt|;
name|assertArtifactInfoPage
argument_list|(
literal|"1.0-SNAPSHOT/"
argument_list|,
name|releasesRepo
argument_list|,
literal|"archiva"
argument_list|,
literal|"archiva-webapp"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
comment|// upload a snapshot artifact to repository 'snapshots'
name|addArtifact
argument_list|(
literal|"continuum"
argument_list|,
literal|"continuum-core"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|,
name|path
argument_list|,
name|snapshotsRepo
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact 'continuum:continuum-core:1.0-SNAPSHOT' was successfully deployed to repository '"
operator|+
name|snapshotsRepo
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|goToBrowsePage
argument_list|()
expr_stmt|;
name|assertBrowsePage
argument_list|()
expr_stmt|;
name|assertGroupsPage
argument_list|(
literal|"continuum/"
argument_list|)
expr_stmt|;
name|assertArtifactsPage
argument_list|(
literal|"continuum-core/"
argument_list|)
expr_stmt|;
name|assertArtifactInfoPage
argument_list|(
literal|"1.0-SNAPSHOT/"
argument_list|,
name|snapshotsRepo
argument_list|,
literal|"continuum"
argument_list|,
literal|"continuum-core"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
comment|// MRM-1353
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"requiresUpload"
block|}
argument_list|)
specifier|public
name|void
name|testBuildNumberOfSnapshotArtifact
parameter_list|()
block|{
name|String
name|snapshotsRepo
init|=
name|getProperty
argument_list|(
literal|"SNAPSHOTS_REPOSITORY"
argument_list|)
decl_stmt|;
name|String
name|path
init|=
literal|"src/test/resources/snapshots/org/apache/maven/archiva/web/test/foo-bar/1.0-SNAPSHOT/foo-bar-1.0-SNAPSHOT.jar"
decl_stmt|;
comment|// TODO: do this differently as uploading doesn't work on browsers other than *chrome (below as well)
comment|// upload a snapshot artifact to repository 'releases'
name|addArtifact
argument_list|(
literal|"archiva"
argument_list|,
literal|"archiva-multiple-artifacts"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|,
name|path
argument_list|,
name|snapshotsRepo
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact 'archiva:archiva-multiple-artifacts:1.0-SNAPSHOT' was successfully deployed to repository '"
operator|+
name|snapshotsRepo
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|goToBrowsePage
argument_list|()
expr_stmt|;
name|assertBrowsePage
argument_list|()
expr_stmt|;
name|assertGroupsPage
argument_list|(
literal|"archiva/"
argument_list|)
expr_stmt|;
name|assertArtifactsPage
argument_list|(
literal|"archiva-multiple-artifacts/"
argument_list|)
expr_stmt|;
name|assertArtifactInfoPage
argument_list|(
literal|"1.0-SNAPSHOT/"
argument_list|,
name|snapshotsRepo
argument_list|,
literal|"archiva"
argument_list|,
literal|"archiva-multiple-artifacts"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|addArtifact
argument_list|(
literal|"archiva"
argument_list|,
literal|"archiva-multiple-artifacts"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|,
name|path
argument_list|,
name|snapshotsRepo
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact 'archiva:archiva-multiple-artifacts:1.0-SNAPSHOT' was successfully deployed to repository '"
operator|+
name|snapshotsRepo
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|goToBrowsePage
argument_list|()
expr_stmt|;
name|assertBrowsePage
argument_list|()
expr_stmt|;
name|assertGroupsPage
argument_list|(
literal|"archiva/"
argument_list|)
expr_stmt|;
name|assertArtifactsPage
argument_list|(
literal|"archiva-multiple-artifacts/"
argument_list|)
expr_stmt|;
name|assertArtifactInfoPage
argument_list|(
literal|"1.0-SNAPSHOT/"
argument_list|,
name|snapshotsRepo
argument_list|,
literal|"archiva"
argument_list|,
literal|"archiva-multiple-artifacts"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|String
name|firstSnapshotVersion
init|=
name|getText
argument_list|(
literal|"//div[@id='download']/div[@id='accordion']/p[2]/a/"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|firstSnapshotVersion
operator|.
name|endsWith
argument_list|(
literal|"-1"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|secondSnapshotVersion
init|=
name|getText
argument_list|(
literal|"//div[@id='download']/div[@id='accordion']/p[1]/a/"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|secondSnapshotVersion
operator|.
name|endsWith
argument_list|(
literal|"-2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertArtifactInfoPage
parameter_list|(
name|String
name|version
parameter_list|,
name|String
name|artifactInfoRepositoryId
parameter_list|,
name|String
name|artifactInfoGroupId
parameter_list|,
name|String
name|artifactInfoArtifactId
parameter_list|,
name|String
name|artifactInfoVersion
parameter_list|,
name|String
name|artifactInfoPackaging
parameter_list|)
block|{
name|clickLinkWithText
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Browse Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|artifactInfoRepositoryId
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|artifactInfoGroupId
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|artifactInfoArtifactId
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|artifactInfoVersion
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|artifactInfoPackaging
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertArtifactsPage
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|clickLinkWithText
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Browse Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Versions"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertGroupsPage
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|clickLinkWithText
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Browse Repository"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifacts"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

