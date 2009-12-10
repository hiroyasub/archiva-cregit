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
name|tags
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DecimalFormat
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
name|Collection
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
name|List
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|PageContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|ActionContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|util
operator|.
name|ValueStack
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
name|model
operator|.
name|ArtifactMetadata
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
name|commons
operator|.
name|lang
operator|.
name|StringEscapeUtils
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
name|lang
operator|.
name|StringUtils
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
name|security
operator|.
name|ArchivaSecurityException
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
name|ArchivaXworkUser
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
name|UserRepositories
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|StrutsException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|components
operator|.
name|Component
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
name|component
operator|.
name|repository
operator|.
name|exception
operator|.
name|ComponentLookupException
import|;
end_import

begin_class
specifier|public
class|class
name|DownloadArtifact
extends|extends
name|Component
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_DOWNLOAD_IMAGE
init|=
literal|"download-type-other.png"
decl_stmt|;
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
decl_stmt|;
specifier|private
name|MetadataResolver
name|metadataResolver
decl_stmt|;
specifier|private
name|HttpServletRequest
name|req
decl_stmt|;
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|boolean
name|mini
init|=
literal|false
decl_stmt|;
specifier|private
name|DecimalFormat
name|decimalFormat
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|DOWNLOAD_IMAGES
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|UserRepositories
name|userRepositories
decl_stmt|;
static|static
block|{
name|DOWNLOAD_IMAGES
operator|.
name|put
argument_list|(
literal|"jar"
argument_list|,
literal|"download-type-jar.png"
argument_list|)
expr_stmt|;
name|DOWNLOAD_IMAGES
operator|.
name|put
argument_list|(
literal|"java-source"
argument_list|,
literal|"download-type-jar.png"
argument_list|)
expr_stmt|;
name|DOWNLOAD_IMAGES
operator|.
name|put
argument_list|(
literal|"pom"
argument_list|,
literal|"download-type-pom.png"
argument_list|)
expr_stmt|;
name|DOWNLOAD_IMAGES
operator|.
name|put
argument_list|(
literal|"maven-plugin"
argument_list|,
literal|"download-type-maven-plugin.png"
argument_list|)
expr_stmt|;
name|DOWNLOAD_IMAGES
operator|.
name|put
argument_list|(
literal|"maven-archetype"
argument_list|,
literal|"download-type-archetype.png"
argument_list|)
expr_stmt|;
name|DOWNLOAD_IMAGES
operator|.
name|put
argument_list|(
literal|"maven-skin"
argument_list|,
literal|"download-type-skin.png"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DownloadArtifact
parameter_list|(
name|ValueStack
name|stack
parameter_list|,
name|PageContext
name|pageContext
parameter_list|)
block|{
name|super
argument_list|(
name|stack
argument_list|)
expr_stmt|;
name|decimalFormat
operator|=
operator|new
name|DecimalFormat
argument_list|(
literal|"#,#00"
argument_list|)
expr_stmt|;
name|this
operator|.
name|req
operator|=
operator|(
name|HttpServletRequest
operator|)
name|pageContext
operator|.
name|getRequest
argument_list|()
expr_stmt|;
try|try
block|{
name|metadataResolver
operator|=
operator|(
name|MetadataResolver
operator|)
name|PlexusTagUtil
operator|.
name|lookup
argument_list|(
name|pageContext
argument_list|,
name|MetadataResolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|repositoryFactory
operator|=
operator|(
name|RepositoryContentFactory
operator|)
name|PlexusTagUtil
operator|.
name|lookup
argument_list|(
name|pageContext
argument_list|,
name|RepositoryContentFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|userRepositories
operator|=
operator|(
name|UserRepositories
operator|)
name|PlexusTagUtil
operator|.
name|lookup
argument_list|(
name|pageContext
argument_list|,
name|UserRepositories
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ComponentLookupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
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
annotation|@
name|Override
specifier|public
name|boolean
name|end
parameter_list|(
name|Writer
name|writer
parameter_list|,
name|String
name|body
parameter_list|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|getObservableRepos
argument_list|()
control|)
block|{
name|artifacts
operator|.
name|addAll
argument_list|(
name|metadataResolver
operator|.
name|getArtifacts
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|artifacts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|prefix
init|=
name|req
operator|.
name|getContextPath
argument_list|()
operator|+
literal|"/repository/"
decl_stmt|;
if|if
condition|(
name|mini
condition|)
block|{
comment|// TODO: write 1 line download link for main artifact.
block|}
else|else
block|{
name|appendNormal
argument_list|(
name|sb
argument_list|,
name|prefix
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|writer
operator|.
name|write
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StrutsException
argument_list|(
literal|"IOError: "
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
return|return
name|super
operator|.
name|end
argument_list|(
name|writer
argument_list|,
name|body
argument_list|)
return|;
block|}
specifier|private
name|void
name|appendNormal
parameter_list|(
name|StringBuffer
name|sb
parameter_list|,
name|String
name|prefix
parameter_list|,
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|relatedArtifacts
parameter_list|)
throws|throws
name|RepositoryException
block|{
comment|/*          *<div class="download">          *<div class="hd">           *<div class="c"></div>          *</div>          *<div class="bd">          *<div class="c">          *<-- main content goes here -->          *</div>          *</div>          *<div class="ft">          *<div class="c"></div>          *</div>          *</div>          */
name|sb
operator|.
name|append
argument_list|(
literal|"<div class=\"download\">"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<div class=\"hd\"><div class=\"c\"></div></div>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<div class=\"bd\"><div class=\"c\">"
argument_list|)
expr_stmt|;
comment|// Heading
name|sb
operator|.
name|append
argument_list|(
literal|"<h2>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|relatedArtifacts
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"Downloads"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"Download"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</h2>"
argument_list|)
expr_stmt|;
comment|// Body
name|sb
operator|.
name|append
argument_list|(
literal|"<p class=\"body\">"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
argument_list|)
expr_stmt|;
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|relatedArtifacts
control|)
block|{
name|String
name|repoId
init|=
name|artifact
operator|.
name|getRepositoryId
argument_list|()
decl_stmt|;
name|ManagedRepositoryContent
name|repo
init|=
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n<tr>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<td class=\"icon\">"
argument_list|)
expr_stmt|;
name|appendImageLink
argument_list|(
name|sb
argument_list|,
name|prefix
operator|+
name|repoId
argument_list|,
name|repo
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<td class=\"type\">"
argument_list|)
expr_stmt|;
name|appendLink
argument_list|(
name|sb
argument_list|,
name|prefix
operator|+
name|repoId
argument_list|,
name|repo
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<td class=\"size\">"
argument_list|)
expr_stmt|;
name|appendFilesize
argument_list|(
name|sb
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</tr>"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</table>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</p>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</div>"
argument_list|)
expr_stmt|;
comment|// close "downloadbox.bd.c"
name|sb
operator|.
name|append
argument_list|(
literal|"</div>"
argument_list|)
expr_stmt|;
comment|// close "downloadbox.bd"
name|sb
operator|.
name|append
argument_list|(
literal|"<div class=\"ft\"><div class=\"c\"></div></div>"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</div>"
argument_list|)
expr_stmt|;
comment|// close "download"
block|}
specifier|private
name|void
name|appendImageLink
parameter_list|(
name|StringBuffer
name|sb
parameter_list|,
name|String
name|prefix
parameter_list|,
name|ManagedRepositoryContent
name|repo
parameter_list|,
name|ArtifactMetadata
name|artifact
parameter_list|)
block|{
name|String
name|path
init|=
name|getPath
argument_list|(
name|repo
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|getType
argument_list|(
name|repo
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|linkText
init|=
literal|"<img src=\""
operator|+
name|req
operator|.
name|getContextPath
argument_list|()
operator|+
literal|"/images/"
operator|+
name|getDownloadImage
argument_list|(
name|type
argument_list|)
operator|+
literal|"\" />"
decl_stmt|;
name|appendLink
argument_list|(
name|sb
argument_list|,
name|prefix
argument_list|,
name|artifact
argument_list|,
name|linkText
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getType
parameter_list|(
name|ManagedRepositoryContent
name|repo
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|String
name|type
init|=
literal|null
decl_stmt|;
try|try
block|{
name|type
operator|=
name|repo
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|//TODO
block|}
return|return
name|type
return|;
block|}
specifier|private
name|String
name|getDownloadImage
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|String
name|name
init|=
name|DOWNLOAD_IMAGES
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
name|name
operator|!=
literal|null
condition|?
name|name
else|:
name|DEFAULT_DOWNLOAD_IMAGE
return|;
block|}
specifier|private
specifier|static
name|void
name|appendLink
parameter_list|(
name|StringBuffer
name|sb
parameter_list|,
name|String
name|prefix
parameter_list|,
name|ArtifactMetadata
name|artifact
parameter_list|,
name|String
name|linkText
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|StringBuffer
name|url
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|url
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<a href=\""
argument_list|)
operator|.
name|append
argument_list|(
name|StringEscapeUtils
operator|.
name|escapeXml
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" title=\""
argument_list|)
operator|.
name|append
argument_list|(
literal|"Download "
argument_list|)
operator|.
name|append
argument_list|(
name|StringEscapeUtils
operator|.
name|escapeXml
argument_list|(
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|linkText
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</a>"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getPath
parameter_list|(
name|ManagedRepositoryContent
name|repo
parameter_list|,
name|ArtifactMetadata
name|artifact
parameter_list|)
block|{
comment|// TODO: use metadata resolver capability instead
name|ArtifactReference
name|ref
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|repo
operator|.
name|toPath
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
operator|+
literal|1
argument_list|)
operator|+
name|artifact
operator|.
name|getId
argument_list|()
expr_stmt|;
return|return
name|path
return|;
block|}
specifier|private
name|void
name|appendLink
parameter_list|(
name|StringBuffer
name|sb
parameter_list|,
name|String
name|prefix
parameter_list|,
name|ManagedRepositoryContent
name|repo
parameter_list|,
name|ArtifactMetadata
name|artifact
parameter_list|)
block|{
name|String
name|path
init|=
name|getPath
argument_list|(
name|repo
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|getType
argument_list|(
name|repo
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|String
name|linkText
init|=
name|StringUtils
operator|.
name|capitalize
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|appendLink
argument_list|(
name|sb
argument_list|,
name|prefix
argument_list|,
name|artifact
argument_list|,
name|linkText
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendFilesize
parameter_list|(
name|StringBuffer
name|sb
parameter_list|,
name|ArtifactMetadata
name|artifact
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|decimalFormat
operator|.
name|format
argument_list|(
name|artifact
operator|.
name|getSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
name|void
name|setMini
parameter_list|(
name|boolean
name|mini
parameter_list|)
block|{
name|this
operator|.
name|mini
operator|=
name|mini
expr_stmt|;
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
name|Collection
argument_list|<
name|String
argument_list|>
name|getObservableRepos
parameter_list|()
block|{
try|try
block|{
name|ActionContext
name|context
init|=
name|ActionContext
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|Map
name|session
init|=
name|context
operator|.
name|getSession
argument_list|()
decl_stmt|;
return|return
name|userRepositories
operator|.
name|getObservableRepositoryIds
argument_list|(
name|ArchivaXworkUser
operator|.
name|getActivePrincipal
argument_list|(
name|session
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ArchivaSecurityException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|//TODO
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

