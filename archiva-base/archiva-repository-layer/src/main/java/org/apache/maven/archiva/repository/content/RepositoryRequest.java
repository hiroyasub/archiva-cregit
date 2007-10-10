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
name|repository
operator|.
name|content
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
name|FileTypes
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
name|layout
operator|.
name|LayoutException
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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryListener
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
name|SelectorUtils
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
comment|/**  * RepositoryRequest is used to determine the type of request that is incoming, and convert it to an appropriate  * ArtifactReference.    *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component   *      role="org.apache.maven.archiva.repository.content.RepositoryRequest"  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryRequest
implements|implements
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|artifactPatterns
decl_stmt|;
comment|/**      * Test path to see if it is an artifact being requested (or not).      *       * @param requestedPath the path to test.      * @return true if it is an artifact being requested.      */
specifier|public
name|boolean
name|isArtifact
parameter_list|(
name|String
name|requestedPath
parameter_list|)
block|{
comment|// Correct the slash pattern.
name|String
name|relativePath
init|=
name|requestedPath
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|this
operator|.
name|artifactPatterns
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|pattern
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|SelectorUtils
operator|.
name|matchPath
argument_list|(
name|pattern
argument_list|,
name|relativePath
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// Found match
return|return
literal|true
return|;
block|}
block|}
comment|// No match.
return|return
literal|false
return|;
block|}
comment|/**      * Takes an incoming requested path (in "/" format) and gleans the layout      * and ArtifactReference appropriate for that content.      *       * @param requestedPath the relative path to the content.      * @return the ArtifactReference for the requestedPath.      * @throws LayoutException if the request path is not layout valid.       */
specifier|public
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|requestedPath
parameter_list|)
throws|throws
name|LayoutException
block|{
name|String
name|pathParts
index|[]
init|=
name|StringUtils
operator|.
name|splitPreserveAllTokens
argument_list|(
name|requestedPath
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pathParts
operator|.
name|length
operator|>
literal|3
condition|)
block|{
return|return
name|DefaultPathParser
operator|.
name|toArtifactReference
argument_list|(
name|requestedPath
argument_list|)
return|;
block|}
if|else if
condition|(
name|pathParts
operator|.
name|length
operator|==
literal|3
condition|)
block|{
return|return
name|LegacyPathParser
operator|.
name|toArtifactReference
argument_list|(
name|requestedPath
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Not a valid request path layout, too short."
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
name|this
operator|.
name|artifactPatterns
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|initVariables
argument_list|()
expr_stmt|;
name|this
operator|.
name|archivaConfiguration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initVariables
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
operator|.
name|artifactPatterns
init|)
block|{
name|this
operator|.
name|artifactPatterns
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|artifactPatterns
operator|.
name|addAll
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|propertyName
operator|.
name|contains
argument_list|(
literal|"fileType"
argument_list|)
condition|)
block|{
name|initVariables
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* nothing to do */
block|}
block|}
end_class

end_unit

