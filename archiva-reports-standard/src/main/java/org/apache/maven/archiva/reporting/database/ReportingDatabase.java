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
name|reporting
operator|.
name|database
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * The Main Reporting Database.  *   * @todo i18n, including message formatting and parameterisation  * @plexus.component role="org.apache.maven.archiva.reporting.database.ReportingDatabase"  *                   role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|ReportingDatabase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|ReportingDatabase
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|ArtifactResultsDatabase
name|artifactDatabase
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="default"      */
specifier|private
name|MetadataResultsDatabase
name|metadataDatabase
decl_stmt|;
specifier|public
name|Iterator
name|getArtifactIterator
parameter_list|()
block|{
return|return
name|artifactDatabase
operator|.
name|getIterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getMetadataIterator
parameter_list|()
block|{
return|return
name|metadataDatabase
operator|.
name|getIterator
argument_list|()
return|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
block|}
comment|/**      *<p>      * Get the number of failures in the database.      *</p>      *       *<p>      *<b>WARNING:</b> This is a very resource intensive request. Use sparingly.      *</p>      *       * @return the number of failures in the database.      */
specifier|public
name|int
name|getNumFailures
parameter_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|count
operator|+=
name|artifactDatabase
operator|.
name|getNumFailures
argument_list|()
expr_stmt|;
name|count
operator|+=
name|metadataDatabase
operator|.
name|getNumFailures
argument_list|()
expr_stmt|;
return|return
name|count
return|;
block|}
comment|/**      *<p>      * Get the number of notices in the database.      *</p>      *       *<p>      *<b>WARNING:</b> This is a very resource intensive request. Use sparingly.      *</p>      *       * @return the number of notices in the database.      */
specifier|public
name|int
name|getNumNotices
parameter_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|count
operator|+=
name|artifactDatabase
operator|.
name|getNumNotices
argument_list|()
expr_stmt|;
name|count
operator|+=
name|metadataDatabase
operator|.
name|getNumNotices
argument_list|()
expr_stmt|;
return|return
name|count
return|;
block|}
comment|/**      *<p>      * Get the number of warnings in the database.      *</p>      *       *<p>      *<b>WARNING:</b> This is a very resource intensive request. Use sparingly.      *</p>      *       * @return the number of warnings in the database.      */
specifier|public
name|int
name|getNumWarnings
parameter_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|count
operator|+=
name|artifactDatabase
operator|.
name|getNumWarnings
argument_list|()
expr_stmt|;
name|count
operator|+=
name|metadataDatabase
operator|.
name|getNumWarnings
argument_list|()
expr_stmt|;
return|return
name|count
return|;
block|}
specifier|public
name|ArtifactResultsDatabase
name|getArtifactDatabase
parameter_list|()
block|{
return|return
name|artifactDatabase
return|;
block|}
specifier|public
name|MetadataResultsDatabase
name|getMetadataDatabase
parameter_list|()
block|{
return|return
name|metadataDatabase
return|;
block|}
block|}
end_class

end_unit

