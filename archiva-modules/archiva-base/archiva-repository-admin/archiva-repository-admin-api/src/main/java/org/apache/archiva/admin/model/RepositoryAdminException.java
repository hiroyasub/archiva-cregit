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
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

begin_comment
comment|/**  *  * Base exception class for the admin interfaces. Exceptions should set keys that allows identifying and classifying the error.  *  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryAdminException
extends|extends
name|Exception
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|bundle
init|=
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
literal|"org.apache.archiva.admin.model.error.AdminErrors"
argument_list|,
name|Locale
operator|.
name|ROOT
argument_list|)
decl_stmt|;
comment|/**      * can return the field name of bean with issue      * can be<code>null</code>      * @since 1.4-M3      */
specifier|private
name|String
name|fieldName
decl_stmt|;
comment|/**      * A unique identifier of this error      * @since 3.0      */
specifier|private
name|String
name|key
decl_stmt|;
specifier|private
name|boolean
name|keyExists
init|=
literal|false
decl_stmt|;
comment|/**      * Message parameters      */
name|String
index|[]
name|parameters
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
specifier|protected
specifier|static
name|String
name|getMessage
parameter_list|(
name|String
name|key
parameter_list|,
name|String
index|[]
name|params
parameter_list|)
block|{
return|return
name|MessageFormat
operator|.
name|format
argument_list|(
name|bundle
operator|.
name|getString
argument_list|(
name|key
argument_list|)
argument_list|,
name|params
argument_list|)
return|;
block|}
comment|/**      * Tries to retrieve a message from the bundle for the given key and returns the      * exception.      * @param key the identifier of the error      * @param params parameters for translating the message      * @return the exception      */
specifier|public
specifier|static
name|RepositoryAdminException
name|ofKey
parameter_list|(
name|String
name|key
parameter_list|,
name|String
modifier|...
name|params
parameter_list|)
block|{
name|String
name|message
init|=
name|getMessage
argument_list|(
name|key
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|RepositoryAdminException
name|ex
init|=
operator|new
name|RepositoryAdminException
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|ex
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
return|return
name|ex
return|;
block|}
comment|/**      * Tries to retrieve a message from the bundle for the given key and returns the      * exception.      * @param key the identifier of the error      * @param cause the exception that caused the error      * @param params parameters for translating the message      * @return the exception      */
specifier|public
specifier|static
name|RepositoryAdminException
name|ofKey
parameter_list|(
name|String
name|key
parameter_list|,
name|Throwable
name|cause
parameter_list|,
name|String
modifier|...
name|params
parameter_list|)
block|{
name|String
name|message
init|=
name|getMessage
argument_list|(
name|key
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|RepositoryAdminException
name|ex
init|=
operator|new
name|RepositoryAdminException
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
decl_stmt|;
name|ex
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
return|return
name|ex
return|;
block|}
comment|/**      * Tries to retrieve a message from the bundle for the given key and the given field and returns the      * exception.      * @param key the identifier of the error      * @param fieldName the field this exception is for      * @param params parameters for translating the message      * @return the exception      */
specifier|public
specifier|static
name|RepositoryAdminException
name|ofKeyAndField
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
modifier|...
name|params
parameter_list|)
block|{
name|String
name|message
init|=
name|getMessage
argument_list|(
name|key
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|RepositoryAdminException
name|ex
init|=
operator|new
name|RepositoryAdminException
argument_list|(
name|message
argument_list|,
name|fieldName
argument_list|)
decl_stmt|;
name|ex
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
return|return
name|ex
return|;
block|}
comment|/**      * Tries to retrieve a message from the bundle for the given key and the given field and returns the      * exception.      * @param key the identifier of the error      * @param fieldName the field this exception is for      * @param cause the exception that caused this error      * @param params parameters for translating the message      * @return the exception      */
specifier|public
specifier|static
name|RepositoryAdminException
name|ofKeyAndField
parameter_list|(
name|String
name|key
parameter_list|,
name|Throwable
name|cause
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
modifier|...
name|params
parameter_list|)
block|{
name|String
name|message
init|=
name|getMessage
argument_list|(
name|key
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|RepositoryAdminException
name|ex
init|=
operator|new
name|RepositoryAdminException
argument_list|(
name|message
argument_list|,
name|cause
argument_list|,
name|fieldName
argument_list|)
decl_stmt|;
name|ex
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setFieldName
argument_list|(
name|fieldName
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
return|return
name|ex
return|;
block|}
specifier|public
name|RepositoryAdminException
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|super
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RepositoryAdminException
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
name|this
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|this
operator|.
name|fieldName
operator|=
name|fieldName
expr_stmt|;
block|}
specifier|public
name|RepositoryAdminException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RepositoryAdminException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|fieldName
operator|=
name|fieldName
expr_stmt|;
block|}
specifier|public
name|String
name|getFieldName
parameter_list|()
block|{
return|return
name|fieldName
return|;
block|}
specifier|public
name|void
name|setFieldName
parameter_list|(
name|String
name|fieldName
parameter_list|)
block|{
name|this
operator|.
name|fieldName
operator|=
name|fieldName
expr_stmt|;
block|}
specifier|public
name|String
name|getKey
parameter_list|( )
block|{
return|return
name|key
return|;
block|}
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|keyExists
operator|=
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
specifier|public
name|boolean
name|keyExists
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyExists
return|;
block|}
specifier|public
name|String
index|[]
name|getParameters
parameter_list|( )
block|{
return|return
name|parameters
return|;
block|}
specifier|public
name|void
name|setParameters
parameter_list|(
name|String
index|[]
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|parameters
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|parameters
operator|=
operator|new
name|String
index|[
literal|0
index|]
expr_stmt|;
block|}
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
block|}
end_class

end_unit

