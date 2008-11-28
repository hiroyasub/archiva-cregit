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
name|lucene
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|List
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
name|Configuration
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
name|database
operator|.
name|updater
operator|.
name|DatabaseUnprocessedArtifactConsumer
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
name|indexer
operator|.
name|search
operator|.
name|SearchResultLimits
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
name|search
operator|.
name|SearchResults
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
name|ArchivaArtifactModel
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
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_comment
comment|/**  *   * @version  *  */
end_comment

begin_class
specifier|public
class|class
name|IndexJavaPublicMethodsConsumerTest
extends|extends
name|PlexusInSpringTestCase
block|{
name|DatabaseUnprocessedArtifactConsumer
name|indexMethodsConsumer
decl_stmt|;
name|IndexJavaPublicMethodsCrossRepositorySearch
name|searcher
decl_stmt|;
specifier|private
name|RepositoryContentIndexFactory
name|indexFactory
decl_stmt|;
specifier|public
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
name|indexMethodsConsumer
operator|=
operator|(
name|DatabaseUnprocessedArtifactConsumer
operator|)
name|lookup
argument_list|(
name|DatabaseUnprocessedArtifactConsumer
operator|.
name|class
argument_list|,
literal|"index-public-methods"
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|config
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setLocation
argument_list|(
name|getBasedir
argument_list|()
operator|+
literal|"/target/test-classes/test-repo"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setName
argument_list|(
literal|"Test Repository"
argument_list|)
expr_stmt|;
name|addRepoToConfiguration
argument_list|(
literal|"index-public-methods"
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|indexFactory
operator|=
operator|(
name|RepositoryContentIndexFactory
operator|)
name|lookup
argument_list|(
name|RepositoryContentIndexFactory
operator|.
name|class
argument_list|,
literal|"lucene"
argument_list|)
expr_stmt|;
name|searcher
operator|=
operator|new
name|IndexJavaPublicMethodsCrossRepositorySearch
argument_list|(
name|config
argument_list|,
name|indexFactory
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addRepoToConfiguration
parameter_list|(
name|String
name|configHint
parameter_list|,
name|ManagedRepositoryConfiguration
name|repoConfiguration
parameter_list|)
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
name|configHint
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|removeManagedRepository
argument_list|(
name|configuration
operator|.
name|findManagedRepositoryById
argument_list|(
name|repoConfiguration
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|repoConfiguration
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testJarPublicMethods
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"archiva-index-methods-jar-test"
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|indexMethodsConsumer
operator|.
name|processArchivaArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|selectedRepos
operator|.
name|add
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
comment|// search for class names
name|SearchResults
name|results
init|=
name|searcher
operator|.
name|searchForBytecode
argument_list|(
literal|""
argument_list|,
name|selectedRepos
argument_list|,
literal|"FirstPackageApp"
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|getTotalHits
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|=
name|searcher
operator|.
name|searchForBytecode
argument_list|(
literal|""
argument_list|,
name|selectedRepos
argument_list|,
literal|"SecondPackageApp"
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|getTotalHits
argument_list|()
argument_list|)
expr_stmt|;
comment|// search for public methods
name|results
operator|=
name|searcher
operator|.
name|searchForBytecode
argument_list|(
literal|""
argument_list|,
name|selectedRepos
argument_list|,
literal|"appMethodOne"
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|getTotalHits
argument_list|()
argument_list|)
expr_stmt|;
comment|// should return only the overridding public method in SecondPackageApp
name|results
operator|=
name|searcher
operator|.
name|searchForBytecode
argument_list|(
literal|""
argument_list|,
name|selectedRepos
argument_list|,
literal|"protectedMethod"
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|getTotalHits
argument_list|()
argument_list|)
expr_stmt|;
comment|// should not return any private methods
name|results
operator|=
name|searcher
operator|.
name|searchForBytecode
argument_list|(
literal|""
argument_list|,
name|selectedRepos
argument_list|,
literal|"privMethod"
argument_list|,
operator|new
name|SearchResultLimits
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|results
operator|.
name|getTotalHits
argument_list|()
argument_list|)
expr_stmt|;
comment|// test for public variables?
block|}
specifier|private
name|ArchivaArtifact
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
name|ArchivaArtifactModel
name|model
init|=
operator|new
name|ArchivaArtifactModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|model
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|model
operator|.
name|setRepositoryId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ArchivaArtifact
argument_list|(
name|model
argument_list|)
return|;
block|}
block|}
end_class

end_unit

