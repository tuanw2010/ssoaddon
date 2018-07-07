package com.bluecom.social.connect;

import com.bluecom.social.dao.SsoConnectionDao;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tuan.vu on 2/9/17.
 */
public class SsoUsersConnectionRepository implements UsersConnectionRepository {

    private ModelService modelService;
    private TextEncryptor textEncryptor;
    private SsoConnectionDao connectionDao;
    private ConnectionFactoryLocator connectionFactoryLocator;
    private ConnectionSignUp connectionSignUp;

    public SsoUsersConnectionRepository(ModelService modelService, TextEncryptor textEncryptor,
                                        SsoConnectionDao connectionDao, ConnectionFactoryLocator connectionFactoryLocator) {
        this.modelService = modelService;
        this.textEncryptor = textEncryptor;
        this.connectionDao = connectionDao;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        List<String> localUserIds = connectionDao.findUserIds(key.getProviderId(), key.getProviderUserId());
        if (localUserIds.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null)
            {
                createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }
        return localUserIds;
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        final Set<String> localUserIds = new HashSet<>();
        List<String> userIdsConnectedTo = connectionDao.findUserIdsConnectedTo(providerId, providerUserIds);
        for (final String userId : userIdsConnectedTo) {
            localUserIds.add(userId);
        }

        return localUserIds;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new SsoConnectionRepository(userId, connectionFactoryLocator, textEncryptor, connectionDao, modelService);
    }

    @Override
    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }


}
