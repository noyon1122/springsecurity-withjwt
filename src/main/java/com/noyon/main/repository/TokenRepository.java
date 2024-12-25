package com.noyon.main.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.noyon.main.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

	Optional<Token>findByToken(String token);
	
	@Query("""
			
			select t from Token t inner join User u on t.user.id=u.id  
                where t.user.id= :userid and t.logout=false
               """)
	
	List<Token>findAllTokensByUserid(int userid);
}
