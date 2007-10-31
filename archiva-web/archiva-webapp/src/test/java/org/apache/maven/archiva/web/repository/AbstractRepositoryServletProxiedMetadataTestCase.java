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
name|web
operator|.
name|repository
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
name|commons
operator|.
name|lang
operator|.
name|ArrayUtils
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
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|DetailedDiff
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|Diff
import|;
end_import

begin_comment
comment|/**  * Abstract TestCase for RepositoryServlet Tests, Proxied, Get of Metadata.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryServletProxiedMetadataTestCase
extends|extends
name|AbstractRepositoryServletProxiedTestCase
block|{
specifier|protected
name|RemoteRepoInfo
name|remotePrivateSnapshots
decl_stmt|;
specifier|protected
name|void
name|assertExpectedMetadata
parameter_list|(
name|String
name|expectedMetadata
parameter_list|,
name|String
name|actualMetadata
parameter_list|)
throws|throws
name|Exception
block|{
name|DetailedDiff
name|detailedDiff
init|=
operator|new
name|DetailedDiff
argument_list|(
operator|new
name|Diff
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|detailedDiff
operator|.
name|similar
argument_list|()
condition|)
block|{
comment|// If it isn't similar, dump the difference.
name|assertEquals
argument_list|(
name|expectedMetadata
argument_list|,
name|actualMetadata
argument_list|)
expr_stmt|;
block|}
comment|// XMLAssert.assertXMLEqual( "Expected Metadata:", expectedMetadata, actualMetadata );
block|}
specifier|protected
name|String
name|requestMetadataOK
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
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
name|path
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
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return
name|response
operator|.
name|getText
argument_list|()
return|;
block|}
specifier|protected
name|String
name|createVersionMetadata
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
name|createVersionMetadata
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|String
name|createVersionMetadata
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|timestamp
parameter_list|,
name|String
name|buildNumber
parameter_list|,
name|String
name|lastUpdated
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<metadata>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<groupId>"
argument_list|)
operator|.
name|append
argument_list|(
name|groupId
argument_list|)
operator|.
name|append
argument_list|(
literal|"</groupId>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<artifactId>"
argument_list|)
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|"</artifactId>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<version>"
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
literal|"</version>\n"
argument_list|)
expr_stmt|;
name|boolean
name|hasSnapshot
init|=
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|timestamp
argument_list|)
operator|||
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|buildNumber
argument_list|)
decl_stmt|;
name|boolean
name|hasLastUpdated
init|=
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|lastUpdated
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasSnapshot
operator|||
name|hasLastUpdated
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<versioning>\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasSnapshot
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<snapshot>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<buildNumber>"
argument_list|)
operator|.
name|append
argument_list|(
name|buildNumber
argument_list|)
operator|.
name|append
argument_list|(
literal|"</buildNumber>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<timestamp>"
argument_list|)
operator|.
name|append
argument_list|(
name|timestamp
argument_list|)
operator|.
name|append
argument_list|(
literal|"</timestamp>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"</snapshot>\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasLastUpdated
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<lastUpdated>"
argument_list|)
operator|.
name|append
argument_list|(
name|lastUpdated
argument_list|)
operator|.
name|append
argument_list|(
literal|"</lastUpdated>\n"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"</versioning>\n"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"</metadata>"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|createProjectMetadata
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|latest
parameter_list|,
name|String
name|release
parameter_list|,
name|String
index|[]
name|versions
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<metadata>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<groupId>"
argument_list|)
operator|.
name|append
argument_list|(
name|groupId
argument_list|)
operator|.
name|append
argument_list|(
literal|"</groupId>\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<artifactId>"
argument_list|)
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|"</artifactId>\n"
argument_list|)
expr_stmt|;
name|boolean
name|hasLatest
init|=
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|latest
argument_list|)
decl_stmt|;
name|boolean
name|hasRelease
init|=
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|release
argument_list|)
decl_stmt|;
name|boolean
name|hasVersions
init|=
operator|!
name|ArrayUtils
operator|.
name|isEmpty
argument_list|(
name|versions
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasLatest
operator|||
name|hasRelease
operator|||
name|hasVersions
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<versioning>\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasLatest
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<latest>"
argument_list|)
operator|.
name|append
argument_list|(
name|latest
argument_list|)
operator|.
name|append
argument_list|(
literal|"</latest>\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasRelease
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<release>"
argument_list|)
operator|.
name|append
argument_list|(
name|release
argument_list|)
operator|.
name|append
argument_list|(
literal|"</release>\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasVersions
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<versions>\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|availVersion
range|:
name|versions
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<version>"
argument_list|)
operator|.
name|append
argument_list|(
name|availVersion
argument_list|)
operator|.
name|append
argument_list|(
literal|"</version>\n"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"</versions>\n"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"</versioning>\n"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"</metadata>"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|void
name|setupPrivateSnapshotsRemoteRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|remotePrivateSnapshots
operator|=
name|createServer
argument_list|(
literal|"private-snapshots"
argument_list|)
expr_stmt|;
name|assertServerSetupCorrectly
argument_list|(
name|remotePrivateSnapshots
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addRemoteRepository
argument_list|(
name|remotePrivateSnapshots
operator|.
name|config
argument_list|)
expr_stmt|;
name|setupCleanRepo
argument_list|(
name|remotePrivateSnapshots
operator|.
name|root
argument_list|)
expr_stmt|;
block|}
comment|//    private void assertGetProxiedSnapshotMetadata( int expectation, boolean hasManagedCopy,
comment|//                                                   long deltaManagedToRemoteTimestamp )
comment|//        throws Exception
comment|//    {
comment|//        // --- Setup
comment|//        setupSnapshotsRemoteRepo();
comment|//        setupCleanInternalRepo();
comment|//
comment|//        String resourcePath = "org/apache/archiva/archivatest-maven-plugin/4.0-alpha-1-SNAPSHOT/maven-metadata.xml";
comment|//        String expectedRemoteContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<metadata>\n"
comment|//            + "<groupId>org.apache.maven.plugins</groupId>\n" + "<artifactId>maven-assembly-plugin</artifactId>\n"
comment|//            + "<version>2.2-beta-2-SNAPSHOT</version>\n" + "<versioning>\n" + "<snapshot>\n"
comment|//            + "<timestamp>20071017.162810</timestamp>\n" + "<buildNumber>20</buildNumber>\n"
comment|//            + "</snapshot>\n" + "<lastUpdated>20071017162814</lastUpdated>\n" + "</versioning>\n"
comment|//            + "</metadata>";
comment|//        String expectedManagedContents = null;
comment|//        File remoteFile = populateRepo( remoteSnapshots, resourcePath, expectedRemoteContents );
comment|//
comment|//        if ( hasManagedCopy )
comment|//        {
comment|//            expectedManagedContents = "<metadata>\n" + "<groupId>org.apache.maven.plugins</groupId>\n"
comment|//                + "<artifactId>maven-assembly-plugin</artifactId>\n" + "<version>2.2-beta-2-SNAPSHOT</version>\n"
comment|//                + "</metadata>";
comment|//
comment|//            File managedFile = populateRepo( repoRootInternal, resourcePath, expectedManagedContents );
comment|//            managedFile.setLastModified( remoteFile.lastModified() + deltaManagedToRemoteTimestamp );
comment|//        }
comment|//
comment|//        setupConnector( REPOID_INTERNAL, remoteSnapshots );
comment|//        saveConfiguration();
comment|//
comment|//        // --- Execution
comment|//        // process the response code later, not via an exception.
comment|//        HttpUnitOptions.setExceptionsThrownOnErrorStatus( false );
comment|//
comment|//        WebRequest request = new GetMethodWebRequest( "http://machine.com/repository/internal/" + resourcePath );
comment|//        WebResponse response = sc.getResponse( request );
comment|//
comment|//        // --- Verification
comment|//
comment|//        switch ( expectation )
comment|//        {
comment|//            case EXPECT_MANAGED_CONTENTS:
comment|//                assertResponseOK( response );
comment|//                assertTrue( "Invalid Test Case: Can't expect managed contents with "
comment|//                    + "test that doesn't have a managed copy in the first place.", hasManagedCopy );
comment|//                String actualContents = response.getText();
comment|//                XMLAssert.assertXMLEqual( expectedManagedContents, actualContents );
comment|//                // assertEquals( "Expected managed file contents", expectedManagedContents, response.getText() );
comment|//                break;
comment|//            case EXPECT_REMOTE_CONTENTS:
comment|//                assertResponseOK( response );
comment|//                assertEquals( "Expected remote file contents", expectedRemoteContents, response.getText() );
comment|//                break;
comment|//            case EXPECT_NOT_FOUND:
comment|//                assertResponseNotFound( response );
comment|//                assertManagedFileNotExists( repoRootInternal, resourcePath );
comment|//                break;
comment|//        }
comment|//    }
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|shutdownServer
argument_list|(
name|remotePrivateSnapshots
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

