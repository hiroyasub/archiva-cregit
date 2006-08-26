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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_comment
comment|/**  * An SHA-1 implementation of the streaming digester.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @plexus.component role="org.apache.maven.archiva.digest.StreamingDigester" role-hint="sha1"  */
end_comment

begin_class
specifier|public
class|class
name|StreamingSha1Digester
extends|extends
name|AbstractStreamingDigester
block|{
specifier|public
name|StreamingSha1Digester
parameter_list|()
throws|throws
name|NoSuchAlgorithmException
block|{
name|super
argument_list|(
literal|"SHA-1"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

