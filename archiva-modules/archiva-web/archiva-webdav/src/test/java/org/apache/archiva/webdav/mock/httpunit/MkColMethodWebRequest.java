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
name|mock
operator|.
name|httpunit
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/**  * MkColMethodWebRequest  * See RFC-2518 Section 8.3  */
end_comment

begin_class
specifier|public
class|class
name|MkColMethodWebRequest
extends|extends
name|WebRequest
block|{
specifier|public
name|MkColMethodWebRequest
parameter_list|(
name|String
name|urlString
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
operator|new
name|URL
argument_list|(
name|urlString
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

