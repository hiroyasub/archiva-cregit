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
name|AbstractArchivaTest
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
literal|"auditlogsreport"
block|}
argument_list|,
name|dependsOnMethods
operator|=
block|{
literal|"testWithCorrectUsernamePassword"
block|}
argument_list|)
specifier|public
class|class
name|AuditLogsReportTest
extends|extends
name|AbstractArchivaTest
block|{
specifier|private
name|void
name|goToAuditLogReports
parameter_list|()
block|{
name|clickLinkWithText
argument_list|(
literal|"Audit Log Report"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertAuditLogsReportPage
parameter_list|()
block|{
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Audit Log Report"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Audit Log Report"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"repository"
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
literal|"startDate"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"endDate"
argument_list|)
expr_stmt|;
name|assertElementPresent
argument_list|(
literal|"rowCount"
argument_list|)
expr_stmt|;
name|assertButtonWithValuePresent
argument_list|(
literal|"View Audit Log"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testWithCorrectUsernamePassword"
block|}
argument_list|)
specifier|public
name|void
name|testAuditLogsReport
parameter_list|()
block|{
name|goToAuditLogReports
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Latest Events"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testWithCorrectUsernamePassword"
block|}
argument_list|)
specifier|public
name|void
name|testViewAuditLogsNoDataFound
parameter_list|()
block|{
name|goToAuditLogReports
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"groupId"
argument_list|,
literal|"non.existing"
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Audit Log Report"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"No audit logs found."
argument_list|)
expr_stmt|;
block|}
comment|// TODO: add test for adding via WebDAV
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactValidValues"
block|}
argument_list|,
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testViewAuditLogsDataFound
parameter_list|()
block|{
name|goToAuditLogReports
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|selectValue
argument_list|(
literal|"repository"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"groupId"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"No audit logs found."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"testAddArtifactValidValues-1.0.jar"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Uploaded File"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"admin"
argument_list|)
expr_stmt|;
block|}
comment|// TODO: add test for adding via WebDAV
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactValidValues"
block|}
argument_list|,
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testViewAuditLogsOnlyArtifactIdIsSpecified
parameter_list|()
block|{
name|goToAuditLogReports
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|selectValue
argument_list|(
literal|"repository"
argument_list|,
literal|"internal"
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"artifactId"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"No audit logs found."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"testAddArtifactValidValues-1.0.jar"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Uploaded File"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"admin"
argument_list|)
expr_stmt|;
block|}
comment|// TODO: add test for adding via WebDAV
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactValidValues"
block|}
argument_list|,
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testViewAuditLogsForAllRepositories
parameter_list|()
block|{
name|goToAuditLogReports
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|selectValue
argument_list|(
literal|"repository"
argument_list|,
literal|"all"
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"No audit logs found."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"testAddArtifactValidValues-1.0.jar"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Uploaded File"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"admin"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactValidValues"
block|,
literal|"testUserWithRepoManagerInternalRole"
block|}
argument_list|,
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testViewAuditLogsViewAuditEventsForManageableRepositoriesOnly
parameter_list|()
block|{
name|String
name|groupId
init|=
name|getProperty
argument_list|(
literal|"SNAPSHOT_GROUPID"
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|getProperty
argument_list|(
literal|"SNAPSHOT_ARTIFACTID"
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|getProperty
argument_list|(
literal|"SNAPSHOT_VERSION"
argument_list|)
decl_stmt|;
name|String
name|repo
init|=
name|getProperty
argument_list|(
literal|"SNAPSHOT_REPOSITORYID"
argument_list|)
decl_stmt|;
name|String
name|packaging
init|=
name|getProperty
argument_list|(
literal|"SNAPSHOT_PACKAGING"
argument_list|)
decl_stmt|;
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
name|getProperty
argument_list|(
literal|"SNAPSHOT_ARTIFACTFILEPATH"
argument_list|)
argument_list|,
name|repo
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
name|version
operator|+
literal|"' was successfully deployed to repository '"
operator|+
name|repo
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Logout"
argument_list|)
expr_stmt|;
name|login
argument_list|(
name|getProperty
argument_list|(
literal|"REPOMANAGER_INTERNAL_USERNAME"
argument_list|)
argument_list|,
name|getUserRoleNewPassword
argument_list|()
argument_list|)
expr_stmt|;
name|goToAuditLogReports
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|selectValue
argument_list|(
literal|"repository"
argument_list|,
literal|"all"
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Results"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"No audit logs found."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"testAddArtifactValidValues-1.0.jar"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Uploaded File"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"admin"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
name|artifactId
operator|+
literal|"-"
operator|+
name|version
operator|+
literal|"."
operator|+
name|packaging
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Logout"
argument_list|)
expr_stmt|;
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
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactValidValues"
block|}
argument_list|,
name|groups
operator|=
literal|"requiresUpload"
argument_list|)
specifier|public
name|void
name|testViewAuditLogsReportForGroupId
parameter_list|()
block|{
name|String
name|groupId
init|=
name|getProperty
argument_list|(
literal|"AUDITLOG_GROUPID"
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|getProperty
argument_list|(
literal|"ARTIFACTID"
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|getProperty
argument_list|(
literal|"VERSION"
argument_list|)
decl_stmt|;
name|String
name|packaging
init|=
name|getProperty
argument_list|(
literal|"PACKAGING"
argument_list|)
decl_stmt|;
name|String
name|repositoryId
init|=
name|getProperty
argument_list|(
literal|"REPOSITORYID"
argument_list|)
decl_stmt|;
name|String
name|expectedArtifact
init|=
name|getProperty
argument_list|(
literal|"AUDITLOG_EXPECTED_ARTIFACT"
argument_list|)
decl_stmt|;
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
name|getProperty
argument_list|(
literal|"SNAPSHOT_ARTIFACTFILEPATH"
argument_list|)
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
name|goToAuditLogReports
argument_list|()
expr_stmt|;
name|selectValue
argument_list|(
literal|"repository"
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|submit
argument_list|()
expr_stmt|;
name|assertAuditLogsReportPage
argument_list|()
expr_stmt|;
name|assertTextPresent
argument_list|(
name|expectedArtifact
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

