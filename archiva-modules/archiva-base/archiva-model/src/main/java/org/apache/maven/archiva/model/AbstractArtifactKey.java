begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  *<p>  * AbstractArtifactKey - a artifact reference to a versioned project.  * This refers to all artifacts of a specific version of a project.  * This type of reference is typically used by {@link ArchivaProjectModel} objects.   *</p>  *   *<p>  *   If you don't require things like "Version" or "Type", consider the other keys below.  *</p>  *   *<table border="1" cellpadding="3">  *<tr>  *<th>Key Type</th>  *<th>Group ID</th>  *<th>Artifact ID</th>  *<th>Version</th>  *<th>Classifier</th>  *<th>Type</th>  *</tr>  *<tr>  *<td>{@link AbstractProjectKey}</td>  *<td align="center">Yes</td>  *<td align="center">Yes</td>  *<td>&nbsp;</td>  *<td>&nbsp;</td>  *<td>&nbsp;</td>  *</tr>  *<tr>  *<td>{@link AbstractVersionedKey}</td>  *<td align="center">Yes</td>  *<td align="center">Yes</td>  *<td align="center">Yes</td>  *<td>&nbsp;</td>  *<td>&nbsp;</td>  *</tr>  *<tr>  *<td>{@link AbstractArtifactKey}</td>  *<td align="center">Yes</td>  *<td align="center">Yes</td>  *<td align="center">Yes</td>  *<td align="center">Yes</td>  *<td align="center">Yes</td>  *</tr>  *</table>  *   *<p>  * NOTE: This is a jpox required compound key handler class.  *</p>  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|AbstractArtifactKey
implements|implements
name|CompoundKey
implements|,
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1084250769223555422L
decl_stmt|;
comment|/**      * The Group ID. (JPOX Requires this remain public)      */
specifier|public
name|String
name|groupId
init|=
literal|""
decl_stmt|;
comment|/**      * The Artifact ID. (JPOX Requires this remain public)      */
specifier|public
name|String
name|artifactId
init|=
literal|""
decl_stmt|;
comment|/**      * The Version. (JPOX Requires this remain public)      */
specifier|public
name|String
name|version
init|=
literal|""
decl_stmt|;
comment|/**      * The Classifier. (JPOX Requires this remain public)      */
specifier|public
name|String
name|classifier
init|=
literal|""
decl_stmt|;
comment|/**      * The Type. (JPOX Requires this remain public)      */
specifier|public
name|String
name|type
init|=
literal|""
decl_stmt|;
comment|/**      * The Repository Id (JPOX Requires this remain public)      */
specifier|public
name|String
name|repositoryId
init|=
literal|""
decl_stmt|;
comment|/**      * Default Constructor.  Required by JPOX.      */
specifier|public
name|AbstractArtifactKey
parameter_list|()
block|{
comment|/* do nothing */
block|}
comment|/**      * Key Based Constructor.  Required by JPOX.      *       * @param key the String representing this object's values.      */
specifier|public
name|AbstractArtifactKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|String
name|parts
index|[]
init|=
name|StringUtils
operator|.
name|splitPreserveAllTokens
argument_list|(
name|key
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|groupId
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|artifactId
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
name|version
operator|=
name|parts
index|[
literal|2
index|]
expr_stmt|;
name|classifier
operator|=
name|parts
index|[
literal|3
index|]
expr_stmt|;
name|type
operator|=
name|parts
index|[
literal|4
index|]
expr_stmt|;
name|repositoryId
operator|=
name|parts
index|[
literal|5
index|]
expr_stmt|;
block|}
comment|/**      * Get the String representation of this object. - Required by JPOX.      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|join
argument_list|(
operator|new
name|String
index|[]
block|{
name|groupId
block|,
name|artifactId
block|,
name|version
block|,
name|classifier
block|,
name|type
block|,
name|repositoryId
block|}
argument_list|,
literal|':'
argument_list|)
return|;
block|}
comment|/**      * Get the hashcode for this object's values - Required by JPOX.      */
annotation|@
name|Override
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
name|super
operator|.
name|hashCode
argument_list|()
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
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|repositoryId
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|repositoryId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Get the equals for this object's values - Required by JPOX.      */
annotation|@
name|Override
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
operator|!
name|super
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
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
name|AbstractArtifactKey
name|other
init|=
operator|(
name|AbstractArtifactKey
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
if|if
condition|(
name|repositoryId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|repositoryId
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
name|repositoryId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|repositoryId
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
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A blank Group ID is not allowed."
argument_list|)
throw|;
block|}
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A blank Artifact ID is not allowed."
argument_list|)
throw|;
block|}
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A blank version is not allowed."
argument_list|)
throw|;
block|}
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
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
literal|""
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
block|}
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
literal|""
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
literal|""
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

