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
name|AbstractArtifactManagementTest
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
literal|"artifactmanagement"
block|}
argument_list|,
name|dependsOnGroups
operator|=
block|{
literal|"about"
block|}
argument_list|)
specifier|public
class|class
name|ArtifactManagementTest
extends|extends
name|AbstractArtifactManagementTest
block|{
annotation|@
name|Test
argument_list|(
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testAddArtifactNullValues
parameter_list|()
block|{
name|goToAddArtifactPage
argument_list|()
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Please add a file to upload."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Invalid version."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a groupId."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter an artifactId."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a version"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a packaging"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactNullValues"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testAddArtifactNoGroupId
parameter_list|()
block|{
name|addArtifact
argument_list|(
literal|" "
argument_list|,
name|getArtifactId
argument_list|()
argument_list|,
name|getVersion
argument_list|()
argument_list|,
name|getPackaging
argument_list|()
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a groupId."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactNoGroupId"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testAddArtifactNoArtifactId
parameter_list|()
block|{
name|addArtifact
argument_list|(
name|getGroupId
argument_list|()
argument_list|,
literal|" "
argument_list|,
name|getVersion
argument_list|()
argument_list|,
name|getPackaging
argument_list|()
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter an artifactId."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactNoGroupId"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testAddArtifactNoVersion
parameter_list|()
block|{
name|addArtifact
argument_list|(
name|getGroupId
argument_list|()
argument_list|,
name|getArtifactId
argument_list|()
argument_list|,
literal|" "
argument_list|,
name|getPackaging
argument_list|()
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a version."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactNoGroupId"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testAddArtifactInvalidVersion
parameter_list|()
block|{
name|addArtifact
argument_list|(
name|getGroupId
argument_list|()
argument_list|,
name|getArtifactId
argument_list|()
argument_list|,
literal|"asdf"
argument_list|,
name|getPackaging
argument_list|()
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Invalid version."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactNoGroupId"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testAddArtifactNoPackaging
parameter_list|()
block|{
name|addArtifact
argument_list|(
name|getGroupId
argument_list|()
argument_list|,
name|getArtifactId
argument_list|()
argument_list|,
name|getVersion
argument_list|()
argument_list|,
literal|" "
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a packaging."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactNoGroupId"
block|}
argument_list|,
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testAddArtifactNoFilePath
parameter_list|()
block|{
name|addArtifact
argument_list|(
name|getGroupId
argument_list|()
argument_list|,
name|getArtifactId
argument_list|()
argument_list|,
name|getVersion
argument_list|()
argument_list|,
name|getPackaging
argument_list|()
argument_list|,
literal|" "
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Please add a file to upload."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testAddArtifactValidValues
parameter_list|()
block|{
name|String
name|groupId
init|=
name|getProperty
argument_list|(
literal|"VALIDARTIFACT_GROUPID"
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|getProperty
argument_list|(
literal|"VALIDARTIFACT_ARTIFACTID"
argument_list|)
decl_stmt|;
name|addArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|getVersion
argument_list|()
argument_list|,
name|getPackaging
argument_list|()
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact '"
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|getVersion
argument_list|()
operator|+
literal|"' was successfully deployed to repository 'internal'"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testDotNetTypes
parameter_list|()
block|{
name|String
name|groupId
init|=
name|getProperty
argument_list|(
literal|"GROUPID_DOTNETARTIFACT"
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|getProperty
argument_list|(
literal|"ARTIFACTID_DOTNETARTIFACT"
argument_list|)
decl_stmt|;
name|String
name|packaging
init|=
name|getProperty
argument_list|(
literal|"PACKAGING_DOTNETARTIFACT"
argument_list|)
decl_stmt|;
name|addArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|getVersion
argument_list|()
argument_list|,
name|packaging
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact '"
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|getVersion
argument_list|()
operator|+
literal|"' was successfully deployed to repository 'internal'"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|open
argument_list|(
name|baseUrl
operator|+
literal|"/browse/"
operator|+
name|groupId
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
operator|+
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|waitPage
argument_list|()
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"<type>library</type>"
argument_list|)
expr_stmt|;
name|String
name|basePath
init|=
literal|"/archiva/repository/internal/"
operator|+
name|groupId
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
operator|+
name|getVersion
argument_list|()
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"-"
operator|+
name|getVersion
argument_list|()
decl_stmt|;
name|assertLinkPresent
argument_list|(
literal|".NET Library"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"//a[@href='"
operator|+
name|basePath
operator|+
literal|".dll']"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"//a[@href='"
operator|+
name|basePath
operator|+
literal|".pom']"
argument_list|)
expr_stmt|;
block|}
comment|// MRM-747
annotation|@
name|Test
argument_list|(
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testAddArtifactBlockRedeployments
parameter_list|()
block|{
name|addArtifact
argument_list|(
name|getGroupId
argument_list|()
argument_list|,
name|getArtifactId
argument_list|()
argument_list|,
name|getVersion
argument_list|()
argument_list|,
name|getPackaging
argument_list|()
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Overwriting released artifacts in repository '"
operator|+
name|getRepositoryId
argument_list|()
operator|+
literal|"' is not allowed."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testDeleteArtifact
parameter_list|()
block|{
comment|// prep
name|String
name|groupId
init|=
name|getProperty
argument_list|(
literal|"GROUPID1"
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|getProperty
argument_list|(
literal|"ARTIFACTID1"
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|getProperty
argument_list|(
literal|"VERSION1"
argument_list|)
decl_stmt|;
name|String
name|packaging
init|=
name|getProperty
argument_list|(
literal|"PACKAGING1"
argument_list|)
decl_stmt|;
name|String
name|repositoryId
init|=
name|getProperty
argument_list|(
literal|"REPOSITORYID1"
argument_list|)
decl_stmt|;
comment|// TODO: do this differently as it only works in Firefox's chrome mode
name|addArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|packaging
argument_list|,
name|getArtifactFilePath
argument_list|()
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact 'delete:delete:1.0' was successfully deployed to repository 'internal'"
argument_list|)
expr_stmt|;
name|deleteArtifact
argument_list|(
literal|"delete"
argument_list|,
literal|"delete"
argument_list|,
literal|"1.0"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact 'delete:delete:1.0' was successfully deleted from repository 'internal'"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testDeleteArtifactNoGroupId
parameter_list|()
block|{
name|deleteArtifact
argument_list|(
literal|" "
argument_list|,
literal|"delete"
argument_list|,
literal|"1.0"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a groupId."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testDeleteArtifactNoArtifactId
parameter_list|()
block|{
name|deleteArtifact
argument_list|(
literal|"delete"
argument_list|,
literal|" "
argument_list|,
literal|"1.0"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter an artifactId."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testDeleteArtifactNoVersion
parameter_list|()
block|{
name|deleteArtifact
argument_list|(
literal|"delete"
argument_list|,
literal|"delete"
argument_list|,
literal|" "
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Invalid version."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a version."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testDeleteArtifactInvalidVersion
parameter_list|()
block|{
name|deleteArtifact
argument_list|(
literal|"delete"
argument_list|,
literal|"delete"
argument_list|,
literal|"asdf"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Invalid version."
argument_list|)
expr_stmt|;
block|}
comment|// HTML select should have the proper value, else it will cause a selenium error: Option with label 'customValue' not found
annotation|@
name|Test
argument_list|(
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testDeleteArtifactInvalidValues
parameter_list|()
block|{
name|deleteArtifact
argument_list|(
literal|"<> \\/~+[ ]'\""
argument_list|,
literal|"<> \\/~+[ ]'\""
argument_list|,
literal|"<>"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Invalid version."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Group id must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact id must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testDeleteArtifactInvalidGroupId
parameter_list|()
block|{
name|deleteArtifact
argument_list|(
literal|"<> \\/~+[ ]'\""
argument_list|,
literal|"delete"
argument_list|,
literal|"1.0"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Group id must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|alwaysRun
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|testDeleteArtifactInvalidArtifactId
parameter_list|()
block|{
name|deleteArtifact
argument_list|(
literal|"delete"
argument_list|,
literal|"<> \\/~+[ ]'\""
argument_list|,
literal|"1.0"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact id must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

