begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rss
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndFeed
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
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
name|Date
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
name|test
operator|.
name|ArchivaSpringJUnit4ClassRunner
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|RssFeedGeneratorTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
specifier|private
name|RssFeedGenerator
name|generator
decl_stmt|;
annotation|@
name|Before
specifier|public
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
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testNewFeed
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RssFeedEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|RssFeedEntry
argument_list|>
argument_list|()
decl_stmt|;
name|RssFeedEntry
name|entry
init|=
operator|new
name|RssFeedEntry
argument_list|(
literal|"Item 1"
argument_list|)
decl_stmt|;
name|Date
name|whenGathered
init|=
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|entry
operator|.
name|setDescription
argument_list|(
literal|"RSS 2.0 feed item 1."
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setPublishedDate
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|entry
operator|=
operator|new
name|RssFeedEntry
argument_list|(
literal|"Item 2"
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setDescription
argument_list|(
literal|"RSS 2.0 feed item 2."
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setPublishedDate
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|entry
operator|=
operator|new
name|RssFeedEntry
argument_list|(
literal|"Item 3"
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setDescription
argument_list|(
literal|"RSS 2.0 feed item 3."
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setPublishedDate
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|SyndFeed
name|feed
init|=
name|generator
operator|.
name|generateFeed
argument_list|(
literal|"Test Feed"
argument_list|,
literal|"The test feed from Archiva."
argument_list|,
name|entries
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Test Feed"
argument_list|,
name|feed
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The test feed from Archiva."
argument_list|,
name|feed
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"en-us"
argument_list|,
name|feed
operator|.
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getPublishedDate
argument_list|()
argument_list|,
name|feed
operator|.
name|getPublishedDate
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SyndEntry
argument_list|>
name|syndEntries
init|=
name|feed
operator|.
name|getEntries
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|syndEntries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Item 1"
argument_list|,
name|syndEntries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Item 2"
argument_list|,
name|syndEntries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Item 3"
argument_list|,
name|syndEntries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoDataEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RssFeedEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|RssFeedEntry
argument_list|>
argument_list|()
decl_stmt|;
name|SyndFeed
name|feed
init|=
name|generator
operator|.
name|generateFeed
argument_list|(
literal|"Test Feed"
argument_list|,
literal|"The test feed from Archiva."
argument_list|,
name|entries
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|feed
argument_list|)
expr_stmt|;
block|}
comment|/*      * this test might need to be removed since      * no updates are happening in the feeds anymore since everything's processed from the db.      *      public void testUpdateFeed()         throws Exception     {         generator.setRssDirectory( getBasedir() + "/target/test-classes/rss-feeds/" );          List<RssFeedEntry> entries = new ArrayList<RssFeedEntry>();         RssFeedEntry entry = new RssFeedEntry( "Item 1" );          entry.setDescription( "RSS 2.0 feed item 1." );         entries.add( entry );          entry = new RssFeedEntry( "Item 2" );         entry.setDescription( "RSS 2.0 feed item 2." );         entries.add( entry );          generator.generateFeed( "Test Feed", "The test feed from Archiva.", entries,                                 "generated-test-update-rss2.0-feed.xml" );          File outputFile = new File( getBasedir(), "/target/test-classes/rss-feeds/generated-test-update-rss2.0-feed.xml" );         String generatedContent = FileUtils.readFileToString( outputFile );          XMLAssert.assertXpathEvaluatesTo( "Test Feed", "//channel/title", generatedContent );         XMLAssert.assertXpathEvaluatesTo( "http://localhost:8080/archiva/rss/generated-test-update-rss2.0-feed.xml", "//channel/link", generatedContent );         XMLAssert.assertXpathEvaluatesTo( "The test feed from Archiva.", "//channel/description", generatedContent );         XMLAssert.assertXpathEvaluatesTo( "en-us", "//channel/language", generatedContent );          String expectedItem1 =             "<channel><item><title>Item 1</title></item><item><title>Item 2</title></item></channel>";                  XMLAssert.assertXpathsEqual( "//channel/item/title", expectedItem1, "//channel/item/title", generatedContent );          //update existing rss feed         entries = new ArrayList<RssFeedEntry>();         entry = new RssFeedEntry( "Item 3" );          entry.setDescription( "RSS 2.0 feed item 3." );         entries.add( entry );          entry = new RssFeedEntry( "Item 4" );         entry.setDescription( "RSS 2.0 feed item 4." );         entries.add( entry );          generator.generateFeed( "Test Feed", "The test feed from Archiva.", entries,                                 "generated-test-update-rss2.0-feed.xml" );                  outputFile = new File( getBasedir(), "/target/test-classes/rss-feeds/generated-test-update-rss2.0-feed.xml" );                 generatedContent = FileUtils.readFileToString( outputFile );                         XMLAssert.assertXpathEvaluatesTo( "Test Feed", "//channel/title", generatedContent );         XMLAssert.assertXpathEvaluatesTo( "http://localhost:8080/archiva/rss/generated-test-update-rss2.0-feed.xml", "//channel/link", generatedContent );         XMLAssert.assertXpathEvaluatesTo( "The test feed from Archiva.", "//channel/description", generatedContent );         XMLAssert.assertXpathEvaluatesTo( "en-us", "//channel/language", generatedContent );          expectedItem1 =             "<channel><item><title>Item 1</title></item><item><title>Item 2</title></item>"             + "<item><title>Item 3</title></item><item><title>Item 4</title></item></channel>";         XMLAssert.assertXpathsEqual( "//channel/item/title", expectedItem1, "//channel/item/title", generatedContent );                  outputFile.deleteOnExit();     }      */
block|}
end_class

end_unit

