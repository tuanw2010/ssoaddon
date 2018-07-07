package com.bluecom.social.dao;

import com.bluecom.model.UserConnectionModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

/**
 * Created by tuan.vu on 2/9/17.
 */
public interface SsoConnectionDao extends Dao {

    List<UserConnectionModel> findAllConnections(final String userId);

    List<UserConnectionModel> findConnections(final String userId, final String providerId);

    List<UserConnectionModel> findConnectionsToUsers(final String userId, final MultiValueMap<String, String> providerUsers);

    UserConnectionModel findConnectionsByKey(final String userId, final ConnectionKey connectionKey);

    Integer findMaxRank(final String userId, final String providerId);

    List<String> findUserIds(final String providerId, final String providerUserId);

    List<String> findUserIdsConnectedTo(final String providerId, final Set<String> providerUserIds);

}
