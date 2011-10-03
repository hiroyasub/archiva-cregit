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
literal|"appearance"
block|}
argument_list|,
name|dependsOnMethods
operator|=
block|{
literal|"testWithCorrectUsernamePassword"
block|}
argument_list|,
name|sequential
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|AppearanceTest
extends|extends
name|AbstractArchivaTest
block|{
specifier|public
name|void
name|testAddAppearanceEmptyValues
parameter_list|()
block|{
name|goToAppearancePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|addEditAppearance
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a name"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddAppearanceEmptyValues"
block|}
argument_list|)
specifier|public
name|void
name|testAddAppearanceInvalidValues
parameter_list|()
block|{
name|goToAppearancePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|addEditAppearance
argument_list|(
literal|"<>~+[ ]'\""
argument_list|,
literal|"/home/user/abcXYZ0129._/\\~:?!&=-<> ~+[ ]'\""
argument_list|,
literal|"/home/user/abcXYZ0129._/\\~:?!&=-<> ~+[ ]'\""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Organisation name must only contain alphanumeric characters, white-spaces(' '), equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a URL"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a URL for your logo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddAppearanceInvalidValues"
block|}
argument_list|)
specifier|public
name|void
name|testAddAppearanceInvalidOrganisationName
parameter_list|()
block|{
name|goToAppearancePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|addEditAppearance
argument_list|(
literal|"<>~+[ ]'\""
argument_list|,
literal|"http://www.apache.org/"
argument_list|,
literal|"http://www.apache.org/images/asf_logo_wide.gifs"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Organisation name must only contain alphanumeric characters, white-spaces(' '), equals(=), question-marks(?), exclamation-points(!), ampersands(&), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddAppearanceInvalidOrganisationUrl
parameter_list|()
block|{
name|goToAppearancePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|addEditAppearance
argument_list|(
literal|"The Apache Software Foundation"
argument_list|,
literal|"/home/user/abcXYZ0129._/\\~:?!&=-<> ~+[ ]'\""
argument_list|,
literal|"http://www.apache.org/images/asf_logo_wide.gifs"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a URL."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddAppearanceInvalidOrganisationLogo
parameter_list|()
block|{
name|goToAppearancePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|addEditAppearance
argument_list|(
literal|"The Apache Software Foundation"
argument_list|,
literal|"http://www.apache.org/"
argument_list|,
literal|"/home/user/abcXYZ0129._/\\~:?!&=-<> ~+[ ]'\""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"You must enter a URL for your logo."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddAppearanceValidValues
parameter_list|()
block|{
name|goToAppearancePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|addEditAppearance
argument_list|(
literal|"The Apache Software Foundation"
argument_list|,
literal|"http://www.apache.org/"
argument_list|,
literal|"http://www.apache.org/images/asf_logo_wide.gifs"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"The Apache Software Foundation"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddAppearanceValidValues"
block|}
argument_list|)
specifier|public
name|void
name|testEditAppearance
parameter_list|()
block|{
name|goToAppearancePage
argument_list|()
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|addEditAppearance
argument_list|(
literal|"Apache Software Foundation"
argument_list|,
literal|"http://www.apache.org/"
argument_list|,
literal|"http://www.apache.org/images/asf_logo_wide.gifs"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Apache Software Foundation"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

