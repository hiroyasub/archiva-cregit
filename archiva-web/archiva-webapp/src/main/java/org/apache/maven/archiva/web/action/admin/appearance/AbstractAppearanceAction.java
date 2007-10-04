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
name|action
operator|.
name|admin
operator|.
name|appearance
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
name|codehaus
operator|.
name|plexus
operator|.
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
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
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * AbstractAppearanceAction   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAppearanceAction
extends|extends
name|PlexusActionSupport
block|{
comment|/**      * @plexus.requirement role="org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout"      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ArtifactRepositoryLayout
argument_list|>
name|repositoryLayouts
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactRepositoryFactory
name|repoFactory
decl_stmt|;
specifier|protected
name|ArtifactRepository
name|createLocalRepository
parameter_list|()
block|{
name|String
name|id
init|=
literal|"archiva-local-repo"
decl_stmt|;
name|String
name|layout
init|=
literal|"default"
decl_stmt|;
name|String
name|directory
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.m2/archiva"
decl_stmt|;
name|ArtifactRepositoryLayout
name|repositoryLayout
init|=
operator|(
name|ArtifactRepositoryLayout
operator|)
name|repositoryLayouts
operator|.
name|get
argument_list|(
name|layout
argument_list|)
decl_stmt|;
name|File
name|repository
init|=
operator|new
name|File
argument_list|(
name|directory
argument_list|)
decl_stmt|;
name|repository
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|String
name|repoDir
init|=
name|repository
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//workaround for spaces non converted by PathUtils in wagon
comment|//TODO: remove it when PathUtils will be fixed
if|if
condition|(
name|repoDir
operator|.
name|indexOf
argument_list|(
literal|"%20"
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|repoDir
operator|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|repoDir
argument_list|,
literal|"%20"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
block|}
return|return
name|repoFactory
operator|.
name|createArtifactRepository
argument_list|(
name|id
argument_list|,
name|repoDir
argument_list|,
name|repositoryLayout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

