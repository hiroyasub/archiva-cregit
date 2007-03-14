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
name|reporting
operator|.
name|model
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
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * ArtifactResultsKey - used by jpox for application identity for the {@link ArtifactResults} object and table.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactResultsKey
implements|implements
name|Serializable
block|{
specifier|public
name|String
name|groupId
init|=
literal|""
decl_stmt|;
specifier|public
name|String
name|artifactId
init|=
literal|""
decl_stmt|;
specifier|public
name|String
name|version
init|=
literal|""
decl_stmt|;
specifier|public
name|String
name|artifactType
init|=
literal|""
decl_stmt|;
specifier|public
name|String
name|classifier
init|=
literal|""
decl_stmt|;
specifier|public
name|ArtifactResultsKey
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|ArtifactResultsKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|String
name|parts
index|[]
init|=
name|StringUtils
operator|.
name|splitPreserveAllTokens
argument_list|(
name|key
argument_list|,
literal|':'
argument_list|)
decl_stmt|;
name|groupId
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|artifactId
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
name|version
operator|=
name|parts
index|[
literal|2
index|]
expr_stmt|;
name|artifactType
operator|=
name|parts
index|[
literal|3
index|]
expr_stmt|;
name|classifier
operator|=
name|parts
index|[
literal|4
index|]
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|join
argument_list|(
operator|new
name|String
index|[]
block|{
name|groupId
block|,
name|artifactId
block|,
name|version
block|,
name|artifactType
block|,
name|classifier
block|}
argument_list|,
literal|':'
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|PRIME
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|groupId
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|groupId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|artifactId
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|artifactId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|version
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|version
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|artifactType
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|artifactType
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|classifier
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|classifier
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|ArtifactResultsKey
name|other
init|=
operator|(
name|ArtifactResultsKey
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|groupId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|groupId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|groupId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|artifactId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|artifactId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|artifactId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|version
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|version
operator|.
name|equals
argument_list|(
name|other
operator|.
name|version
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|artifactType
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|artifactType
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|artifactType
operator|.
name|equals
argument_list|(
name|other
operator|.
name|artifactType
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|classifier
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|classifier
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|classifier
operator|.
name|equals
argument_list|(
name|other
operator|.
name|classifier
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

