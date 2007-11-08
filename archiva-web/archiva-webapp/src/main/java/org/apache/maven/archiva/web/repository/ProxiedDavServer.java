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
name|common
operator|.
name|utils
operator|.
name|PathUtil
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
name|ManagedRepositoryContent
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
name|RepositoryContentFactory
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
name|RepositoryException
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
name|RepositoryNotFoundException
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
name|audit
operator|.
name|AuditEvent
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
name|audit
operator|.
name|AuditListener
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
name|audit
operator|.
name|Auditable
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
name|content
operator|.
name|RepositoryRequest
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|ArchivaUser
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
name|model
operator|.
name|DistributionManagement
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
name|model
operator|.
name|Model
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
name|model
operator|.
name|Relocation
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
name|model
operator|.
name|io
operator|.
name|xpp3
operator|.
name|MavenXpp3Reader
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
name|util
operator|.
name|xml
operator|.
name|pull
operator|.
name|XmlPullParserException
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
name|DavServerListener
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
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|List
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

begin_comment
comment|/**  * ProxiedDavServer  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.codehaus.plexus.webdav.DavServerComponent"  * role-hint="proxied" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ProxiedDavServer
extends|extends
name|AbstractDavServerComponent
implements|implements
name|Auditable
block|{
comment|/**      * @plexus.requirement role-hint="simple"      */
specifier|private
name|DavServerComponent
name|davServer
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.repository.audit.AuditListener"      */
specifier|private
name|List
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
init|=
operator|new
name|ArrayList
argument_list|<
name|AuditListener
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryRequest
name|repositoryRequest
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
comment|/**      * @plexus.requirement role-hint="xwork"      */
specifier|private
name|ArchivaUser
name|archivaUser
decl_stmt|;
specifier|private
name|ManagedRepositoryContent
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
try|try
block|{
name|managedRepository
operator|=
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DavServerException
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
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DavServerException
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
name|boolean
name|isGet
init|=
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
decl_stmt|;
name|boolean
name|isPut
init|=
name|WebdavMethodUtil
operator|.
name|isWriteMethod
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|)
decl_stmt|;
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
name|isGet
condition|)
block|{
comment|// Default behaviour is to treat the resource natively.
name|File
name|resourceFile
init|=
operator|new
name|File
argument_list|(
name|managedRepository
operator|.
name|getRepoRoot
argument_list|()
argument_list|,
name|resource
argument_list|)
decl_stmt|;
comment|// If this a directory resource, then we are likely browsing.
if|if
condition|(
name|resourceFile
operator|.
name|exists
argument_list|()
operator|&&
name|resourceFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|String
name|requestURL
init|=
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// [MRM-440] - If webdav URL lacks a trailing /, navigating to all links in the listing return 404.
if|if
condition|(
operator|!
name|requestURL
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|String
name|redirectToLocation
init|=
name|requestURL
operator|+
literal|"/"
decl_stmt|;
name|response
operator|.
name|sendRedirect
argument_list|(
name|redirectToLocation
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Process the request.
name|davServer
operator|.
name|process
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
comment|// All done.
return|return;
block|}
comment|// At this point the incoming request can either be in default or legacy layout format.
try|try
block|{
comment|// Perform an adjustment of the resource to the managed repository expected path.
name|resource
operator|=
name|repositoryRequest
operator|.
name|toNativePath
argument_list|(
name|request
operator|.
name|getLogicalResource
argument_list|()
argument_list|,
name|managedRepository
argument_list|)
expr_stmt|;
name|resourceFile
operator|=
operator|new
name|File
argument_list|(
name|managedRepository
operator|.
name|getRepoRoot
argument_list|()
argument_list|,
name|resource
argument_list|)
expr_stmt|;
comment|// Adjust the pathInfo resource to be in the format that the dav server impl expects.
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|setPathInfo
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|boolean
name|previouslyExisted
init|=
name|resourceFile
operator|.
name|exists
argument_list|()
decl_stmt|;
comment|// Attempt to fetch the resource from any defined proxy.
if|if
condition|(
name|fetchContentFromProxies
argument_list|(
name|request
argument_list|,
name|resource
argument_list|)
condition|)
block|{
name|processAuditEvents
argument_list|(
name|request
argument_list|,
name|resource
argument_list|,
name|previouslyExisted
argument_list|,
name|resourceFile
argument_list|,
literal|" (proxied)"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// Invalid resource, pass it on.
name|respondResourceMissing
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// All done.
return|return;
block|}
if|if
condition|(
name|resourceFile
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// [MRM-503] - Metadata file need Pragma:no-cache response header.
if|if
condition|(
name|request
operator|.
name|getLogicalResource
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/maven-metadata.xml"
argument_list|)
condition|)
block|{
name|response
operator|.
name|addHeader
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
expr_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"Cache-Control"
argument_list|,
literal|"no-cache"
argument_list|)
expr_stmt|;
block|}
comment|// TODO: [MRM-524] determine http caching options for other types of files (artifacts, sha1, md5, snapshots)
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
else|else
block|{
name|respondResourceMissing
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isPut
condition|)
block|{
comment|/* Create parent directories that don't exist when writing a file              * This actually makes this implementation not compliant to the              * WebDAV RFC - but we have enough knowledge              * about how the collection is being used to do this reasonably and              * some versions of Maven's WebDAV don't              * correctly create the collections themselves.              */
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
name|File
name|destDir
init|=
operator|new
name|File
argument_list|(
name|rootDirectory
argument_list|,
name|resource
argument_list|)
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|destDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|destDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|String
name|relPath
init|=
name|PathUtil
operator|.
name|getRelative
argument_list|(
name|rootDirectory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|destDir
argument_list|)
decl_stmt|;
name|triggerAuditEvent
argument_list|(
name|request
argument_list|,
name|relPath
argument_list|,
name|AuditEvent
operator|.
name|CREATE_DIR
argument_list|)
expr_stmt|;
block|}
block|}
name|File
name|resourceFile
init|=
operator|new
name|File
argument_list|(
name|managedRepository
operator|.
name|getRepoRoot
argument_list|()
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|boolean
name|previouslyExisted
init|=
name|resourceFile
operator|.
name|exists
argument_list|()
decl_stmt|;
comment|// Allow the dav server to process the put request.
name|davServer
operator|.
name|process
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|processAuditEvents
argument_list|(
name|request
argument_list|,
name|resource
argument_list|,
name|previouslyExisted
argument_list|,
name|resourceFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// All done.
return|return;
block|}
block|}
specifier|private
name|void
name|respondResourceMissing
parameter_list|(
name|DavServerRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|response
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
try|try
block|{
name|StringBuffer
name|missingUrl
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|missingUrl
operator|.
name|append
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getScheme
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"://"
argument_list|)
expr_stmt|;
name|missingUrl
operator|.
name|append
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getServerName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|missingUrl
operator|.
name|append
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getServerPort
argument_list|()
argument_list|)
expr_stmt|;
name|missingUrl
operator|.
name|append
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getServletPath
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|message
init|=
literal|"Error 404 Not Found"
decl_stmt|;
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|response
operator|.
name|setContentType
argument_list|(
literal|"text/html; charset=\"UTF-8\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<html>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<head><title>"
operator|+
name|message
operator|+
literal|"</title></head>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<body>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"<p><h1>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</h1></p>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"<p>The following resource does not exist:<a href=\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|missingUrl
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\">"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|missingUrl
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</a></p>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<pre>"
argument_list|)
expr_stmt|;
name|t
operator|.
name|printStackTrace
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</pre>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"</body></html>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|fetchContentFromProxies
parameter_list|(
name|DavServerRequest
name|request
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|ServletException
block|{
if|if
condition|(
name|repositoryRequest
operator|.
name|isSupportFile
argument_list|(
name|resource
argument_list|)
condition|)
block|{
comment|// Checksums are fetched with artifact / metadata.
comment|// Need to adjust the path for the checksum resource.
return|return
literal|false
return|;
block|}
comment|// Is it a Metadata resource?
if|if
condition|(
name|repositoryRequest
operator|.
name|isDefault
argument_list|(
name|resource
argument_list|)
operator|&&
name|repositoryRequest
operator|.
name|isMetadata
argument_list|(
name|resource
argument_list|)
condition|)
block|{
return|return
name|fetchMetadataFromProxies
argument_list|(
name|request
argument_list|,
name|resource
argument_list|)
return|;
block|}
comment|// Not any of the above? Then it's gotta be an artifact reference.
try|try
block|{
comment|// Get the artifact reference in a layout neutral way.
name|ArtifactReference
name|artifact
init|=
name|repositoryRequest
operator|.
name|toArtifactReference
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|applyServerSideRelocation
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|File
name|proxiedFile
init|=
name|connectors
operator|.
name|fetchFromProxies
argument_list|(
name|managedRepository
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
comment|// Set the path to the resource using managed repository specific layout format.
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|setPathInfo
argument_list|(
name|managedRepository
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|(
name|proxiedFile
operator|!=
literal|null
operator|)
return|;
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
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|fetchMetadataFromProxies
parameter_list|(
name|DavServerRequest
name|request
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|ServletException
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
return|return
literal|true
return|;
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
return|return
literal|true
return|;
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
return|return
literal|false
return|;
block|}
comment|/**      * A relocation capable client will request the POM prior to the artifact,      * and will then read meta-data and do client side relocation. A simplier      * client (like maven 1) will only request the artifact and not use the      * metadatas.      *<p>      * For such clients, archiva does server-side relocation by reading itself      * the&lt;relocation&gt; element in metadatas and serving the expected      * artifact.      */
specifier|protected
name|void
name|applyServerSideRelocation
parameter_list|(
name|ArtifactReference
name|artifact
parameter_list|)
throws|throws
name|ProxyException
block|{
if|if
condition|(
literal|"pom"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// Build the artifact POM reference
name|ArtifactReference
name|pomReference
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|pomReference
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|pomReference
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|pomReference
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|pomReference
operator|.
name|setType
argument_list|(
literal|"pom"
argument_list|)
expr_stmt|;
comment|// Get the artifact POM from proxied repositories if needed
name|connectors
operator|.
name|fetchFromProxies
argument_list|(
name|managedRepository
argument_list|,
name|pomReference
argument_list|)
expr_stmt|;
comment|// Open and read the POM from the managed repo
name|File
name|pom
init|=
name|managedRepository
operator|.
name|toFile
argument_list|(
name|pomReference
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pom
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Model
name|model
init|=
operator|new
name|MavenXpp3Reader
argument_list|()
operator|.
name|read
argument_list|(
operator|new
name|FileReader
argument_list|(
name|pom
argument_list|)
argument_list|)
decl_stmt|;
name|DistributionManagement
name|dist
init|=
name|model
operator|.
name|getDistributionManagement
argument_list|()
decl_stmt|;
if|if
condition|(
name|dist
operator|!=
literal|null
condition|)
block|{
name|Relocation
name|relocation
init|=
name|dist
operator|.
name|getRelocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|relocation
operator|!=
literal|null
condition|)
block|{
comment|// artifact is relocated : update the repositoryPath
if|if
condition|(
name|relocation
operator|.
name|getGroupId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|artifact
operator|.
name|setGroupId
argument_list|(
name|relocation
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|relocation
operator|.
name|getArtifactId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|relocation
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|relocation
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|artifact
operator|.
name|setVersion
argument_list|(
name|relocation
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
comment|// Artifact has no POM in repo : ignore
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Unable to read POM : ignore.
block|}
catch|catch
parameter_list|(
name|XmlPullParserException
name|e
parameter_list|)
block|{
comment|// Invalid POM : ignore
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addListener
parameter_list|(
name|DavServerListener
name|listener
parameter_list|)
block|{
name|super
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|davServer
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isUseIndexHtml
parameter_list|()
block|{
return|return
name|davServer
operator|.
name|isUseIndexHtml
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasResource
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
return|return
name|davServer
operator|.
name|hasResource
argument_list|(
name|resource
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeListener
parameter_list|(
name|DavServerListener
name|listener
parameter_list|)
block|{
name|davServer
operator|.
name|removeListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUseIndexHtml
parameter_list|(
name|boolean
name|useIndexHtml
parameter_list|)
block|{
name|super
operator|.
name|setUseIndexHtml
argument_list|(
name|useIndexHtml
argument_list|)
expr_stmt|;
name|davServer
operator|.
name|setUseIndexHtml
argument_list|(
name|useIndexHtml
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ManagedRepositoryContent
name|getRepository
parameter_list|()
block|{
return|return
name|managedRepository
return|;
block|}
specifier|private
name|void
name|processAuditEvents
parameter_list|(
name|DavServerRequest
name|request
parameter_list|,
name|String
name|resource
parameter_list|,
name|boolean
name|previouslyExisted
parameter_list|,
name|File
name|resourceFile
parameter_list|,
name|String
name|suffix
parameter_list|)
block|{
if|if
condition|(
name|suffix
operator|==
literal|null
condition|)
block|{
name|suffix
operator|=
literal|""
expr_stmt|;
block|}
comment|// Process Create Audit Events.
if|if
condition|(
operator|!
name|previouslyExisted
operator|&&
name|resourceFile
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|resourceFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|triggerAuditEvent
argument_list|(
name|request
argument_list|,
name|resource
argument_list|,
name|AuditEvent
operator|.
name|CREATE_FILE
operator|+
name|suffix
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|resourceFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|triggerAuditEvent
argument_list|(
name|request
argument_list|,
name|resource
argument_list|,
name|AuditEvent
operator|.
name|CREATE_DIR
operator|+
name|suffix
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Process Remove Audit Events.
if|else if
condition|(
name|previouslyExisted
operator|&&
operator|!
name|resourceFile
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|resourceFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|triggerAuditEvent
argument_list|(
name|request
argument_list|,
name|resource
argument_list|,
name|AuditEvent
operator|.
name|REMOVE_FILE
operator|+
name|suffix
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|resourceFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|triggerAuditEvent
argument_list|(
name|request
argument_list|,
name|resource
argument_list|,
name|AuditEvent
operator|.
name|REMOVE_DIR
operator|+
name|suffix
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Process modify events.
else|else
block|{
if|if
condition|(
name|resourceFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|triggerAuditEvent
argument_list|(
name|request
argument_list|,
name|resource
argument_list|,
name|AuditEvent
operator|.
name|MODIFY_FILE
operator|+
name|suffix
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|remoteIP
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|AuditEvent
name|event
init|=
operator|new
name|AuditEvent
argument_list|(
name|this
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|user
argument_list|,
name|resource
argument_list|,
name|action
argument_list|)
decl_stmt|;
name|event
operator|.
name|setRemoteIP
argument_list|(
name|remoteIP
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditListener
name|listener
range|:
name|auditListeners
control|)
block|{
name|listener
operator|.
name|auditEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|triggerAuditEvent
parameter_list|(
name|DavServerRequest
name|request
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|triggerAuditEvent
argument_list|(
name|archivaUser
operator|.
name|getActivePrincipal
argument_list|()
argument_list|,
name|getRemoteIP
argument_list|(
name|request
argument_list|)
argument_list|,
name|resource
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getRemoteIP
parameter_list|(
name|DavServerRequest
name|request
parameter_list|)
block|{
return|return
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getRemoteAddr
argument_list|()
return|;
block|}
specifier|public
name|void
name|addAuditListener
parameter_list|(
name|AuditListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearAuditListeners
parameter_list|()
block|{
name|this
operator|.
name|auditListeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|removeAuditListener
parameter_list|(
name|AuditListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

