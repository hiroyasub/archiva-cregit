begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
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
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|ArtifactMetadata
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
name|versioning
operator|.
name|DefaultArtifactVersion
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactMetadataVersionComparator
implements|implements
name|Comparator
argument_list|<
name|ArtifactMetadata
argument_list|>
block|{
specifier|public
specifier|static
name|ArtifactMetadataVersionComparator
name|INSTANCE
init|=
operator|new
name|ArtifactMetadataVersionComparator
argument_list|()
decl_stmt|;
specifier|public
name|int
name|compare
parameter_list|(
name|ArtifactMetadata
name|o1
parameter_list|,
name|ArtifactMetadata
name|o2
parameter_list|)
block|{
comment|// sort by version (reverse), then ID
name|int
name|result
init|=
operator|new
name|DefaultArtifactVersion
argument_list|(
name|o2
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|compareTo
argument_list|(
operator|new
name|DefaultArtifactVersion
argument_list|(
name|o1
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|result
operator|!=
literal|0
condition|?
name|result
else|:
name|o1
operator|.
name|getId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

