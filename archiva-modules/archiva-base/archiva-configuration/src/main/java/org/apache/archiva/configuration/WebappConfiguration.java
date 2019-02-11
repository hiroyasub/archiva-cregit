begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  *   *         The webapp configuration settings.  *         *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|WebappConfiguration
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      * options for altering the ui presentation.      */
specifier|private
name|UserInterfaceOptions
name|ui
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get options for altering the ui presentation.      *       * @return UserInterfaceOptions      */
specifier|public
name|UserInterfaceOptions
name|getUi
parameter_list|()
block|{
return|return
name|this
operator|.
name|ui
return|;
block|}
comment|//-- UserInterfaceOptions getUi()
comment|/**      * Set options for altering the ui presentation.      *       * @param ui      */
specifier|public
name|void
name|setUi
parameter_list|(
name|UserInterfaceOptions
name|ui
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
block|}
comment|//-- void setUi( UserInterfaceOptions )
block|}
end_class

end_unit

