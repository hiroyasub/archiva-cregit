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
name|repository
operator|.
name|Repository
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
name|repository
operator|.
name|RepositoryRegistry
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
name|repository
operator|.
name|RepositoryType
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * A repository validator validates given repository data against certain rules.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryValidator
parameter_list|<
name|R
extends|extends
name|Repository
parameter_list|>
extends|extends
name|RepositoryChecker
argument_list|<
name|R
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
argument_list|>
argument_list|>
extends|,
name|Comparable
argument_list|<
name|RepositoryValidator
argument_list|<
name|R
argument_list|>
argument_list|>
block|{
name|String
name|REPOSITORY_ID_VALID_EXPRESSION
init|=
literal|"^[a-zA-Z0-9._-]+$"
decl_stmt|;
name|String
index|[]
name|REPOSITORY_ID_ALLOWED
init|=
operator|new
name|String
index|[]
block|{
literal|"alphanumeric, '.', '-','_'"
block|}
decl_stmt|;
name|String
name|REPOSITORY_NAME_VALID_EXPRESSION
init|=
literal|"^([a-zA-Z0-9.)/_(-]|\\s)+$"
decl_stmt|;
name|String
index|[]
name|REPOSITORY_NAME_ALLOWED
init|=
operator|new
name|String
index|[]
block|{
literal|"alphanumeric"
block|,
literal|"whitespace"
block|,
literal|"/"
block|,
literal|"("
block|,
literal|")"
block|,
literal|"_"
block|,
literal|"."
block|,
literal|"-"
block|}
decl_stmt|;
name|String
name|REPOSITORY_LOCATION_VALID_EXPRESSION
init|=
literal|"^[-a-zA-Z0-9._/~:?!&amp;=\\\\]+$"
decl_stmt|;
name|int
name|DEFAULT_PRIORITY
init|=
literal|1000
decl_stmt|;
comment|/**      * Returns the repository type for which this validator can be used. If the validator is applicable      * to all types, it should return {@link RepositoryType#ALL}      *      * @return the repository type for which this validator is applicable      */
specifier|default
name|RepositoryType
name|getType
parameter_list|()
block|{
return|return
name|RepositoryType
operator|.
name|ALL
return|;
block|}
comment|/**      * Returns the priority of this validator. Smaller values mean higher priority.      * All common validators have priority {@link #DEFAULT_PRIORITY}      *      * Validators are called in numerical order of their priority.      *      * @return      */
specifier|default
name|int
name|getPriority
parameter_list|()
block|{
return|return
name|DEFAULT_PRIORITY
return|;
block|}
comment|/**      * Orders by priority      *      * @see Comparable#compareTo(Object)      */
annotation|@
name|Override
specifier|default
name|int
name|compareTo
parameter_list|(
name|RepositoryValidator
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|this
operator|.
name|getPriority
argument_list|( )
operator|-
name|o
operator|.
name|getPriority
argument_list|( )
return|;
block|}
block|}
comment|/**      * Sets the repository registry to the given instance.      * @param repositoryRegistry the repository registry      */
name|void
name|setRepositoryRegistry
parameter_list|(
name|RepositoryRegistry
name|repositoryRegistry
parameter_list|)
function_decl|;
name|Class
argument_list|<
name|R
argument_list|>
name|getFlavour
parameter_list|()
function_decl|;
specifier|default
name|boolean
name|isFlavour
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|getFlavour
argument_list|( )
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|default
parameter_list|<
name|RR
extends|extends
name|Repository
parameter_list|>
name|RepositoryValidator
argument_list|<
name|RR
argument_list|>
name|narrowTo
parameter_list|(
name|Class
argument_list|<
name|RR
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|isFlavour
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|RepositoryValidator
argument_list|<
name|RR
argument_list|>
operator|)
name|this
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not narrow to "
operator|+
name|clazz
argument_list|)
throw|;
block|}
block|}
block|}
end_interface

end_unit

