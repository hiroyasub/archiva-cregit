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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|VersionUtil
import|;
end_import

begin_comment
comment|/**  * ArchivaArtifact - Mutable artifact object.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaArtifact
block|{
specifier|private
name|ArchivaArtifactModel
name|model
decl_stmt|;
specifier|private
name|ArchivaArtifactPlatformDetails
name|platformDetails
decl_stmt|;
specifier|private
name|String
name|baseVersion
decl_stmt|;
specifier|private
name|boolean
name|snapshot
init|=
literal|false
decl_stmt|;
specifier|public
name|ArchivaArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArchivaArtifact
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|empty
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to create ArchivaArtifact with empty groupId."
argument_list|)
throw|;
block|}
if|if
condition|(
name|empty
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to create ArchivaArtifact with empty artifactId."
argument_list|)
throw|;
block|}
if|if
condition|(
name|empty
argument_list|(
name|version
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to create ArchivaArtifact with empty version."
argument_list|)
throw|;
block|}
if|if
condition|(
name|empty
argument_list|(
name|type
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to create ArchivaArtifact with empty type."
argument_list|)
throw|;
block|}
name|model
operator|=
operator|new
name|ArchivaArtifactModel
argument_list|()
expr_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|model
operator|.
name|setRepositoryId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setClassifier
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|classifier
argument_list|)
argument_list|)
expr_stmt|;
name|model
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|snapshot
operator|=
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|this
operator|.
name|baseVersion
operator|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArchivaArtifactModel
name|getModel
parameter_list|()
block|{
return|return
name|model
return|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|model
operator|.
name|getGroupId
argument_list|()
return|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|model
operator|.
name|getArtifactId
argument_list|()
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|model
operator|.
name|getVersion
argument_list|()
return|;
block|}
specifier|public
name|String
name|getBaseVersion
parameter_list|()
block|{
return|return
name|baseVersion
return|;
block|}
specifier|public
name|boolean
name|isSnapshot
parameter_list|()
block|{
return|return
name|snapshot
return|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|model
operator|.
name|getClassifier
argument_list|()
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|model
operator|.
name|getType
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasClassifier
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|model
operator|.
name|getClassifier
argument_list|()
argument_list|)
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
if|if
condition|(
name|model
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|model
operator|.
name|getGroupId
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|model
operator|.
name|getGroupId
argument_list|()
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
name|model
operator|.
name|getArtifactId
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|model
operator|.
name|getArtifactId
argument_list|()
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
name|model
operator|.
name|getVersion
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|model
operator|.
name|getVersion
argument_list|()
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
name|model
operator|.
name|getClassifier
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|model
operator|.
name|getClassifier
argument_list|()
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
name|model
operator|.
name|getType
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|model
operator|.
name|getType
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
block|}
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
name|ArchivaArtifact
name|other
init|=
operator|(
name|ArchivaArtifact
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|model
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
operator|!
name|model
operator|.
name|equals
argument_list|(
name|other
operator|.
name|model
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
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|model
operator|.
name|getGroupId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
block|}
name|appendArtifactTypeClassifierString
argument_list|(
name|sb
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
if|if
condition|(
name|model
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|model
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|appendArtifactTypeClassifierString
parameter_list|(
name|StringBuffer
name|sb
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasClassifier
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|empty
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
operator|(
name|value
operator|==
literal|null
operator|)
operator|||
operator|(
name|value
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|<
literal|1
operator|)
return|;
block|}
specifier|public
name|ArchivaArtifactPlatformDetails
name|getPlatformDetails
parameter_list|()
block|{
return|return
name|platformDetails
return|;
block|}
specifier|public
name|void
name|setPlatformDetails
parameter_list|(
name|ArchivaArtifactPlatformDetails
name|platformDetails
parameter_list|)
block|{
name|this
operator|.
name|platformDetails
operator|=
name|platformDetails
expr_stmt|;
block|}
block|}
end_class

end_unit

