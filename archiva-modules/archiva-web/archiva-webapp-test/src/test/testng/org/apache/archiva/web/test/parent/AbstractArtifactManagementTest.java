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
operator|.
name|parent
package|;
end_package

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractArtifactManagementTest
extends|extends
name|AbstractArchivaTest
block|{
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
name|String
name|groupId
init|=
name|getProperty
argument_list|(
literal|"GROUPID"
argument_list|)
decl_stmt|;
return|return
name|groupId
return|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
name|String
name|artifactId
init|=
name|getProperty
argument_list|(
literal|"ARTIFACTID"
argument_list|)
decl_stmt|;
return|return
name|artifactId
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
name|String
name|version
init|=
name|getProperty
argument_list|(
literal|"VERSION"
argument_list|)
decl_stmt|;
return|return
name|version
return|;
block|}
specifier|public
name|String
name|getPackaging
parameter_list|()
block|{
name|String
name|packaging
init|=
name|getProperty
argument_list|(
literal|"PACKAGING"
argument_list|)
decl_stmt|;
return|return
name|packaging
return|;
block|}
specifier|public
name|String
name|getArtifactFilePath
parameter_list|()
block|{
return|return
literal|"src/test/it-resources/snapshots/org/apache/maven/archiva/web/test/foo-bar/1.0-SNAPSHOT/foo-bar-1.0-SNAPSHOT.jar"
return|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
name|String
name|repositoryId
init|=
name|getProperty
argument_list|(
literal|"REPOSITORYID"
argument_list|)
decl_stmt|;
return|return
name|repositoryId
return|;
block|}
specifier|public
name|void
name|goToDeleteArtifactPage
parameter_list|()
block|{
name|login
argument_list|(
name|getProperty
argument_list|(
literal|"ADMIN_USERNAME"
argument_list|)
argument_list|,
name|getProperty
argument_list|(
literal|"ADMIN_PASSWORD"
argument_list|)
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Delete Artifact"
argument_list|)
expr_stmt|;
name|assertDeleteArtifactPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|deleteArtifact
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
name|repositoryId
parameter_list|)
block|{
name|goToDeleteArtifactPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|selectValue
argument_list|(
literal|"repositoryId"
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertDeleteArtifactPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Delete Artifact"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Delete Artifact"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Group Id*:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Artifact Id*:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Version*:"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Repository Id:"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"groupId"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"artifactId"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"repositoryId"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Submit"
argument_list|)
expr_stmt|;
block|}
comment|// Legacy Support
specifier|public
name|void
name|goToLegacySupportPage
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Legacy Support"
argument_list|)
expr_stmt|;
name|assertLegacySupportPage
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|assertLegacySupportPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Legacy Support"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Administration - Legacy Artifact Path Resolution"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Path Mappings"
argument_list|)
expr_stmt|;
name|assertLinkPresent
argument_list|(
literal|"Add"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addLegacyArtifactPath
parameter_list|(
name|String
name|path
parameter_list|,
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
block|{
name|assertAddLegacyArtifactPathPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"legacyArtifactPath.path"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"classifier"
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Add Legacy Artifact Path"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertAddLegacyArtifactPathPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Admin: Add Legacy Artifact Path"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Admin: Add Legacy Artifact Path"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Enter the legacy path to map to a particular artifact reference, then adjust the fields as necessary."
argument_list|)
expr_stmt|;
name|String
name|element
init|=
literal|"addLegacyArtifactPath_legacyArtifactPath_path,addLegacyArtifactPath_groupId,addLegacyArtifactPath_artifactId,addLegacyArtifactPath_version,addLegacyArtifactPath_classifier,addLegacyArtifactPath_type"
decl_stmt|;
name|String
index|[]
name|arrayElement
init|=
name|element
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|arrayelement
range|:
name|arrayElement
control|)
name|assertElementPresent
argument_list|(
name|arrayelement
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"Add Legacy Artifact Path"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

