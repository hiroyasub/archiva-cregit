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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|xpath
operator|.
name|XPath
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
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|IOUtil
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
name|Iterator
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
name|FileReader
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  */
end_comment

begin_class
specifier|public
class|class
name|CleanupReleasedSnapshotsRepositoryPurgeTest
extends|extends
name|AbstractRepositoryPurgeTest
block|{
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
name|repoPurge
operator|=
operator|new
name|CleanupReleasedSnapshotsRepositoryPurge
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|getLayout
argument_list|()
argument_list|,
name|dao
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReleasedSnapshots
parameter_list|()
throws|throws
name|Exception
block|{
name|populateReleasedSnapshotsTest
argument_list|()
expr_stmt|;
name|File
name|testDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectoryToDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test-classes/test-repo"
argument_list|)
argument_list|,
name|testDir
argument_list|)
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|PATH_TO_RELEASED_SNAPSHOT
argument_list|)
expr_stmt|;
comment|// check if the snapshot was removed
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3-SNAPSHOT"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3-SNAPSHOT/maven-plugin-plugin-2.3-SNAPSHOT.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check if the released version was not removed
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3-sources.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3-sources.jar.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3-sources.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3.jar.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3.pom.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/2.3/maven-plugin-plugin-2.3.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check if metadata file was updated
name|File
name|artifactMetadataFile
init|=
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-plugin-plugin/maven-metadata-local.xml"
argument_list|)
decl_stmt|;
name|FileReader
name|fileReader
init|=
operator|new
name|FileReader
argument_list|(
name|artifactMetadataFile
argument_list|)
decl_stmt|;
name|Document
name|document
decl_stmt|;
try|try
block|{
name|SAXBuilder
name|builder
init|=
operator|new
name|SAXBuilder
argument_list|()
decl_stmt|;
name|document
operator|=
name|builder
operator|.
name|build
argument_list|(
name|fileReader
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|fileReader
argument_list|)
expr_stmt|;
block|}
comment|// parse the metadata file
name|XPath
name|xPath
init|=
name|XPath
operator|.
name|newInstance
argument_list|(
literal|"//metadata/versioning"
argument_list|)
decl_stmt|;
name|Element
name|rootElement
init|=
name|document
operator|.
name|getRootElement
argument_list|()
decl_stmt|;
name|Element
name|versioning
init|=
operator|(
name|Element
operator|)
name|xPath
operator|.
name|selectSingleNode
argument_list|(
name|rootElement
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|xPath
operator|.
name|newInstance
argument_list|(
literal|"./latest"
argument_list|)
operator|.
name|selectSingleNode
argument_list|(
name|versioning
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2.3"
argument_list|,
name|el
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|el
operator|=
operator|(
name|Element
operator|)
name|xPath
operator|.
name|newInstance
argument_list|(
literal|"./lastUpdated"
argument_list|)
operator|.
name|selectSingleNode
argument_list|(
name|versioning
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|el
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
literal|"20070315032817"
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|nodes
init|=
name|xPath
operator|.
name|newInstance
argument_list|(
literal|"./versions"
argument_list|)
operator|.
name|selectNodes
argument_list|(
name|rootElement
argument_list|)
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|nodes
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|el
operator|=
operator|(
name|Element
operator|)
name|iter
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|el
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
literal|"2.3-SNAPSHOT"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertFalse
argument_list|(
name|found
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|testDir
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testHigherSnapshotExists
parameter_list|()
throws|throws
name|Exception
block|{
name|populateHigherSnapshotExistsTest
argument_list|()
expr_stmt|;
name|File
name|testDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectoryToDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test-classes/test-repo"
argument_list|)
argument_list|,
name|testDir
argument_list|)
expr_stmt|;
name|repoPurge
operator|.
name|process
argument_list|(
name|PATH_TO_HIGHER_SNAPSHOT_EXISTS
argument_list|)
expr_stmt|;
comment|// check if the snapshot was removed
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.3-SNAPSHOT"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.jar.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.pom.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.3-SNAPSHOT/maven-source-plugin-2.0.3-SNAPSHOT.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check if the released version was not removed
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.4-SNAPSHOT"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.jar.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.pom.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/2.0.4-SNAPSHOT/maven-source-plugin-2.0.4-SNAPSHOT.pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check if metadata file was updated
name|File
name|artifactMetadataFile
init|=
operator|new
name|File
argument_list|(
literal|"target/test/test-repo/org/apache/maven/plugins/maven-source-plugin/maven-metadata-local.xml"
argument_list|)
decl_stmt|;
name|FileReader
name|fileReader
init|=
operator|new
name|FileReader
argument_list|(
name|artifactMetadataFile
argument_list|)
decl_stmt|;
name|Document
name|document
decl_stmt|;
try|try
block|{
name|SAXBuilder
name|builder
init|=
operator|new
name|SAXBuilder
argument_list|()
decl_stmt|;
name|document
operator|=
name|builder
operator|.
name|build
argument_list|(
name|fileReader
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|fileReader
argument_list|)
expr_stmt|;
block|}
comment|// parse the metadata file
name|XPath
name|xPath
init|=
name|XPath
operator|.
name|newInstance
argument_list|(
literal|"//metadata/versioning"
argument_list|)
decl_stmt|;
name|Element
name|rootElement
init|=
name|document
operator|.
name|getRootElement
argument_list|()
decl_stmt|;
name|Element
name|versioning
init|=
operator|(
name|Element
operator|)
name|xPath
operator|.
name|selectSingleNode
argument_list|(
name|rootElement
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|xPath
operator|.
name|newInstance
argument_list|(
literal|"./latest"
argument_list|)
operator|.
name|selectSingleNode
argument_list|(
name|versioning
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2.0.4-SNAPSHOT"
argument_list|,
name|el
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|el
operator|=
operator|(
name|Element
operator|)
name|xPath
operator|.
name|newInstance
argument_list|(
literal|"./lastUpdated"
argument_list|)
operator|.
name|selectSingleNode
argument_list|(
name|versioning
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|el
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
literal|"20070427033345"
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|nodes
init|=
name|xPath
operator|.
name|newInstance
argument_list|(
literal|"./versions"
argument_list|)
operator|.
name|selectNodes
argument_list|(
name|rootElement
argument_list|)
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|nodes
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|el
operator|=
operator|(
name|Element
operator|)
name|iter
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|el
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
literal|"2.0.3-SNAPSHOT"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertFalse
argument_list|(
name|found
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|testDir
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|populateReleasedSnapshotsTest
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|List
name|versions
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"2.3-SNAPSHOT"
argument_list|)
expr_stmt|;
name|populateDb
argument_list|(
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-plugin-plugin"
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|populateHigherSnapshotExistsTest
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|versions
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"2.0.3-SNAPSHOT"
argument_list|)
expr_stmt|;
name|populateDb
argument_list|(
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-source-plugin"
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

