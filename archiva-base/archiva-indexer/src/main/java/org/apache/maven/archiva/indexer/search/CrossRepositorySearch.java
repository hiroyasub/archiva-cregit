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
name|search
package|;
end_package

begin_comment
comment|/**  * Search across repositories for specified term.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  * @todo add security to not perform search in repositories you don't have access to.  */
end_comment

begin_interface
specifier|public
interface|interface
name|CrossRepositorySearch
block|{
comment|/**      * Search for the specific term across all repositories.      *       * @param term the term to search for.      * @return the results.      */
specifier|public
name|SearchResults
name|searchForTerm
parameter_list|(
name|String
name|term
parameter_list|)
function_decl|;
comment|/**      * Search for the specific MD5 string across all repositories.      *       * @param md5 the md5 string to search for.      * @return the results.      */
specifier|public
name|SearchResults
name|searchForMd5
parameter_list|(
name|String
name|md5
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

