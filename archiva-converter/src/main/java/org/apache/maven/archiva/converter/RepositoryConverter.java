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
name|converter
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|reporting
operator|.
name|database
operator|.
name|ReportingDatabase
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

begin_comment
comment|/**  * Copy a set of artifacts from one repository to the other, converting if necessary.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryConverter
block|{
name|String
name|ROLE
init|=
name|RepositoryConverter
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Convert a single artifact, writing it into the target repository.      *      * @param artifact         the artifact to convert      * @param targetRepository the target repository      * @param reporter         reporter to track the results of the conversion      */
name|void
name|convert
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ArtifactRepository
name|targetRepository
parameter_list|,
name|ReportingDatabase
name|reporter
parameter_list|)
throws|throws
name|RepositoryConversionException
function_decl|;
comment|/**      * Convert a set of artifacts, writing them into the target repository.      *      * @param artifacts        the set of artifacts to convert      * @param targetRepository the target repository      * @param reporter         reporter to track the results of the conversions      */
name|void
name|convert
parameter_list|(
name|List
name|artifacts
parameter_list|,
name|ArtifactRepository
name|targetRepository
parameter_list|,
name|ReportingDatabase
name|reporter
parameter_list|)
throws|throws
name|RepositoryConversionException
function_decl|;
block|}
end_interface

end_unit

