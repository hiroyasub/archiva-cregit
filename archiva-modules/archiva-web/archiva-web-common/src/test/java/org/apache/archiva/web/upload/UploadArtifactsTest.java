begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|upload
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJaxbJsonProvider
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
name|configuration
operator|.
name|provider
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
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
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
name|ArchivaBlockJUnit4ClassRunner
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
name|web
operator|.
name|AbstractRestServicesTest
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
name|web
operator|.
name|api
operator|.
name|FileUploadService
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
name|web
operator|.
name|model
operator|.
name|FileMetadata
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
name|lang3
operator|.
name|StringUtils
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
name|lang3
operator|.
name|SystemUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|Attachment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|AttachmentBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|ContentDisposition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|MultipartBody
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ClientErrorException
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
name|net
operator|.
name|URLEncoder
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|UploadArtifactsTest
extends|extends
name|AbstractRestServicesTest
block|{
specifier|private
specifier|static
name|String
name|PREVIOUS_ARCHIVA_PATH
decl_stmt|;
specifier|private
name|AtomicReference
argument_list|<
name|Path
argument_list|>
name|projectDir
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|( )
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|initConfigurationPath
parameter_list|()
throws|throws
name|Exception
block|{
name|PREVIOUS_ARCHIVA_PATH
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|ArchivaConfiguration
operator|.
name|USER_CONFIG_PROPERTY
argument_list|)
expr_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"test.resources.path"
argument_list|)
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|ArchivaConfiguration
operator|.
name|USER_CONFIG_PROPERTY
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"test.resources.path"
argument_list|)
operator|+
literal|"/archiva.xml"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Path
name|path
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/archiva.xml"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|ArchivaConfiguration
operator|.
name|USER_CONFIG_PROPERTY
argument_list|,
name|path
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"USER_CONFIG_DIR "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
name|ArchivaConfiguration
operator|.
name|USER_CONFIG_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|restoreConfigurationPath
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|ArchivaConfiguration
operator|.
name|USER_CONFIG_PROPERTY
argument_list|,
name|PREVIOUS_ARCHIVA_PATH
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getSpringConfigLocation
parameter_list|()
block|{
return|return
literal|"classpath*:META-INF/spring-context.xml,classpath:/spring-context-test-upload.xml"
return|;
block|}
specifier|protected
name|Path
name|getProjectDirectory
parameter_list|()
block|{
if|if
condition|(
name|projectDir
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|propVal
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"mvn.project.base.dir"
argument_list|)
decl_stmt|;
name|Path
name|newVal
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|propVal
argument_list|)
condition|)
block|{
name|newVal
operator|=
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|newVal
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|propVal
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
expr_stmt|;
block|}
name|projectDir
operator|.
name|compareAndSet
argument_list|(
literal|null
argument_list|,
name|newVal
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"project dir: "
operator|+
name|projectDir
operator|.
name|get
argument_list|( )
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|projectDir
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getRestServicesPath
parameter_list|()
block|{
return|return
literal|"restServices"
return|;
block|}
specifier|protected
name|String
name|getBaseUrl
parameter_list|()
block|{
name|String
name|baseUrlSysProps
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"archiva.baseRestUrl"
argument_list|)
decl_stmt|;
return|return
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|baseUrlSysProps
argument_list|)
condition|?
literal|"http://localhost:"
operator|+
name|getServerPort
argument_list|()
else|:
name|baseUrlSysProps
return|;
block|}
specifier|private
name|FileUploadService
name|getUploadService
parameter_list|()
block|{
name|FileUploadService
name|service
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|getBaseUrl
argument_list|()
operator|+
literal|"/"
operator|+
name|getRestServicesPath
argument_list|()
operator|+
literal|"/archivaUiServices/"
argument_list|,
name|FileUploadService
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|JacksonJaxbJsonProvider
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Service class {}"
argument_list|,
name|service
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Referer"
argument_list|,
literal|"http://localhost:"
operator|+
name|getServerPort
argument_list|()
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|service
argument_list|)
operator|.
name|header
argument_list|(
literal|"Referer"
argument_list|,
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|EXCEPTION_MESSAGE_CAUSE_ENABLED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|FAULT_STACKTRACE_ENABLED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROPOGATE_EXCEPTION
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|service
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.transport.no_io_exceptions"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// WebClient.client( service ).
return|return
name|service
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|clearUploadedFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
name|service
operator|.
name|clearUploadedFiles
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|uploadFile
parameter_list|()
throws|throws
name|IOException
throws|,
name|ArchivaRestServiceException
block|{
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
try|try
block|{
name|Path
name|file
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/snapshot-repo/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-20130425.081822-1.jar"
argument_list|)
decl_stmt|;
specifier|final
name|Attachment
name|fileAttachment
init|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\""
operator|+
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
decl_stmt|;
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|clearUploadedFiles
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|failUploadFileWithBadFileName
parameter_list|()
throws|throws
name|IOException
throws|,
name|ArchivaRestServiceException
block|{
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
try|try
block|{
name|Path
name|file
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/snapshot-repo/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-20130425.081822-1.jar"
argument_list|)
decl_stmt|;
specifier|final
name|Attachment
name|fileAttachment
init|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\"/../TestFile.testext\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
decl_stmt|;
try|try
block|{
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"FileNames with path contents should not be allowed."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClientErrorException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|422
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|service
operator|.
name|clearUploadedFiles
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|uploadAndDeleteFile
parameter_list|()
throws|throws
name|IOException
throws|,
name|ArchivaRestServiceException
block|{
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
try|try
block|{
name|Path
name|file
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/snapshot-repo/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-20130425.081822-1.jar"
argument_list|)
decl_stmt|;
specifier|final
name|Attachment
name|fileAttachment
init|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\""
operator|+
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
decl_stmt|;
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|service
operator|.
name|deleteFile
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|clearUploadedFiles
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|failUploadAndDeleteWrongFile
parameter_list|()
throws|throws
name|IOException
throws|,
name|ArchivaRestServiceException
block|{
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
try|try
block|{
name|Path
name|file
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/snapshot-repo/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-20130425.081822-1.jar"
argument_list|)
decl_stmt|;
specifier|final
name|Attachment
name|fileAttachment
init|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\""
operator|+
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
decl_stmt|;
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|service
operator|.
name|deleteFile
argument_list|(
literal|"file123"
operator|+
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|clearUploadedFiles
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|failUploadAndDeleteFileInOtherDir
parameter_list|()
throws|throws
name|IOException
throws|,
name|ArchivaRestServiceException
block|{
name|Path
name|testFile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
name|Path
name|file
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/snapshot-repo/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-20130425.081822-1.jar"
argument_list|)
decl_stmt|;
name|Path
name|targetDir
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"target/testDelete"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|targetDir
argument_list|)
condition|)
name|Files
operator|.
name|createDirectories
argument_list|(
name|targetDir
argument_list|)
expr_stmt|;
name|Path
name|tempDir
init|=
name|SystemUtils
operator|.
name|getJavaIoTmpDir
argument_list|()
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|testFile
operator|=
name|Files
operator|.
name|createTempFile
argument_list|(
name|targetDir
argument_list|,
literal|"TestFile"
argument_list|,
literal|".txt"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Test file {}"
argument_list|,
name|testFile
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Tmp dir {}"
argument_list|,
name|tempDir
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|testFile
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relativePath
init|=
name|tempDir
operator|.
name|relativize
argument_list|(
name|testFile
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Attachment
name|fileAttachment
init|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\""
operator|+
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
decl_stmt|;
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|String
name|relativePathEncoded
init|=
name|URLEncoder
operator|.
name|encode
argument_list|(
literal|"../target/"
operator|+
name|relativePath
operator|.
name|toString
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Trying to delete with path traversal: {}, {}"
argument_list|,
name|relativePath
argument_list|,
name|relativePathEncoded
argument_list|)
expr_stmt|;
try|try
block|{
name|service
operator|.
name|deleteFile
argument_list|(
name|relativePathEncoded
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaRestServiceException
name|ex
parameter_list|)
block|{
comment|// Expected exception
block|}
name|assertTrue
argument_list|(
literal|"File in another directory may not be deleted"
argument_list|,
name|Files
operator|.
name|exists
argument_list|(
name|testFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|testFile
operator|!=
literal|null
condition|)
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|testFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|failSaveFileWithBadParams
parameter_list|()
throws|throws
name|IOException
throws|,
name|ArchivaRestServiceException
block|{
name|Path
name|path
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"target/appserver-base/repositories/internal/org/apache/archiva/archiva-model/1.2/archiva-model-1.2.jar"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
name|Path
name|file
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/snapshot-repo/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-20130425.081822-1.jar"
argument_list|)
decl_stmt|;
name|Attachment
name|fileAttachment
init|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\"archiva-model.jar\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
decl_stmt|;
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|save
argument_list|(
literal|"internal"
argument_list|,
literal|"org.apache.archiva"
argument_list|,
literal|"archiva-model"
argument_list|,
literal|"1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|fileAttachment
operator|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\"TestFile.FileExt\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|body
operator|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
expr_stmt|;
name|FileMetadata
name|meta
init|=
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Metadata {}"
argument_list|,
name|meta
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|service
operator|.
name|save
argument_list|(
literal|"internal"
argument_list|,
literal|"org"
argument_list|,
name|URLEncoder
operator|.
name|encode
argument_list|(
literal|"../../../test"
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|,
name|URLEncoder
operator|.
name|encode
argument_list|(
literal|"testSave"
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|,
literal|"4"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Error expected, if the content contains bad characters."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClientErrorException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|422
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"target/test-testSave.4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|saveFile
parameter_list|()
throws|throws
name|IOException
throws|,
name|ArchivaRestServiceException
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Starting saveFile()"
argument_list|)
expr_stmt|;
name|Path
name|path
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"target/appserver-base/repositories/internal/org/apache/archiva/archiva-model/1.2/archiva-model-1.2.jar"
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Jar exists: {}"
argument_list|,
name|Files
operator|.
name|exists
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|path
operator|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"target/appserver-base/repositories/internal/org/apache/archiva/archiva-model/1.2/archiva-model-1.2.pom"
argument_list|)
expr_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
name|service
operator|.
name|clearUploadedFiles
argument_list|()
expr_stmt|;
name|Path
name|file
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/snapshot-repo/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-20130425.081822-1.jar"
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Upload file exists: {}"
argument_list|,
name|Files
operator|.
name|exists
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Attachment
name|fileAttachment
init|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\"archiva-model.jar\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
decl_stmt|;
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|service
operator|.
name|save
argument_list|(
literal|"internal"
argument_list|,
literal|"org.apache.archiva"
argument_list|,
literal|"archiva-model"
argument_list|,
literal|"1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|saveFileWithOtherExtension
parameter_list|()
throws|throws
name|IOException
throws|,
name|ArchivaRestServiceException
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Starting saveFileWithOtherExtension()"
argument_list|)
expr_stmt|;
name|Path
name|path
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"target/appserver-base/repositories/internal/org/apache/archiva/archiva-model/1.2/archiva-model-1.2.bin"
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Jar exists: {}"
argument_list|,
name|Files
operator|.
name|exists
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Path
name|pomPath
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"target/appserver-base/repositories/internal/org/apache/archiva/archiva-model/1.2/archiva-model-1.2.pom"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|pomPath
argument_list|)
expr_stmt|;
name|FileUploadService
name|service
init|=
name|getUploadService
argument_list|()
decl_stmt|;
name|service
operator|.
name|clearUploadedFiles
argument_list|()
expr_stmt|;
name|Path
name|file
init|=
name|getProjectDirectory
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"src/test/repositories/snapshot-repo/org/apache/archiva/archiva-model/1.4-M4-SNAPSHOT/archiva-model-1.4-M4-20130425.081822-1.jar"
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Upload file exists: {}"
argument_list|,
name|Files
operator|.
name|exists
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Attachment
name|fileAttachment
init|=
operator|new
name|AttachmentBuilder
argument_list|()
operator|.
name|object
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
operator|.
name|contentDisposition
argument_list|(
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; filename=\"archiva-model.bin\"; name=\"files[]\""
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|fileAttachment
argument_list|)
decl_stmt|;
name|service
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|save
argument_list|(
literal|"internal"
argument_list|,
literal|"org.apache.archiva"
argument_list|,
literal|"archiva-model"
argument_list|,
literal|"1.2"
argument_list|,
literal|"bin"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

