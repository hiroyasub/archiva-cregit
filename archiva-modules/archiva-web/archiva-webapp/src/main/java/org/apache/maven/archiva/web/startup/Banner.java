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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

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
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Banner   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|Banner
block|{
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|raw
parameter_list|)
block|{
name|StringBuffer
name|encoded
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|rawlen
init|=
name|raw
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rawlen
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|raw
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\\'
condition|)
block|{
name|encoded
operator|.
name|append
argument_list|(
literal|"$."
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|c
operator|==
literal|'$'
condition|)
block|{
name|encoded
operator|.
name|append
argument_list|(
literal|"$$"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
name|encoded
operator|.
name|append
argument_list|(
literal|"$n"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|encoded
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|Character
operator|.
name|isLetter
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|encoded
operator|.
name|append
argument_list|(
name|rot13
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|i
operator|<
name|raw
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
name|char
name|nc
decl_stmt|;
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
name|i
init|;
operator|!
name|done
condition|;
name|n
operator|++
control|)
block|{
if|if
condition|(
name|n
operator|>=
name|rawlen
condition|)
block|{
break|break;
block|}
name|nc
operator|=
name|raw
operator|.
name|charAt
argument_list|(
name|n
argument_list|)
expr_stmt|;
if|if
condition|(
name|nc
operator|!=
name|c
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|count
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|count
operator|<
literal|3
condition|)
block|{
name|encoded
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|encoded
operator|.
name|append
argument_list|(
literal|"$"
argument_list|)
operator|.
name|append
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|count
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|i
operator|+=
name|count
operator|-
literal|1
expr_stmt|;
block|}
block|}
else|else
block|{
name|encoded
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|encoded
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|decode
parameter_list|(
name|String
name|encoded
parameter_list|)
block|{
name|StringBuffer
name|decoded
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|enlen
init|=
name|encoded
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|enlen
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|encoded
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'$'
condition|)
block|{
name|char
name|nc
init|=
name|encoded
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|nc
operator|==
literal|'$'
condition|)
block|{
name|decoded
operator|.
name|append
argument_list|(
literal|'$'
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
if|else if
condition|(
name|nc
operator|==
literal|'.'
condition|)
block|{
name|decoded
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
if|else if
condition|(
name|nc
operator|==
literal|'n'
condition|)
block|{
name|decoded
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
if|else if
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|nc
argument_list|)
condition|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|nn
init|=
name|i
operator|+
literal|1
decl_stmt|;
while|while
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|nc
argument_list|)
condition|)
block|{
name|count
operator|=
operator|(
name|count
operator|*
literal|10
operator|)
expr_stmt|;
name|count
operator|+=
operator|(
name|nc
operator|-
literal|'0'
operator|)
expr_stmt|;
name|nc
operator|=
name|encoded
operator|.
name|charAt
argument_list|(
operator|++
name|nn
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|d
init|=
literal|0
init|;
name|d
operator|<
name|count
condition|;
name|d
operator|++
control|)
block|{
name|decoded
operator|.
name|append
argument_list|(
name|nc
argument_list|)
expr_stmt|;
block|}
name|i
operator|=
name|nn
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|Character
operator|.
name|isLetter
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|decoded
operator|.
name|append
argument_list|(
name|rot13
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|decoded
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|decoded
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|char
name|rot13
parameter_list|(
name|char
name|c
parameter_list|)
block|{
if|if
condition|(
operator|(
name|c
operator|>=
literal|'a'
operator|)
operator|&&
operator|(
name|c
operator|<=
literal|'z'
operator|)
condition|)
block|{
name|char
name|dc
init|=
name|c
operator|+=
literal|13
decl_stmt|;
if|if
condition|(
name|dc
operator|>
literal|'z'
condition|)
block|{
name|dc
operator|-=
literal|26
expr_stmt|;
block|}
return|return
name|dc
return|;
block|}
if|else if
condition|(
operator|(
name|c
operator|>=
literal|'A'
operator|)
operator|&&
operator|(
name|c
operator|<=
literal|'Z'
operator|)
condition|)
block|{
name|char
name|dc
init|=
name|c
operator|+=
literal|13
decl_stmt|;
if|if
condition|(
name|dc
operator|>
literal|'Z'
condition|)
block|{
name|dc
operator|-=
literal|26
expr_stmt|;
block|}
return|return
name|dc
return|;
block|}
else|else
block|{
return|return
name|c
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|injectVersion
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|Pattern
name|pat
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"#{2,}"
argument_list|)
decl_stmt|;
name|Matcher
name|mat
init|=
name|pat
operator|.
name|matcher
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|StringBuffer
name|ret
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|off
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|mat
operator|.
name|find
argument_list|(
name|off
argument_list|)
condition|)
block|{
name|ret
operator|.
name|append
argument_list|(
name|text
operator|.
name|substring
argument_list|(
name|off
argument_list|,
name|mat
operator|.
name|start
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|repl
init|=
name|mat
operator|.
name|group
argument_list|()
decl_stmt|;
name|ret
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|center
argument_list|(
name|version
argument_list|,
name|repl
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|off
operator|=
name|mat
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
name|ret
operator|.
name|append
argument_list|(
name|text
operator|.
name|substring
argument_list|(
name|off
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ret
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|getBanner
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|String
name|encodedBanner
init|=
literal|"$26 $34_$n$15 /$._$7 /$34 $.$n$14 /`/@),$4 |  Ba"
operator|+
literal|" orunys bs nyy bs gur nycnpn'f  |$n$14 |  (~'  __| gbvyvat njnl ba "
operator|+
literal|"gur Ncnpur Nepuvin |$n$6 _,--.$3_/  |$4 $.$5  cebwrpg grnz, V jbhyq y"
operator|+
literal|"vxr gb$3 |$n$4 ,' ,$5 ($3 |$5 $.$5     jrypbzr lbh gb Nepuvin$6 |$"
operator|+
literal|"n$4 |  ($6 $.  /$6 |  $32#  |$n$5 $.  )$._/  ,_/$7 |$36 |$n$5 / /$3 "
operator|+
literal|"( |/$9 |     uggc://nepuvin.ncnpur.bet/     |$n$4 ( |$4 ( |$10 |     hf"
operator|+
literal|"ref@nepuvin.ncnpur.bet$7 |$n$5 $.|$5 $.|$11 $.$34_/$n$n"
decl_stmt|;
return|return
name|injectVersion
argument_list|(
name|decode
argument_list|(
name|encodedBanner
argument_list|)
argument_list|,
name|version
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|display
parameter_list|()
block|{
name|String
name|banner
init|=
name|getBanner
argument_list|(
name|ArchivaVersion
operator|.
name|determineVersion
argument_list|()
argument_list|)
decl_stmt|;
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Banner
operator|.
name|class
argument_list|)
operator|.
name|info
argument_list|(
name|StringUtils
operator|.
name|repeat
argument_list|(
literal|"_"
argument_list|,
literal|25
argument_list|)
operator|+
literal|"\n"
operator|+
name|banner
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

