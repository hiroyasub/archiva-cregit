begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * A reference to another (unversioned) Project.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|ProjectReference
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
comment|/**      *       *             The Group ID of the project reference.      *                 */
specifier|private
name|String
name|groupId
decl_stmt|;
comment|/**      *       *             The Artifact ID of the project reference.      *                 */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the Artifact ID of the project reference.      *       * @return String      */
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|this
operator|.
name|artifactId
return|;
block|}
comment|//-- String getArtifactId()
comment|/**      * Get the Group ID of the project reference.      *       * @return String      */
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|this
operator|.
name|groupId
return|;
block|}
comment|//-- String getGroupId()
comment|/**      * Set the Artifact ID of the project reference.      *       * @param artifactId      */
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
comment|//-- void setArtifactId( String )
comment|/**      * Set the Group ID of the project reference.      *       * @param groupId      */
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
comment|//-- void setGroupId( String )
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8947981859537138991L
decl_stmt|;
specifier|private
specifier|static
name|String
name|defaultString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|value
operator|.
name|trim
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|toKey
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
block|{
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
name|defaultString
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|defaultString
argument_list|(
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit
