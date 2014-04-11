begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
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
name|jackrabbit
operator|.
name|util
operator|.
name|Text
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavLocatorFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavResourceLocator
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
name|webdav
operator|.
name|util
operator|.
name|RepositoryPathUtil
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaDavLocatorFactory
implements|implements
name|DavLocatorFactory
block|{
annotation|@
name|Override
specifier|public
name|DavResourceLocator
name|createResourceLocator
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|href
parameter_list|)
block|{
comment|// build prefix string and remove all prefixes from the given href.
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|href
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|href
operator|=
name|href
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// special treatment for root item, that has no name but '/' path.
if|if
condition|(
name|href
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|href
argument_list|)
condition|)
block|{
name|href
operator|=
literal|"/"
expr_stmt|;
block|}
specifier|final
name|String
name|repository
init|=
name|RepositoryPathUtil
operator|.
name|getRepositoryName
argument_list|(
name|href
argument_list|)
decl_stmt|;
return|return
operator|new
name|ArchivaDavResourceLocator
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|,
name|Text
operator|.
name|unescape
argument_list|(
name|href
argument_list|)
argument_list|,
name|repository
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|DavResourceLocator
name|createResourceLocator
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|workspacePath
parameter_list|,
name|String
name|resourcePath
parameter_list|)
block|{
return|return
name|createResourceLocator
argument_list|(
name|prefix
argument_list|,
name|workspacePath
argument_list|,
name|resourcePath
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|DavResourceLocator
name|createResourceLocator
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|workspacePath
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|isResourcePath
parameter_list|)
block|{
specifier|final
name|String
name|repository
init|=
name|RepositoryPathUtil
operator|.
name|getRepositoryName
argument_list|(
name|path
argument_list|)
decl_stmt|;
return|return
operator|new
name|ArchivaDavResourceLocator
argument_list|(
name|prefix
argument_list|,
name|path
argument_list|,
name|repository
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

