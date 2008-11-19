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
name|policies
operator|.
name|ReleasesPolicy
import|;
end_import

begin_comment
comment|/**  * RepositoryServlet Tests, Proxied, Get of Release Artifacts, with varying policy settings.  *  * @version $Id: RepositoryServletProxiedReleasePolicyTest.java 590908 2007-11-01 06:21:26Z joakime $  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletProxiedRelocatedTest
extends|extends
name|AbstractRepositoryServletProxiedTestCase
block|{
specifier|public
name|void
name|testGetProxiedReleaseArtifactPolicyOncePass
parameter_list|()
throws|throws
name|Exception
block|{
comment|// --- Setup
name|setupCentralRemoteRepo
argument_list|()
expr_stmt|;
name|setupCleanInternalRepo
argument_list|()
expr_stmt|;
name|String
name|resourcePath
init|=
literal|"org/apache/archiva/test/1.0/test-1.0.jar"
decl_stmt|;
name|String
name|expectedRemoteContents
init|=
literal|"archiva-test-1.0|jar-remote-contents"
decl_stmt|;
name|populateRepo
argument_list|(
name|remoteCentral
argument_list|,
name|resourcePath
argument_list|,
name|expectedRemoteContents
argument_list|)
expr_stmt|;
name|resourcePath
operator|=
literal|"archiva/test/1.0/test-1.0.pom"
expr_stmt|;
name|String
name|pom
init|=
literal|"<project>"
operator|+
literal|"<modelVersion>4.0.0</modelVersion>"
operator|+
literal|"<groupId>archiva</groupId>"
operator|+
literal|"<artifactId>test</artifactId>"
operator|+
literal|"<version>1.0</version>"
operator|+
literal|"<distributionManagement>"
operator|+
literal|"<relocation>"
operator|+
literal|"<groupId>org.apache.archiva</groupId>"
operator|+
literal|"</relocation>"
operator|+
literal|"</distributionManagement>"
operator|+
literal|"</project>"
decl_stmt|;
name|populateRepo
argument_list|(
name|remoteCentral
argument_list|,
name|resourcePath
argument_list|,
name|pom
argument_list|)
expr_stmt|;
name|resourcePath
operator|=
literal|"archiva/jars/test-1.0.jar"
expr_stmt|;
name|setupReleaseConnector
argument_list|(
name|REPOID_INTERNAL
argument_list|,
name|remoteCentral
argument_list|,
name|ReleasesPolicy
operator|.
name|ONCE
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
block|}
block|}
end_class

end_unit

