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
name|webdav
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|GetMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|HttpUnitOptions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
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
name|ProxyConnectorConfiguration
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
name|policies
operator|.
name|SnapshotsPolicy
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
name|io
operator|.
name|File
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

begin_comment
comment|/**  * RepositoryServlet Tests, Proxied, Get of Snapshot Artifacts, with varying policy settings.  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletProxiedSnapshotPolicyTest
extends|extends
name|AbstractRepositoryServletProxiedTestCase
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|.
name|getBean
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setProxyConnectors
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|.
name|getBean
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setProxyConnectors
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyAlwaysManagedNewer
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|NEWER
operator|*
name|OVER_ONE_DAY
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyAlwaysManagedOlder
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|OLDER
operator|*
name|OVER_ONE_DAY
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyAlwaysNoManagedContent
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|NO_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyDailyFail
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|NEWER
operator|*
name|ONE_MINUTE
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyDailyNoManagedContent
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|NO_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyDailyPass
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|OLDER
operator|*
name|OVER_ONE_DAY
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyRejectFail
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|HAS_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyRejectNoManagedContentFail
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_NOT_FOUND
argument_list|,
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|NO_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyRejectPass
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|HAS_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyHourlyFail
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|NEWER
operator|*
name|ONE_MINUTE
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyHourlyNoManagedContent
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|NO_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyHourlyPass
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|HAS_MANAGED_COPY
argument_list|,
operator|(
name|OLDER
operator|*
name|OVER_ONE_HOUR
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyOnceFail
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_MANAGED_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|HAS_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyOnceNoManagedContent
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|NO_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProxiedSnapshotsArtifactPolicyOncePass
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|EXPECT_REMOTE_CONTENTS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|NO_MANAGED_COPY
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertGetProxiedSnapshotsArtifactWithPolicy
parameter_list|(
name|int
name|expectation
parameter_list|,
name|String
name|snapshotsPolicy
parameter_list|,
name|boolean
name|hasManagedCopy
parameter_list|)
throws|throws
name|Exception
block|{
name|assertGetProxiedSnapshotsArtifactWithPolicy
argument_list|(
name|expectation
argument_list|,
name|snapshotsPolicy
argument_list|,
name|hasManagedCopy
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertGetProxiedSnapshotsArtifactWithPolicy
parameter_list|(
name|int
name|expectation
parameter_list|,
name|String
name|snapshotsPolicy
parameter_list|,
name|boolean
name|hasManagedCopy
parameter_list|,
name|long
name|deltaManagedToRemoteTimestamp
parameter_list|)
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|resourcePath
init|=
literal|"org/apache/archiva/test/2.0-SNAPSHOT/test-2.0-SNAPSHOT.jar"
decl_stmt|;
name|String
name|expectedRemoteContents
init|=
literal|"archiva-test-2.0-SNAPSHOT|jar-remote-contents"
decl_stmt|;
name|String
name|expectedManagedContents
init|=
literal|null
decl_stmt|;
name|File
name|remoteFile
init|=
name|populateRepo
argument_list|(
name|remoteSnapshots
argument_list|,
name|resourcePath
argument_list|,
name|expectedRemoteContents
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasManagedCopy
condition|)
block|{
name|expectedManagedContents
operator|=
literal|"archiva-test-2.0-SNAPSHOT|jar-managed-contents"
expr_stmt|;
name|File
name|managedFile
init|=
name|populateRepo
argument_list|(
name|repoRootInternal
argument_list|,
name|resourcePath
argument_list|,
name|expectedManagedContents
argument_list|)
decl_stmt|;
name|managedFile
operator|.
name|setLastModified
argument_list|(
name|remoteFile
operator|.
name|lastModified
argument_list|()
operator|+
name|deltaManagedToRemoteTimestamp
argument_list|)
expr_stmt|;
block|}
name|setupSnapshotConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteSnapshots
argument_list|,
name|snapshotsPolicy
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
comment|// --- Execution
comment|// process the response code later, not via an exception.
name|HttpUnitOptions
operator|.
name|setExceptionsThrownOnErrorStatus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|resourcePath
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
comment|// --- Verification
switch|switch
condition|(
name|expectation
condition|)
block|{
case|case
name|EXPECT_MANAGED_CONTENTS
case|:
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Invalid Test Case: Can't expect managed contents with "
operator|+
literal|"test that doesn't have a managed copy in the first place."
argument_list|,
name|hasManagedCopy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected managed file contents"
argument_list|,
name|expectedManagedContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXPECT_REMOTE_CONTENTS
case|:
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected remote file contents"
argument_list|,
name|expectedRemoteContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXPECT_NOT_FOUND
case|:
name|assertResponseNotFound
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertManagedFileNotExists
argument_list|(
name|repoRootInternal
argument_list|,
name|resourcePath
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

