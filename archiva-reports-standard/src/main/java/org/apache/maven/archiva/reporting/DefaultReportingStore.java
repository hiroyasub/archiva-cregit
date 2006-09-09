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
name|archiva
operator|.
name|reporting
operator|.
name|model
operator|.
name|io
operator|.
name|xpp3
operator|.
name|ReportingXpp3Reader
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
name|reporting
operator|.
name|model
operator|.
name|io
operator|.
name|xpp3
operator|.
name|ReportingXpp3Writer
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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|IOUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|xml
operator|.
name|pull
operator|.
name|XmlPullParserException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
comment|/**  * Load and store the reports. No synchronization is used, but it is unnecessary as the old object  * can continue to be used.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @todo would be great for plexus to do this for us - so the configuration would be a component itself rather than this store  * @todo support other implementations than XML file  * @plexus.component  */
end_comment

begin_class
specifier|public
class|class
name|DefaultReportingStore
extends|extends
name|AbstractLogEnabled
implements|implements
name|ReportingStore
block|{
comment|/**      * The cached reports for given repositories.      */
specifier|private
name|Map
comment|/*<ArtifactRepository,ReportingDatabase>*/
name|reports
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|ReportingDatabase
name|getReportsFromStore
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|ReportingStoreException
block|{
name|ReportingDatabase
name|database
init|=
operator|(
name|ReportingDatabase
operator|)
name|reports
operator|.
name|get
argument_list|(
name|repository
argument_list|)
decl_stmt|;
if|if
condition|(
name|database
operator|==
literal|null
condition|)
block|{
name|ReportingXpp3Reader
name|reader
init|=
operator|new
name|ReportingXpp3Reader
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"report-database.xml"
argument_list|)
decl_stmt|;
name|FileReader
name|fileReader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fileReader
operator|=
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|database
operator|=
operator|new
name|ReportingDatabase
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|database
operator|==
literal|null
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Reading report database from "
operator|+
name|file
argument_list|)
expr_stmt|;
try|try
block|{
name|database
operator|=
operator|new
name|ReportingDatabase
argument_list|(
name|reader
operator|.
name|read
argument_list|(
name|fileReader
argument_list|,
literal|false
argument_list|)
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ReportingStoreException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|XmlPullParserException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ReportingStoreException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|fileReader
argument_list|)
expr_stmt|;
block|}
block|}
name|reports
operator|.
name|put
argument_list|(
name|repository
argument_list|,
name|database
argument_list|)
expr_stmt|;
block|}
return|return
name|database
return|;
block|}
specifier|public
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
block|{
name|database
operator|.
name|updateTimings
argument_list|()
expr_stmt|;
name|ReportingXpp3Writer
name|writer
init|=
operator|new
name|ReportingXpp3Writer
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"report-database.xml"
argument_list|)
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Writing reports to "
operator|+
name|file
argument_list|)
expr_stmt|;
name|FileWriter
name|fileWriter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|fileWriter
operator|=
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|fileWriter
argument_list|,
name|database
operator|.
name|getReporting
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ReportingStoreException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|fileWriter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

