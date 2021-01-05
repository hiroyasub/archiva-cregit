begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|v2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Schema
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
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer  * @since 3.0  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"archivaRestError"
argument_list|)
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"ArchivaRestError"
argument_list|,
name|description
operator|=
literal|"Contains a list of error messages that resulted from the current REST call"
argument_list|)
specifier|public
class|class
name|ArchivaRestError
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8892617571273167067L
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ErrorMessage
argument_list|>
name|errorMessages
init|=
operator|new
name|ArrayList
argument_list|<
name|ErrorMessage
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|public
name|ArchivaRestError
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ArchivaRestError
parameter_list|(
name|ArchivaRestServiceException
name|e
parameter_list|)
block|{
name|errorMessages
operator|.
name|addAll
argument_list|(
name|e
operator|.
name|getErrorMessages
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|.
name|getErrorMessages
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|errorMessages
operator|.
name|add
argument_list|(
operator|new
name|ErrorMessage
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"error_messages"
argument_list|,
name|description
operator|=
literal|"The list of errors that occurred while processing the REST request"
argument_list|)
specifier|public
name|List
argument_list|<
name|ErrorMessage
argument_list|>
name|getErrorMessages
parameter_list|()
block|{
return|return
name|errorMessages
return|;
block|}
specifier|public
name|void
name|setErrorMessages
parameter_list|(
name|List
argument_list|<
name|ErrorMessage
argument_list|>
name|errorMessages
parameter_list|)
block|{
name|this
operator|.
name|errorMessages
operator|=
name|errorMessages
expr_stmt|;
block|}
specifier|public
name|void
name|addErrorMessage
parameter_list|(
name|ErrorMessage
name|errorMessage
parameter_list|)
block|{
name|this
operator|.
name|errorMessages
operator|.
name|add
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

