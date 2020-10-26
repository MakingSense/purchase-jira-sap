package com.makingsense.sap.purchase.services;

/**
 * Interface to use when migrating an entity to a well known SAP entity.
 *
 * @param <T>   The entity that SAP returns.
 * @param <U>   The entity to migrate
 */
public interface MigrateToSAP<T,U> {

    /**
     * Migrates a U entity to a T entity known by SAP.
     *
     * @param toMigrate the entity to migrate.
     * @return          the migrated entity response.
     */
    T migrate(U toMigrate);
}
