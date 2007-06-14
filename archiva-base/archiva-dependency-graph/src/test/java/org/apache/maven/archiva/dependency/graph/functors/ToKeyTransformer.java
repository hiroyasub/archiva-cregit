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
name|DependencyGraphEdge
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
name|DependencyGraphKeys
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
name|Keys
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
name|model
operator|.
name|ArchivaProjectModel
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
name|model
operator|.
name|ArtifactReference
import|;
end_import

begin_comment
comment|/**  * ToKeyTransformer   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ToKeyTransformer
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
name|ArchivaProjectModel
condition|)
block|{
return|return
name|Keys
operator|.
name|toKey
argument_list|(
operator|(
name|ArchivaProjectModel
operator|)
name|input
argument_list|)
return|;
block|}
if|if
condition|(
name|input
operator|instanceof
name|DependencyGraphNode
condition|)
block|{
return|return
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
operator|(
operator|(
name|DependencyGraphNode
operator|)
name|input
operator|)
operator|.
name|getArtifact
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|input
operator|instanceof
name|DependencyGraphEdge
condition|)
block|{
name|DependencyGraphEdge
name|edge
init|=
operator|(
name|DependencyGraphEdge
operator|)
name|input
decl_stmt|;
comment|// Potentially Confusing, but this is called "To"KeyTransformer after all.
return|return
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|edge
operator|.
name|getNodeTo
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|input
operator|instanceof
name|ArtifactReference
condition|)
block|{
return|return
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
operator|(
operator|(
name|ArtifactReference
operator|)
name|input
operator|)
argument_list|)
return|;
block|}
return|return
name|input
return|;
block|}
block|}
end_class

end_unit

