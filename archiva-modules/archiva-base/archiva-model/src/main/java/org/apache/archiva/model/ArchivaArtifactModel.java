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
comment|/**  * Class ArchivaArtifactModel.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|ArchivaArtifactModel
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
comment|/**      *       *             The repository associated with this content.      *                 */
specifier|private
name|String
name|repositoryId
decl_stmt|;
comment|/**      *       *             True if this is a snapshot.      *                 */
specifier|private
name|boolean
name|snapshot
init|=
literal|false
decl_stmt|;
comment|/**      *       *             The MD5 checksum for the artifact file.      *                 */
specifier|private
name|String
name|checksumMD5
decl_stmt|;
comment|/**      *       *             The SHA1 checksum for the artifact file.      *                 */
specifier|private
name|String
name|checksumSHA1
decl_stmt|;
comment|/**      *       *             The Last Modified Timestamp of this artifact.      *                 */
specifier|private
name|java
operator|.
name|util
operator|.
name|Date
name|lastModified
decl_stmt|;
comment|/**      *       *             The size of the artifact on disk.      *                 */
specifier|private
name|long
name|size
init|=
literal|0L
decl_stmt|;
comment|/**      *       *             When this artifact was gathered or discovered      * from the repository.      *                 */
specifier|private
name|java
operator|.
name|util
operator|.
name|Date
name|whenGathered
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
name|ArchivaArtifactModel
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ArchivaArtifactModel
name|that
init|=
operator|(
name|ArchivaArtifactModel
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
name|getGroupId
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getGroupId
argument_list|()
operator|==
literal|null
else|:
name|getGroupId
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|)
expr_stmt|;
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
name|result
operator|=
name|result
operator|&&
operator|(
name|getVersion
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getVersion
argument_list|()
operator|==
literal|null
else|:
name|getVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|)
expr_stmt|;
name|result
operator|=
name|result
operator|&&
operator|(
name|getClassifier
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getClassifier
argument_list|()
operator|==
literal|null
else|:
name|getClassifier
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getClassifier
argument_list|()
argument_list|)
operator|)
expr_stmt|;
name|result
operator|=
name|result
operator|&&
operator|(
name|getType
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getType
argument_list|()
operator|==
literal|null
else|:
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getType
argument_list|()
argument_list|)
operator|)
expr_stmt|;
name|result
operator|=
name|result
operator|&&
operator|(
name|getRepositoryId
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getRepositoryId
argument_list|()
operator|==
literal|null
else|:
name|getRepositoryId
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|//-- boolean equals( Object )
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
comment|/**      * Get the MD5 checksum for the artifact file.      *       * @return String      */
specifier|public
name|String
name|getChecksumMD5
parameter_list|()
block|{
return|return
name|this
operator|.
name|checksumMD5
return|;
block|}
comment|//-- String getChecksumMD5()
comment|/**      * Get the SHA1 checksum for the artifact file.      *       * @return String      */
specifier|public
name|String
name|getChecksumSHA1
parameter_list|()
block|{
return|return
name|this
operator|.
name|checksumSHA1
return|;
block|}
comment|//-- String getChecksumSHA1()
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
comment|/**      * Get the Last Modified Timestamp of this artifact.      *       * @return Date      */
specifier|public
name|java
operator|.
name|util
operator|.
name|Date
name|getLastModified
parameter_list|()
block|{
return|return
name|this
operator|.
name|lastModified
return|;
block|}
comment|//-- java.util.Date getLastModified()
comment|/**      * Get the repository associated with this content.      *       * @return String      */
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|this
operator|.
name|repositoryId
return|;
block|}
comment|//-- String getRepositoryId()
comment|/**      * Get the size of the artifact on disk.      *       * @return long      */
specifier|public
name|long
name|getSize
parameter_list|()
block|{
return|return
name|this
operator|.
name|size
return|;
block|}
comment|//-- long getSize()
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
comment|/**      * Get when this artifact was gathered or discovered from the      * repository.      *       * @return Date      */
specifier|public
name|java
operator|.
name|util
operator|.
name|Date
name|getWhenGathered
parameter_list|()
block|{
return|return
name|this
operator|.
name|whenGathered
return|;
block|}
comment|//-- java.util.Date getWhenGathered()
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
name|groupId
operator|!=
literal|null
condition|?
name|groupId
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
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
name|result
operator|=
literal|37
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
literal|37
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
literal|37
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
literal|37
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
return|return
name|result
return|;
block|}
comment|//-- int hashCode()
comment|/**      * Get true if this is a snapshot.      *       * @return boolean      */
specifier|public
name|boolean
name|isSnapshot
parameter_list|()
block|{
return|return
name|this
operator|.
name|snapshot
return|;
block|}
comment|//-- boolean isSnapshot()
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
comment|/**      * Set the MD5 checksum for the artifact file.      *       * @param checksumMD5      */
specifier|public
name|void
name|setChecksumMD5
parameter_list|(
name|String
name|checksumMD5
parameter_list|)
block|{
name|this
operator|.
name|checksumMD5
operator|=
name|checksumMD5
expr_stmt|;
block|}
comment|//-- void setChecksumMD5( String )
comment|/**      * Set the SHA1 checksum for the artifact file.      *       * @param checksumSHA1      */
specifier|public
name|void
name|setChecksumSHA1
parameter_list|(
name|String
name|checksumSHA1
parameter_list|)
block|{
name|this
operator|.
name|checksumSHA1
operator|=
name|checksumSHA1
expr_stmt|;
block|}
comment|//-- void setChecksumSHA1( String )
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
comment|/**      * Set the Last Modified Timestamp of this artifact.      *       * @param lastModified      */
specifier|public
name|void
name|setLastModified
parameter_list|(
name|java
operator|.
name|util
operator|.
name|Date
name|lastModified
parameter_list|)
block|{
name|this
operator|.
name|lastModified
operator|=
name|lastModified
expr_stmt|;
block|}
comment|//-- void setLastModified( java.util.Date )
comment|/**      * Set the repository associated with this content.      *       * @param repositoryId      */
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
comment|//-- void setRepositoryId( String )
comment|/**      * Set the size of the artifact on disk.      *       * @param size      */
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
comment|//-- void setSize( long )
comment|/**      * Set true if this is a snapshot.      *       * @param snapshot      */
specifier|public
name|void
name|setSnapshot
parameter_list|(
name|boolean
name|snapshot
parameter_list|)
block|{
name|this
operator|.
name|snapshot
operator|=
name|snapshot
expr_stmt|;
block|}
comment|//-- void setSnapshot( boolean )
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
comment|/**      * Set when this artifact was gathered or discovered from the      * repository.      *       * @param whenGathered      */
specifier|public
name|void
name|setWhenGathered
parameter_list|(
name|java
operator|.
name|util
operator|.
name|Date
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
comment|//-- void setWhenGathered( java.util.Date )
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
literal|"groupId = '"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|getGroupId
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
name|buf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
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
name|buf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"version = '"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|getVersion
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
name|buf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"classifier = '"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|getClassifier
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
name|buf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"type = '"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|getType
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
name|buf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"repositoryId = '"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|getRepositoryId
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
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6292417108113887384L
decl_stmt|;
block|}
end_class

end_unit
