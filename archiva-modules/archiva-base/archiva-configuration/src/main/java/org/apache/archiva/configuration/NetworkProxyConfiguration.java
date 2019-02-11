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
comment|/**  * Class NetworkProxyConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|NetworkProxyConfiguration
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
comment|/**      *       *             The ID for this proxy.      *                 */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      *       *             The network protocol to use with this proxy:      * "http", "socks-4"      *           .      */
specifier|private
name|String
name|protocol
init|=
literal|"http"
decl_stmt|;
comment|/**      *       *             The proxy host.      *                 */
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      *       *             The proxy port.      *                 */
specifier|private
name|int
name|port
init|=
literal|8080
decl_stmt|;
comment|/**      *       *             The proxy user.      *                 */
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      *       *             The proxy password.      *                 */
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      *       *             Use ntlm authentification.      *                 */
specifier|private
name|boolean
name|useNtlm
init|=
literal|false
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the proxy host.      *       * @return String      */
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|this
operator|.
name|host
return|;
block|}
comment|//-- String getHost()
comment|/**      * Get the ID for this proxy.      *       * @return String      */
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
comment|//-- String getId()
comment|/**      * Get the proxy password.      *       * @return String      */
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|this
operator|.
name|password
return|;
block|}
comment|//-- String getPassword()
comment|/**      * Get the proxy port.      *       * @return int      */
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|this
operator|.
name|port
return|;
block|}
comment|//-- int getPort()
comment|/**      * Get the network protocol to use with this proxy: "http",      * "socks-4".      *       * @return String      */
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|this
operator|.
name|protocol
return|;
block|}
comment|//-- String getProtocol()
comment|/**      * Get the proxy user.      *       * @return String      */
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|this
operator|.
name|username
return|;
block|}
comment|//-- String getUsername()
comment|/**      * Get use ntlm authentification.      *       * @return boolean      */
specifier|public
name|boolean
name|isUseNtlm
parameter_list|()
block|{
return|return
name|this
operator|.
name|useNtlm
return|;
block|}
comment|//-- boolean isUseNtlm()
comment|/**      * Set the proxy host.      *       * @param host      */
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
comment|//-- void setHost( String )
comment|/**      * Set the ID for this proxy.      *       * @param id      */
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|//-- void setId( String )
comment|/**      * Set the proxy password.      *       * @param password      */
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|//-- void setPassword( String )
comment|/**      * Set the proxy port.      *       * @param port      */
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
comment|//-- void setPort( int )
comment|/**      * Set the network protocol to use with this proxy: "http",      * "socks-4".      *       * @param protocol      */
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
comment|//-- void setProtocol( String )
comment|/**      * Set use ntlm authentification.      *       * @param useNtlm      */
specifier|public
name|void
name|setUseNtlm
parameter_list|(
name|boolean
name|useNtlm
parameter_list|)
block|{
name|this
operator|.
name|useNtlm
operator|=
name|useNtlm
expr_stmt|;
block|}
comment|//-- void setUseNtlm( boolean )
comment|/**      * Set the proxy user.      *       * @param username      */
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
comment|//-- void setUsername( String )
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|17
decl_stmt|;
name|result
operator|=
literal|37
operator|*
name|result
operator|+
operator|(
name|id
operator|!=
literal|null
condition|?
name|id
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|other
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|other
operator|instanceof
name|NetworkProxyConfiguration
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|NetworkProxyConfiguration
name|that
init|=
operator|(
name|NetworkProxyConfiguration
operator|)
name|other
decl_stmt|;
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|result
operator|=
name|result
operator|&&
operator|(
name|getId
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getId
argument_list|()
operator|==
literal|null
else|:
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getId
argument_list|()
argument_list|)
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

