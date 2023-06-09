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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|UserManagementTest
extends|extends
name|AbstractArchivaTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testBasicAddDeleteUser
parameter_list|()
block|{
name|username
operator|=
name|getProperty
argument_list|(
literal|"GUEST_USERNAME"
argument_list|)
expr_stmt|;
name|fullname
operator|=
name|getProperty
argument_list|(
literal|"GUEST_FULLNAME"
argument_list|)
expr_stmt|;
name|createUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|,
name|getUserRolePassword
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|deleteUser
argument_list|(
name|username
argument_list|,
name|fullname
argument_list|,
name|getUserEmail
argument_list|()
argument_list|)
expr_stmt|;
name|logout
argument_list|()
expr_stmt|;
name|login
argument_list|(
name|getAdminUsername
argument_list|()
argument_list|,
name|getAdminPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

