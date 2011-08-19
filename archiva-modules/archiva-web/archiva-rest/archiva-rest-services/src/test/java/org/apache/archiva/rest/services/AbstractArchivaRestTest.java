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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|AbstractRestServicesTest
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since TODO  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractArchivaRestTest
extends|extends
name|AbstractRestServicesTest
block|{
specifier|public
name|String
name|guestAuthzHeader
init|=
literal|"Basic "
operator|+
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|Base64Utility
operator|.
name|encode
argument_list|(
operator|(
literal|"guest"
operator|+
literal|":"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
name|getSpringConfigLocation
parameter_list|()
block|{
return|return
literal|"classpath*:META-INF/spring-context.xml,classpath:META-INF/spring-context-test.xml"
return|;
block|}
block|}
end_class

end_unit

