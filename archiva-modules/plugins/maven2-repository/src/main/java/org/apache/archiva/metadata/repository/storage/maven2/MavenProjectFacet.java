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
name|MavenProjectFacet
implements|implements
name|MetadataFacet
block|{
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|MavenProjectParent
name|parent
decl_stmt|;
specifier|private
name|String
name|packaging
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FACET_ID
init|=
literal|"org.apache.archiva.metadata.repository.storage.maven2.project"
decl_stmt|;
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|MavenProjectParent
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|MavenProjectParent
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
specifier|public
name|String
name|getPackaging
parameter_list|()
block|{
return|return
name|packaging
return|;
block|}
specifier|public
name|void
name|setPackaging
parameter_list|(
name|String
name|packaging
parameter_list|)
block|{
name|this
operator|.
name|packaging
operator|=
name|packaging
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
comment|// TODO: not needed, perhaps version metadata facet should be separate interface?
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
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"packaging"
argument_list|,
name|packaging
argument_list|)
expr_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"parent.groupId"
argument_list|,
name|parent
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"parent.artifactId"
argument_list|,
name|parent
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"parent.version"
argument_list|,
name|parent
operator|.
name|getVersion
argument_list|()
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
name|groupId
operator|=
name|properties
operator|.
name|get
argument_list|(
literal|"groupId"
argument_list|)
expr_stmt|;
name|artifactId
operator|=
name|properties
operator|.
name|get
argument_list|(
literal|"artifactId"
argument_list|)
expr_stmt|;
name|packaging
operator|=
name|properties
operator|.
name|get
argument_list|(
literal|"packaging"
argument_list|)
expr_stmt|;
name|String
name|parentArtifactId
init|=
name|properties
operator|.
name|get
argument_list|(
literal|"parent.artifactId"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentArtifactId
operator|!=
literal|null
condition|)
block|{
name|MavenProjectParent
name|parent
init|=
operator|new
name|MavenProjectParent
argument_list|()
decl_stmt|;
name|parent
operator|.
name|setGroupId
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"parent.groupId"
argument_list|)
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setArtifactId
argument_list|(
name|parentArtifactId
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setVersion
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"parent.version"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

