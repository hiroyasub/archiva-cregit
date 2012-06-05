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
name|MetadataFacet
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
name|lang
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
name|HashMap
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

begin_class
specifier|public
class|class
name|MavenArtifactFacet
implements|implements
name|MetadataFacet
block|{
specifier|private
name|String
name|classifier
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|timestamp
decl_stmt|;
specifier|private
name|int
name|buildNumber
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FACET_ID
init|=
literal|"org.apache.archiva.metadata.repository.storage.maven2.artifact"
decl_stmt|;
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|String
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
specifier|public
name|int
name|getBuildNumber
parameter_list|()
block|{
return|return
name|buildNumber
return|;
block|}
specifier|public
name|void
name|setBuildNumber
parameter_list|(
name|int
name|buildNumber
parameter_list|)
block|{
name|this
operator|.
name|buildNumber
operator|=
name|buildNumber
expr_stmt|;
block|}
specifier|public
name|String
name|getFacetId
parameter_list|()
block|{
return|return
name|FACET_ID
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
comment|// TODO: not needed, perhaps artifact/version metadata facet should be separate interface?
return|return
literal|null
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toProperties
parameter_list|()
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|classifier
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"classifier"
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|timestamp
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"timestamp"
argument_list|,
name|timestamp
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|buildNumber
operator|>
literal|0
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"buildNumber"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|buildNumber
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
specifier|public
name|void
name|fromProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|type
operator|=
name|properties
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
expr_stmt|;
name|classifier
operator|=
name|properties
operator|.
name|get
argument_list|(
literal|"classifier"
argument_list|)
expr_stmt|;
name|timestamp
operator|=
name|properties
operator|.
name|get
argument_list|(
literal|"timestamp"
argument_list|)
expr_stmt|;
name|String
name|buildNumber
init|=
name|properties
operator|.
name|get
argument_list|(
literal|"buildNumber"
argument_list|)
decl_stmt|;
if|if
condition|(
name|buildNumber
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|buildNumber
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|buildNumber
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|MavenArtifactFacet
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|MavenArtifactFacet
name|that
init|=
operator|(
name|MavenArtifactFacet
operator|)
name|o
decl_stmt|;
return|return
name|StringUtils
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|this
operator|.
name|classifier
argument_list|)
return|;
block|}
block|}
end_class

end_unit

