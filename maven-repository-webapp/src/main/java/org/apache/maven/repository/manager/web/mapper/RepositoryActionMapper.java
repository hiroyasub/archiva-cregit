begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|manager
operator|.
name|web
operator|.
name|mapper
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|webwork
operator|.
name|dispatcher
operator|.
name|mapper
operator|.
name|ActionMapping
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|webwork
operator|.
name|dispatcher
operator|.
name|mapper
operator|.
name|DefaultActionMapper
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
comment|/**  * Map alternate URLs to specific actions. Used for the repository browser and the proxy.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryActionMapper
extends|extends
name|DefaultActionMapper
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BROWSE_PREFIX
init|=
literal|"/browse/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROXY_PREFIX
init|=
literal|"/proxy/"
decl_stmt|;
specifier|public
name|String
name|getUriFromActionMapping
parameter_list|(
name|ActionMapping
name|actionMapping
parameter_list|)
block|{
name|Map
name|params
init|=
name|actionMapping
operator|.
name|getParams
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"browseGroup"
operator|.
name|equals
argument_list|(
name|actionMapping
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|BROWSE_PREFIX
operator|+
name|params
operator|.
name|remove
argument_list|(
literal|"groupId"
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"browseArtifact"
operator|.
name|equals
argument_list|(
name|actionMapping
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|BROWSE_PREFIX
operator|+
name|params
operator|.
name|remove
argument_list|(
literal|"groupId"
argument_list|)
operator|+
literal|"/"
operator|+
name|params
operator|.
name|remove
argument_list|(
literal|"artifactId"
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"showArtifact"
operator|.
name|equals
argument_list|(
name|actionMapping
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|BROWSE_PREFIX
operator|+
name|params
operator|.
name|remove
argument_list|(
literal|"groupId"
argument_list|)
operator|+
literal|"/"
operator|+
name|params
operator|.
name|remove
argument_list|(
literal|"artifactId"
argument_list|)
operator|+
literal|"/"
operator|+
name|params
operator|.
name|remove
argument_list|(
literal|"version"
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"proxy"
operator|.
name|equals
argument_list|(
name|actionMapping
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|PROXY_PREFIX
operator|+
name|params
operator|.
name|remove
argument_list|(
literal|"path"
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getUriFromActionMapping
argument_list|(
name|actionMapping
argument_list|)
return|;
block|}
specifier|public
name|ActionMapping
name|getMapping
parameter_list|(
name|HttpServletRequest
name|httpServletRequest
parameter_list|)
block|{
name|String
name|path
init|=
name|httpServletRequest
operator|.
name|getServletPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|BROWSE_PREFIX
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|BROWSE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|ActionMapping
argument_list|(
literal|"browse"
argument_list|,
literal|"/"
argument_list|,
literal|""
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
name|String
index|[]
name|parts
init|=
name|path
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
return|return
operator|new
name|ActionMapping
argument_list|(
literal|"browseGroup"
argument_list|,
literal|"/"
argument_list|,
literal|""
argument_list|,
name|params
argument_list|)
return|;
block|}
if|else if
condition|(
name|parts
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
return|return
operator|new
name|ActionMapping
argument_list|(
literal|"browseArtifact"
argument_list|,
literal|"/"
argument_list|,
literal|""
argument_list|,
name|params
argument_list|)
return|;
block|}
if|else if
condition|(
name|parts
operator|.
name|length
operator|==
literal|3
condition|)
block|{
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|parts
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
return|return
operator|new
name|ActionMapping
argument_list|(
literal|"showArtifact"
argument_list|,
literal|"/"
argument_list|,
literal|""
argument_list|,
name|params
argument_list|)
return|;
block|}
block|}
block|}
if|else if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|PROXY_PREFIX
argument_list|)
condition|)
block|{
comment|// retain the leading /
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|PROXY_PREFIX
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
operator|new
name|ActionMapping
argument_list|(
literal|"proxy"
argument_list|,
literal|"/"
argument_list|,
literal|""
argument_list|,
name|params
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getMapping
argument_list|(
name|httpServletRequest
argument_list|)
return|;
block|}
block|}
end_class

end_unit

