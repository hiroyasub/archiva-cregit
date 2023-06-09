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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Represents a artifact of a repository. This object contains unique coordinates of the  * artifact. A artifact has exactly one file representation in the repository.  * The artifact instance does not tell, if the file exists or is readable. It just  * keeps the coordinates and some meta information of the artifact.  *<p>  * Artifact implementations should be immutable. The implementation must not always represent the current state of the  * corresponding storage asset (file). It is just a view of the attributes for a given point in time.  *<p>  * Implementations must provide proper hash and equals methods.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Artifact
extends|extends
name|DataItem
block|{
comment|/**      * The artifact identifier. The ID is unique in a given namespace of a given repository.      * But there may exist artifacts with the same ID but different types, classifiers or extensions.      *<p>      * Never returns<code>null</code> or a empty string.      *      * @return the identifier of the artifact. Never returns<code>null</code> or empty string      */
annotation|@
name|Override
name|String
name|getId
parameter_list|( )
function_decl|;
comment|/**      * The version string of the artifact. The version string is exactly the version that is attached      * to the artifact.      * The version string may be different to the version string returned by the attached {@link Version} object.      * E.g for maven artifacts the artifact version may be 1.3-20070725.210059-1 and the attached {@link Version} object      * has version 1.3-SNAPSHOT.      *      * @return the artifact version string      * @see #getVersion()      */
name|String
name|getArtifactVersion
parameter_list|( )
function_decl|;
comment|/**      * Returns the project this artifact is attached to      * @return the project      */
name|Project
name|getProject
parameter_list|()
function_decl|;
comment|/**      * Returns the namespace this artifact is attached to      * @return the namespace object      */
name|Namespace
name|getNamespace
parameter_list|()
function_decl|;
comment|/**      * Returns the attached version this artifact is part of.      *      * @return the version object      */
name|Version
name|getVersion
parameter_list|( )
function_decl|;
comment|/**      * Returns the type of the artifact. The type is some hint about the usage of the artifact.      * Implementations may always return a empty string, if it is not used.      *      * @return the type of the artifact. Returns never<code>null</code>, but may be empty string      */
name|String
name|getType
parameter_list|( )
function_decl|;
comment|/**      * A classifier that distinguishes artifacts.      * Implementations may always return a empty string, if it is not used.      *      * @return the classifier of the artifact. Returns never<code>null</code>, but may be empty string      */
name|String
name|getClassifier
parameter_list|( )
function_decl|;
comment|/**      * This may be different from extension and gives the remainder that is used to build the file path from      * the artifact coordinates (namespace, id, version, classifier, type)      *      * @return the file name remainder      */
name|String
name|getRemainder
parameter_list|( )
function_decl|;
comment|/**      * Returns the type of the artifact      * @return      */
annotation|@
name|Override
name|ArtifactType
name|getDataType
parameter_list|()
function_decl|;
comment|/**      * Returns a unique key      * @return      */
name|String
name|toKey
parameter_list|()
function_decl|;
comment|/**      * Short cut for the file name. Should always return the same value as the artifact name.      *      * @return the name of the file      */
annotation|@
name|Override
specifier|default
name|String
name|getFileName
parameter_list|( )
block|{
return|return
name|getAsset
argument_list|( )
operator|.
name|getName
argument_list|( )
return|;
block|}
comment|/**      * Returns the extension of the file. This method should always return the extension string after the last      * '.'-character.      *      * @return the file name extension      */
annotation|@
name|Override
specifier|default
name|String
name|getExtension
parameter_list|( )
block|{
specifier|final
name|String
name|name
init|=
name|getAsset
argument_list|( )
operator|.
name|getName
argument_list|( )
decl_stmt|;
specifier|final
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
specifier|default
name|ContentItem
name|getParent
parameter_list|( )
block|{
return|return
name|getVersion
argument_list|()
return|;
block|}
block|}
end_interface

end_unit

