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
name|database
operator|.
name|jdo
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|Constraint
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
name|database
operator|.
name|ObjectNotFoundException
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
name|database
operator|.
name|RepositoryContentStatisticsDAO
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
name|RepositoryContentStatistics
import|;
end_import

begin_comment
comment|/**  * JdoRepositoryContentStatisticsDAO   *  * @version  *   * @plexus.component role-hint="jdo"  */
end_comment

begin_class
specifier|public
class|class
name|JdoRepositoryContentStatisticsDAO
implements|implements
name|RepositoryContentStatisticsDAO
block|{
comment|/**      * @plexus.requirement role-hint="archiva"      */
specifier|private
name|JdoAccess
name|jdo
decl_stmt|;
specifier|public
name|void
name|deleteRepositoryContentStatistics
parameter_list|(
name|RepositoryContentStatistics
name|stats
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|jdo
operator|.
name|removeObject
argument_list|(
name|stats
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|queryRepositoryContentStatistics
parameter_list|(
name|Constraint
name|constraint
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|List
name|results
init|=
name|jdo
operator|.
name|queryObjects
argument_list|(
name|RepositoryContentStatistics
operator|.
name|class
argument_list|,
name|constraint
argument_list|)
decl_stmt|;
return|return
name|results
return|;
block|}
specifier|public
name|RepositoryContentStatistics
name|saveRepositoryContentStatistics
parameter_list|(
name|RepositoryContentStatistics
name|stats
parameter_list|)
block|{
name|RepositoryContentStatistics
name|savedStats
init|=
operator|(
name|RepositoryContentStatistics
operator|)
name|jdo
operator|.
name|saveObject
argument_list|(
name|stats
argument_list|)
decl_stmt|;
return|return
name|savedStats
return|;
block|}
block|}
end_class

end_unit

