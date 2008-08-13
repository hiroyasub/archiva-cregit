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
name|startup
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
name|io
operator|.
name|IOUtils
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
name|io
operator|.
name|FileInputStream
import|;
end_import

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
name|zip
operator|.
name|GZIPInputStream
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

begin_comment
comment|/**  * BannerTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|BannerTest
extends|extends
name|TestCase
block|{
specifier|private
name|void
name|assertEncodeDecode
parameter_list|(
name|String
name|encoded
parameter_list|,
name|String
name|decoded
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Encoding: "
argument_list|,
name|encoded
argument_list|,
name|Banner
operator|.
name|encode
argument_list|(
name|decoded
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Decoding: "
argument_list|,
name|decoded
argument_list|,
name|Banner
operator|.
name|decode
argument_list|(
name|encoded
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEncodeDecode
parameter_list|()
block|{
name|assertEncodeDecode
argument_list|(
literal|"[$10 ]"
argument_list|,
literal|"[          ]"
argument_list|)
expr_stmt|;
name|assertEncodeDecode
argument_list|(
literal|"$$$5_$n$5_"
argument_list|,
literal|"$_____\n_____"
argument_list|)
expr_stmt|;
name|assertEncodeDecode
argument_list|(
literal|"$${Refgjuvyr}"
argument_list|,
literal|"${Erstwhile}"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInjectVersion
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"[ 1.0 ]"
argument_list|,
name|Banner
operator|.
name|injectVersion
argument_list|(
literal|"[#####]"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|".\\  1.0-SNAPSHOT  \\._____"
argument_list|,
name|Banner
operator|.
name|injectVersion
argument_list|(
literal|".\\################\\._____"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Archiva:\n ( 1.0-alpha-1  )"
argument_list|,
name|Banner
operator|.
name|injectVersion
argument_list|(
literal|"Archiva:\n (##############)"
argument_list|,
literal|"1.0-alpha-1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetBanner
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|version
init|=
literal|"1.0-alpha-1-SNAPSHOT"
decl_stmt|;
name|String
name|banner
init|=
name|Banner
operator|.
name|getBanner
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Banner should not be null."
argument_list|,
name|banner
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Banner contains version."
argument_list|,
name|banner
operator|.
name|indexOf
argument_list|(
name|version
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|/* Want to make a new banner?          * Steps to do it.          * 1) Edit the src/test/resources/banner.gz file.          * 2) Save it compressed.          * 3) Add (to this test method) ...          *    System.out.println( "\"" + Banner.encode( getRawBanner() ) + "\"" );          * 4) Run the test          * 5) Copy / Paste the encoded form into the Banner.getBanner() method.          */
block|}
specifier|public
name|String
name|getRawBanner
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|gzBanner
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/banner.gz"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File ["
operator|+
name|gzBanner
operator|.
name|getPath
argument_list|()
operator|+
literal|"] not found."
argument_list|,
name|gzBanner
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|gzBanner
argument_list|)
decl_stmt|;
name|GZIPInputStream
name|gzis
init|=
operator|new
name|GZIPInputStream
argument_list|(
name|fis
argument_list|)
decl_stmt|;
return|return
name|IOUtils
operator|.
name|toString
argument_list|(
name|gzis
argument_list|)
return|;
block|}
block|}
end_class

end_unit

