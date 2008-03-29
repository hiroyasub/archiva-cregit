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
name|dependency
operator|.
name|graph
operator|.
name|functors
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
name|commons
operator|.
name|collections
operator|.
name|Transformer
import|;
end_import

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
name|dependency
operator|.
name|graph
operator|.
name|DependencyGraphNode
import|;
end_import

begin_comment
comment|/**  * Transform some common dependency graph objects into their  * ArtifactReference form.  */
end_comment

begin_class
specifier|public
class|class
name|ToArtifactReferenceTransformer
implements|implements
name|Transformer
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|instanceof
name|DependencyGraphNode
condition|)
block|{
return|return
operator|(
operator|(
name|DependencyGraphNode
operator|)
name|input
operator|)
operator|.
name|getArtifact
argument_list|()
return|;
block|}
comment|// TODO: Add more objects to transform here.
return|return
name|input
return|;
block|}
block|}
end_class

end_unit

