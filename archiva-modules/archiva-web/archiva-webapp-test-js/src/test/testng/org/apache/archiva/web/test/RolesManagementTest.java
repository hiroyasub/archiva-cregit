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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Test
argument_list|(
name|groups
operator|=
block|{
literal|"usermanagement"
block|}
argument_list|,
name|dependsOnGroups
operator|=
literal|"about"
argument_list|)
specifier|public
class|class
name|RolesManagementTest
extends|extends
name|AbstractArchivaTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReadRolesAndUpdateDescription
parameter_list|()
throws|throws
name|Exception
block|{
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
name|clickLinkWithLocator
argument_list|(
literal|"menu-roles-list-a"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Archiva System Administrator "
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getText
argument_list|(
literal|"role-description-Guest"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|clickLinkWithLocator
argument_list|(
literal|"edit-role-Guest"
argument_list|)
expr_stmt|;
name|String
name|desc
init|=
literal|"The guest description"
decl_stmt|;
name|setFieldValue
argument_list|(
literal|"role-edit-description"
argument_list|,
name|desc
argument_list|)
expr_stmt|;
name|clickButtonWithLocator
argument_list|(
literal|"role-edit-description-save"
argument_list|)
expr_stmt|;
name|clickLinkWithLocator
argument_list|(
literal|"roles-view-tabs-a-roles-grid"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|desc
argument_list|,
name|getText
argument_list|(
literal|"role-description-Guest"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

