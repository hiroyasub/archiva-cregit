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
name|redback
operator|.
name|rest
operator|.
name|services
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
name|api
operator|.
name|RuntimeInfoService
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
name|ApplicationRuntimeInfo
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
name|Before
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
name|util
operator|.
name|Collections
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
name|RuntimeInfoServiceTest
extends|extends
name|AbstractRestServicesTest
block|{
specifier|private
specifier|static
name|Path
name|appServerBase
decl_stmt|;
specifier|private
specifier|static
name|String
name|previousAppServerBase
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setAppServerBase
parameter_list|()
throws|throws
name|IOException
block|{
name|previousAppServerBase
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"appserver.base"
argument_list|)
expr_stmt|;
name|appServerBase
operator|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"archiva-common-web_appsrvrt_"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"appserver.base"
argument_list|,
name|appServerBase
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|resetAppServerBase
parameter_list|()
block|{
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|appServerBase
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|appServerBase
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|setProperty
argument_list|(
literal|"appserver.base"
argument_list|,
name|previousAppServerBase
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
specifier|public
name|void
name|startServer
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|jcrDirectory
init|=
name|appServerBase
operator|.
name|resolve
argument_list|(
literal|"jcr"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|jcrDirectory
argument_list|)
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|jcrDirectory
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|startServer
argument_list|()
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
literal|"classpath*:META-INF/spring-context.xml,classpath:/spring-context-with-jcr.xml"
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
annotation|@
name|Test
specifier|public
name|void
name|runtimeInfoService
parameter_list|()
throws|throws
name|Exception
block|{
name|RuntimeInfoService
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
name|RuntimeInfoService
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
name|ApplicationRuntimeInfo
name|applicationRuntimeInfo
init|=
name|service
operator|.
name|getApplicationRuntimeInfo
argument_list|(
literal|"en"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"expectedVersion"
argument_list|)
argument_list|,
name|applicationRuntimeInfo
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|applicationRuntimeInfo
operator|.
name|isJavascriptLog
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|applicationRuntimeInfo
operator|.
name|isLogMissingI18n
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
