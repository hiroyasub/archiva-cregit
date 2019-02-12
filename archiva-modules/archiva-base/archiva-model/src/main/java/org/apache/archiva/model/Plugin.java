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
comment|/**  * The Plugin.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|Plugin
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
comment|/**      *       *             The prefix for a plugin      *           .      */
specifier|private
name|String
name|prefix
decl_stmt|;
comment|/**      *       *             The artifactId for a plugin      *           .      */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|/**      *       *             The name for a plugin      *           .      */
specifier|private
name|String
name|name
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method equals.      *       * @param other      * @return boolean      */
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
name|Plugin
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Plugin
name|that
init|=
operator|(
name|Plugin
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
name|getArtifactId
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getArtifactId
argument_list|()
operator|==
literal|null
else|:
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|//-- boolean equals( Object )
comment|/**      * Get the artifactId for a plugin.      *       * @return String      */
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
comment|/**      * Get the name for a plugin.      *       * @return String      */
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
comment|/**      * Get the prefix for a plugin.      *       * @return String      */
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|this
operator|.
name|prefix
return|;
block|}
comment|//-- String getPrefix()
comment|/**      * Method hashCode.      *       * @return int      */
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
name|artifactId
operator|!=
literal|null
condition|?
name|artifactId
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
comment|//-- int hashCode()
comment|/**      * Set the artifactId for a plugin.      *       * @param artifactId      */
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
comment|/**      * Set the name for a plugin.      *       * @param name      */
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
comment|/**      * Set the prefix for a plugin.      *       * @param prefix      */
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
comment|//-- void setPrefix( String )
comment|/**      * Method toString.      *       * @return String      */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|(
literal|128
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"artifactId = '"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|//-- java.lang.String toString()
block|}
end_class

end_unit

