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
comment|/**  *   *         The network configuration for external http request to  * repositories.  *         *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|NetworkConfiguration
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
comment|/**      * maximum total external http connections.      */
specifier|private
name|int
name|maxTotal
init|=
literal|30
decl_stmt|;
comment|/**      * maximum total external http connections per host.      */
specifier|private
name|int
name|maxTotalPerHost
init|=
literal|30
decl_stmt|;
comment|/**      * use or not http connection pooling default true.      */
specifier|private
name|boolean
name|usePooling
init|=
literal|true
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get maximum total external http connections.      *       * @return int      */
specifier|public
name|int
name|getMaxTotal
parameter_list|()
block|{
return|return
name|this
operator|.
name|maxTotal
return|;
block|}
comment|//-- int getMaxTotal()
comment|/**      * Get maximum total external http connections per host.      *       * @return int      */
specifier|public
name|int
name|getMaxTotalPerHost
parameter_list|()
block|{
return|return
name|this
operator|.
name|maxTotalPerHost
return|;
block|}
comment|//-- int getMaxTotalPerHost()
comment|/**      * Get use or not http connection pooling default true.      *       * @return boolean      */
specifier|public
name|boolean
name|isUsePooling
parameter_list|()
block|{
return|return
name|this
operator|.
name|usePooling
return|;
block|}
comment|//-- boolean isUsePooling()
comment|/**      * Set maximum total external http connections.      *       * @param maxTotal      */
specifier|public
name|void
name|setMaxTotal
parameter_list|(
name|int
name|maxTotal
parameter_list|)
block|{
name|this
operator|.
name|maxTotal
operator|=
name|maxTotal
expr_stmt|;
block|}
comment|//-- void setMaxTotal( int )
comment|/**      * Set maximum total external http connections per host.      *       * @param maxTotalPerHost      */
specifier|public
name|void
name|setMaxTotalPerHost
parameter_list|(
name|int
name|maxTotalPerHost
parameter_list|)
block|{
name|this
operator|.
name|maxTotalPerHost
operator|=
name|maxTotalPerHost
expr_stmt|;
block|}
comment|//-- void setMaxTotalPerHost( int )
comment|/**      * Set use or not http connection pooling default true.      *       * @param usePooling      */
specifier|public
name|void
name|setUsePooling
parameter_list|(
name|boolean
name|usePooling
parameter_list|)
block|{
name|this
operator|.
name|usePooling
operator|=
name|usePooling
expr_stmt|;
block|}
comment|//-- void setUsePooling( boolean )
block|}
end_class

end_unit

