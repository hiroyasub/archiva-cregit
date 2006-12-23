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
name|indexer
operator|.
name|record
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
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
name|artifact
operator|.
name|resolver
operator|.
name|filter
operator|.
name|ArtifactFilter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Filter that removes artifacts already in the index.  * TODO: we could do timestamp comparisons here  */
end_comment

begin_class
specifier|public
class|class
name|IndexRecordExistsArtifactFilter
implements|implements
name|ArtifactFilter
block|{
specifier|private
specifier|final
name|Collection
name|keys
decl_stmt|;
specifier|public
name|IndexRecordExistsArtifactFilter
parameter_list|(
name|Collection
name|keys
parameter_list|)
block|{
name|this
operator|.
name|keys
operator|=
name|keys
expr_stmt|;
block|}
specifier|public
name|boolean
name|include
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|String
name|artifactKey
init|=
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|":"
operator|+
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|":"
operator|+
name|artifact
operator|.
name|getVersion
argument_list|()
operator|+
operator|(
name|artifact
operator|.
name|getClassifier
argument_list|()
operator|!=
literal|null
condition|?
literal|":"
operator|+
name|artifact
operator|.
name|getClassifier
argument_list|()
else|:
literal|""
operator|)
decl_stmt|;
return|return
operator|!
name|keys
operator|.
name|contains
argument_list|(
name|artifactKey
argument_list|)
return|;
block|}
block|}
end_class

end_unit

