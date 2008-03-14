begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|webdav
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Iterator
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
comment|/**  * AbstractDavServerComponent   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id: AbstractDavServerComponent.java 6000 2007-03-04 22:01:49Z joakime $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDavServerComponent
implements|implements
name|DavServerComponent
block|{
specifier|private
name|List
name|listeners
decl_stmt|;
specifier|protected
name|boolean
name|useIndexHtml
init|=
literal|false
decl_stmt|;
specifier|public
name|AbstractDavServerComponent
parameter_list|()
block|{
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addListener
parameter_list|(
name|DavServerListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeListener
parameter_list|(
name|DavServerListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|triggerCollectionCreated
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DavServerListener
name|listener
init|=
operator|(
name|DavServerListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|serverCollectionCreated
argument_list|(
name|this
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* ignore error */
block|}
block|}
block|}
specifier|protected
name|void
name|triggerCollectionRemoved
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DavServerListener
name|listener
init|=
operator|(
name|DavServerListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|serverCollectionRemoved
argument_list|(
name|this
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* ignore error */
block|}
block|}
block|}
specifier|protected
name|void
name|triggerResourceCreated
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DavServerListener
name|listener
init|=
operator|(
name|DavServerListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|serverResourceCreated
argument_list|(
name|this
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* ignore error */
block|}
block|}
block|}
specifier|protected
name|void
name|triggerResourceRemoved
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DavServerListener
name|listener
init|=
operator|(
name|DavServerListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|serverResourceRemoved
argument_list|(
name|this
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* ignore error */
block|}
block|}
block|}
specifier|protected
name|void
name|triggerResourceModified
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DavServerListener
name|listener
init|=
operator|(
name|DavServerListener
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|listener
operator|.
name|serverResourceModified
argument_list|(
name|this
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* ignore error */
block|}
block|}
block|}
specifier|public
name|boolean
name|hasResource
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|File
name|rootDir
init|=
name|getRootDirectory
argument_list|()
decl_stmt|;
if|if
condition|(
name|rootDir
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|File
name|resourceFile
init|=
operator|new
name|File
argument_list|(
name|rootDir
argument_list|,
name|resource
argument_list|)
decl_stmt|;
return|return
name|resourceFile
operator|.
name|exists
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isUseIndexHtml
parameter_list|()
block|{
return|return
name|this
operator|.
name|useIndexHtml
return|;
block|}
specifier|public
name|void
name|setUseIndexHtml
parameter_list|(
name|boolean
name|useIndexHtml
parameter_list|)
block|{
name|this
operator|.
name|useIndexHtml
operator|=
name|useIndexHtml
expr_stmt|;
block|}
block|}
end_class

end_unit

