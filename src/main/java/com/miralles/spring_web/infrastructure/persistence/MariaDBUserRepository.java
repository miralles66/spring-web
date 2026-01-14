package com.miralles.spring_web.infrastructure.persistence;

import com.miralles.spring_web.domain.models.User;
import com.miralles.spring_web.domain.repositories.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MariaDB implementation of UserRepository.
 * This is an alternative infrastructure implementation that could connect to a MariaDB database.
 * Currently uses in-memory storage for demonstration purposes.
 * 
 * To use this implementation, you would:
 * 1. Add MariaDB dependencies to build.gradle.kts
 * 2. Configure MariaDB connection in application.properties
 * 3. Activate the "mariadb" profile: --spring.profiles.active=mariadb
 */
@Repository
@Profile("mariadb")
public class MariaDBUserRepository implements UserRepository {

    // In-memory storage for demonstration
    // In a real MariaDB implementation, this would be replaced with JPA/Hibernate entities
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        
        // In a real MariaDB implementation:
        // EntityManager.persist(userEntity);
        // return userEntity.toDomain();
        
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        // In a real MariaDB implementation:
        // return entityManager.find(UserEntity.class, id).map(UserEntity::toDomain);
        
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        // In a real MariaDB implementation:
        // return entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class)
        //         .getResultList()
        //         .stream()
        //         .map(UserEntity::toDomain)
        //         .collect(Collectors.toList());
        
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {
        // In a real MariaDB implementation:
        // UserEntity entity = entityManager.find(UserEntity.class, id);
        // if (entity != null) {
        //     entityManager.remove(entity);
        // }
        
        users.remove(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // In a real MariaDB implementation:
        // return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class)
        //         .setParameter("email", email)
        //         .getResultStream()
        //         .findFirst()
        //         .map(UserEntity::toDomain);
        
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    /**
     * Example of what a MariaDB entity might look like (commented out since we're using domain model directly)
     * 
     * @Entity
     * @Table(name = "users")
     * public class UserEntity {
     *     @Id
     *     @GeneratedValue(strategy = GenerationType.IDENTITY)
     *     private Long id;
     * 
     *     @Column(nullable = false, unique = true, length = 50)
     *     private String username;
     * 
     *     @Column(nullable = false, unique = true, length = 100)
     *     private String email;
     * 
     *     // Constructors, getters, setters
     * 
     *     public User toDomain() {
     *         return new User(id, username, email);
     *     }
     * 
     *     public static UserEntity fromDomain(User user) {
     *         UserEntity entity = new UserEntity();
     *         entity.setId(user.getId());
     *         entity.setUsername(user.getUsername());
     *         entity.setEmail(user.getEmail());
     *         return entity;
     *     }
     * }
     */
}