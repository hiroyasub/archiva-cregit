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
name|util
operator|.
name|Calendar
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
name|GregorianCalendar
import|;
end_import

begin_comment
comment|/**  * DateUtil - some (not-so) common date utility methods.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DateUtil
block|{
specifier|public
specifier|static
name|String
name|getDuration
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
return|return
name|getDuration
argument_list|(
operator|new
name|Date
argument_list|(
literal|0
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
name|duration
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getDuration
parameter_list|(
name|long
name|ms1
parameter_list|,
name|long
name|ms2
parameter_list|)
block|{
return|return
name|getDuration
argument_list|(
operator|new
name|Date
argument_list|(
name|ms1
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
name|ms2
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getDuration
parameter_list|(
name|Date
name|d1
parameter_list|,
name|Date
name|d2
parameter_list|)
block|{
name|Calendar
name|cal1
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|cal1
operator|.
name|setTime
argument_list|(
name|d1
argument_list|)
expr_stmt|;
name|Calendar
name|cal2
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|cal2
operator|.
name|setTime
argument_list|(
name|d2
argument_list|)
expr_stmt|;
return|return
name|getDuration
argument_list|(
name|cal1
argument_list|,
name|cal2
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getDuration
parameter_list|(
name|Calendar
name|cal1
parameter_list|,
name|Calendar
name|cal2
parameter_list|)
block|{
name|int
name|year1
init|=
name|cal1
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
decl_stmt|;
name|int
name|day1
init|=
name|cal1
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_YEAR
argument_list|)
decl_stmt|;
name|int
name|hour1
init|=
name|cal1
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
decl_stmt|;
name|int
name|min1
init|=
name|cal1
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
decl_stmt|;
name|int
name|sec1
init|=
name|cal1
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
decl_stmt|;
name|int
name|ms1
init|=
name|cal1
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|)
decl_stmt|;
name|int
name|year2
init|=
name|cal2
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
decl_stmt|;
name|int
name|day2
init|=
name|cal2
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_YEAR
argument_list|)
decl_stmt|;
name|int
name|hour2
init|=
name|cal2
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
decl_stmt|;
name|int
name|min2
init|=
name|cal2
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
decl_stmt|;
name|int
name|sec2
init|=
name|cal2
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
decl_stmt|;
name|int
name|ms2
init|=
name|cal2
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|)
decl_stmt|;
name|int
name|leftDays
init|=
operator|(
name|day1
operator|-
name|day2
operator|)
operator|+
operator|(
name|year1
operator|-
name|year2
operator|)
operator|*
literal|365
decl_stmt|;
name|int
name|leftHours
init|=
name|hour2
operator|-
name|hour1
decl_stmt|;
name|int
name|leftMins
init|=
name|min2
operator|-
name|min1
decl_stmt|;
name|int
name|leftSeconds
init|=
name|sec2
operator|-
name|sec1
decl_stmt|;
name|int
name|leftMilliSeconds
init|=
name|ms2
operator|-
name|ms1
decl_stmt|;
if|if
condition|(
name|leftMilliSeconds
operator|<
literal|0
condition|)
block|{
name|leftMilliSeconds
operator|+=
literal|1000
expr_stmt|;
operator|--
name|leftSeconds
expr_stmt|;
block|}
if|if
condition|(
name|leftSeconds
operator|<
literal|0
condition|)
block|{
name|leftSeconds
operator|+=
literal|60
expr_stmt|;
operator|--
name|leftMins
expr_stmt|;
block|}
if|if
condition|(
name|leftMins
operator|<
literal|0
condition|)
block|{
name|leftMins
operator|+=
literal|60
expr_stmt|;
operator|--
name|leftHours
expr_stmt|;
block|}
if|if
condition|(
name|leftHours
operator|<
literal|0
condition|)
block|{
name|leftHours
operator|+=
literal|24
expr_stmt|;
operator|--
name|leftDays
expr_stmt|;
block|}
name|StringBuffer
name|interval
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|appendInterval
argument_list|(
name|interval
argument_list|,
name|leftDays
argument_list|,
literal|"Day"
argument_list|)
expr_stmt|;
name|appendInterval
argument_list|(
name|interval
argument_list|,
name|leftHours
argument_list|,
literal|"Hour"
argument_list|)
expr_stmt|;
name|appendInterval
argument_list|(
name|interval
argument_list|,
name|leftMins
argument_list|,
literal|"Minute"
argument_list|)
expr_stmt|;
name|appendInterval
argument_list|(
name|interval
argument_list|,
name|leftSeconds
argument_list|,
literal|"Second"
argument_list|)
expr_stmt|;
name|appendInterval
argument_list|(
name|interval
argument_list|,
name|leftMilliSeconds
argument_list|,
literal|"Millisecond"
argument_list|)
expr_stmt|;
return|return
name|interval
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|appendInterval
parameter_list|(
name|StringBuffer
name|interval
parameter_list|,
name|int
name|count
parameter_list|,
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|interval
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|interval
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|interval
operator|.
name|append
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|interval
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|count
operator|>
literal|1
condition|)
block|{
name|interval
operator|.
name|append
argument_list|(
literal|"s"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

