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
name|repository
operator|.
name|layout
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|ArtifactReference
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
name|ProjectReference
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
name|VersionedReference
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

begin_comment
comment|/**  * DefaultBidirectionalRepositoryLayoutTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DefaultBidirectionalRepositoryLayoutTest
extends|extends
name|AbstractBidirectionalRepositoryLayoutTestCase
block|{
class|class
name|LayoutExample
block|{
specifier|public
name|String
name|groupId
decl_stmt|;
specifier|public
name|String
name|artifactId
decl_stmt|;
specifier|public
name|String
name|version
decl_stmt|;
specifier|public
name|String
name|classifier
decl_stmt|;
specifier|public
name|String
name|type
decl_stmt|;
specifier|public
name|String
name|pathArtifact
decl_stmt|;
specifier|public
name|String
name|pathVersiond
decl_stmt|;
specifier|public
name|String
name|pathProjectd
decl_stmt|;
specifier|public
name|LayoutExample
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|void
name|setDelimitedPath
parameter_list|(
name|String
name|delimPath
parameter_list|)
block|{
comment|// Silly Test Writer! Don't end the path with a slash!
if|if
condition|(
name|delimPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|delimPath
operator|=
name|delimPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|delimPath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|parts
index|[]
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|delimPath
argument_list|,
literal|'|'
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|parts
operator|.
name|length
condition|)
block|{
case|case
literal|3
case|:
name|this
operator|.
name|pathArtifact
operator|=
name|parts
index|[
literal|0
index|]
operator|+
literal|"/"
operator|+
name|parts
index|[
literal|1
index|]
operator|+
literal|"/"
operator|+
name|parts
index|[
literal|2
index|]
expr_stmt|;
case|case
literal|2
case|:
name|this
operator|.
name|pathVersiond
operator|=
name|parts
index|[
literal|0
index|]
operator|+
literal|"/"
operator|+
name|parts
index|[
literal|1
index|]
operator|+
literal|"/maven-metadata.xml"
expr_stmt|;
case|case
literal|1
case|:
name|this
operator|.
name|pathProjectd
operator|=
name|parts
index|[
literal|0
index|]
operator|+
literal|"/maven-metadata.xml"
expr_stmt|;
break|break;
default|default:
name|fail
argument_list|(
literal|"Unknown number of path pieces, expected between 1 and 3, got<"
operator|+
name|parts
operator|.
name|length
operator|+
literal|"> on<"
operator|+
name|delimPath
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isSuitableForArtifactTest
parameter_list|()
block|{
return|return
operator|(
name|this
operator|.
name|type
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|this
operator|.
name|classifier
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|this
operator|.
name|version
operator|!=
literal|null
operator|)
return|;
block|}
specifier|public
name|boolean
name|isSuitableForVersionedTest
parameter_list|()
block|{
return|return
operator|(
name|this
operator|.
name|type
operator|==
literal|null
operator|)
operator|&&
operator|(
name|this
operator|.
name|classifier
operator|==
literal|null
operator|)
operator|&&
operator|(
name|this
operator|.
name|version
operator|!=
literal|null
operator|)
return|;
block|}
specifier|public
name|boolean
name|isSuitableForProjectTest
parameter_list|()
block|{
return|return
operator|(
name|this
operator|.
name|type
operator|==
literal|null
operator|)
operator|&&
operator|(
name|this
operator|.
name|classifier
operator|==
literal|null
operator|)
operator|&&
operator|(
name|this
operator|.
name|version
operator|==
literal|null
operator|)
return|;
block|}
block|}
class|class
name|InvalidExample
block|{
specifier|public
name|String
name|path
decl_stmt|;
specifier|public
name|String
name|reason
decl_stmt|;
specifier|public
name|boolean
name|hasFilename
decl_stmt|;
specifier|public
name|InvalidExample
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|hasFilename
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|hasFilename
operator|=
name|hasFilename
expr_stmt|;
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
block|}
specifier|private
name|BidirectionalRepositoryLayout
name|layout
decl_stmt|;
specifier|public
name|List
comment|/*<LayoutExample>*/
name|getGoodExamples
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|LayoutExample
name|example
decl_stmt|;
comment|// Artifact References
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-tool"
argument_list|,
literal|"1.0"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-tool|1.0|foo-tool-1.0.jar"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-client"
argument_list|,
literal|"1.0"
argument_list|,
literal|null
argument_list|,
literal|"ejb-client"
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-client|1.0|foo-client-1.0.jar"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo.lib"
argument_list|,
literal|"foo-lib"
argument_list|,
literal|"2.1-alpha-1"
argument_list|,
literal|"sources"
argument_list|,
literal|"java-source"
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/lib/foo-lib|2.1-alpha-1|foo-lib-2.1-alpha-1-sources.jar"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-connector"
argument_list|,
literal|"2.1-20060822.123456-35"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-connector/2.1-SNAPSHOT/foo-connector-2.1-20060822.123456-35.jar"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"org.apache.maven.test"
argument_list|,
literal|"get-metadata-snapshot"
argument_list|,
literal|"1.0-20050831.101112-1"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"org/apache/maven/test/get-metadata-snapshot|1.0-SNAPSHOT|get-metadata-snapshot-1.0-20050831.101112-1.jar"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.1"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"commons-lang/commons-lang|2.1|commons-lang-2.1.jar"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-tool"
argument_list|,
literal|"1.0"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-tool|1.0|foo-tool-1.0.jar"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
comment|// Versioned References (done here by setting classifier and type to null)
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-tool"
argument_list|,
literal|"1.0"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-tool|1.0"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"net.i.have.a.really.long.path.just.for.the.hell.of.it"
argument_list|,
literal|"a"
argument_list|,
literal|"1.1-alpha-1"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"net/i/have/a/really/long/path/just/for/the/hell/of/it/a|1.1-alpha-1"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-connector"
argument_list|,
literal|"2.1-20060822.123456-35"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-connector|2.1-SNAPSHOT"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-connector"
argument_list|,
literal|"2.1-SNAPSHOT"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-connector|2.1-SNAPSHOT"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
comment|// Project References (done here by setting version, classifier, and type to null)
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-tool"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-tool/"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"net.i.have.a.really.long.path.just.for.the.hell.of.it"
argument_list|,
literal|"a"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"net/i/have/a/really/long/path/just/for/the/hell/of/it/a/"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|LayoutExample
argument_list|(
literal|"com.foo"
argument_list|,
literal|"foo-connector"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|example
operator|.
name|setDelimitedPath
argument_list|(
literal|"com/foo/foo-connector"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|List
comment|/*<InvalidExample>*/
name|getInvalidPaths
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|InvalidExample
name|example
decl_stmt|;
name|example
operator|=
operator|new
name|InvalidExample
argument_list|(
literal|"invalid/invalid/1/invalid-1"
argument_list|,
literal|false
argument_list|,
literal|"missing type"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|InvalidExample
argument_list|(
literal|"invalid/invalid/1.0-SNAPSHOT/invalid-1.0.jar"
argument_list|,
literal|true
argument_list|,
literal|"non snapshot artifact inside of a snapshot dir"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|InvalidExample
argument_list|(
literal|"invalid/invalid-1.0.jar"
argument_list|,
literal|true
argument_list|,
literal|"path is too short"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|InvalidExample
argument_list|(
literal|"invalid/invalid/1.0-20050611.123456-1/invalid-1.0-20050611.123456-1.jar"
argument_list|,
literal|true
argument_list|,
literal|"Timestamped Snapshot artifact not inside of an Snapshot dir"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|InvalidExample
argument_list|(
literal|"invalid/invalid/1.0/invalid-2.0.jar"
argument_list|,
literal|true
argument_list|,
literal|"version mismatch between path and artifact"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|InvalidExample
argument_list|(
literal|"invalid/invalid/1.0/invalid-1.0b.jar"
argument_list|,
literal|true
argument_list|,
literal|"version mismatch between path and artifact"
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|example
argument_list|)
expr_stmt|;
name|example
operator|=
operator|new
name|InvalidExample
argument_list|(
literal|"org/apache/maven/test/1.0-SNAPSHOT/wrong-artifactId-1.0-20050611.112233-1.jar"
argument_list|,
literal|true
argument_list|,
literal|"wrong artifact id"
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|testArtifactToPath
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForArtifactTest
argument_list|()
condition|)
block|{
name|ArchivaArtifact
name|artifact
init|=
name|createArtifact
argument_list|(
name|example
operator|.
name|groupId
argument_list|,
name|example
operator|.
name|artifactId
argument_list|,
name|example
operator|.
name|version
argument_list|,
name|example
operator|.
name|classifier
argument_list|,
name|example
operator|.
name|type
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Artifact<"
operator|+
name|artifact
operator|+
literal|"> to path:"
argument_list|,
name|example
operator|.
name|pathArtifact
argument_list|,
name|layout
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testArtifactReferenceToPath
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForArtifactTest
argument_list|()
condition|)
block|{
name|ArtifactReference
name|reference
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
name|example
operator|.
name|groupId
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|example
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setVersion
argument_list|(
name|example
operator|.
name|version
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setClassifier
argument_list|(
name|example
operator|.
name|classifier
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setType
argument_list|(
name|example
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArtifactReference<"
operator|+
name|reference
operator|+
literal|"> to path:"
argument_list|,
name|example
operator|.
name|pathArtifact
argument_list|,
name|layout
operator|.
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testVersionedReferenceToPath
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForVersionedTest
argument_list|()
operator|||
name|example
operator|.
name|isSuitableForArtifactTest
argument_list|()
condition|)
block|{
name|VersionedReference
name|reference
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
name|example
operator|.
name|groupId
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|example
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setVersion
argument_list|(
name|example
operator|.
name|version
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"VersionedReference<"
operator|+
name|reference
operator|+
literal|"> to path:"
argument_list|,
name|example
operator|.
name|pathVersiond
argument_list|,
name|layout
operator|.
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testProjectReferenceToPath
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForProjectTest
argument_list|()
operator|||
name|example
operator|.
name|isSuitableForVersionedTest
argument_list|()
operator|||
name|example
operator|.
name|isSuitableForArtifactTest
argument_list|()
condition|)
block|{
name|ProjectReference
name|reference
init|=
operator|new
name|ProjectReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
name|example
operator|.
name|groupId
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|example
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ProjectReference<"
operator|+
name|reference
operator|+
literal|"> to path:"
argument_list|,
name|example
operator|.
name|pathProjectd
argument_list|,
name|layout
operator|.
name|toPath
argument_list|(
name|reference
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testInvalidPathToArtifact
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|getInvalidPaths
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|InvalidExample
name|example
init|=
operator|(
name|InvalidExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|layout
operator|.
name|toArtifact
argument_list|(
name|example
operator|.
name|path
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException on the invalid path ["
operator|+
name|example
operator|.
name|path
operator|+
literal|"] because of ["
operator|+
name|example
operator|.
name|reason
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
block|}
specifier|public
name|void
name|testInvalidPathToArtifactReference
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|getInvalidPaths
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|InvalidExample
name|example
init|=
operator|(
name|InvalidExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|layout
operator|.
name|toArtifactReference
argument_list|(
name|example
operator|.
name|path
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException on the invalid path ["
operator|+
name|example
operator|.
name|path
operator|+
literal|"] because of ["
operator|+
name|example
operator|.
name|reason
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
block|}
specifier|public
name|void
name|testInvalidPathToVersionedReference
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|getInvalidPaths
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|InvalidExample
name|example
init|=
operator|(
name|InvalidExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|layout
operator|.
name|toVersionedReference
argument_list|(
name|example
operator|.
name|path
argument_list|)
expr_stmt|;
if|if
condition|(
name|example
operator|.
name|hasFilename
condition|)
block|{
name|fail
argument_list|(
literal|"Should have thrown a LayoutException on the invalid path ["
operator|+
name|example
operator|.
name|path
operator|+
literal|"] because of ["
operator|+
name|example
operator|.
name|reason
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
block|}
specifier|public
name|void
name|testInvalidPathToProjectReference
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|getInvalidPaths
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|InvalidExample
name|example
init|=
operator|(
name|InvalidExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|layout
operator|.
name|toProjectReference
argument_list|(
name|example
operator|.
name|path
argument_list|)
expr_stmt|;
if|if
condition|(
name|example
operator|.
name|hasFilename
condition|)
block|{
name|fail
argument_list|(
literal|"Should have thrown a LayoutException on the invalid path ["
operator|+
name|example
operator|.
name|path
operator|+
literal|"] because of ["
operator|+
name|example
operator|.
name|reason
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
block|}
specifier|public
name|void
name|testPathToArtifact
parameter_list|()
throws|throws
name|LayoutException
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForArtifactTest
argument_list|()
condition|)
block|{
name|ArchivaArtifact
name|artifact
init|=
name|layout
operator|.
name|toArtifact
argument_list|(
name|example
operator|.
name|pathArtifact
argument_list|)
decl_stmt|;
name|assertArtifact
argument_list|(
name|artifact
argument_list|,
name|example
operator|.
name|groupId
argument_list|,
name|example
operator|.
name|artifactId
argument_list|,
name|example
operator|.
name|version
argument_list|,
name|example
operator|.
name|classifier
argument_list|,
name|example
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testPathToArtifactReference
parameter_list|()
throws|throws
name|LayoutException
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForArtifactTest
argument_list|()
condition|)
block|{
name|ArtifactReference
name|reference
init|=
name|layout
operator|.
name|toArtifactReference
argument_list|(
name|example
operator|.
name|pathArtifact
argument_list|)
decl_stmt|;
name|assertArtifactReference
argument_list|(
name|reference
argument_list|,
name|example
operator|.
name|groupId
argument_list|,
name|example
operator|.
name|artifactId
argument_list|,
name|example
operator|.
name|version
argument_list|,
name|example
operator|.
name|classifier
argument_list|,
name|example
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testPathToVersionedReference
parameter_list|()
throws|throws
name|LayoutException
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForVersionedTest
argument_list|()
condition|)
block|{
name|VersionedReference
name|reference
init|=
name|layout
operator|.
name|toVersionedReference
argument_list|(
name|example
operator|.
name|pathVersiond
argument_list|)
decl_stmt|;
name|String
name|baseVersion
init|=
name|reference
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|assertVersionedReference
argument_list|(
name|reference
argument_list|,
name|example
operator|.
name|groupId
argument_list|,
name|example
operator|.
name|artifactId
argument_list|,
name|baseVersion
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testPathToProjectReference
parameter_list|()
throws|throws
name|LayoutException
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForProjectTest
argument_list|()
condition|)
block|{
name|ProjectReference
name|reference
init|=
name|layout
operator|.
name|toProjectReference
argument_list|(
name|example
operator|.
name|pathProjectd
argument_list|)
decl_stmt|;
name|assertProjectReference
argument_list|(
name|reference
argument_list|,
name|example
operator|.
name|groupId
argument_list|,
name|example
operator|.
name|artifactId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testRoundtripArtifactToPathToArtifact
parameter_list|()
throws|throws
name|LayoutException
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForArtifactTest
argument_list|()
condition|)
block|{
name|ArchivaArtifact
name|artifact
init|=
name|createArtifact
argument_list|(
name|example
operator|.
name|groupId
argument_list|,
name|example
operator|.
name|artifactId
argument_list|,
name|example
operator|.
name|version
argument_list|,
name|example
operator|.
name|classifier
argument_list|,
name|example
operator|.
name|type
argument_list|)
decl_stmt|;
name|String
name|testPath
init|=
name|layout
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Artifact<"
operator|+
name|artifact
operator|+
literal|"> to path:"
argument_list|,
name|example
operator|.
name|pathArtifact
argument_list|,
name|testPath
argument_list|)
expr_stmt|;
name|ArchivaArtifact
name|testArtifact
init|=
name|layout
operator|.
name|toArtifact
argument_list|(
name|testPath
argument_list|)
decl_stmt|;
name|assertArtifact
argument_list|(
name|testArtifact
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testRoundtripPathToArtifactToPath
parameter_list|()
throws|throws
name|LayoutException
block|{
name|Iterator
name|it
init|=
name|getGoodExamples
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|LayoutExample
name|example
init|=
operator|(
name|LayoutExample
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|example
operator|.
name|isSuitableForArtifactTest
argument_list|()
condition|)
block|{
name|ArchivaArtifact
name|artifact
init|=
name|layout
operator|.
name|toArtifact
argument_list|(
name|example
operator|.
name|pathArtifact
argument_list|)
decl_stmt|;
name|assertArtifact
argument_list|(
name|artifact
argument_list|,
name|example
operator|.
name|groupId
argument_list|,
name|example
operator|.
name|artifactId
argument_list|,
name|example
operator|.
name|version
argument_list|,
name|example
operator|.
name|classifier
argument_list|,
name|example
operator|.
name|type
argument_list|)
expr_stmt|;
name|String
name|testPath
init|=
name|layout
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Artifact<"
operator|+
name|artifact
operator|+
literal|"> to path:"
argument_list|,
name|example
operator|.
name|pathArtifact
argument_list|,
name|testPath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testTimestampedSnapshotRoundtrip
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|originalPath
init|=
literal|"org/apache/maven/test/get-metadata-snapshot/1.0-SNAPSHOT/get-metadata-snapshot-1.0-20050831.101112-1.jar"
decl_stmt|;
name|ArchivaArtifact
name|artifact
init|=
name|layout
operator|.
name|toArtifact
argument_list|(
name|originalPath
argument_list|)
decl_stmt|;
name|assertArtifact
argument_list|(
name|artifact
argument_list|,
literal|"org.apache.maven.test"
argument_list|,
literal|"get-metadata-snapshot"
argument_list|,
literal|"1.0-20050831.101112-1"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|originalPath
argument_list|,
name|layout
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
name|ArtifactReference
name|aref
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|aref
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|aref
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|aref
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|aref
operator|.
name|setClassifier
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|aref
operator|.
name|setType
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|originalPath
argument_list|,
name|layout
operator|.
name|toPath
argument_list|(
name|aref
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|layout
operator|=
operator|(
name|BidirectionalRepositoryLayout
operator|)
name|lookup
argument_list|(
name|BidirectionalRepositoryLayout
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

