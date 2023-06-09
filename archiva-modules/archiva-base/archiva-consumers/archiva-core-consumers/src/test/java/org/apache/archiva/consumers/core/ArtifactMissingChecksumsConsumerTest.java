begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|core
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|checksum
operator|.
name|ChecksumAlgorithm
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
name|checksum
operator|.
name|ChecksummedFile
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
name|common
operator|.
name|utils
operator|.
name|PathUtil
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
name|archiva
operator|.
name|repository
operator|.
name|base
operator|.
name|managed
operator|.
name|BasicManagedRepository
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
name|EditableManagedRepository
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
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
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
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Calendar
import|;
end_import

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactMissingChecksumsConsumerTest
extends|extends
name|AbstractArtifactConsumerTest
block|{
specifier|private
name|EditableManagedRepository
name|repoConfig
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
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
name|Path
name|basePath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-classes"
argument_list|)
decl_stmt|;
name|repoConfig
operator|=
name|BasicManagedRepository
operator|.
name|newFilesystemInstance
argument_list|(
literal|"test-repo"
argument_list|,
literal|"Test Repository"
argument_list|,
name|basePath
operator|.
name|resolve
argument_list|(
literal|"test-repo"
argument_list|)
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLocation
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|"test-repo/"
argument_list|)
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"knownRepositoryContentConsumer#create-missing-checksums"
argument_list|,
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoExistingChecksums
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"no-checksums-artifact/1.0/no-checksums-artifact-1.0.jar"
decl_stmt|;
name|Path
name|basePath
init|=
name|PathUtil
operator|.
name|getPathFromUri
argument_list|(
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|Path
name|sha1Path
init|=
name|basePath
operator|.
name|resolve
argument_list|(
name|path
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
name|Path
name|md5FilePath
init|=
name|basePath
operator|.
name|resolve
argument_list|(
name|path
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|sha1Path
argument_list|)
expr_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|md5FilePath
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|sha1Path
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|doesNotExist
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|md5FilePath
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|doesNotExist
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|(
name|repoConfig
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|sha1Path
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|long
name|sha1LastModified
init|=
name|sha1Path
operator|.
name|toFile
argument_list|()
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|md5FilePath
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|long
name|md5LastModified
init|=
name|md5FilePath
operator|.
name|toFile
argument_list|()
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|sha1Path
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|md5FilePath
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|sha1Path
operator|.
name|toFile
argument_list|()
operator|.
name|lastModified
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|sha1LastModified
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|md5FilePath
operator|.
name|toFile
argument_list|()
operator|.
name|lastModified
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|md5LastModified
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExistingIncorrectChecksums
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|newLocation
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-repo"
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|newLocation
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|,
name|newLocation
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLocation
argument_list|(
name|newLocation
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|Path
name|basePath
init|=
name|PathUtil
operator|.
name|getPathFromUri
argument_list|(
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|path
init|=
literal|"incorrect-checksums/1.0/incorrect-checksums-1.0.jar"
decl_stmt|;
name|Path
name|sha1Path
init|=
name|basePath
operator|.
name|resolve
argument_list|(
name|path
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
name|Path
name|md5Path
init|=
name|basePath
operator|.
name|resolve
argument_list|(
name|path
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksum
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|sha1Path
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|md5Path
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|checksum
operator|.
name|isValidChecksums
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|,
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
argument_list|)
argument_list|)
comment|//
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|(
name|repoConfig
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|sha1Path
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|md5Path
operator|.
name|toFile
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|checksum
operator|.
name|isValidChecksums
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|,
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
argument_list|)
argument_list|)
comment|//
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

