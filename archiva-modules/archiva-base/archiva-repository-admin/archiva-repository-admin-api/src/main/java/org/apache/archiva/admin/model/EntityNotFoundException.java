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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * This exception is thrown, if a requested entity does not exist.  *  * @author Martin Stockhammer<martin_s@apache.org>  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|EntityNotFoundException
extends|extends
name|RepositoryAdminException
block|{
specifier|public
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"entity.not_found"
decl_stmt|;
specifier|public
specifier|static
name|EntityNotFoundException
name|of
parameter_list|(
name|String
modifier|...
name|parameters
parameter_list|)
block|{
name|String
name|message
init|=
name|getMessage
argument_list|(
name|KEY
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
return|return
operator|new
name|EntityNotFoundException
argument_list|(
name|message
argument_list|,
name|parameters
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|EntityNotFoundException
name|ofMessage
parameter_list|(
name|String
name|message
parameter_list|,
name|String
modifier|...
name|parameters
parameter_list|)
block|{
return|return
operator|new
name|EntityNotFoundException
argument_list|(
name|message
argument_list|,
name|parameters
argument_list|)
return|;
block|}
specifier|public
name|EntityNotFoundException
parameter_list|(
name|String
name|s
parameter_list|,
name|String
modifier|...
name|parameters
parameter_list|)
block|{
name|super
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|setKey
argument_list|(
name|KEY
argument_list|)
expr_stmt|;
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EntityNotFoundException
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
modifier|...
name|parameters
parameter_list|)
block|{
name|super
argument_list|(
name|s
argument_list|,
name|fieldName
argument_list|)
expr_stmt|;
name|setKey
argument_list|(
name|KEY
argument_list|)
expr_stmt|;
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EntityNotFoundException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|,
name|String
modifier|...
name|parameters
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|setKey
argument_list|(
name|KEY
argument_list|)
expr_stmt|;
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EntityNotFoundException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
modifier|...
name|parameters
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|,
name|fieldName
argument_list|)
expr_stmt|;
name|setKey
argument_list|(
name|KEY
argument_list|)
expr_stmt|;
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

