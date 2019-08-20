begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * Metadata stored in the content repository for a particular artifact. Information that is shared between different  * artifacts of a given project version can be found in the  * {@link org.apache.archiva.metadata.model.ProjectVersionMetadata} class. The metadata is faceted to store information  * about particular types of artifacts, for example Maven 2.x artifact specific information.  * For more information, see the  *<a href="{@docRoot}/../metadata-content-model.html" target="_top">Metadata Content Model</a>.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"artifactMetadata"
argument_list|)
specifier|public
class|class
name|ArtifactMetadata
extends|extends
name|FacetedMetadata
block|{
comment|/**      * The artifact ID uniquely identifies an artifact within a given namespace, project and project version. For      * example,<tt>archiva-1.4-20100201.345612-2.jar</tt>      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * The repository that the artifact is stored in within the content repository.      */
specifier|private
name|String
name|repositoryId
decl_stmt|;
comment|/**      * The namespace of the project within the repository.      *      * @see org.apache.archiva.metadata.model.ProjectMetadata#namespace      */
specifier|private
name|String
name|namespace
decl_stmt|;
comment|/**      * The identifier of the project within the repository and namespace.      *      * @see org.apache.archiva.metadata.model.ProjectMetadata#id      */
specifier|private
name|String
name|project
decl_stmt|;
comment|/**      * The version of the project. This may be more generalised than @{link #version}.      *      * @see org.apache.archiva.metadata.model.ProjectVersionMetadata#id      */
specifier|private
name|String
name|projectVersion
decl_stmt|;
comment|/**      * The artifact version, if different from the project version. Note that the metadata does not do any calculation      * of this based on the project version - the calling code must be sure to set and check it appropriately if      *<tt>null</tt>.      */
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      * The last modified date of the artifact file, if known.      */
specifier|private
name|Date
name|fileLastModified
decl_stmt|;
comment|/**      * The file size of the artifact, if known.      */
specifier|private
name|long
name|size
decl_stmt|;
comment|/**      * The MD5 checksum of the artifact, if calculated.      */
specifier|private
name|String
name|md5
decl_stmt|;
comment|/**      * The SHA-1 checksum of the artifact, if calculated.      */
specifier|private
name|String
name|sha1
decl_stmt|;
comment|/**      * When the artifact was found in the repository storage and added to the metadata content repository.      */
specifier|private
name|ZonedDateTime
name|whenGathered
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
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
specifier|public
name|long
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
specifier|public
name|void
name|setSize
parameter_list|(
name|long
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
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
name|String
name|getProjectVersion
parameter_list|()
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
specifier|public
name|void
name|setFileLastModified
parameter_list|(
name|long
name|fileLastModified
parameter_list|)
block|{
name|this
operator|.
name|fileLastModified
operator|=
operator|new
name|Date
argument_list|(
name|fileLastModified
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setWhenGathered
parameter_list|(
name|ZonedDateTime
name|whenGathered
parameter_list|)
block|{
name|this
operator|.
name|whenGathered
operator|=
name|whenGathered
expr_stmt|;
block|}
specifier|public
name|void
name|setMd5
parameter_list|(
name|String
name|md5
parameter_list|)
block|{
name|this
operator|.
name|md5
operator|=
name|md5
expr_stmt|;
block|}
specifier|public
name|void
name|setSha1
parameter_list|(
name|String
name|sha1
parameter_list|)
block|{
name|this
operator|.
name|sha1
operator|=
name|sha1
expr_stmt|;
block|}
specifier|public
name|ZonedDateTime
name|getWhenGathered
parameter_list|()
block|{
return|return
name|whenGathered
return|;
block|}
specifier|public
name|String
name|getMd5
parameter_list|()
block|{
return|return
name|md5
return|;
block|}
specifier|public
name|String
name|getSha1
parameter_list|()
block|{
return|return
name|sha1
return|;
block|}
specifier|public
name|Date
name|getFileLastModified
parameter_list|()
block|{
return|return
name|fileLastModified
return|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
specifier|public
name|void
name|setProject
parameter_list|(
name|String
name|project
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
block|}
specifier|public
name|String
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
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
name|repositoryId
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ArtifactMetadata
name|that
init|=
operator|(
name|ArtifactMetadata
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|size
operator|!=
name|that
operator|.
name|size
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// We allow some
if|if
condition|(
name|fileLastModified
operator|!=
literal|null
condition|?
operator|!
name|fileLastModified
operator|.
name|equals
argument_list|(
name|that
operator|.
name|fileLastModified
argument_list|)
else|:
name|that
operator|.
name|fileLastModified
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
name|id
operator|.
name|equals
argument_list|(
name|that
operator|.
name|id
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|md5
operator|!=
literal|null
condition|?
operator|!
name|md5
operator|.
name|equals
argument_list|(
name|that
operator|.
name|md5
argument_list|)
else|:
name|that
operator|.
name|md5
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
name|namespace
operator|!=
literal|null
condition|?
operator|!
name|namespace
operator|.
name|equals
argument_list|(
name|that
operator|.
name|namespace
argument_list|)
else|:
name|that
operator|.
name|namespace
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
name|project
operator|!=
literal|null
condition|?
operator|!
name|project
operator|.
name|equals
argument_list|(
name|that
operator|.
name|project
argument_list|)
else|:
name|that
operator|.
name|project
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
name|projectVersion
operator|!=
literal|null
condition|?
operator|!
name|projectVersion
operator|.
name|equals
argument_list|(
name|that
operator|.
name|projectVersion
argument_list|)
else|:
name|that
operator|.
name|projectVersion
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|/**          * We cannot compare in different repositories, if this is in here         if ( !repositoryId.equals( that.repositoryId ) )         {             return false;         }          **/
if|if
condition|(
name|sha1
operator|!=
literal|null
condition|?
operator|!
name|sha1
operator|.
name|equals
argument_list|(
name|that
operator|.
name|sha1
argument_list|)
else|:
name|that
operator|.
name|sha1
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
name|version
operator|!=
literal|null
condition|?
operator|!
name|version
operator|.
name|equals
argument_list|(
name|that
operator|.
name|version
argument_list|)
else|:
name|that
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
if|if
condition|(
name|whenGathered
operator|!=
literal|null
condition|?
operator|!
name|whenGathered
operator|.
name|toInstant
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|whenGathered
operator|.
name|toInstant
argument_list|()
argument_list|)
else|:
name|that
operator|.
name|whenGathered
operator|!=
literal|null
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
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
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
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|repositoryId
operator|!=
literal|null
condition|?
name|repositoryId
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
name|namespace
operator|!=
literal|null
condition|?
name|namespace
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
name|project
operator|!=
literal|null
condition|?
name|project
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
name|projectVersion
operator|!=
literal|null
condition|?
name|projectVersion
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
name|version
operator|!=
literal|null
condition|?
name|version
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
name|fileLastModified
operator|!=
literal|null
condition|?
name|fileLastModified
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
name|int
operator|)
operator|(
name|size
operator|^
operator|(
name|size
operator|>>>
literal|32
operator|)
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|md5
operator|!=
literal|null
condition|?
name|md5
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
name|sha1
operator|!=
literal|null
condition|?
name|sha1
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
name|whenGathered
operator|!=
literal|null
condition|?
name|whenGathered
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
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ArtifactMetadata{"
operator|+
literal|"id='"
operator|+
name|id
operator|+
literal|'\''
operator|+
literal|", size="
operator|+
name|size
operator|+
literal|", version='"
operator|+
name|version
operator|+
literal|'\''
operator|+
literal|", fileLastModified="
operator|+
name|fileLastModified
operator|+
literal|", whenGathered="
operator|+
name|whenGathered
operator|+
literal|", md5='"
operator|+
name|md5
operator|+
literal|'\''
operator|+
literal|", sha1='"
operator|+
name|sha1
operator|+
literal|'\''
operator|+
literal|", namespace='"
operator|+
name|namespace
operator|+
literal|'\''
operator|+
literal|", project='"
operator|+
name|project
operator|+
literal|'\''
operator|+
literal|", projectVersion='"
operator|+
name|projectVersion
operator|+
literal|'\''
operator|+
literal|", repositoryId='"
operator|+
name|repositoryId
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

