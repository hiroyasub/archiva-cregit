begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|validation
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|components
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ErrorKeys
block|{
name|String
name|ISNULL
init|=
literal|"isnull"
decl_stmt|;
name|String
name|ISEMPTY
init|=
literal|"empty"
decl_stmt|;
name|String
name|EXISTS
init|=
literal|"exists"
decl_stmt|;
name|String
name|MANAGED_REPOSITORY_EXISTS
init|=
literal|"managed_repo_exists"
decl_stmt|;
name|String
name|REMOTE_REPOSITORY_EXISTS
init|=
literal|"remote_repo_exists"
decl_stmt|;
name|String
name|REPOSITORY_GROUP_EXISTS
init|=
literal|"group_exists"
decl_stmt|;
name|String
name|MAX_LENGTH_EXCEEDED
init|=
literal|"max_length"
decl_stmt|;
name|String
name|INVALID_CHARS
init|=
literal|"invalid_chars"
decl_stmt|;
name|String
name|BELOW_MIN
init|=
literal|"min"
decl_stmt|;
name|String
name|INVALID_SCHEDULING_EXPRESSION
init|=
literal|"scheduling_exp_invalid"
decl_stmt|;
name|String
name|INVALID_LOCATION
init|=
literal|"location_invalid"
decl_stmt|;
block|}
end_interface

end_unit

