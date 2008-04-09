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
name|content
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
name|lang
operator|.
name|StringUtils
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
name|FileTypes
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
name|ArtifactReference
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
name|ManagedRepositoryContent
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
name|layout
operator|.
name|LayoutException
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
name|metadata
operator|.
name|MetadataTools
import|;
end_import

begin_comment
comment|/**  * RepositoryRequest is used to determine the type of request that is incoming, and convert it to an appropriate  * ArtifactReference.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *  * @todo no need to be a component once legacy path parser is not  *  * @plexus.component  *      role="org.apache.maven.archiva.repository.content.RepositoryRequest"  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryRequest
block|{
specifier|private
name|PathParser
name|defaultPathParser
init|=
operator|new
name|DefaultPathParser
argument_list|()
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="legacy"      */
specifier|private
name|PathParser
name|legacyPathParser
decl_stmt|;
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
return|return
name|legacyPathParser
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
return|;
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
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a default layout request.      *</p>      *<p>      * NOTE: This does a cursory check on the count of path elements only.  A result of      * true from this method is not a guarantee that the path sections are valid and      * can be resolved to an artifact reference.  use {@link #toArtifactReference(String)}      * if you want a more complete analysis of the validity of the path.      *</p>      *      * @param requestedPath the path to test.      * @return true if the requestedPath is likely that of a default layout request.      */
specifier|public
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
return|return
name|pathParts
operator|.
name|length
operator|>
literal|3
return|;
block|}
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a legacy layout request.      *</p>      *<p>      * NOTE: This does a cursory check on the count of path elements only.  A result of      * true from this method is not a guarantee that the path sections are valid and      * can be resolved to an artifact reference.  use {@link #toArtifactReference(String)}      * if you want a more complete analysis of the validity of the path.      *</p>      *      * @param requestedPath the path to test.      * @return true if the requestedPath is likely that of a legacy layout request.      */
specifier|public
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
comment|/**      * Adjust the requestedPath to conform to the native layout of the provided {@link ManagedRepositoryContent}.      *      * @param requestedPath the incoming requested path.      * @param repository the repository to adjust to.      * @return the adjusted (to native) path.      * @throws LayoutException if the path cannot be parsed.      */
specifier|public
name|String
name|toNativePath
parameter_list|(
name|String
name|requestedPath
parameter_list|,
name|ManagedRepositoryContent
name|repository
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
if|if
condition|(
name|repository
operator|instanceof
name|ManagedLegacyRepositoryContent
condition|)
block|{
throw|throw
operator|new
name|LayoutException
argument_list|(
literal|"Cannot translate metadata request to legacy layout."
argument_list|)
throw|;
block|}
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
block|}
end_class

end_unit

