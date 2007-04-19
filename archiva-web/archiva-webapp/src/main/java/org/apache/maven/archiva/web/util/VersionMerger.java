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
name|web
operator|.
name|util
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|//import org.apache.maven.archiva.indexer.record.StandardArtifactIndexRecord;
end_comment

begin_comment
comment|//import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
end_comment

begin_comment
comment|//import org.apache.maven.model.Dependency;
end_comment

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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|List
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
specifier|public
class|class
name|VersionMerger
block|{
specifier|public
specifier|static
name|List
comment|/*<DependencyWrapper>*/
name|wrap
parameter_list|(
name|List
comment|/*<StandardArtifactIndexRecord>*/
name|artifacts
parameter_list|)
block|{
name|List
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|//        for ( Iterator i = artifacts.iterator(); i.hasNext(); )
comment|//        {
comment|//            Dependency dependency = (Dependency) i.next();
comment|//
comment|//            dependencies.add( new DependencyWrapper( dependency ) );
comment|//        }
return|return
name|dependencies
return|;
block|}
specifier|public
specifier|static
name|Collection
comment|/*<DependencyWrapper*/
name|merge
parameter_list|(
name|Collection
comment|/*<StandardArtifactIndexRecord>*/
name|artifacts
parameter_list|)
block|{
name|Map
name|dependees
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
comment|//        for ( Iterator i = artifacts.iterator(); i.hasNext(); )
comment|//        {
comment|//            StandardArtifactIndexRecord record = (StandardArtifactIndexRecord) i.next();
comment|//
comment|//            String key = record.getGroupId() + ":" + record.getArtifactId();
comment|//            if ( dependees.containsKey( key ) )
comment|//            {
comment|//                DependencyWrapper wrapper = (DependencyWrapper) dependees.get( key );
comment|//                wrapper.addVersion( record.getVersion() );
comment|//            }
comment|//            else
comment|//            {
comment|//                DependencyWrapper wrapper = new DependencyWrapper( record );
comment|//
comment|//                dependees.put( key, wrapper );
comment|//            }
comment|//        }
return|return
name|dependees
operator|.
name|values
argument_list|()
return|;
block|}
comment|//    public static class DependencyWrapper
comment|//    {
comment|//        private final String groupId;
comment|//
comment|//        private final String artifactId;
comment|//
comment|//        /**
comment|//         * Versions added. We ignore duplicates since you might add those with varying classifiers.
comment|//         */
comment|//        private Set versions = new HashSet();
comment|//
comment|//        private String version;
comment|//
comment|//        private String scope;
comment|//
comment|//        private String classifier;
comment|//
comment|//        public DependencyWrapper( StandardArtifactIndexRecord record )
comment|//        {
comment|//            this.groupId = record.getGroupId();
comment|//
comment|//            this.artifactId = record.getArtifactId();
comment|//
comment|//            addVersion( record.getVersion() );
comment|//        }
comment|//
comment|//        public DependencyWrapper( Dependency dependency )
comment|//        {
comment|//            this.groupId = dependency.getGroupId();
comment|//
comment|//            this.artifactId = dependency.getArtifactId();
comment|//
comment|//            this.scope = dependency.getScope();
comment|//
comment|//            this.classifier = dependency.getClassifier();
comment|//
comment|//            addVersion( dependency.getVersion() );
comment|//        }
comment|//
comment|//        public String getScope()
comment|//        {
comment|//            return scope;
comment|//        }
comment|//
comment|//        public String getClassifier()
comment|//        {
comment|//            return classifier;
comment|//        }
comment|//
comment|//        public void addVersion( String version )
comment|//        {
comment|//            // We use DefaultArtifactVersion to get the correct sorting order later, however it does not have
comment|//            // hashCode properly implemented, so we add it here.
comment|//            // TODO: add these methods to the actual DefaultArtifactVersion and use that.
comment|//            versions.add( new DefaultArtifactVersion( version )
comment|//            {
comment|//                public int hashCode()
comment|//                {
comment|//                    int result;
comment|//                    result = getBuildNumber();
comment|//                    result = 31 * result + getMajorVersion();
comment|//                    result = 31 * result + getMinorVersion();
comment|//                    result = 31 * result + getIncrementalVersion();
comment|//                    result = 31 * result + ( getQualifier() != null ? getQualifier().hashCode() : 0 );
comment|//                    return result;
comment|//                }
comment|//
comment|//                public boolean equals( Object o )
comment|//                {
comment|//                    if ( this == o )
comment|//                    {
comment|//                        return true;
comment|//                    }
comment|//                    if ( o == null || getClass() != o.getClass() )
comment|//                    {
comment|//                        return false;
comment|//                    }
comment|//
comment|//                    DefaultArtifactVersion that = (DefaultArtifactVersion) o;
comment|//
comment|//                    if ( getBuildNumber() != that.getBuildNumber() )
comment|//                    {
comment|//                        return false;
comment|//                    }
comment|//                    if ( getIncrementalVersion() != that.getIncrementalVersion() )
comment|//                    {
comment|//                        return false;
comment|//                    }
comment|//                    if ( getMajorVersion() != that.getMajorVersion() )
comment|//                    {
comment|//                        return false;
comment|//                    }
comment|//                    if ( getMinorVersion() != that.getMinorVersion() )
comment|//                    {
comment|//                        return false;
comment|//                    }
comment|//                    if ( getQualifier() != null ? !getQualifier().equals( that.getQualifier() )
comment|//                        : that.getQualifier() != null )
comment|//                    {
comment|//                        return false;
comment|//                    }
comment|//
comment|//                    return true;
comment|//                }
comment|//            } );
comment|//
comment|//            if ( versions.size() == 1 )
comment|//            {
comment|//                this.version = version;
comment|//            }
comment|//            else
comment|//            {
comment|//                this.version = null;
comment|//            }
comment|//        }
comment|//
comment|//        public String getGroupId()
comment|//        {
comment|//            return groupId;
comment|//        }
comment|//
comment|//        public String getArtifactId()
comment|//        {
comment|//            return artifactId;
comment|//        }
comment|//
comment|//        public List getVersions()
comment|//        {
comment|//            List versions = new ArrayList( this.versions );
comment|//            Collections.sort( versions );
comment|//            return versions;
comment|//        }
comment|//
comment|//        public String getVersion()
comment|//        {
comment|//            return version;
comment|//        }
comment|//    }
block|}
end_class

end_unit

