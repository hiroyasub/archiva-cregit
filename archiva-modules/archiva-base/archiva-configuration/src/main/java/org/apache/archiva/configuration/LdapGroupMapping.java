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
comment|/**  * configuration of a LDAP group to Archiva roles.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|LdapGroupMapping
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
comment|/**      * LDAP Group.      */
specifier|private
name|String
name|group
decl_stmt|;
comment|/**      * Field roleNames.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|roleNames
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method addRoleName.      *       * @param string      */
specifier|public
name|void
name|addRoleName
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getRoleNames
argument_list|()
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|//-- void addRoleName( String )
comment|/**      * Get lDAP Group.      *       * @return String      */
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|this
operator|.
name|group
return|;
block|}
comment|//-- String getGroup()
comment|/**      * Method getRoleNames.      *       * @return List      */
specifier|public
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|getRoleNames
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|roleNames
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|roleNames
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|roleNames
return|;
block|}
comment|//-- java.util.List<String> getRoleNames()
comment|/**      * Method removeRoleName.      *       * @param string      */
specifier|public
name|void
name|removeRoleName
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getRoleNames
argument_list|()
operator|.
name|remove
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|//-- void removeRoleName( String )
comment|/**      * Set lDAP Group.      *       * @param group      */
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
comment|//-- void setGroup( String )
comment|/**      * Set archiva roles.      *       * @param roleNames      */
specifier|public
name|void
name|setRoleNames
parameter_list|(
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|roleNames
parameter_list|)
block|{
name|this
operator|.
name|roleNames
operator|=
name|roleNames
expr_stmt|;
block|}
comment|//-- void setRoleNames( java.util.List )
block|}
end_class

end_unit

