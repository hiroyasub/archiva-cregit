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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
comment|/**  * SearchResultHit   *  * @version $Id: SearchResultHit.java 740552 2009-02-04 01:09:17Z oching $  */
end_comment

begin_class
specifier|public
class|class
name|SearchResultHit
block|{
comment|// The (optional) context for this result.
specifier|private
name|String
name|context
decl_stmt|;
comment|// Basic hit, direct to non-artifact resource.
specifier|private
name|String
name|url
decl_stmt|;
comment|// Advanced hit, reference to groupId.
specifier|private
name|String
name|groupId
decl_stmt|;
comment|//  Advanced hit, reference to artifactId.
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|// TODO: remove/deprecate this field!
specifier|private
name|String
name|version
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|repositoryId
init|=
literal|""
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|void
name|setContext
parameter_list|(
name|String
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getUrlFilename
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
operator|.
name|substring
argument_list|(
name|this
operator|.
name|url
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
return|;
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
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getVersions
parameter_list|()
block|{
return|return
name|versions
return|;
block|}
specifier|public
name|void
name|setVersions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|versions
parameter_list|)
block|{
name|this
operator|.
name|versions
operator|=
name|versions
expr_stmt|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|void
name|addVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|versions
operator|==
literal|null
condition|)
block|{
name|versions
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|versions
operator|.
name|add
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SearchResultHit{"
operator|+
literal|"context='"
operator|+
name|context
operator|+
literal|'\''
operator|+
literal|", url='"
operator|+
name|url
operator|+
literal|'\''
operator|+
literal|", groupId='"
operator|+
name|groupId
operator|+
literal|'\''
operator|+
literal|", artifactId='"
operator|+
name|artifactId
operator|+
literal|'\''
operator|+
literal|", version='"
operator|+
name|version
operator|+
literal|'\''
operator|+
literal|", repositoryId='"
operator|+
name|repositoryId
operator|+
literal|'\''
operator|+
literal|", versions="
operator|+
name|versions
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

