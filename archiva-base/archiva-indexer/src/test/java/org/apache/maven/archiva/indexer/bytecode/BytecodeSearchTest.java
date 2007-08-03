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
name|indexer
operator|.
name|bytecode
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
name|lucene
operator|.
name|search
operator|.
name|BooleanClause
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|MatchAllDocsQuery
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
name|indexer
operator|.
name|AbstractSearchTestCase
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
name|indexer
operator|.
name|ArtifactKeys
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
name|indexer
operator|.
name|RepositoryContentIndex
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
name|indexer
operator|.
name|RepositoryContentIndexFactory
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
name|indexer
operator|.
name|lucene
operator|.
name|LuceneIndexHandlers
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
name|ArchivaArtifact
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
name|HashMap
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

begin_comment
comment|/**  * BytecodeSearchTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|BytecodeSearchTest
extends|extends
name|AbstractSearchTestCase
block|{
specifier|public
name|String
name|getIndexName
parameter_list|()
block|{
return|return
literal|"bytecode"
return|;
block|}
specifier|public
name|LuceneIndexHandlers
name|getIndexHandler
parameter_list|()
block|{
return|return
operator|new
name|BytecodeHandlers
argument_list|()
return|;
block|}
specifier|public
name|RepositoryContentIndex
name|createIndex
parameter_list|(
name|RepositoryContentIndexFactory
name|indexFactory
parameter_list|,
name|ArchivaRepository
name|repository
parameter_list|)
block|{
return|return
name|indexFactory
operator|.
name|createBytecodeIndex
argument_list|(
name|repository
argument_list|)
return|;
block|}
specifier|protected
name|Map
name|createSampleRecordsMap
parameter_list|()
block|{
name|Map
name|records
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|Map
name|artifactDumps
init|=
name|getArchivaArtifactDumpMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifactDumps
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ArchivaArtifact
name|artifact
init|=
operator|(
name|ArchivaArtifact
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|File
name|dumpFile
init|=
name|getDumpFile
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|BytecodeRecord
name|record
init|=
name|BytecodeRecordLoader
operator|.
name|loadRecord
argument_list|(
name|dumpFile
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|record
operator|.
name|setRepositoryId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|record
argument_list|)
expr_stmt|;
block|}
return|return
name|records
return|;
block|}
specifier|public
name|void
name|testExactMatchVersionSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION_EXACT
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|}
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchVersionSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION_EXACT
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"continuum-webapp"
block|}
argument_list|,
literal|"1.0.3-SNAPSHOT"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchVersionAlphaSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION_EXACT
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"redback-authorization-open"
block|}
argument_list|,
literal|"1.0-alpha-1-SNAPSHOT"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchVersionTimestampedSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION_EXACT
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"wagon-provider-api"
block|}
argument_list|,
literal|"1.0-beta-3-20070209.213958-2"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchVersionInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatchNoResults
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION_EXACT
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchGroupIdOrgApacheMavenArchiva
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID_EXACT
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|}
argument_list|,
literal|"org.apache.maven.archiva"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchGroupIdOrgApacheMaven
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID_EXACT
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"maven-archetype-simple"
block|}
argument_list|,
literal|"org.apache.maven"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchGroupIdInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatchNoResults
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID_EXACT
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchArtifactIdArchivaCommon
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID_EXACT
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|}
argument_list|,
literal|"archiva-common"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchArtifactIdTestNg
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID_EXACT
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"testng"
block|}
argument_list|,
literal|"testng"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchArtifactIdInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatchNoResults
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID_EXACT
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchTypeJar
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|TYPE
argument_list|,
operator|(
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|,
literal|"redback-authorization-open"
block|,
literal|"testng"
block|,
literal|"wagon-provider-api"
block|}
operator|)
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExactMatchTypeWar
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatch
argument_list|(
name|ArtifactKeys
operator|.
name|TYPE
argument_list|,
operator|(
operator|new
name|String
index|[]
block|{
literal|"continuum-webapp"
block|}
operator|)
argument_list|,
literal|"war"
argument_list|)
expr_stmt|;
block|}
comment|/* TODO: Fix 'maven-plugin' type      public void testExactMatchTypePlugin() throws Exception      {      assertQueryExactMatch( ArtifactKeys.TYPE, ( new String[] { "maven-help-plugin" } ), "maven-plugin" );      } */
comment|/* TODO: Fix 'maven-archetype' type      public void testExactMatchTypeArchetype() throws Exception      {      assertQueryExactMatch( ArtifactKeys.TYPE, ( new String[] { "maven-archetype-simple" } ), "maven-archetype" );      }      */
specifier|public
name|void
name|testExactMatchTypeInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryExactMatchNoResults
argument_list|(
name|ArtifactKeys
operator|.
name|TYPE
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchGroupIdOrgApacheMaven
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|,
literal|"continuum-webapp"
block|,
literal|"maven-archetype-simple"
block|,
literal|"maven-help-plugin"
block|,
literal|"wagon-provider-api"
block|}
argument_list|,
literal|"org.apache.maven"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchGroupIdMaven
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|,
literal|"continuum-webapp"
block|,
literal|"maven-archetype-simple"
block|,
literal|"maven-help-plugin"
block|,
literal|"wagon-provider-api"
block|}
argument_list|,
literal|"maven"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchGroupIdMavenMixed
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|,
literal|"continuum-webapp"
block|,
literal|"maven-archetype-simple"
block|,
literal|"maven-help-plugin"
block|,
literal|"wagon-provider-api"
block|}
argument_list|,
literal|"Maven"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchGroupIdInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatchNoResults
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchArtifactIdPlugin
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"maven-help-plugin"
block|}
argument_list|,
literal|"plugin"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchArtifactIdMaven
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"maven-help-plugin"
block|,
literal|"maven-archetype-simple"
block|}
argument_list|,
literal|"maven"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchArtifactIdHelp
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"maven-help-plugin"
block|}
argument_list|,
literal|"help"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchVersionOne
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"daytrader-ear"
block|,
literal|"testng"
block|,
literal|"archiva-common"
block|,
literal|"redback-authorization-open"
block|,
literal|"maven-archetype-simple"
block|,
literal|"continuum-webapp"
block|,
literal|"wagon-provider-api"
block|}
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchVersionOneOh
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|,
literal|"continuum-webapp"
block|,
literal|"maven-archetype-simple"
block|,
literal|"redback-authorization-open"
block|,
literal|"wagon-provider-api"
block|}
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchVersionSnapshotLower
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"continuum-webapp"
block|,
literal|"redback-authorization-open"
block|}
argument_list|,
literal|"snapshot"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchVersionSnapshotUpper
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"continuum-webapp"
block|,
literal|"redback-authorization-open"
block|}
argument_list|,
literal|"SNAPSHOT"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchVersionAlpha
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"maven-archetype-simple"
block|,
literal|"redback-authorization-open"
block|}
argument_list|,
literal|"alpha"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchVersionOneAlpha
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"redback-authorization-open"
block|}
argument_list|,
literal|"1.0-alpha-1"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchVersionInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatchNoResults
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|,
literal|"255"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchClassifierNotJdk15
parameter_list|()
throws|throws
name|Exception
block|{
name|BooleanQuery
name|bQuery
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|bQuery
operator|.
name|add
argument_list|(
operator|new
name|MatchAllDocsQuery
argument_list|()
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST
argument_list|)
expr_stmt|;
name|bQuery
operator|.
name|add
argument_list|(
name|createMatchQuery
argument_list|(
name|ArtifactKeys
operator|.
name|CLASSIFIER
argument_list|,
literal|"jdk15"
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|MUST_NOT
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|search
argument_list|(
name|bQuery
argument_list|)
decl_stmt|;
name|assertResults
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"archiva-common"
block|,
literal|"continuum-webapp"
block|,
literal|"redback-authorization-open"
block|,
literal|"daytrader-ear"
block|,
literal|"maven-archetype-simple"
block|,
literal|"maven-help-plugin"
block|,
literal|"wagon-provider-api"
block|}
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchClassifierJdk15
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|ArtifactKeys
operator|.
name|CLASSIFIER
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"testng"
block|}
argument_list|,
literal|"jdk15"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchClassifierInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatchNoResults
argument_list|(
name|ArtifactKeys
operator|.
name|CLASSIFIER
argument_list|,
literal|"redo"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchClassSessionListener
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|BytecodeKeys
operator|.
name|CLASSES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"wagon-provider-api"
block|}
argument_list|,
literal|"wagon.events.SessionListener"
argument_list|)
expr_stmt|;
block|}
comment|/* TODO: Suffix searching does not seem to work.     public void testMatchClassUtil() throws Exception     {         assertQueryMatch( BytecodeKeys.CLASSES, new String[] { "archiva-common", "continuum-webapp", "testng",             "wagon-provider-api" }, "Util" );     }     */
specifier|public
name|void
name|testMatchClassWagon
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|BytecodeKeys
operator|.
name|CLASSES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"wagon-provider-api"
block|}
argument_list|,
literal|"Wagon"
argument_list|)
expr_stmt|;
block|}
comment|/* TODO: Suffix searching does not seem to work.     public void testMatchClassMojoAllUpper() throws Exception     {         assertQueryMatch( BytecodeKeys.CLASSES, new String[] { "maven-help-plugin" }, "MOJO" );     }     */
comment|/* TODO: Suffix searching does not seem to work.     public void testMatchClassMojo() throws Exception     {         assertQueryMatch( BytecodeKeys.CLASSES, new String[] { "maven-help-plugin" }, "Mojo" );     }     */
specifier|public
name|void
name|testMatchClassInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatchNoResults
argument_list|(
name|BytecodeKeys
operator|.
name|CLASSES
argument_list|,
literal|"Destruct|Button"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchFilesManifestMf
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|BytecodeKeys
operator|.
name|FILES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"daytrader-ear"
block|,
literal|"maven-archetype-simple"
block|,
literal|"redback-authorization-open"
block|,
literal|"maven-help-plugin"
block|,
literal|"archiva-common"
block|,
literal|"wagon-provider-api"
block|,
literal|"continuum-webapp"
block|,
literal|"testng"
block|}
argument_list|,
literal|"MANIFEST.MF"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchFilesMetaInf
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|BytecodeKeys
operator|.
name|FILES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"daytrader-ear"
block|,
literal|"maven-archetype-simple"
block|,
literal|"redback-authorization-open"
block|,
literal|"maven-help-plugin"
block|,
literal|"archiva-common"
block|,
literal|"wagon-provider-api"
block|,
literal|"continuum-webapp"
block|,
literal|"testng"
block|}
argument_list|,
literal|"META-INF"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchFilesPluginXml
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatch
argument_list|(
name|BytecodeKeys
operator|.
name|FILES
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"maven-help-plugin"
block|}
argument_list|,
literal|"plugin.xml"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchFilesInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|assertQueryMatchNoResults
argument_list|(
name|BytecodeKeys
operator|.
name|FILES
argument_list|,
literal|"Veni Vidi Castratavi Illegitimos"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

