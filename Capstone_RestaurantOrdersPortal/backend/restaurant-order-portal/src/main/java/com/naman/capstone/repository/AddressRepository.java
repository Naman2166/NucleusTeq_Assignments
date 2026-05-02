package com.naman.capstone.repository;

import com.naman.capstone.entity.Address;
import com.naman.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Address entity operations
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * find all addresses of a user
     * @param user user whose address need to fetch
     * @return list of addresses
     */
    List<Address> findByUser(User user);

    /**
     * find address by id and user
     * @param id address id
     * @param user user object
     * @return address of user
     */
    Optional<Address> findByIdAndUser(Long id, User user);
}