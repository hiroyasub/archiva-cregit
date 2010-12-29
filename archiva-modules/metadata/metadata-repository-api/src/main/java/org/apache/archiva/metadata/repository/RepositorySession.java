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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * The repository session provides a single interface to accessing Archiva repositories. It provides access to three  * resources:  *<ul>  *<li>{@link MetadataRepository} - the metadata content repository for read/write access, in its current state (no  * remote resources will be retrieved in the process</li>  *<li>{@link MetadataResolver} - access to resolve metadata content, accommodating metadata not yet stored or up to  * date in the content repository (i.e. virtualised repositories, remote proxied content, or metadata in a different  * model format in the repository storage)</li>  *<li>{@link org.apache.archiva.metadata.repository.storage.RepositoryStorage} - access to the physical storage of a  * repository and the source artifacts and project models</li>  *</ul>  */
end_comment

begin_class
specifier|public
class|class
name|RepositorySession
block|{
specifier|private
specifier|final
name|MetadataRepository
name|repository
decl_stmt|;
specifier|private
specifier|final
name|MetadataResolver
name|resolver
decl_stmt|;
specifier|private
name|boolean
name|dirty
decl_stmt|;
comment|// FIXME: include storage here too - perhaps a factory based on repository ID, or one per type to retrieve and
comment|//        operate on a given repo within the storage API
specifier|public
name|RepositorySession
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|MetadataResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|metadataRepository
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
specifier|public
name|MetadataRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|MetadataResolver
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
specifier|public
name|void
name|save
parameter_list|()
block|{
try|try
block|{
name|repository
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
comment|// FIXME
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|dirty
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|revert
parameter_list|()
block|{
try|try
block|{
name|repository
operator|.
name|revert
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
comment|// FIXME
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|dirty
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Close the session. Required to be called for all open sessions to ensure resources are properly released.      * If the session has been marked as dirty, it will be saved. This may save partial changes in the case of a typical      *<code>try { ... } finally { ... }</code> approach - if this is a problem, ensure you revert changes when an      * exception occurs.      */
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|dirty
condition|)
block|{
name|save
argument_list|()
expr_stmt|;
block|}
name|repository
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|markDirty
parameter_list|()
block|{
name|this
operator|.
name|dirty
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

