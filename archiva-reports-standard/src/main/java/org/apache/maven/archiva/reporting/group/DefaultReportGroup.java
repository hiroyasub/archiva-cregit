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
name|group
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
name|group
operator|.
name|AbstractReportGroup
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
comment|/**  * The default report set, for repository health.  *  * @plexus.component role="org.apache.maven.archiva.reporting.group.ReportGroup" role-hint="health"  * @todo could these report groups be assembled dynamically by configuration rather than as explicit components? eg, reportGroup.addReport( ARP ), reportGroup.addReport( MRP )  */
end_comment

begin_class
specifier|public
class|class
name|DefaultReportGroup
extends|extends
name|AbstractReportGroup
block|{
comment|/**      * Role hints of the reports to include in this set.      */
specifier|private
specifier|static
specifier|final
name|Map
name|reports
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|reports
operator|.
name|put
argument_list|(
literal|"checksum"
argument_list|,
literal|"Checksum Problems"
argument_list|)
expr_stmt|;
name|reports
operator|.
name|put
argument_list|(
literal|"dependency"
argument_list|,
literal|"Dependency Problems"
argument_list|)
expr_stmt|;
comment|// TODO re-enable duplicate, once a way to populate the index is determined!
comment|//        reports.put( "duplicate", "Duplicate Artifact Problems" );
name|reports
operator|.
name|put
argument_list|(
literal|"invalid-pom"
argument_list|,
literal|"POM Problems"
argument_list|)
expr_stmt|;
name|reports
operator|.
name|put
argument_list|(
literal|"bad-metadata"
argument_list|,
literal|"Metadata Problems"
argument_list|)
expr_stmt|;
name|reports
operator|.
name|put
argument_list|(
literal|"checksum-metadata"
argument_list|,
literal|"Metadata Checksum Problems"
argument_list|)
expr_stmt|;
name|reports
operator|.
name|put
argument_list|(
literal|"artifact-location"
argument_list|,
literal|"Artifact Location Problems"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|includeReport
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|reports
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|Map
name|getReports
parameter_list|()
block|{
return|return
name|reports
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Repository Health"
return|;
block|}
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
literal|"health-report.xml"
return|;
block|}
block|}
end_class

end_unit

