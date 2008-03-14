begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
operator|.
name|util
package|;
end_package

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

begin_comment
comment|/**  * MimeTypesTest   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id: MimeTypesTest.java 6556 2007-06-20 20:44:46Z joakime $  */
end_comment

begin_class
specifier|public
class|class
name|MimeTypesTest
extends|extends
name|PlexusTestCase
block|{
specifier|public
name|void
name|testGetMimeType
parameter_list|()
throws|throws
name|Exception
block|{
name|MimeTypes
name|mime
init|=
operator|(
name|MimeTypes
operator|)
name|lookup
argument_list|(
name|MimeTypes
operator|.
name|class
argument_list|)
decl_stmt|;
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

