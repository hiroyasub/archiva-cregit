begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|base
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|*
import|;
end_import

begin_class
class|class
name|ArchivaItemSelectorTest
block|{
annotation|@
name|Test
name|void
name|getProjectId
parameter_list|( )
block|{
name|ArchivaItemSelector
name|selector
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withProjectId
argument_list|(
literal|"test-project-123"
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test-project-123"
argument_list|,
name|selector
operator|.
name|getProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|selector
operator|.
name|hasProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasAttributes
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|selector
operator|.
name|getAttributes
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|getNamespace
parameter_list|( )
block|{
name|ArchivaItemSelector
name|selector
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withNamespace
argument_list|(
literal|"abc.de.fg"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc.de.fg"
argument_list|,
name|selector
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasAttributes
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|selector
operator|.
name|getAttributes
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|getVersion
parameter_list|( )
block|{
name|ArchivaItemSelector
name|selector
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withVersion
argument_list|(
literal|"1.15.20.3"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1.15.20.3"
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|selector
operator|.
name|hasVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasAttributes
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|selector
operator|.
name|getAttributes
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|getArtifactVersion
parameter_list|( )
block|{
name|ArchivaItemSelector
name|selector
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withArtifactVersion
argument_list|(
literal|"5.13.2.4"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"5.13.2.4"
argument_list|,
name|selector
operator|.
name|getArtifactVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasAttributes
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|selector
operator|.
name|getAttributes
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|getArtifactId
parameter_list|( )
block|{
name|ArchivaItemSelector
name|selector
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withArtifactId
argument_list|(
literal|"xml-tools"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xml-tools"
argument_list|,
name|selector
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|selector
operator|.
name|hasArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasAttributes
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|selector
operator|.
name|getAttributes
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|getType
parameter_list|( )
block|{
name|ArchivaItemSelector
name|selector
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withType
argument_list|(
literal|"javadoc"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"javadoc"
argument_list|,
name|selector
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|selector
operator|.
name|hasType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasAttributes
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|selector
operator|.
name|getAttributes
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|getClassifier
parameter_list|( )
block|{
name|ArchivaItemSelector
name|selector
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withClassifier
argument_list|(
literal|"source"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"source"
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasAttributes
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getNamespace
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|selector
operator|.
name|getAttributes
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|getAttribute
parameter_list|( )
block|{
name|ArchivaItemSelector
name|selector
init|=
name|ArchivaItemSelector
operator|.
name|builder
argument_list|( )
operator|.
name|withAttribute
argument_list|(
literal|"test1"
argument_list|,
literal|"value1"
argument_list|)
operator|.
name|withAttribute
argument_list|(
literal|"test2"
argument_list|,
literal|"value2"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"value1"
argument_list|,
name|selector
operator|.
name|getAttribute
argument_list|(
literal|"test1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value2"
argument_list|,
name|selector
operator|.
name|getAttribute
argument_list|(
literal|"test2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasProjectId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|selector
operator|.
name|hasClassifier
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|selector
operator|.
name|hasAttributes
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|selector
operator|.
name|getClassifier
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

