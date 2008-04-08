begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rss
operator|.
name|processor
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
name|Calendar
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
name|Enumeration
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
name|rss
operator|.
name|RssFeedEntry
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
name|rss
operator|.
name|RssFeedGenerator
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
name|ArchivaArtifact
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
comment|/**  * Process new artifacts in the repository and generate RSS feeds.  *   * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version  * @plexus.component role="org.apache.archiva.rss.processor.RssFeedProcessor" role-hint="new-artifacts"  */
end_comment

begin_class
specifier|public
class|class
name|NewArtifactsRssFeedProcessor
implements|implements
name|RssFeedProcessor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NEW_ARTIFACTS_IN_REPO
init|=
literal|"New Artifacts in Repository "
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NEW_VERSIONS_OF_ARTIFACT
init|=
literal|"New Versions of Artifact "
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RssFeedGenerator
name|generator
decl_stmt|;
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|NewArtifactsRssFeedProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The hostname that will be used in the urls for the feed links.      */
specifier|private
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
comment|/**      * The port that will be used in the urls for the feed links.      */
specifier|private
name|String
name|port
init|=
literal|"8080"
decl_stmt|;
comment|/**      * Process the newly discovered artifacts in the repository. Generate feeds for new artifacts in the repository and      * new versions of artifact.      */
specifier|public
name|void
name|process
parameter_list|(
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|data
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Process new artifacts into rss feeds."
argument_list|)
expr_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jetty.host"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|host
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"jetty.host"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jetty.port"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|port
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"jetty.port"
argument_list|)
expr_stmt|;
block|}
name|processNewArtifactsInRepo
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|processNewVersionsOfArtifact
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processNewArtifactsInRepo
parameter_list|(
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|data
parameter_list|)
block|{
name|List
argument_list|<
name|RssFeedEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|RssFeedEntry
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|repoId
init|=
name|getRepoId
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|RssFeedEntry
name|entry
init|=
operator|new
name|RssFeedEntry
argument_list|(
name|NEW_ARTIFACTS_IN_REPO
operator|+
literal|"\'"
operator|+
name|repoId
operator|+
literal|"\'"
operator|+
literal|" as of "
operator|+
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|,
name|getBaseUrl
argument_list|()
operator|+
literal|"/archiva/rss/new_artifacts_"
operator|+
name|repoId
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|String
name|description
init|=
literal|"These are the new artifacts found in repository "
operator|+
literal|"\'"
operator|+
name|repoId
operator|+
literal|"\'"
operator|+
literal|": \n"
decl_stmt|;
for|for
control|(
name|ArchivaArtifact
name|artifact
range|:
name|data
control|)
block|{
name|description
operator|=
name|description
operator|+
name|artifact
operator|.
name|toString
argument_list|()
operator|+
literal|" | "
expr_stmt|;
block|}
name|entry
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|generateFeed
argument_list|(
literal|"new_artifacts_"
operator|+
name|repoId
operator|+
literal|".xml"
argument_list|,
name|NEW_ARTIFACTS_IN_REPO
operator|+
literal|"\'"
operator|+
name|repoId
operator|+
literal|"\'"
argument_list|,
name|getBaseUrl
argument_list|()
operator|+
literal|"/archiva/repository/rss/new_artifacts_"
operator|+
name|repoId
operator|+
literal|".xml"
argument_list|,
literal|"New artifacts found in repository "
operator|+
literal|"\'"
operator|+
name|repoId
operator|+
literal|"\'"
operator|+
literal|" during repository scan."
argument_list|,
name|entries
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processNewVersionsOfArtifact
parameter_list|(
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|data
parameter_list|)
block|{
name|String
name|repoId
init|=
name|getRepoId
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|artifacts
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
name|ArchivaArtifact
name|artifact
range|:
name|data
control|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|artifactsMap
init|=
name|toMap
argument_list|(
name|artifacts
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|artifactsMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|RssFeedEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|RssFeedEntry
argument_list|>
argument_list|()
decl_stmt|;
name|RssFeedEntry
name|entry
init|=
operator|new
name|RssFeedEntry
argument_list|(
name|NEW_VERSIONS_OF_ARTIFACT
operator|+
literal|"\'"
operator|+
name|key
operator|+
literal|"\'"
operator|+
literal|" as of "
operator|+
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|,
name|getBaseUrl
argument_list|()
operator|+
literal|"/archiva/rss/new_versions_"
operator|+
name|key
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|String
name|description
init|=
literal|"These are the new versions of artifact "
operator|+
literal|"\'"
operator|+
name|key
operator|+
literal|"\'"
operator|+
literal|" in the repository: \n"
operator|+
operator|(
operator|(
name|String
operator|)
name|artifactsMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|)
decl_stmt|;
name|entry
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|generateFeed
argument_list|(
literal|"new_versions_"
operator|+
name|key
operator|+
literal|".xml"
argument_list|,
name|NEW_VERSIONS_OF_ARTIFACT
operator|+
literal|"\'"
operator|+
name|key
operator|+
literal|"\'"
argument_list|,
name|getBaseUrl
argument_list|()
operator|+
literal|"/archiva/rss/new_versions_"
operator|+
name|key
operator|+
literal|".xml"
argument_list|,
literal|"New versions of artifact "
operator|+
literal|"\'"
operator|+
name|key
operator|+
literal|"\' found in repository "
operator|+
literal|"\'"
operator|+
name|repoId
operator|+
literal|"\'"
operator|+
literal|" during repository scan."
argument_list|,
name|entries
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getRepoId
parameter_list|(
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|data
parameter_list|)
block|{
name|String
name|repoId
init|=
literal|""
decl_stmt|;
if|if
condition|(
operator|!
name|data
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|repoId
operator|=
operator|(
operator|(
name|ArchivaArtifact
operator|)
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
expr_stmt|;
block|}
return|return
name|repoId
return|;
block|}
specifier|private
name|void
name|generateFeed
parameter_list|(
name|String
name|filename
parameter_list|,
name|String
name|title
parameter_list|,
name|String
name|link
parameter_list|,
name|String
name|description
parameter_list|,
name|List
argument_list|<
name|RssFeedEntry
argument_list|>
name|dataEntries
parameter_list|)
block|{
name|generator
operator|.
name|generateFeed
argument_list|(
name|title
argument_list|,
name|link
argument_list|,
name|description
argument_list|,
name|dataEntries
argument_list|,
name|filename
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toMap
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|artifacts
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|artifactsMap
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
name|id
range|:
name|artifacts
control|)
block|{
name|String
name|key
init|=
name|StringUtils
operator|.
name|substringBefore
argument_list|(
name|id
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|key
operator|=
name|key
operator|+
literal|":"
operator|+
name|StringUtils
operator|.
name|substringBefore
argument_list|(
name|StringUtils
operator|.
name|substringAfter
argument_list|(
name|id
argument_list|,
literal|":"
argument_list|)
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|artifactsMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|value
operator|+
literal|" | "
operator|+
name|id
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|id
expr_stmt|;
block|}
name|artifactsMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|artifactsMap
return|;
block|}
specifier|public
name|RssFeedGenerator
name|getGenerator
parameter_list|()
block|{
return|return
name|generator
return|;
block|}
specifier|public
name|void
name|setGenerator
parameter_list|(
name|RssFeedGenerator
name|generator
parameter_list|)
block|{
name|this
operator|.
name|generator
operator|=
name|generator
expr_stmt|;
block|}
specifier|private
name|String
name|getBaseUrl
parameter_list|()
block|{
name|String
name|baseUrl
init|=
literal|"http://"
operator|+
name|host
decl_stmt|;
if|if
condition|(
name|port
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|port
argument_list|)
condition|)
block|{
name|baseUrl
operator|=
name|baseUrl
operator|+
literal|":"
operator|+
name|port
expr_stmt|;
block|}
return|return
name|baseUrl
return|;
block|}
block|}
end_class

end_unit

