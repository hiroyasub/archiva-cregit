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
name|web
operator|.
name|test
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|cli
operator|.
name|CommandLineUtils
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
name|cli
operator|.
name|Commandline
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
name|cli
operator|.
name|StreamConsumer
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
name|cli
operator|.
name|WriterStreamConsumer
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
name|commandline
operator|.
name|ExecutableResolver
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
name|commandline
operator|.
name|DefaultExecutableResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jdom
operator|.
name|input
operator|.
name|SAXBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jdom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jdom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jdom
operator|.
name|output
operator|.
name|XMLOutputter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jdom
operator|.
name|xpath
operator|.
name|XPath
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
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|Collections
import|;
end_import

begin_comment
comment|/**  * Test maven connection to archiva  *  */
end_comment

begin_class
specifier|public
class|class
name|MavenConnectionTest
extends|extends
name|AbstractArchivaTestCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_ARCHIVA_XML
init|=
literal|"/target/appserver-base/conf/archiva.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_SETTINGS_XML
init|=
literal|"/target/local-repo/settings.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NEW_LOCAL_REPO_VALUE
init|=
literal|"/target/local-repo"
decl_stmt|;
comment|/**      * @throws Exception      */
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
name|String
name|newValue
init|=
name|getBasedir
argument_list|()
operator|+
name|NEW_LOCAL_REPO_VALUE
decl_stmt|;
name|updateXml
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
name|PATH_TO_ARCHIVA_XML
argument_list|)
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
name|updateXml
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
name|PATH_TO_SETTINGS_XML
argument_list|)
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Update localRepository element value      *      * @param f      * @param newValue      * @throws Exception      */
specifier|private
name|void
name|updateXml
parameter_list|(
name|File
name|f
parameter_list|,
name|String
name|newValue
parameter_list|)
throws|throws
name|Exception
block|{
name|SAXBuilder
name|builder
init|=
operator|new
name|SAXBuilder
argument_list|()
decl_stmt|;
name|FileReader
name|reader
init|=
operator|new
name|FileReader
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|Document
name|document
init|=
name|builder
operator|.
name|build
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|Element
name|localRepository
init|=
operator|(
name|Element
operator|)
name|XPath
operator|.
name|newInstance
argument_list|(
literal|"./"
operator|+
literal|"localRepository"
argument_list|)
operator|.
name|selectSingleNode
argument_list|(
name|document
operator|.
name|getRootElement
argument_list|()
argument_list|)
decl_stmt|;
name|localRepository
operator|.
name|setText
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
comment|// re-write xml file
name|FileWriter
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|XMLOutputter
name|output
init|=
operator|new
name|XMLOutputter
argument_list|()
decl_stmt|;
name|output
operator|.
name|output
argument_list|(
name|document
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|clickManagedRepositories
parameter_list|()
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|submitLoginPage
argument_list|(
name|adminUsername
argument_list|,
name|adminPassword
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Managed Repositories"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Administration"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Administration"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|removeManagedRepository
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|clickManagedRepositories
argument_list|()
expr_stmt|;
name|clickLinkWithLocator
argument_list|(
literal|"//a[contains(@href, '/admin/deleteRepository!input.action?repoId="
operator|+
name|id
operator|+
literal|"')]"
argument_list|)
expr_stmt|;
name|clickLinkWithLocator
argument_list|(
literal|"deleteRepository_operationdelete-contents"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Go"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Administration"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Click Settings from the navigation menu      */
specifier|private
name|void
name|clickProxiedRepositories
parameter_list|()
block|{
name|goToLoginPage
argument_list|()
expr_stmt|;
name|submitLoginPage
argument_list|(
name|adminUsername
argument_list|,
name|adminPassword
argument_list|)
expr_stmt|;
name|clickLinkWithText
argument_list|(
literal|"Proxied Repositories"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Administration"
argument_list|)
expr_stmt|;
name|assertTextPresent
argument_list|(
literal|"Proxied Repositories"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Remove the created test repo      */
specifier|protected
name|void
name|removeProxiedRepository
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isLinkPresent
argument_list|(
literal|"Login"
argument_list|)
condition|)
block|{
name|logout
argument_list|()
expr_stmt|;
block|}
name|clickProxiedRepositories
argument_list|()
expr_stmt|;
if|if
condition|(
name|isTextPresent
argument_list|(
literal|"Delete Repository "
argument_list|)
condition|)
block|{
name|clickLinkWithText
argument_list|(
literal|"Delete Repository"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Configuration"
argument_list|)
expr_stmt|;
name|clickLinkWithLocator
argument_list|(
literal|"deleteProxiedRepository_operationdelete-entry"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|clickButtonWithValue
argument_list|(
literal|"Go"
argument_list|)
expr_stmt|;
name|assertPage
argument_list|(
literal|"Administration"
argument_list|)
expr_stmt|;
name|assertTextNotPresent
argument_list|(
literal|"Test Proxied Repository"
argument_list|)
expr_stmt|;
block|}
name|logout
argument_list|()
expr_stmt|;
block|}
comment|/**      * Execute 'mvn' from commandline      *      * @param workingDir      * @param outputFile      * @return      * @throws Exception      */
specifier|private
name|int
name|executeMaven
parameter_list|(
name|String
name|workingDir
parameter_list|,
name|File
name|outputFile
parameter_list|)
throws|throws
name|Exception
block|{
name|ExecutableResolver
name|executableResolver
init|=
operator|new
name|DefaultExecutableResolver
argument_list|()
decl_stmt|;
name|String
name|actualExecutable
init|=
literal|"mvn"
decl_stmt|;
name|File
name|workingDirectory
init|=
operator|new
name|File
argument_list|(
name|workingDir
argument_list|)
decl_stmt|;
name|List
name|path
init|=
name|executableResolver
operator|.
name|getDefaultPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
name|Collections
operator|.
name|EMPTY_LIST
expr_stmt|;
block|}
name|File
name|e
init|=
name|executableResolver
operator|.
name|findExecutable
argument_list|(
literal|"mvn"
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|actualExecutable
operator|=
name|e
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
name|File
name|actualExecutableFile
init|=
operator|new
name|File
argument_list|(
name|actualExecutable
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|actualExecutableFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|actualExecutable
operator|=
literal|"mvn"
expr_stmt|;
block|}
comment|// Set command line
name|Commandline
name|cmd
init|=
operator|new
name|Commandline
argument_list|()
decl_stmt|;
name|cmd
operator|.
name|addSystemEnvironment
argument_list|()
expr_stmt|;
name|cmd
operator|.
name|addEnvironment
argument_list|(
literal|"MAVEN_TERMINATE_CMD"
argument_list|,
literal|"on"
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setExecutable
argument_list|(
name|actualExecutable
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setWorkingDirectory
argument_list|(
name|workingDirectory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|createArgument
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"clean"
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|createArgument
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|createArgument
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"-s"
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|createArgument
argument_list|()
operator|.
name|setValue
argument_list|(
name|getBasedir
argument_list|()
operator|+
literal|"/target/local-repo/settings.xml"
argument_list|)
expr_stmt|;
comment|// Excute command
name|Writer
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|outputFile
argument_list|)
decl_stmt|;
name|StreamConsumer
name|consumer
init|=
operator|new
name|WriterStreamConsumer
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|int
name|exitCode
init|=
name|CommandLineUtils
operator|.
name|executeCommandLine
argument_list|(
name|cmd
argument_list|,
name|consumer
argument_list|,
name|consumer
argument_list|)
decl_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|exitCode
return|;
block|}
specifier|public
name|void
name|testBadDependency
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/projects/bad-dependency/bad-dependency.log"
argument_list|)
decl_stmt|;
name|int
name|exitCode
init|=
name|executeMaven
argument_list|(
name|getBasedir
argument_list|()
operator|+
literal|"/target/projects/bad-dependency"
argument_list|,
name|outputFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exitCode
argument_list|)
expr_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/local-repo/org/apache/mavem/archiva/web/test/foo-bar/1.0-SNAPSHOT/foo-bar-1.0-SNAPSHOT.jar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|f
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|outputFile
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|str
decl_stmt|;
name|boolean
name|foundSnapshot
init|=
literal|false
decl_stmt|,
name|foundBadDep
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|(
name|str
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
comment|//System.out.println( str );
if|if
condition|(
name|str
operator|.
name|indexOf
argument_list|(
literal|"mvn install:install-file -DgroupId=org.apache.maven.archiva.web.test -DartifactId=foo-bar"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|foundSnapshot
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|str
operator|.
name|indexOf
argument_list|(
literal|"mvn install:install-file -DgroupId=org.apache.maven.archiva.web.test -DartifactId=bad-dependency"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|foundBadDep
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|foundSnapshot
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foundBadDep
argument_list|)
expr_stmt|;
block|}
comment|/*     @todo: commented out since tests are currently failing due to MRM-323      public void testDownloadArtifactFromManagedRepo()         throws Exception     {         clickManagedRepositories();                  clickLinkWithText( "Add Repository" );         assertTextPresent( "Configuration" );          setFieldValue( "addRepository_id", "snapshots" );         setFieldValue( "urlName", "snapshots" );         setFieldValue( "addRepository_name", "snapshots-repository" );         setFieldValue( "addRepository_directory", getBasedir() + "/target/snapshots" );          clickButtonWithValue( "Add Repository" );         assertPage( "Administration" );          clickLinkWithText( "User Management" );         clickLinkWithLocator( "//a[contains(@href, '/security/useredit.action?username=admin')]" );         clickLinkWithText( "Edit Roles" );         checkField( "addRolesToUser_addSelectedRolesRepository Observer - snapshots" );         checkField( "addRolesToUser_addSelectedRolesRepository Manager - snapshots" );          clickButtonWithValue( "Add Selected Roles" );         assertPage( "[Admin] User List" );          logout();                 File outputFile = new File( getBasedir(), "/target/projects/bad-dependency/bad-dependency2.log" );         int exitCode = executeMaven( getBasedir() + "/target/projects/bad-dependency",             outputFile );          assertEquals( 0, exitCode );          File f = new File( getBasedir(),             "/target/local-repo/org/apache/maven/archiva/web/test/foo-bar-1.0-SNAPSHOT.jar" );         assertTrue( f.exists() );          BufferedReader reader = new BufferedReader( new FileReader( outputFile ) );         String str;                           while( ( str = reader.readLine() ) != null)         {             System.out.println( str );         }         reader.close();          removeManagedRepository( "snapshots" );     }       public void testDownloadArtifactFromProxiedRepo()         throws Exception     {         //add managed repository         clickManagedRepositories();          clickLinkWithText( "Add Repository" );         assertTextPresent( "Configuration" );          setFieldValue( "addRepository_id", "repository" );         setFieldValue( "urlName", "repository" );         setFieldValue( "addRepository_name", "repository" );         setFieldValue( "addRepository_directory", getBasedir() + "/target/repository" );                  clickButtonWithValue( "Add Repository" );         waitPage();         assertPage( "Administration" );          clickLinkWithText( "User Management" );         clickLinkWithLocator( "//a[contains(@href, '/security/useredit.action?username=admin')]" );         clickLinkWithText( "Edit Roles" );         checkField( "addRolesToUser_addSelectedRolesRepository Observer - repository" );         checkField( "addRolesToUser_addSelectedRolesRepository Manager - repository" );          clickButtonWithValue( "Add Selected Roles" );         assertPage( "[Admin] User List" );         logout();          //add proxied repository         clickProxiedRepositories();         clickLinkWithText( "Add Repository" );         assertPage( "Configuration" );         setFieldValue( "id", "central" );         setFieldValue( "name", "Central Repository" );         setFieldValue( "url", "http://mirrors.ibiblio.org/pub/mirrors/maven2" );         clickButtonWithValue( "Add Repository" );         waitPage();          assertPage( "Administration" );         assertTextPresent( "Central Repository" );         assertLinkPresent( "Edit Repository" );          logout();          File outputFile = new File( getBasedir(), "/target/projects/dependency-in-proxied/dependency-in-proxied.log" );         int exitCode = executeMaven( getBasedir() + "/target/projects/dependency-in-proxied",             outputFile );          assertEquals( 0, exitCode );          File f = new File( getBasedir(),"/target/repository/com/lowagie/itext/1.3/itext-1.3.jar" );         assertTrue( f.exists() );          f = new File( getBasedir(), "/target/local-repo/com/lowagie/itext/1.3/itext-1.3.jar" );         assertTrue( f.exists() );           BufferedReader reader = new BufferedReader( new FileReader( outputFile ) );         String str;          while( ( str = reader.readLine() ) != null)         {             System.out.println( str );         }         reader.close();          removeProxiedRepository();         removeManagedRepository( "repository" );             }      */
comment|/**      * @throws Exception      */
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
block|}
block|}
end_class

end_unit

