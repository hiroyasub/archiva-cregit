begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"searchRequest"
argument_list|)
specifier|public
class|class
name|SearchRequest
implements|implements
name|Serializable
block|{
comment|/**      * @since 1.4-M3      *        to be able to search with a query on selected repositories      */
specifier|private
name|String
name|queryTerms
decl_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-Version if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleVersion
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-SymbolicName if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleSymbolicName
decl_stmt|;
comment|/**      * contains osgi metadata Export-Package if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleExportPackage
decl_stmt|;
comment|/**      * contains osgi metadata Export-Service if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleExportService
decl_stmt|;
comment|/**      * contains osgi metadata Import-Package if available      *      * @since 1.4-M3      */
specifier|private
name|String
name|bundleImportPackage
decl_stmt|;
comment|/**      * contains osgi metadata Require-Bundle if available      *      * @since 1.4-M3      */
specifier|private
name|String
name|bundleRequireBundle
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
comment|/**      * not return artifact with file extension pom      *      * @since 1.4-M2      */
specifier|private
name|boolean
name|includePomArtifacts
init|=
literal|false
decl_stmt|;
comment|/**      * @since 1.4-M4      */
specifier|private
name|int
name|pageSize
init|=
literal|30
decl_stmt|;
comment|/**      * @since 1.4-M4      */
specifier|private
name|int
name|selectedPage
init|=
literal|0
decl_stmt|;
specifier|public
name|SearchRequest
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|SearchRequest
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
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIncludePomArtifacts
parameter_list|()
block|{
return|return
name|includePomArtifacts
return|;
block|}
specifier|public
name|void
name|setIncludePomArtifacts
parameter_list|(
name|boolean
name|includePomArtifacts
parameter_list|)
block|{
name|this
operator|.
name|includePomArtifacts
operator|=
name|includePomArtifacts
expr_stmt|;
block|}
specifier|public
name|String
name|getQueryTerms
parameter_list|()
block|{
return|return
name|queryTerms
return|;
block|}
specifier|public
name|void
name|setQueryTerms
parameter_list|(
name|String
name|queryTerms
parameter_list|)
block|{
name|this
operator|.
name|queryTerms
operator|=
name|queryTerms
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleImportPackage
parameter_list|()
block|{
return|return
name|bundleImportPackage
return|;
block|}
specifier|public
name|void
name|setBundleImportPackage
parameter_list|(
name|String
name|bundleImportPackage
parameter_list|)
block|{
name|this
operator|.
name|bundleImportPackage
operator|=
name|bundleImportPackage
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleRequireBundle
parameter_list|()
block|{
return|return
name|bundleRequireBundle
return|;
block|}
specifier|public
name|void
name|setBundleRequireBundle
parameter_list|(
name|String
name|bundleRequireBundle
parameter_list|)
block|{
name|this
operator|.
name|bundleRequireBundle
operator|=
name|bundleRequireBundle
expr_stmt|;
block|}
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
specifier|public
name|void
name|setPageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
block|}
specifier|public
name|int
name|getSelectedPage
parameter_list|()
block|{
return|return
name|selectedPage
return|;
block|}
specifier|public
name|void
name|setSelectedPage
parameter_list|(
name|int
name|selectedPage
parameter_list|)
block|{
name|this
operator|.
name|selectedPage
operator|=
name|selectedPage
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"SearchRequest"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{queryTerms='"
argument_list|)
operator|.
name|append
argument_list|(
name|queryTerms
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", groupId='"
argument_list|)
operator|.
name|append
argument_list|(
name|groupId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", artifactId='"
argument_list|)
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", version='"
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", packaging='"
argument_list|)
operator|.
name|append
argument_list|(
name|packaging
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", className='"
argument_list|)
operator|.
name|append
argument_list|(
name|className
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", repositories="
argument_list|)
operator|.
name|append
argument_list|(
name|repositories
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", bundleVersion='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleVersion
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", bundleSymbolicName='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleSymbolicName
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", bundleExportPackage='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleExportPackage
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", bundleExportService='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleExportService
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", bundleImportPackage='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleImportPackage
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", bundleRequireBundle='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleRequireBundle
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", classifier='"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", includePomArtifacts="
argument_list|)
operator|.
name|append
argument_list|(
name|includePomArtifacts
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", pageSize="
argument_list|)
operator|.
name|append
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", selectedPage="
argument_list|)
operator|.
name|append
argument_list|(
name|selectedPage
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

