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
name|layout
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
name|AbstractRepositoryConfiguration
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
name|ConfigurationNames
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
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
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
name|Iterator
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
comment|/**  * BidirectionalRepositoryLayoutFactory  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.repository.layout.BidirectionalRepositoryLayoutFactory"  */
end_comment

begin_class
specifier|public
class|class
name|BidirectionalRepositoryLayoutFactory
extends|extends
name|AbstractLogEnabled
implements|implements
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.repository.layout.BidirectionalRepositoryLayout"      */
specifier|private
name|Map
name|layouts
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|Map
name|repositoryMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|BidirectionalRepositoryLayout
name|getLayout
parameter_list|(
name|String
name|type
parameter_list|)
throws|throws
name|LayoutException
block|{
if|if
condition|(
operator|!
name|layouts
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Layout type ["
operator|+
name|type
operator|+
literal|"] does not exist.  "
operator|+
literal|"Available types ["
operator|+
name|layouts
operator|.
name|keySet
argument_list|()
operator|+
literal|"]"
argument_list|)
throw|;
block|}
return|return
operator|(
name|BidirectionalRepositoryLayout
operator|)
name|layouts
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|BidirectionalRepositoryLayout
name|getLayoutForPath
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|layouts
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|BidirectionalRepositoryLayout
name|layout
init|=
operator|(
name|BidirectionalRepositoryLayout
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|layout
operator|.
name|isValidPath
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|layout
return|;
block|}
block|}
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"No valid layout was found for path ["
operator|+
name|path
operator|+
literal|"]"
argument_list|)
throw|;
block|}
specifier|public
name|BidirectionalRepositoryLayout
name|getLayout
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|LayoutException
block|{
if|if
condition|(
name|artifact
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Cannot determine layout using a null artifact."
argument_list|)
throw|;
block|}
name|String
name|repoId
init|=
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Cannot determine layout using artifact with no repository id: "
operator|+
name|artifact
argument_list|)
throw|;
block|}
name|AbstractRepositoryConfiguration
name|repo
init|=
operator|(
name|AbstractRepositoryConfiguration
operator|)
name|this
operator|.
name|repositoryMap
operator|.
name|get
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
return|return
name|getLayout
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
return|;
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
name|ConfigurationNames
operator|.
name|isManagedRepositories
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initRepositoryMap
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
comment|/* do nothing */
block|}
specifier|private
name|void
name|initRepositoryMap
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
operator|.
name|repositoryMap
init|)
block|{
name|this
operator|.
name|repositoryMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|repositoryMap
operator|.
name|putAll
argument_list|(
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|initRepositoryMap
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

