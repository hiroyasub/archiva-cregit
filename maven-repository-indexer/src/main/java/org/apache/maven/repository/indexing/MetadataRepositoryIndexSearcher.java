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
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
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
name|artifact
operator|.
name|Artifact
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
name|artifact
operator|.
name|factory
operator|.
name|ArtifactFactory
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|ArtifactRepositoryMetadata
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|GroupRepositoryMetadata
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|RepositoryMetadata
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|SnapshotArtifactRepositoryMetadata
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|io
operator|.
name|xpp3
operator|.
name|MetadataXpp3Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * This class searches the specified index given the search/query criteria.  */
end_comment

begin_class
specifier|public
class|class
name|MetadataRepositoryIndexSearcher
extends|extends
name|AbstractRepositoryIndexSearcher
block|{
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FLD_METADATAPATH
init|=
literal|"path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FLD_GROUPID
init|=
literal|"groupId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FLD_ARTIFACTID
init|=
literal|"artifactId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FLD_VERSION
init|=
literal|"version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_TYPE
init|=
literal|"GROUP"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT_TYPE
init|=
literal|"ARTIFACT"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SNAPSHOT_TYPE
init|=
literal|"SNAPSHOT"
decl_stmt|;
comment|/**      * Constructor      *      * @param index   the index object to be set      * @param factory      */
specifier|public
name|MetadataRepositoryIndexSearcher
parameter_list|(
name|MetadataRepositoryIndex
name|index
parameter_list|,
name|ArtifactFactory
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|artifactFactory
operator|=
name|factory
expr_stmt|;
block|}
comment|/**      * Create object to be returned by the search based on the document      *      * @param doc      * @return Object      */
specifier|protected
name|Object
name|createSearchedObjectFromIndexDocument
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
name|List
name|pathParts
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|doc
operator|.
name|get
argument_list|(
name|FLD_METADATAPATH
argument_list|)
argument_list|,
literal|"/\\"
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|pathParts
operator|.
name|add
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|reverse
argument_list|(
name|pathParts
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|pathParts
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|metadataFile
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|tmpDir
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|metadataType
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|tmpDir
operator|.
name|equals
argument_list|(
name|doc
operator|.
name|get
argument_list|(
name|FLD_GROUPID
argument_list|)
argument_list|)
condition|)
block|{
name|metadataType
operator|=
name|GROUP_TYPE
expr_stmt|;
block|}
if|else if
condition|(
name|tmpDir
operator|.
name|equals
argument_list|(
name|doc
operator|.
name|get
argument_list|(
name|FLD_ARTIFACTID
argument_list|)
argument_list|)
condition|)
block|{
name|metadataType
operator|=
name|ARTIFACT_TYPE
expr_stmt|;
block|}
else|else
block|{
name|metadataType
operator|=
name|SNAPSHOT_TYPE
expr_stmt|;
block|}
name|RepositoryMetadata
name|repoMetadata
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repoMetadata
operator|=
name|getMetadata
argument_list|(
name|doc
operator|.
name|get
argument_list|(
name|FLD_GROUPID
argument_list|)
argument_list|,
name|doc
operator|.
name|get
argument_list|(
name|FLD_ARTIFACTID
argument_list|)
argument_list|,
name|doc
operator|.
name|get
argument_list|(
name|FLD_VERSION
argument_list|)
argument_list|,
name|metadataFile
argument_list|,
name|metadataType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//@todo
block|}
return|return
name|repoMetadata
return|;
block|}
comment|/**      * Create RepositoryMetadata object.      *      * @param groupId      the groupId to be set      * @param artifactId   the artifactId to be set      * @param version      the version to be set      * @param filename     the name of the metadata file      * @param metadataType the type of RepositoryMetadata object to be created (GROUP, ARTIFACT or SNAPSHOT)      * @return RepositoryMetadata      * @throws Exception      */
specifier|private
name|RepositoryMetadata
name|getMetadata
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
name|filename
parameter_list|,
name|String
name|metadataType
parameter_list|)
throws|throws
name|Exception
block|{
name|RepositoryMetadata
name|repoMetadata
init|=
literal|null
decl_stmt|;
name|URL
name|url
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|MetadataXpp3Reader
name|metadataReader
init|=
operator|new
name|MetadataXpp3Reader
argument_list|()
decl_stmt|;
comment|//group metadata
if|if
condition|(
name|metadataType
operator|.
name|equals
argument_list|(
name|GROUP_TYPE
argument_list|)
condition|)
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|index
operator|.
name|getRepository
argument_list|()
operator|.
name|getBasedir
argument_list|()
operator|+
name|groupId
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|filename
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|metadataReader
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//artifact metadata
if|else if
condition|(
name|metadataType
operator|.
name|equals
argument_list|(
name|ARTIFACT_TYPE
argument_list|)
condition|)
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|index
operator|.
name|getRepository
argument_list|()
operator|.
name|getBasedir
argument_list|()
operator|+
name|groupId
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
operator|+
name|filename
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|metadataReader
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//snapshot/version metadata
if|else if
condition|(
name|metadataType
operator|.
name|equals
argument_list|(
name|SNAPSHOT_TYPE
argument_list|)
condition|)
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|index
operator|.
name|getRepository
argument_list|()
operator|.
name|getBasedir
argument_list|()
operator|+
name|groupId
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
operator|+
name|version
operator|+
literal|"/"
operator|+
name|filename
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|SnapshotArtifactRepositoryMetadata
argument_list|(
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|metadataReader
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|repoMetadata
return|;
block|}
comment|/**      * Create artifact object.      *      * @param groupId    the groupId of the artifact      * @param artifactId the artifactId of the artifact      * @param version    the version of the artifact      * @return Artifact      * @throws Exception      */
specifier|private
name|Artifact
name|getArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"jar"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

