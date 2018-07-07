/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Feb 15, 2017 10:00:24 AM                    ---
 * ----------------------------------------------------------------
 */
package com.bluecom.jalo;

import com.bluecom.constants.SsoaddonConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.bluecom.jalo.UserConnection UserConnection}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedUserConnection extends GenericItem
{
	/** Qualifier of the <code>UserConnection.userId</code> attribute **/
	public static final String USERID = "userId";
	/** Qualifier of the <code>UserConnection.providerId</code> attribute **/
	public static final String PROVIDERID = "providerId";
	/** Qualifier of the <code>UserConnection.providerUserId</code> attribute **/
	public static final String PROVIDERUSERID = "providerUserId";
	/** Qualifier of the <code>UserConnection.rank</code> attribute **/
	public static final String RANK = "rank";
	/** Qualifier of the <code>UserConnection.displayName</code> attribute **/
	public static final String DISPLAYNAME = "displayName";
	/** Qualifier of the <code>UserConnection.profileUrl</code> attribute **/
	public static final String PROFILEURL = "profileUrl";
	/** Qualifier of the <code>UserConnection.imageUrl</code> attribute **/
	public static final String IMAGEURL = "imageUrl";
	/** Qualifier of the <code>UserConnection.accessToken</code> attribute **/
	public static final String ACCESSTOKEN = "accessToken";
	/** Qualifier of the <code>UserConnection.secret</code> attribute **/
	public static final String SECRET = "secret";
	/** Qualifier of the <code>UserConnection.refreshToken</code> attribute **/
	public static final String REFRESHTOKEN = "refreshToken";
	/** Qualifier of the <code>UserConnection.expireTime</code> attribute **/
	public static final String EXPIRETIME = "expireTime";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(USERID, AttributeMode.INITIAL);
		tmp.put(PROVIDERID, AttributeMode.INITIAL);
		tmp.put(PROVIDERUSERID, AttributeMode.INITIAL);
		tmp.put(RANK, AttributeMode.INITIAL);
		tmp.put(DISPLAYNAME, AttributeMode.INITIAL);
		tmp.put(PROFILEURL, AttributeMode.INITIAL);
		tmp.put(IMAGEURL, AttributeMode.INITIAL);
		tmp.put(ACCESSTOKEN, AttributeMode.INITIAL);
		tmp.put(SECRET, AttributeMode.INITIAL);
		tmp.put(REFRESHTOKEN, AttributeMode.INITIAL);
		tmp.put(EXPIRETIME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.accessToken</code> attribute.
	 * @return the accessToken
	 */
	public String getAccessToken(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ACCESSTOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.accessToken</code> attribute.
	 * @return the accessToken
	 */
	public String getAccessToken()
	{
		return getAccessToken( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.accessToken</code> attribute. 
	 * @param value the accessToken
	 */
	public void setAccessToken(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ACCESSTOKEN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.accessToken</code> attribute. 
	 * @param value the accessToken
	 */
	public void setAccessToken(final String value)
	{
		setAccessToken( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.displayName</code> attribute.
	 * @return the displayName
	 */
	public String getDisplayName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DISPLAYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.displayName</code> attribute.
	 * @return the displayName
	 */
	public String getDisplayName()
	{
		return getDisplayName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.displayName</code> attribute. 
	 * @param value the displayName
	 */
	public void setDisplayName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DISPLAYNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.displayName</code> attribute. 
	 * @param value the displayName
	 */
	public void setDisplayName(final String value)
	{
		setDisplayName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.expireTime</code> attribute.
	 * @return the expireTime
	 */
	public Long getExpireTime(final SessionContext ctx)
	{
		return (Long)getProperty( ctx, EXPIRETIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.expireTime</code> attribute.
	 * @return the expireTime
	 */
	public Long getExpireTime()
	{
		return getExpireTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.expireTime</code> attribute. 
	 * @return the expireTime
	 */
	public long getExpireTimeAsPrimitive(final SessionContext ctx)
	{
		Long value = getExpireTime( ctx );
		return value != null ? value.longValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.expireTime</code> attribute. 
	 * @return the expireTime
	 */
	public long getExpireTimeAsPrimitive()
	{
		return getExpireTimeAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.expireTime</code> attribute. 
	 * @param value the expireTime
	 */
	public void setExpireTime(final SessionContext ctx, final Long value)
	{
		setProperty(ctx, EXPIRETIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.expireTime</code> attribute. 
	 * @param value the expireTime
	 */
	public void setExpireTime(final Long value)
	{
		setExpireTime( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.expireTime</code> attribute. 
	 * @param value the expireTime
	 */
	public void setExpireTime(final SessionContext ctx, final long value)
	{
		setExpireTime( ctx,Long.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.expireTime</code> attribute. 
	 * @param value the expireTime
	 */
	public void setExpireTime(final long value)
	{
		setExpireTime( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.imageUrl</code> attribute.
	 * @return the imageUrl
	 */
	public String getImageUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IMAGEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.imageUrl</code> attribute.
	 * @return the imageUrl
	 */
	public String getImageUrl()
	{
		return getImageUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.imageUrl</code> attribute. 
	 * @param value the imageUrl
	 */
	public void setImageUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IMAGEURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.imageUrl</code> attribute. 
	 * @param value the imageUrl
	 */
	public void setImageUrl(final String value)
	{
		setImageUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.profileUrl</code> attribute.
	 * @return the profileUrl
	 */
	public String getProfileUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROFILEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.profileUrl</code> attribute.
	 * @return the profileUrl
	 */
	public String getProfileUrl()
	{
		return getProfileUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.profileUrl</code> attribute. 
	 * @param value the profileUrl
	 */
	public void setProfileUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROFILEURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.profileUrl</code> attribute. 
	 * @param value the profileUrl
	 */
	public void setProfileUrl(final String value)
	{
		setProfileUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.providerId</code> attribute.
	 * @return the providerId - Provider identification
	 */
	public String getProviderId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROVIDERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.providerId</code> attribute.
	 * @return the providerId - Provider identification
	 */
	public String getProviderId()
	{
		return getProviderId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.providerId</code> attribute. 
	 * @param value the providerId - Provider identification
	 */
	public void setProviderId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROVIDERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.providerId</code> attribute. 
	 * @param value the providerId - Provider identification
	 */
	public void setProviderId(final String value)
	{
		setProviderId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.providerUserId</code> attribute.
	 * @return the providerUserId - User of provider identification
	 */
	public String getProviderUserId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROVIDERUSERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.providerUserId</code> attribute.
	 * @return the providerUserId - User of provider identification
	 */
	public String getProviderUserId()
	{
		return getProviderUserId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.providerUserId</code> attribute. 
	 * @param value the providerUserId - User of provider identification
	 */
	public void setProviderUserId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROVIDERUSERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.providerUserId</code> attribute. 
	 * @param value the providerUserId - User of provider identification
	 */
	public void setProviderUserId(final String value)
	{
		setProviderUserId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.rank</code> attribute.
	 * @return the rank
	 */
	public Integer getRank(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, RANK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.rank</code> attribute.
	 * @return the rank
	 */
	public Integer getRank()
	{
		return getRank( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.rank</code> attribute. 
	 * @return the rank
	 */
	public int getRankAsPrimitive(final SessionContext ctx)
	{
		Integer value = getRank( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.rank</code> attribute. 
	 * @return the rank
	 */
	public int getRankAsPrimitive()
	{
		return getRankAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.rank</code> attribute. 
	 * @param value the rank
	 */
	public void setRank(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, RANK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.rank</code> attribute. 
	 * @param value the rank
	 */
	public void setRank(final Integer value)
	{
		setRank( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.rank</code> attribute. 
	 * @param value the rank
	 */
	public void setRank(final SessionContext ctx, final int value)
	{
		setRank( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.rank</code> attribute. 
	 * @param value the rank
	 */
	public void setRank(final int value)
	{
		setRank( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.refreshToken</code> attribute.
	 * @return the refreshToken
	 */
	public String getRefreshToken(final SessionContext ctx)
	{
		return (String)getProperty( ctx, REFRESHTOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.refreshToken</code> attribute.
	 * @return the refreshToken
	 */
	public String getRefreshToken()
	{
		return getRefreshToken( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.refreshToken</code> attribute. 
	 * @param value the refreshToken
	 */
	public void setRefreshToken(final SessionContext ctx, final String value)
	{
		setProperty(ctx, REFRESHTOKEN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.refreshToken</code> attribute. 
	 * @param value the refreshToken
	 */
	public void setRefreshToken(final String value)
	{
		setRefreshToken( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.secret</code> attribute.
	 * @return the secret
	 */
	public String getSecret(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SECRET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.secret</code> attribute.
	 * @return the secret
	 */
	public String getSecret()
	{
		return getSecret( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.secret</code> attribute. 
	 * @param value the secret
	 */
	public void setSecret(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SECRET,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.secret</code> attribute. 
	 * @param value the secret
	 */
	public void setSecret(final String value)
	{
		setSecret( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.userId</code> attribute.
	 * @return the userId - Provider identification
	 */
	public String getUserId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, USERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserConnection.userId</code> attribute.
	 * @return the userId - Provider identification
	 */
	public String getUserId()
	{
		return getUserId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.userId</code> attribute. 
	 * @param value the userId - Provider identification
	 */
	public void setUserId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, USERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UserConnection.userId</code> attribute. 
	 * @param value the userId - Provider identification
	 */
	public void setUserId(final String value)
	{
		setUserId( getSession().getSessionContext(), value );
	}
	
}
