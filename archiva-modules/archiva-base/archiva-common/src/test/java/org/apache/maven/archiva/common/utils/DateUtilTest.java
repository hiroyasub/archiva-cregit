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
name|common
operator|.
name|utils
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * DateUtilTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DateUtilTest
extends|extends
name|TestCase
block|{
specifier|private
name|void
name|assertDuration
parameter_list|(
name|String
name|expectedDuration
parameter_list|,
name|String
name|startTimestamp
parameter_list|,
name|String
name|endTimestamp
parameter_list|)
throws|throws
name|ParseException
block|{
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss SSS"
argument_list|)
decl_stmt|;
name|Date
name|startDate
init|=
name|sdf
operator|.
name|parse
argument_list|(
name|startTimestamp
argument_list|)
decl_stmt|;
name|Date
name|endDate
init|=
name|sdf
operator|.
name|parse
argument_list|(
name|endTimestamp
argument_list|)
decl_stmt|;
comment|//        System.out.println( "Date: " + endTimestamp + " - " + startTimestamp + " = "
comment|//                        + ( endDate.getTime() - startDate.getTime() ) + " ms" );
name|assertEquals
argument_list|(
name|expectedDuration
argument_list|,
name|DateUtil
operator|.
name|getDuration
argument_list|(
name|startDate
argument_list|,
name|endDate
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetDurationDifference
parameter_list|()
throws|throws
name|ParseException
block|{
name|assertDuration
argument_list|(
literal|"2 Seconds"
argument_list|,
literal|"2006-08-22 13:00:02 0000"
argument_list|,
literal|"2006-08-22 13:00:04 0000"
argument_list|)
expr_stmt|;
name|assertDuration
argument_list|(
literal|"12 Minutes 12 Seconds 234 Milliseconds"
argument_list|,
literal|"2006-08-22 13:12:02 0000"
argument_list|,
literal|"2006-08-22 13:24:14 0234"
argument_list|)
expr_stmt|;
name|assertDuration
argument_list|(
literal|"12 Minutes 501 Milliseconds"
argument_list|,
literal|"2006-08-22 13:12:01 0500"
argument_list|,
literal|"2006-08-22 13:24:02 0001"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetDurationDirect
parameter_list|()
throws|throws
name|ParseException
block|{
name|assertEquals
argument_list|(
literal|"2 Seconds"
argument_list|,
name|DateUtil
operator|.
name|getDuration
argument_list|(
literal|2000
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"12 Minutes 12 Seconds 234 Milliseconds"
argument_list|,
name|DateUtil
operator|.
name|getDuration
argument_list|(
literal|732234
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"12 Minutes 501 Milliseconds"
argument_list|,
name|DateUtil
operator|.
name|getDuration
argument_list|(
literal|720501
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

