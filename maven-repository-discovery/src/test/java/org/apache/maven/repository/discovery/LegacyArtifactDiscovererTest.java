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
name|PlexusTestCase
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
comment|/**  * Test the legacy artifact discoverer.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @version $Id$  * @todo share as much as possible with default via abstract test case  */
end_comment

begin_class
specifier|public
class|class
name|LegacyArtifactDiscovererTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ArtifactDiscoverer
name|discoverer
decl_stmt|;
specifier|private
name|ArtifactFactory
name|factory
decl_stmt|;
specifier|private
name|File
name|repositoryLocation
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|discoverer
operator|=
operator|(
name|ArtifactDiscoverer
operator|)
name|lookup
argument_list|(
name|ArtifactDiscoverer
operator|.
name|ROLE
argument_list|,
literal|"legacy"
argument_list|)
expr_stmt|;
name|factory
operator|=
operator|(
name|ArtifactFactory
operator|)
name|lookup
argument_list|(
name|ArtifactFactory
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|repositoryLocation
operator|=
name|getTestFile
argument_list|(
literal|"src/test/legacy-repository"
argument_list|)
expr_stmt|;
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|indexOf
argument_list|(
literal|".svn"
argument_list|)
operator|>=
literal|0
expr_stmt|;
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|equals
argument_list|(
literal|"KEYS"
argument_list|)
expr_stmt|;
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
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"KEYS"
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|equals
argument_list|(
literal|"javax.sql/jars/jdbc-2.0.jar"
argument_list|)
expr_stmt|;
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|equals
argument_list|(
literal|"invalid/invalid-1.0.jar"
argument_list|)
expr_stmt|;
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
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"invalid-1.0.jar"
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|equals
argument_list|(
literal|"invalid/jars/1.0/invalid-1.0.jar"
argument_list|)
expr_stmt|;
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
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"invalid-1.0.jar"
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|equals
argument_list|(
literal|"invalid/foo/invalid-1.0.foo"
argument_list|)
expr_stmt|;
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
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"invalid-1.0.foo"
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|equals
argument_list|(
literal|"invalid/jars/no-extension"
argument_list|)
expr_stmt|;
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
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"no-extension"
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|equals
argument_list|(
literal|"invalid/jars/invalid-1.0.rar"
argument_list|)
expr_stmt|;
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
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"invalid-1.0.rar"
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
name|repositoryLocation
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
name|String
name|path
init|=
operator|(
name|String
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|found
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|equals
argument_list|(
literal|"invalid/jars/invalid.jar"
argument_list|)
expr_stmt|;
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
name|a
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"invalid.jar"
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
name|repositoryLocation
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
name|repositoryLocation
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
name|repositoryLocation
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
name|repositoryLocation
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
name|repositoryLocation
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
name|repositoryLocation
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
name|repositoryLocation
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
specifier|private
name|Artifact
name|createArtifact
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
block|{
return|return
name|factory
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
return|;
block|}
specifier|private
name|Artifact
name|createArtifact
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
name|type
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
specifier|private
name|Artifact
name|createArtifact
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
name|type
parameter_list|,
name|String
name|classifier
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|)
return|;
block|}
block|}
end_class

end_unit

