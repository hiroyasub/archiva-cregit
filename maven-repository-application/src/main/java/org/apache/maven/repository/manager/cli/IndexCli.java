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
name|cli
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepositoryFactory
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
name|artifact
operator|.
name|repository
operator|.
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|repository
operator|.
name|discovery
operator|.
name|ArtifactDiscoverer
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
name|repository
operator|.
name|indexing
operator|.
name|ArtifactRepositoryIndex
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndexException
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndexingFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|classworlds
operator|.
name|ClassWorld
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
name|PlexusContainerException
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

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|embed
operator|.
name|Embedder
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
name|net
operator|.
name|MalformedURLException
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
comment|/**  * Entry point for indexing CLI.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|IndexCli
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|PlexusContainerException
throws|,
name|ComponentLookupException
throws|,
name|RepositoryIndexException
throws|,
name|MalformedURLException
block|{
name|Embedder
name|embedder
init|=
operator|new
name|Embedder
argument_list|()
decl_stmt|;
name|embedder
operator|.
name|start
argument_list|(
operator|new
name|ClassWorld
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryIndexingFactory
name|indexFactory
init|=
operator|(
name|RepositoryIndexingFactory
operator|)
name|embedder
operator|.
name|lookup
argument_list|(
name|RepositoryIndexingFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|ArtifactRepositoryFactory
name|factory
init|=
operator|(
name|ArtifactRepositoryFactory
operator|)
name|embedder
operator|.
name|lookup
argument_list|(
name|ArtifactRepositoryFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|ArtifactRepositoryLayout
name|layout
init|=
operator|(
name|ArtifactRepositoryLayout
operator|)
name|embedder
operator|.
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|ROLE
argument_list|,
literal|"legacy"
argument_list|)
decl_stmt|;
name|ArtifactRepository
name|repository
init|=
name|factory
operator|.
name|createArtifactRepository
argument_list|(
literal|"repository"
argument_list|,
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|layout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ArtifactDiscoverer
name|discoverer
init|=
operator|(
name|ArtifactDiscoverer
operator|)
name|embedder
operator|.
name|lookup
argument_list|(
name|ArtifactDiscoverer
operator|.
name|ROLE
argument_list|,
literal|"legacy"
argument_list|)
decl_stmt|;
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ArtifactRepositoryIndex
name|index
init|=
name|indexFactory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|,
literal|".index"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|index
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
name|index
operator|.
name|optimize
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|index
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|time
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|time
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Indexed "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|" artifacts in "
operator|+
name|time
operator|+
literal|"ms"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

