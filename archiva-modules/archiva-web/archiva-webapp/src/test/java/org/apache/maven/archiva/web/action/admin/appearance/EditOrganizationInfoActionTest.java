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
operator|.
name|admin
operator|.
name|appearance
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|validator
operator|.
name|ActionValidatorManager
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
name|configuration
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
name|DefaultActionValidatorManagerFactory
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
name|validator
operator|.
name|utils
operator|.
name|ValidatorUtil
import|;
end_import

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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|EditOrganizationInfoActionTest
extends|extends
name|AbstractOrganizationInfoActionTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_STRING
init|=
literal|""
decl_stmt|;
comment|// valid inputs
specifier|private
specifier|static
specifier|final
name|String
name|ORGANISATION_NAME_VALID_INPUT
init|=
literal|"abcXYZ0129.   _/\\~   :?!&=-"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ORGANISATION_URL_VALID_INPUT
init|=
literal|"file://home/user/abcXYZ0129._/\\~:?!&=-<> ~+[ ]'\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ORGANISATION_LOGO_VALID_INPUT
init|=
literal|"file://home/user/abcXYZ0129._/\\~:?!&=-<> ~+[ ]'\""
decl_stmt|;
comment|// invalid inputs
specifier|private
specifier|static
specifier|final
name|String
name|ORGANISATION_NAME_INVALID_INPUT
init|=
literal|"<>~+[ ]'\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ORGANISATION_URL_INVALID_INPUT
init|=
literal|"/home/user/abcXYZ0129._/\\~:?!&=-<> ~+[ ]'\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ORGANISATION_LOGO_INVALID_INPUT
init|=
literal|"/home/user/abcXYZ0129._/\\~:?!&=-<> ~+[ ]'\""
decl_stmt|;
comment|// testing requisite
specifier|private
name|ActionValidatorManager
name|actionValidatorManager
decl_stmt|;
annotation|@
name|Override
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
name|DefaultActionValidatorManagerFactory
name|factory
init|=
operator|new
name|DefaultActionValidatorManagerFactory
argument_list|()
decl_stmt|;
name|actionValidatorManager
operator|=
name|factory
operator|.
name|createDefaultActionValidatorManager
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testOrganisationInfoSaves
parameter_list|()
throws|throws
name|Exception
block|{
name|config
operator|.
name|setOrganisationInfo
argument_list|(
operator|new
name|OrganisationInformation
argument_list|()
argument_list|)
expr_stmt|;
name|OrganisationInformation
name|orginfo
init|=
name|config
operator|.
name|getOrganisationInfo
argument_list|()
decl_stmt|;
name|orginfo
operator|.
name|setLogoLocation
argument_list|(
literal|"LOGO"
argument_list|)
expr_stmt|;
name|orginfo
operator|.
name|setName
argument_list|(
literal|"NAME"
argument_list|)
expr_stmt|;
name|orginfo
operator|.
name|setUrl
argument_list|(
literal|"URL"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|reloadAction
argument_list|()
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"LOGO"
argument_list|,
name|action
operator|.
name|getOrganisationLogo
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NAME"
argument_list|,
name|action
operator|.
name|getOrganisationName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URL"
argument_list|,
name|action
operator|.
name|getOrganisationUrl
argument_list|()
argument_list|)
expr_stmt|;
name|action
operator|.
name|setOrganisationLogo
argument_list|(
literal|"LOGO1"
argument_list|)
expr_stmt|;
name|action
operator|.
name|setOrganisationName
argument_list|(
literal|"NAME1"
argument_list|)
expr_stmt|;
name|action
operator|.
name|setOrganisationUrl
argument_list|(
literal|"URL1"
argument_list|)
expr_stmt|;
name|action
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"LOGO1"
argument_list|,
name|orginfo
operator|.
name|getLogoLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NAME1"
argument_list|,
name|orginfo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URL1"
argument_list|,
name|orginfo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStruts2ValidationFrameworkWithNullInputs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prep
name|action
operator|=
name|getAction
argument_list|()
expr_stmt|;
name|populateOrganisationValues
argument_list|(
name|action
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// test
name|actionValidatorManager
operator|.
name|validate
argument_list|(
name|action
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// verify
name|assertTrue
argument_list|(
name|action
operator|.
name|hasFieldErrors
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|fieldErrors
init|=
name|action
operator|.
name|getFieldErrors
argument_list|()
decl_stmt|;
comment|// make an expected field error object
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|expectedFieldErrors
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// populate
name|List
argument_list|<
name|String
argument_list|>
name|expectedErrorMessages
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expectedErrorMessages
operator|.
name|add
argument_list|(
literal|"You must enter a name"
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"organisationName"
argument_list|,
name|expectedErrorMessages
argument_list|)
expr_stmt|;
name|ValidatorUtil
operator|.
name|assertFieldErrors
argument_list|(
name|expectedFieldErrors
argument_list|,
name|fieldErrors
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStruts2ValidationFrameworkWithBlankInputs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prep
name|action
operator|=
name|getAction
argument_list|()
expr_stmt|;
name|populateOrganisationValues
argument_list|(
name|action
argument_list|,
name|EMPTY_STRING
argument_list|,
name|EMPTY_STRING
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// test
name|actionValidatorManager
operator|.
name|validate
argument_list|(
name|action
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// verify
name|assertTrue
argument_list|(
name|action
operator|.
name|hasFieldErrors
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|fieldErrors
init|=
name|action
operator|.
name|getFieldErrors
argument_list|()
decl_stmt|;
comment|// make an expected field error object
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|expectedFieldErrors
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// populate
name|List
argument_list|<
name|String
argument_list|>
name|expectedErrorMessages
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expectedErrorMessages
operator|.
name|add
argument_list|(
literal|"You must enter a name"
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"organisationName"
argument_list|,
name|expectedErrorMessages
argument_list|)
expr_stmt|;
name|ValidatorUtil
operator|.
name|assertFieldErrors
argument_list|(
name|expectedFieldErrors
argument_list|,
name|fieldErrors
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStruts2ValidationFrameworkWithInvalidInputs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prep
name|action
operator|=
name|getAction
argument_list|()
expr_stmt|;
name|populateOrganisationValues
argument_list|(
name|action
argument_list|,
name|ORGANISATION_NAME_INVALID_INPUT
argument_list|,
name|ORGANISATION_URL_INVALID_INPUT
argument_list|,
name|ORGANISATION_LOGO_INVALID_INPUT
argument_list|)
expr_stmt|;
comment|// test
name|actionValidatorManager
operator|.
name|validate
argument_list|(
name|action
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// verify
name|assertTrue
argument_list|(
name|action
operator|.
name|hasFieldErrors
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|fieldErrors
init|=
name|action
operator|.
name|getFieldErrors
argument_list|()
decl_stmt|;
comment|// make an expected field error object
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|expectedFieldErrors
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// populate
name|List
argument_list|<
name|String
argument_list|>
name|expectedErrorMessages
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expectedErrorMessages
operator|.
name|add
argument_list|(
literal|"Organisation name must only contain alphanumeric characters, white-spaces(' '), equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"organisationName"
argument_list|,
name|expectedErrorMessages
argument_list|)
expr_stmt|;
name|expectedErrorMessages
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|expectedErrorMessages
operator|.
name|add
argument_list|(
literal|"You must enter a URL"
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"organisationUrl"
argument_list|,
name|expectedErrorMessages
argument_list|)
expr_stmt|;
name|expectedErrorMessages
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|expectedErrorMessages
operator|.
name|add
argument_list|(
literal|"You must enter a URL"
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"organisationLogo"
argument_list|,
name|expectedErrorMessages
argument_list|)
expr_stmt|;
name|ValidatorUtil
operator|.
name|assertFieldErrors
argument_list|(
name|expectedFieldErrors
argument_list|,
name|fieldErrors
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStruts2ValidationFrameworkWithValidInputs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prep
name|action
operator|=
name|getAction
argument_list|()
expr_stmt|;
name|populateOrganisationValues
argument_list|(
name|action
argument_list|,
name|ORGANISATION_NAME_VALID_INPUT
argument_list|,
name|ORGANISATION_URL_VALID_INPUT
argument_list|,
name|ORGANISATION_LOGO_VALID_INPUT
argument_list|)
expr_stmt|;
comment|// test
name|actionValidatorManager
operator|.
name|validate
argument_list|(
name|action
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// verify
name|assertFalse
argument_list|(
name|action
operator|.
name|hasFieldErrors
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|populateOrganisationValues
parameter_list|(
name|AbstractAppearanceAction
name|abstractAppearanceAction
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|logo
parameter_list|)
block|{
name|abstractAppearanceAction
operator|.
name|setOrganisationName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|abstractAppearanceAction
operator|.
name|setOrganisationUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|abstractAppearanceAction
operator|.
name|setOrganisationLogo
argument_list|(
name|logo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|AbstractAppearanceAction
name|getAction
parameter_list|()
block|{
comment|//return (EditOrganisationInfoAction) lookup( Action.class.getName(), "editOrganisationInfo" );
return|return
operator|(
name|EditOrganisationInfoAction
operator|)
name|getActionProxy
argument_list|(
literal|"/admin/editOrganisationInfo.action"
argument_list|)
operator|.
name|getAction
argument_list|()
return|;
block|}
block|}
end_class

end_unit

