package com.bluecom.social.connect;

import com.bluecom.model.UserConnectionModel;
import com.bluecom.social.dao.SsoConnectionDao;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;


import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * Created by tuan.vu on 2/9/17.
 */
public class SsoConnectionRepository implements ConnectionRepository {

    private String userId;
    private ModelService modelService;
    private TextEncryptor textEncryptor;
    private SsoConnectionDao connectionDao;
    private ConnectionFactoryLocator connectionFactoryLocator;

    public SsoConnectionRepository(String userId, ConnectionFactoryLocator connectionFactoryLocator,
                                   TextEncryptor textEncryptor, SsoConnectionDao connectionDao, ModelService modelService) {
        this.userId = userId;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
        this.connectionDao = connectionDao;
        this.modelService = modelService;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        List<Connection<?>> resultList = mapConnections(connectionDao.findAllConnections(this.userId));
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
        }
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            if (connections.get(providerId).size() == 0) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, connection);
        }
        return connections;
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {
        return mapConnections(connectionDao.findConnections(this.userId, providerId));
    }

    @Override
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers == null || providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }
        List<Connection<?>> resultList = mapConnections(connectionDao.findConnectionsToUsers(this.userId, providerUsers));
        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            List<String> userIds = providerUsers.get(providerId);
            List<Connection<?>> connections = connectionsForUsers.get(providerId);
            if (connections == null) {
                connections = new ArrayList<Connection<?>>(userIds.size());
                for (int i = 0; i < userIds.size(); i++) {
                    connections.add(null);
                }
                connectionsForUsers.put(providerId, connections);
            }
            String providerUserId = connection.getKey().getProviderUserId();
            int connectionIndex = userIds.indexOf(providerUserId);
            connections.set(connectionIndex, connection);
        }
        return connectionsForUsers;
    }

    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        try {
            return mapConnection(connectionDao.findConnectionsByKey(this.userId, connectionKey));
        } catch (FlexibleSearchException e) {
            throw new NoSuchConnectionException(connectionKey);
        }
    }

    @Override
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
    }

    @Override
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }

    @Override
    public void addConnection(Connection<?> connection) {
        try {
            final ConnectionData data = connection.createData();
            Integer rank = connectionDao.findMaxRank(this.userId, data.getProviderId());

            final UserConnectionModel userConnectionModel = modelService.create(UserConnectionModel.class);
            userConnectionModel.setUserId(this.userId);
            userConnectionModel.setProviderId(data.getProviderId());
            userConnectionModel.setProviderUserId(data.getProviderUserId());
            userConnectionModel.setRank(rank);
            userConnectionModel.setDisplayName(data.getDisplayName());
            userConnectionModel.setProfileUrl(data.getProfileUrl());
            userConnectionModel.setImageUrl(data.getImageUrl());
            userConnectionModel.setAccessToken(encrypt(data.getAccessToken()));
            userConnectionModel.setSecret(encrypt(data.getSecret()));
            userConnectionModel.setRefreshToken(encrypt(data.getRefreshToken()));
            userConnectionModel.setExpireTime(data.getExpireTime());

            modelService.save(userConnectionModel);
        } catch (ModelSavingException e) {
            throw new DuplicateConnectionException(connection.getKey());
        }
    }

    @Override
    public void updateConnection(Connection<?> connection) {
        final ConnectionData data = connection.createData();
        final ConnectionKey connectionKey = connection.getKey();
        final UserConnectionModel userConnectionModel = connectionDao.findConnectionsByKey(this.userId, connectionKey);
        userConnectionModel.setDisplayName(data.getDisplayName());
        userConnectionModel.setProfileUrl(data.getProfileUrl());
        userConnectionModel.setImageUrl(data.getImageUrl());
        userConnectionModel.setAccessToken(encrypt(data.getAccessToken()));
        userConnectionModel.setSecret(encrypt(data.getSecret()));
        userConnectionModel.setRefreshToken(encrypt(data.getRefreshToken()));
        userConnectionModel.setExpireTime(data.getExpireTime());

        modelService.save(userConnectionModel);
    }

    @Override
    public void removeConnections(String providerId) {
        final List<UserConnectionModel> userConnectionModels = connectionDao.findConnections(this.userId, providerId);
        modelService.removeAll(userConnectionModels);
    }

    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        final UserConnectionModel userConnectionModel = connectionDao.findConnectionsByKey(this.userId, connectionKey);
        modelService.remove(userConnectionModel);
    }

    private ConnectionData mapConnectionData(final UserConnectionModel userConnectionModel) {
        return new ConnectionData(
                userConnectionModel.getProviderId(),
                userConnectionModel.getProviderUserId(),
                userConnectionModel.getDisplayName(),
                userConnectionModel.getProfileUrl(),
                userConnectionModel.getImageUrl(),
                decrypt(userConnectionModel.getAccessToken()),
                decrypt(userConnectionModel.getSecret()),
                decrypt(userConnectionModel.getRefreshToken()),
                userConnectionModel.getExpireTime());
    }

    private Connection<?> mapConnection(final UserConnectionModel userConnectionModel) {
        ConnectionData connectionData = mapConnectionData(userConnectionModel);
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
        return connectionFactory.createConnection(connectionData);
    }

    private List<Connection<?>> mapConnections(final List<UserConnectionModel> userConnectionModels) {
        final List<Connection<?>> connections = new ArrayList<>();
        for (UserConnectionModel userConnectionModel : userConnectionModels) {
            connections.add(mapConnection(userConnectionModel));
        }
        return connections;
    }

    private Connection<?> findPrimaryConnection(String providerId) {
        List<Connection<?>> connections = mapConnections(connectionDao.findConnections(this.userId, providerId));
        if (connections.size() > 0) {
            return connections.get(0);
        } else {
            return null;
        }
    }

    private <A> String getProviderId(Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }

    private String encrypt(String text) {
        return text != null ? textEncryptor.encrypt(text) : text;
    }

    private String decrypt(String encryptedText) {
        return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
    }

}
