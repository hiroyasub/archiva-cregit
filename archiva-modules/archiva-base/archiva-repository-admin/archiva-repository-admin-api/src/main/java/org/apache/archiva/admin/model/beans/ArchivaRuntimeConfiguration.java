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
operator|.
name|beans
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
comment|/**  * @author Olivier Lamy  * @since 1.4-M4  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"archivaRuntimeConfiguration"
argument_list|)
specifier|public
class|class
name|ArchivaRuntimeConfiguration
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|userManagerImpl
init|=
literal|"jdo"
decl_stmt|;
specifier|public
name|ArchivaRuntimeConfiguration
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|String
name|getUserManagerImpl
parameter_list|()
block|{
return|return
name|userManagerImpl
return|;
block|}
specifier|public
name|void
name|setUserManagerImpl
parameter_list|(
name|String
name|userManagerImpl
parameter_list|)
block|{
name|this
operator|.
name|userManagerImpl
operator|=
name|userManagerImpl
expr_stmt|;
block|}
block|}
end_class

end_unit

