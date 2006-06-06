begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|manager
operator|.
name|web
operator|.
name|job
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepositoryFactory
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
name|DefaultArtifactRepositoryFactory
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
name|repository
operator|.
name|discovery
operator|.
name|ArtifactDiscoverer
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
name|repository
operator|.
name|discovery
operator|.
name|MetadataDiscoverer
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndexingFactory
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
name|repository
operator|.
name|manager
operator|.
name|web
operator|.
name|execution
operator|.
name|DiscovererExecution
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
name|scheduler
operator|.
name|Scheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|CronTrigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDetail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SchedulerException
import|;
end_import

begin_comment
comment|/**  * This class sets the job to be executed in the plexus-quartz scheduler  *  * @plexus.component role="org.apache.maven.repository.manager.web.job.DiscovererScheduler"  */
end_comment

begin_class
specifier|public
class|class
name|DiscovererScheduler
extends|extends
name|AbstractLogEnabled
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|Configuration
name|config
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|Scheduler
name|scheduler
decl_stmt|;
specifier|private
name|Properties
name|props
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|DiscovererExecution
name|execution
decl_stmt|;
comment|/**      * Method that sets the schedule in the plexus-quartz scheduler      *      * @throws IOException      * @throws ParseException      * @throws SchedulerException      */
specifier|public
name|void
name|setSchedule
parameter_list|()
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SchedulerException
block|{
name|props
operator|=
name|config
operator|.
name|getProperties
argument_list|()
expr_stmt|;
name|JobDetail
name|jobDetail
init|=
operator|new
name|JobDetail
argument_list|(
literal|"discovererJob"
argument_list|,
literal|"DISCOVERER"
argument_list|,
name|DiscovererJob
operator|.
name|class
argument_list|)
decl_stmt|;
name|JobDataMap
name|dataMap
init|=
operator|new
name|JobDataMap
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|put
argument_list|(
name|DiscovererJob
operator|.
name|LOGGER
argument_list|,
name|getLogger
argument_list|()
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|put
argument_list|(
name|DiscovererJob
operator|.
name|MAP_DISCOVERER_EXECUTION
argument_list|,
name|execution
argument_list|)
expr_stmt|;
name|jobDetail
operator|.
name|setJobDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|CronTrigger
name|trigger
init|=
operator|new
name|CronTrigger
argument_list|(
literal|"DiscovererTrigger"
argument_list|,
literal|"DISCOVERER"
argument_list|,
name|props
operator|.
name|getProperty
argument_list|(
literal|"cron.expression"
argument_list|)
argument_list|)
decl_stmt|;
name|scheduler
operator|.
name|scheduleJob
argument_list|(
name|jobDetail
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method that sets the configuration object      *      * @param config      */
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
comment|/**      * Returns the cofiguration      *      * @return a Configuration object that contains the configuration values      */
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

