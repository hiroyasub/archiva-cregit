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
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Class SyncConnectorConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|SyncConnectorConfiguration
extends|extends
name|AbstractRepositoryConnectorConfiguration
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
comment|/**      * When to run the sync mechanism. Default is every hour on the      * hour.      */
specifier|private
name|String
name|cronExpression
init|=
literal|"0 0 * * * ?"
decl_stmt|;
comment|/**      * The type of synchronization to use.      */
specifier|private
name|String
name|method
init|=
literal|"rsync"
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get when to run the sync mechanism. Default is every hour on      * the hour.      *       * @return String      */
specifier|public
name|String
name|getCronExpression
parameter_list|()
block|{
return|return
name|this
operator|.
name|cronExpression
return|;
block|}
comment|//-- String getCronExpression()
comment|/**      * Get the type of synchronization to use.      *       * @return String      */
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|this
operator|.
name|method
return|;
block|}
comment|//-- String getMethod()
comment|/**      * Set when to run the sync mechanism. Default is every hour on      * the hour.      *       * @param cronExpression      */
specifier|public
name|void
name|setCronExpression
parameter_list|(
name|String
name|cronExpression
parameter_list|)
block|{
name|this
operator|.
name|cronExpression
operator|=
name|cronExpression
expr_stmt|;
block|}
comment|//-- void setCronExpression( String )
comment|/**      * Set the type of synchronization to use.      *       * @param method      */
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
comment|//-- void setMethod( String )
block|}
end_class

end_unit

