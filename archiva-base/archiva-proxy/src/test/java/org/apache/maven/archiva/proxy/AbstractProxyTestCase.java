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
name|proxy
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
name|configuration
operator|.
name|ProxyConnectorConfiguration
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
name|RemoteRepositoryConfiguration
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
name|policies
operator|.
name|CachedFailuresPolicy
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
name|policies
operator|.
name|ChecksumPolicy
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
name|policies
operator|.
name|ReleasesPolicy
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
name|policies
operator|.
name|SnapshotsPolicy
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
name|policies
operator|.
name|urlcache
operator|.
name|UrlFailureCache
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
name|ManagedRepositoryContent
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
name|Wagon
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
name|easymock
operator|.
name|MockControl
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
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_comment
comment|/**  * AbstractProxyTestCase  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractProxyTestCase
extends|extends
name|PlexusTestCase
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|ID_LEGACY_PROXIED
init|=
literal|"legacy-proxied"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ID_PROXIED1
init|=
literal|"proxied1"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ID_PROXIED1_TARGET
init|=
literal|"proxied1-target"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ID_PROXIED2
init|=
literal|"proxied2"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ID_PROXIED2_TARGET
init|=
literal|"proxied2-target"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ID_DEFAULT_MANAGED
init|=
literal|"default-managed-repository"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ID_LEGACY_MANAGED
init|=
literal|"legacy-managed-repository"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPOPATH_PROXIED_LEGACY
init|=
literal|"src/test/repositories/legacy-proxied"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPOPATH_PROXIED1
init|=
literal|"src/test/repositories/proxied1"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPOPATH_PROXIED1_TARGET
init|=
literal|"target/test-repository/proxied1"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPOPATH_PROXIED2
init|=
literal|"src/test/repositories/proxied2"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPOPATH_PROXIED2_TARGET
init|=
literal|"target/test-repository/proxied2"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPOPATH_DEFAULT_MANAGED
init|=
literal|"src/test/repositories/managed"
decl_stmt|;
comment|// protected static final String REPOPATH_DEFAULT_MANAGED_TARGET = "target/test-repository/managed";
specifier|protected
specifier|static
specifier|final
name|String
name|REPOPATH_LEGACY_MANAGED
init|=
literal|"src/test/repositories/legacy-managed"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|REPOPATH_LEGACY_MANAGED_TARGET
init|=
literal|"target/test-repository/legacy-managed"
decl_stmt|;
specifier|protected
name|MockControl
name|wagonMockControl
decl_stmt|;
specifier|protected
name|Wagon
name|wagonMock
decl_stmt|;
specifier|protected
name|RepositoryProxyConnectors
name|proxyHandler
decl_stmt|;
specifier|protected
name|ManagedRepositoryContent
name|managedDefaultRepository
decl_stmt|;
specifier|protected
name|File
name|managedDefaultDir
decl_stmt|;
specifier|protected
name|ManagedRepositoryContent
name|managedLegacyRepository
decl_stmt|;
specifier|protected
name|File
name|managedLegacyDir
decl_stmt|;
specifier|protected
name|MockConfiguration
name|config
decl_stmt|;
specifier|protected
name|void
name|assertChecksums
parameter_list|(
name|File
name|expectedFile
parameter_list|,
name|String
name|expectedSha1Contents
parameter_list|,
name|String
name|expectedMd5Contents
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
name|File
name|md5File
init|=
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedSha1Contents
operator|==
literal|null
condition|)
block|{
name|assertFalse
argument_list|(
literal|"SHA1 File should NOT exist: "
operator|+
name|sha1File
operator|.
name|getPath
argument_list|()
argument_list|,
name|sha1File
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
literal|"SHA1 File should exist: "
operator|+
name|sha1File
operator|.
name|getPath
argument_list|()
argument_list|,
name|sha1File
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|actualSha1Contents
init|=
name|readChecksumFile
argument_list|(
name|sha1File
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SHA1 File contents: "
operator|+
name|sha1File
operator|.
name|getPath
argument_list|()
argument_list|,
name|expectedSha1Contents
argument_list|,
name|actualSha1Contents
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expectedMd5Contents
operator|==
literal|null
condition|)
block|{
name|assertFalse
argument_list|(
literal|"MD5 File should NOT exist: "
operator|+
name|md5File
operator|.
name|getPath
argument_list|()
argument_list|,
name|md5File
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
literal|"MD5 File should exist: "
operator|+
name|md5File
operator|.
name|getPath
argument_list|()
argument_list|,
name|md5File
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|actualMd5Contents
init|=
name|readChecksumFile
argument_list|(
name|md5File
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MD5 File contents: "
operator|+
name|md5File
operator|.
name|getPath
argument_list|()
argument_list|,
name|expectedMd5Contents
argument_list|,
name|actualMd5Contents
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|assertFileEquals
parameter_list|(
name|File
name|expectedFile
parameter_list|,
name|File
name|actualFile
parameter_list|,
name|File
name|sourceFile
parameter_list|)
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
literal|"Expected File should not be null."
argument_list|,
name|expectedFile
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Actual File should not be null."
argument_list|,
name|actualFile
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file exists."
argument_list|,
name|actualFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check file path matches."
argument_list|,
name|expectedFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|actualFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|expectedContents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|sourceFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|actualContents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|actualFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check file contents."
argument_list|,
name|expectedContents
argument_list|,
name|actualContents
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertNotDownloaded
parameter_list|(
name|File
name|downloadedFile
parameter_list|)
block|{
name|assertNull
argument_list|(
literal|"Found file: "
operator|+
name|downloadedFile
operator|+
literal|"; but was expecting a failure"
argument_list|,
name|downloadedFile
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertNoTempFiles
parameter_list|(
name|File
name|expectedFile
parameter_list|)
block|{
name|File
name|workingDir
init|=
name|expectedFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|workingDir
operator|==
literal|null
operator|)
operator|||
operator|!
name|workingDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return;
block|}
name|Collection
argument_list|<
name|File
argument_list|>
name|tmpFiles
init|=
name|FileUtils
operator|.
name|listFiles
argument_list|(
name|workingDir
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"tmp"
block|}
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tmpFiles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|StringBuffer
name|emsg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|"Found Temp Files in dir: "
argument_list|)
operator|.
name|append
argument_list|(
name|workingDir
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|tfile
range|:
name|tmpFiles
control|)
block|{
name|emsg
operator|.
name|append
argument_list|(
literal|"\n   "
argument_list|)
operator|.
name|append
argument_list|(
name|tfile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fail
argument_list|(
name|emsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A faster recursive copy that omits .svn directories.      *      * @param sourceDirectory the source directory to copy      * @param destDirectory   the target location      * @throws java.io.IOException if there is a copying problem      * @todo get back into plexus-utils, share with converter module      */
specifier|protected
name|void
name|copyDirectoryStructure
parameter_list|(
name|File
name|sourceDirectory
parameter_list|,
name|File
name|destDirectory
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|sourceDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Source directory doesn't exists ("
operator|+
name|sourceDirectory
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|")."
argument_list|)
throw|;
block|}
name|File
index|[]
name|files
init|=
name|sourceDirectory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|String
name|sourcePath
init|=
name|sourceDirectory
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|file
init|=
name|files
index|[
name|i
index|]
decl_stmt|;
name|String
name|dest
init|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|dest
operator|=
name|dest
operator|.
name|substring
argument_list|(
name|sourcePath
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|File
name|destination
init|=
operator|new
name|File
argument_list|(
name|destDirectory
argument_list|,
name|dest
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|destination
operator|=
name|destination
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|file
argument_list|,
operator|new
name|File
argument_list|(
name|destination
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// TODO: Change when there is a FileUtils.copyFileToDirectory(file, destination, boolean) option
comment|//FileUtils.copyFileToDirectory( file, destination );
block|}
if|else if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
literal|".svn"
operator|.
name|equals
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|destination
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|destination
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not create destination directory '"
operator|+
name|destination
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"'."
argument_list|)
throw|;
block|}
name|copyDirectoryStructure
argument_list|(
name|file
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown file type: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
name|ManagedRepositoryContent
name|createManagedLegacyRepository
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createRepository
argument_list|(
literal|"testManagedLegacyRepo"
argument_list|,
literal|"Test Managed (Legacy) Repository"
argument_list|,
literal|"src/test/repositories/legacy-managed"
argument_list|,
literal|"legacy"
argument_list|)
return|;
block|}
specifier|protected
name|ManagedRepositoryContent
name|createProxiedLegacyRepository
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createRepository
argument_list|(
literal|"testProxiedLegacyRepo"
argument_list|,
literal|"Test Proxied (Legacy) Repository"
argument_list|,
literal|"src/test/repositories/legacy-proxied"
argument_list|,
literal|"legacy"
argument_list|)
return|;
block|}
specifier|protected
name|ManagedRepositoryContent
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|layout
parameter_list|)
throws|throws
name|Exception
block|{
name|ManagedRepositoryConfiguration
name|repo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|ManagedRepositoryContent
name|repoContent
init|=
operator|(
name|ManagedRepositoryContent
operator|)
name|lookup
argument_list|(
name|ManagedRepositoryContent
operator|.
name|class
argument_list|,
name|layout
argument_list|)
decl_stmt|;
name|repoContent
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
return|return
name|repoContent
return|;
block|}
specifier|protected
name|UrlFailureCache
name|lookupUrlFailureCache
parameter_list|()
throws|throws
name|Exception
block|{
name|UrlFailureCache
name|failurlCache
init|=
operator|(
name|UrlFailureCache
operator|)
name|lookup
argument_list|(
name|UrlFailureCache
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
literal|"URL Failure Cache cannot be null."
argument_list|,
name|failurlCache
argument_list|)
expr_stmt|;
return|return
name|failurlCache
return|;
block|}
comment|/**      * Read the first line from the checksum file, and return it (trimmed).      */
specifier|protected
name|String
name|readChecksumFile
parameter_list|(
name|File
name|checksumFile
parameter_list|)
throws|throws
name|Exception
block|{
name|FileReader
name|freader
init|=
literal|null
decl_stmt|;
name|BufferedReader
name|buf
init|=
literal|null
decl_stmt|;
try|try
block|{
name|freader
operator|=
operator|new
name|FileReader
argument_list|(
name|checksumFile
argument_list|)
expr_stmt|;
name|buf
operator|=
operator|new
name|BufferedReader
argument_list|(
name|freader
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|readLine
argument_list|()
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|buf
operator|!=
literal|null
condition|)
block|{
name|buf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|freader
operator|!=
literal|null
condition|)
block|{
name|freader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|saveConnector
parameter_list|(
name|String
name|sourceRepoId
parameter_list|,
name|String
name|targetRepoId
parameter_list|)
block|{
name|saveConnector
argument_list|(
name|sourceRepoId
argument_list|,
name|targetRepoId
argument_list|,
name|ChecksumPolicy
operator|.
name|IGNORED
argument_list|,
name|ReleasesPolicy
operator|.
name|IGNORED
argument_list|,
name|SnapshotsPolicy
operator|.
name|IGNORED
argument_list|,
name|CachedFailuresPolicy
operator|.
name|IGNORED
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|saveConnector
parameter_list|(
name|String
name|sourceRepoId
parameter_list|,
name|String
name|targetRepoId
parameter_list|,
name|String
name|checksumPolicy
parameter_list|,
name|String
name|releasePolicy
parameter_list|,
name|String
name|snapshotPolicy
parameter_list|,
name|String
name|cacheFailuresPolicy
parameter_list|)
block|{
name|ProxyConnectorConfiguration
name|connectorConfig
init|=
operator|new
name|ProxyConnectorConfiguration
argument_list|()
decl_stmt|;
name|connectorConfig
operator|.
name|setSourceRepoId
argument_list|(
name|sourceRepoId
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|setTargetRepoId
argument_list|(
name|targetRepoId
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_CHECKSUM
argument_list|,
name|checksumPolicy
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_RELEASES
argument_list|,
name|releasePolicy
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_SNAPSHOTS
argument_list|,
name|snapshotPolicy
argument_list|)
expr_stmt|;
name|connectorConfig
operator|.
name|addPolicy
argument_list|(
name|ProxyConnectorConfiguration
operator|.
name|POLICY_CACHE_FAILURES
argument_list|,
name|cacheFailuresPolicy
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addProxyConnector
argument_list|(
name|connectorConfig
argument_list|)
expr_stmt|;
comment|// Proper Triggering ...
name|String
name|prefix
init|=
literal|"proxyConnectors.proxyConnector("
operator|+
name|count
operator|+
literal|")"
decl_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".sourceRepoId"
argument_list|,
name|connectorConfig
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".targetRepoId"
argument_list|,
name|connectorConfig
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".proxyId"
argument_list|,
name|connectorConfig
operator|.
name|getProxyId
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".policies.releases"
argument_list|,
name|connectorConfig
operator|.
name|getPolicy
argument_list|(
literal|"releases"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".policies.checksum"
argument_list|,
name|connectorConfig
operator|.
name|getPolicy
argument_list|(
literal|"checksum"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".policies.snapshots"
argument_list|,
name|connectorConfig
operator|.
name|getPolicy
argument_list|(
literal|"snapshots"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".policies.cache-failures"
argument_list|,
name|connectorConfig
operator|.
name|getPolicy
argument_list|(
literal|"cache-failures"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|saveManagedRepositoryConfig
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|layout
parameter_list|)
block|{
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
name|id
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLocation
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
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
name|String
name|prefix
init|=
literal|"managedRepositories.managedRepository("
operator|+
name|count
operator|+
literal|")"
decl_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".id"
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".name"
argument_list|,
name|repoConfig
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".location"
argument_list|,
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".layout"
argument_list|,
name|repoConfig
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|saveRemoteRepositoryConfig
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|layout
parameter_list|)
block|{
name|RemoteRepositoryConfiguration
name|repoConfig
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repoConfig
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addRemoteRepository
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
literal|"remoteRepositories.remoteRepository("
operator|+
name|count
operator|+
literal|")"
decl_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".id"
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".name"
argument_list|,
name|repoConfig
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".url"
argument_list|,
name|repoConfig
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|triggerChange
argument_list|(
name|prefix
operator|+
literal|".layout"
argument_list|,
name|repoConfig
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|File
name|saveTargetedRepositoryConfig
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|originalPath
parameter_list|,
name|String
name|targetPath
parameter_list|,
name|String
name|layout
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|repoLocation
init|=
name|getTestFile
argument_list|(
name|targetPath
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|copyDirectoryStructure
argument_list|(
name|getTestFile
argument_list|(
name|originalPath
argument_list|)
argument_list|,
name|repoLocation
argument_list|)
expr_stmt|;
name|saveRemoteRepositoryConfig
argument_list|(
name|id
argument_list|,
literal|"Target Repo-"
operator|+
name|id
argument_list|,
name|targetPath
argument_list|,
name|layout
argument_list|)
expr_stmt|;
return|return
name|repoLocation
return|;
block|}
annotation|@
name|Override
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
name|config
operator|=
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
expr_stmt|;
comment|// Setup source repository (using default layout)
name|String
name|repoPath
init|=
literal|"target/test-repository/managed/"
operator|+
name|getName
argument_list|()
decl_stmt|;
name|File
name|repoLocation
init|=
name|getTestFile
argument_list|(
name|repoPath
argument_list|)
decl_stmt|;
name|managedDefaultRepository
operator|=
name|createRepository
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"Default Managed Repository"
argument_list|,
name|repoPath
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|managedDefaultDir
operator|=
operator|new
name|File
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepoRoot
argument_list|()
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfig
init|=
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
decl_stmt|;
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
comment|// Setup source repository (using legacy layout)
name|repoLocation
operator|=
name|getTestFile
argument_list|(
name|REPOPATH_LEGACY_MANAGED_TARGET
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|copyDirectoryStructure
argument_list|(
name|getTestFile
argument_list|(
name|REPOPATH_LEGACY_MANAGED
argument_list|)
argument_list|,
name|repoLocation
argument_list|)
expr_stmt|;
name|managedLegacyRepository
operator|=
name|createRepository
argument_list|(
name|ID_LEGACY_MANAGED
argument_list|,
literal|"Legacy Managed Repository"
argument_list|,
name|REPOPATH_LEGACY_MANAGED_TARGET
argument_list|,
literal|"legacy"
argument_list|)
expr_stmt|;
name|managedLegacyDir
operator|=
operator|new
name|File
argument_list|(
name|managedLegacyRepository
operator|.
name|getRepoRoot
argument_list|()
argument_list|)
expr_stmt|;
name|repoConfig
operator|=
name|managedLegacyRepository
operator|.
name|getRepository
argument_list|()
expr_stmt|;
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
comment|// Setup target (proxied to) repository.
name|saveRemoteRepositoryConfig
argument_list|(
name|ID_PROXIED1
argument_list|,
literal|"Proxied Repository 1"
argument_list|,
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED1
argument_list|)
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
comment|// Setup target (proxied to) repository.
name|saveRemoteRepositoryConfig
argument_list|(
name|ID_PROXIED2
argument_list|,
literal|"Proxied Repository 2"
argument_list|,
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED2
argument_list|)
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
comment|// Setup target (proxied to) repository using legacy layout.
name|saveRemoteRepositoryConfig
argument_list|(
name|ID_LEGACY_PROXIED
argument_list|,
literal|"Proxied Legacy Repository"
argument_list|,
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED_LEGACY
argument_list|)
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|,
literal|"legacy"
argument_list|)
expr_stmt|;
comment|// Setup the proxy handler.
name|proxyHandler
operator|=
operator|(
name|RepositoryProxyConnectors
operator|)
name|lookup
argument_list|(
name|RepositoryProxyConnectors
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Setup the wagon mock.
name|wagonMockControl
operator|=
name|MockControl
operator|.
name|createNiceControl
argument_list|(
name|Wagon
operator|.
name|class
argument_list|)
expr_stmt|;
name|wagonMock
operator|=
operator|(
name|Wagon
operator|)
name|wagonMockControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|WagonDelegate
name|delegate
init|=
operator|(
name|WagonDelegate
operator|)
name|lookup
argument_list|(
name|Wagon
operator|.
name|ROLE
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|delegate
operator|.
name|setDelegate
argument_list|(
name|wagonMock
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n.\\ "
operator|+
name|getName
argument_list|()
operator|+
literal|"() \\._________________________________________\n"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Copy the specified resource directory from the src/test/repository/managed/ to      * the testable directory under target/test-repository/managed/${testName}/      *      * @param resourceDir      * @throws IOException      */
specifier|protected
name|void
name|setupTestableManagedRepository
parameter_list|(
name|String
name|resourcePath
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|resourceDir
init|=
name|resourcePath
decl_stmt|;
if|if
condition|(
operator|!
name|resourcePath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|int
name|idx
init|=
name|resourcePath
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|resourceDir
operator|=
name|resourcePath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
name|File
name|sourceRepoDir
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_DEFAULT_MANAGED
argument_list|)
decl_stmt|;
name|File
name|sourceDir
init|=
operator|new
name|File
argument_list|(
name|sourceRepoDir
argument_list|,
name|resourceDir
argument_list|)
decl_stmt|;
name|File
name|destRepoDir
init|=
name|managedDefaultDir
decl_stmt|;
name|File
name|destDir
init|=
operator|new
name|File
argument_list|(
name|destRepoDir
argument_list|,
name|resourceDir
argument_list|)
decl_stmt|;
comment|// Cleanout destination dirs.
if|if
condition|(
name|destDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
block|}
comment|// Test the source dir.
if|if
condition|(
operator|!
name|sourceDir
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// This is just a warning.
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"[WARN] Skipping setup of testable managed repository, source dir does not exist: "
operator|+
name|sourceDir
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Test that the source is a dir.
if|if
condition|(
operator|!
name|sourceDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Unable to setup testable managed repository, source is not a directory: "
operator|+
name|sourceDir
argument_list|)
expr_stmt|;
block|}
comment|// Make the destination dir.
name|destDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
comment|// Copy directory structure.
name|copyDirectoryStructure
argument_list|(
name|sourceDir
argument_list|,
name|destDir
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|Date
name|getFutureDate
parameter_list|()
throws|throws
name|ParseException
block|{
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
name|cal
operator|.
name|getTime
argument_list|()
return|;
block|}
specifier|protected
specifier|static
name|Date
name|getPastDate
parameter_list|()
throws|throws
name|ParseException
block|{
return|return
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd"
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|parse
argument_list|(
literal|"2000-01-01"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

