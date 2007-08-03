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
name|Predicate
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
name|commons
operator|.
name|collections
operator|.
name|functors
operator|.
name|OrPredicate
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
name|consumers
operator|.
name|RepositoryContentConsumer
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
name|functors
operator|.
name|PermanentConsumerPredicate
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

begin_comment
comment|/**  * RepositoryContentConsumerUtil   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.repository.scanner.RepositoryContentConsumers"  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryContentConsumers
extends|extends
name|AbstractLogEnabled
implements|implements
name|Initializable
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"      */
specifier|private
name|List
name|availableKnownConsumers
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.consumers.InvalidRepositoryContentConsumer"      */
specifier|private
name|List
name|availableInvalidConsumers
decl_stmt|;
specifier|private
name|Predicate
name|selectedKnownPredicate
decl_stmt|;
specifier|private
name|Predicate
name|selectedInvalidPredicate
decl_stmt|;
class|class
name|SelectedKnownRepoConsumersPredicate
implements|implements
name|Predicate
block|{
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|boolean
name|satisfies
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|KnownRepositoryContentConsumer
condition|)
block|{
name|KnownRepositoryContentConsumer
name|known
init|=
operator|(
name|KnownRepositoryContentConsumer
operator|)
name|object
decl_stmt|;
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
operator|.
name|contains
argument_list|(
name|known
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
return|return
name|satisfies
return|;
block|}
block|}
class|class
name|SelectedInvalidRepoConsumersPredicate
implements|implements
name|Predicate
block|{
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|boolean
name|satisfies
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|InvalidRepositoryContentConsumer
condition|)
block|{
name|InvalidRepositoryContentConsumer
name|invalid
init|=
operator|(
name|InvalidRepositoryContentConsumer
operator|)
name|object
decl_stmt|;
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
operator|.
name|contains
argument_list|(
name|invalid
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
return|return
name|satisfies
return|;
block|}
block|}
class|class
name|RepoConsumerToMapClosure
implements|implements
name|Closure
block|{
specifier|private
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|instanceof
name|RepositoryContentConsumer
condition|)
block|{
name|RepositoryContentConsumer
name|consumer
init|=
operator|(
name|RepositoryContentConsumer
operator|)
name|input
decl_stmt|;
name|map
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
block|}
specifier|public
name|Map
name|getMap
parameter_list|()
block|{
return|return
name|map
return|;
block|}
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|Predicate
name|permanentConsumers
init|=
operator|new
name|PermanentConsumerPredicate
argument_list|()
decl_stmt|;
name|this
operator|.
name|selectedKnownPredicate
operator|=
operator|new
name|OrPredicate
argument_list|(
name|permanentConsumers
argument_list|,
operator|new
name|SelectedKnownRepoConsumersPredicate
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|selectedInvalidPredicate
operator|=
operator|new
name|OrPredicate
argument_list|(
name|permanentConsumers
argument_list|,
operator|new
name|SelectedInvalidRepoConsumersPredicate
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
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
specifier|public
name|List
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
specifier|public
name|Map
name|getSelectedKnownConsumersMap
parameter_list|()
block|{
name|RepoConsumerToMapClosure
name|consumerMapClosure
init|=
operator|new
name|RepoConsumerToMapClosure
argument_list|()
decl_stmt|;
name|Closure
name|ifclosure
init|=
name|IfClosure
operator|.
name|getInstance
argument_list|(
name|selectedKnownPredicate
argument_list|,
name|consumerMapClosure
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|availableKnownConsumers
argument_list|,
name|ifclosure
argument_list|)
expr_stmt|;
return|return
name|consumerMapClosure
operator|.
name|getMap
argument_list|()
return|;
block|}
specifier|public
name|Map
name|getSelectedInvalidConsumersMap
parameter_list|()
block|{
name|RepoConsumerToMapClosure
name|consumerMapClosure
init|=
operator|new
name|RepoConsumerToMapClosure
argument_list|()
decl_stmt|;
name|Closure
name|ifclosure
init|=
name|IfClosure
operator|.
name|getInstance
argument_list|(
name|selectedInvalidPredicate
argument_list|,
name|consumerMapClosure
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|availableInvalidConsumers
argument_list|,
name|ifclosure
argument_list|)
expr_stmt|;
return|return
name|consumerMapClosure
operator|.
name|getMap
argument_list|()
return|;
block|}
specifier|public
name|List
name|getSelectedKnownConsumers
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ret
operator|.
name|addAll
argument_list|(
name|CollectionUtils
operator|.
name|select
argument_list|(
name|availableKnownConsumers
argument_list|,
name|selectedKnownPredicate
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|List
name|getSelectedInvalidConsumers
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ret
operator|.
name|addAll
argument_list|(
name|CollectionUtils
operator|.
name|select
argument_list|(
name|availableInvalidConsumers
argument_list|,
name|selectedInvalidPredicate
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|List
name|getAvailableKnownConsumers
parameter_list|()
block|{
return|return
name|availableKnownConsumers
return|;
block|}
specifier|public
name|List
name|getAvailableInvalidConsumers
parameter_list|()
block|{
return|return
name|availableInvalidConsumers
return|;
block|}
block|}
end_class

end_unit

