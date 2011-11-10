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
name|services
operator|.
name|interceptors
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
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
name|rest
operator|.
name|services
operator|.
name|RestError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ExceptionMapper
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M2  */
end_comment

begin_class
annotation|@
name|Provider
annotation|@
name|Service
argument_list|(
literal|"archivaRestServiceExceptionMapper"
argument_list|)
specifier|public
class|class
name|ArchivaRestServiceExceptionMapper
implements|implements
name|ExceptionMapper
argument_list|<
name|ArchivaRestServiceException
argument_list|>
block|{
specifier|public
name|Response
name|toResponse
parameter_list|(
name|ArchivaRestServiceException
name|e
parameter_list|)
block|{
name|RestError
name|restError
init|=
operator|new
name|RestError
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|Response
operator|.
name|ResponseBuilder
name|responseBuilder
init|=
name|Response
operator|.
name|status
argument_list|(
name|e
operator|.
name|getHttpErrorCode
argument_list|()
argument_list|)
operator|.
name|entity
argument_list|(
name|restError
argument_list|)
decl_stmt|;
return|return
name|responseBuilder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

