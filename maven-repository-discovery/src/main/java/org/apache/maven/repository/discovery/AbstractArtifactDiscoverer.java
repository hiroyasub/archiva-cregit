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
comment|/**  * Base class for artifact discoverers.  *  * @author John Casey  * @author Brett Porter  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractArtifactDiscoverer
extends|extends
name|AbstractLogEnabled
implements|implements
name|ArtifactDiscoverer
block|{
comment|/**      * Standard patterns to exclude from discovery as they are not artifacts.      */
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|STANDARD_DISCOVERY_EXCLUDES
init|=
block|{
literal|"bin/**"
block|,
literal|"reports/**"
block|,
literal|".maven/**"
block|,
literal|"**/*.md5"
block|,
literal|"**/*.MD5"
block|,
literal|"**/*.sha1"
block|,
literal|"**/*.SHA1"
block|,
literal|"**/*snapshot-version"
block|,
literal|"*/website/**"
block|,
literal|"*/licenses/**"
block|,
literal|"*/licences/**"
block|,
literal|"**/.htaccess"
block|,
literal|"**/*.html"
block|,
literal|"**/*.asc"
block|,
literal|"**/*.txt"
block|,
literal|"**/*.xml"
block|,
literal|"**/README*"
block|,
literal|"**/CHANGELOG*"
block|,
literal|"**/KEYS*"
block|}
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
specifier|private
name|List
name|kickedOutPaths
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/**      * Scan the repository for artifact paths.      *      * @todo replace blacklisted patterns by an artifact filter      */
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
name|allExcludes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|STANDARD_DISCOVERY_EXCLUDES
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|blacklistedPatterns
operator|!=
literal|null
operator|&&
name|blacklistedPatterns
operator|.
name|length
argument_list|()
operator|>
literal|0
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
name|excludedPaths
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|scanner
operator|.
name|getExcludedFiles
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|scanner
operator|.
name|getIncludedFiles
argument_list|()
return|;
block|}
comment|/**      * Add a path to the list of files that were kicked out due to being invalid.      *      * @param path the path to add      * @todo add a reason      */
specifier|protected
name|void
name|addKickedOutPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|kickedOutPaths
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

