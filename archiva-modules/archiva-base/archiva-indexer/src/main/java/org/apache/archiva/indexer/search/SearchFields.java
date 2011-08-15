begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|search
package|;
end_package

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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_class
specifier|public
class|class
name|SearchFields
block|{
comment|/**      * groupId      */
specifier|private
name|String
name|groupId
decl_stmt|;
comment|/**      * artifactId      */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|/**      * version      */
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      * packaging (jar, war, pom, etc.)      */
specifier|private
name|String
name|packaging
decl_stmt|;
comment|/**      * class name or package name      */
specifier|private
name|String
name|className
decl_stmt|;
comment|/**      * repositories      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-Version if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleVersion
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-SymbolicName if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleSymbolicName
decl_stmt|;
comment|/**      * contains osgi metadata Export-Package if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleExportPackage
decl_stmt|;
comment|/**      * contains osgi metadata Export-Service if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleExportService
decl_stmt|;
specifier|public
name|SearchFields
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|SearchFields
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|packaging
parameter_list|,
name|String
name|className
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|repositories
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|packaging
operator|=
name|packaging
expr_stmt|;
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
name|this
operator|.
name|repositories
operator|=
name|repositories
expr_stmt|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getPackaging
parameter_list|()
block|{
return|return
name|packaging
return|;
block|}
specifier|public
name|void
name|setPackaging
parameter_list|(
name|String
name|packaging
parameter_list|)
block|{
name|this
operator|.
name|packaging
operator|=
name|packaging
expr_stmt|;
block|}
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
specifier|public
name|void
name|setClassName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRepositories
parameter_list|()
block|{
return|return
name|repositories
return|;
block|}
specifier|public
name|void
name|setRepositories
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositories
parameter_list|)
block|{
name|this
operator|.
name|repositories
operator|=
name|repositories
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleVersion
parameter_list|()
block|{
return|return
name|bundleVersion
return|;
block|}
specifier|public
name|void
name|setBundleVersion
parameter_list|(
name|String
name|bundleVersion
parameter_list|)
block|{
name|this
operator|.
name|bundleVersion
operator|=
name|bundleVersion
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleSymbolicName
parameter_list|()
block|{
return|return
name|bundleSymbolicName
return|;
block|}
specifier|public
name|void
name|setBundleSymbolicName
parameter_list|(
name|String
name|bundleSymbolicName
parameter_list|)
block|{
name|this
operator|.
name|bundleSymbolicName
operator|=
name|bundleSymbolicName
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleExportPackage
parameter_list|()
block|{
return|return
name|bundleExportPackage
return|;
block|}
specifier|public
name|void
name|setBundleExportPackage
parameter_list|(
name|String
name|bundleExportPackage
parameter_list|)
block|{
name|this
operator|.
name|bundleExportPackage
operator|=
name|bundleExportPackage
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleExportService
parameter_list|()
block|{
return|return
name|bundleExportService
return|;
block|}
specifier|public
name|void
name|setBundleExportService
parameter_list|(
name|String
name|bundleExportService
parameter_list|)
block|{
name|this
operator|.
name|bundleExportService
operator|=
name|bundleExportService
expr_stmt|;
block|}
block|}
end_class

end_unit

