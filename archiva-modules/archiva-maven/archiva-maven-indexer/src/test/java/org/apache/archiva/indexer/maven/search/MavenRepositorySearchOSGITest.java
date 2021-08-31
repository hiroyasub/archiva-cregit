begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|maven
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
name|archiva
operator|.
name|indexer
operator|.
name|search
operator|.
name|SearchFields
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|search
operator|.
name|SearchResultHit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
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
name|archiva
operator|.
name|repository
operator|.
name|base
operator|.
name|ArchivaRepositoryRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|base
operator|.
name|RepositoryHandlerDependencies
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|Collections
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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|MavenRepositorySearchOSGITest
extends|extends
name|AbstractMavenRepositorySearch
block|{
annotation|@
name|Inject
name|ArchivaRepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|Inject
name|RepositoryHandlerDependencies
name|repositoryHandlerDependencies
decl_stmt|;
annotation|@
name|After
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|repositoryRegistry
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|searchFelixWithSymbolicName
parameter_list|()
throws|throws
name|Exception
block|{
name|createIndex
argument_list|(
name|TEST_REPO_1
argument_list|,
name|Collections
operator|.
expr|<
name|Path
operator|>
name|emptyList
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_REPO_1
argument_list|)
decl_stmt|;
comment|// search artifactId
comment|// EasyMock.expect( archivaConfig.getDefaultLocale() ).andReturn( Locale.getDefault( ) ).anyTimes();
name|EasyMock
operator|.
name|expect
argument_list|(
name|archivaConfig
operator|.
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|config
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|archivaConfigControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SearchFields
name|searchFields
init|=
operator|new
name|SearchFields
argument_list|()
decl_stmt|;
name|searchFields
operator|.
name|setBundleSymbolicName
argument_list|(
literal|"org.apache.felix.bundlerepository"
argument_list|)
expr_stmt|;
name|searchFields
operator|.
name|setBundleVersion
argument_list|(
literal|"1.6.6"
argument_list|)
expr_stmt|;
name|searchFields
operator|.
name|setRepositories
argument_list|(
name|selectedRepos
argument_list|)
expr_stmt|;
name|SearchResults
name|results
init|=
name|search
operator|.
name|search
argument_list|(
literal|"user"
argument_list|,
name|searchFields
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|archivaConfigControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|results
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
name|SearchResultHit
name|hit
init|=
name|results
operator|.
name|getHits
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.felix"
argument_list|,
name|hit
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.felix.bundlerepository"
argument_list|,
name|hit
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.6.6"
argument_list|,
name|hit
operator|.
name|getVersions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.felix.bundlerepository;uses:=\"org.osgi.framework\";version=\"2.0\""
argument_list|,
name|hit
operator|.
name|getBundleExportPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.felix.bundlerepository.RepositoryAdmin,org.osgi.service.obr.RepositoryAdmin"
argument_list|,
name|hit
operator|.
name|getBundleExportService
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.felix.bundlerepository"
argument_list|,
name|hit
operator|.
name|getBundleSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.6.6"
argument_list|,
name|hit
operator|.
name|getBundleVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

