package com.realestate.repository;

import com.realestate.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for User entity with custom queries
 * Provides CRUD operations and custom query methods for user management
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Derived query methods using method naming conventions
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    List<User> findByRole(User.UserRole role);
    
    List<User> findByIsEnabledTrue();
    
    List<User> findByIsEnabledFalse();
    
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    long countByRole(User.UserRole role);
    
    long countByIsEnabled(boolean isEnabled);

    // Custom JPQL queries
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findUsersByCreatedDateRange(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT u FROM User u WHERE u.lastLogin IS NULL OR u.lastLogin < :cutoffDate")
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Query("SELECT u FROM User u WHERE u.loginAttempts >= :maxAttempts")
    List<User> findUsersWithExcessiveLoginAttempts(@Param("maxAttempts") Integer maxAttempts);

    @Query("SELECT u FROM User u WHERE SIZE(u.properties) > :propertyCount")
    List<User> findUsersWithMoreThanNProperties(@Param("propertyCount") int propertyCount);

    @Query("SELECT u FROM User u JOIN u.properties p WHERE p.status = :status GROUP BY u HAVING COUNT(p) > 0")
    List<User> findUsersByPropertyStatus(@Param("status") com.realestate.model.entity.Property.PropertyStatus status);

    @Query("SELECT u FROM User u WHERE u.phoneNumber IS NOT NULL")
    List<User> findUsersWithPhoneNumber();

    @Query("SELECT u FROM User u WHERE u.address IS NOT NULL AND u.address != ''")
    List<User> findUsersWithAddress();

    @Query(value = "SELECT COUNT(*) FROM users u WHERE u.role = :role AND u.created_at >= :date", 
           nativeQuery = true)
    long countNewUsersByRoleSince(@Param("role") String role, @Param("date") LocalDateTime date);

    // Custom query with pagination
    @Query("SELECT u FROM User u WHERE u.role IN :roles ORDER BY u.createdAt DESC")
    Page<User> findUsersByRolesWithPagination(@Param("roles") List<User.UserRole> roles, 
                                             Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.properties WHERE u.id = :userId")
    Optional<User> findUserWithProperties(@Param("userId") Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.calculations WHERE u.id = :userId")
    Optional<User> findUserWithCalculations(@Param("userId") Long userId);

    // Native SQL queries for complex operations
    @Query(value = "SELECT u.* FROM users u WHERE u.id IN " +
                   "(SELECT DISTINCT ic.user_id FROM investment_calculations ic " +
                   "WHERE ic.roi_percentage > :minRoi)", 
           nativeQuery = true)
    List<User> findUsersWithHighROICalculations(@Param("minRoi") double minRoi);

    @Query(value = "SELECT u.*, " +
                   "COUNT(p.id) as property_count, " +
                   "AVG(p.price) as avg_property_price " +
                   "FROM users u " +
                   "LEFT JOIN properties p ON u.id = p.owner_id " +
                   "GROUP BY u.id " +
                   "HAVING COUNT(p.id) > 0 " +
                   "ORDER BY property_count DESC",
           nativeQuery = true)
    List<Object[]> getUsersWithPropertyStatistics();

    // Update queries
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :loginTime WHERE u.username = :username")
    int updateLastLogin(@Param("username") String username, @Param("loginTime") LocalDateTime loginTime);

    @Modifying
    @Query("UPDATE User u SET u.loginAttempts = :attempts WHERE u.username = :username")
    int updateLoginAttempts(@Param("username") String username, @Param("attempts") Integer attempts);

    @Modifying
    @Query("UPDATE User u SET u.isEnabled = :enabled WHERE u.id = :userId")
    int updateUserStatus(@Param("userId") Long userId, @Param("enabled") boolean enabled);

    @Modifying
    @Query("UPDATE User u SET u.profilePictureUrl = :url WHERE u.id = :userId")
    int updateProfilePicture(@Param("userId") Long userId, @Param("url") String url);

    // Soft delete
    @Modifying
    @Query("UPDATE User u SET u.isDeleted = true WHERE u.id = :userId")
    int softDeleteUser(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false")
    List<User> findAllActiveUsers();

    // Search with multiple criteria
    @Query("SELECT u FROM User u WHERE " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:enabled IS NULL OR u.isEnabled = :enabled) AND " +
           "(:searchTerm IS NULL OR " +
           " LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           " LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           " LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           " LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> searchUsers(@Param("role") User.UserRole role,
                          @Param("enabled") Boolean enabled,
                          @Param("searchTerm") String searchTerm,
                          Pageable pageable);

    // Performance optimized query with hints
    @Query(value = "SELECT /*+ INDEX(users, idx_users_role) */ * FROM users WHERE role = :role",
           nativeQuery = true)
    List<User> findUsersByRoleOptimized(@Param("role") String role);
}