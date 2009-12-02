begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|xmlrpc
operator|.
name|client
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|xmlrpc
operator|.
name|AuthenticationInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|xmlrpc
operator|.
name|Binder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|xmlrpc
operator|.
name|BindingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|atlassian
operator|.
name|xmlrpc
operator|.
name|DefaultBinder
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
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|AdministrationService
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
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|PingService
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
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|beans
operator|.
name|RemoteRepository
import|;
end_import

begin_comment
comment|/**  * TestClient  *   * Test client for Archiva Web Services.   * To execute:  *   * 1. set the<arguments> in the exec-maven-plugin config in the pom.xml in the following order:  *    - url  *    - username  *    - password  * 2. execute 'mvn exec:java' from the command-line  *   * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|SampleClient
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|Binder
name|binder
init|=
operator|new
name|DefaultBinder
argument_list|()
decl_stmt|;
try|try
block|{
name|AuthenticationInfo
name|authnInfo
init|=
operator|new
name|AuthenticationInfo
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|,
name|args
index|[
literal|2
index|]
argument_list|)
decl_stmt|;
name|AdministrationService
name|adminService
init|=
name|binder
operator|.
name|bind
argument_list|(
name|AdministrationService
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|authnInfo
argument_list|)
decl_stmt|;
name|PingService
name|pingService
init|=
name|binder
operator|.
name|bind
argument_list|(
name|PingService
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|authnInfo
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Ping : "
operator|+
name|pingService
operator|.
name|ping
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|managedRepos
init|=
name|adminService
operator|.
name|getAllManagedRepositories
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n******** Managed Repositories ********"
argument_list|)
expr_stmt|;
for|for
control|(
name|ManagedRepository
name|managedRepo
range|:
name|managedRepos
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"================================="
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Id: "
operator|+
name|managedRepo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Name: "
operator|+
name|managedRepo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Layout: "
operator|+
name|managedRepo
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"URL: "
operator|+
name|managedRepo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Releases: "
operator|+
name|managedRepo
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Snapshots: "
operator|+
name|managedRepo
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n******** Remote Repositories ********"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|remoteRepos
init|=
name|adminService
operator|.
name|getAllRemoteRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|RemoteRepository
name|remoteRepo
range|:
name|remoteRepos
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"================================="
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Id: "
operator|+
name|remoteRepo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Name: "
operator|+
name|remoteRepo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Layout: "
operator|+
name|remoteRepo
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"URL: "
operator|+
name|remoteRepo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n******** Repository Consumers ********"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repoConsumers
init|=
name|adminService
operator|.
name|getAllRepositoryConsumers
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|consumer
range|:
name|repoConsumers
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
name|Boolean
name|success
init|=
name|adminService
operator|.
name|configureRepositoryConsumer
argument_list|(
literal|"internal"
argument_list|,
literal|"repository-purge"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nConfigured repo consumer 'repository-purge' : "
operator|+
operator|(
operator|(
name|Boolean
operator|)
name|success
operator|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|success
operator|=
name|adminService
operator|.
name|executeRepositoryScanner
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nExecuted repo scanner of repository 'internal' : "
operator|+
operator|(
operator|(
name|Boolean
operator|)
name|success
operator|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
comment|/* delete artifact */
comment|/*               * NOTE: before enabling& invoking deleteArtifact, make sure that the repository and artifact exists first!              *                                   success = adminService.deleteArtifact( "internal", "javax.activation", "activation", "1.1" );             System.out.println( "\nDeleted artifact 'javax.activation:activation:1.1' from repository 'internal' : " +                 ( (Boolean) success ).booleanValue() );             */
comment|/* quick search */
comment|/*              * NOTE: before enabling& invoking search service, make sure that the artifacts you're searching              *      for has been indexed already in order to get results              *                     SearchService searchService = binder.bind( SearchService.class, new URL( args[0] ), authnInfo );             List<Artifact> artifacts = searchService.quickSearch( "org" );                          System.out.println( "\n************ Search Results for 'org' *************" );             for( Artifact artifact : artifacts )             {                 System.out.println( "Artifact: " + artifact.getGroupId() + ":" + artifact.getArtifactId() +                                     ":" + artifact.getVersion() );             }                          */
block|}
catch|catch
parameter_list|(
name|BindingException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

