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
operator|.
name|util
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
comment|/**  * MimeTypesTest  *  * @version $Id: MimeTypesTest.java 6556 2007-06-20 20:44:46Z joakime $  */
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
name|MimeTypesTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
name|MimeTypes
name|mime
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetMimeType
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
literal|"MimeTypes should not be null."
argument_list|,
name|mime
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/pdf"
argument_list|,
name|mime
operator|.
name|getMimeType
argument_list|(
literal|"big-book.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/octet-stream"
argument_list|,
name|mime
operator|.
name|getMimeType
argument_list|(
literal|"BookMaker.class"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/vnd.ms-powerpoint"
argument_list|,
name|mime
operator|.
name|getMimeType
argument_list|(
literal|"TypeSetting.ppt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/java-archive"
argument_list|,
name|mime
operator|.
name|getMimeType
argument_list|(
literal|"BookViewer.jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

