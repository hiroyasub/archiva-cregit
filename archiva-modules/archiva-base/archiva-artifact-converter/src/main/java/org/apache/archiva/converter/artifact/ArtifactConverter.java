begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|converter
operator|.
name|artifact
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
name|artifact
operator|.
name|Artifact
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * ArtifactConverter   *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArtifactConverter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|ArtifactConverter
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Convert an provided artifact, and place it into the destination repository.      *       * @param artifact the artifact to convert.      * @param destinationRepository the respository to send the artifact to.      * @throws ArtifactConversionException       */
name|void
name|convert
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ArtifactRepository
name|destinationRepository
parameter_list|)
throws|throws
name|ArtifactConversionException
function_decl|;
comment|/**      * Get the map of accumulated warnings for the conversion.      *       * @return the {@link Map}&lt;{@link Artifact}, {@link String}&gt; warning messages.      */
name|Map
argument_list|<
name|Artifact
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getWarnings
parameter_list|()
function_decl|;
comment|/**      * Clear the list of warning messages.      */
name|void
name|clearWarnings
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

