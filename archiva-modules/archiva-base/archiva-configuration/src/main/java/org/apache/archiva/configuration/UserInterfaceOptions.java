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
comment|/**  *   *         The user interface configuration settings.  *         *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|UserInterfaceOptions
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
comment|/**      * true if find artifacts should be enabled.      */
specifier|private
name|boolean
name|showFindArtifacts
init|=
literal|true
decl_stmt|;
comment|/**      * true if applet behavior for find artifacts should be enabled.      */
specifier|private
name|boolean
name|appletFindEnabled
init|=
literal|true
decl_stmt|;
comment|/**      * Field disableEasterEggs.      */
specifier|private
name|boolean
name|disableEasterEggs
init|=
literal|false
decl_stmt|;
comment|/**      * Field applicationUrl.      */
specifier|private
name|String
name|applicationUrl
decl_stmt|;
comment|/**      * Field disableRegistration.      */
specifier|private
name|boolean
name|disableRegistration
init|=
literal|false
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the applicationUrl field.      *       * @return String      */
specifier|public
name|String
name|getApplicationUrl
parameter_list|()
block|{
return|return
name|this
operator|.
name|applicationUrl
return|;
block|}
comment|//-- String getApplicationUrl()
comment|/**      * Get true if applet behavior for find artifacts should be      * enabled.      *       * @return boolean      */
specifier|public
name|boolean
name|isAppletFindEnabled
parameter_list|()
block|{
return|return
name|this
operator|.
name|appletFindEnabled
return|;
block|}
comment|//-- boolean isAppletFindEnabled()
comment|/**      * Get the disableEasterEggs field.      *       * @return boolean      */
specifier|public
name|boolean
name|isDisableEasterEggs
parameter_list|()
block|{
return|return
name|this
operator|.
name|disableEasterEggs
return|;
block|}
comment|//-- boolean isDisableEasterEggs()
comment|/**      * Get the disableRegistration field.      *       * @return boolean      */
specifier|public
name|boolean
name|isDisableRegistration
parameter_list|()
block|{
return|return
name|this
operator|.
name|disableRegistration
return|;
block|}
comment|//-- boolean isDisableRegistration()
comment|/**      * Get true if find artifacts should be enabled.      *       * @return boolean      */
specifier|public
name|boolean
name|isShowFindArtifacts
parameter_list|()
block|{
return|return
name|this
operator|.
name|showFindArtifacts
return|;
block|}
comment|//-- boolean isShowFindArtifacts()
comment|/**      * Set true if applet behavior for find artifacts should be      * enabled.      *       * @param appletFindEnabled      */
specifier|public
name|void
name|setAppletFindEnabled
parameter_list|(
name|boolean
name|appletFindEnabled
parameter_list|)
block|{
name|this
operator|.
name|appletFindEnabled
operator|=
name|appletFindEnabled
expr_stmt|;
block|}
comment|//-- void setAppletFindEnabled( boolean )
comment|/**      * Set the applicationUrl field.      *       * @param applicationUrl      */
specifier|public
name|void
name|setApplicationUrl
parameter_list|(
name|String
name|applicationUrl
parameter_list|)
block|{
name|this
operator|.
name|applicationUrl
operator|=
name|applicationUrl
expr_stmt|;
block|}
comment|//-- void setApplicationUrl( String )
comment|/**      * Set the disableEasterEggs field.      *       * @param disableEasterEggs      */
specifier|public
name|void
name|setDisableEasterEggs
parameter_list|(
name|boolean
name|disableEasterEggs
parameter_list|)
block|{
name|this
operator|.
name|disableEasterEggs
operator|=
name|disableEasterEggs
expr_stmt|;
block|}
comment|//-- void setDisableEasterEggs( boolean )
comment|/**      * Set the disableRegistration field.      *       * @param disableRegistration      */
specifier|public
name|void
name|setDisableRegistration
parameter_list|(
name|boolean
name|disableRegistration
parameter_list|)
block|{
name|this
operator|.
name|disableRegistration
operator|=
name|disableRegistration
expr_stmt|;
block|}
comment|//-- void setDisableRegistration( boolean )
comment|/**      * Set true if find artifacts should be enabled.      *       * @param showFindArtifacts      */
specifier|public
name|void
name|setShowFindArtifacts
parameter_list|(
name|boolean
name|showFindArtifacts
parameter_list|)
block|{
name|this
operator|.
name|showFindArtifacts
operator|=
name|showFindArtifacts
expr_stmt|;
block|}
comment|//-- void setShowFindArtifacts( boolean )
block|}
end_class

end_unit

