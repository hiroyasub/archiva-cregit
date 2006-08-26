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
comment|/**  * Gets the default implementation of a repository query layer for the given repository.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @version $Id$  * @plexus.component role="org.apache.maven.repository.reporting.RepositoryQueryLayerFactory"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRepositoryQueryLayerFactory
implements|implements
name|RepositoryQueryLayerFactory
block|{
specifier|public
name|RepositoryQueryLayer
name|createRepositoryQueryLayer
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
block|{
return|return
operator|new
name|DefaultRepositoryQueryLayer
argument_list|(
name|repository
argument_list|)
return|;
block|}
block|}
end_class

end_unit

