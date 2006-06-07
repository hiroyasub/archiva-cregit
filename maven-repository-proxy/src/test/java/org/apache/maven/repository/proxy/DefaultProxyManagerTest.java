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
name|proxy
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
name|repository
operator|.
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|repository
operator|.
name|layout
operator|.
name|DefaultRepositoryLayout
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
name|repository
operator|.
name|proxy
operator|.
name|configuration
operator|.
name|ProxyConfiguration
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
name|repository
operator|.
name|proxy
operator|.
name|repository
operator|.
name|ProxyRepository
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
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|component
operator|.
name|repository
operator|.
name|exception
operator|.
name|ComponentLookupException
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

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|DefaultProxyManagerTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ProxyManager
name|proxy
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
name|proxy
operator|=
operator|(
name|ProxyManager
operator|)
name|container
operator|.
name|lookup
argument_list|(
name|ProxyManager
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setConfiguration
argument_list|(
name|getTestConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExceptions
parameter_list|()
block|{
name|proxy
operator|.
name|setConfiguration
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|proxy
operator|.
name|get
argument_list|(
literal|"/invalid"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected empty configuration error."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Expected Exception not thrown."
argument_list|,
literal|"No proxy configuration defined."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Expected Exception not thrown."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testArtifactDownload
parameter_list|()
throws|throws
name|Exception
block|{
comment|//test download
name|File
name|file
init|=
name|proxy
operator|.
name|get
argument_list|(
literal|"/commons-logging/commons-logging/1.0/commons-logging-1.0.jar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File must be downloaded."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Downloaded file should be present in the cache."
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|startsWith
argument_list|(
name|proxy
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryCachePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|//test cache
name|proxy
operator|.
name|get
argument_list|(
literal|"/commons-logging/commons-logging/1.0/commons-logging-1.0.jar"
argument_list|)
expr_stmt|;
try|try
block|{
name|proxy
operator|.
name|get
argument_list|(
literal|"/commons-logging/commons-logging/2.0/commons-logging-2.0.jar"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ResourceDoesNotExistException exception not thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testArtifactChecksum
parameter_list|()
throws|throws
name|Exception
block|{
comment|//force the downlod from the remote repository, use getRemoteFile()
name|File
name|file
init|=
name|proxy
operator|.
name|getRemoteFile
argument_list|(
literal|"/commons-logging/commons-logging/1.0/commons-logging-1.0.jar.md5"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File must be downloaded."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Downloaded file should be present in the cache."
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|startsWith
argument_list|(
name|proxy
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryCachePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNonArtifactWithNoChecksum
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
name|proxy
operator|.
name|get
argument_list|(
literal|"/not-standard/repository/file.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File must be downloaded."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Downloaded file should be present in the cache."
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|startsWith
argument_list|(
name|proxy
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryCachePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNonArtifactWithMD5Checksum
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
name|proxy
operator|.
name|get
argument_list|(
literal|"/checksumed-md5/repository/file.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File must be downloaded."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Downloaded file should be present in the cache."
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|startsWith
argument_list|(
name|proxy
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryCachePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNonArtifactWithSHA1Checksum
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
name|proxy
operator|.
name|get
argument_list|(
literal|"/checksumed-sha1/repository/file.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File must be downloaded."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Downloaded file should be present in the cache."
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|startsWith
argument_list|(
name|proxy
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryCachePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|container
operator|.
name|release
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|private
name|ProxyConfiguration
name|getTestConfiguration
parameter_list|()
throws|throws
name|ComponentLookupException
block|{
name|ProxyConfiguration
name|config
init|=
operator|(
name|ProxyConfiguration
operator|)
name|container
operator|.
name|lookup
argument_list|(
name|ProxyConfiguration
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|config
operator|.
name|setRepositoryCachePath
argument_list|(
literal|"target/proxy-cache"
argument_list|)
expr_stmt|;
name|ArtifactRepositoryLayout
name|defLayout
init|=
operator|new
name|DefaultRepositoryLayout
argument_list|()
decl_stmt|;
name|File
name|repo1File
init|=
name|getTestFile
argument_list|(
literal|"src/test/remote-repo1"
argument_list|)
decl_stmt|;
name|ProxyRepository
name|repo1
init|=
operator|new
name|ProxyRepository
argument_list|(
literal|"test-repo"
argument_list|,
literal|"file://"
operator|+
name|repo1File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|defLayout
argument_list|)
decl_stmt|;
name|config
operator|.
name|addRepository
argument_list|(
name|repo1
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

