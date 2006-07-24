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
name|discovery
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
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|DirectoryScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
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
name|File
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
name|Arrays
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

begin_comment
comment|/**  * Base class for the artifact and metadata discoverers.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDiscoverer
extends|extends
name|AbstractLogEnabled
implements|implements
name|Discoverer
block|{
specifier|private
name|List
name|kickedOutPaths
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|protected
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|EMPTY_STRING_ARRAY
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
specifier|private
name|List
name|excludedPaths
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/**      * Add a path to the list of files that were kicked out due to being invalid.      *      * @param path   the path to add      * @param reason the reason why the path is being kicked out      */
specifier|protected
name|void
name|addKickedOutPath
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|kickedOutPaths
operator|.
name|add
argument_list|(
operator|new
name|DiscovererPath
argument_list|(
name|path
argument_list|,
name|reason
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns an iterator for the list if DiscovererPaths that were found to not represent a searched object      *      * @return Iterator for the DiscovererPath List      */
specifier|public
name|Iterator
name|getKickedOutPathsIterator
parameter_list|()
block|{
return|return
name|kickedOutPaths
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * Scan the repository for artifact paths.      */
specifier|protected
name|String
index|[]
name|scanForArtifactPaths
parameter_list|(
name|File
name|repositoryBase
parameter_list|,
name|String
name|blacklistedPatterns
parameter_list|,
name|String
index|[]
name|includes
parameter_list|,
name|String
index|[]
name|excludes
parameter_list|)
block|{
name|List
name|allExcludes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|allExcludes
operator|.
name|addAll
argument_list|(
name|FileUtils
operator|.
name|getDefaultExcludesAsList
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|excludes
operator|!=
literal|null
condition|)
block|{
name|allExcludes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|excludes
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|blacklistedPatterns
argument_list|)
condition|)
block|{
name|allExcludes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|blacklistedPatterns
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|DirectoryScanner
name|scanner
init|=
operator|new
name|DirectoryScanner
argument_list|()
decl_stmt|;
name|scanner
operator|.
name|setBasedir
argument_list|(
name|repositoryBase
argument_list|)
expr_stmt|;
if|if
condition|(
name|includes
operator|!=
literal|null
condition|)
block|{
name|scanner
operator|.
name|setIncludes
argument_list|(
name|includes
argument_list|)
expr_stmt|;
block|}
name|scanner
operator|.
name|setExcludes
argument_list|(
operator|(
name|String
index|[]
operator|)
name|allExcludes
operator|.
name|toArray
argument_list|(
name|EMPTY_STRING_ARRAY
argument_list|)
argument_list|)
expr_stmt|;
name|scanner
operator|.
name|scan
argument_list|()
expr_stmt|;
for|for
control|(
name|Iterator
name|files
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|scanner
operator|.
name|getExcludedFiles
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
init|;
name|files
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|path
init|=
name|files
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|excludedPaths
operator|.
name|add
argument_list|(
operator|new
name|DiscovererPath
argument_list|(
name|path
argument_list|,
literal|"Artifact was in the specified list of exclusions"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|scanner
operator|.
name|getIncludedFiles
argument_list|()
return|;
block|}
comment|/**      * Returns an iterator for the list if DiscovererPaths that were not processed because they are explicitly excluded      *      * @return Iterator for the DiscovererPath List      */
specifier|public
name|Iterator
name|getExcludedPathsIterator
parameter_list|()
block|{
return|return
name|excludedPaths
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
end_class

end_unit

