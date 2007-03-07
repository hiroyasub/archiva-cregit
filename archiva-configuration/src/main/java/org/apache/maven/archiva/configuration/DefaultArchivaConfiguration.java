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
name|configuration
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|io
operator|.
name|registry
operator|.
name|ConfigurationRegistryReader
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
name|io
operator|.
name|registry
operator|.
name|ConfigurationRegistryWriter
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
name|RegistryException
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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * Implementation of configuration holder that retrieves it from the registry.  *  * @plexus.component  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArchivaConfiguration
implements|implements
name|ArchivaConfiguration
implements|,
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * Plexus registry to read the configuration from.      *      * @plexus.requirement role-hint="commons-configuration"      */
specifier|private
name|Registry
name|registry
decl_stmt|;
comment|/**      * The configuration that has been converted.      */
specifier|private
name|Configuration
name|configuration
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"org.apache.maven.archiva"
decl_stmt|;
specifier|public
specifier|synchronized
name|Configuration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
comment|// TODO: should this be the same as section? make sure unnamed sections still work (eg, sys properties)
name|configuration
operator|=
operator|new
name|ConfigurationRegistryReader
argument_list|()
operator|.
name|read
argument_list|(
name|registry
operator|.
name|getSubset
argument_list|(
name|KEY
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: for commons-configuration 1.3 only
name|configuration
operator|.
name|setIndexPath
argument_list|(
name|removeExpressions
argument_list|(
name|configuration
operator|.
name|getIndexPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setMinimalIndexPath
argument_list|(
name|removeExpressions
argument_list|(
name|configuration
operator|.
name|getMinimalIndexPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setLocalRepository
argument_list|(
name|removeExpressions
argument_list|(
name|configuration
operator|.
name|getLocalRepository
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|configuration
operator|.
name|getRepositories
argument_list|()
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
name|RepositoryConfiguration
name|c
init|=
operator|(
name|RepositoryConfiguration
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|c
operator|.
name|setDirectory
argument_list|(
name|removeExpressions
argument_list|(
name|c
operator|.
name|getDirectory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|configuration
return|;
block|}
specifier|public
name|void
name|save
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RegistryException
block|{
name|Registry
name|section
init|=
name|registry
operator|.
name|getSection
argument_list|(
name|KEY
argument_list|)
decl_stmt|;
operator|new
name|ConfigurationRegistryWriter
argument_list|()
operator|.
name|write
argument_list|(
name|configuration
argument_list|,
name|section
argument_list|)
expr_stmt|;
name|section
operator|.
name|save
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
specifier|public
name|void
name|addChangeListener
parameter_list|(
name|RegistryListener
name|listener
parameter_list|)
block|{
name|Registry
name|section
init|=
name|registry
operator|.
name|getSection
argument_list|(
name|KEY
argument_list|)
decl_stmt|;
name|section
operator|.
name|addChangeListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|registry
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
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
comment|// nothing to do here
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
name|configuration
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|String
name|removeExpressions
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|String
name|value
init|=
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|StringUtils
operator|.
name|replace
argument_list|(
name|directory
argument_list|,
literal|"${appserver.base}"
argument_list|,
name|registry
operator|.
name|getString
argument_list|(
literal|"appserver.base"
argument_list|,
literal|"${appserver.base}"
argument_list|)
argument_list|)
decl_stmt|;
name|value
operator|=
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|StringUtils
operator|.
name|replace
argument_list|(
name|value
argument_list|,
literal|"${appserver.home}"
argument_list|,
name|registry
operator|.
name|getString
argument_list|(
literal|"appserver.home"
argument_list|,
literal|"${appserver.home}"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

