begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|PluginsService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * @author Eric Barboni  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"pluginsService#rest"
argument_list|)
specifier|public
class|class
name|DefaultPluginsServices
implements|implements
name|PluginsService
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|repositoryType
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|adminFeatures
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ApplicationContext
name|appCont
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|DefaultPluginsServices
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"appCont"
argument_list|)
expr_stmt|;
name|this
operator|.
name|appCont
operator|=
name|applicationContext
expr_stmt|;
block|}
specifier|private
name|void
name|feed
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repository
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"feeed"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Resource
index|[]
name|xmlResources
decl_stmt|;
try|try
block|{
name|xmlResources
operator|=
name|appCont
operator|.
name|getResources
argument_list|(
literal|"/**/"
operator|+
name|key
operator|+
literal|"/**/main.js"
argument_list|)
expr_stmt|;
for|for
control|(
name|Resource
name|rc
range|:
name|xmlResources
control|)
block|{
name|String
name|tmp
init|=
name|rc
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|tmp
operator|=
name|tmp
operator|.
name|substring
argument_list|(
name|tmp
operator|.
name|lastIndexOf
argument_list|(
name|key
argument_list|)
operator|+
name|key
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|,
name|tmp
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
expr_stmt|;
name|repository
operator|.
name|add
argument_list|(
literal|"archiva/admin/"
operator|+
name|key
operator|+
literal|"/"
operator|+
name|tmp
operator|+
literal|"/main"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
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
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAdminPlugins
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
comment|// rebuild
name|feed
argument_list|(
name|repositoryType
argument_list|,
literal|"repository"
argument_list|)
expr_stmt|;
name|feed
argument_list|(
name|adminFeatures
argument_list|,
literal|"features"
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoType
range|:
name|repositoryType
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|repoType
argument_list|)
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|repoType
range|:
name|adminFeatures
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|repoType
argument_list|)
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"sb"
operator|+
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
return|return
name|sb
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|sb
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

