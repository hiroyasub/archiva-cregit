begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
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
name|AbstractRepository
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
name|ManagedRepository
import|;
end_import

begin_comment
comment|/**  * apply basic repository validation : id and name.  * Check if already exists.  *  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryCommonValidator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_ID_VALID_EXPRESSION
init|=
literal|"^[a-zA-Z0-9._-]+$"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_NAME_VALID_EXPRESSION
init|=
literal|"^([a-zA-Z0-9.)/_(-]|\\s)+$"
decl_stmt|;
name|void
name|basicValidation
parameter_list|(
name|AbstractRepository
name|abstractRepository
parameter_list|,
name|boolean
name|update
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * validate cronExpression and location format      *      * @param managedRepository      * @since 1.4-M2      */
name|void
name|validateManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
comment|/**      * replace some interpolations ${appserver.base} with correct values      *      * @param directory      * @return      */
name|String
name|removeExpressions
parameter_list|(
name|String
name|directory
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

