begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
operator|.
name|utils
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|filelock
operator|.
name|DefaultFileLockManager
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
name|storage
operator|.
name|fs
operator|.
name|FilesystemAsset
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
name|storage
operator|.
name|fs
operator|.
name|FilesystemStorage
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
name|storage
operator|.
name|StorageAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|TestSubject
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
name|io
operator|.
name|IOException
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
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
specifier|public
class|class
name|ArtifactBuilderTest
block|{
annotation|@
name|TestSubject
specifier|private
name|ArtifactBuilder
name|builder
init|=
operator|new
name|ArtifactBuilder
argument_list|()
decl_stmt|;
name|StorageAsset
name|getFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|filePath
init|=
name|Paths
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|FilesystemStorage
name|filesystemStorage
init|=
operator|new
name|FilesystemStorage
argument_list|(
name|filePath
operator|.
name|getParent
argument_list|()
argument_list|,
operator|new
name|DefaultFileLockManager
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|FilesystemAsset
argument_list|(
name|filesystemStorage
argument_list|,
name|filePath
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|filePath
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildSnapshot
parameter_list|()
throws|throws
name|IOException
block|{
name|assertThat
argument_list|(
name|builder
operator|.
name|getExtensionFromFile
argument_list|(
name|getFile
argument_list|(
literal|"/tmp/foo-2.3-20141119.064321-40.jar"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildPom
parameter_list|()
throws|throws
name|IOException
block|{
name|assertThat
argument_list|(
name|builder
operator|.
name|getExtensionFromFile
argument_list|(
name|getFile
argument_list|(
literal|"/tmp/foo-1.0.pom"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"pom"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildJar
parameter_list|()
throws|throws
name|IOException
block|{
name|assertThat
argument_list|(
name|builder
operator|.
name|getExtensionFromFile
argument_list|(
name|getFile
argument_list|(
literal|"/tmp/foo-1.0-sources.jar"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildTarGz
parameter_list|()
throws|throws
name|IOException
block|{
name|assertThat
argument_list|(
name|builder
operator|.
name|getExtensionFromFile
argument_list|(
name|getFile
argument_list|(
literal|"/tmp/foo-1.0.tar.gz"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"tar.gz"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildPomZip
parameter_list|()
throws|throws
name|IOException
block|{
name|assertThat
argument_list|(
name|builder
operator|.
name|getExtensionFromFile
argument_list|(
name|getFile
argument_list|(
literal|"/tmp/foo-1.0.pom.zip"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"pom.zip"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildR00
parameter_list|()
throws|throws
name|IOException
block|{
name|assertThat
argument_list|(
name|builder
operator|.
name|getExtensionFromFile
argument_list|(
name|getFile
argument_list|(
literal|"/tmp/foo-1.0.r00"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"r00"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

