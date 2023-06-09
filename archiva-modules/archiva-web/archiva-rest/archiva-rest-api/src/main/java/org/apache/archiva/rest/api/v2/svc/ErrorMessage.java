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
name|v2
operator|.
name|svc
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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

begin_comment
comment|/**  * @author Olivier Lamy  * @author Martin Stockhammer  * @since 3.0  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"errorMessage"
argument_list|)
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"ErrorMessage"
argument_list|,
name|description
operator|=
literal|"Information about the error, that occurred while processing the REST request."
argument_list|)
specifier|public
class|class
name|ErrorMessage
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|errorKey
init|=
literal|""
decl_stmt|;
specifier|private
name|String
index|[]
name|args
init|=
name|EMPTY
decl_stmt|;
specifier|private
name|String
name|message
init|=
literal|""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|EMPTY
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
specifier|public
name|ErrorMessage
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ErrorMessage
parameter_list|(
name|String
name|errorKey
parameter_list|)
block|{
name|this
operator|.
name|errorKey
operator|=
name|errorKey
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|EMPTY
expr_stmt|;
block|}
specifier|public
name|ErrorMessage
parameter_list|(
name|String
name|errorKey
parameter_list|,
name|String
index|[]
name|args
parameter_list|)
block|{
name|this
operator|.
name|errorKey
operator|=
name|errorKey
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
specifier|public
specifier|static
name|ErrorMessage
name|of
parameter_list|(
name|String
name|errorKey
parameter_list|,
name|String
modifier|...
name|args
parameter_list|)
block|{
return|return
operator|new
name|ErrorMessage
argument_list|(
name|errorKey
argument_list|,
name|args
argument_list|)
return|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"error_key"
argument_list|,
name|description
operator|=
literal|"The key of the error message. If this is empty, the message message must be set."
argument_list|)
specifier|public
name|String
name|getErrorKey
parameter_list|()
block|{
return|return
name|errorKey
return|;
block|}
specifier|public
name|void
name|setErrorKey
parameter_list|(
name|String
name|errorKey
parameter_list|)
block|{
name|this
operator|.
name|errorKey
operator|=
name|errorKey
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Parameters that can be filled to the translated error message"
argument_list|)
specifier|public
name|String
index|[]
name|getArgs
parameter_list|()
block|{
return|return
name|args
return|;
block|}
specifier|public
name|void
name|setArgs
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Full error message. Either additional to the key in the default language, or if the message is without key."
argument_list|)
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|ErrorMessage
name|message
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

