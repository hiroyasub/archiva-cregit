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
name|legacy
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|LegacyArtifactPath
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

begin_class
specifier|public
class|class
name|AddLegacyArtifactPathActionTest
extends|extends
name|TestCase
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
name|LEGACY_ARTIFACT_PATH_PATH_VALID_INPUT
init|=
literal|"-abcXYZ0129._/\\"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_ID_VALID_INPUT
init|=
literal|"abcXYZ0129._-"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT_ID_VALID_INPUT
init|=
literal|"abcXYZ0129._-"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VERSION_VALID_INPUT
init|=
literal|"abcXYZ0129._-"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLASSIFIER_VALID_INPUT
init|=
literal|"abcXYZ0129._-"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_VALID_INPUT
init|=
literal|"abcXYZ0129._-"
decl_stmt|;
comment|// invalid inputs
specifier|private
specifier|static
specifier|final
name|String
name|LEGACY_ARTIFACT_PATH_PATH_INVALID_INPUT
init|=
literal|"<> ~+[ ]'\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_ID_INVALID_INPUT
init|=
literal|"<> \\/~+[ ]'\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT_ID_INVALID_INPUT
init|=
literal|"<> \\/~+[ ]'\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VERSION_INVALID_INPUT
init|=
literal|"<> \\/~+[ ]'\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLASSIFIER_INVALID_INPUT
init|=
literal|"<> \\/~+[ ]'\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_INVALID_INPUT
init|=
literal|"<> \\/~+[ ]'\""
decl_stmt|;
comment|// testing requisite
specifier|private
name|AddLegacyArtifactPathAction
name|addLegacyArtifactPathAction
decl_stmt|;
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
name|addLegacyArtifactPathAction
operator|=
operator|new
name|AddLegacyArtifactPathAction
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
name|testStruts2ValidationFrameworkWithNullInputs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prep
name|LegacyArtifactPath
name|legacyArtifactPath
init|=
name|createLegacyArtifactPath
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|populateAddLegacyArtifactPathActionFields
argument_list|(
name|addLegacyArtifactPathAction
argument_list|,
name|legacyArtifactPath
argument_list|,
literal|null
argument_list|,
literal|null
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
name|addLegacyArtifactPathAction
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// verify
name|assertTrue
argument_list|(
name|addLegacyArtifactPathAction
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
name|addLegacyArtifactPathAction
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
literal|"You must enter a legacy path."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"legacyArtifactPath.path"
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
literal|"You must enter a groupId."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"groupId"
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
literal|"You must enter an artifactId."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"artifactId"
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
literal|"You must enter a version."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"version"
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
literal|"You must enter a type."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"type"
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
name|LegacyArtifactPath
name|legacyArtifactPath
init|=
name|createLegacyArtifactPath
argument_list|(
name|EMPTY_STRING
argument_list|)
decl_stmt|;
name|populateAddLegacyArtifactPathActionFields
argument_list|(
name|addLegacyArtifactPathAction
argument_list|,
name|legacyArtifactPath
argument_list|,
name|EMPTY_STRING
argument_list|,
name|EMPTY_STRING
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
name|addLegacyArtifactPathAction
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// verify
name|assertTrue
argument_list|(
name|addLegacyArtifactPathAction
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
name|addLegacyArtifactPathAction
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
literal|"You must enter a legacy path."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"legacyArtifactPath.path"
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
literal|"You must enter a groupId."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"groupId"
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
literal|"You must enter an artifactId."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"artifactId"
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
literal|"You must enter a version."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"version"
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
literal|"You must enter a type."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"type"
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
name|LegacyArtifactPath
name|legacyArtifactPath
init|=
name|createLegacyArtifactPath
argument_list|(
name|LEGACY_ARTIFACT_PATH_PATH_INVALID_INPUT
argument_list|)
decl_stmt|;
name|populateAddLegacyArtifactPathActionFields
argument_list|(
name|addLegacyArtifactPathAction
argument_list|,
name|legacyArtifactPath
argument_list|,
name|GROUP_ID_INVALID_INPUT
argument_list|,
name|ARTIFACT_ID_INVALID_INPUT
argument_list|,
name|VERSION_INVALID_INPUT
argument_list|,
name|CLASSIFIER_INVALID_INPUT
argument_list|,
name|TYPE_INVALID_INPUT
argument_list|)
expr_stmt|;
comment|// test
name|actionValidatorManager
operator|.
name|validate
argument_list|(
name|addLegacyArtifactPathAction
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// verify
name|assertTrue
argument_list|(
name|addLegacyArtifactPathAction
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
name|addLegacyArtifactPathAction
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
literal|"Legacy path must only contain alphanumeric characters, forward-slashes(/), back-slashes(\\), underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"legacyArtifactPath.path"
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
literal|"Group id must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"groupId"
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
literal|"Artifact id must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"artifactId"
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
literal|"Version must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"version"
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
literal|"Classifier must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"classifier"
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
literal|"Type must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
expr_stmt|;
name|expectedFieldErrors
operator|.
name|put
argument_list|(
literal|"type"
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
name|LegacyArtifactPath
name|legacyArtifactPath
init|=
name|createLegacyArtifactPath
argument_list|(
name|LEGACY_ARTIFACT_PATH_PATH_VALID_INPUT
argument_list|)
decl_stmt|;
name|populateAddLegacyArtifactPathActionFields
argument_list|(
name|addLegacyArtifactPathAction
argument_list|,
name|legacyArtifactPath
argument_list|,
name|GROUP_ID_VALID_INPUT
argument_list|,
name|ARTIFACT_ID_VALID_INPUT
argument_list|,
name|VERSION_VALID_INPUT
argument_list|,
name|CLASSIFIER_VALID_INPUT
argument_list|,
name|TYPE_VALID_INPUT
argument_list|)
expr_stmt|;
comment|// test
name|actionValidatorManager
operator|.
name|validate
argument_list|(
name|addLegacyArtifactPathAction
argument_list|,
name|EMPTY_STRING
argument_list|)
expr_stmt|;
comment|// verify
name|assertFalse
argument_list|(
name|addLegacyArtifactPathAction
operator|.
name|hasFieldErrors
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|LegacyArtifactPath
name|createLegacyArtifactPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|LegacyArtifactPath
name|legacyArtifactPath
init|=
operator|new
name|LegacyArtifactPath
argument_list|()
decl_stmt|;
name|legacyArtifactPath
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
name|legacyArtifactPath
return|;
block|}
specifier|private
name|void
name|populateAddLegacyArtifactPathActionFields
parameter_list|(
name|AddLegacyArtifactPathAction
name|addLegacyArtifactPathAction
parameter_list|,
name|LegacyArtifactPath
name|legacyArtifactPath
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
name|addLegacyArtifactPathAction
operator|.
name|setLegacyArtifactPath
argument_list|(
name|legacyArtifactPath
argument_list|)
expr_stmt|;
name|addLegacyArtifactPathAction
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|addLegacyArtifactPathAction
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|addLegacyArtifactPathAction
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|addLegacyArtifactPathAction
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
name|addLegacyArtifactPathAction
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

