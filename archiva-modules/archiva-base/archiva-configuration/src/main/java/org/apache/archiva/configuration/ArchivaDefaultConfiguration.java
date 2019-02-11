begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  *   *       Archiva default settings.  *       *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|ArchivaDefaultConfiguration
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      * Field defaultCheckPaths.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|RepositoryCheckPath
argument_list|>
name|defaultCheckPaths
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method addDefaultCheckPath.      *       * @param repositoryCheckPath      */
specifier|public
name|void
name|addDefaultCheckPath
parameter_list|(
name|RepositoryCheckPath
name|repositoryCheckPath
parameter_list|)
block|{
name|getDefaultCheckPaths
argument_list|()
operator|.
name|add
argument_list|(
name|repositoryCheckPath
argument_list|)
expr_stmt|;
block|}
comment|//-- void addDefaultCheckPath( RepositoryCheckPath )
comment|/**      * Method getDefaultCheckPaths.      *       * @return List      */
specifier|public
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|RepositoryCheckPath
argument_list|>
name|getDefaultCheckPaths
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|defaultCheckPaths
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|defaultCheckPaths
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|RepositoryCheckPath
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|defaultCheckPaths
return|;
block|}
comment|//-- java.util.List<RepositoryCheckPath> getDefaultCheckPaths()
comment|/**      * Method removeDefaultCheckPath.      *       * @param repositoryCheckPath      */
specifier|public
name|void
name|removeDefaultCheckPath
parameter_list|(
name|RepositoryCheckPath
name|repositoryCheckPath
parameter_list|)
block|{
name|getDefaultCheckPaths
argument_list|()
operator|.
name|remove
argument_list|(
name|repositoryCheckPath
argument_list|)
expr_stmt|;
block|}
comment|//-- void removeDefaultCheckPath( RepositoryCheckPath )
comment|/**      * Set the default check paths for certain remote repositories.      *       * @param defaultCheckPaths      */
specifier|public
name|void
name|setDefaultCheckPaths
parameter_list|(
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|RepositoryCheckPath
argument_list|>
name|defaultCheckPaths
parameter_list|)
block|{
name|this
operator|.
name|defaultCheckPaths
operator|=
name|defaultCheckPaths
expr_stmt|;
block|}
comment|//-- void setDefaultCheckPaths( java.util.List )
block|}
end_class

end_unit

