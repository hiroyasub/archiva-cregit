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
name|AbstractRepositoryTest
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
literal|"reposcan"
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
name|RepositoryScanningTest
extends|extends
name|AbstractRepositoryTest
block|{
specifier|public
name|void
name|testAddArtifactFileType_NullValue
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_0"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Unable to process blank pattern."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddArtifactFileType
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_0"
argument_list|,
literal|"**/*.dll"
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_0"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
literal|"//div[@id='contentArea']/div/div[1]/table.13.0"
argument_list|)
argument_list|,
literal|"**/*.dll"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactFileType"
block|}
argument_list|)
specifier|public
name|void
name|testAddArtifactFileType_ExistingValue
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_0"
argument_list|,
literal|"**/*.zip"
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_0"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getErrorMessageText
argument_list|()
argument_list|,
literal|"File type [artifacts] already contains pattern [**/*.zip]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddArtifactFileType"
block|}
argument_list|)
specifier|public
name|void
name|testDeleteArtifactFileType
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"//div[@id='contentArea']/div/div/table/tbody/tr[14]/td/code"
decl_stmt|;
name|assertElementPresent
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getText
argument_list|(
name|path
argument_list|)
argument_list|,
literal|"**/*.dll"
argument_list|)
expr_stmt|;
name|clickDeleteIcon
argument_list|(
literal|"**/*.dll"
argument_list|)
expr_stmt|;
name|assertElementNotPresent
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testDeleteArtifactFileType"
block|}
argument_list|)
specifier|public
name|void
name|testAddAutoRemove_NullValue
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_1"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_1"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Unable to process blank pattern."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddAutoRemove_NullValue"
block|}
argument_list|)
specifier|public
name|void
name|testAddAutoRemove_ExistingValue
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_1"
argument_list|,
literal|"**/*-"
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_1"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getErrorMessageText
argument_list|()
argument_list|,
literal|"File type [auto-remove] already contains pattern [**/*-]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddAutoRemove_NullValue"
block|}
argument_list|)
specifier|public
name|void
name|testAddAutoRemove
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_1"
argument_list|,
literal|"**/*.test"
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_1"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
literal|"//div[@id='contentArea']/div/div[2]/table.3.0"
argument_list|)
argument_list|,
literal|"**/*.test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddAutoRemove"
block|}
argument_list|)
specifier|public
name|void
name|testDeleteAutoRemove
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"//div[@id='contentArea']/div/div[2]/table/tbody/tr[4]/td/code"
decl_stmt|;
name|assertElementPresent
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getText
argument_list|(
name|path
argument_list|)
argument_list|,
literal|"**/*.test"
argument_list|)
expr_stmt|;
name|clickDeleteIcon
argument_list|(
literal|"**/*.test"
argument_list|)
expr_stmt|;
name|assertElementNotPresent
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testDeleteAutoRemove"
block|}
argument_list|)
specifier|public
name|void
name|testAddIgnoredArtifacts_NullValue
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_2"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_2"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getErrorMessageText
argument_list|()
argument_list|,
literal|"Unable to process blank pattern."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddIgnoredArtifacts_ExistingValue
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_2"
argument_list|,
literal|"**/*.sh"
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_2"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getErrorMessageText
argument_list|()
argument_list|,
literal|"File type [ignored] already contains pattern [**/*.sh]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddIgnoredArtifacts
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_2"
argument_list|,
literal|"**/*.log"
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_2"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
literal|"//div[@id='contentArea']/div/div[3]/table.6.0"
argument_list|)
argument_list|,
literal|"**/*.log"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddIgnoredArtifacts"
block|}
argument_list|)
specifier|public
name|void
name|testDeleteIgnoredArtifacts
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|String
name|pattern
init|=
literal|"**/*.log"
decl_stmt|;
name|String
name|path
init|=
literal|"//div[@id='contentArea']/div/div[3]/table/tbody/tr[7]/td/code"
decl_stmt|;
name|assertElementPresent
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getText
argument_list|(
name|path
argument_list|)
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|clickDeleteIcon
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|assertElementNotPresent
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|//
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testDeleteIgnoredArtifacts"
block|}
argument_list|)
specifier|public
name|void
name|testAddIndexableContent_NullValue
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_3"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_3"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getErrorMessageText
argument_list|()
argument_list|,
literal|"Unable to process blank pattern."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddIndexableContent_ExistingValue
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_3"
argument_list|,
literal|"**/*.xml"
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_3"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getErrorMessageText
argument_list|()
argument_list|,
literal|"File type [indexable-content] already contains pattern [**/*.xml]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddIndexableContent
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|setFieldValue
argument_list|(
literal|"newpattern_3"
argument_list|,
literal|"**/*.html"
argument_list|)
expr_stmt|;
name|clickAddIcon
argument_list|(
literal|"newpattern_3"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getTable
argument_list|(
literal|"//div[@id='contentArea']/div/div[4]/table.9.0"
argument_list|)
argument_list|,
literal|"**/*.html"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testAddIndexableContent"
block|}
argument_list|)
specifier|public
name|void
name|testDeleteIndexableContent
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|String
name|pattern
init|=
literal|"**/*.html"
decl_stmt|;
name|String
name|path
init|=
literal|"//div[@id='contentArea']/div/div[4]/table/tbody/tr[10]/td/code"
decl_stmt|;
name|assertElementPresent
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getSelenium
argument_list|()
operator|.
name|getText
argument_list|(
name|path
argument_list|)
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|clickDeleteIcon
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|assertElementNotPresent
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testDeleteIndexableContent"
block|}
argument_list|)
specifier|public
name|void
name|testUpdateConsumers
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|checkField
argument_list|(
literal|"enabledKnownContentConsumers"
argument_list|)
expr_stmt|;
name|checkField
argument_list|(
literal|"//input[@name='enabledKnownContentConsumers' and @value='auto-rename']"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Update Consumers"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Repository Scanning"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|dependsOnMethods
operator|=
block|{
literal|"testUpdateConsumers"
block|}
argument_list|)
specifier|public
name|void
name|testUpdateConsumers_UnsetAll
parameter_list|()
block|{
name|goToRepositoryScanningPage
argument_list|()
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|uncheck
argument_list|(
literal|"enabledKnownContentConsumers"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|uncheck
argument_list|(
literal|"//input[@name='enabledKnownContentConsumers' and @value='auto-rename']"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|uncheck
argument_list|(
literal|"//input[@name='enabledKnownContentConsumers' and @value='create-missing-checksums']"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|uncheck
argument_list|(
literal|"//input[@name='enabledKnownContentConsumers' and @value='index-content']"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|uncheck
argument_list|(
literal|"//input[@name='enabledKnownContentConsumers' and @value='metadata-updater']"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|uncheck
argument_list|(
literal|"//input[@name='enabledKnownContentConsumers' and @value='repository-purge']"
argument_list|)
expr_stmt|;
name|getSelenium
argument_list|()
operator|.
name|uncheck
argument_list|(
literal|"//input[@name='enabledKnownContentConsumers' and @value='validate-checksums']"
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Update Consumers"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Apache Archiva \\ Administration - Repository Scanning"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|clickAddIcon
parameter_list|(
name|String
name|fieldId
parameter_list|)
block|{
name|String
name|xPath
init|=
literal|"//preceding::td/input[@id='"
operator|+
name|fieldId
operator|+
literal|"']//following::td/a/img"
decl_stmt|;
name|clickLinkWithLocator
argument_list|(
name|xPath
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|clickDeleteIcon
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|String
name|xPath
init|=
literal|"//preceding::td/code[contains(text(),'"
operator|+
name|pattern
operator|+
literal|"')]//following::td/a/img"
decl_stmt|;
name|clickLinkWithLocator
argument_list|(
name|xPath
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

