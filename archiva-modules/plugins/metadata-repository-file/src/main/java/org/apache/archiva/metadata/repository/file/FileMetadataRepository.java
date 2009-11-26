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
name|file
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|FileInputStream
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
name|FileOutputStream
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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|model
operator|.
name|CiManagement
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
name|IssueManagement
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
name|License
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
name|model
operator|.
name|Organization
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
name|ProjectMetadata
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
name|ProjectVersionFacet
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
name|ProjectVersionMetadata
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
name|Scm
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
name|commons
operator|.
name|io
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * @plexus.component role="org.apache.archiva.metadata.repository.MetadataRepository"  */
end_comment

begin_class
specifier|public
class|class
name|FileMetadataRepository
implements|implements
name|MetadataRepository
block|{
comment|/**      * TODO: this isn't suitable for production use      *      * @plexus.configuration      */
specifier|private
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
argument_list|,
literal|".archiva-metadata"
argument_list|)
decl_stmt|;
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
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FileMetadataRepository
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|updateProject
parameter_list|(
name|String
name|repoId
parameter_list|,
name|ProjectMetadata
name|project
parameter_list|)
block|{
comment|// TODO: this is a more braindead implementation than we would normally expect, for prototyping purposes
try|try
block|{
name|File
name|projectDirectory
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|directory
argument_list|,
name|repoId
operator|+
literal|"/"
operator|+
name|project
operator|.
name|getNamespace
argument_list|()
operator|+
literal|"/"
operator|+
name|project
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"namespace"
argument_list|,
name|project
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"id"
argument_list|,
name|project
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|writeProperties
argument_list|(
name|properties
argument_list|,
name|projectDirectory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO!
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|updateProjectVersion
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|ProjectVersionMetadata
name|versionMetadata
parameter_list|)
block|{
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|directory
argument_list|,
name|repoId
operator|+
literal|"/"
operator|+
name|namespace
operator|+
literal|"/"
operator|+
name|projectId
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"id"
argument_list|,
name|versionMetadata
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"name"
argument_list|,
name|versionMetadata
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"description"
argument_list|,
name|versionMetadata
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"url"
argument_list|,
name|versionMetadata
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|versionMetadata
operator|.
name|getScm
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"scm.connection"
argument_list|,
name|versionMetadata
operator|.
name|getScm
argument_list|()
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"scm.developerConnection"
argument_list|,
name|versionMetadata
operator|.
name|getScm
argument_list|()
operator|.
name|getDeveloperConnection
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"scm.url"
argument_list|,
name|versionMetadata
operator|.
name|getScm
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|versionMetadata
operator|.
name|getCiManagement
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"ci.system"
argument_list|,
name|versionMetadata
operator|.
name|getCiManagement
argument_list|()
operator|.
name|getSystem
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"ci.url"
argument_list|,
name|versionMetadata
operator|.
name|getCiManagement
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|versionMetadata
operator|.
name|getIssueManagement
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"issue.system"
argument_list|,
name|versionMetadata
operator|.
name|getIssueManagement
argument_list|()
operator|.
name|getSystem
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"issue.url"
argument_list|,
name|versionMetadata
operator|.
name|getIssueManagement
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|versionMetadata
operator|.
name|getOrganization
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"org.name"
argument_list|,
name|versionMetadata
operator|.
name|getOrganization
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"org.url"
argument_list|,
name|versionMetadata
operator|.
name|getOrganization
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|License
name|license
range|:
name|versionMetadata
operator|.
name|getLicenses
argument_list|()
control|)
block|{
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"license."
operator|+
name|i
operator|+
literal|".name"
argument_list|,
name|license
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
literal|"license."
operator|+
name|i
operator|+
literal|".url"
argument_list|,
name|license
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|properties
operator|.
name|setProperty
argument_list|(
literal|"facetIds"
argument_list|,
name|join
argument_list|(
name|versionMetadata
operator|.
name|getAllFacetIds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ProjectVersionFacet
name|facet
range|:
name|versionMetadata
operator|.
name|getAllFacets
argument_list|()
control|)
block|{
name|properties
operator|.
name|putAll
argument_list|(
name|facet
operator|.
name|toProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|writeProperties
argument_list|(
name|properties
argument_list|,
operator|new
name|File
argument_list|(
name|directory
argument_list|,
name|versionMetadata
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|//To change body of catch statement use File | Settings | File Templates.
block|}
block|}
specifier|private
name|String
name|join
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
name|StringBuilder
name|s
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|ids
control|)
block|{
name|s
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
return|return
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
specifier|private
name|void
name|setProperty
parameter_list|(
name|Properties
name|properties
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|updateArtifact
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|ArtifactMetadata
name|artifact
parameter_list|)
block|{
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|directory
argument_list|,
name|repoId
operator|+
literal|"/"
operator|+
name|namespace
operator|+
literal|"/"
operator|+
name|projectId
operator|+
literal|"/"
operator|+
name|projectVersion
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
name|readProperties
argument_list|(
name|directory
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"updated:"
operator|+
name|artifact
operator|.
name|getId
argument_list|()
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|artifact
operator|.
name|getUpdated
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"size:"
operator|+
name|artifact
operator|.
name|getId
argument_list|()
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|artifact
operator|.
name|getSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"version:"
operator|+
name|artifact
operator|.
name|getId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|writeProperties
argument_list|(
name|properties
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|//To change body of catch statement use File | Settings | File Templates.
block|}
block|}
specifier|private
name|Properties
name|readProperties
parameter_list|(
name|File
name|directory
parameter_list|)
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|FileInputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|directory
argument_list|,
literal|"metadata.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
comment|// skip - use blank properties
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|//To change body of catch statement use File | Settings | File Templates.
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
specifier|public
name|ProjectMetadata
name|getProject
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|directory
argument_list|,
name|repoId
operator|+
literal|"/"
operator|+
name|namespace
operator|+
literal|"/"
operator|+
name|projectId
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
name|readProperties
argument_list|(
name|directory
argument_list|)
decl_stmt|;
name|ProjectMetadata
name|project
init|=
operator|new
name|ProjectMetadata
argument_list|()
decl_stmt|;
name|project
operator|.
name|setNamespace
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"namespace"
argument_list|)
argument_list|)
expr_stmt|;
name|project
operator|.
name|setId
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|project
return|;
block|}
specifier|public
name|ProjectVersionMetadata
name|getProjectVersion
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|directory
argument_list|,
name|repoId
operator|+
literal|"/"
operator|+
name|namespace
operator|+
literal|"/"
operator|+
name|projectId
operator|+
literal|"/"
operator|+
name|projectVersion
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
name|readProperties
argument_list|(
name|directory
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|ProjectVersionMetadata
name|versionMetadata
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|versionMetadata
operator|=
operator|new
name|ProjectVersionMetadata
argument_list|()
expr_stmt|;
name|versionMetadata
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setName
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setDescription
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setUrl
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"url"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|scmConnection
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"scm.connection"
argument_list|)
decl_stmt|;
name|String
name|scmDeveloperConnection
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"scm.developerConnection"
argument_list|)
decl_stmt|;
name|String
name|scmUrl
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"scm.url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scmConnection
operator|!=
literal|null
operator|||
name|scmDeveloperConnection
operator|!=
literal|null
operator|||
name|scmUrl
operator|!=
literal|null
condition|)
block|{
name|Scm
name|scm
init|=
operator|new
name|Scm
argument_list|()
decl_stmt|;
name|scm
operator|.
name|setConnection
argument_list|(
name|scmConnection
argument_list|)
expr_stmt|;
name|scm
operator|.
name|setDeveloperConnection
argument_list|(
name|scmDeveloperConnection
argument_list|)
expr_stmt|;
name|scm
operator|.
name|setUrl
argument_list|(
name|scmUrl
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setScm
argument_list|(
name|scm
argument_list|)
expr_stmt|;
block|}
name|String
name|ciSystem
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"ci.system"
argument_list|)
decl_stmt|;
name|String
name|ciUrl
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"ci.url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ciSystem
operator|!=
literal|null
operator|||
name|ciUrl
operator|!=
literal|null
condition|)
block|{
name|CiManagement
name|ci
init|=
operator|new
name|CiManagement
argument_list|()
decl_stmt|;
name|ci
operator|.
name|setSystem
argument_list|(
name|ciSystem
argument_list|)
expr_stmt|;
name|ci
operator|.
name|setUrl
argument_list|(
name|ciUrl
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setCiManagement
argument_list|(
name|ci
argument_list|)
expr_stmt|;
block|}
name|String
name|issueSystem
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"issue.system"
argument_list|)
decl_stmt|;
name|String
name|issueUrl
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"issue.url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|issueSystem
operator|!=
literal|null
operator|||
name|issueUrl
operator|!=
literal|null
condition|)
block|{
name|IssueManagement
name|issueManagement
init|=
operator|new
name|IssueManagement
argument_list|()
decl_stmt|;
name|issueManagement
operator|.
name|setSystem
argument_list|(
name|ciSystem
argument_list|)
expr_stmt|;
name|issueManagement
operator|.
name|setUrl
argument_list|(
name|ciUrl
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setIssueManagement
argument_list|(
name|issueManagement
argument_list|)
expr_stmt|;
block|}
name|String
name|orgName
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"org.name"
argument_list|)
decl_stmt|;
name|String
name|orgUrl
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"org.url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|orgName
operator|!=
literal|null
operator|||
name|orgUrl
operator|!=
literal|null
condition|)
block|{
name|Organization
name|org
init|=
operator|new
name|Organization
argument_list|()
decl_stmt|;
name|org
operator|.
name|setName
argument_list|(
name|orgName
argument_list|)
expr_stmt|;
name|org
operator|.
name|setUrl
argument_list|(
name|orgUrl
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|setOrganization
argument_list|(
name|org
argument_list|)
expr_stmt|;
block|}
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|String
name|licenseName
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"license."
operator|+
name|i
operator|+
literal|".name"
argument_list|)
decl_stmt|;
name|String
name|licenseUrl
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"license."
operator|+
name|i
operator|+
literal|".url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|licenseName
operator|!=
literal|null
operator|||
name|licenseUrl
operator|!=
literal|null
condition|)
block|{
name|License
name|license
init|=
operator|new
name|License
argument_list|()
decl_stmt|;
name|license
operator|.
name|setName
argument_list|(
name|licenseName
argument_list|)
expr_stmt|;
name|license
operator|.
name|setUrl
argument_list|(
name|licenseUrl
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|addLicense
argument_list|(
name|license
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|done
operator|=
literal|true
expr_stmt|;
block|}
name|i
operator|++
expr_stmt|;
block|}
for|for
control|(
name|String
name|facetId
range|:
name|properties
operator|.
name|getProperty
argument_list|(
literal|"facetIds"
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|MetadataFacetFactory
name|factory
init|=
name|metadataFacetFactories
operator|.
name|get
argument_list|(
name|facetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Attempted to load unknown metadata facet: "
operator|+
name|facetId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ProjectVersionFacet
name|facet
init|=
name|factory
operator|.
name|createProjectVersionFacet
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
for|for
control|(
name|String
name|key
range|:
name|properties
operator|.
name|stringPropertyNames
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|facet
operator|.
name|getFacetId
argument_list|()
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|properties
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|facet
operator|.
name|fromProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|versionMetadata
operator|.
name|addFacet
argument_list|(
name|facet
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ProjectVersionFacet
name|facet
range|:
name|versionMetadata
operator|.
name|getAllFacets
argument_list|()
control|)
block|{
name|properties
operator|.
name|putAll
argument_list|(
name|facet
operator|.
name|toProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|versionMetadata
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getArtifactVersions
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|directory
argument_list|,
name|repoId
operator|+
literal|"/"
operator|+
name|namespace
operator|+
literal|"/"
operator|+
name|projectId
operator|+
literal|"/"
operator|+
name|projectVersion
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
name|readProperties
argument_list|(
name|directory
argument_list|)
decl_stmt|;
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
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"version:"
argument_list|)
condition|)
block|{
name|versions
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|versions
return|;
block|}
specifier|private
name|void
name|writeProperties
parameter_list|(
name|Properties
name|properties
parameter_list|,
name|File
name|directory
parameter_list|)
throws|throws
name|IOException
block|{
name|directory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileOutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|directory
argument_list|,
literal|"metadata.properties"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|store
argument_list|(
name|os
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

