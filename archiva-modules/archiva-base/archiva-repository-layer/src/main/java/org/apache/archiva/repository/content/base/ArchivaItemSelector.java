begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|base
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
name|repository
operator|.
name|content
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
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|ContentItem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|ItemSelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|Namespace
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|Version
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
name|Collections
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

begin_comment
comment|/**  * Item selector for querying artifacts and other content items.  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaItemSelector
implements|implements
name|ItemSelector
block|{
specifier|private
name|String
name|projectId
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|version
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|artifactVersion
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|artifactId
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|namespace
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|type
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|classifier
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|extension
init|=
literal|""
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
decl_stmt|;
specifier|private
name|boolean
name|includeRelatedArtifacts
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|recurse
init|=
literal|false
decl_stmt|;
specifier|private
name|ArchivaItemSelector
parameter_list|( )
block|{
block|}
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|( )
block|{
return|return
operator|new
name|Builder
argument_list|( )
return|;
block|}
specifier|public
specifier|static
class|class
name|Builder
block|{
specifier|private
specifier|final
name|ArchivaItemSelector
name|selector
init|=
operator|new
name|ArchivaItemSelector
argument_list|( )
decl_stmt|;
specifier|public
name|Builder
name|withItem
parameter_list|(
name|ContentItem
name|item
parameter_list|)
block|{
if|if
condition|(
name|item
operator|instanceof
name|Namespace
condition|)
block|{
name|Namespace
name|ns
init|=
operator|(
name|Namespace
operator|)
name|item
decl_stmt|;
name|selector
operator|.
name|namespace
operator|=
name|ns
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|item
operator|instanceof
name|Project
condition|)
block|{
name|Project
name|proj
init|=
operator|(
name|Project
operator|)
name|item
decl_stmt|;
name|selector
operator|.
name|namespace
operator|=
name|proj
operator|.
name|getNamespace
argument_list|( )
operator|.
name|getId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|projectId
operator|=
name|proj
operator|.
name|getId
argument_list|( )
expr_stmt|;
block|}
if|else if
condition|(
name|item
operator|instanceof
name|Version
condition|)
block|{
name|Version
name|version
init|=
operator|(
name|Version
operator|)
name|item
decl_stmt|;
name|selector
operator|.
name|namespace
operator|=
name|version
operator|.
name|getProject
argument_list|( )
operator|.
name|getNamespace
argument_list|( )
operator|.
name|getId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|projectId
operator|=
name|version
operator|.
name|getProject
argument_list|( )
operator|.
name|getId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|version
operator|=
name|version
operator|.
name|getId
argument_list|( )
expr_stmt|;
block|}
if|else if
condition|(
name|item
operator|instanceof
name|Artifact
condition|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|item
decl_stmt|;
name|selector
operator|.
name|namespace
operator|=
name|artifact
operator|.
name|getVersion
argument_list|( )
operator|.
name|getProject
argument_list|( )
operator|.
name|getNamespace
argument_list|( )
operator|.
name|getId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|projectId
operator|=
name|artifact
operator|.
name|getVersion
argument_list|( )
operator|.
name|getProject
argument_list|( )
operator|.
name|getId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|version
operator|=
name|artifact
operator|.
name|getVersion
argument_list|( )
operator|.
name|getId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|artifactId
operator|=
name|artifact
operator|.
name|getId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|artifactVersion
operator|=
name|artifact
operator|.
name|getArtifactVersion
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|extension
operator|=
name|artifact
operator|.
name|getExtension
argument_list|( )
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|att
range|:
name|item
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|selector
operator|.
name|setAttribute
argument_list|(
name|att
operator|.
name|getKey
argument_list|( )
argument_list|,
name|att
operator|.
name|getValue
argument_list|( )
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withSelector
parameter_list|(
name|ItemSelector
name|givenSelector
parameter_list|)
block|{
name|selector
operator|.
name|namespace
operator|=
name|givenSelector
operator|.
name|getNamespace
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|projectId
operator|=
name|givenSelector
operator|.
name|getProjectId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|version
operator|=
name|givenSelector
operator|.
name|getVersion
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|extension
operator|=
name|givenSelector
operator|.
name|getExtension
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|artifactId
operator|=
name|givenSelector
operator|.
name|getArtifactId
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|artifactVersion
operator|=
name|givenSelector
operator|.
name|getArtifactVersion
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|recurse
operator|=
name|givenSelector
operator|.
name|recurse
argument_list|( )
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|att
range|:
name|givenSelector
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|selector
operator|.
name|setAttribute
argument_list|(
name|att
operator|.
name|getKey
argument_list|( )
argument_list|,
name|att
operator|.
name|getValue
argument_list|( )
argument_list|)
expr_stmt|;
block|}
name|selector
operator|.
name|type
operator|=
name|givenSelector
operator|.
name|getType
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|classifier
operator|=
name|givenSelector
operator|.
name|getClassifier
argument_list|( )
expr_stmt|;
name|selector
operator|.
name|includeRelatedArtifacts
operator|=
name|givenSelector
operator|.
name|includeRelatedArtifacts
argument_list|( )
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
if|if
condition|(
name|namespace
operator|!=
literal|null
condition|)
block|{
name|selector
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withProjectId
parameter_list|(
name|String
name|projectId
parameter_list|)
block|{
if|if
condition|(
name|projectId
operator|!=
literal|null
condition|)
block|{
name|selector
operator|.
name|projectId
operator|=
name|projectId
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|selector
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withArtifactVersion
parameter_list|(
name|String
name|artifactVersion
parameter_list|)
block|{
if|if
condition|(
name|artifactVersion
operator|!=
literal|null
condition|)
block|{
name|selector
operator|.
name|artifactVersion
operator|=
name|artifactVersion
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
if|if
condition|(
name|artifactId
operator|!=
literal|null
condition|)
block|{
name|selector
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|selector
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
if|if
condition|(
name|classifier
operator|!=
literal|null
condition|)
block|{
name|selector
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withAttribute
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|selector
operator|.
name|setAttribute
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|withExtension
parameter_list|(
name|String
name|extension
parameter_list|)
block|{
if|if
condition|(
name|extension
operator|!=
literal|null
condition|)
block|{
name|selector
operator|.
name|extension
operator|=
name|extension
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|includeRelatedArtifacts
parameter_list|()
block|{
name|selector
operator|.
name|includeRelatedArtifacts
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|recurse
parameter_list|()
block|{
name|selector
operator|.
name|recurse
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ArchivaItemSelector
name|build
parameter_list|( )
block|{
return|return
name|selector
return|;
block|}
block|}
specifier|private
name|void
name|setAttribute
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|attributes
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|attributes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
expr_stmt|;
block|}
name|this
operator|.
name|attributes
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getProjectId
parameter_list|( )
block|{
return|return
name|projectId
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNamespace
parameter_list|( )
block|{
return|return
name|namespace
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getVersion
parameter_list|( )
block|{
return|return
name|version
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getArtifactVersion
parameter_list|( )
block|{
return|return
name|artifactVersion
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getArtifactId
parameter_list|( )
block|{
return|return
name|artifactId
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getType
parameter_list|( )
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassifier
parameter_list|( )
block|{
return|return
name|classifier
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAttribute
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|attributes
operator|==
literal|null
operator|||
operator|!
name|this
operator|.
name|attributes
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|this
operator|.
name|attributes
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getExtension
parameter_list|(  )
block|{
return|return
name|extension
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|( )
block|{
if|if
condition|(
name|this
operator|.
name|attributes
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|( )
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|this
operator|.
name|attributes
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|recurse
parameter_list|( )
block|{
return|return
name|recurse
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|includeRelatedArtifacts
parameter_list|( )
block|{
return|return
name|includeRelatedArtifacts
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasAttributes
parameter_list|( )
block|{
return|return
name|attributes
operator|!=
literal|null
operator|&&
name|attributes
operator|.
name|size
argument_list|( )
operator|>
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasExtension
parameter_list|( )
block|{
return|return
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|extension
argument_list|)
return|;
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
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|( )
operator|!=
name|o
operator|.
name|getClass
argument_list|( )
condition|)
return|return
literal|false
return|;
name|ArchivaItemSelector
name|that
init|=
operator|(
name|ArchivaItemSelector
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|includeRelatedArtifacts
operator|!=
name|that
operator|.
name|includeRelatedArtifacts
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|recurse
operator|!=
name|that
operator|.
name|recurse
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|projectId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|projectId
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|version
operator|.
name|equals
argument_list|(
name|that
operator|.
name|version
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|artifactVersion
operator|.
name|equals
argument_list|(
name|that
operator|.
name|artifactVersion
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|artifactId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|artifactId
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|namespace
operator|.
name|equals
argument_list|(
name|that
operator|.
name|namespace
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|type
operator|.
name|equals
argument_list|(
name|that
operator|.
name|type
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|classifier
operator|.
name|equals
argument_list|(
name|that
operator|.
name|classifier
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|extension
operator|.
name|equals
argument_list|(
name|that
operator|.
name|extension
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
name|attributes
operator|!=
literal|null
condition|?
name|attributes
operator|.
name|equals
argument_list|(
name|that
operator|.
name|attributes
argument_list|)
else|:
name|that
operator|.
name|attributes
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
name|int
name|result
init|=
name|projectId
operator|.
name|hashCode
argument_list|( )
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|version
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|artifactVersion
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|artifactId
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|namespace
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|type
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|classifier
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|extension
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|attributes
operator|!=
literal|null
condition|?
name|attributes
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|includeRelatedArtifacts
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|recurse
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

