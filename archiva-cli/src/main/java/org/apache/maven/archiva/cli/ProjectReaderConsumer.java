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
name|cli
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
name|archiva
operator|.
name|consumers
operator|.
name|ConsumerException
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
name|consumers
operator|.
name|RepositoryContentConsumer
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
name|ArchivaProjectModel
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
name|ArchivaRepository
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
name|project
operator|.
name|ProjectModelException
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
name|project
operator|.
name|ProjectModelReader
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
name|project
operator|.
name|readers
operator|.
name|ProjectModel400Reader
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
name|util
operator|.
name|ArrayList
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
comment|/**  * ProjectReaderConsumer   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.consumers.RepositoryContentConsumer"  *                   role-hint="read-poms"  *                   instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ProjectReaderConsumer
extends|extends
name|AbstractProgressConsumer
implements|implements
name|RepositoryContentConsumer
block|{
comment|/**      * @plexus.configuration default-value="read-poms"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Read POMs and report anomolies."      */
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|ProjectModelReader
name|reader
decl_stmt|;
specifier|private
name|ArchivaRepository
name|repo
decl_stmt|;
specifier|private
name|List
name|includes
decl_stmt|;
specifier|public
name|ProjectReaderConsumer
parameter_list|()
block|{
name|reader
operator|=
operator|new
name|ProjectModel400Reader
argument_list|()
expr_stmt|;
name|includes
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|List
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|super
operator|.
name|beginScan
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repository
expr_stmt|;
block|}
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|super
operator|.
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|File
name|pomFile
init|=
operator|new
name|File
argument_list|(
name|repo
operator|.
name|getUrl
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
try|try
block|{
name|ArchivaProjectModel
name|model
init|=
name|reader
operator|.
name|read
argument_list|(
name|pomFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Got null model on "
operator|+
name|pomFile
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ProjectModelException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unable to process: "
operator|+
name|pomFile
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

