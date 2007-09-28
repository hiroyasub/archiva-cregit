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
name|consumers
operator|.
name|core
operator|.
name|repository
package|;
end_package

begin_comment
comment|/* * Licensed to the Apache Software Foundation (ASF) under one * or more contributor license agreements.  See the NOTICE file * distributed with this work for additional information * regarding copyright ownership.  The ASF licenses this file * to you under the Apache License, Version 2.0 (the * "License"); you may not use this file except in compliance * with the License.  You may obtain a copy of the License at * *  http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, * software distributed under the License is distributed on an * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY * KIND, either express or implied.  See the License for the * specific language governing permissions and limitations * under the License. */
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
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|ArtifactDAO
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
name|indexer
operator|.
name|RepositoryIndexException
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
name|model
operator|.
name|ArchivaArtifact
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
name|model
operator|.
name|ArchivaRepository
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
name|repository
operator|.
name|layout
operator|.
name|BidirectionalRepositoryLayout
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
name|repository
operator|.
name|layout
operator|.
name|FilenameParts
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
name|repository
operator|.
name|layout
operator|.
name|LayoutException
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
name|repository
operator|.
name|layout
operator|.
name|RepositoryLayoutUtils
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
name|FilenameFilter
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryPurge
implements|implements
name|RepositoryPurge
block|{
specifier|protected
name|ArchivaRepository
name|repository
decl_stmt|;
specifier|protected
name|BidirectionalRepositoryLayout
name|layout
decl_stmt|;
specifier|protected
name|ArtifactDAO
name|artifactDao
decl_stmt|;
specifier|public
name|AbstractRepositoryPurge
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|,
name|BidirectionalRepositoryLayout
name|layout
parameter_list|,
name|ArtifactDAO
name|artifactDao
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
name|this
operator|.
name|artifactDao
operator|=
name|artifactDao
expr_stmt|;
block|}
comment|/**      * Get all files from the directory that matches the specified filename.      *      * @param dir      the directory to be scanned      * @param filename the filename to be matched      * @return      */
specifier|protected
name|File
index|[]
name|getFiles
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|filename
parameter_list|)
block|{
name|FilenameFilter
name|filter
init|=
operator|new
name|ArtifactFilenameFilter
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|(
name|filter
argument_list|)
decl_stmt|;
return|return
name|files
return|;
block|}
comment|/**      * Purge the repo. Update db and index of removed artifacts.      *      * @param artifactFiles      * @throws RepositoryIndexException      */
specifier|protected
name|void
name|purge
parameter_list|(
name|File
index|[]
name|artifactFiles
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|artifactFiles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|artifactFiles
index|[
name|i
index|]
operator|.
name|delete
argument_list|()
expr_stmt|;
name|String
index|[]
name|artifactPathParts
init|=
name|artifactFiles
index|[
name|i
index|]
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|split
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|artifactPath
init|=
name|artifactPathParts
index|[
name|artifactPathParts
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|artifactPath
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"SHA1"
argument_list|)
operator|&&
operator|!
name|artifactPath
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"MD5"
argument_list|)
condition|)
block|{
comment|// intended to be swallowed
comment|// continue updating the database for all artifacts
try|try
block|{
name|updateDatabase
argument_list|(
name|artifactPath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|ae
parameter_list|)
block|{
comment|//@todo determine logging to be used
block|}
catch|catch
parameter_list|(
name|LayoutException
name|le
parameter_list|)
block|{
block|}
block|}
block|}
block|}
specifier|private
name|void
name|updateDatabase
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ArchivaDatabaseException
throws|,
name|LayoutException
block|{
name|ArchivaArtifact
name|artifact
init|=
name|layout
operator|.
name|toArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|ArchivaArtifact
name|queriedArtifact
init|=
name|artifactDao
operator|.
name|getArtifact
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|artifactDao
operator|.
name|deleteArtifact
argument_list|(
name|queriedArtifact
argument_list|)
expr_stmt|;
comment|// TODO [MRM-37]: re-run the database consumers to clean up
block|}
comment|/**      * Get the artifactId, version, extension and classifier from the path parameter      *      * @param path      * @return      * @throws LayoutException      */
specifier|protected
name|FilenameParts
name|getFilenameParts
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
name|String
name|normalizedPath
init|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|path
argument_list|,
literal|"\\"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|pathParts
index|[]
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|normalizedPath
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
name|FilenameParts
name|parts
init|=
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
name|pathParts
index|[
name|pathParts
operator|.
name|length
operator|-
literal|1
index|]
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|parts
return|;
block|}
block|}
end_class

end_unit

