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
comment|/**  *   *         The organisation information settings.  *         *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|OrganisationInformation
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
comment|/**      * name of the organisation.      */
specifier|private
name|String
name|name
decl_stmt|;
comment|/**      * name of the organisation.      */
specifier|private
name|String
name|url
decl_stmt|;
comment|/**      * name of the organisation.      */
specifier|private
name|String
name|logoLocation
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get name of the organisation.      *       * @return String      */
specifier|public
name|String
name|getLogoLocation
parameter_list|()
block|{
return|return
name|this
operator|.
name|logoLocation
return|;
block|}
comment|//-- String getLogoLocation()
comment|/**      * Get name of the organisation.      *       * @return String      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
comment|//-- String getName()
comment|/**      * Get name of the organisation.      *       * @return String      */
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
return|;
block|}
comment|//-- String getUrl()
comment|/**      * Set name of the organisation.      *       * @param logoLocation      */
specifier|public
name|void
name|setLogoLocation
parameter_list|(
name|String
name|logoLocation
parameter_list|)
block|{
name|this
operator|.
name|logoLocation
operator|=
name|logoLocation
expr_stmt|;
block|}
comment|//-- void setLogoLocation( String )
comment|/**      * Set name of the organisation.      *       * @param name      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|//-- void setName( String )
comment|/**      * Set name of the organisation.      *       * @param url      */
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
comment|//-- void setUrl( String )
block|}
end_class

end_unit

