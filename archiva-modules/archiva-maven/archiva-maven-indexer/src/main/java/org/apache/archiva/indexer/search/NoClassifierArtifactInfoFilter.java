begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|search
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
name|model
operator|.
name|ArchivaArtifactModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|NoClassifierArtifactInfoFilter
implements|implements
name|ArtifactInfoFilter
block|{
specifier|public
specifier|static
specifier|final
name|NoClassifierArtifactInfoFilter
name|INSTANCE
init|=
operator|new
name|NoClassifierArtifactInfoFilter
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|?
extends|extends
name|ArtifactInfoFilter
argument_list|>
name|LIST
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|INSTANCE
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|addArtifactInResult
parameter_list|(
name|ArchivaArtifactModel
name|artifact
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|SearchResultHit
argument_list|>
name|currentResult
parameter_list|)
block|{
return|return
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

