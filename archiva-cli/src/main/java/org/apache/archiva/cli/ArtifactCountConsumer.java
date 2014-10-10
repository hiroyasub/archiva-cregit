begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|cli
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
name|archiva
operator|.
name|consumers
operator|.
name|KnownRepositoryContentConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * ArtifactCountConsumer  *  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#count-artifacts"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|ArtifactCountConsumer
extends|extends
name|AbstractProgressConsumer
implements|implements
name|KnownRepositoryContentConsumer
block|{
comment|/**      * default-value="count-artifacts"      */
specifier|private
name|String
name|id
init|=
literal|"count-artifacts"
decl_stmt|;
comment|/**      * default-value="Count Artifacts"      */
specifier|private
name|String
name|description
init|=
literal|"Count Artifacts"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
decl_stmt|;
specifier|public
name|ArtifactCountConsumer
parameter_list|()
block|{
comment|// TODO: shouldn't this use filetypes?
name|includes
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.pom"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.jar"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.war"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.ear"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.sar"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.car"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.mar"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.dtd"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.tld"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.gz"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.bz2"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.zip"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
block|}
end_class

end_unit

