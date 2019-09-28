begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|xml
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
comment|/**  * XMLReaderTest   *  *  */
end_comment

begin_class
specifier|public
class|class
name|XMLReaderTest
extends|extends
name|AbstractArchivaXmlTestCase
block|{
specifier|private
name|void
name|assertElementTexts
parameter_list|(
name|List
argument_list|<
name|Node
argument_list|>
name|elementList
parameter_list|,
name|String
index|[]
name|expectedTexts
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Element List Size"
argument_list|,
name|expectedTexts
operator|.
name|length
argument_list|,
name|elementList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|texts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Node
name|element
range|:
name|elementList
control|)
block|{
name|texts
operator|.
name|add
argument_list|(
name|element
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedTexts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|expectedText
init|=
name|expectedTexts
index|[
name|i
index|]
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Contains ["
operator|+
name|expectedText
operator|+
literal|"]"
argument_list|,
name|texts
operator|.
name|contains
argument_list|(
name|expectedText
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoPrologBasicRead
parameter_list|()
throws|throws
name|XMLException
block|{
name|Path
name|xmlFile
init|=
name|getExampleXml
argument_list|(
literal|"no-prolog-basic.xml"
argument_list|)
decl_stmt|;
name|XMLReader
name|reader
init|=
operator|new
name|XMLReader
argument_list|(
literal|"basic"
argument_list|,
name|xmlFile
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Node
argument_list|>
name|fruits
init|=
name|reader
operator|.
name|getElementList
argument_list|(
literal|"//basic/fruits/fruit"
argument_list|)
decl_stmt|;
name|assertElementTexts
argument_list|(
name|fruits
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"apple"
block|,
literal|"cherry"
block|,
literal|"pear"
block|,
literal|"peach"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoPrologEntitiesRead
parameter_list|()
throws|throws
name|XMLException
block|{
name|Path
name|xmlFile
init|=
name|getExampleXml
argument_list|(
literal|"no-prolog-with-entities.xml"
argument_list|)
decl_stmt|;
name|XMLReader
name|reader
init|=
operator|new
name|XMLReader
argument_list|(
literal|"basic"
argument_list|,
name|xmlFile
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Node
argument_list|>
name|names
init|=
name|reader
operator|.
name|getElementList
argument_list|(
literal|"//basic/names/name"
argument_list|)
decl_stmt|;
name|assertElementTexts
argument_list|(
name|names
argument_list|,
operator|new
name|String
index|[]
block|{
name|TRYGVIS
block|,
name|INFINITE_ARCHIVA
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoPrologUtf8Read
parameter_list|()
throws|throws
name|XMLException
block|{
name|Path
name|xmlFile
init|=
name|getExampleXml
argument_list|(
literal|"no-prolog-with-utf8.xml"
argument_list|)
decl_stmt|;
name|XMLReader
name|reader
init|=
operator|new
name|XMLReader
argument_list|(
literal|"basic"
argument_list|,
name|xmlFile
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Node
argument_list|>
name|names
init|=
name|reader
operator|.
name|getElementList
argument_list|(
literal|"//basic/names/name"
argument_list|)
decl_stmt|;
name|assertElementTexts
argument_list|(
name|names
argument_list|,
operator|new
name|String
index|[]
block|{
name|TRYGVIS
block|,
name|INFINITE_ARCHIVA
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrologUtf8Read
parameter_list|()
throws|throws
name|XMLException
block|{
name|Path
name|xmlFile
init|=
name|getExampleXml
argument_list|(
literal|"prolog-with-utf8.xml"
argument_list|)
decl_stmt|;
name|XMLReader
name|reader
init|=
operator|new
name|XMLReader
argument_list|(
literal|"basic"
argument_list|,
name|xmlFile
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Node
argument_list|>
name|names
init|=
name|reader
operator|.
name|getElementList
argument_list|(
literal|"//basic/names/name"
argument_list|)
decl_stmt|;
name|assertElementTexts
argument_list|(
name|names
argument_list|,
operator|new
name|String
index|[]
block|{
name|TRYGVIS
block|,
name|INFINITE_ARCHIVA
block|}
argument_list|)
expr_stmt|;
block|}
comment|// MRM-1136
annotation|@
name|Test
specifier|public
name|void
name|testProxiedMetadataRead
parameter_list|()
throws|throws
name|XMLException
block|{
name|Path
name|xmlFile
init|=
name|getExampleXml
argument_list|(
literal|"maven-metadata-codehaus-snapshots.xml"
argument_list|)
decl_stmt|;
name|XMLReader
name|reader
init|=
operator|new
name|XMLReader
argument_list|(
literal|"metadata"
argument_list|,
name|xmlFile
argument_list|)
decl_stmt|;
name|reader
operator|.
name|removeNamespaces
argument_list|()
expr_stmt|;
name|Element
name|groupId
init|=
operator|(
name|Element
operator|)
name|reader
operator|.
name|getElement
argument_list|(
literal|"//metadata/groupId"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.codehaus.mojo"
argument_list|,
name|groupId
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

