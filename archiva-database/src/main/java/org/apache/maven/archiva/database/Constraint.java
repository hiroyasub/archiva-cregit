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
name|database
package|;
end_package

begin_comment
comment|/**  * Constraint   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|Constraint
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ASCENDING
init|=
literal|"ascending"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DESCENDING
init|=
literal|"descending"
decl_stmt|;
comment|/**      * Get the fetch limits on the object.      *       * @return the fetch limits on the object. (can be null) (O/RM specific)      */
specifier|public
name|String
name|getFetchLimits
parameter_list|()
function_decl|;
comment|/**      * Get the SELECT WHERE (condition) value for the constraint.      *       * @return the equivalent of the SELECT WHERE (condition) value for this constraint. (can be null)      */
specifier|public
name|String
name|getWhereCondition
parameter_list|()
function_decl|;
comment|/**      * Get the sort column name.      *       * @return the sort column name. (can be null)      */
specifier|public
name|String
name|getSortColumn
parameter_list|()
function_decl|;
comment|/**      * Get the sort direction name.      *       * @return the sort direction name. ("ASC" or "DESC") (only valid if {@link #getSortColumn()} is specified.)      */
specifier|public
name|String
name|getSortDirection
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

