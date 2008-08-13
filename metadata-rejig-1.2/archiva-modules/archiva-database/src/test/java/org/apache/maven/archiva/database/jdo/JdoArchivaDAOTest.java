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
name|AbstractArchivaDatabaseTestCase
import|;
end_import

begin_comment
comment|/**  * JdoArchivaDAOTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|JdoArchivaDAOTest
extends|extends
name|AbstractArchivaDatabaseTestCase
block|{
specifier|public
name|void
name|testSubDAOs
parameter_list|()
block|{
name|assertNotNull
argument_list|(
literal|"Artifact DAO"
argument_list|,
name|dao
operator|.
name|getArtifactDAO
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Project Model DAO"
argument_list|,
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Repository Problem DAO"
argument_list|,
name|dao
operator|.
name|getRepositoryProblemDAO
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

