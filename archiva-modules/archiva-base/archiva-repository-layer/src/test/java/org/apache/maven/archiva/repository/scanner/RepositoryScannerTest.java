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
name|repository
operator|.
name|scanner
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
name|io
operator|.
name|FileUtils
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
name|consumers
operator|.
name|InvalidRepositoryContentConsumer
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|RepositoryContentStatistics
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
name|repository
operator|.
name|AbstractRepositoryLayerTestCase
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_comment
comment|/**  * RepositoryScannerTest  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryScannerTest
extends|extends
name|AbstractRepositoryLayerTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|ARTIFACT_PATTERNS
init|=
operator|new
name|String
index|[]
block|{
literal|"**/*.jar"
block|,
literal|"**/*.pom"
block|,
literal|"**/*.rar"
block|,
literal|"**/*.zip"
block|,
literal|"**/*.war"
block|,
literal|"**/*.tar.gz"
block|}
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|createDefaultRepository
parameter_list|()
block|{
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repositories/default-repository"
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
return|return
name|createRepository
argument_list|(
literal|"testDefaultRepo"
argument_list|,
literal|"Test Default Repository"
argument_list|,
name|repoDir
argument_list|)
return|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|createSimpleRepository
parameter_list|()
throws|throws
name|IOException
throws|,
name|ParseException
block|{
name|File
name|srcDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repositories/simple-repository"
argument_list|)
decl_stmt|;
name|File
name|repoDir
init|=
name|getTestFile
argument_list|(
literal|"target/test-repos/simple-repository"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoDir
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|srcDir
argument_list|,
name|repoDir
argument_list|)
expr_stmt|;
name|File
name|repoFile
init|=
operator|new
name|File
argument_list|(
name|repoDir
argument_list|,
literal|"groupId/snapshot-artifact/1.0-alpha-1-SNAPSHOT/snapshot-artifact-1.0-alpha-1-20050611.202024-1.pom"
argument_list|)
decl_stmt|;
name|repoFile
operator|.
name|setLastModified
argument_list|(
name|getTimestampAsMillis
argument_list|(
literal|"20050611.202024"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Simple Test Repository should exist."
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
return|return
name|createRepository
argument_list|(
literal|"testSimpleRepo"
argument_list|,
literal|"Test Simple Repository"
argument_list|,
name|repoDir
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|long
name|getTimestampAsMillis
parameter_list|(
name|String
name|timestamp
parameter_list|)
throws|throws
name|ParseException
block|{
name|SimpleDateFormat
name|fmt
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMdd.HHmmss"
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
name|fmt
operator|.
name|setTimeZone
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|fmt
operator|.
name|parse
argument_list|(
name|timestamp
argument_list|)
operator|.
name|getTime
argument_list|()
return|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|createLegacyRepository
parameter_list|()
block|{
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repositories/legacy-repository"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Legacy Test Repository should exist."
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
name|ManagedRepositoryConfiguration
name|repo
init|=
name|createRepository
argument_list|(
literal|"testLegacyRepo"
argument_list|,
literal|"Test Legacy Repository"
argument_list|,
name|repoDir
argument_list|)
decl_stmt|;
name|repo
operator|.
name|setLayout
argument_list|(
literal|"legacy"
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|private
name|void
name|assertMinimumHits
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|minimumHitCount
parameter_list|,
name|long
name|actualCount
parameter_list|)
block|{
if|if
condition|(
name|actualCount
operator|<
name|minimumHitCount
condition|)
block|{
name|fail
argument_list|(
literal|"Minimum hit count on "
operator|+
name|msg
operator|+
literal|" not satisfied.  Expected more than<"
operator|+
name|minimumHitCount
operator|+
literal|">, but actually got<"
operator|+
name|actualCount
operator|+
literal|">."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|RepositoryScanner
name|lookupRepositoryScanner
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|(
name|RepositoryScanner
operator|)
name|lookup
argument_list|(
name|RepositoryScanner
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getIgnoreList
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ignores
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ignores
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|RepositoryScanner
operator|.
name|IGNORABLE_CONTENT
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ignores
return|;
block|}
specifier|public
name|void
name|testTimestampRepositoryScanner
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryConfiguration
name|repository
init|=
name|createSimpleRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|KnownScanConsumer
name|consumer
init|=
operator|new
name|KnownScanConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|setIncludes
argument_list|(
name|ARTIFACT_PATTERNS
argument_list|)
expr_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|InvalidScanConsumer
name|badconsumer
init|=
operator|new
name|InvalidScanConsumer
argument_list|()
decl_stmt|;
name|invalidConsumers
operator|.
name|add
argument_list|(
name|badconsumer
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|lookupRepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|getIgnoreList
argument_list|()
argument_list|,
name|getTimestampAsMillis
argument_list|(
literal|"20061101.000000"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Stats should not be null."
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stats.totalFileCount"
argument_list|,
literal|4
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stats.newFileCount"
argument_list|,
literal|3
argument_list|,
name|stats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Processed Count"
argument_list|,
literal|3
argument_list|,
name|consumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Processed Count (of invalid items)"
argument_list|,
literal|1
argument_list|,
name|badconsumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTimestampRepositoryScannerFreshScan
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryConfiguration
name|repository
init|=
name|createSimpleRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|KnownScanConsumer
name|consumer
init|=
operator|new
name|KnownScanConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|setIncludes
argument_list|(
name|ARTIFACT_PATTERNS
argument_list|)
expr_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|InvalidScanConsumer
name|badconsumer
init|=
operator|new
name|InvalidScanConsumer
argument_list|()
decl_stmt|;
name|invalidConsumers
operator|.
name|add
argument_list|(
name|badconsumer
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|lookupRepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|getIgnoreList
argument_list|()
argument_list|,
name|RepositoryScanner
operator|.
name|FRESH_SCAN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Stats should not be null."
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stats.totalFileCount"
argument_list|,
literal|4
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stats.newFileCount"
argument_list|,
literal|4
argument_list|,
name|stats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Processed Count"
argument_list|,
literal|3
argument_list|,
name|consumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Processed Count (of invalid items)"
argument_list|,
literal|1
argument_list|,
name|badconsumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTimestampRepositoryScannerProcessUnmodified
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryConfiguration
name|repository
init|=
name|createSimpleRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|KnownScanConsumer
name|consumer
init|=
operator|new
name|KnownScanConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|setProcessUnmodified
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setIncludes
argument_list|(
name|ARTIFACT_PATTERNS
argument_list|)
expr_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|InvalidScanConsumer
name|badconsumer
init|=
operator|new
name|InvalidScanConsumer
argument_list|()
decl_stmt|;
name|invalidConsumers
operator|.
name|add
argument_list|(
name|badconsumer
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|lookupRepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|getIgnoreList
argument_list|()
argument_list|,
name|getTimestampAsMillis
argument_list|(
literal|"20061101.000000"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Stats should not be null."
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stats.totalFileCount"
argument_list|,
literal|4
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stats.newFileCount"
argument_list|,
literal|3
argument_list|,
name|stats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Processed Count"
argument_list|,
literal|3
argument_list|,
name|consumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Processed Count (of invalid items)"
argument_list|,
literal|1
argument_list|,
name|badconsumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultRepositoryScanner
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryConfiguration
name|repository
init|=
name|createDefaultRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|KnownScanConsumer
name|consumer
init|=
operator|new
name|KnownScanConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|setIncludes
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"**/*.jar"
block|,
literal|"**/*.war"
block|,
literal|"**/*.pom"
block|,
literal|"**/maven-metadata.xml"
block|,
literal|"**/*-site.xml"
block|,
literal|"**/*.zip"
block|,
literal|"**/*.tar.gz"
block|,
literal|"**/*.sha1"
block|,
literal|"**/*.md5"
block|}
argument_list|)
expr_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|InvalidScanConsumer
name|badconsumer
init|=
operator|new
name|InvalidScanConsumer
argument_list|()
decl_stmt|;
name|invalidConsumers
operator|.
name|add
argument_list|(
name|badconsumer
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|lookupRepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|getIgnoreList
argument_list|()
argument_list|,
name|RepositoryScanner
operator|.
name|FRESH_SCAN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Stats should not be null."
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Stats.totalFileCount"
argument_list|,
literal|17
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Processed Count"
argument_list|,
literal|17
argument_list|,
name|consumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Processed Count (of invalid items)"
argument_list|,
literal|6
argument_list|,
name|badconsumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultRepositoryArtifactScanner
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actualArtifactPaths
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/invalid/1.0-20050611.123456-1/invalid-1.0-20050611.123456-1.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/invalid/1.0-SNAPSHOT/invalid-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/invalid/1.0/invalid-1.0b.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/invalid/1.0/invalid-2.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/invalid-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/test/1.0-SNAPSHOT/wrong-artifactId-1.0-20050611.112233-1.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/test/1.0-SNAPSHOT/test-1.0-20050611.112233-1-javadoc.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/test/1.0-SNAPSHOT/test-1.0-20050611.112233-1.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/A/1.0/A-1.0.war"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/A/1.0/A-1.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/B/2.0/B-2.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/B/1.0/B-1.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/some-ejb/1.0/some-ejb-1.0-client.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/C/1.0/C-1.0.war"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/C/1.0/C-1.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/update/test-not-updated/1.0/test-not-updated-1.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/update/test-not-updated/1.0/test-not-updated-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/update/test-updated/1.0/test-updated-1.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/update/test-updated/1.0/test-updated-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/discovery/1.0/discovery-1.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/testing/1.0/testing-1.0-test-sources.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/testing/1.0/testing-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/testing/1.0/testing-1.0-sources.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/testing/1.0/testing-1.0.zip"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/testing/1.0/testing-1.0.tar.gz"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/samplejar/2.0/samplejar-2.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/samplejar/2.0/samplejar-2.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/samplejar/1.0/samplejar-1.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/samplejar/1.0/samplejar-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org/apache/testgroup/discovery/1.0/discovery-1.0.pom"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"javax/sql/jdbc/2.0/jdbc-2.0.jar"
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|createDefaultRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|KnownScanConsumer
name|consumer
init|=
operator|new
name|KnownScanConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|setIncludes
argument_list|(
name|ARTIFACT_PATTERNS
argument_list|)
expr_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|InvalidScanConsumer
name|badconsumer
init|=
operator|new
name|InvalidScanConsumer
argument_list|()
decl_stmt|;
name|invalidConsumers
operator|.
name|add
argument_list|(
name|badconsumer
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|lookupRepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|getIgnoreList
argument_list|()
argument_list|,
name|RepositoryScanner
operator|.
name|FRESH_SCAN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Stats should not be null."
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Stats.totalFileCount"
argument_list|,
name|actualArtifactPaths
operator|.
name|size
argument_list|()
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Processed Count"
argument_list|,
name|actualArtifactPaths
operator|.
name|size
argument_list|()
argument_list|,
name|consumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultRepositoryMetadataScanner
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actualMetadataPaths
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/some-ejb/1.0/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/update/test-not-updated/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/update/test-updated/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"org/apache/testgroup/discovery/1.0/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"org/apache/testgroup/discovery/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"javax/sql/jdbc/2.0/maven-metadata-repository.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"javax/sql/jdbc/maven-metadata-repository.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"javax/sql/maven-metadata-repository.xml"
argument_list|)
expr_stmt|;
name|actualMetadataPaths
operator|.
name|add
argument_list|(
literal|"javax/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|createDefaultRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|KnownScanConsumer
name|knownConsumer
init|=
operator|new
name|KnownScanConsumer
argument_list|()
decl_stmt|;
name|knownConsumer
operator|.
name|setIncludes
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"**/maven-metadata*.xml"
block|}
argument_list|)
expr_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|knownConsumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|InvalidScanConsumer
name|badconsumer
init|=
operator|new
name|InvalidScanConsumer
argument_list|()
decl_stmt|;
name|invalidConsumers
operator|.
name|add
argument_list|(
name|badconsumer
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|lookupRepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|getIgnoreList
argument_list|()
argument_list|,
name|RepositoryScanner
operator|.
name|FRESH_SCAN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Stats should not be null."
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Stats.totalFileCount"
argument_list|,
name|actualMetadataPaths
operator|.
name|size
argument_list|()
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Processed Count"
argument_list|,
name|actualMetadataPaths
operator|.
name|size
argument_list|()
argument_list|,
name|knownConsumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultRepositoryProjectScanner
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actualProjectPaths
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/A/1.0/A-1.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/B/2.0/B-2.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/B/1.0/B-1.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/C/1.0/C-1.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/update/test-not-updated/1.0/test-not-updated-1.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/update/test-updated/1.0/test-updated-1.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/discovery/1.0/discovery-1.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/samplejar/2.0/samplejar-2.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/maven/samplejar/1.0/samplejar-1.0.pom"
argument_list|)
expr_stmt|;
name|actualProjectPaths
operator|.
name|add
argument_list|(
literal|"org/apache/testgroup/discovery/1.0/discovery-1.0.pom"
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|createDefaultRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|KnownScanConsumer
name|consumer
init|=
operator|new
name|KnownScanConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|setIncludes
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"**/*.pom"
block|}
argument_list|)
expr_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|InvalidScanConsumer
name|badconsumer
init|=
operator|new
name|InvalidScanConsumer
argument_list|()
decl_stmt|;
name|invalidConsumers
operator|.
name|add
argument_list|(
name|badconsumer
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|lookupRepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|getIgnoreList
argument_list|()
argument_list|,
name|RepositoryScanner
operator|.
name|FRESH_SCAN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Stats should not be null."
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Stats.totalFileCount"
argument_list|,
name|actualProjectPaths
operator|.
name|size
argument_list|()
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Processed Count"
argument_list|,
name|actualProjectPaths
operator|.
name|size
argument_list|()
argument_list|,
name|consumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLegacyRepositoryArtifactScanner
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actualArtifactPaths
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/jars/1.0/invalid-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/jars/invalid-1.0.rar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/jars/invalid.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"invalid/invalid-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"javax.sql/jars/jdbc-2.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven/jars/some-ejb-1.0-client.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven/jars/testing-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven/jars/testing-1.0-sources.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven/jars/testing-UNKNOWN.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven/jars/testing-1.0.zip"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven/jars/testing-1.0-20050611.112233-1.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven/jars/testing-1.0.tar.gz"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven.update/jars/test-not-updated-1.0.jar"
argument_list|)
expr_stmt|;
name|actualArtifactPaths
operator|.
name|add
argument_list|(
literal|"org.apache.maven.update/jars/test-updated-1.0.jar"
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|createLegacyRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|KnownScanConsumer
name|consumer
init|=
operator|new
name|KnownScanConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|setIncludes
argument_list|(
name|ARTIFACT_PATTERNS
argument_list|)
expr_stmt|;
name|knownConsumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|InvalidScanConsumer
name|badconsumer
init|=
operator|new
name|InvalidScanConsumer
argument_list|()
decl_stmt|;
name|invalidConsumers
operator|.
name|add
argument_list|(
name|badconsumer
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|lookupRepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repository
argument_list|,
name|knownConsumers
argument_list|,
name|invalidConsumers
argument_list|,
name|getIgnoreList
argument_list|()
argument_list|,
name|RepositoryScanner
operator|.
name|FRESH_SCAN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Stats should not be null."
argument_list|,
name|stats
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Stats.totalFileCount"
argument_list|,
name|actualArtifactPaths
operator|.
name|size
argument_list|()
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertMinimumHits
argument_list|(
literal|"Processed Count"
argument_list|,
name|actualArtifactPaths
operator|.
name|size
argument_list|()
argument_list|,
name|consumer
operator|.
name|getProcessCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

