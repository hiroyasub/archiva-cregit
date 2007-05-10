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
name|scanning
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|Preparable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|Validateable
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
name|collections
operator|.
name|CollectionUtils
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
name|FileType
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
name|RepositoryScanningConfiguration
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
name|functors
operator|.
name|FiletypeSelectionPredicate
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
name|functors
operator|.
name|FiletypeToMapClosure
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
name|scanner
operator|.
name|RepositoryContentConsumers
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
name|ArchivaRoleConstants
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
name|redback
operator|.
name|rbac
operator|.
name|Resource
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
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureAction
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
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionBundle
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
name|redback
operator|.
name|xwork
operator|.
name|interceptor
operator|.
name|SecureActionException
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
name|Collections
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

begin_comment
comment|/**  * RepositoryScanningAction   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="com.opensymphony.xwork.Action" role-hint="repositoryScanningAction"  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryScanningAction
extends|extends
name|PlexusActionSupport
implements|implements
name|Preparable
implements|,
name|Validateable
implements|,
name|SecureAction
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentConsumers
name|repoconsumerUtil
decl_stmt|;
specifier|private
name|Map
name|fileTypeMap
decl_stmt|;
specifier|private
name|List
name|fileTypeIds
decl_stmt|;
comment|/**      * List of {@link AdminRepositoryConsumer} objects for consumers of known content.      */
specifier|private
name|List
name|knownContentConsumers
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/**      * List of {@link AdminRepositoryConsumer} objects for consumers of invalid/unknown content.      */
specifier|private
name|List
name|invalidContentConsumers
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|pattern
decl_stmt|;
specifier|private
name|String
name|fileTypeId
decl_stmt|;
specifier|public
name|void
name|addActionError
parameter_list|(
name|String
name|anErrorMessage
parameter_list|)
block|{
name|super
operator|.
name|addActionError
argument_list|(
name|anErrorMessage
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"[ActionError] "
operator|+
name|anErrorMessage
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addActionMessage
parameter_list|(
name|String
name|aMessage
parameter_list|)
block|{
name|super
operator|.
name|addActionMessage
argument_list|(
name|aMessage
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"[ActionMessage] "
operator|+
name|aMessage
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|addFiletypePattern
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Add New File Type Pattern ["
operator|+
name|getFileTypeId
argument_list|()
operator|+
literal|":"
operator|+
name|getPattern
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isValidFiletypeCommand
argument_list|()
condition|)
block|{
return|return
name|INPUT
return|;
block|}
name|String
name|id
init|=
name|getFileTypeId
argument_list|()
decl_stmt|;
name|String
name|pattern
init|=
name|getPattern
argument_list|()
decl_stmt|;
name|FileType
name|filetype
init|=
name|findFileType
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|filetype
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"Pattern not added, unable to find filetype "
operator|+
name|id
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
if|if
condition|(
name|filetype
operator|.
name|getPatterns
argument_list|()
operator|.
name|contains
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Not adding pattern \""
operator|+
name|pattern
operator|+
literal|"\" to filetype "
operator|+
name|id
operator|+
literal|" as it already exists."
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
name|filetype
operator|.
name|addPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Added pattern \""
operator|+
name|pattern
operator|+
literal|"\" to filetype "
operator|+
name|id
argument_list|)
expr_stmt|;
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
specifier|public
name|String
name|getFileTypeId
parameter_list|()
block|{
return|return
name|fileTypeId
return|;
block|}
specifier|public
name|List
name|getFileTypeIds
parameter_list|()
block|{
return|return
name|fileTypeIds
return|;
block|}
specifier|public
name|Map
name|getFileTypeMap
parameter_list|()
block|{
return|return
name|fileTypeMap
return|;
block|}
specifier|public
name|List
name|getInvalidContentConsumers
parameter_list|()
block|{
return|return
name|invalidContentConsumers
return|;
block|}
specifier|public
name|List
name|getKnownContentConsumers
parameter_list|()
block|{
return|return
name|knownContentConsumers
return|;
block|}
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
specifier|public
name|SecureActionBundle
name|getSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|SecureActionBundle
name|bundle
init|=
operator|new
name|SecureActionBundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|setRequiresAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|addRequiredAuthorization
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|,
name|Resource
operator|.
name|GLOBAL
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|RepositoryScanningConfiguration
name|reposcanning
init|=
name|config
operator|.
name|getRepositoryScanning
argument_list|()
decl_stmt|;
name|FiletypeToMapClosure
name|filetypeToMapClosure
init|=
operator|new
name|FiletypeToMapClosure
argument_list|()
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|reposcanning
operator|.
name|getFileTypes
argument_list|()
argument_list|,
name|filetypeToMapClosure
argument_list|)
expr_stmt|;
name|fileTypeMap
operator|=
name|filetypeToMapClosure
operator|.
name|getMap
argument_list|()
expr_stmt|;
name|AddAdminRepoConsumerClosure
name|addAdminRepoConsumer
decl_stmt|;
name|addAdminRepoConsumer
operator|=
operator|new
name|AddAdminRepoConsumerClosure
argument_list|(
name|reposcanning
operator|.
name|getKnownContentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|repoconsumerUtil
operator|.
name|getAvailableKnownConsumers
argument_list|()
argument_list|,
name|addAdminRepoConsumer
argument_list|)
expr_stmt|;
name|knownContentConsumers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|knownContentConsumers
operator|.
name|addAll
argument_list|(
name|addAdminRepoConsumer
operator|.
name|getList
argument_list|()
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|knownContentConsumers
argument_list|,
name|AdminRepositoryConsumerComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|addAdminRepoConsumer
operator|=
operator|new
name|AddAdminRepoConsumerClosure
argument_list|(
name|reposcanning
operator|.
name|getInvalidContentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|repoconsumerUtil
operator|.
name|getAvailableInvalidConsumers
argument_list|()
argument_list|,
name|addAdminRepoConsumer
argument_list|)
expr_stmt|;
name|invalidContentConsumers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|invalidContentConsumers
operator|.
name|addAll
argument_list|(
name|addAdminRepoConsumer
operator|.
name|getList
argument_list|()
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|invalidContentConsumers
argument_list|,
name|AdminRepositoryConsumerComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|fileTypeIds
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|fileTypeIds
operator|.
name|addAll
argument_list|(
name|fileTypeMap
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|fileTypeIds
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|removeFiletypePattern
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Remove File Type Pattern ["
operator|+
name|getFileTypeId
argument_list|()
operator|+
literal|":"
operator|+
name|getPattern
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isValidFiletypeCommand
argument_list|()
condition|)
block|{
return|return
name|INPUT
return|;
block|}
name|FileType
name|filetype
init|=
name|findFileType
argument_list|(
name|getFileTypeId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|filetype
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"Pattern not removed, unable to find filetype "
operator|+
name|getFileTypeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
name|filetype
operator|.
name|removePattern
argument_list|(
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|saveConfiguration
argument_list|()
return|;
block|}
specifier|public
name|void
name|setFileTypeId
parameter_list|(
name|String
name|fileTypeId
parameter_list|)
block|{
name|this
operator|.
name|fileTypeId
operator|=
name|fileTypeId
expr_stmt|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
specifier|public
name|String
name|updateInvalidConsumers
parameter_list|()
block|{
name|addActionMessage
argument_list|(
literal|"Update Invalid Consumers"
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|updateKnownConsumers
parameter_list|()
block|{
name|addActionMessage
argument_list|(
literal|"Update Known Consumers"
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
specifier|private
name|FileType
name|findFileType
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|RepositoryScanningConfiguration
name|scanning
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
decl_stmt|;
return|return
operator|(
name|FileType
operator|)
name|CollectionUtils
operator|.
name|find
argument_list|(
name|scanning
operator|.
name|getFileTypes
argument_list|()
argument_list|,
operator|new
name|FiletypeSelectionPredicate
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isValidFiletypeCommand
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|getFileTypeId
argument_list|()
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to process blank filetype id."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|getPattern
argument_list|()
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to process blank pattern."
argument_list|)
expr_stmt|;
block|}
return|return
operator|!
name|hasActionErrors
argument_list|()
return|;
block|}
specifier|private
name|String
name|saveConfiguration
parameter_list|()
block|{
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Successfully saved configuration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to save configuration: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|INPUT
return|;
block|}
block|}
end_class

end_unit

