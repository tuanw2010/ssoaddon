package com.bluecom.social.dao.impl;

import com.bluecom.model.UserConnectionModel;
import com.bluecom.social.dao.SsoConnectionDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * Created by tuan.vu on 2/9/17.
 */
public class DefaultSsoConnectionDao extends AbstractItemDao implements SsoConnectionDao {

    // Query
    private static final String QUERY_ALL_CONNECTIONS = "SELECT {" + UserConnectionModel.PK + "} FROM {" +
            UserConnectionModel._TYPECODE + "} WHERE {" + UserConnectionModel.USERID + "} = ?userId ORDER BY " +
            UserConnectionModel.PROVIDERID + ", " + UserConnectionModel.RANK;

    private static final String QUERY_CONNECTIONS = "SELECT {" + UserConnectionModel.PK + "} FROM {" +
            UserConnectionModel._TYPECODE + "} WHERE {" + UserConnectionModel.USERID + "} = ?userId AND {" +
            UserConnectionModel.PROVIDERID + "} = ?providerId ORDER BY " + UserConnectionModel.RANK;

    private static final String QUERY_CONNECTIONS_TO_USERS = "SELECT {" + UserConnectionModel.PK + "} FROM {" +
            UserConnectionModel._TYPECODE + "} WHERE {" + UserConnectionModel.USERID + "} = ?userId AND %s" +
            " ORDER BY " + UserConnectionModel.PROVIDERID + ", " + UserConnectionModel.RANK;

    private static final String QUERY_CONNECTIONS_BY_KEY = "SELECT {" + UserConnectionModel.PK + "} FROM {" +
            UserConnectionModel._TYPECODE + "} WHERE {" + UserConnectionModel.USERID + "} = ?userId AND {" +
            UserConnectionModel.PROVIDERID + "} = ?providerId AND {" + UserConnectionModel.PROVIDERUSERID + "} = ?providerUserId";

    private static final String QUERY_RANK = "SELECT COALESCE(MAX({" + UserConnectionModel.RANK + "}) + 1, 1) AS rank " +
            "FROM {" + UserConnectionModel._TYPECODE + "} WHERE {" + UserConnectionModel.USERID + "} = ?userId AND {" +
            UserConnectionModel.PROVIDERID + "} = ?providerId";

    private static final String QUERY_USER_IDS = "SELECT {" + UserConnectionModel.USERID + "} FROM {" +
            UserConnectionModel._TYPECODE + "} WHERE {" + UserConnectionModel.PROVIDERID + "} = ?providerId AND {" +
            UserConnectionModel.PROVIDERUSERID + "} = ?providerUserId";

    private static final String QUERY_USER_IDS_CONNECTED_TO = "SELECT {" + UserConnectionModel.USERID + "} FROM {" +
            UserConnectionModel._TYPECODE + "} WHERE {" + UserConnectionModel.PROVIDERID + "} = ?providerId AND {" +
            UserConnectionModel.PROVIDERUSERID + "} IN (?providerUserIds)";

    @Override
    public List<UserConnectionModel> findAllConnections(final String userId) {
        final Map<String, Object> queryParams = new HashMap();
        queryParams.put("userId", userId);
        final SearchResult<UserConnectionModel> result = getFlexibleSearchService().search(
                new FlexibleSearchQuery(QUERY_ALL_CONNECTIONS, queryParams));
        return result.getResult();
    }

    @Override
    public List<UserConnectionModel> findConnections(final String userId, final String providerId) {
        final Map<String, Object> queryParams = new HashMap();
        queryParams.put("userId", userId);
        queryParams.put("providerId", providerId);
        final SearchResult<UserConnectionModel> result = getFlexibleSearchService().search(
                new FlexibleSearchQuery(QUERY_CONNECTIONS, queryParams));
        return result.getResult();
    }

    @Override
    public List<UserConnectionModel> findConnectionsToUsers(final String userId, final MultiValueMap<String, String> providerUsers) {
        StringBuilder providerUsersCriteriaSql = new StringBuilder();
        final Map<String, Object> queryParams = new HashMap();
        queryParams.put("userId", userId);
        for (Iterator<Map.Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, List<String>> entry = it.next();
            String providerId = entry.getKey();
            providerUsersCriteriaSql.append("{" + UserConnectionModel.PROVIDERID + "} = ?providerId_")
                    .append(providerId).append(" AND {" + UserConnectionModel.PROVIDERUSERID + "} IN (?providerUserIds_")
                    .append(providerId).append(")");
            queryParams.put("providerId_" + providerId, providerId);
            queryParams.put("providerUserIds_" + providerId, entry.getValue());
            if (it.hasNext()) {
                providerUsersCriteriaSql.append(" OR " );
            }
        }
        final SearchResult<UserConnectionModel> result = getFlexibleSearchService().search(
                new FlexibleSearchQuery(String.format(QUERY_CONNECTIONS_TO_USERS, providerUsersCriteriaSql), queryParams));
        return result.getResult();
    }

    @Override
    public UserConnectionModel findConnectionsByKey(final String userId, final ConnectionKey connectionKey) {
        final Map<String, Object> queryParams = new HashMap();
        queryParams.put("userId", userId);
        queryParams.put("providerId", connectionKey.getProviderId());
        queryParams.put("providerUserId", connectionKey.getProviderUserId());
        final UserConnectionModel connection = getFlexibleSearchService().searchUnique(
                new FlexibleSearchQuery(QUERY_CONNECTIONS_BY_KEY, queryParams));
        return connection;
    }

    @Override
    public Integer findMaxRank(final String userId, final String providerId) {
        final Map<String, Object> queryParams = new HashMap();
        queryParams.put("userId", userId);
        queryParams.put("providerId", providerId);
        final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_RANK, queryParams);
        query.setResultClassList(Arrays.asList(Integer.class));
        final SearchResult<Integer> result = getFlexibleSearchService().search(query);
        final List<Integer> resultList = result.getResult();
        final Iterator<Integer> ite = resultList.iterator();

        return ite.hasNext() ? ite.next() : new Integer(1);
    }

    @Override
    public List<String> findUserIds(String providerId, String providerUserId) {
        final Map<String, Object> queryParams = new HashMap();
        queryParams.put("providerId", providerId);
        queryParams.put("providerUserId", providerUserId);
        final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_USER_IDS, queryParams);
        query.setResultClassList(Arrays.asList(String.class));
        final SearchResult<String> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }

    @Override
    public List<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        final Map<String, Object> queryParams = new HashMap();
        queryParams.put("providerId", providerId);
        queryParams.put("providerUserIds", providerUserIds);
        final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_USER_IDS_CONNECTED_TO, queryParams);
        query.setResultClassList(Arrays.asList(String.class));
        final SearchResult<String> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


}
