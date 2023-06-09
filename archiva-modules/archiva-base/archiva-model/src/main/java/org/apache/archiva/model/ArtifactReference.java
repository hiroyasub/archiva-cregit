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
comment|/**  * Class ArtifactReference.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|ArtifactReference
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
comment|/**      *       *             The Group ID of the repository content.      *                 */
specifier|private
name|String
name|groupId
decl_stmt|;
comment|/**      *       *             The Artifact ID of the repository content.      *                 */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|/**      *       *             The version of the repository content.      *                 */
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      *       *             The classifier for this artifact.      *                 */
specifier|private
name|String
name|classifier
decl_stmt|;
comment|/**      *       *             The type of artifact.      *                 */
specifier|private
name|String
name|type
decl_stmt|;
specifier|public
name|String
name|getProjectVersion
parameter_list|( )
block|{
return|return
name|projectVersion
return|;
block|}
specifier|public
name|void
name|setProjectVersion
parameter_list|(
name|String
name|projectVersion
parameter_list|)
block|{
name|this
operator|.
name|projectVersion
operator|=
name|projectVersion
expr_stmt|;
block|}
specifier|private
name|String
name|projectVersion
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the Artifact ID of the repository content.      *       * @return String      */
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
specifier|public
name|ArtifactReference
name|artifactId
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
return|return
name|this
return|;
block|}
comment|/**      * Get the classifier for this artifact.      *       * @return String      */
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|this
operator|.
name|classifier
return|;
block|}
comment|//-- String getClassifier()
specifier|public
name|ArtifactReference
name|classifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Get the Group ID of the repository content.      *       * @return String      */
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
specifier|public
name|ArtifactReference
name|groupId
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
return|return
name|this
return|;
block|}
comment|/**      * Get the type of artifact.      *       * @return String      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|type
return|;
block|}
comment|//-- String getType()
specifier|public
name|ArtifactReference
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Get the version of the repository content.      *       * @return String      */
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|this
operator|.
name|version
return|;
block|}
comment|//-- String getVersion()
specifier|public
name|ArtifactReference
name|version
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the Artifact ID of the repository content.      *       * @param artifactId      */
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
comment|/**      * Set the classifier for this artifact.      *       * @param classifier      */
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
comment|//-- void setClassifier( String )
comment|/**      * Set the Group ID of the repository content.      *       * @param groupId      */
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
comment|/**      * Set the type of artifact.      *       * @param type      */
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|//-- void setType( String )
comment|/**      * Set the version of the repository content.      *       * @param version      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
comment|//-- void setVersion( String )
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6116764846682178732L
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
name|ArtifactReference
name|artifactReference
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
name|artifactReference
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
name|artifactReference
operator|.
name|getArtifactId
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
name|artifactReference
operator|.
name|getVersion
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
name|artifactReference
operator|.
name|getClassifier
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
name|artifactReference
operator|.
name|getType
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
specifier|public
specifier|static
name|String
name|toVersionlessKey
parameter_list|(
name|ArtifactReference
name|artifactReference
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
name|artifactReference
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
name|artifactReference
operator|.
name|getArtifactId
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
name|artifactReference
operator|.
name|getClassifier
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
name|artifactReference
operator|.
name|getType
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
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|PRIME
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|groupId
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|groupId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|artifactId
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|artifactId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|version
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|version
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|classifier
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|classifier
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|type
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|type
operator|.
name|hashCode
argument_list|()
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
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|ArtifactReference
name|other
init|=
operator|(
name|ArtifactReference
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|groupId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|groupId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|groupId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|artifactId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|artifactId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|artifactId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|version
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|version
operator|.
name|equals
argument_list|(
name|other
operator|.
name|version
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|classifier
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|classifier
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|classifier
operator|.
name|equals
argument_list|(
name|other
operator|.
name|classifier
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|type
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|type
operator|.
name|equals
argument_list|(
name|other
operator|.
name|type
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

