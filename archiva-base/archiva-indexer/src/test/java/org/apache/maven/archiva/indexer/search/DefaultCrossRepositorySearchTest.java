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
name|indexer
operator|.
name|search
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
name|lucene
operator|.
name|search
operator|.
name|Hits
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|MatchAllDocsQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Searcher
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|MockConfiguration
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
name|RepositoryContentIndex
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
name|RepositoryContentIndexFactory
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
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
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
name|Map
import|;
end_import

begin_comment
comment|/**  * DefaultCrossRepositorySearchTest  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DefaultCrossRepositorySearchTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_DEFAULT_REPOSITORY_NAME
init|=
literal|"Test Default Repository"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_DEFAULT_REPO_ID
init|=
literal|"testDefaultRepo"
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
name|RepositoryContentIndexFactory
name|indexFactory
init|=
operator|(
name|RepositoryContentIndexFactory
operator|)
name|lookup
argument_list|(
name|RepositoryContentIndexFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"lucene"
argument_list|)
decl_stmt|;
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/managed-repository"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Default Test Repository should exist."
argument_list|,
name|repoDir
operator|.
name|exists
argument_list|()
operator|&&
name|repoDir
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|repoUri
init|=
literal|"file://"
operator|+
name|StringUtils
operator|.
name|replace
argument_list|(
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"\\"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|ArchivaRepository
name|repository
init|=
operator|new
name|ArchivaRepository
argument_list|(
name|TEST_DEFAULT_REPO_ID
argument_list|,
name|TEST_DEFAULT_REPOSITORY_NAME
argument_list|,
name|repoUri
argument_list|)
decl_stmt|;
name|File
name|indexLocation
init|=
operator|new
name|File
argument_list|(
literal|"target/index-crossrepo-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/"
argument_list|)
decl_stmt|;
name|MockConfiguration
name|config
init|=
operator|(
name|MockConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"mock"
argument_list|)
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfig
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repoConfig
operator|.
name|setId
argument_list|(
name|TEST_DEFAULT_REPO_ID
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setName
argument_list|(
name|TEST_DEFAULT_REPOSITORY_NAME
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLocation
argument_list|(
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setIndexDir
argument_list|(
name|indexLocation
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setIndexed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|indexLocation
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexLocation
argument_list|)
expr_stmt|;
block|}
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addManagedRepository
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
comment|// Create the (empty) indexes.
name|RepositoryContentIndex
name|indexHashcode
init|=
name|indexFactory
operator|.
name|createHashcodeIndex
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|RepositoryContentIndex
name|indexBytecode
init|=
name|indexFactory
operator|.
name|createBytecodeIndex
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|RepositoryContentIndex
name|indexContents
init|=
name|indexFactory
operator|.
name|createFileContentIndex
argument_list|(
name|repository
argument_list|)
decl_stmt|;
comment|// Now populate them.
name|Map
name|hashcodesMap
init|=
operator|(
operator|new
name|HashcodesIndexPopulator
argument_list|()
operator|)
operator|.
name|populate
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|indexHashcode
operator|.
name|indexRecords
argument_list|(
name|hashcodesMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hashcode Key Count"
argument_list|,
name|hashcodesMap
operator|.
name|size
argument_list|()
argument_list|,
name|indexHashcode
operator|.
name|getAllRecordKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertRecordCount
argument_list|(
name|indexHashcode
argument_list|,
name|hashcodesMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|bytecodeMap
init|=
operator|(
operator|new
name|BytecodeIndexPopulator
argument_list|()
operator|)
operator|.
name|populate
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|indexBytecode
operator|.
name|indexRecords
argument_list|(
name|bytecodeMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bytecode Key Count"
argument_list|,
name|bytecodeMap
operator|.
name|size
argument_list|()
argument_list|,
name|indexBytecode
operator|.
name|getAllRecordKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertRecordCount
argument_list|(
name|indexBytecode
argument_list|,
name|bytecodeMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|contentMap
init|=
operator|(
operator|new
name|FileContentIndexPopulator
argument_list|()
operator|)
operator|.
name|populate
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|indexContents
operator|.
name|indexRecords
argument_list|(
name|contentMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"File Content Key Count"
argument_list|,
name|contentMap
operator|.
name|size
argument_list|()
argument_list|,
name|indexContents
operator|.
name|getAllRecordKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertRecordCount
argument_list|(
name|indexContents
argument_list|,
name|contentMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertRecordCount
parameter_list|(
name|RepositoryContentIndex
name|index
parameter_list|,
name|int
name|expectedCount
parameter_list|)
throws|throws
name|Exception
block|{
name|Query
name|query
init|=
operator|new
name|MatchAllDocsQuery
argument_list|()
decl_stmt|;
name|Searcher
name|searcher
init|=
operator|(
name|Searcher
operator|)
name|index
operator|.
name|getSearchable
argument_list|()
decl_stmt|;
name|Hits
name|hits
init|=
name|searcher
operator|.
name|search
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Expected Record Count for "
operator|+
name|index
operator|.
name|getId
argument_list|()
argument_list|,
name|expectedCount
argument_list|,
name|hits
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|CrossRepositorySearch
name|lookupCrossRepositorySearch
parameter_list|()
throws|throws
name|Exception
block|{
name|CrossRepositorySearch
name|search
init|=
operator|(
name|CrossRepositorySearch
operator|)
name|lookup
argument_list|(
name|CrossRepositorySearch
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"CrossRepositorySearch:default should not be null."
argument_list|,
name|search
argument_list|)
expr_stmt|;
return|return
name|search
return|;
block|}
specifier|public
name|void
name|testSearchTerm_Org
parameter_list|()
throws|throws
name|Exception
block|{
name|CrossRepositorySearch
name|search
init|=
name|lookupCrossRepositorySearch
argument_list|()
decl_stmt|;
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|limits
operator|.
name|setPageSize
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|SearchResults
name|results
init|=
name|search
operator|.
name|searchForTerm
argument_list|(
literal|"org"
argument_list|,
name|limits
argument_list|)
decl_stmt|;
name|assertResults
argument_list|(
literal|1
argument_list|,
literal|7
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSearchTerm_Junit
parameter_list|()
throws|throws
name|Exception
block|{
name|CrossRepositorySearch
name|search
init|=
name|lookupCrossRepositorySearch
argument_list|()
decl_stmt|;
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|limits
operator|.
name|setPageSize
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|SearchResults
name|results
init|=
name|search
operator|.
name|searchForTerm
argument_list|(
literal|"junit"
argument_list|,
name|limits
argument_list|)
decl_stmt|;
name|assertResults
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSearchInvalidTerm
parameter_list|()
throws|throws
name|Exception
block|{
name|CrossRepositorySearch
name|search
init|=
name|lookupCrossRepositorySearch
argument_list|()
decl_stmt|;
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|limits
operator|.
name|setPageSize
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|SearchResults
name|results
init|=
name|search
operator|.
name|searchForTerm
argument_list|(
literal|"monosodium"
argument_list|,
name|limits
argument_list|)
decl_stmt|;
name|assertResults
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertResults
parameter_list|(
name|int
name|repoCount
parameter_list|,
name|int
name|hitCount
parameter_list|,
name|SearchResults
name|results
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Search Results should not be null."
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository Hits"
argument_list|,
name|repoCount
argument_list|,
name|results
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Search Result Hits"
argument_list|,
name|hitCount
argument_list|,
name|results
operator|.
name|getHits
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

