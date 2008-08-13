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

begin_comment
comment|/**  * RepositoryServlet Tests, Proxied, Get of Metadata, exists on local managed repository only.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletProxiedMetadataLocalOnlyTest
extends|extends
name|AbstractRepositoryServletProxiedMetadataTestCase
block|{
specifier|public
name|void
name|testGetProxiedSnapshotVersionMetadataLocalOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/archivatest-maven-plugin/4.0-alpha-1-SNAPSHOT/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createVersionMetadata
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"archivatest-maven-plugin"
argument_list|,
literal|"4.0-alpha-1-SNAPSHOT"
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|repoRootInternal
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteSnapshots
argument_list|)
expr_stmt|;
name|setupConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remotePrivateSnapshots
argument_list|)
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetProxiedVersionMetadataLocalOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/archivatest-maven-plugin/4.0-alpha-2/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createVersionMetadata
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"archivatest-maven-plugin"
argument_list|,
literal|"4.0-alpha-2"
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|repoRootInternal
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetProxiedProjectMetadataLocalOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/archivatest-maven-plugin/maven-metadata.xml"
decl_stmt|;
name|String
name|version
init|=
literal|"1.0-alpha-4"
decl_stmt|;
name|String
name|release
init|=
literal|"1.0-alpha-4"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createProjectMetadata
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"archivatest-maven-plugin"
argument_list|,
name|version
argument_list|,
name|release
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0-alpha-4"
block|}
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|repoRootInternal
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetProxiedGroupMetadataLocalOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupPrivateSnapshotsRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadata
init|=
name|createGroupMetadata
argument_list|(
literal|"org.apache.archiva"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archivatest-maven-plugin"
block|}
argument_list|)
decl_stmt|;
name|populateRepo
argument_list|(
name|repoRootInternal
argument_list|,
name|path
argument_list|,
name|expectedMetadata
argument_list|)
expr_stmt|;
comment|// --- Execution
name|String
name|actualMetadata
init|=
name|requestMetadataOK
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// --- Verification
name|assertExpectedMetadata
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

