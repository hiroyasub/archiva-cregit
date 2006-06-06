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
name|discovery
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
name|Artifact
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
name|repository
operator|.
name|ArtifactUtils
import|;
end_import

begin_comment
comment|/**  * Artifact discoverer for the new repository layout (Maven 2.0+).  *  * @author John Casey  * @author Brett Porter  * @plexus.component role="org.apache.maven.repository.discovery.ArtifactDiscoverer" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArtifactDiscoverer
extends|extends
name|AbstractArtifactDiscoverer
implements|implements
name|ArtifactDiscoverer
block|{
specifier|protected
name|Artifact
name|buildArtifactFromPath
parameter_list|(
name|String
name|path
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
block|{
return|return
name|ArtifactUtils
operator|.
name|buildArtifact
argument_list|(
name|path
argument_list|,
name|repository
argument_list|,
name|artifactFactory
argument_list|)
return|;
block|}
block|}
end_class

end_unit

