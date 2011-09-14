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

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|List
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"artifact"
argument_list|)
specifier|public
class|class
name|Artifact
implements|implements
name|Serializable
block|{
comment|// The (optional) context for this result.
specifier|private
name|String
name|context
decl_stmt|;
comment|// Basic hit, direct to non-artifact resource.
specifier|private
name|String
name|url
decl_stmt|;
comment|// Advanced hit, reference to groupId.
specifier|private
name|String
name|groupId
decl_stmt|;
comment|//  Advanced hit, reference to artifactId.
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|repositoryId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      * Plugin goal prefix (only if packaging is "maven-plugin")      */
specifier|private
name|String
name|prefix
decl_stmt|;
comment|/**      * Plugin goals (only if packaging is "maven-plugin")      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|goals
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
comment|/**      * contains osgi metadata Bundle-Description if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleDescription
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-Name if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleName
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-License if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleLicense
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-DocURL if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleDocUrl
decl_stmt|;
comment|/**      * contains osgi metadata Import-Package if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleImportPackage
decl_stmt|;
comment|/**      * contains osgi metadata Require-Bundle if available      *      * @since 1.4      */
specifier|private
name|String
name|bundleRequireBundle
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
specifier|public
name|Artifact
parameter_list|()
block|{
comment|// no op
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
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
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
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
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
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|String
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|void
name|setContext
parameter_list|(
name|String
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getGoals
parameter_list|()
block|{
return|return
name|goals
return|;
block|}
specifier|public
name|void
name|setGoals
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|goals
parameter_list|)
block|{
name|this
operator|.
name|goals
operator|=
name|goals
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
name|getBundleDescription
parameter_list|()
block|{
return|return
name|bundleDescription
return|;
block|}
specifier|public
name|void
name|setBundleDescription
parameter_list|(
name|String
name|bundleDescription
parameter_list|)
block|{
name|this
operator|.
name|bundleDescription
operator|=
name|bundleDescription
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleName
parameter_list|()
block|{
return|return
name|bundleName
return|;
block|}
specifier|public
name|void
name|setBundleName
parameter_list|(
name|String
name|bundleName
parameter_list|)
block|{
name|this
operator|.
name|bundleName
operator|=
name|bundleName
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleLicense
parameter_list|()
block|{
return|return
name|bundleLicense
return|;
block|}
specifier|public
name|void
name|setBundleLicense
parameter_list|(
name|String
name|bundleLicense
parameter_list|)
block|{
name|this
operator|.
name|bundleLicense
operator|=
name|bundleLicense
expr_stmt|;
block|}
specifier|public
name|String
name|getBundleDocUrl
parameter_list|()
block|{
return|return
name|bundleDocUrl
return|;
block|}
specifier|public
name|void
name|setBundleDocUrl
parameter_list|(
name|String
name|bundleDocUrl
parameter_list|)
block|{
name|this
operator|.
name|bundleDocUrl
operator|=
name|bundleDocUrl
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
literal|"Artifact"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{context='"
argument_list|)
operator|.
name|append
argument_list|(
name|context
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
literal|", url='"
argument_list|)
operator|.
name|append
argument_list|(
name|url
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
literal|", repositoryId='"
argument_list|)
operator|.
name|append
argument_list|(
name|repositoryId
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
literal|", prefix='"
argument_list|)
operator|.
name|append
argument_list|(
name|prefix
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
literal|", goals="
argument_list|)
operator|.
name|append
argument_list|(
name|goals
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
literal|", bundleDescription='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleDescription
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
literal|", bundleName='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleName
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
literal|", bundleLicense='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleLicense
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
literal|", bundleDocUrl='"
argument_list|)
operator|.
name|append
argument_list|(
name|bundleDocUrl
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

