begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_comment
comment|/**  * RepositoryServletTest  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletNoProxyMetadataTest
extends|extends
name|AbstractRepositoryServletTestCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetVersionMetadataDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangMetadata
init|=
literal|"commons-lang/commons-lang/2.1/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadataContents
init|=
literal|"metadata-for-commons-lang-version-2.1"
decl_stmt|;
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|checksumFile
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
name|checksumFile
argument_list|,
name|expectedMetadataContents
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
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
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
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
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedMetadataContents
argument_list|,
name|response
operator|.
name|getContentAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProjectMetadataDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangMetadata
init|=
literal|"commons-lang/commons-lang/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadataContents
init|=
literal|"metadata-for-commons-lang-version-for-project"
decl_stmt|;
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|checksumFile
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
name|checksumFile
argument_list|,
name|expectedMetadataContents
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
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
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
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
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedMetadataContents
argument_list|,
name|response
operator|.
name|getContentAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetGroupMetadataDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangMetadata
init|=
literal|"commons-lang/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadataContents
init|=
literal|"metadata-for-commons-lang-group"
decl_stmt|;
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|checksumFile
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
name|checksumFile
argument_list|,
name|expectedMetadataContents
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
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
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
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
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedMetadataContents
argument_list|,
name|response
operator|.
name|getContentAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSnapshotVersionMetadataDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|assemblyPluginMetadata
init|=
literal|"org/apache/maven/plugins/maven-assembly-plugin/2.2-beta-2-SNAPSHOT/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadataContents
init|=
literal|"metadata-for-assembly-plugin-version-2.2-beta-2-SNAPSHOT"
decl_stmt|;
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|assemblyPluginMetadata
argument_list|)
decl_stmt|;
name|checksumFile
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
name|checksumFile
argument_list|,
name|expectedMetadataContents
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
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
name|assemblyPluginMetadata
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
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
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedMetadataContents
argument_list|,
name|response
operator|.
name|getContentAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

