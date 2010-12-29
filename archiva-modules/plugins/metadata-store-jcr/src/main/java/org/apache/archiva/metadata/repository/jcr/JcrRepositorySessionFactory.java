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
name|jcr
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|MetadataFacetFactory
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataResolver
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
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

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|RepositoryException
import|;
end_import

begin_comment
comment|/**  * @plexus.component role="org.apache.archiva.metadata.repository.RepositorySessionFactory" role-hint="jcr"  */
end_comment

begin_class
specifier|public
class|class
name|JcrRepositorySessionFactory
implements|implements
name|RepositorySessionFactory
implements|,
name|Initializable
block|{
comment|/**      * @plexus.requirement role="org.apache.archiva.metadata.model.MetadataFacetFactory"      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|metadataFacetFactories
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|Repository
name|repository
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataResolver
name|metadataResolver
decl_stmt|;
specifier|public
name|RepositorySession
name|createSession
parameter_list|()
block|{
try|try
block|{
comment|// FIXME: is this the right separation? or should a JCR session object contain the JCR session information?
comment|//  such a change might allow us to avoid creating two objects for each request. It would also clear up
comment|//  the ambiguities in the API where session& repository are the inverse of JCR; and the resolver is
comment|//  retrieved from the session but must have it passed in. These should be reviewed before finalising the
comment|//  API.
name|MetadataRepository
name|metadataRepository
init|=
operator|new
name|JcrMetadataRepository
argument_list|(
name|metadataFacetFactories
argument_list|,
name|repository
argument_list|)
decl_stmt|;
return|return
operator|new
name|RepositorySession
argument_list|(
name|metadataRepository
argument_list|,
name|metadataResolver
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
comment|// FIXME: a custom exception requires refactoring for callers to handle it
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|JcrMetadataRepository
name|metadataRepository
init|=
literal|null
decl_stmt|;
try|try
block|{
name|metadataRepository
operator|=
operator|new
name|JcrMetadataRepository
argument_list|(
name|metadataFacetFactories
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|JcrMetadataRepository
operator|.
name|initialize
argument_list|(
name|metadataRepository
operator|.
name|getJcrSession
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InitializationException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|metadataRepository
operator|!=
literal|null
condition|)
block|{
name|metadataRepository
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

