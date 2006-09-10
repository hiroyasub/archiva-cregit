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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
import|;
end_import

begin_comment
comment|/**  * A component for loading the reporting database into the model.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @todo this is something that could possibly be generalised into Modello.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ReportingStore
block|{
comment|/**      * The Plexus role for the component.      */
name|String
name|ROLE
init|=
name|ReportingStore
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Get the reports from the store. A cached version may be used.      *      * @param repository  the repository to load the reports for      * @param reportGroup the report group to get the report for      * @return the reporting database      * @throws ReportingStoreException if there was a problem reading the store      */
name|ReportingDatabase
name|getReportsFromStore
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|ReportGroup
name|reportGroup
parameter_list|)
throws|throws
name|ReportingStoreException
function_decl|;
comment|/**      * Save the reporting to the store.      *      * @param database   the reports to store      * @param repository the repositorry to store the reports in      * @throws ReportingStoreException if there was a problem writing the store      */
name|void
name|storeReports
parameter_list|(
name|ReportingDatabase
name|database
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|ReportingStoreException
function_decl|;
block|}
end_interface

end_unit

