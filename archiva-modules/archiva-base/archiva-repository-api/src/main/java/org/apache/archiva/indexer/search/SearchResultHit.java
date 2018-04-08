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

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
comment|/**  * SearchResultHit  *  */
end_comment

begin_class
specifier|public
class|class
name|SearchResultHit
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
init|=
literal|""
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|packaging
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
comment|/**      * contains osgi metadata Bundle-Description if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleDescription
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-Name if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleName
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-License if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleLicense
decl_stmt|;
comment|/**      * contains osgi metadata Bundle-DocURL if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleDocUrl
decl_stmt|;
comment|/**      * contains osgi metadata Import-Package if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleImportPackage
decl_stmt|;
comment|/**      * contains osgi metadata Require-Bundle if available      *      * @since 1.4-M1      */
specifier|private
name|String
name|bundleRequireBundle
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
comment|/**      * file extension of the search result      * @since 1.4-M2      */
specifier|private
name|String
name|fileExtension
decl_stmt|;
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
name|getUrlFilename
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
operator|.
name|substring
argument_list|(
name|this
operator|.
name|url
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
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
name|List
argument_list|<
name|String
argument_list|>
name|getVersions
parameter_list|()
block|{
return|return
name|versions
return|;
block|}
specifier|public
name|void
name|setVersions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|versions
parameter_list|)
block|{
name|this
operator|.
name|versions
operator|=
name|versions
expr_stmt|;
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
name|void
name|addVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|versions
operator|.
name|add
argument_list|(
name|version
argument_list|)
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
name|getType
parameter_list|()
block|{
return|return
name|getPackaging
argument_list|()
return|;
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
name|String
name|getFileExtension
parameter_list|()
block|{
return|return
name|fileExtension
return|;
block|}
specifier|public
name|void
name|setFileExtension
parameter_list|(
name|String
name|fileExtension
parameter_list|)
block|{
name|this
operator|.
name|fileExtension
operator|=
name|fileExtension
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
literal|"SearchResultHit"
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
literal|", versions="
argument_list|)
operator|.
name|append
argument_list|(
name|versions
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
literal|", fileExtension='"
argument_list|)
operator|.
name|append
argument_list|(
name|fileExtension
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
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|SearchResultHit
name|that
init|=
operator|(
name|SearchResultHit
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|artifactId
operator|!=
literal|null
condition|?
operator|!
name|artifactId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|artifactId
argument_list|)
else|:
name|that
operator|.
name|artifactId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|classifier
operator|!=
literal|null
condition|?
operator|!
name|classifier
operator|.
name|equals
argument_list|(
name|that
operator|.
name|classifier
argument_list|)
else|:
name|that
operator|.
name|classifier
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|groupId
operator|!=
literal|null
condition|?
operator|!
name|groupId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|groupId
argument_list|)
else|:
name|that
operator|.
name|groupId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|packaging
operator|!=
literal|null
condition|?
operator|!
name|packaging
operator|.
name|equals
argument_list|(
name|that
operator|.
name|packaging
argument_list|)
else|:
name|that
operator|.
name|packaging
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|groupId
operator|!=
literal|null
condition|?
name|groupId
operator|.
name|hashCode
argument_list|()
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|artifactId
operator|!=
literal|null
condition|?
name|artifactId
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|packaging
operator|!=
literal|null
condition|?
name|packaging
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|classifier
operator|!=
literal|null
condition|?
name|classifier
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit
