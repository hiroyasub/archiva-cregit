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
name|scanner
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
name|File
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
name|Date
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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
operator|.
name|functors
operator|.
name|TriggerScanCompletedClosure
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
name|Closure
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
name|collections
operator|.
name|functors
operator|.
name|IfClosure
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
name|common
operator|.
name|utils
operator|.
name|BaseFile
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
name|ManagedRepositoryConfiguration
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
name|consumers
operator|.
name|InvalidRepositoryContentConsumer
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|functors
operator|.
name|ConsumerProcessFileClosure
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
name|functors
operator|.
name|ConsumerWantsFilePredicate
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
name|functors
operator|.
name|TriggerBeginScanClosure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_comment
comment|/**  * RepositoryContentConsumerUtil   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryContentConsumers
implements|implements
name|ApplicationContextAware
block|{
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|selectedKnownConsumers
decl_stmt|;
specifier|private
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|selectedInvalidConsumers
decl_stmt|;
specifier|public
name|RepositoryContentConsumers
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
block|{
name|this
operator|.
name|archivaConfiguration
operator|=
name|archivaConfiguration
expr_stmt|;
block|}
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
comment|/**      *<p>      * Get the list of Ids associated with those {@link KnownRepositoryContentConsumer} that have      * been selected in the configuration to execute.      *</p>      *       *<p>      * NOTE: This list can be larger and contain entries that might not exist or be available      * in the classpath, or as a component.      *</p>      *       * @return the list of consumer ids that have been selected by the configuration.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSelectedKnownConsumerIds
parameter_list|()
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
name|scanning
operator|.
name|getKnownContentConsumers
argument_list|()
return|;
block|}
comment|/**      *<p>      * Get the list of Ids associated with those {@link InvalidRepositoryContentConsumer} that have      * been selected in the configuration to execute.      *</p>      *       *<p>      * NOTE: This list can be larger and contain entries that might not exist or be available      * in the classpath, or as a component.      *</p>      *       * @return the list of consumer ids that have been selected by the configuration.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSelectedInvalidConsumerIds
parameter_list|()
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
name|scanning
operator|.
name|getInvalidContentConsumers
argument_list|()
return|;
block|}
comment|/**      * Get the map of {@link String} ids to {@link KnownRepositoryContentConsumer} implementations,      * for those consumers that have been selected according to the active configuration.       *       * @return the map of String ids to {@link KnownRepositoryContentConsumer} objects.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|getSelectedKnownConsumersMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|consumerMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|KnownRepositoryContentConsumer
name|consumer
range|:
name|getSelectedKnownConsumers
argument_list|()
control|)
block|{
name|consumerMap
operator|.
name|put
argument_list|(
name|consumer
operator|.
name|getId
argument_list|()
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
return|return
name|consumerMap
return|;
block|}
comment|/**      * Get the map of {@link String} ids to {@link InvalidRepositoryContentConsumer} implementations,      * for those consumers that have been selected according to the active configuration.       *       * @return the map of String ids to {@link InvalidRepositoryContentConsumer} objects.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|InvalidRepositoryContentConsumer
argument_list|>
name|getSelectedInvalidConsumersMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|InvalidRepositoryContentConsumer
argument_list|>
name|consumerMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|InvalidRepositoryContentConsumer
name|consumer
range|:
name|getSelectedInvalidConsumers
argument_list|()
control|)
block|{
name|consumerMap
operator|.
name|put
argument_list|(
name|consumer
operator|.
name|getId
argument_list|()
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
return|return
name|consumerMap
return|;
block|}
comment|/**      * Get the list of {@link KnownRepositoryContentConsumer} objects that are      * selected according to the active configuration.      *       * @return the list of {@link KnownRepositoryContentConsumer} that have been selected      *         by the active configuration.      */
specifier|public
specifier|synchronized
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|getSelectedKnownConsumers
parameter_list|()
block|{
if|if
condition|(
name|selectedKnownConsumers
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|knownSelected
init|=
name|getSelectedKnownConsumerIds
argument_list|()
decl_stmt|;
for|for
control|(
name|KnownRepositoryContentConsumer
name|consumer
range|:
name|getAvailableKnownConsumers
argument_list|()
control|)
block|{
if|if
condition|(
name|knownSelected
operator|.
name|contains
argument_list|(
name|consumer
operator|.
name|getId
argument_list|()
argument_list|)
operator|||
name|consumer
operator|.
name|isPermanent
argument_list|()
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|selectedKnownConsumers
operator|=
name|ret
expr_stmt|;
block|}
return|return
name|selectedKnownConsumers
return|;
block|}
comment|/**      * Get the list of {@link InvalidRepositoryContentConsumer} objects that are      * selected according to the active configuration.      *       * @return the list of {@link InvalidRepositoryContentConsumer} that have been selected      *         by the active configuration.      */
specifier|public
specifier|synchronized
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|getSelectedInvalidConsumers
parameter_list|()
block|{
if|if
condition|(
name|selectedInvalidConsumers
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|invalidSelected
init|=
name|getSelectedInvalidConsumerIds
argument_list|()
decl_stmt|;
for|for
control|(
name|InvalidRepositoryContentConsumer
name|consumer
range|:
name|getAvailableInvalidConsumers
argument_list|()
control|)
block|{
if|if
condition|(
name|invalidSelected
operator|.
name|contains
argument_list|(
name|consumer
operator|.
name|getId
argument_list|()
argument_list|)
operator|||
name|consumer
operator|.
name|isPermanent
argument_list|()
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
name|selectedInvalidConsumers
operator|=
name|ret
expr_stmt|;
block|}
return|return
name|selectedInvalidConsumers
return|;
block|}
comment|/**      * Get the list of {@link KnownRepositoryContentConsumer} objects that are      * available and present in the classpath and as components in the IoC.      *       * @return the list of all available {@link KnownRepositoryContentConsumer} present in the classpath       *         and as a component in the IoC.      */
specifier|public
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|getAvailableKnownConsumers
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|(
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Get the list of {@link InvalidRepositoryContentConsumer} objects that are      * available and present in the classpath and as components in the IoC.      *       * @return the list of all available {@link InvalidRepositoryContentConsumer} present in the classpath       *         and as a component in the IoC.      */
specifier|public
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|getAvailableInvalidConsumers
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|(
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|InvalidRepositoryContentConsumer
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * A convienence method to execute all of the active selected consumers for a       * particular arbitrary file.      *       * @param repository the repository configuration to use.      * @param localFile the local file to execute the consumers against.      */
specifier|public
name|void
name|executeConsumers
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|,
name|File
name|localFile
parameter_list|)
block|{
comment|// Run the repository consumers
try|try
block|{
name|Closure
name|triggerBeginScan
init|=
operator|new
name|TriggerBeginScanClosure
argument_list|(
name|repository
argument_list|,
name|getStartTime
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|selectedKnownConsumers
init|=
name|getSelectedKnownConsumers
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|selectedInvalidConsumers
init|=
name|getSelectedInvalidConsumers
argument_list|()
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|selectedKnownConsumers
argument_list|,
name|triggerBeginScan
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|selectedInvalidConsumers
argument_list|,
name|triggerBeginScan
argument_list|)
expr_stmt|;
comment|// yuck. In case you can't read this, it says
comment|// "process the file if the consumer has it in the includes list, and not in the excludes list"
name|BaseFile
name|baseFile
init|=
operator|new
name|BaseFile
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|localFile
argument_list|)
decl_stmt|;
name|ConsumerWantsFilePredicate
name|predicate
init|=
operator|new
name|ConsumerWantsFilePredicate
argument_list|()
decl_stmt|;
name|predicate
operator|.
name|setBasefile
argument_list|(
name|baseFile
argument_list|)
expr_stmt|;
name|ConsumerProcessFileClosure
name|closure
init|=
operator|new
name|ConsumerProcessFileClosure
argument_list|()
decl_stmt|;
name|closure
operator|.
name|setBasefile
argument_list|(
name|baseFile
argument_list|)
expr_stmt|;
name|predicate
operator|.
name|setCaseSensitive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Closure
name|processIfWanted
init|=
name|IfClosure
operator|.
name|getInstance
argument_list|(
name|predicate
argument_list|,
name|closure
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|selectedKnownConsumers
argument_list|,
name|processIfWanted
argument_list|)
expr_stmt|;
if|if
condition|(
name|predicate
operator|.
name|getWantedFileCount
argument_list|()
operator|<=
literal|0
condition|)
block|{
comment|// Nothing known processed this file.  It is invalid!
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|selectedInvalidConsumers
argument_list|,
name|closure
argument_list|)
expr_stmt|;
block|}
name|TriggerScanCompletedClosure
name|scanCompletedClosure
init|=
operator|new
name|TriggerScanCompletedClosure
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|selectedKnownConsumers
argument_list|,
name|scanCompletedClosure
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|/* TODO: This is never called by the repository scanner instance, so not calling here either - but it probably should be?                         CollectionUtils.forAllDo( availableKnownConsumers, triggerCompleteScan );                         CollectionUtils.forAllDo( availableInvalidConsumers, triggerCompleteScan );             */
block|}
block|}
specifier|public
name|void
name|setSelectedKnownConsumers
parameter_list|(
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|selectedKnownConsumers
parameter_list|)
block|{
name|this
operator|.
name|selectedKnownConsumers
operator|=
name|selectedKnownConsumers
expr_stmt|;
block|}
specifier|public
name|void
name|setSelectedInvalidConsumers
parameter_list|(
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|selectedInvalidConsumers
parameter_list|)
block|{
name|this
operator|.
name|selectedInvalidConsumers
operator|=
name|selectedInvalidConsumers
expr_stmt|;
block|}
specifier|protected
name|Date
name|getStartTime
parameter_list|()
block|{
return|return
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setArchivaConfiguration
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
block|{
name|this
operator|.
name|archivaConfiguration
operator|=
name|archivaConfiguration
expr_stmt|;
block|}
block|}
end_class

end_unit

