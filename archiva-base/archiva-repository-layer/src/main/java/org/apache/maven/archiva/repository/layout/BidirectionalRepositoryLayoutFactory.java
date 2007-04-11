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
name|repository
operator|.
name|layout
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * BidirectionalRepositoryLayoutFactory   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.repository.layout.BidirectionalRepositoryLayoutFactory"  */
end_comment

begin_class
specifier|public
class|class
name|BidirectionalRepositoryLayoutFactory
block|{
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.repository.layout.BidirectionalRepositoryLayout"      */
specifier|private
name|Map
name|layouts
decl_stmt|;
specifier|public
name|BidirectionalRepositoryLayout
name|getLayout
parameter_list|(
name|String
name|type
parameter_list|)
throws|throws
name|LayoutException
block|{
if|if
condition|(
operator|!
name|layouts
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Layout type ["
operator|+
name|type
operator|+
literal|"] does not exist.  "
operator|+
literal|"Available types ["
operator|+
name|layouts
operator|.
name|keySet
argument_list|()
operator|+
literal|"]"
argument_list|)
throw|;
block|}
return|return
operator|(
name|BidirectionalRepositoryLayout
operator|)
name|layouts
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

