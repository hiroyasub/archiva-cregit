begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|indexing
operator|.
name|record
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * The a record with the fields in the standard index.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|StandardArtifactIndexRecord
extends|extends
name|MinimalArtifactIndexRecord
block|{
comment|/**      * The SHA-1 checksum of the artifact file.      */
specifier|private
name|String
name|sha1Checksum
decl_stmt|;
comment|/**      * The artifact's group.      */
specifier|private
name|String
name|groupId
decl_stmt|;
comment|/**      * The artifact's identifier within the group.      */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|/**      * The artifact's version.      */
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      * The classifier, if there is one.      */
specifier|private
name|String
name|classifier
decl_stmt|;
comment|/**      * The artifact type (from the file).      */
specifier|private
name|String
name|type
decl_stmt|;
comment|/**      * A list of packages (separated by '\n') in the artifact if it contains Java classes.      */
specifier|private
name|String
name|packages
decl_stmt|;
comment|/**      * A list of files (separated by '\n') in the artifact if it is an archive.      */
specifier|private
name|String
name|files
decl_stmt|;
comment|/**      * The identifier of the repository that the artifact came from.      */
specifier|private
name|String
name|repository
decl_stmt|;
specifier|public
name|void
name|setSha1Checksum
parameter_list|(
name|String
name|sha1Checksum
parameter_list|)
block|{
name|this
operator|.
name|sha1Checksum
operator|=
name|sha1Checksum
expr_stmt|;
block|}
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
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
specifier|public
name|void
name|setFiles
parameter_list|(
name|String
name|files
parameter_list|)
block|{
name|this
operator|.
name|files
operator|=
name|files
expr_stmt|;
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|String
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
comment|/**      * @noinspection RedundantIfStatement      */
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
operator|||
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
name|StandardArtifactIndexRecord
name|that
init|=
operator|(
name|StandardArtifactIndexRecord
operator|)
name|obj
decl_stmt|;
if|if
condition|(
operator|!
name|artifactId
operator|.
name|equals
argument_list|(
name|that
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
name|classifier
operator|!=
literal|null
condition|?
operator|!
name|classifier
operator|.
name|equals
argument_list|(
name|that
operator|.
name|classifier
argument_list|)
else|:
name|that
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
if|if
condition|(
name|files
operator|!=
literal|null
condition|?
operator|!
name|files
operator|.
name|equals
argument_list|(
name|that
operator|.
name|files
argument_list|)
else|:
name|that
operator|.
name|files
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|groupId
operator|.
name|equals
argument_list|(
name|that
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
name|packages
operator|!=
literal|null
condition|?
operator|!
name|packages
operator|.
name|equals
argument_list|(
name|that
operator|.
name|packages
argument_list|)
else|:
name|that
operator|.
name|packages
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|repository
operator|!=
literal|null
condition|?
operator|!
name|repository
operator|.
name|equals
argument_list|(
name|that
operator|.
name|repository
argument_list|)
else|:
name|that
operator|.
name|repository
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|sha1Checksum
operator|!=
literal|null
condition|?
operator|!
name|sha1Checksum
operator|.
name|equals
argument_list|(
name|that
operator|.
name|sha1Checksum
argument_list|)
else|:
name|that
operator|.
name|sha1Checksum
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|?
operator|!
name|type
operator|.
name|equals
argument_list|(
name|that
operator|.
name|type
argument_list|)
else|:
name|that
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
if|if
condition|(
operator|!
name|version
operator|.
name|equals
argument_list|(
name|that
operator|.
name|version
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
name|int
name|hashCode
parameter_list|()
block|{
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
literal|31
operator|*
name|result
operator|+
operator|(
name|sha1Checksum
operator|!=
literal|null
condition|?
name|sha1Checksum
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|groupId
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|artifactId
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|version
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|classifier
operator|!=
literal|null
condition|?
name|classifier
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|type
operator|!=
literal|null
condition|?
name|type
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|packages
operator|!=
literal|null
condition|?
name|packages
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|files
operator|!=
literal|null
condition|?
name|files
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|repository
operator|!=
literal|null
condition|?
name|repository
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
block|}
end_class

end_unit

