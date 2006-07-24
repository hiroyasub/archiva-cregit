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
name|Artifact
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
name|component
operator|.
name|repository
operator|.
name|exception
operator|.
name|ComponentLookupException
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
name|net
operator|.
name|MalformedURLException
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
comment|/**  * Test the legacy artifact discoverer.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|LegacyArtifactDiscovererTest
extends|extends
name|AbstractArtifactDiscovererTest
block|{
specifier|protected
name|String
name|getLayout
parameter_list|()
block|{
return|return
literal|"legacy"
return|;
block|}
specifier|protected
name|File
name|getRepositoryFile
parameter_list|()
block|{
return|return
name|getTestFile
argument_list|(
literal|"src/test/legacy-repository"
argument_list|)
return|;
block|}
specifier|public
name|void
name|testDefaultExcludes
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getExcludedPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|indexOf
argument_list|(
literal|".svn"
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check comment"
argument_list|,
literal|"Artifact was in the specified list of exclusions"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check exclusion was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check not .svn"
argument_list|,
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getPath
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|".svn"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testStandardExcludes
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getExcludedPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"KEYS"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check comment"
argument_list|,
literal|"Artifact was in the specified list of exclusions"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check exclusion was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check not KEYS"
argument_list|,
literal|"KEYS"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testBlacklistedExclude
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|"javax.sql/**"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getExcludedPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"javax.sql/jars/jdbc-2.0.jar"
operator|.
name|equals
argument_list|(
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check comment is about blacklisting"
argument_list|,
literal|"Artifact was in the specified list of exclusions"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check exclusion was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check jdbc not included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"javax.sql"
argument_list|,
literal|"jdbc"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testKickoutWithShortPath
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getKickedOutPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"invalid/invalid-1.0.jar"
operator|.
name|equals
argument_list|(
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check reason for kickout"
argument_list|,
literal|"Path does not match a legacy repository path for an artifact"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check kickout was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check not invalid-1.0.jar"
argument_list|,
literal|"invalid-1.0.jar"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testKickoutWithLongPath
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getKickedOutPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"invalid/jars/1.0/invalid-1.0.jar"
operator|.
name|equals
argument_list|(
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check reason for kickout"
argument_list|,
literal|"Path does not match a legacy repository path for an artifact"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check kickout was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check not invalid-1.0.jar"
argument_list|,
literal|"invalid-1.0.jar"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testKickoutWithInvalidType
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getKickedOutPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"invalid/foo/invalid-1.0.foo"
operator|.
name|equals
argument_list|(
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check reason for kickout"
argument_list|,
literal|"Path artifact type does not corresspond to an artifact type"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check kickout was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check not invalid-1.0.foo"
argument_list|,
literal|"invalid-1.0.foo"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testKickoutWithNoExtension
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getKickedOutPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"invalid/jars/no-extension"
operator|.
name|equals
argument_list|(
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check reason for kickout"
argument_list|,
literal|"Path filename does not have an extension"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check kickout was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check not 'no-extension'"
argument_list|,
literal|"no-extension"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testKickoutWithWrongExtension
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getKickedOutPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"invalid/jars/invalid-1.0.rar"
operator|.
name|equals
argument_list|(
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check reason for kickout"
argument_list|,
literal|"Path type does not match the extension"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check kickout was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check not 'invalid-1.0.rar'"
argument_list|,
literal|"invalid-1.0.rar"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testKickoutWithNoVersion
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|discoverer
operator|.
name|getKickedOutPathsIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|found
condition|;
control|)
block|{
name|DiscovererPath
name|dPath
init|=
operator|(
name|DiscovererPath
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dPath
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"invalid/jars/invalid.jar"
operator|.
name|equals
argument_list|(
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check reason for kickout"
argument_list|,
literal|"Path filename version is empty"
argument_list|,
name|dPath
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Check kickout was found"
argument_list|,
name|found
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check not 'invalid.jar'"
argument_list|,
literal|"invalid.jar"
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testInclusion
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check normal included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTextualVersion
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check normal included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"UNKNOWN"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactWithClassifier
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check normal included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"some-ejb"
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|,
literal|"client"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testJavaSourcesInclusion
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check normal included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"1.0"
argument_list|,
literal|"java-source"
argument_list|,
literal|"sources"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDistributionInclusion
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check zip included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"1.0"
argument_list|,
literal|"distribution-zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check tar.gz included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"1.0"
argument_list|,
literal|"distribution-tgz"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSnapshotInclusion
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check normal included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"javax.sql"
argument_list|,
literal|"jdbc"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check snapshot included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"1.0-20050611.112233-1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSnapshotExclusion
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check normal included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"javax.sql"
argument_list|,
literal|"jdbc"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check snapshot included"
argument_list|,
name|artifacts
operator|.
name|contains
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"1.0-20050611.112233-1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFileSet
parameter_list|()
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check file is set"
argument_list|,
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testRepositorySet
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check artifacts not null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|String
name|url
init|=
name|repository
operator|.
name|getUrl
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Check repository set"
argument_list|,
name|artifact
operator|.
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check repository url is correct"
argument_list|,
name|url
argument_list|,
name|artifact
operator|.
name|getRepository
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testWrongArtifactPackaging
parameter_list|()
throws|throws
name|ComponentLookupException
block|{
name|String
name|testPath
init|=
literal|"org.apache.maven.test/jars/artifactId-1.0.jar.md5"
decl_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifactFromPath
argument_list|(
name|testPath
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Artifact should be null for wrong package extension"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoArtifactid
parameter_list|()
block|{
name|String
name|testPath
init|=
literal|"groupId/jars/-1.0.jar"
decl_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifactFromPath
argument_list|(
name|testPath
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Artifact should be null when artifactId is missing"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|testPath
operator|=
literal|"groupId/jars/1.0.jar"
expr_stmt|;
name|artifact
operator|=
name|getArtifactFromPath
argument_list|(
name|testPath
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Artifact should be null when artifactId is missing"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoType
parameter_list|()
throws|throws
name|ComponentLookupException
block|{
name|String
name|testPath
init|=
literal|"invalid/invalid/1/invalid-1"
decl_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifactFromPath
argument_list|(
name|testPath
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Artifact should be null for no type"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSnapshot
parameter_list|()
throws|throws
name|ComponentLookupException
block|{
name|String
name|testPath
init|=
literal|"org.apache.maven.test/jars/maven-model-1.0-SNAPSHOT.jar"
decl_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifactFromPath
argument_list|(
name|testPath
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Artifact path with invalid snapshot error"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven.test"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|)
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFinal
parameter_list|()
throws|throws
name|ComponentLookupException
block|{
name|String
name|testPath
init|=
literal|"org.apache.maven.test/jars/maven-model-1.0-final-20060606.jar"
decl_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifactFromPath
argument_list|(
name|testPath
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Artifact path with invalid snapshot error"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven.test"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"1.0-final-20060606"
argument_list|)
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNormal
parameter_list|()
throws|throws
name|ComponentLookupException
block|{
name|String
name|testPath
init|=
literal|"javax.sql/jars/jdbc-2.0.jar"
decl_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifactFromPath
argument_list|(
name|testPath
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Normal artifact path error"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|createArtifact
argument_list|(
literal|"javax.sql"
argument_list|,
literal|"jdbc"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Artifact
name|getArtifactFromPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
return|return
name|discoverer
operator|.
name|buildArtifact
argument_list|(
name|path
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|DiscovererException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

