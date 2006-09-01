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
name|digest
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
specifier|public
class|class
name|DigestUtilsTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testCleanChecksum
parameter_list|()
throws|throws
name|DigesterException
block|{
comment|// SHA1 checksum from www.ibiblio.org/maven2, incuding file path
name|DigestUtils
operator|.
name|cleanChecksum
argument_list|(
literal|"bcc82975c0f9c681fcb01cc38504c992553e93ba  /home/projects/maven/repository-staging/to-ibiblio/maven2/servletapi/servletapi/2.4/servletapi-2.4.pom"
argument_list|,
literal|"SHA1"
argument_list|,
literal|"servletapi/servletapi/2.4/servletapi-2.4.pom"
argument_list|)
expr_stmt|;
name|DigestUtils
operator|.
name|cleanChecksum
argument_list|(
literal|"SHA1(/home/projects/maven/repository-staging/to-ibiblio/maven2/servletapi/servletapi/2.4/servletapi-2.4.pom)=bcc82975c0f9c681fcb01cc38504c992553e93ba"
argument_list|,
literal|"SHA1"
argument_list|,
literal|"servletapi/servletapi/2.4/servletapi-2.4.pom"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

