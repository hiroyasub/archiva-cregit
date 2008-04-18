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
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavSession
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:james@atlassian.com">James William Dumay</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaDavSession
implements|implements
name|DavSession
block|{
specifier|public
name|void
name|addReference
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"No yet implemented."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|removeReference
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"No yet implemented."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|addLockToken
parameter_list|(
name|String
name|s
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"No yet implemented."
argument_list|)
throw|;
block|}
specifier|public
name|String
index|[]
name|getLockTokens
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"No yet implemented."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|removeLockToken
parameter_list|(
name|String
name|s
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"No yet implemented."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

