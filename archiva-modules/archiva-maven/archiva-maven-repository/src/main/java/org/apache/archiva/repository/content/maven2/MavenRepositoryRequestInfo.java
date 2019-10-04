begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|maven2
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
name|archiva
operator|.
name|model
operator|.
name|ArtifactReference
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
name|*
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
name|content
operator|.
name|PathParser
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
name|features
operator|.
name|RepositoryFeature
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
name|metadata
operator|.
name|base
operator|.
name|MetadataTools
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
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * RepositoryRequest is used to determine the type of request that is incoming, and convert it to an appropriate  * ArtifactReference.  */
end_comment

begin_class
specifier|public
class|class
name|MavenRepositoryRequestInfo
implements|implements
name|RepositoryRequestInfo
block|{
specifier|private
name|PathParser
name|defaultPathParser
init|=
operator|new
name|DefaultPathParser
argument_list|()
decl_stmt|;
name|ManagedRepository
name|repository
decl_stmt|;
specifier|public
name|MavenRepositoryRequestInfo
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
comment|/**      * Takes an incoming requested path (in "/" format) and gleans the layout      * and ArtifactReference appropriate for that content.      *      * @param requestedPath the relative path to the content.      * @return the ArtifactReference for the requestedPath.      * @throws LayoutException if the request path is not layout valid.      */
specifier|public
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|requestedPath
parameter_list|)
throws|throws
name|LayoutException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|requestedPath
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Blank request path is not a valid."
argument_list|)
throw|;
block|}
name|String
name|path
init|=
name|requestedPath
decl_stmt|;
while|while
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// Only slash? that's bad, mmm-kay?
if|if
condition|(
literal|"/"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Invalid request path: Slash only."
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|isDefault
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|defaultPathParser
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
return|;
block|}
if|else if
condition|(
name|isLegacy
argument_list|(
name|path
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Legacy Maven1 repository not supported anymore."
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Not a valid request path layout, too short."
argument_list|)
throw|;
block|}
block|}
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a metadata request.      *</p>      *<p>      * NOTE: This does a cursory check on the path's last element.  A result of true      * from this method is not a guarantee that the metadata is in a valid format, or      * that it even contains data.      *</p>      *      * @param requestedPath the path to test.      * @return true if the requestedPath is likely a metadata request.      */
specifier|public
name|boolean
name|isMetadata
parameter_list|(
name|String
name|requestedPath
parameter_list|)
block|{
return|return
name|requestedPath
operator|.
name|endsWith
argument_list|(
literal|"/"
operator|+
name|MetadataTools
operator|.
name|MAVEN_METADATA
argument_list|)
return|;
block|}
comment|/**      * @param requestedPath      * @return true if the requestedPath is likely an archetype catalog request.      */
specifier|public
name|boolean
name|isArchetypeCatalog
parameter_list|(
name|String
name|requestedPath
parameter_list|)
block|{
return|return
name|requestedPath
operator|.
name|endsWith
argument_list|(
literal|"/"
operator|+
name|MetadataTools
operator|.
name|MAVEN_ARCHETYPE_CATALOG
argument_list|)
return|;
block|}
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a support file request.      *</p>      *<p>      * Tests for<code>.sha1</code>,<code>.md5</code>,<code>.asc</code>, and<code>.php</code>.      *</p>      *<p>      * NOTE: This does a cursory check on the path's extension only.  A result of true      * from this method is not a guarantee that the support resource is in a valid format, or      * that it even contains data.      *</p>      *      * @param requestedPath the path to test.      * @return true if the requestedPath is likely that of a support file request.      */
specifier|public
name|boolean
name|isSupportFile
parameter_list|(
name|String
name|requestedPath
parameter_list|)
block|{
name|int
name|idx
init|=
name|requestedPath
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|ext
init|=
name|requestedPath
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
decl_stmt|;
return|return
operator|(
literal|".sha1"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|||
literal|".md5"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|||
literal|".asc"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|||
literal|".pgp"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|)
return|;
block|}
specifier|public
name|boolean
name|isMetadataSupportFile
parameter_list|(
name|String
name|requestedPath
parameter_list|)
block|{
if|if
condition|(
name|isSupportFile
argument_list|(
name|requestedPath
argument_list|)
condition|)
block|{
name|String
name|basefilePath
init|=
name|StringUtils
operator|.
name|substring
argument_list|(
name|requestedPath
argument_list|,
literal|0
argument_list|,
name|requestedPath
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isMetadata
argument_list|(
name|basefilePath
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLayout
parameter_list|(
name|String
name|requestPath
parameter_list|)
block|{
if|if
condition|(
name|isDefault
argument_list|(
name|requestPath
argument_list|)
condition|)
block|{
return|return
literal|"default"
return|;
block|}
if|else if
condition|(
name|isLegacy
argument_list|(
name|requestPath
argument_list|)
condition|)
block|{
return|return
literal|"legacy"
return|;
block|}
else|else
block|{
return|return
literal|"unknown"
return|;
block|}
block|}
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a default layout request.      *</p>      *<p>      * NOTE: This does a cursory check on the count of path elements only.  A result of      * true from this method is not a guarantee that the path sections are valid and      * can be resolved to an artifact reference.  use {@link #toArtifactReference(String)}      * if you want a more complete analysis of the validity of the path.      *</p>      *      * @param requestedPath the path to test.      * @return true if the requestedPath is likely that of a default layout request.      */
specifier|private
name|boolean
name|isDefault
parameter_list|(
name|String
name|requestedPath
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|requestedPath
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|pathParts
index|[]
init|=
name|StringUtils
operator|.
name|splitPreserveAllTokens
argument_list|(
name|requestedPath
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pathParts
operator|.
name|length
operator|>
literal|3
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|pathParts
operator|.
name|length
operator|==
literal|3
condition|)
block|{
comment|// check if artifact-level metadata (ex. eclipse/jdtcore/maven-metadata.xml)
if|if
condition|(
name|isMetadata
argument_list|(
name|requestedPath
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
comment|// check if checksum of artifact-level metadata (ex. eclipse/jdtcore/maven-metadata.xml.sha1)
name|int
name|idx
init|=
name|requestedPath
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|String
name|base
init|=
name|requestedPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
if|if
condition|(
name|isMetadata
argument_list|(
name|base
argument_list|)
operator|&&
name|isSupportFile
argument_list|(
name|requestedPath
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a legacy layout request.      *</p>      *<p>      * NOTE: This does a cursory check on the count of path elements only.  A result of      * true from this method is not a guarantee that the path sections are valid and      * can be resolved to an artifact reference.  use {@link #toArtifactReference(String)}      * if you want a more complete analysis of the validity of the path.      *</p>      *      * @param requestedPath the path to test.      * @return true if the requestedPath is likely that of a legacy layout request.      */
specifier|private
name|boolean
name|isLegacy
parameter_list|(
name|String
name|requestedPath
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|requestedPath
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|pathParts
index|[]
init|=
name|StringUtils
operator|.
name|splitPreserveAllTokens
argument_list|(
name|requestedPath
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
return|return
name|pathParts
operator|.
name|length
operator|==
literal|3
return|;
block|}
comment|/**      * Adjust the requestedPath to conform to the native layout of the provided {@link org.apache.archiva.repository.ManagedRepositoryContent}.      *      * @param requestedPath the incoming requested path.      * @return the adjusted (to native) path.      * @throws LayoutException if the path cannot be parsed.      */
specifier|public
name|String
name|toNativePath
parameter_list|(
name|String
name|requestedPath
parameter_list|)
throws|throws
name|LayoutException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|requestedPath
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Request Path is blank."
argument_list|)
throw|;
block|}
name|String
name|referencedResource
init|=
name|requestedPath
decl_stmt|;
comment|// No checksum by default.
name|String
name|supportfile
init|=
literal|""
decl_stmt|;
comment|// Figure out support file, and actual referencedResource.
if|if
condition|(
name|isSupportFile
argument_list|(
name|requestedPath
argument_list|)
condition|)
block|{
name|int
name|idx
init|=
name|requestedPath
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|referencedResource
operator|=
name|requestedPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|supportfile
operator|=
name|requestedPath
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isMetadata
argument_list|(
name|referencedResource
argument_list|)
condition|)
block|{
comment|/* Nothing to translate.              * Default layout is the only layout that can contain maven-metadata.xml files, and              * if the managedRepository is layout legacy, this request would never occur.              */
return|return
name|requestedPath
return|;
block|}
comment|// Treat as an artifact reference.
name|ArtifactReference
name|ref
init|=
name|toArtifactReference
argument_list|(
name|referencedResource
argument_list|)
decl_stmt|;
name|String
name|adjustedPath
init|=
name|repository
operator|.
name|getContent
argument_list|()
operator|.
name|toPath
argument_list|(
name|ref
argument_list|)
decl_stmt|;
return|return
name|adjustedPath
operator|+
name|supportfile
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
name|getFeature
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|UnsupportedFeatureException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|boolean
name|supportsFeature
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

