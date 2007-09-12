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
name|web
operator|.
name|repository
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ArchivaConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArtifactReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ProjectReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|VersionedReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|ProxyException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|RepositoryProxyConnectors
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ArchivaConfigurationAdaptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|BidirectionalRepositoryLayout
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|BidirectionalRepositoryLayoutFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|LayoutException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|metadata
operator|.
name|MetadataTools
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|metadata
operator|.
name|RepositoryMetadataException
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
name|webdav
operator|.
name|AbstractDavServerComponent
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
name|webdav
operator|.
name|DavServerComponent
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
name|webdav
operator|.
name|DavServerException
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
name|webdav
operator|.
name|servlet
operator|.
name|DavServerRequest
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
name|webdav
operator|.
name|util
operator|.
name|WebdavMethodUtil
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
name|HttpServletResponse
import|;
end_import

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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * ProxiedDavServer  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.codehaus.plexus.webdav.DavServerComponent"  * role-hint="proxied"  * instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ProxiedDavServer
extends|extends
name|AbstractDavServerComponent
block|{
comment|/**      * @plexus.requirement role-hint="simple"      */
specifier|private
name|DavServerComponent
name|davServer
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|RepositoryProxyConnectors
name|connectors
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataTools
name|metadataTools
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|BidirectionalRepositoryLayoutFactory
name|layoutFactory
decl_stmt|;
specifier|private
name|BidirectionalRepositoryLayout
name|layout
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|repositoryConfiguration
decl_stmt|;
specifier|private
name|ArchivaRepository
name|managedRepository
decl_stmt|;
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|davServer
operator|.
name|getPrefix
argument_list|()
return|;
block|}
specifier|public
name|File
name|getRootDirectory
parameter_list|()
block|{
return|return
name|davServer
operator|.
name|getRootDirectory
argument_list|()
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|davServer
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRootDirectory
parameter_list|(
name|File
name|rootDirectory
parameter_list|)
block|{
name|davServer
operator|.
name|setRootDirectory
argument_list|(
name|rootDirectory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|DavServerException
block|{
name|davServer
operator|.
name|init
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|repositoryConfiguration
operator|=
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|managedRepository
operator|=
name|ArchivaConfigurationAdaptor
operator|.
name|toArchivaRepository
argument_list|(
name|repositoryConfiguration
argument_list|)
expr_stmt|;
try|try
block|{
name|layout
operator|=
name|layoutFactory
operator|.
name|getLayout
argument_list|(
name|managedRepository
operator|.
name|getLayoutType
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DavServerException
argument_list|(
literal|"Unable to initialize dav server: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|process
parameter_list|(
name|DavServerRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|DavServerException
throws|,
name|ServletException
throws|,
name|IOException
block|{
if|if
condition|(
name|WebdavMethodUtil
operator|.
name|isReadMethod
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|hasResource
argument_list|(
name|request
operator|.
name|getLogicalResource
argument_list|()
argument_list|)
condition|)
block|{
name|fetchContentFromProxies
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Create parent directories that don't exist when writing a file
comment|// This actually makes this implementation not compliant to the WebDAV RFC - but we have enough knowledge
comment|// about how the collection is being used to do this reasonably and some versions of Maven's WebDAV don't
comment|// correctly create the collections themselves.
name|File
name|rootDirectory
init|=
name|getRootDirectory
argument_list|()
decl_stmt|;
if|if
condition|(
name|rootDirectory
operator|!=
literal|null
condition|)
block|{
operator|new
name|File
argument_list|(
name|rootDirectory
argument_list|,
name|request
operator|.
name|getLogicalResource
argument_list|()
argument_list|)
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
name|davServer
operator|.
name|process
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|fetchContentFromProxies
parameter_list|(
name|DavServerRequest
name|request
parameter_list|)
throws|throws
name|ServletException
block|{
name|String
name|resource
init|=
name|request
operator|.
name|getLogicalResource
argument_list|()
decl_stmt|;
if|if
condition|(
name|resource
operator|.
name|endsWith
argument_list|(
literal|".sha1"
argument_list|)
operator|||
name|resource
operator|.
name|endsWith
argument_list|(
literal|".md5"
argument_list|)
condition|)
block|{
comment|// Checksums are fetched with artifact / metadata.
return|return;
block|}
comment|// Is it a Metadata resource?
if|if
condition|(
name|resource
operator|.
name|endsWith
argument_list|(
literal|"/"
operator|+
name|MetadataTools
operator|.
name|MAVEN_METADATA
argument_list|)
condition|)
block|{
name|ProjectReference
name|project
decl_stmt|;
name|VersionedReference
name|versioned
decl_stmt|;
try|try
block|{
name|versioned
operator|=
name|metadataTools
operator|.
name|toVersionedReference
argument_list|(
name|resource
argument_list|)
expr_stmt|;
if|if
condition|(
name|versioned
operator|!=
literal|null
condition|)
block|{
name|connectors
operator|.
name|fetchFromProxies
argument_list|(
name|managedRepository
argument_list|,
name|versioned
argument_list|)
expr_stmt|;
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|setPathInfo
argument_list|(
name|metadataTools
operator|.
name|toPath
argument_list|(
name|versioned
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
comment|/* eat it */
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Unable to fetch versioned metadata resource."
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|project
operator|=
name|metadataTools
operator|.
name|toProjectReference
argument_list|(
name|resource
argument_list|)
expr_stmt|;
if|if
condition|(
name|project
operator|!=
literal|null
condition|)
block|{
name|connectors
operator|.
name|fetchFromProxies
argument_list|(
name|managedRepository
argument_list|,
name|project
argument_list|)
expr_stmt|;
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|setPathInfo
argument_list|(
name|metadataTools
operator|.
name|toPath
argument_list|(
name|project
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryMetadataException
name|e
parameter_list|)
block|{
comment|/* eat it */
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Unable to fetch project metadata resource."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// Not any of the above? Then it's gotta be an artifact reference.
name|ArtifactReference
name|artifact
decl_stmt|;
name|BidirectionalRepositoryLayout
name|resourceLayout
decl_stmt|;
try|try
block|{
name|resourceLayout
operator|=
name|layoutFactory
operator|.
name|getLayoutForPath
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* invalid request - eat it */
return|return;
block|}
try|try
block|{
name|artifact
operator|=
name|resourceLayout
operator|.
name|toArtifactReference
argument_list|(
name|resource
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|connectors
operator|.
name|fetchFromProxies
argument_list|(
name|managedRepository
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|setPathInfo
argument_list|(
name|layout
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* eat it */
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Unable to fetch artifact resource."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|ManagedRepositoryConfiguration
name|getRepositoryConfiguration
parameter_list|()
block|{
return|return
name|repositoryConfiguration
return|;
block|}
block|}
end_class

end_unit

