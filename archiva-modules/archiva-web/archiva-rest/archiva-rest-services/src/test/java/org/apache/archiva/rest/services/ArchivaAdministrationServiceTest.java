begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|FileType
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|OrganisationInformation
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|UiConfiguration
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|AdminRepositoryConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaAdministrationServiceTest
extends|extends
name|AbstractArchivaRestTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|getAllLegacyPaths
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getLegacyArtifactPaths
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getLegacyArtifactPaths
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|addAndDeleteFileType
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|initialSize
init|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getFileTypes
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|FileType
name|fileType
init|=
operator|new
name|FileType
argument_list|()
decl_stmt|;
name|fileType
operator|.
name|setId
argument_list|(
literal|"footwo"
argument_list|)
expr_stmt|;
name|fileType
operator|.
name|setPatterns
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|addFileType
argument_list|(
name|fileType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|+
literal|1
argument_list|,
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getFileTypes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getFileType
argument_list|(
literal|"footwo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|,
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getFileType
argument_list|(
literal|"footwo"
argument_list|)
operator|.
name|getPatterns
argument_list|()
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|removeFileType
argument_list|(
literal|"footwo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
argument_list|,
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getFileTypes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getFileType
argument_list|(
literal|"footwo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|organisationInformationUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|OrganisationInformation
name|organisationInformation
init|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getOrganisationInformation
argument_list|()
decl_stmt|;
comment|// rest return an empty bean
name|assertNotNull
argument_list|(
name|organisationInformation
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|organisationInformation
operator|.
name|getLogoLocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|organisationInformation
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|organisationInformation
operator|.
name|getUrl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|organisationInformation
operator|=
operator|new
name|OrganisationInformation
argument_list|()
expr_stmt|;
name|organisationInformation
operator|.
name|setLogoLocation
argument_list|(
literal|"http://foo.com/bar.png"
argument_list|)
expr_stmt|;
name|organisationInformation
operator|.
name|setName
argument_list|(
literal|"foo org"
argument_list|)
expr_stmt|;
name|organisationInformation
operator|.
name|setUrl
argument_list|(
literal|"http://foo.com"
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|setOrganisationInformation
argument_list|(
name|organisationInformation
argument_list|)
expr_stmt|;
name|organisationInformation
operator|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getOrganisationInformation
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|organisationInformation
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo.com/bar.png"
argument_list|,
name|organisationInformation
operator|.
name|getLogoLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo org"
argument_list|,
name|organisationInformation
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo.com"
argument_list|,
name|organisationInformation
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|uiConfigurationReadUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|UiConfiguration
name|ui
init|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getUiConfiguration
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ui
argument_list|)
expr_stmt|;
comment|// assert default values
name|assertFalse
argument_list|(
name|ui
operator|.
name|isDisableEasterEggs
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ui
operator|.
name|isDisableRegistration
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ui
operator|.
name|isAppletFindEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ui
operator|.
name|isShowFindArtifacts
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ui
operator|.
name|getApplicationUrl
argument_list|()
argument_list|)
expr_stmt|;
name|ui
operator|.
name|setAppletFindEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ui
operator|.
name|setShowFindArtifacts
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ui
operator|.
name|setDisableEasterEggs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ui
operator|.
name|setDisableRegistration
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|String
name|url
init|=
literal|"http://foo.fr/bar"
decl_stmt|;
name|ui
operator|.
name|setApplicationUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|setUiConfiguration
argument_list|(
name|ui
argument_list|)
expr_stmt|;
name|ui
operator|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getUiConfiguration
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|ui
operator|.
name|isDisableEasterEggs
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ui
operator|.
name|isDisableRegistration
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ui
operator|.
name|isAppletFindEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ui
operator|.
name|isShowFindArtifacts
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|url
argument_list|,
name|ui
operator|.
name|getApplicationUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|uiConfigurationUpdate_ApplicationUrlHasTrailingSlash
parameter_list|()
throws|throws
name|Exception
block|{
name|UiConfiguration
name|ui
init|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getUiConfiguration
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ui
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ui
operator|.
name|getApplicationUrl
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|url
init|=
literal|"http://foo.fr/bar/"
decl_stmt|;
name|ui
operator|.
name|setApplicationUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|setUiConfiguration
argument_list|(
name|ui
argument_list|)
expr_stmt|;
name|ui
operator|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getUiConfiguration
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo.fr/bar"
argument_list|,
name|ui
operator|.
name|getApplicationUrl
argument_list|()
argument_list|)
expr_stmt|;
comment|// test if multiple '/' is trailing
name|url
operator|=
literal|"http://foo.fr/bar//"
expr_stmt|;
name|ui
operator|.
name|setApplicationUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|setUiConfiguration
argument_list|(
name|ui
argument_list|)
expr_stmt|;
name|ui
operator|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getUiConfiguration
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo.fr/bar"
argument_list|,
name|ui
operator|.
name|getApplicationUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getKnownContentAdminRepositoryConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|consumers
init|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getKnownContentAdminRepositoryConsumers
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|consumers
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getInvalidContentAdminRepositoryConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|consumers
init|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getInvalidContentAdminRepositoryConsumers
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|consumers
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertAllDisabled
argument_list|(
name|consumers
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|enabledInvalidContentConsumer
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|consumers
operator|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getInvalidContentAdminRepositoryConsumers
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|consumers
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertAllEnabled
argument_list|(
name|consumers
argument_list|)
expr_stmt|;
name|getArchivaAdministrationService
argument_list|()
operator|.
name|disabledInvalidContentConsumer
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|consumers
operator|=
name|getArchivaAdministrationService
argument_list|()
operator|.
name|getInvalidContentAdminRepositoryConsumers
argument_list|()
expr_stmt|;
name|assertAllDisabled
argument_list|(
name|consumers
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|consumers
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertAllEnabled
parameter_list|(
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|consumers
parameter_list|)
block|{
for|for
control|(
name|AdminRepositoryConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|assertTrue
argument_list|(
name|consumer
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertAllDisabled
parameter_list|(
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|consumers
parameter_list|)
block|{
for|for
control|(
name|AdminRepositoryConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|assertFalse
argument_list|(
name|consumer
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

