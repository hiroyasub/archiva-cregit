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
name|startup
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
name|common
operator|.
name|ArchivaException
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
name|database
operator|.
name|project
operator|.
name|ProjectModelToDatabaseListener
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
name|project
operator|.
name|ProjectModelResolver
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
name|project
operator|.
name|ProjectModelResolverFactory
import|;
end_import

begin_comment
comment|/**  * ResolverFactoryInit - Initialize the Resolver Factory, and hook it up to  * the database.  *  * @version $Id$  *   * @plexus.component   *              role="org.apache.maven.archiva.web.startup.ResolverFactoryInit"  *              role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|ResolverFactoryInit
block|{
comment|/**      * @plexus.requirement role-hint="database"      */
specifier|private
name|ProjectModelResolver
name|databaseResolver
decl_stmt|;
comment|/**      * @plexus.requirement       *          role="org.apache.maven.archiva.repository.project.resolvers.ProjectModelResolutionListener"      *          role-hint="model-to-db"      */
specifier|private
name|ProjectModelToDatabaseListener
name|modelToDbListener
decl_stmt|;
comment|/**      * The resolver factorying being initialized.      *       * @plexus.requirement      */
specifier|private
name|ProjectModelResolverFactory
name|resolverFactory
decl_stmt|;
specifier|public
name|void
name|startup
parameter_list|()
throws|throws
name|ArchivaException
block|{
if|if
condition|(
operator|!
name|resolverFactory
operator|.
name|getCurrentResolverStack
argument_list|()
operator|.
name|hasResolver
argument_list|(
name|databaseResolver
argument_list|)
condition|)
block|{
name|resolverFactory
operator|.
name|getCurrentResolverStack
argument_list|()
operator|.
name|prependProjectModelResolver
argument_list|(
name|databaseResolver
argument_list|)
expr_stmt|;
block|}
name|resolverFactory
operator|.
name|getCurrentResolverStack
argument_list|()
operator|.
name|addListener
argument_list|(
name|modelToDbListener
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

