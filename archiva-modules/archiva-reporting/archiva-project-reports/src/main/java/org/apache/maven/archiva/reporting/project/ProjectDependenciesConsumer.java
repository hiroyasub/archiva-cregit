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
name|reporting
operator|.
name|project
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
name|maven
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|AbstractMonitoredConsumer
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
name|ArchivaArtifactConsumer
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
name|ConsumerException
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
name|List
import|;
end_import

begin_comment
comment|/**  * ProjectDependenciesConsumer   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.consumers.ArchivaArtifactConsumer"  *                   role-hint="missing-dependencies"  */
end_comment

begin_class
specifier|public
class|class
name|ProjectDependenciesConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|ArchivaArtifactConsumer
block|{
comment|/**      * @plexus.configuration default-value="missing-dependencies"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Check for missing dependencies."      */
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|List
name|includes
decl_stmt|;
specifier|public
name|ProjectDependenciesConsumer
parameter_list|()
block|{
name|this
operator|.
name|includes
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|this
operator|.
name|includes
operator|.
name|add
argument_list|(
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|List
name|getIncludedTypes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
specifier|public
name|void
name|processArchivaArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ConsumerException
block|{
comment|// TODO: consider loading this logic into the 'update-db-project' consumer.
comment|// TODO: Load the ArchivaProjectModel.
comment|// TODO: Attach a monitor for missing parent poms to resolvers / filters.
comment|// TODO: Attach a monitor for missing dependencies to resolvers / filters.
comment|// TODO: Fully resolve the ArchivaProjectModel and listen on monitors.
block|}
block|}
end_class

end_unit

