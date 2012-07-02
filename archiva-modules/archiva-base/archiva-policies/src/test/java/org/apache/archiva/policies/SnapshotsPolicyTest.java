begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|policies
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
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
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|Properties
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
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
import|;
end_import

begin_comment
comment|/**  * SnapshotsPolicyTest   *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath*:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|SnapshotsPolicyTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PATH_VERSION_METADATA
init|=
literal|"org/apache/archiva/archiva-testable/1.0-SNAPSHOT/maven-metadata.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PROJECT_METADATA
init|=
literal|"org/apache/archiva/archiva-testable/maven-metadata.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATH_SNAPSHOT_ARTIFACT
init|=
literal|"org/apache/archiva/archiva-testable/1.0-SNAPSHOT/archiva-testable-1.0-SNAPSHOT.jar"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATH_RELEASE_ARTIFACT
init|=
literal|"org/apache/archiva/archiva-testable/2.0/archiva-testable-2.0.jar"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|WITH_LOCAL
init|=
literal|true
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|NO_LOCAL
init|=
literal|false
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|ONE_SECOND
init|=
operator|(
literal|1000
comment|/* milliseconds */
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|ONE_MINUTE
init|=
operator|(
name|ONE_SECOND
operator|*
literal|60
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|ONE_HOUR
init|=
operator|(
name|ONE_MINUTE
operator|*
literal|60
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|ONE_DAY
init|=
operator|(
name|ONE_HOUR
operator|*
literal|24
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|OVER_ONE_HOUR
init|=
operator|(
name|ONE_HOUR
operator|+
name|ONE_MINUTE
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|OVER_ONE_DAY
init|=
operator|(
name|ONE_DAY
operator|+
name|ONE_HOUR
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|OLDER
init|=
operator|(
operator|-
literal|1
operator|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|long
name|NEWER
init|=
literal|0
decl_stmt|;
specifier|private
name|long
name|generatedLocalFileUpdateDelta
init|=
literal|0
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"preDownloadPolicy#snapshots"
argument_list|)
name|PreDownloadPolicy
name|policy
decl_stmt|;
specifier|private
name|PreDownloadPolicy
name|lookupPolicy
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|policy
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyDailyProjectMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
name|OVER_ONE_DAY
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
operator|(
name|ONE_HOUR
operator|*
literal|22
operator|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyDailyReleaseArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
name|OVER_ONE_DAY
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
operator|(
name|ONE_HOUR
operator|*
literal|22
operator|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyDailySnapshotArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicyViolation
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
name|OVER_ONE_DAY
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
operator|(
name|ONE_HOUR
operator|*
literal|22
operator|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicyViolation
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyDailyVersionedMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
name|OVER_ONE_DAY
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
operator|(
name|ONE_HOUR
operator|*
literal|22
operator|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyRejectProjectMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyRejectReleaseArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyRejectSnapshotArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicyViolation
argument_list|(
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicyViolation
argument_list|(
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyRejectVersionedMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|NEVER
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyHourlyProjectMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
name|OVER_ONE_HOUR
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
operator|(
name|ONE_MINUTE
operator|*
literal|45
operator|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyHourlyReleaseArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
name|OVER_ONE_HOUR
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
operator|(
name|ONE_MINUTE
operator|*
literal|45
operator|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyHourlySnapshotArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicyViolation
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
name|OVER_ONE_HOUR
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
operator|(
name|ONE_MINUTE
operator|*
literal|45
operator|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicyViolation
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyHourlyVersionedMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
name|OVER_ONE_HOUR
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
name|generatedLocalFileUpdateDelta
operator|=
operator|(
name|ONE_MINUTE
operator|*
literal|45
operator|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|HOURLY
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyAlwaysProjectMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyAlwaysReleaseArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyAlwaysSnapshotArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyAlwaysVersionedMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyOnceProjectMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|PATH_PROJECT_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyOnceReleaseArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|PATH_RELEASE_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyOnceSnapshotArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicyViolation
argument_list|(
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|PATH_SNAPSHOT_ARTIFACT
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotPolicyOnceVersionedMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Pass the policy when working with metadata, no matter what.
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|NO_LOCAL
argument_list|)
expr_stmt|;
name|assertSnapshotPolicy
argument_list|(
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|PATH_VERSION_METADATA
argument_list|,
name|WITH_LOCAL
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertSnapshotPolicy
parameter_list|(
name|String
name|setting
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|createLocalFile
parameter_list|)
throws|throws
name|Exception
block|{
name|PreDownloadPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|Properties
name|request
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|request
operator|.
name|setProperty
argument_list|(
literal|"filetype"
argument_list|,
name|path
operator|.
name|endsWith
argument_list|(
literal|"/maven-metadata.xml"
argument_list|)
condition|?
literal|"metadata"
else|:
literal|"artifact"
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
literal|"1.0-SNAPSHOT"
argument_list|)
condition|)
block|{
name|request
operator|.
name|setProperty
argument_list|(
literal|"version"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
literal|"2.0"
argument_list|)
condition|)
block|{
name|request
operator|.
name|setProperty
argument_list|(
literal|"version"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
block|}
name|File
name|targetDir
init|=
name|ChecksumPolicyTest
operator|.
name|getTestFile
argument_list|(
literal|"target/test-policy/"
argument_list|)
decl_stmt|;
name|File
name|localFile
init|=
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|localFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|localFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|createLocalFile
condition|)
block|{
name|localFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|localFile
argument_list|,
literal|"random-junk"
argument_list|)
expr_stmt|;
name|localFile
operator|.
name|setLastModified
argument_list|(
name|localFile
operator|.
name|lastModified
argument_list|()
operator|-
name|generatedLocalFileUpdateDelta
argument_list|)
expr_stmt|;
block|}
name|policy
operator|.
name|applyPolicy
argument_list|(
name|setting
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertSnapshotPolicyViolation
parameter_list|(
name|String
name|setting
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|createLocalFile
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|assertSnapshotPolicy
argument_list|(
name|setting
argument_list|,
name|path
argument_list|,
name|createLocalFile
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected a PolicyViolationException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyViolationException
name|e
parameter_list|)
block|{
comment|// expected path.
block|}
block|}
annotation|@
name|Override
annotation|@
name|Before
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
comment|// reset delta to 0.
name|generatedLocalFileUpdateDelta
operator|=
literal|0
expr_stmt|;
block|}
block|}
end_class

end_unit

