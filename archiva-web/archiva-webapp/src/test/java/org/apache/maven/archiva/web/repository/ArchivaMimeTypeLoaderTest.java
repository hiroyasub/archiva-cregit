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
operator|.
name|MimeTypes
import|;
end_import

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
comment|/**  * ArchivaMimeTypesTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaMimeTypeLoaderTest
extends|extends
name|PlexusTestCase
block|{
specifier|public
name|void
name|testArchivaTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|lookup
argument_list|(
name|ArchivaMimeTypeLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|MimeTypes
name|mimeTypes
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
name|mimeTypes
argument_list|)
expr_stmt|;
comment|// Test for some added types.
name|assertEquals
argument_list|(
literal|"sha1"
argument_list|,
literal|"text/plain"
argument_list|,
name|mimeTypes
operator|.
name|getMimeType
argument_list|(
literal|"foo.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"md5"
argument_list|,
literal|"text/plain"
argument_list|,
name|mimeTypes
operator|.
name|getMimeType
argument_list|(
literal|"foo.md5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pgp"
argument_list|,
literal|"application/pgp-encrypted"
argument_list|,
name|mimeTypes
operator|.
name|getMimeType
argument_list|(
literal|"foo.pgp"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
literal|"application/java-archive"
argument_list|,
name|mimeTypes
operator|.
name|getMimeType
argument_list|(
literal|"foo.jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

